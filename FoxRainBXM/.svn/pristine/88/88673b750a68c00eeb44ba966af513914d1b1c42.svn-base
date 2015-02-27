package com.foxrainbxm.skmin.base;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.foxrainbxm.WriteFileLog;

public class NoticeData {
	int _id;
	int postNo;
	int type;
	String title;
	String postTime;
	String contents;
	boolean read;
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public int getPostNo() {
		return postNo;
	}
	public void setPostNo(int postNo) {
		this.postNo = postNo;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPostTime() {
		return postTime;
	}
	public void setPostTime(String postTime) {
		this.postTime = postTime;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	
	public String getTimeAgoInWords() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
	    Date now = new Date();
	    Date from = null;
		try {
			from = sdf.parse(postTime);
		} catch (ParseException e) {
			WriteFileLog.writeException(e);
		}
	    long difference = (now.getTime() - from.getTime()) / 1000;

	    if ( difference < 60) {
	        return difference + "초 전";
	    } else if ( difference < 3600) {
	    	int min = (int) (difference / 60);
	        return min + "분 전";
	    } else if ( difference < 86400) {
	    	int hour = (int) (difference / 3600);
	        return hour + "시간 전";
	    } else {
	        return postTime.substring(0, 10);
	    }
	}
	public boolean isRead() {
		return read;
	}
	public void setRead(boolean read) {
		this.read = read;
	}
}
