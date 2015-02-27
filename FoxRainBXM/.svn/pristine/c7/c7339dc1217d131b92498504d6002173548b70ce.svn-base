package com.foxrainbxm.tab6;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.foxrainbxm.FoxRainBXM;
import com.foxrainbxm.R;
import com.foxrainbxm.base.BaseFragment;

public class Scenario6_1 extends BaseFragment{

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
			view = inflater.inflate(R.layout.scen00118, container, false);
			myapp = (FoxRainBXM) getActivity().getApplicationContext();
			initialize();
		}
		return view;
	}
	
	private void initialize(){
//		TextView textview = (TextView)view.findViewById(R.id.textview);
//		textview.setText("시나리오5 메인화면");
	}
	
}
