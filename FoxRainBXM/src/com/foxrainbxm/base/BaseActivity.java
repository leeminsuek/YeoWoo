package com.foxrainbxm.base;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.foxrainbxm.FoxRainBXM;
import com.foxrainbxm.R;
import com.foxrainbxm.WriteFileLog;
import com.foxrainbxm.skmin.base.HttpApi;

public class BaseActivity extends Activity {
	private HttpApi api;
	protected FoxRainBXM myApp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		api = new HttpApi(this);
		
		myApp = (FoxRainBXM) getApplication();
		myApp.loadTypeface();
		
	}
	
	public  Bitmap getImageFromURL(String imageURL){
        Bitmap imgBitmap = null;
        HttpURLConnection conn = null;
        BufferedInputStream bis = null;
        
        try
        {
            URL url = new URL(imageURL);
            conn = (HttpURLConnection)url.openConnection();
            conn.connect();
            
            int nSize = conn.getContentLength();
            bis = new BufferedInputStream(conn.getInputStream(), nSize);
            imgBitmap = BitmapFactory.decodeStream(bis);
        }
        catch (Exception e){
            WriteFileLog.writeException(e);
        } finally{
            if(bis != null) {
                try {bis.close();} catch (IOException e) {}
            }
            if(conn != null ) {
                conn.disconnect();
            }
        }
        
        return imgBitmap;
}
	@Override
    public void setContentView(int viewId) {
		//
        View view = LayoutInflater.from(this).inflate(viewId, null);
        ViewGroup group = (ViewGroup)view;
        int childCnt = group.getChildCount();
        for(int i=0; i<childCnt; i++){
            View v = group.getChildAt(i);
            if(v instanceof TextView){
            	if(((TextView) v).getTypeface() == null){
            		((TextView)v).setTypeface(myApp.getFontNormal());
            	}else{
            		if (((TextView) v).getTypeface().equals(Typeface.BOLD)) {
                		((TextView)v).setTypeface(myApp.getFontBold());
    				}else {
    					((TextView)v).setTypeface(myApp.getFontNormal());
    				}	
            	}
            }
        }
        super.setContentView(view);
    }
	
	public HttpApi getApi() {
		return api;
	}
	
	public void profileView(View v){
		return;
	}
	
	public String getUrl(int strId){
		return getString(R.string.url_base) + getString(strId);
	}
}
