package com.foxrainbxm.skmin.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.foxrainbxm.FoxRainBXM;
import com.foxrainbxm.R;
import com.foxrainbxm.base.BaseFragment;
import com.foxrainbxm.skmin.base.DBManager;

/**
 * 2014. 10. 5.
 * MinKhun
 *
 * Setting00019
 */
public class Setting00019 extends BaseFragment implements OnClickListener {
	
	ListView lstView;
	
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
			view = inflater.inflate(R.layout.setting00019, container, false);
			myapp = (FoxRainBXM) getActivity().getApplicationContext();
			initialize();
		}
		return view;
	}
	
	private void initialize(){
		View btnAlarmList = view.findViewById(R.id.btnAlarmList);
		View btnNoticeList = view.findViewById(R.id.btnNoticeList);
		btnAlarmList.setOnClickListener(this);
		btnNoticeList.setOnClickListener(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();

		View alarmCount = view.findViewById(R.id.alarmCount);
		DBManager db = DBManager.getDBManager(getBaseActivity());
		int count = db.countOfUnReadNotifications();
		if (count < 1) {
			alarmCount.setVisibility(View.GONE);
		} else {
			TextView txtAlarmCount = (TextView) view.findViewById(R.id.txtAlarmCount);
			txtAlarmCount.setText(count + "");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAlarmList:
			startActivity(new Intent(getBaseActivity(), Setting00119.class));
			break;
		case R.id.btnNoticeList:
			startActivity(new Intent(getBaseActivity(), Setting00120.class));
			break;
		default:
			break;
		}		
	}
}
