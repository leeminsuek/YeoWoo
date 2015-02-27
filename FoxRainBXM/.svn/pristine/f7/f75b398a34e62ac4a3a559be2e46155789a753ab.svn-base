package com.foxrainbxm.skmin.login;

import java.util.Map;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.brainyxlib.BrUtilManager;
import com.foxrainbxm.R;
import com.foxrainbxm.WriteFileLog;
import com.foxrainbxm.base.BaseActivity;
import com.foxrainbxm.jjlim.libary.CustomEditText;
import com.foxrainbxm.skmin.base.HttpTask.OnTaskResultListener;
import com.foxrainbxm.skmin.base.Popup1Button;
import com.foxrainbxm.util.RecycleUtils;

/**
 * 2014. 10. 5.
 * MinKhun
 *
 * Login00005
 * 비밀번호 찾기
 */


public class Login00005 extends BaseActivity {
	
	LinearLayout linearLayout_loading;
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
		
		setContentView(R.layout.login00005);
		View backbutton = findViewById(R.id.btnTitleBack);
		linearLayout_loading = (LinearLayout)findViewById(R.id.linearLayout_loading);
		
		backbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		Button btnFindPw = (Button) findViewById(R.id.btnFindPw);
		
		btnFindPw.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				CustomEditText editEmail = (CustomEditText) findViewById(R.id.editEmail);
				String email = editEmail.getText().toString().trim();
				if(BrUtilManager.getInstance().checkEmail(email)){
					linearLayout_loading.setVisibility(View.VISIBLE);
					getApi().findPassword(email, new OnTaskResultListener() {
						@Override
						public void onTaskResult(boolean success, Map<String, Object> result, String message) {
							if(success){
								linearLayout_loading.setVisibility(View.GONE);
								new Popup1Button(Login00005.this, "이메일로 새로운 비밀번호 인증을 전송했습니다.", new Dialog.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										finish();
									}
								}).show();
								
//						    	finish();
							}
							else {
								new Popup1Button(Login00005.this, "이메일 전송에 실패했습니다.", null).show();
							}
						}
					});
				}
				else {
					new Popup1Button(Login00005.this, "올바른 이메일 주소를 입력해 주세요.", null).show();
				}
			}
		});
	}
}
