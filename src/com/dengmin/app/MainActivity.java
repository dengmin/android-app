package com.dengmin.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends FragmentActivity {

	private Fragment[] mFragments;
	private RadioGroup main_tab;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	
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

}
