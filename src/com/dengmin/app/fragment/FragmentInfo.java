package com.dengmin.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dengmin.app.R;

public class FragmentInfo extends Fragment {
	
	private TextView title_text;
	
	private Button btn_back;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_info, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		title_text = (TextView) getView().findViewById(R.id.title_text);
		title_text.setText("我的资料");
		btn_back = (Button) getView().findViewById(R.id.btn_title_left);
		btn_back.setVisibility(View.VISIBLE);
		btn_back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
			}
		});
	}
}
