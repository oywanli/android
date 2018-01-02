package com.example.finger_android_demo;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.dspread.xpos.DspFingerPrint;
import com.dspread.xpos.DspFingerPrint.CommunicationMode;
import com.dspread.xpos.DspFingerPrint.DspFingerPrintListener;
import com.dspread.xpos.DspFingerPrint.LcdModeAlign;
import com.dspread.xpos.DspFingerPrint.Error;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.usb.UsbDevice;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends AppCompatActivity implements OnClickListener{

	private TextView text;
	private Button connectBtn,sendBtn,cancelBtn,bluConnect;
	private UsbDevice usbDevice;
	private USBClass usb;
	private String moduleNumber,serialNumber;
	private MyPosListener listener;
	private DspFingerPrint dspPos;
	private ListView m_ListView;
	private MyListViewAdapter m_Adapter = null;
	private ImageView imvAnimScan;
	private AnimationDrawable animScan;
	private List<BluetoothDevice> lstDevScanned;
	private boolean isUart = false;
	private String blueTootchAddress = "";
	private boolean flag=false;
	private final int  REQUEST_PERMISSION_ACCESS_LOCATION=0;
	
	private void onBTPosSelected(Activity activity, View itemView, int index) {
		dspPos.stopScanQPos2Mode();
//			 打开蓝牙连接
//		 open(CommunicationMode.BLUETOOTH);
		Map<String, ?> dev = (Map<String, ?>) m_Adapter.getItem(index);
		blueTootchAddress = (String) dev.get("ADDRESS");
		sendMsg(1001);
	}

	protected List<Map<String, ?>> generateAdapterData() {
		if(dspPos!=null){
			lstDevScanned=dspPos.getDeviceList();
		}
		List<Map<String, ?>> data = new ArrayList<Map<String, ?>>();
		
		if (isUart) {
			//
			Map<String, Object> itmSerialPort = new HashMap<String, Object>();
			itmSerialPort.put("ICON", Integer.valueOf(R.drawable.serialport));
			itmSerialPort.put("TITLE",
					getResources().getString(R.string.serialport));
			itmSerialPort.put("ADDRESS",
					getResources().getString(R.string.serialport));

			data.add(itmSerialPort);
			//
		}

		for (BluetoothDevice dev : lstDevScanned) {
			Map<String, Object> itm = new HashMap<String, Object>();
			L.i("==================设备信息===="+dev.getName()+"::::"+dev.getAddress());
			itm.put("ICON",
					dev.getBondState() == BluetoothDevice.BOND_BONDED ? Integer
							.valueOf(R.drawable.bluetooth_blue) : Integer
							.valueOf(R.drawable.bluetooth_blue_unbond));
			itm.put("TITLE", dev.getName() + "(" + dev.getAddress() + ")");
			itm.put("ADDRESS", dev.getAddress());
			//
			data.add(itm);
		}
		//
		return data;
	}

	private void refreshAdapter() {
		if (m_Adapter != null) {
			m_Adapter.clearData();
			m_Adapter = null;
		}
		//
		List<Map<String, ?>> data = generateAdapterData();
		m_Adapter = new MyListViewAdapter(MainActivity.this, data);
		//
		m_ListView.setAdapter(m_Adapter);
	}

	private class MyListViewAdapter extends BaseAdapter {
		private List<Map<String, ?>> m_DataMap;
		private LayoutInflater m_Inflater;

		public void clearData() {
			m_DataMap.clear();
			m_DataMap = null;
		}

		public MyListViewAdapter(Context context, List<Map<String, ?>> map) {
			this.m_DataMap = map;
			this.m_Inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return m_DataMap.size();
		}

		@Override
		public Object getItem(int position) {
			return m_DataMap.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = m_Inflater.inflate(R.layout.bt_qpos_item, null);
			}
			ImageView m_Icon = (ImageView) convertView
					.findViewById(R.id.item_iv_icon);
			TextView m_TitleName = (TextView) convertView
					.findViewById(R.id.item_tv_lable);
			//
			Map<String, ?> itemdata = (Map<String, ?>) m_DataMap.get(position);
			int idIcon = (Integer) itemdata.get("ICON");
			String sTitleName = (String) itemdata.get("TITLE");
			//
			m_Icon.setBackgroundResource(idIcon);
			m_TitleName.setText(sTitleName);
			//
			return convertView;
		}

	}
	
	private POS_TYPE posType = POS_TYPE.BLUETOOTH;

	private static enum POS_TYPE {
		BLUETOOTH,OTG
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_morpho);
		requestPermission();//先请求权限
		m_ListView = (ListView) findViewById(R.id.lv_indicator_BTPOS);
		m_ListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onBTPosSelected(MainActivity.this, view, position);
				m_ListView.setVisibility(View.GONE);
				animScan.stop();
				imvAnimScan.setVisibility(View.GONE);
			}

		});
		sdkVersion = DspFingerPrint.getSDKVersion();
		imvAnimScan = (ImageView) findViewById(R.id.img_anim_scanbt);
		animScan = (AnimationDrawable) getResources().getDrawable(R.anim.progressanmi);
		imvAnimScan.setBackgroundDrawable(animScan);
		text=(TextView) findViewById(R.id.txt);
		text.setText("sdkVersion:"+sdkVersion);
		connectBtn=(Button) findViewById(R.id.connect_btn);
		sendBtn=(Button) findViewById(R.id.send_btn);
		cancelBtn=(Button) findViewById(R.id.cancel_btn);//
		cancelBtn.setEnabled(false);
		sendBtn.setEnabled(false);
		bluConnect=(Button) findViewById(R.id.bluconnect_btn);
		connectBtn.setOnClickListener(this);
		sendBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		bluConnect.setOnClickListener(this);
	}
	
	//按钮的点击事件
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.connect_btn:
			usb = new USBClass();
		    ArrayList<String> deviceList = usb.GetUSBDevices(getBaseContext());
		    if (deviceList == null) {
		    	Toast.makeText(MainActivity.this, "权限没有设置", Toast.LENGTH_SHORT).show();
				return;
			}
		    final CharSequence[] items = deviceList.toArray(new CharSequence[deviceList.size()]);

		    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		    builder.setTitle("Select a Reader");
		    builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int item) {
		            String selectedDevice = (String) items[item];
		            dialog.dismiss();

		            usbDevice = USBClass.getMdevices().get(selectedDevice);
		            open(CommunicationMode.USB_OTG_CDC_ACM);
		            posType=POS_TYPE.OTG;
					dspPos.openUsb(usbDevice);
		        }
		    });
		    AlertDialog alert = builder.create();
		    alert.show();
			break;
		case R.id.send_btn://发送信息，采集指纹
			flag=true;
