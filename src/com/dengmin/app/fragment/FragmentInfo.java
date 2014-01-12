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
		View root = inflater.inflate(R.layout.fragment_info, container, false);
		title_text = (TextView) root.findViewById(R.id.title_text);
		title_text.setText("我的资料");
		btn_back = (Button) root.findViewById(R.id.btn_title_left);
		btn_back.setVisibility(View.VISIBLE);
		btn_back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
			}
		});
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}
}
