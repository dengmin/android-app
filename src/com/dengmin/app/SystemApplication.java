package com.dengmin.app;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.dengmin.app.util.ImageCacheUtil;

public class SystemApplication extends Application {

	private static final String TAG = "SystemApplication";

    @Override
    public void onCreate() {    	     
    	 Log.d(TAG, "onCreate");
         super.onCreate();
         ImageCacheUtil.init(this);
    }
    
    public int getCurrentVersion() {
		try {
			PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		return -1;
	}
}