//			Toast.makeText(MainActivity.this, "begin enroll", Toast.LENGTH_SHORT).show();
			AlertDialog.Builder builder2=new AlertDialog.Builder(MainActivity.this);
			builder2.setTitle("please input amount");
			EditText editText=new EditText(MainActivity.this);
			builder2.setView(editText);
			builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//开始采集
					String str="Pls press the \nfingerprint";
					byte[] paras;
					try {
						paras = str.getBytes("GBK");
						dspPos.lcdShowCustomDisplay(LcdModeAlign.LCD_MODE_ALIGNCENTER, QPOSUtil.byteArray2Hex(paras), 10);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			});
			builder2.setNegativeButton("取消", null);
			builder2.setCancelable(false);
			builder2.show();
			break;
		case R.id.cancel_btn:
			if(posType==POS_TYPE.BLUETOOTH){
				dspPos.disconnectBT();//断开连接
			}else if(posType == POS_TYPE.OTG){
				dspPos.closeUsb();
			}
			break;
		case R.id.bluconnect_btn://蓝牙扫描开始
			open(CommunicationMode.BLUETOOTH);
			posType=POS_TYPE.BLUETOOTH;
			dspPos.clearBluetoothBuffer();
			dspPos.scanQPos2Mode(MainActivity.this, 15);//扫描蓝牙
			m_ListView.setVisibility(View.VISIBLE);
			animScan.start();
			imvAnimScan.setVisibility(View.VISIBLE);
			break;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater=getMenuInflater();
		inflater.inflate(R.menu.activity_morpho, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(dspPos==null){
			return false;
		}
		else if(item.getItemId() == R.id.lcd_customer_sreen){//自定义屏幕显示
			String str="presst";
			byte[] paras;
			try {
				paras = str.getBytes("GBK");
				dspPos.lcdShowCustomDisplay(LcdModeAlign.LCD_MODE_ALIGNCENTER, QPOSUtil.byteArray2Hex(paras), 10);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}else if(item.getItemId() == R.id.menu_get_deivce_info){
			Toast.makeText(MainActivity.this, "get the device info", Toast.LENGTH_SHORT).show();
			text.setText("getting pos info。。。");
			sendMsg(2002);
		}else if(item.getItemId()==R.id.menu_get_qposinfo){
			L.d("begin info");
			sendMsg(2003);
		}else if(item.getItemId()==R.id.menu_get_moduleNumber){
			text.setText("get module info...");
			sendMsg(2004);
		}else if(item.getItemId()==R.id.menu_get_serialNumbers){
			text.setText("get serial info...");
			sendMsg(2005);
		}
		return true;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(dspPos!=null){
			if(posType==POS_TYPE.BLUETOOTH){
				dspPos.disconnectBT();
			}else if(posType == POS_TYPE.OTG){
				dspPos.closeUsb();
			}
			dspPos=null;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(dspPos!=null){
			if(posType==POS_TYPE.BLUETOOTH){
				dspPos.disconnectBT();
			}else if(posType == POS_TYPE.OTG){
				dspPos.closeUsb();
			}
		}
	}
	
	private void requestPermission() {
	    if (Build.VERSION.SDK_INT >= 23) {
	        int checkAccessFinePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
//	        int checkAccessCOARSEPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
	        if (checkAccessFinePermission != PackageManager.PERMISSION_GRANTED) {
	            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
	                    REQUEST_PERMISSION_ACCESS_LOCATION);
	            L.d( "没有权限，请求权限");
	            Toast.makeText(MainActivity.this, "没有权限，请求权限!",Toast.LENGTH_LONG).show();
	            return;
	        }
	        Toast.makeText(MainActivity.this, "已有定位权限!",Toast.LENGTH_LONG).show();
	        L.d( "已有定位权限");
	    }
	   //做下面该做的事
	}
	
	@SuppressLint("NewApi")
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
	    switch (requestCode) {
	        case REQUEST_PERMISSION_ACCESS_LOCATION: {
	            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
	                Toast.makeText(MainActivity.this, "开启权限permission granted!",Toast.LENGTH_LONG).show();
	            } else {
	                L.d("没有定位权限，请先开启!");
	            }
	        }
	    }
	    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}
	
	private void open(CommunicationMode mode) {
		listener = new MyPosListener();
		dspPos = DspFingerPrint.getInstance(mode);
		if (dspPos == null) {
			L.d("dspPos"+dspPos);
			return;
		}
		L.d("dspPos-------------1> "+dspPos);
		dspPos.setConext(getApplicationContext());
		Handler handler = new Handler(Looper.myLooper());
		dspPos.initListener(handler, listener);
		sdkVersion=DspFingerPrint.getSDKVersion();
		L.d("dspPos-------------2> "+dspPos);
	}
	
	private void sendMsg(int what) {
		Message msg = new Message();
		msg.what = what;
		mHandler.sendMessage(msg);
	}

	private boolean selectBTFlag = false;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1001:
				selectBTFlag = true;
				text.setText("begin connect...");
				sendMsg(1002);
				break;
			case 1002:
				L.i(">>>connect blueTootchAddress: " + blueTootchAddress);
				dspPos.stopScanQPos2Mode();
				dspPos.connectBluetoothDevice(true, 25, blueTootchAddress);
				// doTradeButton.setEnabled(true);
				selectBTFlag = false;
				break;
			case 2001://采集指纹指令
				dspPos.FingerTrasmission("212a0000140000010100AA380100003D0600003E02002C004404001200000034040043000000A5010001A6010000AE010000AF010000",10);
				break;
			case 2002://获取指纹信息
				dspPos.getFingerInfo(5);
				break;
			case 2003:
			/*	new Thread(new Runnable() {
					
					@Override
					public void run() {
						String s=dspPos.FingerPenetrate("211000000A0000010100013801006EA5010001", 10);
						L.i("result:"+s);
					}
				}).start();
				*/
				dspPos.getQposInfo();
				break;
			case 2004:
				moduleNumber=dspPos.getModuleNumber();
				text.setText(moduleNumber);
				break;
			case 2005:
				serialNumber=dspPos.getSerialNumber();
				text.setText(serialNumber);
				break;
			}
		}
	};
	private String sdkVersion;
	
	class MyPosListener implements DspFingerPrintListener{

		@Override
		public void onRequestQposConnected() {
			L.i("device connected");
			text.setText("device connected");
			cancelBtn.setEnabled(true);
			sendBtn.setEnabled(true);
		}

		@Override
		public void onRequestQposDisconnected() {
			L.i("device disconnected");
			text.setText("device disconnected");
			cancelBtn.setEnabled(false);
			sendBtn.setEnabled(false);
		}

		@Override
		public void onRequestNoQposDetected() {
			text.setText("pos no response!");
		}

		@Override
		public void onRequestDeviceScanFinished() {
			L.i("扫描结束");
			Toast.makeText(MainActivity.this, "扫描结束", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onDeviceFound(BluetoothDevice device) {
			if(device!=null){
				String address=device.getAddress();
				text.setText("发现设备"+address+"\n");
				refreshAdapter();
				L.d("发现设备"+address+"\n");
			}else {
				text.setText("没有发现设备");
				L.d("没有发现设备");
			}
		}

		@Override
		public void onLcdShowCustomDisplay(boolean isSuccess) {
			if(isSuccess){
				L.e("CustomDisplay success");
				if(flag){
					sendMsg(2001);
				}
				if(!flag){
					sendBtn.setEnabled(true);
				}
			}else{
				text.setText("CustomDisplay failed");
			}
		}

		@Override
		public void onError(Error arg0) {
			// TODO Auto-generated method stub
			if (arg0 == Error.CMD_NOT_AVAILABLE) {
				text.setText(getString(R.string.command_not_available));
			} else if (arg0 == Error.TIMEOUT) {
				text.setText(getString(R.string.device_no_response));
			} else if (arg0 == Error.DEVICE_RESET) {
				text.setText(getString(R.string.device_reset));
			} else if (arg0 == Error.UNKNOWN) {
				text.setText(getString(R.string.unknown_error));
			} else if (arg0 == Error.DEVICE_BUSY) {
				text.setText(getString(R.string.device_busy));
			} else if (arg0 == Error.INPUT_OUT_OF_RANGE) {
				text.setText(getString(R.string.out_of_range));
			} else if (arg0 == Error.INPUT_INVALID_FORMAT) {
				text.setText(getString(R.string.invalid_format));
			} else if (arg0 == Error.INPUT_ZERO_VALUES) {
				text.setText(getString(R.string.zero_values));
			} else if (arg0 == Error.INPUT_INVALID) {
				text.setText(getString(R.string.input_invalid));
			} else if (arg0 == Error.CASHBACK_NOT_SUPPORTED) {
				text.setText(getString(R.string.cashback_not_supported));
			} else if (arg0 == Error.CRC_ERROR) {
				text.setText(getString(R.string.crc_error));
			} else if (arg0 == Error.COMM_ERROR) {
				text.setText(getString(R.string.comm_error));
			} else if (arg0 == Error.MAC_ERROR) {
				text.setText(getString(R.string.mac_error));
			} else if (arg0 == Error.CMD_TIMEOUT) {
				text.setText(getString(R.string.cmd_timeout));
			}else if (arg0 == Error.AMOUNT_OUT_OF_LIMIT) {
				text.setText(getString(R.string.amount_out_of_limit));
			}else if(arg0 == Error.ICC_ONLINE_TIMEOUT){
				text.setText("send have timeout!!");
			}else {
				text.setText("errorState："+arg0);
			}
		}

		@Override
		public void onFingerErollPrintTranmis(String arg0) {
			// TODO Auto-generated method stub
			if(arg0!=null){
				text.setText("enroll the finger:"+arg0);
			}else{
				text.setText("enroll finger failed!");
			}
			String str="";
			byte[] paras;
			try {
				paras = str.getBytes("GBK");
				dspPos.lcdShowCustomDisplay(LcdModeAlign.LCD_MODE_ALIGNCENTER, QPOSUtil.byteArray2Hex(paras), 10);
				flag=false;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFingerPrintTranmis(Hashtable<String, String> arg0) {
			// TODO Auto-generated method stub
			if(arg0!=null){
				String productId=arg0.get("productPro");
				String sensorId=arg0.get("sensorPro");
				String softwareId=arg0.get("softwarePro");
				String content=productId+"\n"+sensorId+"\n"+softwareId;
//				String ModuleNumber=arg0.get("sensorPro");
				text.setText(content);
			}else{
				text.setText("the device info is null");
			}
		}

		@Override
		public void onRequestSetAmount() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onFingerPenetrate(String arg0) {
			if(arg0!=null){
				text.setText("data: "+arg0);
			}else{
				text.setText("null !");
			}
		}

		@Override
		public void onQposInfoResult(Hashtable<String, String> arg0) {
			String isSupportedTrack1 = arg0.get("isSupportedTrack1") == null ? ""
					: arg0.get("isSupportedTrack1");
			String isSupportedTrack2 = arg0.get("isSupportedTrack2") == null ? ""
					: arg0.get("isSupportedTrack2");
			String isSupportedTrack3 = arg0.get("isSupportedTrack3") == null ? ""
					: arg0.get("isSupportedTrack3");
			String bootloaderVersion = arg0.get("bootloaderVersion") == null ? ""
					: arg0.get("bootloaderVersion");
			String firmwareVersion = arg0.get("firmwareVersion") == null ? ""
					: arg0.get("firmwareVersion");
			String isUsbConnected = arg0.get("isUsbConnected") == null ? ""
					: arg0.get("isUsbConnected");
			String isCharging = arg0.get("isCharging") == null ? ""
					: arg0.get("isCharging");
			String batteryLevel = arg0.get("batteryLevel") == null ? ""
					: arg0.get("batteryLevel");
			String batteryPercentage = arg0.get("batteryPercentage") == null ? ""
					: arg0.get("batteryPercentage");
			String hardwareVersion = arg0.get("hardwareVersion") == null ? ""
					: arg0.get("hardwareVersion");
//			String sub=arg0.get("SUB")==null?"":arg0.get("SUB");
			String content = "";
			content += getString(R.string.bootloader_version)
					+ bootloaderVersion + "\n";
			content += getString(R.string.firmware_version) + firmwareVersion
					+ "\n";
			content += getString(R.string.usb) + isUsbConnected + "\n";
			content += getString(R.string.charge) + isCharging + "\n";
//			if (batteryPercentage==null || "".equals(batteryPercentage)) {
				content += getString(R.string.battery_level) + batteryLevel + "\n";
//			}else {
				content += getString(R.string.battery_percentage)  + batteryPercentage + "\n";
//			}
			
			content += getString(R.string.hardware_version) + hardwareVersion
					+ "\n";
//			content+="SUB"+sub+ "\n";
			content += getString(R.string.track_1_supported)
					+ isSupportedTrack1 + "\n";
			content += getString(R.string.track_2_supported)
					+ isSupportedTrack2 + "\n";
			content += getString(R.string.track_3_supported)
					+ isSupportedTrack3 + "\n";

			text.setText(content);
		}

	}
}
