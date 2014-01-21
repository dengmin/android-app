package com.dengmin.app.ui;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.dengmin.app.AppException;
import com.dengmin.app.R;
import com.dengmin.app.SystemApplication;
import com.dengmin.app.fragment.FragmentInfo;
import com.dengmin.app.fragment.FragmentMain;
import com.dengmin.app.fragment.FragmentMore;
import com.dengmin.app.fragment.FragmentNews;
import com.dengmin.app.http.APIClient;
import com.dengmin.app.model.AppUpgrade;
import com.dengmin.app.service.NetworkStateService;
import com.dengmin.app.service.UpgradeService;

public class MainActivity extends FragmentActivity{
	
	private long touchTime = 0;
	//定义FragmentTabHost对象
	private FragmentTabHost mTabHost;
	
	//定义一个布局
	private LayoutInflater layoutInflater;
		
	//定义数组来存放Fragment界面
	private Class<?> fragmentArray[] = {FragmentMain.class,FragmentNews.class,FragmentInfo.class,FragmentMore.class};
	
	//定义数组来存放按钮图片
	private int mImageViewArray[] = {R.drawable.tab_home_btn,R.drawable.tab_message_btn,R.drawable.tab_selfinfo_btn,
									 R.drawable.tab_square_btn,R.drawable.tab_more_btn};
	
	//Tab选项卡的文字
	private String mTextviewArray[] = {"首页", "消息", "好友", "更多"};
	
	private Intent networkStateIntent;
	
	private Intent upgradeIntent = null;
	
	private SystemApplication app;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        app = (SystemApplication)getApplication();
        
        initView();
        
        //网络状态监测服务
  		networkStateIntent =new Intent(this,NetworkStateService.class);
  		startService(networkStateIntent);
  		
  		checkVersion();
    }
	 
	/**
	 * 初始化组件
	 */
	private void initView(){
		//实例化布局对象
		layoutInflater = LayoutInflater.from(this);
				
		//实例化TabHost对象，得到TabHost
		mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);	
		
		//得到fragment的个数
		int count = fragmentArray.length;	
				
		for(int i = 0; i < count; i++){	
			//为每一个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
			//将Tab按钮添加进Tab选项卡中
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
			//设置Tab按钮的背景
			mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
		}
	}
				
	/**
	 * 给Tab按钮设置图标和文字
	 */
	private View getTabItemView(int index){
		View view = layoutInflater.inflate(R.layout.tab_item_view, null);
	
		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		imageView.setImageResource(mImageViewArray[index]);
		
		TextView textView = (TextView) view.findViewById(R.id.textview);		
		textView.setText(mTextviewArray[index]);
	
		return view;
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
		final int curVersionCode = app.getCurrentVersion();
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
