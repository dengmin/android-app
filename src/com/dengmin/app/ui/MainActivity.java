package com.dengmin.app.ui;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
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

import com.dengmin.app.AppException;
import com.dengmin.app.R;
import com.dengmin.app.http.APIClient;
import com.dengmin.app.model.AppUpgrade;
import com.dengmin.app.service.NetworkStateService;
import com.dengmin.app.service.UpgradeService;

public class MainActivity extends FragmentActivity {

    private long touchTime = 0;    
    
	private Fragment[] mFragments;
	private RadioGroup main_tab;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	
	private int curVersionCode;
	
	private Intent networkStateIntent;
	
	private Intent upgradeIntent = null;
	
	
	
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
		
		//网络状态监测服务
		networkStateIntent =new Intent(MainActivity.this,NetworkStateService.class);
		startService(networkStateIntent);
		
		checkVersion();
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
				ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
				manager.killBackgroundProcesses(getPackageName());
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

    
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	stopService(networkStateIntent);
    	if(upgradeIntent != null){
    		stopService(upgradeIntent);
    	}
    }
    
	private void checkVersion(){
		getCurrentVersion();
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					AppUpgrade update = (AppUpgrade) msg.obj;
					if (null != update) {
						if (curVersionCode < update.getVersion_code()) {
							showAlert();
						}
					}
				}
			}
		};
		
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					//获取软件更新信息
					AppUpgrade update = APIClient.checkVersion();
					msg.obj = update;
					msg.what = 1;
					handler.sendMessage(msg);
				} catch (AppException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	/**
	 * 获取当前客户端版本信息
	 */
	private void getCurrentVersion() {
		try {
			PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
			curVersionCode = info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
	}
	
	private void showAlert(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("软件升级");
        alert.setMessage("发现新版本,建议立即更新使用.");
        alert.setPositiveButton("立即更新", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                upgradeIntent = new Intent(MainActivity.this, UpgradeService.class);
                startService(upgradeIntent);
            }
        });
        alert.setNegativeButton("以后再说", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.create().show();
	}
    
}
