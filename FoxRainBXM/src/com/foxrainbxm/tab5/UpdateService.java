package com.foxrainbxm.tab5;

import java.io.File;
import java.io.InputStream;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import com.brainyxlib.BrUtilManager;
import com.brainyxlib.util.HttpRequestor;
import com.foxrainbxm.Common;
import com.foxrainbxm.WriteFileLog;
import com.foxrainbxm.util.BrHttpManager;

public class UpdateService extends Service{

	String postNo;
	String type;
	String title;
	String contents;
	String id;
	String localVideo;
	String localThumbnail;

	public interface callbackService {
		public void callback();
	}
	
	callbackService callback;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		postNo = intent.getStringExtra("postNo");
		type = intent.getStringExtra("type");
		title = intent.getStringExtra("title");
		contents = intent.getStringExtra("contents");
		id = intent.getStringExtra("id");
		localVideo = intent.getStringExtra("localVideo");
		localThumbnail = intent.getStringExtra("localThumbnail");
		AddTip();


		return super.onStartCommand(intent, flags, startId);
	}

	private HttpRequestor addUpload(HttpRequestor http){
		http.addParameter("type", type);
		http.addParameter("title", title);
		http.addParameter("contents", contents);
		http.addParameter("id", id);
		if(localVideo.startsWith("http")) {
			http.addParameter("localVideo", localVideo);
		}
		else {
			http.addFile("localVideo", new File(localVideo));
		}
		http.addFile("localThumbnail", new File(localThumbnail));

		return http;
	}


	private void AddTip() {
		AsyncTask<Void, Void, Void> getDataWorker = new AsyncTask<Void, Void, Void>() {
			String[] resultdata = null;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				try {
				} catch (Exception e) {
					WriteFileLog.writeException(e);
				}
			}
			@Override
			protected Void doInBackground(Void... params) {
				try {
					InputStream input = null;
					HttpRequestor http =  null;
					//					if(modify){
					//						http =   BrHttpManager.getInstance().getHttpRequestor(Common.BaseUrl + Common.UPDATETIP);
					//						http.addParameter("postNo", String.valueOf(postno));
					//					}else{
					http =   BrHttpManager.getInstance().getHttpRequestor(Common.BaseUrl + Common.ADDTIP);	
					//					}
					http = addUpload(http);
					input =http.sendMultipartPost();	
					resultdata = BrHttpManager.getInstance().ResultToJson(input);

				} catch (Exception e) {
					WriteFileLog.writeException(e);

				}			
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				try {	

					if(resultdata != null){
						if(resultdata[0].equals("true")){
							BrUtilManager.getInstance().showToast(getBaseContext(), "등록되었습니다.");
						}else{
							BrUtilManager.getInstance().showToast(getBaseContext(), "잠시후 다시 시도해주세요");
						}
					}else{
						BrUtilManager.getInstance().showToast(getBaseContext(), "잠시후 다시 시도해주세요");
					}
				} catch (Exception e) {
					WriteFileLog.writeException(e);
				} finally {
				}				
				super.onPostExecute(result);		
			}		
		};
		getDataWorker.execute();
	}

}
