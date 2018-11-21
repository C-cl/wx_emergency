package com.wxsoft.emergency.data.remote.log;


import android.util.Log;

public class Logger implements LogInterceptor.Logger {

    @Override
    public void log(String message) {
        Log.i("emergency http", message);
    }
}
