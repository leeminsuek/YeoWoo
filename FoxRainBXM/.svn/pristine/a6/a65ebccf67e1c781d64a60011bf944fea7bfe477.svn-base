package com.foxrainbxm.util;

import org.json.JSONObject;

public class ResponseJson  extends Response {
	JSONObject resObject = null;
	
	public void setReponse(String value) throws Exception{
		
		try {
			super.setReponse(value);
					
			resObject = getJsonObj();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}
	
	public JSONObject getResObject() {
		return resObject;
	}
}
