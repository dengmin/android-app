package com.dengmin.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.dengmin.app.R;

public class SplashActivity extends Activity {

	private boolean isFirst;
	private SharedPreferences prefer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.splash, null);
		this.setContentView(view);
		
		AlphaAnimation aa = new AlphaAnimation(0.3f,1.0f);
		aa.setDuration(2000);
		view.startAnimation(aa);
		
		prefer = getSharedPreferences("app", MODE_MULTI_PROCESS);
		isFirst = prefer.getBoolean("first_use", true);
		aa.setAnimationListener(new AnimationListener()
		{
			@Override
			public void onAnimationEnd(Animation arg0) {
				start();
			}
			@Override
			public void onAnimationRepeat(Animation animation) {}
			@Override
			public void onAnimationStart(Animation animation) {}

		});
		
	}

	private void start(){
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = null;
				if(isFirst){
					intent = new Intent(SplashActivity.this,GuideActivity.class);
					Editor editor = prefer.edit();
					editor.putBoolean("first_use", false);
					editor.commit();
				}else{
					intent = new Intent(SplashActivity.this,MainActivity.class);
				}
				startActivity(intent);
				//结束本Activity
				SplashActivity.this.finish();
			}
		}, 1000);
	}
	
}
