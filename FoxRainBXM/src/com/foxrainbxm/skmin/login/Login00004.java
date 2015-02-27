package com.foxrainbxm.skmin.login;

import java.security.MessageDigest;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.facebook.Session;
import com.foxrainbxm.R;
import com.foxrainbxm.skmin.base.LoginBaseActivity;

/**
 * 2014. 10. 5.
 * MinKhun
 *
 * Login00004
 */


public class Login00004 extends LoginBaseActivity implements OnClickListener {

	public static Login00004 instance = null;
	public static Login00004 getinstance(){
		return instance;
	}
	
//	private void getAppKeyHash() {
//	    try {
//	        PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
//	        for (Signature signature : info.signatures) {
//	            MessageDigest md;
//	            md = MessageDigest.getInstance("SHA");
//	            md.update(signature.toByteArray());
//	            String something = new String(Base64.encode(md.digest(), 0));
//	            Log.d("Hash key", something);
//	        }
//	    } catch (Exception e) {
//	        // TODO Auto-generated catch block
//	        Log.e("name not found", e.toString());
//	    }
//	}
	LinearLayout linearLayout_loading;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.login00004);
		instance = this;
		com.facebook.widget.LoginButton login = (com.facebook.widget.LoginButton) findViewById(R.id.btn_join_facebook);
		initFacebook(login);
		
		Button btnEmail = (Button) findViewById(R.id.btn_join_email);
		Button btnJoin = (Button) findViewById(R.id.btn_login_join);
		btnEmail.setOnClickListener(this);
		btnJoin.setOnClickListener(this);
		linearLayout_loading=	(LinearLayout)findViewById(R.id.linearLayout_loading);
//		getAppKeyHash();
	}
	
	@Override
    protected void onResume() {
        super.onResume();
         if(!checkType(null,linearLayout_loading)){
        	if(Session.getActiveSession() != null){
        		facebookUserInfo();
        	}
		}else{
			linearLayout_loading.setVisibility(View.GONE);
		}
    }
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_join_facebook:
			
			break;
		case R.id.btn_join_email:
			startActivity(new Intent(this, Login00006.class));
			break;
		case R.id.btn_login_join:
			startActivityForResult(new Intent(this, Login00011.class), 0);
			break;

		default:
			break;
		}
	}
}