package com.example.finger_android_demo;

import android.util.Log;

public class L {
	private static boolean debug=true;
	private static String TAG="finger_print";
	public static void i(String msg){
		if(debug){
			Log.i(TAG, msg);
		}
	}
	
	public static void e(String msg){
		if(debug){
			Log.e(TAG, msg);
		}
	}
	
	public static void v(String msg){
		if(debug){
			Log.v(TAG, msg);
		}
	}
	
	public static void w(String msg){
		if(debug){
			Log.w(TAG, msg);
		}
	}
	
	public static void d(String msg){
		if(debug){
			Log.d(TAG, msg);
		}
	}
}
