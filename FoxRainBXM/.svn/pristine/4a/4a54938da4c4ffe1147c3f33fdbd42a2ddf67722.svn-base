package com.foxrainbxm;

import java.net.URLDecoder;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.foxrainbxm.skmin.base.SharedValues;
import com.foxrainbxm.skmin.base.SharedValues.SharedKeys;
import com.foxrainbxm.skmin.base.SplashActivity;
import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService{
	private String TAG = "GCMService";
	private FoxRainBXM myapp;
	@Override
	protected void onError(Context context, String arg1) {
		Log.i(TAG, "Received error: " + arg1);
		SplashActivity instance = SplashActivity.getinstance();
		if(instance != null){
			instance.unRegister();	
		}
	} 

	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.d(TAG, "GCM Service onMessage");
		try {//title, content, type
			if(Boolean.valueOf(SharedValues.getSharedValue(this, SharedKeys.NOTI_ALARM))){
				String contents = intent.getStringExtra("contents");
				String type = intent.getStringExtra("type");
				String postno = intent.getStringExtra("postNo");
				String title = intent.getStringExtra("title");
				String encontents = URLDecoder.decode(contents, "utf-8");
				String entitle = URLDecoder.decode(title,"utf-8");

				myapp = (FoxRainBXM)context.getApplicationContext();
				myapp.notiPushType = type;
				myapp.notiPushNo = postno;

				PushProc pushProc = new PushProc();
				pushProc.onReceivePushMsg(context, entitle, type,postno, encontents);	 
			}
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
	}

	@Override
	protected void onRegistered(Context context, String regId) {
		Log.v(TAG, "onRegistered-registrationId = " + regId);
		SharedValues.setSharedValue(context, SharedKeys.PUSH_KEY, regId);

		SplashActivity instance = SplashActivity.getinstance();
		if(instance != null){
			instance.loading();	
		}

	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// TODO Auto-generated method stub	
		SplashActivity instance = SplashActivity.getinstance();
		if(instance != null){
			instance.unRegister();	
		}

		return super.onRecoverableError(context, errorId);
	}


	@Override
	protected void onUnregistered(Context context, String regId) {
		if (BuildConfig.DEBUG)
			Log.d(TAG, "onUnregistered-registrationId = " + regId);
	}

	public boolean isServiceRunningCheck(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if ("kr.brainyx.libs.EtradePushService".equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
}
