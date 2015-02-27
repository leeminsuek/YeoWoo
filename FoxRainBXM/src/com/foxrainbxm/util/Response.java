package com.foxrainbxm.util;

import org.json.JSONException;
import org.json.JSONObject;

public class Response {
	JSONObject jsonObj = null;
	boolean result = false;
	String msg = "";
	

	public boolean getResult() {
		return result;
	}
		
	public String getMsg() {
		return msg;
	}
	
	public void setReponse(String value) throws Exception {
		
		try {
			jsonObj = new JSONObject(value);
			result = jsonObj.getBoolean("result");
			msg = jsonObj.getString("message");
						
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}
	
	public JSONObject getJsonObj() {
		return jsonObj;
	}
}
