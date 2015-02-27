package com.foxrainbxm.jjlim.base;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

public class ContentMessageParser {

	// 
	public static ArrayList<HashMap<String, String>> getObjectsFromMessage(String msgStr) {
		
		ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String,String>>();
		msgStr =  msgStr.replaceAll("&lt;", "<");
		msgStr =  msgStr.replaceAll("&gt;", ">");
		
		String[] arr = msgStr.split("\\]");
		for (String str : arr) {

			try {
				String subStr = str.substring(1);
				Log.d("TAG_2", subStr);
				String[] subArr = subStr.split(";");

				HashMap<String, String> map = new HashMap<String, String>();
				map.put("type", subArr[0]); 
				Log.d("TAG_2", "sub :: " + subArr[1]);
				map.put("value1", subArr[1]);
				if (Integer.parseInt(subArr[0]) == 3) {
					map.put("value2", subArr[2]);
				}
				results.add(map);
			}
			catch (Exception e){
				continue;
			}
		}
		
		return results;
	}
	
	//
	public static String getCoverImageFromMessage(String msgStr) {
//[3;http://youtu.be/AjzaaRDB_78?list=PLmtapKaZsgZvxmMXNuIwq7hoDIRHCfl7X;
		//http://img.youtube.com/vi/AjzaaRDB_78/hqdefault.jpg]
		String[] arr = msgStr.split("\\]");
		
		try {
			for (String str : arr) {
			
				String subStr = str.substring(1);
				Log.d("TAG", subStr);
				String[] subArr = subStr.split(";");
				Log.d("TAG", subArr[1]);
		
				if (Integer.parseInt(subArr[0]) == 2) {
				
				/*	if (subArr[1].contains("jpg") || subArr[1].contains("png") || subArr[1].contains("JPG")) {
						return subArr[1];
					}*/
					return subArr[1];
//					if (subArr[0].equals("2")) {
//						return subArr[1];
//					}
//					else {
//						return "";
//					}
				}
				else if(Integer.parseInt(subArr[0]) == 3 ) {
					return subArr[2];
				}
			}
		}
		catch (Exception e){
			return "";
		}

		return "";
	}
	
	// ���� �޽������� ù��° text�� �������� �޼ҵ�
	public static String getContentFromMessage(String msgStr) {

		String[] arr = msgStr.split("\\]");
		msgStr =  msgStr.replaceAll("&lt;", "<");
		msgStr =  msgStr.replaceAll("&gt;", ">");
		
		try {
			for (String str : arr) {
			
				String subStr = str.substring(1);
				String[] subArr = subStr.split(";");
		
				if (Integer.parseInt(subArr[0]) == 1) {
					return subArr[1];
				}
			}
		}
		catch (Exception e){
			return "";
		}

		return "";
	}


}
