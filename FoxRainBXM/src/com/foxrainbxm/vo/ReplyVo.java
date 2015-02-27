package com.foxrainbxm.vo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.foxrainbxm.WriteFileLog;

public class ReplyVo {

	public String id;
	public String profilePhoto;
	public int delYn;
	public int replyNo;
	public String replyTime;
	public String replyText;
	public String name;
	public String nickName;
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProfilePhoto() {
		return profilePhoto;
	}

	public void setProfilePhoto(String profilePhoto) {
		this.profilePhoto = profilePhoto;
	}

	public int getDelYn() {
		return delYn;
	}

	public void setDelYn(int delYn) {
		this.delYn = delYn;
	}

	public int getReplyNo() {
		return replyNo;
	}

	public void setReplyNo(int replyNo) {
		this.replyNo = replyNo;
	}

	public String getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}

	public String getReplyText() {
		return replyText;
	}

	public void setReplyText(String replyText) {
		this.replyText = replyText;
	}
	
	public static ArrayList<ReplyVo> LoadParseJson(JSONObject json,ArrayList<ReplyVo> arraylist) {
		JSONArray data = null;
		try {
			data = json.getJSONArray("data");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		if (data != null) {
			try {
				//arraylist.clear();
				
				for (int i = 0; i < data.length(); i++) {
						JSONObject obj = data.getJSONObject(i);
						ReplyVo vo = new ReplyVo();
						vo.setDelYn(obj.getInt("delYn"));
						vo.setId(obj.getString("id"));
						vo.setProfilePhoto(obj.getString("profilePhoto"));
						vo.setReplyNo(obj.getInt("replyNo"));
						vo.setReplyText(obj.getString("replyText"));
						vo.setReplyTime(obj.getString("replyTime"));
						vo.setName(obj.getString("name"));
						vo.setNickName(obj.getString("nickName"));
						arraylist.add(vo);
				}
			} catch (JSONException e) {
				WriteFileLog.writeException(e);
			}

		}
		return arraylist;
	}
}