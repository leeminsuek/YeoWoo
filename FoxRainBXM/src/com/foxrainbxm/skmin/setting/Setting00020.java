package com.foxrainbxm.skmin.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;

import com.brainyxlib.BrUtilManager;
import com.foxrainbxm.Common;
import com.foxrainbxm.R;
import com.foxrainbxm.WriteFileLog;
import com.foxrainbxm.base.BaseActivity;
import com.foxrainbxm.skmin.base.HttpTask.OnTaskResultListener;
import com.foxrainbxm.skmin.base.Popup2Button;
import com.foxrainbxm.skmin.base.SharedValues;
import com.foxrainbxm.skmin.base.SharedValues.SharedKeys;
import com.foxrainbxm.skmin.login.Login00004;
import com.foxrainbxm.tab5.Scenario5_1;
import com.foxrainbxm.tab5.Scenario5_S00117;
import com.foxrainbxm.util.RecycleUtils;
import com.foxrainbxm.util.Request;

/**
 * 2014. 10. 5.
 * MinKhun
 *
 * Setting00020
 * 설정
 */
public class Setting00020 extends BaseActivity implements OnClickListener{
	
	CheckBox chkAlarm;
	
	@Override
	protected void onDestroy() {
		try {
			 System.gc();
			 RecycleUtils.recursiveRecycle(getWindow().getDecorView());
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
		super.onDestroy();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.setting00020);
		View backbutton = findViewById(R.id.btnTitleBack);
		backbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		if(SharedValues.getSharedValue(this, SharedKeys.NOTI_ALARM) == null)
			SharedValues.setSharedValue(this, SharedKeys.NOTI_ALARM, "true");
		
		boolean emailType = "Y".equals(SharedValues.getSharedValue(this, SharedKeys.LOGIN_TYPE));
		if(!emailType){
			View v = findViewById(R.id.changePassword);
			v.setVisibility(View.GONE);
		}
		boolean checked = Boolean.valueOf(SharedValues.getSharedValue(this, SharedKeys.NOTI_ALARM));
		chkAlarm = (CheckBox) findViewById(R.id.checkBox1);
		chkAlarm.setChecked(checked);
		TextView txtVersion = (TextView) findViewById(R.id.txtVersion);
		txtVersion.setText(BrUtilManager.getInstance().GetVersion(this));
//		txtVersion.setText("1.0.2");
		
		TextView txtAccount = (TextView) findViewById(R.id.txtAccount);
		txtAccount.setText(SharedValues.getSharedValue(this, SharedKeys.EMAIL));
	}

