package com.dspread.demoui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.dspread.demoui.R;
import com.printer.sdk.PrinterConstants;
import com.printer.sdk.PrinterInstance;

import java.io.File;

public class WelcomeActivity extends Activity implements OnClickListener{

	private Button audio,serial_port,normal_blu,other_blu,printerBtn;
	private Intent intent;
	public static PrinterInstance myPrinter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		intent = new Intent(this,MainActivity.class);
		audio=(Button) findViewById(R.id.audio);
		serial_port=(Button) findViewById(R.id.serial_port);
		normal_blu=(Button) findViewById(R.id.normal_bluetooth);
		other_blu=(Button) findViewById(R.id.other_bluetooth);
		printerBtn=(Button) findViewById(R.id.test_printer);
		other_blu.setEnabled(false);
		audio.setOnClickListener(this);
		serial_port.setOnClickListener(this);
		normal_blu.setOnClickListener(this);
		other_blu.setOnClickListener(this);
		printerBtn.setOnClickListener(this);
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
				String devicesName = "Serial device";
				String devicesAddress = "/dev/ttyMT0";
				String com_baudrate = "115200";

				int baudrate = Integer.parseInt(com_baudrate);

				myPrinter = PrinterInstance.getPrinterInstance(new File(devicesAddress), baudrate, 0, mHandler);
				TRACE.d("myPrinter.getCurrentStatus()-" + myPrinter.getCurrentStatus());
				boolean b = myPrinter.openConnection();
				TRACE.d("-----------" + b);
				if (b && myPrinter != null && myPrinter.getCurrentStatus() == 0){
					TRACE.i("open");
				}
				else{
					Toast.makeText(getApplicationContext(), "Connection to printer failed.", Toast.LENGTH_SHORT).show();
				}
		}
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			TRACE.d("@@@@@@@@@@@@" + msg.what);
			switch (msg.what) {
				case PrinterConstants.Connect.SUCCESS:
					TRACE.d("isConnected status:::;" + true);
					//printData();
					//printSampleData(myPrinter);
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
