package com.dengmin.app.service;

import com.dengmin.app.Action;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

public class NetworkStateService extends Service {

	private static final String tag="app"; 
	private ConnectivityManager connectivityManager;
	private NetworkInfo info;
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				Log.d(tag, "网络状态已经改变");
				connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
				info = connectivityManager.getActiveNetworkInfo();
				Intent networkIntent = new Intent();
				networkIntent.setAction(Action.NETWORK_STATE);
				if(info != null && info.isAvailable()) {
					String name = info.getTypeName();
					Log.d(tag, "当前网络名称：" + name);
					networkIntent.putExtra("state", true);
				}else{
					Log.d(tag, "没有可用网络");
					networkIntent.putExtra("state", false);
				}
				sendBroadcast(networkIntent);
			}
		}
	};
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, mFilter);
	}
	
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}
	
	 public int onStartCommand(Intent intent, int flags, int startId) {
		 return super.onStartCommand(intent, flags, startId);
	 }
}
