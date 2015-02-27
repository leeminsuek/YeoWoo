package com.foxrainbxm.skmin.login;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.foxrainbxm.R;
import com.foxrainbxm.WriteFileLog;
import com.foxrainbxm.base.BaseActivity;
import com.foxrainbxm.jjlim.libary.CustomEditText;
import com.foxrainbxm.skmin.base.HttpTask.OnTaskResultListener;
import com.foxrainbxm.skmin.base.Popup1Button;
import com.foxrainbxm.skmin.base.SharedValues;
import com.foxrainbxm.skmin.base.SharedValues.SharedKeys;
import com.foxrainbxm.util.RecycleUtils;

/**
 * 2014. 10. 5.
 * MinKhun
 *
 * Login00006
 */
public class Login00006 extends BaseActivity {

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

		setContentView(R.layout.login00006);

		View backbutton = findViewById(R.id.btnTitleBack);
		backbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		final CustomEditText editText1 = (CustomEditText) findViewById(R.id.editText1);
		final CustomEditText editText2 = (CustomEditText) findViewById(R.id.editText2);
		String email = null;
		String password = null;
		if(getIntent().hasExtra("email"))
			email = getIntent().getExtras().get("email").toString().trim();
		if(getIntent().hasExtra("password"))
			password = getIntent().getExtras().get("password").toString();

		editText1.setText(email);
		editText2.setText(password);

		Button btnJoin = (Button) findViewById(R.id.btnJoin);
		btnJoin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				findViewById(R.id.linearLayout_loading).setVisibility(View.VISIBLE);


				final String email = editText1.getText().toString().trim();
				final String password = editText2.getText().toString();

				getApi().loginUser(email, password, "Y", SharedValues.getSharedValue(getBaseContext(), SharedKeys.PUSH_KEY), new OnTaskResultListener() {
					@Override
					public void onTaskResult(boolean success, Map<String, Object> result, String message) {
						try {
							findViewById(R.id.linearLayout_loading).setVisibility(View.GONE);
							if (success) {
								String id = (String)result.get("id");
								int mypoint = (Integer)result.get("mypoint");
								String nickname = (String)result.get("nickname");
								String name = (String)result.get("name");
								String profilePhoto = (String)result.get("profilePhoto");
								String auto = SharedValues.getSharedValue(getBaseContext(), SharedKeys.AUTO_LOGIN);
								if(auto.equals("true")) {
									SharedValues.setSharedValue(getBaseContext(), SharedKeys.LOGIN_TYPE, "Y");
									SharedValues.setSharedValue(getBaseContext(), SharedKeys.EMAIL, email);
									SharedValues.setSharedValue(getBaseContext(), SharedKeys.PASSWORD, password);
									SharedValues.setSharedValue(getBaseContext(), SharedKeys.ID, id);
									SharedValues.setSharedValue(getBaseContext(), SharedKeys.MYPOINT, String.valueOf(mypoint));
									SharedValues.setSharedValue(getBaseContext(), SharedKeys.NICKNAME, nickname);
									SharedValues.setSharedValue(getBaseContext(), SharedKeys.NAME, name);
									SharedValues.setSharedValue(getBaseContext(), SharedKeys.PROFILEPHOTO, profilePhoto);
									SharedValues.setSharedValue(getBaseContext(), SharedKeys.NOTI_ALARM, "true");
								}
								else {
									SharedValues.setSharedValue(getBaseContext(), SharedKeys.LOGIN_TYPE, "Y");
									SharedValues.setSharedValue(getBaseContext(), SharedKeys.EMAIL, email);
									SharedValues.setSharedValue(getBaseContext(), SharedKeys.PASSWORD, password);
									SharedValues.setSharedValue(getBaseContext(), SharedKeys.ID, id);
									SharedValues.setSharedValue(getBaseContext(), SharedKeys.MYPOINT, String.valueOf(mypoint));
									SharedValues.setSharedValue(getBaseContext(), SharedKeys.NICKNAME, nickname);
									SharedValues.setSharedValue(getBaseContext(), SharedKeys.NAME, name);
									SharedValues.setSharedValue(getBaseContext(), SharedKeys.PROFILEPHOTO, profilePhoto);
									SharedValues.setSharedValue(getBaseContext(), SharedKeys.NOTI_ALARM, "true");
								}


								finish();
							}else {
								new Popup1Button(Login00006.this, message ,null).show();
								//								new Popup1Button(Login00006.this, "이메일 주소 혹은 패스워드가 맞지 않습니다.\n다시 입력해 주세요." ,null).show();

							}
						} catch (Exception e) {
							WriteFileLog.writeException(e);
						}
					}
				});
			}
		});

		SharedValues.setSharedValue(this, SharedKeys.AUTO_LOGIN, "true");
		boolean autoLoginCheck = Boolean.valueOf(SharedValues.getSharedValue(this, SharedKeys.AUTO_LOGIN));

		CheckBox autologinCheckBox = (CheckBox) findViewById(R.id.auto_login_check);
		autologinCheckBox.setChecked(autoLoginCheck);
		autologinCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedValues.setSharedValue(Login00006.this, SharedKeys.AUTO_LOGIN, String.valueOf(isChecked));
			}
		});


		TextView txtLostPw = (TextView) findViewById(R.id.txtLostPw);
		txtLostPw.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(Login00006.this, Login00005.class));
				finish();
			}
		});
	}
}
