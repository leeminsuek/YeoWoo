package com.foxrainbxm.vo;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.foxrainbxm.WriteFileLog;


public class TipVo implements Serializable{

	String contents;
	int heart;
	int postNo;
	String id;
	String postTime;
	int reply;
	int scrap;
	String title;
	int type;
	String content;
	String img1;
	String videothum;
	
	
	int viewtype;
	
	String contents2;
	int heart2;
	int postNo2;
	String id2;
	
	String postTime2;
	int reply2;
	int scrap2;
	String title2;
	int type2;
	String content2;
	String img2;
	String videothum2;

	public String getContents2() {
		return contents2;
	}

	public void setContents2(String contents2) {
		this.contents2 = contents2;
	}

	public int getHeart2() {
		return heart2;
	}

	public void setHeart2(int heart2) {
		this.heart2 = heart2;
	}

	public int getPostNo2() {
		return postNo2;
	}

	public void setPostNo2(int postNo2) {
		this.postNo2 = postNo2;
	}

	public String getId2() {
		return id2;
	}

	public void setId2(String id2) {
		this.id2 = id2;
	}

	public String getPostTime2() {
		return postTime2;
	}

	public void setPostTime2(String postTime2) {
		this.postTime2 = postTime2;
	}

	public int getReply2() {
		return reply2;
	}

	public void setReply2(int reply2) {
		this.reply2 = reply2;
	}

	public int getScrap2() {
		return scrap2;
	}

	public void setScrap2(int scrap2) {
		this.scrap2 = scrap2;
	}

	public String getTitle2() {
		return title2;
	}

	public void setTitle2(String title2) {
		this.title2 = title2;
	}

	public int getType2() {
		return type2;
	}

