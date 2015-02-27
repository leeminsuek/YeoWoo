package com.foxrainbxm.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.brainyxlib.util.HttpRequestor;
import com.foxrainbxm.WriteFileLog;


public class BrHttpManager {

	public final String TAG = "BrHttpManager";
	private static BrHttpManager instance = null;
	
	public synchronized static BrHttpManager getInstance() {
		if (instance == null) {
			synchronized (BrHttpManager.class) {
				if (instance == null) {
					instance = new BrHttpManager();
				}
			}
		}
		return instance;
	}
	
	/**
	 * http 객체 가져오기
	 * @param urladdr
	 * @return
	 */
	public HttpRequestor getHttpRequestor(String urladdr){
		HttpRequestor upload = null;
		try {
			upload = new HttpRequestor(new URL(urladdr));	
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
		return upload;
	}
	
	/**
	 * send post
	 * @param upload
	 */
	public InputStream SendHttpPost(HttpRequestor upload){
		InputStream input = null;
		try {
			input = upload.sendPost();
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
		return input;
	}
	
	/**
	 * send multipart post
	 * @param upload
	 * @return
	 */
	public InputStream SendMutilPartPost(HttpRequestor upload){
		InputStream input = null;
		try {
			input = upload.sendMultipartPost();
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
		return input;
	}

	/**
	 * upload 데이터 추가
	 * @param upload
	 * @param key
	 * @param value
	 */
	public void AddParameter(HttpRequestor upload, String key,String value){
		try {
			upload.addParameter(key, value);
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
	}
	
	/**
	 * upload 파일 데이터 추가
	 * @param upload
	 * @param key
	 * @param fileaddr
	 */
	public void AddFileParameter(HttpRequestor upload, String key, String fileaddr){
		try {
			upload.addFile(key, new File(fileaddr));
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
	}
	
	  /**
     * 
     * @param is
     * @return
     */
    public String ResultToObject(InputStream is) {
    	String[] resultdata = new String[2];
		String data = null;
		JSONObject object;
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
				object = new JSONObject(builder.toString());
				resultdata[0] = object.getString("result");
				resultdata[1] = object.getString("data");
			} catch (JSONException e) {
				WriteFileLog.writeException(e);
				return null;
			}
		} catch (IOException e) {
			WriteFileLog.writeException(e);
		}
		return data;
	}
    /**
     * 
     * @param is
     * @return
     */
    public String[] ResultToJson(InputStream is) {
    	String[] resultdata = new String[2];
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
				resultdata[1] = obj.getString("message");
			} catch (JSONException e) {
				WriteFileLog.writeException(e);
				return null;
			}
		} catch (IOException e) {
			WriteFileLog.writeException(e);
		}
		return resultdata;
	}
    
	/**
	 * 
	 * @param is
	 * @return
	 */
	public String ResultToString(InputStream is) {
		String check = "";
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
			check = builder.toString();
		} catch (IOException e) {
			WriteFileLog.writeException(e);
		}
		return check;

	}
}
