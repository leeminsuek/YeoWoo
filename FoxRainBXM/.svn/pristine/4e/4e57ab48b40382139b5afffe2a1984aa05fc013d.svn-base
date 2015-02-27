package com.foxrainbxm.util;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.content.Context;

import com.foxrainbxm.Common;
import com.foxrainbxm.WriteFileLog;


public class Request {
	static Request request = null;
	static String baseUrl = Common.BaseUrl;

	static int CONNECT_TIMEOUT = 25000;
	
	public static Request getInstance() {		
		if(request == null) {
			request = new Request();			
		}		
		return request;
	}
	
	public static void setBaseUrl(String value) {
		baseUrl = value;
	}
	
	public String getBaseUrl() {		
		return baseUrl;
	}
	
	
	
	CookieStore cookieStore = new BasicCookieStore(); 
	BasicHttpContext httpContext = new BasicHttpContext(); 
    public BasicHttpContext getHttpContext() {   	 
    	try {
    		if (httpContext == null) {
    			httpContext = new BasicHttpContext();     		
    		}
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
    	return httpContext;
    }
    
    
    public CookieStore getCookieStore() {   	 
    	try {
    		if (cookieStore == null) {
    			cookieStore = new BasicCookieStore();     		
    		}
		} catch (Exception e) {
			// TODO: handle exception
		}
    	return cookieStore;
    }
        
    
    public void createNewCookie() {
    	httpContext = new BasicHttpContext();
    	cookieStore = new BasicCookieStore();    	
    }
    
	 
	
	public static String request(DefaultHttpClient c, HttpUriRequest req) throws Exception {
		String result = "";
		try {
			Request.getInstance().getHttpContext().setAttribute(ClientContext.COOKIE_STORE
					, Request.getInstance().getCookieStore());
			HttpResponse response = c.execute(req, Request.getInstance().getHttpContext());
			InputStream stream = response.getEntity().getContent();
			java.io.ByteArrayOutputStream bos = new ByteArrayOutputStream();			
			byte[] buff = new byte[5120];
			do{
				int count = stream.read(buff, 0, 5120);
				if(count<=0)
					break;
				bos.write(buff,0,count);				
			}while(true);
			result = new String(bos.toByteArray()); //, "UTF-8"			
		} catch (Exception e) {
			WriteFileLog.writeException(e);
			throw e;
		}
		return result;		
	}
	
	public static ResponseJson requestService(Context context, String procName, JSONObject json, int timeOut) throws Exception{
		ResponseJson result = new ResponseJson();

		try {
			String baseurl = Request.getInstance().getBaseUrl();
			String url = baseurl + procName;
						
			DefaultHttpClient c = new DefaultHttpClient();			
			
			HttpConnectionParams.setConnectionTimeout(c.getParams(), timeOut);
			
			HttpPost post = new HttpPost(url);
			
//			post.setHeader("userid", Myinfo.getId(context));
//			post.setHeader("authCode", Myinfo.getPw(context));
			
			StringEntity entity = new StringEntity(json.toString(), HTTP.UTF_8);
			entity.setContentType("application/json");
			post.setEntity(entity);
			
			String httpResponse = request(c, post);
			
			result.setReponse(httpResponse);
		} catch (Exception e) {
			WriteFileLog.writeException(e);
			throw e;
		}

		return result;
	}
	
	public static JSONObject requestService(Context context, String procName, List<NameValuePair> data) throws Exception{
		//ResponseJson result = new ResponseJson();
		JSONObject result = new JSONObject();

		try {
			String baseurl = Request.getInstance().getBaseUrl();
			String url = baseurl + procName;
						
			DefaultHttpClient c = new DefaultHttpClient();			
			
			HttpConnectionParams.setConnectionTimeout(c.getParams(), CONNECT_TIMEOUT);
			//HttpConnectionParams.setSoTimeout(c.getParams(), _socketTimeout);
			
			HttpPost post = new HttpPost(url);
			
			//post.setHeader("userid", Myinfo.getId(context));
			//post.setHeader("authCode", Myinfo.getPw(context));
			
			//StringEntity entity = new StringEntity(data, HTTP.UTF_8);
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(data,HTTP.UTF_8);
			//entity.setContentType("application/json");
			post.setEntity(entity);
			
			
			String httpResponse = request(c, post);
			
			result = new JSONObject(httpResponse);
		} catch (Exception e) {
			WriteFileLog.writeException(e);
			throw e;
		}

		return result;
	}
	
	public static int getIntFromJson(JSONObject jsonObj, String property) {
		int result = 0;
		try {
			Object tmpObj = jsonObj.get(property);
			String tmpStr = String.valueOf(tmpObj);					
			result = Integer.valueOf(tmpStr);			 
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
		return result;
	}
}
