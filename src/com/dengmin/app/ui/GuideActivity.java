package com.dengmin.app.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dengmin.app.R;
import com.dengmin.app.adapter.ViewPagerAdapter;

public class GuideActivity extends Activity implements OnClickListener, OnPageChangeListener{
	
	private ViewPager viewPager;
	
	private ViewPagerAdapter vAdapter;
	private ArrayList<View> views;
	
	private Button btn_start;
	
	//引导图片资源
    private static final int[] pics = {R.drawable.guide_01,R.drawable.guide_02, R.drawable.guide_03};
    
    //底部小点的图片
    private ImageView[] points;
    
    //记录当前选中位置
    private int currentIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide);
		
		initView();
		
		initData();
	}
	
	private void initView(){
		// 实例化ViewPager
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		views = new ArrayList<View>();
		// 实例化ViewPager适配器
		vAdapter = new ViewPagerAdapter(views);
	}
	
	private void initData(){
		LayoutInflater mLi = LayoutInflater.from(this);
		for(int i=0; i< pics.length; i++){
			View view = mLi.inflate(R.layout.guide_view, null);
			view.setBackgroundResource(pics[i]);
			views.add(view);
		}
		View startView = mLi.inflate(R.layout.guide_view_last, null);
		views.add(startView);
		
		btn_start = (Button)startView.findViewById(R.id.startBtn);
		
		// 设置监听
		viewPager.setOnPageChangeListener(this);
		// 设置适配器数据
		viewPager.setAdapter(vAdapter);
		
		initPoint();
		
		btn_start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GuideActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	
	/**
	 * 初始化底部小点
	 */
	private void initPoint(){
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll);       
		
        points = new ImageView[pics.length + 1];
        //循环取得小点图片
        for (int i = 0; i < points.length; i++) {
        	//得到一个LinearLayout下面的每一个子元素
        	points[i] = (ImageView) linearLayout.getChildAt(i);
        	//默认都设为灰色
        	points[i].setEnabled(true);
        	//给每个小点设置监听
        	points[i].setOnClickListener(this);
        	//设置位置tag，方便取出与当前位置对应
        	points[i].setTag(i);
        }
        
        //设置当面默认的位置
        currentIndex = 0;
        //设置为白色，即选中状态
        points[currentIndex].setEnabled(false);
	}
	
	/**
	 * 当新的页面被选中时调用
	 */

	@Override
	public void onPageSelected(int position) {
		//设置底部小点选中状态
        setCurDot(position);
	}

	/**
	 * 通过点击事件来切换当前的页面
	 */
	@Override
	public void onClick(View v) {
		 int position = (Integer)v.getTag();
         setCurView(position);
         setCurDot(position);		
	}

	/**
	 * 设置当前页面的位置
	 */
	private void setCurView(int position){
         if (position < 0 || position >= points.length) {
             return;
         }
         viewPager.setCurrentItem(position);
     }

     /**
     * 设置当前的小点的位置
     */
    private void setCurDot(int positon){
         if (positon < 0 || positon > points.length - 1 || currentIndex == positon) {
             return;
         }
         points[positon].setEnabled(false);
         points[currentIndex].setEnabled(true);
         currentIndex = positon;
     }
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

}
