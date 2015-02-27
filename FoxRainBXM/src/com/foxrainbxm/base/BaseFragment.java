package com.foxrainbxm.base;

import com.foxrainbxm.R;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public class BaseFragment extends Fragment {

	
	protected FragmentMain getBaseActivity(){
		FragmentActivity fragment = getActivity();    
	    return (FragmentMain) fragment;	    
	}	
	
	public void StartActivity(String classname){
		Intent intent = new Intent();
		intent.setClassName(getBaseActivity(), classname);
		startActivity(intent);
		//getBaseActivity().overridePendingTransition(R.anim.start_enter, R.anim.start_exit);
	}
	
	public String getUrl(int strId){
		return getString(R.string.url_base) + getString(strId);
	}
}
