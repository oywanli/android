package com.dspread.demoui;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class WelcomeActivity extends BaseActivity implements OnClickListener{
	private Button audio,serial_port,normal_blu,other_blu;
	private Intent intent;
	private static final int LOCATION_CODE = 101;
	private LocationManager lm;//【位置管理】

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		setTitle(getString(R.string.title_welcome));
		audio=(Button) findViewById(R.id.audio);
		serial_port=(Button) findViewById(R.id.serial_port);
		normal_blu=(Button) findViewById(R.id.normal_bluetooth);
		other_blu=(Button) findViewById(R.id.other_bluetooth);
		other_blu.setEnabled(false);
		audio.setOnClickListener(this);
		serial_port.setOnClickListener(this);
		normal_blu.setOnClickListener(this);
		other_blu.setOnClickListener(this);
		bluetoothRelaPer();
	}

	@Override
	public void onToolbarLinstener() {

	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_welcome;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.audio://音频
				intent = new Intent(this,OtherActivity.class);
				intent.putExtra("connect_type", 1);
				startActivity(intent);
				break;
			case R.id.serial_port://串口连接
				intent = new Intent(this,OtherActivity.class);

				intent.putExtra("connect_type", 2);
				startActivity(intent);
				break;
			case R.id.normal_bluetooth://普通蓝牙连接
				intent = new Intent(this,MainActivity.class);

				intent.putExtra("connect_type", 3);
				startActivity(intent);
				break;
			case R.id.other_bluetooth://其他蓝牙连接，例如：BLE，，，
				intent = new Intent(this,MainActivity.class);
				intent.putExtra("connect_type", 4);
				startActivity(intent);
				break;
		}
	}

	public void bluetoothRelaPer() {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter != null && !adapter.isEnabled()) {//表示蓝牙不可用 add one fix
			Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivity(enabler);
		}
		lm = (LocationManager) WelcomeActivity.this.getSystemService(WelcomeActivity.this.LOCATION_SERVICE);
		boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (ok) {//开了定位服务
			if (ContextCompat.checkSelfPermission(WelcomeActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
					!= PackageManager.PERMISSION_GRANTED) {
				Log.e("POS_SDK", "没有权限");
				// 没有权限，申请权限。
				// 申请授权。
				ActivityCompat.requestPermissions(WelcomeActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
//                        Toast.makeText(getActivity(), "没有权限", Toast.LENGTH_SHORT).show();
			} else {
				// 有权限了，去放肆吧。
				Toast.makeText(WelcomeActivity.this, "有权限", Toast.LENGTH_SHORT).show();
			}
		} else {
			Log.e("BRG", "系统检测到未开启GPS定位服务");
			Toast.makeText(WelcomeActivity.this, "系统检测到未开启GPS定位服务", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent();
			intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivityForResult(intent, 1315);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case LOCATION_CODE: {
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// 权限被用户同意。
					Toast.makeText(WelcomeActivity.this, getString(R.string.msg_allowed_location_permission), Toast.LENGTH_LONG).show();
				} else {
					// 权限被用户拒绝了。
					Toast.makeText(WelcomeActivity.this, getString(R.string.msg_not_allowed_loaction_permission), Toast.LENGTH_LONG).show();
				}
			}
			break;
		}
	}



}