	public void setType2(int type2) {
		this.type2 = type2;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public int getHeart() {
		return heart;
	}

	public void setHeart(int heart) {
		this.heart = heart;
	}

	public int getPostNo() {
		return postNo;
	}

	public void setPostNo(int postNo) {
		this.postNo = postNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPostTime() {
		return postTime;
	}

	public void setPostTime(String postTime) {
		this.postTime = postTime;
	}

	public int getReply() {
		return reply;
	}

	public void setReply(int reply) {
		this.reply = reply;
	}

	public int getScrap() {
		return scrap;
	}

	public void setScrap(int scrap) {
		this.scrap = scrap;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public int getViewtype() {
		return viewtype;
	}

	public void setViewtype(int viewtype) {
		this.viewtype = viewtype;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImg1() {
		return img1;
	}

	public void setImg1(String img1) {
		this.img1 = img1;
	}

	public String getVideothum() {
		return videothum;
	}

	public void setVideothum(String videothum) {
		this.videothum = videothum;
	}

	public String getContent2() {
		return content2;
	}

	public void setContent2(String content2) {
		this.content2 = content2;
	}

	public String getImg2() {
		return img2;
	}

	public void setImg2(String img2) {
		this.img2 = img2;
	}

	public String getVideothum2() {
		return videothum2;
	}

	public void setVideothum2(String videothum2) {
		this.videothum2 = videothum2;
	}
	
	public static ArrayList<TipVo> LoadMoreParseJson(JSONObject json,ArrayList<TipVo> arraylist) {
		JSONObject object;
		JSONArray data = null;
		try {
//			object = new JSONObject(json.toString());
			data = json.getJSONArray("data");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		if (data != null) {
			try {
//				arraylist.clear();
				TipVo vo = new TipVo();
				for (int i = 0; i < data.length(); i++) {
						JSONObject obj = data.getJSONObject(i);
//						if(i == 0){
//							vo.setId(obj.getString("id"));
//							vo.setContents(obj.getString("contents"));
//							vo.setHeart(obj.getInt("heart"));
//							vo.setPostNo(obj.getInt("postNo"));
//							vo.setPostTime(obj.getString("postTime"));
//							vo.setReply(obj.getInt("reply"));
//							vo.setScrap(obj.getInt("scrap"));
//							vo.setTitle(obj.getString("title"));
//							vo.setType(obj.getInt("type"));
//							vo.setViewtype(1);
//							vo = DataParsing(obj.getString("contents"), vo,true);
//							if(data.length() == 1) 
//								arraylist.add(vo);
////							arraylist.add(vo);
//						}else if(i == 1){
//							vo.setId2(obj.getString("id"));
//							vo.setContents2(obj.getString("contents"));
//							vo.setHeart2(obj.getInt("heart"));
//							vo.setPostNo2(obj.getInt("postNo"));
//							vo.setPostTime2(obj.getString("postTime"));
//							vo.setReply2(obj.getInt("reply"));
//							vo.setScrap2(obj.getInt("scrap"));
//							vo.setTitle2(obj.getString("title"));
//							vo.setType2(obj.getInt("type"));
//							vo = DataParsing(obj.getString("contents"), vo,false);
//							arraylist.add(vo);
//						}else{
							TipVo vo2 = new TipVo();
							vo2.setId(obj.getString("id"));
							vo2.setContents(obj.getString("contents"));
							vo2.setHeart(obj.getInt("heart"));
							vo2.setPostNo(obj.getInt("postNo"));
							vo2.setPostTime(obj.getString("postTime"));
							vo2.setReply(obj.getInt("reply"));
							vo2.setScrap(obj.getInt("scrap"));
							vo2.setTitle(obj.getString("title"));
							vo2.setType(obj.getInt("type"));
							vo2.setViewtype(2);
							vo2 = DataParsing(obj.getString("contents"), vo2,true);
							arraylist.add(vo2);
//						}
				}
			} catch (JSONException e) {
				WriteFileLog.writeException(e);
			}

		}
		return arraylist;
	}

	public static ArrayList<TipVo> LoadParseJson(JSONObject json,ArrayList<TipVo> arraylist) {
		JSONObject object;
		JSONArray data = null;
		try {
//			object = new JSONObject(json.toString());
			data = json.getJSONArray("data");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		if (data != null) {
			try {
				arraylist.clear();
				TipVo vo = new TipVo();
				for (int i = 0; i < data.length(); i++) {
						JSONObject obj = data.getJSONObject(i);
						if(i == 0){
							vo.setId(obj.getString("id"));
							vo.setContents(obj.getString("contents"));
							vo.setHeart(obj.getInt("heart"));
							vo.setPostNo(obj.getInt("postNo"));
							vo.setPostTime(obj.getString("postTime"));
							vo.setReply(obj.getInt("reply"));
							vo.setScrap(obj.getInt("scrap"));
							vo.setTitle(obj.getString("title"));
							vo.setType(obj.getInt("type"));
							vo.setViewtype(1);
							vo = DataParsing(obj.getString("contents"), vo,true);
							if(data.length() == 1) 
								arraylist.add(vo);
//							arraylist.add(vo);
						}else if(i == 1){
							vo.setId2(obj.getString("id"));
							vo.setContents2(obj.getString("contents"));
							vo.setHeart2(obj.getInt("heart"));
							vo.setPostNo2(obj.getInt("postNo"));
							vo.setPostTime2(obj.getString("postTime"));
							vo.setReply2(obj.getInt("reply"));
							vo.setScrap2(obj.getInt("scrap"));
							vo.setTitle2(obj.getString("title"));
							vo.setType2(obj.getInt("type"));
							vo = DataParsing(obj.getString("contents"), vo,false);
							arraylist.add(vo);
						}else{
							TipVo vo2 = new TipVo();
							vo2.setId(obj.getString("id"));
							vo2.setContents(obj.getString("contents"));
							vo2.setHeart(obj.getInt("heart"));
							vo2.setPostNo(obj.getInt("postNo"));
							vo2.setPostTime(obj.getString("postTime"));
							vo2.setReply(obj.getInt("reply"));
							vo2.setScrap(obj.getInt("scrap"));
							vo2.setTitle(obj.getString("title"));
							vo2.setType(obj.getInt("type"));
							vo2.setViewtype(2);
							vo2 = DataParsing(obj.getString("contents"), vo2,true);
							arraylist.add(vo2);
						}
				}
			} catch (JSONException e) {
				WriteFileLog.writeException(e);
			}

		}
		return arraylist;
	}
	
	public static TipVo DataParsing(String data,TipVo vo,boolean flag){
		try {
			String[]  values = data.split("]");
			for(int i = 0 ; i < values.length ;i++){
				String[]  values2 = values[i].split(";");
				values2[0] = values2[0].substring(1);
				if(values2[0].equals("1")){
					if(flag){
						vo.setContent(values2[1]);	
					}else{
						vo.setContent2(values2[1]);
					}
				}else if(values2[0].equals("2")){
					if(flag){
						vo.setImg1(values2[1]);	
					}else{
						vo.setImg2(values2[1]);
					}
					break;
				}else if(values2[0].equals("3")){
					if(flag){
						vo.setVideothum(values2[2]);	
					}else{
						vo.setVideothum2(values2[2]);
					}
				}
			}
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
		
		return vo;
	}

}
