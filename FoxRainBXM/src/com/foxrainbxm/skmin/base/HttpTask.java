package com.foxrainbxm.skmin.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxrainbxm.WriteFileLog;

public class HttpTask extends AsyncTask<String, Void, String[]> {
	public interface OnTaskResultListener{
		public void onTaskResult(boolean success, Map<String, Object> result, String message);
	}

	Context context; 
	private OnTaskResultListener listener;
	Dialog progress;
	
	public HttpTask(Context cx, OnTaskResultListener l){
		context = cx;
		listener = l;
	}
	
	@Override
	protected String[] doInBackground(String... params) {
		Log.d("task", params[0]);
		HttpRequestor http = BrHttpManager.getInstance().getHttpRequestor(params[0]);
		InputStream is;
		try {
			is = http.sendGet();
			return ResultToJson2(is);
		} catch (IOException e) {
			WriteFileLog.writeException(e);
		}
		
		return null;
	}
	
	@Override
	protected void onPreExecute() {
		
		progress = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
		RelativeLayout layout = new RelativeLayout(context);
		layout.setGravity(Gravity.CENTER);
		layout.addView(new ProgressBar(context), new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		progress.setContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		if(progress != null && progress.isShowing()){
			progress.show();	
		}
		
		
//		progress = ProgressDialog.show(context, "", "", true, false);
//		progress = new ProgressDialog(context);
//		progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//		progress.show();
	}
	
	@Override
	protected void onPostExecute(String[] result) {
		progress.dismiss();
		if(listener != null && result != null){
			listener.onTaskResult(Boolean.valueOf(result[0]), jsonToMap(result[1]), result[2]);
		}else {
			listener.onTaskResult(false, null, result[2]);
		}
	}
	
	 public String[] ResultToJson2(InputStream is) {
	    	String[] resultdata = new String[3];
			String data = null;
			
			try {
				StringBuilder builder = new StringBuilder();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				for (;;) {
					String line = br.readLine();
					if (line == null)
						break;
					builder.append(line);
				}
				br.close();
				try {
					JSONObject obj = new JSONObject(builder.toString());
					resultdata[0] = obj.getString("result");
					resultdata[1] = obj.getString("data");
					resultdata[2] = obj.getString("message");
				} catch (JSONException e) {
					WriteFileLog.writeException(e);
					return null;
				}
			} catch (IOException e) {
				WriteFileLog.writeException(e);
			}
			return resultdata;
		}
	 
	private Map<String,Object> jsonToMap(String json){
		Map<String,Object> map = new HashMap<String, Object>();
		if(json == null || json.length() < 1)
			return map;
		
		ObjectMapper mapper = new ObjectMapper();
	 
		try {
			map = mapper.readValue(json, new TypeReference<HashMap<String,Object>>(){});
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
		return map;
	}

}
