package com.dspread.demoui;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.dspread.demoui.R;
import com.printer.sdk.PrinterConstants;
import com.printer.sdk.PrinterInstance;

public class WelcomeActivity extends Activity implements OnClickListener{

	private Button audio,serial_port,normal_blu,other_blu,test_printer;
	private Intent intent;
	private PrinterInstance myPrinter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		intent = new Intent(this,MainActivity.class);
		audio=(Button) findViewById(R.id.audio);
		serial_port=(Button) findViewById(R.id.serial_port);
		normal_blu=(Button) findViewById(R.id.normal_bluetooth);
		other_blu=(Button) findViewById(R.id.other_bluetooth);
		test_printer=(Button) findViewById(R.id.test_printer);
		other_blu.setEnabled(false);
		audio.setOnClickListener(this);
		serial_port.setOnClickListener(this);
		normal_blu.setOnClickListener(this);
		other_blu.setOnClickListener(this);
		test_printer.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.audio://音频
				intent.putExtra("connect_type", 1);
				startActivity(intent);
				break;
			case R.id.serial_port://串口连接
				final PowerManager mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
				int state1 = mPowerManager.switchUartPath(1, PowerManager.SWITCH_UART_1_PATH);
				Log.i("POS_SDK", "mpost_switch==>" + state1);
				int open1 = mPowerManager.devicePowerOnoffs(1,
						PowerManager.DEVICE_1_POWER_ON_OR_OFF);
				Log.i("POS_SDK", "power_on==>" + open1);
				intent.putExtra("connect_type", 2);
				startActivity(intent);
				break;
			case R.id.normal_bluetooth://普通蓝牙连接
				intent.putExtra("connect_type", 3);
				startActivity(intent);
				break;
			case R.id.other_bluetooth://其他蓝牙连接，例如：BLE，，，
				intent.putExtra("connect_type", 4);
				startActivity(intent);
				break;
			case R.id.test_printer:
				final PowerManager mPowerManager2 = (PowerManager) getSystemService(Context.POWER_SERVICE);
				
				int state2 = mPowerManager2.switchUartPath(0,
						PowerManager.SWITCH_UART_1_PATH);
				Log.i("POS_SDK", "printer_switch==>" + state2);
				int openPrinter = mPowerManager2.devicePowerOnoffs(1,
						PowerManager.DEVICE_3_POWER_ON_OR_OFF);
				int open2 = mPowerManager2.devicePowerOnoffs(1,
						PowerManager.DEVICE_4_POWER_ON_OR_OFF);
				Log.i("POS_SDK", "open2==>" + open2);
				Log.i("POS_SDK", "open1==>" + openPrinter);
				 try {
					    Thread.sleep(200);
					   } catch (InterruptedException e) {
					    // TODO Auto-generated catch block
					    e.printStackTrace();
					   }
					   
					   int baudrate = 115200;
					   String path = "/dev/ttyMT0";
					   Log.i("POS_SDK", "baudrate:" + baudrate);
					   myPrinter = PrinterInstance.getPrinterInstance(new File(path),
					     baudrate, 0, mHandler);
					   myPrinter.openConnection();
				break;
		}
	}
	
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			TRACE.d("@@@@@@@@@@@@" + msg.what);
			switch (msg.what) {
				case PrinterConstants.Connect.SUCCESS:
					TRACE.d("isConnected status:::;" + true);
					//printData();
					//printSampleData(myPrinter);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					int i = myPrinter.getCurrentStatus();
					if (i == 0) {
						TRACE.d("打印机状态正常;");
						myPrinter.printText("connect success!!\n\n");
					} else if (i == -1) {
						TRACE.d("接收数据失败;");
					}
					myPrinter.closeConnection();
					break;
				case PrinterConstants.Connect.FAILED:

					break;
				case PrinterConstants.Connect.CLOSED:
					Toast.makeText(getApplicationContext(), "Connection closed", Toast.LENGTH_SHORT).show();
					break;
				case PrinterConstants.Connect.NODEVICE:
					Toast.makeText(getApplicationContext(), "No connection", Toast.LENGTH_SHORT).show();
					break;
				case 0:
					Toast.makeText(getApplicationContext(), "0", Toast.LENGTH_SHORT).show();
					break;
				case -1:
					Toast.makeText(getApplicationContext(), "-1", Toast.LENGTH_SHORT).show();
					break;
				case -2:
					Toast.makeText(getApplicationContext(), "-2", Toast.LENGTH_SHORT).show();
					break;
				case -3:
					Toast.makeText(getApplicationContext(), "-3", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
			}
		}
	};
	
	
	
}
