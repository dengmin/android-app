package com.dengmin.app.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.dengmin.app.Action;
import com.dengmin.app.R;
import com.dengmin.app.ui.RollViewPager;
import com.dengmin.app.ui.RollViewPager.OnPagerClickCallback;

public class FragmentMain extends Fragment {

	private int[] images = { R.drawable.a, R.drawable.b, R.drawable.c,R.drawable.e };
	private String[] titles = new String[] { "标题1", "标题2", "标题3", "标题4","标题5" };
	private List<View> dots = new ArrayList<View>();
	private TextView title;
	private LinearLayout mViewPagerLay;
	private LinearLayout dotLayout; //小圆点的布局
	
	private NetworkStateReceiver networkStateReceiver;
	
	//用来接受从service中监测到的网络状态
    private class NetworkStateReceiver extends BroadcastReceiver {
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
    
    private Handler networkStateHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case -1:
				Toast.makeText(getActivity(), "当前网络连接不可用,请检查你的网络设置.", Toast.LENGTH_LONG).show(); 
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.fragment_main, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		title = (TextView) getView().findViewById(R.id.title);
		mViewPagerLay = (LinearLayout) getView().findViewById(R.id.viewpager);
		dotLayout = (LinearLayout) getView().findViewById(R.id.dot_layout);
		
		networkStateReceiver = new NetworkStateReceiver();
		getActivity().registerReceiver(networkStateReceiver, new IntentFilter(Action.NETWORK_STATE));

		//只有一张图片就不需要添加小圆点了
		if(images.length >  1){
			for(int i = 0; i< images.length; i++){
				//新建一个imageView
				ImageView dotView = new ImageView(getActivity());
				LayoutParams layoutParams = new LayoutParams(5, 5);
				layoutParams.setMargins(2, 0, 2, 0);
				dotView.setLayoutParams(layoutParams);
				if(i == 0){
					dotView.setBackgroundResource(R.drawable.point_select);
				}else{
					dotView.setBackgroundResource(R.drawable.point_normal);
				}
				dotLayout.addView(dotView);
				dots.add(dotView);
			}
		}
		
		RollViewPager mViewPager = new RollViewPager(getActivity(), dots,new OnPagerClickCallback() {
				@Override
				public void onPagerClick(int position) {
					Toast.makeText(getActivity(),"第" + position + "张图片被点击了",Toast.LENGTH_LONG).show();
				}
			}
		);
		
		mViewPager.setResImageIds(images);
		mViewPager.setTitle(title, titles);
		mViewPager.startRoll();
		mViewPagerLay.addView(mViewPager);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(networkStateReceiver);
	}
}