	private void goLogin(){
		Intent intent = new Intent(getApplicationContext(), Login00004.class);
		SharedValues.setSharedValue(this, SharedKeys.AUTO_LOGIN, "false");
		SharedValues.setSharedValue(getBaseContext(), SharedKeys.ID, null);
//		SharedValues.setSharedValue(getBaseContext(), SharedKeys.PUSH_KEY, null);
		SharedValues.setSharedValue(getBaseContext(), SharedKeys.EMAIL, null);
		SharedValues.setSharedValue(getBaseContext(), SharedKeys.PASSWORD, null);
		SharedValues.setSharedValue(getBaseContext(), SharedKeys.LOGIN_TYPE, null);
		SharedValues.setSharedValue(getBaseContext(), SharedKeys.PROFILEPHOTO, null);
		Common.profileBitmap = null;
		
		ComponentName cn = intent.getComponent();
		Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
		startActivity(mainIntent);
		finish();
	}
	
	
	private void Logout() {
			AsyncTask<Void, Void, Void> logout = new AsyncTask<Void, Void, Void>() {
				JSONObject rgb = null;
				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					try {
						//	BrUtilManager.getInstance().InputKeybordHidden(Scenario5_S00117.this, replyinput);
//						linearLayout_loading.setVisibility(View.VISIBLE);
					} catch (Exception e) {
						WriteFileLog.writeException(e);
					}
				}
				@Override
				protected Void doInBackground(Void... params) {
					try {
						List<NameValuePair> dataList = new ArrayList<NameValuePair>();
						dataList.add(new BasicNameValuePair("userId",myApp.getUserInfo().getId()));
						//  dataList.add(new BasicNameValuePair("id",myApp.getUserInfo().getId()));

						rgb = Request.requestService(Setting00020.this,Common.LOG_OUT, dataList);
					} catch (Exception e) {
						WriteFileLog.writeException(e);

					}			
					return null;
				}
				@Override
				protected void onPostExecute(Void result) {
					try {	
						if(rgb != null ) {
							String resultflag = rgb.getString("result");
							if(resultflag.equals("true")) {
								goLogin();
//								BrUtilManager.getInstance().ShowDialog(Setting00020.this,getString(R.string.app_name), rgb.getString("message"));
							}else {
								BrUtilManager.getInstance().ShowDialog(Setting00020.this,getString(R.string.app_name), "잠시후 다시 시도하세요");
							}
						}
					} catch (Exception e) {
						WriteFileLog.writeException(e);
					} finally {
						//finish();
					}				
					super.onPostExecute(result);		
				}		
			};
			logout.execute();
	}
	public void onSettingClick(View v){
		int index = Integer.valueOf(v.getTag().toString());
		switch (index) {
		case 0://알림
			chkAlarm.toggle();
			SharedValues.setSharedValue(this, SharedKeys.NOTI_ALARM, chkAlarm.isChecked()+"");
			break;
		case 1://공지사항
			startActivity(new Intent(this, Setting00120.class));
			break;
		case 2://버전정보
			break;
		case 3://로그아웃
		{
			Popup2Button popup = new Popup2Button(this, "여우비 로그아웃을\n하시겠습니까?", new android.app.Dialog.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					if(arg1 == 0){
						String type = SharedValues.getSharedValue(getBaseContext(), SharedKeys.LOGIN_TYPE);
						if("F".equals(type)){
							com.facebook.Session session = com.facebook.Session.getActiveSession();
							if (session != null) {
								if (!session.isClosed()) {
									session.closeAndClearTokenInformation();
									goLogin();
								}
							}
						}
						else {
//							Logout();
							goLogin();
						}
						
					}
				}
			});
			popup.setPopupButton(0, "네");
			popup.setPopupButton(1, "아니오");
			popup.show();
		}
			break;
		case 4://회원탈퇴
		{
			Popup2Button popup = new Popup2Button(this, "등록하셨던 게시물들이 모두 삭제됩니다.\n그래도 회원탈퇴를 하시겠습니까?", new android.app.Dialog.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(which == 0){
						getApi().userWithdraw(SharedValues.getSharedValue(getBaseContext(), SharedKeys.EMAIL),SharedValues.getSharedValue(getBaseContext(), SharedKeys.PASSWORD),new OnTaskResultListener() {
							@Override
							public void onTaskResult(boolean success, Map<String, Object> result, String message) {
								if (success) {
									String type = SharedValues.getSharedValue(getBaseContext(), SharedKeys.LOGIN_TYPE);
									if("F".equals(type)){
										com.facebook.Session session = com.facebook.Session.getActiveSession();
									    if (session != null) {
									        if (!session.isClosed()) {
									        	session.closeAndClearTokenInformation();
									        	BrUtilManager.getInstance().showToast(Setting00020.this, "회원탈퇴가 완료되었습니다.");
									        	goLogin();
									        }
									    }
									}
									else {
//										Logout();
//										SharedValues.setSharedValue(getBaseContext(), SharedKeys.AUTO_LOGIN, "false");
//										SharedValues.setSharedValue(getBaseContext(), SharedKeys.EMAIL, null);
//										SharedValues.setSharedValue(getBaseContext(), SharedKeys.PASSWORD, null);
//										SharedValues.setSharedValue(getBaseContext(), SharedKeys.LOGIN_TYPE, null);
										BrUtilManager.getInstance().showToast(Setting00020.this, "회원탈퇴가 완료되었습니다.");
										goLogin();
									}
								}
//								else {
//									
//									String type = SharedValues.getSharedValue(getBaseContext(), SharedKeys.LOGIN_TYPE);
//									if("F".equals(type)){
//										com.facebook.Session session = com.facebook.Session.getActiveSession();
//									    if (session != null) {
//									        if (!session.isClosed()) {
//									        	session.closeAndClearTokenInformation();
//									        	goLogin();
//									        }
//									    }
//									}
//									
//									SharedValues.setSharedValue(getBaseContext(), SharedKeys.AUTO_LOGIN, "false");
//									SharedValues.setSharedValue(getBaseContext(), SharedKeys.EMAIL, null);
//									SharedValues.setSharedValue(getBaseContext(), SharedKeys.PASSWORD, null);
//									SharedValues.setSharedValue(getBaseContext(), SharedKeys.LOGIN_TYPE, null);
//									
//									goLogin();
//								}
							}
						});
					}
				}
			});
			popup.setPopupButton(0, "네");
			popup.setPopupButton(1, "아니오");
			popup.show();
		}
			break;
		case 5://내계정
			break;
		case 6://비밀번호 변경
			startActivity(new Intent(this, Setting00023.class));
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_join_clause:
			break;
		case R.id.btn_join_information:
			break;

		default:
			break;
		}
	}
}
