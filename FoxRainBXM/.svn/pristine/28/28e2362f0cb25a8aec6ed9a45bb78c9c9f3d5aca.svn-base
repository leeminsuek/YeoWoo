package com.foxrainbxm.skmin.base;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Context;

import com.foxrainbxm.Common;
import com.foxrainbxm.WriteFileLog;
import com.foxrainbxm.skmin.base.HttpTask.OnTaskResultListener;

public class HttpApi {
	private Context context;
	
	public HttpApi(Context cx){
		this.context = cx;
	}
	
	//private final String host = "http://jtori.cafe24.com/FoxRain";
//	private final String host = "http://brainyx711.iptime.org:6387/";
//	private static String host = "http://183.111.148.117:8080";
//	private static String imghost = host +"/foxrain/images/";
//	private final String host = "http://brainyx711.iptime.org:6385/";
	private final String host = Common.Base+"/";
	
	public void loginUser(String email, String password, String id_k, String pushkey, OnTaskResultListener result){
		HttpTask task = new HttpTask(context, result);
		task.execute(String.format("%s/login/user?uid=%s&upw=%s&pushkey=%s&id_k=%s", host, email, password, pushkey, id_k));
	}
	
	public void joinUser(String email, String password, String id_k, String name, String pushkey, OnTaskResultListener result){
		String nm = name;
		try {
			nm = URLEncoder.encode(name, "utf8");
		} catch (UnsupportedEncodingException e) {
			WriteFileLog.writeException(e);
		}
		HttpTask task = new HttpTask(context, result);
		task.execute(String.format("%s/login/join?uid=%s&upw=%s&pushkey=%s&name=%s&id_k=%s", host, email, password, pushkey, nm, id_k));
	}
	
	public void userWithdraw(String email, String pw, OnTaskResultListener result){
		HttpTask task = new HttpTask(context, result);
		task.execute(String.format("%s/setting/withdraw?uid=%s&upw=%s", host, email, pw));
	}
	
	public void userChangePassword(String email, String oldPassword, String newPassword, OnTaskResultListener result){
		HttpTask task = new HttpTask(context, result);
		task.execute(String.format("%s/setting/changepw?uid=%s&oldpw=%s&newpw=%s", host, email, oldPassword, newPassword));
	}
	
	public void findPassword(String email, OnTaskResultListener result){
		HttpTask task = new HttpTask(context, result);
		task.execute(String.format("%s/login/findpw?uid=%s", host, email));
	}
	
	public void noticeList(OnTaskResultListener result){
		HttpTask task = new HttpTask(context, result);
		task.execute(String.format("%s/setting/notice", host));
	}
}
