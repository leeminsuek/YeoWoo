package com.foxrainbxm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;

import com.foxrainbxm.skmin.base.DBManager;
import com.foxrainbxm.skmin.base.SplashActivity;

public class PushProc {

	public void onReceivePushMsg(Context context, String title,String type,String postno, String contents){
		try {
//			Intent intent = null;
			Intent splIntent = new Intent(context, SplashActivity.class);
			splIntent.putExtra("yn", true);
			splIntent.putExtra("postNo", Integer.parseInt(postno));
			splIntent.putExtra("viewtype", type);
			splIntent.putExtra("contents", contents);
			splIntent.putExtra("title", title);
			splIntent.putExtra("time",  System.currentTimeMillis());
			
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, splIntent, PendingIntent.FLAG_UPDATE_CURRENT); 
			Notification.Builder builder = new Notification.Builder(context); 
			builder.setSmallIcon(R.drawable.ic_launcher); 
			builder.setTicker("새로운 " + getTypeTitle(type) + " 글이 등록되었습니다."); 
			builder.setWhen(System.currentTimeMillis()); 
			builder.setNumber(1); 
			builder.setContentTitle(title); 
			builder.setContentText("새로운 " + getTypeTitle(type) + " 글이 등록되었습니다."); 
			builder.setContentIntent(pendingIntent); 
			builder.setAutoCancel(true); 
			builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
			builder.setDefaults(Notification.DEFAULT_VIBRATE);
			//Notification notification = builder.build();//required API level 16 
			Notification notification = builder.getNotification(); 
			int notiId = 1; 
			NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); 
			mNotificationManager.notify(notiId, notification);
			DBManager db = DBManager.getDBManager(context);
			db.insertNotification(Integer.valueOf(postno), Integer.valueOf(type), contents, System.currentTimeMillis(), contents);
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
	}
	
	public String getTypeTitle(String type){
		if(type.equals("2")){
			return "하이힐스토리";
		}else if(type.equals("3")){
			return "고무장갑스토리";
		}else if(type.equals("4")){
			return "아이를부탁해";
		}else if(type.equals("5")){
			return "즐거운육아";
		}else if(type.equals("6")) {
			return "공지사항";
		}
		
		return "";
	}
}
