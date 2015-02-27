package com.foxrainbxm.skmin.login;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;

import com.foxrainbxm.R;
import com.foxrainbxm.base.BaseActivity;

/**
 * 2014. 10. 5.
 * MinKhun
 *
 * Login00112
 */
public class Login00112 extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.login00112);
		View backbutton = findViewById(R.id.btnTitleBack);
		backbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		WebView webview = (WebView) findViewById(R.id.webView1);
//		webview.loadData("이용약관", "text/html", "utf8");
		webview.loadUrl("file:///android_asset/infor.html");
	}
}
