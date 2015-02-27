package com.foxrainbxm.tab7;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foxrainbxm.FoxRainBXM;
import com.foxrainbxm.R;
import com.foxrainbxm.base.BaseFragment;

public class Scenario5_1 extends BaseFragment{

	View view = null;
	FoxRainBXM myapp = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		} else {
			view = inflater.inflate(R.layout.scenario1_1, container, false);
			myapp = (FoxRainBXM) getActivity().getApplicationContext();
			initialize();
		}
		return view;
	}
	
	private void initialize(){
//		TextView textview = (TextView)view.findViewById(R.id.textview);
//		textview.setText("�ó�����5 ����ȭ��");
	}
	
}
