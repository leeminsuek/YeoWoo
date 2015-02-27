package com.foxrainbxm.tab5;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.brainyxlib.BrUtilManager;
import com.foxrainbxm.FoxRainBXM;
import com.foxrainbxm.R;
import com.foxrainbxm.WriteFileLog;
import com.foxrainbxm.base.BaseActivity;
import com.foxrainbxm.jjlim.libary.CustomEditText;
import com.foxrainbxm.util.TUrlHelper;
import com.foxrainbxm.util.TYoutubeHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class Scenario5_S11113 extends BaseActivity{

	public FoxRainBXM myapp;
	private CustomEditText keyword;
	private ImageView thumnail;
	ProgressDialog progress;
	String ytblink = null, ytbThumbnail= null;
	boolean modify = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.scenario5_s11113);
		myapp = (FoxRainBXM)getApplicationContext();
		init();
	}
	

	@Override
	protected void onDestroy() {
		if(Scenario5_1.getinstance() != null){
			Scenario5_1.getinstance().Update();
		}
		super.onDestroy();
	}
	
	private void init(){
		try {
			
			findViewById(R.id.title_back).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			
			findViewById(R.id.btnSelectOk).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(ytblink != null && ytbThumbnail != null){
						Intent intent = new Intent();
						intent.putExtra("yurl", ytblink);
						intent.putExtra("thumb", ytbThumbnail);
						setResult(RESULT_OK, intent);
						finish();
					}else{
						BrUtilManager.getInstance().showToast(Scenario5_S11113.this, "저장할 유투브 영상이 없습니다.");
					}
				}
			});
			
			keyword = (CustomEditText)findViewById(R.id.keyword);
			
			findViewById(R.id.search).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Thread();
					/*if(!keyword.getText().toString().equals("") && keyword.getText().toString().contains("www.youtube.com/watch?v=")){
						Thread();
					}else{
						BrUtilManager.getInstance().showToast(Scenario5_S11113.this, "�ּҸ� ��Ȯ�ϰ� �Է��ϼ���.");
					}*/
				}
			});
			
			thumnail = (ImageView)findViewById(R.id.thumnail);
			
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
	}
	
	
	private void Thread(){
		progress = BrUtilManager.getInstance().ShowProgressDialog(Scenario5_S11113.this, "검색중 입니다...");
		new Thread() {
			public void run() {
				getThumnail();
			}
		}.start();
	}
	
	private void getThumnail(){
		String key = keyword.getText().toString();
		
		if ( TUrlHelper.isValidUrl( key ) ) {
			String id = TYoutubeHelper.getVideoIdFromUrl( key );
			if ( !id.isEmpty() ) {
				StringBuilder thumbnailUrl = new StringBuilder();
				thumbnailUrl.append( "http://img.youtube.com/vi/" );
				thumbnailUrl.append( id );
				thumbnailUrl.append( "/hqdefault.jpg" );						
				
				ytbThumbnail = thumbnailUrl.toString();
				ytblink = keyword.getText().toString();
				Handler.sendEmptyMessage(1);
				
			}else{
				Handler.sendEmptyMessage(2);
			}
		}else{
			Handler.sendEmptyMessage(2);
		}
		
		
	/*	String key2 = key.replace("www.youtube.com/watch?v=", "");
		String contentText2 = null;
		try {
			contentText2 = java.net.URLEncoder.encode(new String(key2.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=soccer&key=AIzaSyCLQmkOa62vkkV4J93HEhj-ubNPv0ct9xw";
		//String url = domain + contentText2 + "&start-index="+1 +"&max-results=1&alt=jsonc&v=2";
		
		String json = BrUtilManager.getInstance().DownloadHtml(url);
		*/
		/*try{
			//getJsonParser(json);
			Handler.sendEmptyMessage(1);
		}catch(Exception e){
			Handler.sendEmptyMessage(2);
		}*/
	}
	
	public Handler Handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			progress.dismiss();
			switch (msg.what) {
			case 1:
				if(ytblink == null || ytbThumbnail == null){
					BrUtilManager.getInstance().showToast(Scenario5_S11113.this, "경로가 잘못되었습니다.");
				}else{
					try {
						
						
						
						ImageLoader.getInstance().displayImage(ytbThumbnail,
								thumnail, myapp.options, new ImageLoadingListener() {
									
									@Override
									public void onLoadingStarted(String arg0, View arg1) {
									}
									
									@Override
									public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
									}
									
									@Override
									public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
									}
									
									@Override
									public void onLoadingCancelled(String arg0, View arg1) {
									}
								});

						setProgressBarIndeterminateVisibility(false);
					} catch (Exception e) {
						WriteFileLog.writeException(e);
						setProgressBarIndeterminateVisibility(false);
					}
					
				}
				break;
			case 2:
				BrUtilManager.getInstance().showToast(Scenario5_S11113.this, "주소가 잘못되었거나, 데이터가 없습니다.");
				break;
			}
		}
	};
	
	public void getJsonParser(String json){
		JSONObject object;
		JSONArray data = null;
		try {
			object = new JSONObject(json.toString());
			object = object.getJSONObject("data");
			data = object.getJSONArray("items");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		if (data != null) {
			for (int i = 0; i < data.length(); i++) {
				try {
					JSONObject js = data.getJSONObject(i);
					ytbThumbnail = js.getJSONObject("thumbnail").getString("hqDefault");
					ytblink = js.getJSONObject("player").getString("default");
				} catch (JSONException e) {
					WriteFileLog.writeException(e);
				}
			}
		}
	}
}
