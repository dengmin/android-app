package com.dengmin.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dengmin.app.R;

public class FragmentNews extends Fragment {
	
	private TextView title_text;
	
	private Button btn_right;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_news, container, false);
		
		title_text = (TextView) root.findViewById(R.id.title_text);
		title_text.setText("信息");
		btn_right = (Button)root.findViewById(R.id.btn_title_right);
		btn_right.setText("添加");
		btn_right.setVisibility(View.VISIBLE);
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
}
