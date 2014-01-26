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
		return inflater.inflate(R.layout.fragment_news, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		title_text = (TextView) getView().findViewById(R.id.title_text);
		title_text.setText("信息");
		btn_right = (Button) getView().findViewById(R.id.btn_title_right);
		btn_right.setText("群聊");
		btn_right.setVisibility(View.VISIBLE);
	}
}
