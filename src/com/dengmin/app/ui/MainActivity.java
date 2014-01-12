package com.dengmin.app.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.dengmin.app.Action;
import com.dengmin.app.NetworkStateService;
import com.dengmin.app.R;

public class MainActivity extends FragmentActivity {

    private long touchTime = 0;    
    
	private Fragment[] mFragments;
	private RadioGroup main_tab;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	
	private Handler networkStateHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case -1:
				Toast.makeText(MainActivity.this, "当前网络连接不可用,请检查你的网络设置.", Toast.LENGTH_LONG).show(); 
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		mFragments = new Fragment[4];
		fragmentManager = getSupportFragmentManager();
		mFragments[0] = fragmentManager.findFragmentById(R.id.fragement_main);
		mFragments[1] = fragmentManager.findFragmentById(R.id.fragement_news);
		mFragments[2] = fragmentManager.findFragmentById(R.id.fragement_info);
		mFragments[3] = fragmentManager.findFragmentById(R.id.fragement_more);
		fragmentTransaction = fragmentManager.beginTransaction().hide(mFragments[0])
			.hide(mFragments[1]).hide(mFragments[2]).hide(mFragments[3]);
		fragmentTransaction.show(mFragments[0]).commit();
		setFragmentIndicator();
		
		registerReceiver(new NetworkStateReceiver(), new IntentFilter(Action.NETWORK_STATE));
		
		Intent networkService =new Intent(MainActivity.this,NetworkStateService.class);
		startService(networkService);
	}

	private void setFragmentIndicator() {
		main_tab = (RadioGroup) findViewById(R.id.main_tab);
		main_tab.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				fragmentTransaction = fragmentManager.beginTransaction()
						.hide(mFragments[0]).hide(mFragments[1]).
						hide(mFragments[2]).hide(mFragments[3]);
				switch (checkedId) {
					case R.id.tab_home:
						fragmentTransaction.show(mFragments[0]).commit();
						break;
					case R.id.tab_news:
						fragmentTransaction.show(mFragments[1]).commit();
						break;
					case R.id.tab_info:
						fragmentTransaction.show(mFragments[2]).commit();
						break;
					case R.id.tab_more:
						fragmentTransaction.show(mFragments[3]).commit();
						break;
					default:
						break;
				}
			}
			
		});
	}

    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
			long currentTime = System.currentTimeMillis();
			if((currentTime-touchTime) >= 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				touchTime = currentTime;
			}else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

    //用来接受从service中监测到的网络状态
    class NetworkStateReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			boolean state = intent.getBooleanExtra("state", false);
			if(state){
				networkStateHandler.sendEmptyMessage(1);
			}else{
				networkStateHandler.sendEmptyMessage(-1);
			}
			
		}
    }
    
}
