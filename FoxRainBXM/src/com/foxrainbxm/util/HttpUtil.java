package com.foxrainbxm.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.brainyxlib.http.BrHttpManager;
import com.brainyxlib.util.HttpRequestor;
import com.foxrainbxm.Common;
import com.foxrainbxm.R;
import com.foxrainbxm.WriteFileLog;

public class HttpUtil {
	
	private static HttpUtil instance;
	
	public static HttpUtil getInstance() {
		if (instance == null) {
			instance = new HttpUtil();
		}
		return instance;
	}
	
	private Dialog dialog;
	
	/**
	 * @param onAfter
	 * @param data
	 */
	public void requestHttp(final Activity ctx, final OnAfterParsedData onAfter,Map<String, Object> data,final boolean flag){
		
		AsyncTask<Map<String,Object>, Integer, Boolean> executer = new AsyncTask<Map<String,Object>, Integer, Boolean>() {
			String resultMsg;
			ArrayList<Map<String, String>> resultMap;
			PageVO pager;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				showProgress(ctx);
			}
			
			@Override
			protected Boolean doInBackground(Map<String, Object>... arg0) {
				boolean isfile = false;
				Map<String, Object> set = arg0[0];
				if (set == null) {
					return null;							
				}
				
				InputStream is = null;
				
				try {
					String url = (String) set.get(KEY_URL); 
					set.remove(KEY_URL);
					
					HttpRequestor http = BrHttpManager.getInstance().getHttpRequestor(Common.BaseUrl+ url);
					//HttpRequestor http = BrHttpManager.getInstance().getHttpRequestor(ctx.getString(R.string.url_base)+ url);
					if (!set.isEmpty()) {
						for (String key : set.keySet()) {
							if (!key.equals(KEY_URL)) {
								Object value = set.get(key);
								if (value instanceof String) {
									http.addParameter(key, value.toString());
								}else if (value instanceof File) {
									 isfile = true;
									http.addFile(key, (File)value);
								}else {
									if (value == null) {
										continue;
									}
									http.addParameter(key, value.toString());
								}
							}
						}
					}
					
					if(flag){
						if(isfile){
							is = http.sendMultipartPost();
						}else{
							is = http.sendPost();
						}	
					}else{
						is = http.sendMultipartPost();
					}
					
					
					/*if(url.equals("profileContent") || url.equals("myScrab")){
						is = http.sendPost();
					}else{
						is = http.sendMultipartPost();	
					}*/
					/*is = http.sendPost();*/
					
					
					BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8")); 
				    StringBuilder responseStrBuilder = new StringBuilder();

				    String inputStr;
				    while ((inputStr = streamReader.readLine()) != null)
				        responseStrBuilder.append(inputStr);
				    JSONObject obj = new JSONObject(responseStrBuilder.toString());
				    
				    boolean result = (Boolean) obj.get(KEY_RESULT);
				    resultMsg =  obj.get(KEY_MSG).toString();
				    
				    resultMap = new ArrayList<Map<String, String>>();
				    
				    if (obj.get(KEY_DATA) instanceof JSONArray) {
				    	JSONArray jArr = obj.getJSONArray(KEY_DATA);
				    	int length = jArr.length();
				    	for (int i = 0; i < length; i++) {
				    		HashMap<String, String> tmpMap = new HashMap<String, String>();
				    		JSONObject tmpObj = jArr.getJSONObject(i);
				    		Iterator<String> keys = tmpObj.keys();
				    		while (keys.hasNext()) {
				    			String key = keys.next();
				    			tmpMap.put(key, String.valueOf(tmpObj.get(key)));
				    		}
				    		resultMap.add(tmpMap);
				    	}
					}else {
						//Update, Delete, Insert �� ��� �ݿ� row��
						HashMap<String, String> tmpMap = new HashMap<String, String>();
						if (obj.get(KEY_DATA) != null && obj.get(KEY_DATA) instanceof String) {
							tmpMap.put("count", obj.get(KEY_DATA).toString());
							resultMap.add(tmpMap);
						}else {
							JSONObject tmpObj = obj.getJSONObject(KEY_DATA);
							Iterator<String> keys = tmpObj.keys();
							while (keys.hasNext()) {
								String key = keys.next();
								tmpMap.put(key, String.valueOf(tmpObj.get(key)));
							}
							resultMap.add(tmpMap);
						}
					}
				    
				    
				    if (obj.has(KEY_PAGE)) {
				    	pager = new PageVO();
						JSONObject pageObj = (JSONObject) obj.get(KEY_PAGE);
						int totalCnt = pageObj.getInt("totalCount");
						int curPage = pageObj.getInt("curPage");
						pager.setTotalCnt(totalCnt);
						pager.setCurPage(curPage);
					}
					return result;
				} catch (ClientProtocolException e) {
					WriteFileLog.writeException(e);
				} catch (IOException e) {
					WriteFileLog.writeException(e);
				} catch (JSONException e) {
					WriteFileLog.writeException(e);
				}finally{
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							WriteFileLog.writeException(e);
						}
					}
				}
				
				return false;
			}
			
			
			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				dialog.cancel();
				if (result == false) {
					onAfter.onResult(result, null, null);
					return;
				}else {
					onAfter.onResult(result, resultMap, pager);
				}
			}
		};
		
		executer.execute(data);
	}
	
	protected void showProgress(Activity ctx) {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		
		dialog = new Dialog(ctx, R.style.TransDialog);
		dialog.setContentView(new ProgressBar(ctx));
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.show();
	}

	public static final String KEY_URL = "url";
	
	public static final String KEY_DATA = "data";
	public static final String KEY_RESULT = "result";
	public static final String KEY_MSG = "message";
	/** ����¡�� ������ */
	public static final String KEY_PAGE = "paging";
	
	/**
	 * @author ���� ������ �ޱ�
	 */
	public interface OnAfterParsedData{
		void onResult(boolean result, Object resultData, PageVO pageResult);
	}
	
	public class PageVO {
		int totalCnt 	= 0;
		int curPage 	= 0;
		public int getTotalCnt() {
			return totalCnt;
		}
		public void setTotalCnt(int totalCnt) {
			this.totalCnt = totalCnt;
		}
		public int getCurPage() {
			return curPage;
		}
		public void setCurPage(int curPage) {
			this.curPage = curPage;
		}
	}
}
