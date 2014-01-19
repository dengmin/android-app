package com.dengmin.app;

import android.app.Application;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

public class SystemApplication extends Application {

	private static final String TAG = "SystemApplication";

    @Override
    public void onCreate() {    	     
    	 Log.d(TAG, "onCreate");
         super.onCreate();
         JPushInterface.setDebugMode(true); 	//设置开启日志,发布时请关闭日志
         JPushInterface.init(this);     		// 初始化 JPush
    }
}
