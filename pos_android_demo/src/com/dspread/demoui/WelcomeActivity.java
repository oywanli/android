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

public class WelcomeActivity extends Activity implements OnClickListener{

	private Button audio,serial_port,normal_blu,other_blu;
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		intent = new Intent(this,MainActivity.class);
		audio=(Button) findViewById(R.id.audio);
		serial_port=(Button) findViewById(R.id.serial_port);
		normal_blu=(Button) findViewById(R.id.normal_bluetooth);
		other_blu=(Button) findViewById(R.id.other_bluetooth);
		other_blu.setEnabled(false);
		audio.setOnClickListener(this);
		serial_port.setOnClickListener(this);
		normal_blu.setOnClickListener(this);
		other_blu.setOnClickListener(this);
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
			
		}
	}
	
	
	
	
}
