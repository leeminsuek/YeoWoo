package com.foxrainbxm.jjlim.base;

import java.io.IOException;
import java.io.InputStream;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.brainyxlib.http.BrHttpManager;
import com.brainyxlib.util.HttpRequestor;
import com.foxrainbxm.WriteFileLog;

public class HttpContentTask extends AsyncTask<String, Void, String[]> {
	
	public interface OnContentTaskResultListener{
		public void onTaskResult(boolean success, String result);
	}

	Context context; 
	private OnContentTaskResultListener listener;
	Dialog progress;
	
	public HttpContentTask(Context cx, OnContentTaskResultListener l){
		context = cx;
		listener = l;
	}
	
	@Override
	protected String[] doInBackground(String... params) {
		Log.d("TEST", params[0]);
		
		String[] result = {"false", ""}; 
		
		HttpRequestor http = BrHttpManager.getInstance().getHttpRequestor(params[0]);
		
		if (params[1].equals("cat")) {
			http.addParameter("type", params[2]);
			http.addParameter("idx", params[3]);
		}
		else if (params[1].equals("det")) {
			http.addParameter("postno", params[2]);
			http.addParameter("id", params[3]);
			http.addParameter("idx", params[4]);
		}
		else if (params[1].equals("heart")) {
			http.addParameter("oper", params[2]);
			http.addParameter("postno", params[3]);
			http.addParameter("id", params[4]);
		}
		else if (params[1].equals("reply")) {
			http.addParameter("postno", params[2]);
			http.addParameter("id", params[3]);
			http.addParameter("text", params[4]);
		}
		else if (params[1].equals("scrap")) {
			http.addParameter("postno", params[2]);
			http.addParameter("id", params[3]);
		}
		

		
		InputStream is;
		try {
			is = http.sendPost();
			return BrHttpManager.getInstance().ResultToJson(is);
		} catch (IOException e) {
			WriteFileLog.writeException(e);
			return result;
		}
	}
	
	@Override
	protected void onPreExecute() {
		progress = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
		RelativeLayout layout = new RelativeLayout(context);
		layout.setGravity(Gravity.CENTER);
		layout.addView(new ProgressBar(context), new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		progress.setContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		progress.show();
		
	}
	
	@Override
	protected void onPostExecute(String[] result) {
		
		progress.dismiss();
		if(listener != null){
			// data�� ��ȯ���� �ʰ� json ���·� ����...
			listener.onTaskResult(Boolean.valueOf(result[0]), result[1]);
		}
	}
	

}