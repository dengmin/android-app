package com.dengmin.app.ui;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.dengmin.app.R;
import com.dengmin.app.model.RollImageItem;
import com.dengmin.app.util.ImageCacheUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class RollViewPager extends ViewPager{

	private Context context;
	private List<RollImageItem> imageItems;
	private int currentItem;
	private OnPagerClickCallback onPagerClickCallback;
	private boolean hasSetAdapter = false;
	
	private MyOnTouchListener myOnTouchListener;
	private ViewPagerTask viewPagerTask;
	
	private long start = 0;
	
	public RollViewPager(Context context) {
		super(context);
	}

	public RollViewPager(Context context, List<RollImageItem> imageItems,OnPagerClickCallback onPagerClickCallback) {
		super(context);
		this.context = context;
		this.imageItems = imageItems;
		this.onPagerClickCallback = onPagerClickCallback;
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			RollViewPager.this.setCurrentItem(currentItem);
			startRoll();
		}
	};
	
	public class ViewPagerTask implements Runnable {
		@Override
		public void run() {
			currentItem = (currentItem + 1) % imageItems.size();
			handler.obtainMessage().sendToTarget();
		}
	}
	
	public void startRoll() {
		if (!hasSetAdapter) {
			hasSetAdapter = true;
			this.setOnPageChangeListener(new MyOnPageChangeListener());
			this.setAdapter(new ViewPagerAdapter());
		}
		handler.postDelayed(viewPagerTask, 2500);
	}

	
	public class MyOnTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				start = System.currentTimeMillis();
				handler.removeCallbacksAndMessages(null);
				break;
			case MotionEvent.ACTION_MOVE:
				handler.removeCallbacks(viewPagerTask);
				break;
			case MotionEvent.ACTION_CANCEL:
				startRoll();
				break;
			case MotionEvent.ACTION_UP:
				long duration = System.currentTimeMillis() - start;
				if (duration <= 400) {
					if(onPagerClickCallback!=null)onPagerClickCallback.onPagerClick(currentItem);
				} 
				startRoll();
				break;
			}
			return true;
		}
	}
	
	
	class MyOnPageChangeListener implements OnPageChangeListener {
		int oldPosition = 0;

		@Override
		public void onPageSelected(int position) {
			currentItem = position;
			/*if (title != null)
				title.setText(titles[position]);
			if (dots != null && dots.size() > 0) {
				dots.get(position).setBackgroundResource(dot_focus_resId);
				dots.get(oldPosition).setBackgroundResource(dot_normal_resId);
			}*/
			oldPosition = position;
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	}

	
	class ViewPagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return imageItems.size();
		}

		@Override
		public Object instantiateItem(View container, final int position) {
			View view = View.inflate(context, R.layout.ads_image, null);
			((ViewPager) container).addView(view);
			view.setOnTouchListener(myOnTouchListener);
			ImageView imageView = (ImageView) view.findViewById(R.id.image);
			ImageLoader.getInstance().displayImage(imageItems.get(position).getImageUrl(),
					imageView,ImageCacheUtil.OPTIONS.default_options);
			return view;
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// 将ImageView从ViewPager移除
			((ViewPager) arg0).removeView((View) arg2);
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		handler.removeCallbacksAndMessages(null);
		super.onDetachedFromWindow();
	}
	
	interface OnPagerClickCallback {
		public abstract void onPagerClick(int position);
	}
}
