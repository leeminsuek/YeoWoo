package com.foxrainbxm.skmin.login;

import java.util.Map;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

import com.brainyxlib.BrUtilManager;
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
 * Login00011
 */
public class Login00011 extends BaseActivity implements OnClickListener{
	
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
		
		setContentView(R.layout.login00011);
		View backbutton = findViewById(R.id.btnTitleBack);
		backbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		Button btn_join_clause = (Button) findViewById(R.id.btn_join_clause);
		Button btn_join_information = (Button) findViewById(R.id.btn_join_information);
		Button btn_join_ok = (Button) findViewById(R.id.btn_join_ok);
		btn_join_clause.setOnClickListener(this);
		btn_join_information.setOnClickListener(this);
		btn_join_ok.setOnClickListener(this);
	}

	private boolean checkLength(String text, int from, int to){
		if(text != null && text.length() >= from && text.length() <= to){
			return true;
		}
		return false;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_join_clause:
			startActivity(new Intent(this, Login00111.class));
			break;
		case R.id.btn_join_information:
			startActivity(new Intent(this, Login00112.class));
			break;
		case R.id.btn_join_ok:
			final CustomEditText editEmail = (CustomEditText) findViewById(R.id.editEmail);
			CustomEditText editName = (CustomEditText) findViewById(R.id.editName);
			final CustomEditText editPassword = (CustomEditText) findViewById(R.id.editPassword);
			CheckBox chkAgree1 = (CheckBox) findViewById(R.id.chkAgree1);
			CheckBox chkAgree2 = (CheckBox) findViewById(R.id.chkAgree2);
			
			String email = editEmail.getText().toString().trim();
			String name = editName.getText().toString().trim();
			String password = editPassword.getText().toString().trim();
			
			if(!BrUtilManager.getInstance().checkEmail(email)){
				new Popup1Button(this, "올바른 이메일 주소를 입력해 주세요.", null).show();
				return;
			}
			
			if(!checkLength(name, 2, 10)){
				new Popup1Button(this, "이름은 최소 2자에서 최대 10자입니다.\n올바른 이름을 입력해 주세요.", null).show();
				return;
			}
			
			if(!checkLength(password, 6, 20)){
				new Popup1Button(this, "패스워드는 영문 대/소문자, 숫자, 특수문자를 구분하여\n6~20자로 이루어집니다.\n올바른 패스워드를 입력해 주세요.", null).show();
				return;
			}
			
			if(!chkAgree1.isChecked() || !chkAgree2.isChecked()){
				new Popup1Button(this, "이용약관과 개인정보 취급동의 항목에 체크해 줘야 가입이 됩니다.", null).show();
				return;
			}
			
			getApi().joinUser(email, password, "Y", name, SharedValues.getSharedValue(this, SharedKeys.PUSH_KEY), new OnTaskResultListener() {
				@Override
				public void onTaskResult(boolean success, Map<String, Object> result, String message) {
					if(success){
						new Popup1Button(Login00011.this, "회원가입이 완료되었습니다.", new Dialog.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent data = new Intent(getBaseContext(), Login00006.class);
								data.putExtra("email", editEmail.getText().toString().trim());
								data.putExtra("password", editPassword.getText());
								SharedValues.setSharedValue(getBaseContext(), SharedKeys.NOTI_ALARM, "true");
								startActivity(data);
								finish();
							}
						}).show();
					}else {
//						Toast.makeText(getBaseContext(), "ȸ�� ���� ����.",Toast.LENGTH_SHORT).show();
						new Popup1Button(Login00011.this, "중복된 이메일입니다. 다른 이메일로 가입해 주세요.", null).show();
					}
				}
			});
			
			break;

		default:
			break;
		}
	}
}