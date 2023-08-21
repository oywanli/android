package com.example.despreaddemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

public class SharedPreferencesUtil {

    private static final String FILE_NAME="data";

    private static SharedPreferencesUtil mInstance;

    private SharedPreferencesUtil(){

    }

    public static SharedPreferencesUtil getmInstance(){
        if (mInstance == null){
            synchronized (SharedPreferencesUtil.class){
                if (mInstance == null){
                    mInstance =new SharedPreferencesUtil();
                }
            }
        }
        return mInstance;
    }
    /**
     *存入相应的key对应的数据
     *value可以是String、boolean、float、int、long等基本数据类型的值
     */
    public void put(Context context,String key,Object value){

        SharedPreferences sharedPreferences=context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        String type=value.getClass().getSimpleName();

        if ("Integer".equals(type)){
            editor.putInt(key,(Integer) value);
        }else if("Boolean".equals(type)){
            editor.putBoolean(key,(Boolean)value);
        }else if("String".equals(type)){
            editor.putString(key,(String) value);
        }else if ("Float".equals(type)){
            editor.putFloat(key,(Float) value);
        }else if ("Long".equals(type)){
            editor.putLong(key,(Long) value);
        }
        editor.apply();
    }

    /**
     *获取SharedPreferences数据里指定key对应的value。如果key不存在，则返回默认值defValue
     */
    @Nullable
    public Object get(Context context, String key, Object defValue){
        SharedPreferences sharedPreferences=context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        String type=defValue.getClass().getSimpleName();

        if ("Integer".equals(type)){
            return sharedPreferences.getInt(key,(Integer) defValue);
        }else if("Boolean".equals(type)){
            return sharedPreferences.getBoolean(key,(Boolean)defValue);
        }else if("String".equals(type)){
            return sharedPreferences.getString(key,(String) defValue);
        }else if ("Float".equals(type)){
            return sharedPreferences.getFloat(key,(Float) defValue);
        }else if ("Long".equals(type)){
            return sharedPreferences.getLong(key,(Long) defValue);
        }
        return null;
    }
}
