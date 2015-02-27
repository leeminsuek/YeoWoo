package com.foxrainbxm.skmin.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.foxrainbxm.R;
import com.foxrainbxm.skmin.base.SharedValues.SharedKeys;

public class GcmIntentService extends IntentService {
	public static final int NOTIFICATION_ID = 1;

	public GcmIntentService() {
		super("GcmIntentService");
	}

	public Map<String, Object> toMap(JSONObject object) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();

		Iterator<String> keysItr = object.keys();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = object.get(key);

			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			}

			else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			map.put(key, value);
		}
		return map;
	}

	public List<Map<String, Object>> toList(JSONArray array) throws JSONException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < array.length(); i++) {
			Object value = array.get(i);
			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			}

			else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			list.add((Map<String, Object>) value);
		}
		return list;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		/*Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) { // has effect of unparcelling Bundle
			
			 * Filter messages based on message type. Since it is likely that
			 * GCM will be extended in the future with new message types, just
			 * ignore any message types you're not interested in, or that you
			 * don't recognize.
			 
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
				// sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
				// sendNotification("Deleted messages on server: " +
				// extras.toString());
				// If it's a regular GCM message, do some work.
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				String title = intent.getStringExtra("PD_SUBJECT");
				String json = intent.getStringExtra("PD_DICT");
				JSONObject obj;
				try {
					obj = new JSONObject(json);
					Map<String, Object> map = toMap(obj);
					sendNotification(title, map);
				} catch (JSONException e) {
					WriteFileLog.writeException(e);
				}
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);*/
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(String title, Map<String, Object> map) {

		String msg = (String) map.get("contents");
		int postNo = Integer.parseInt(map.get("postNo").toString());
		int type = Integer.parseInt(map.get("type").toString());

		DBManager db = new DBManager(getApplicationContext());
		db.insertNotification(postNo, type, title, new Date().getTime(), msg);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		boolean checked = Boolean.valueOf(SharedValues.getSharedValue(this, SharedKeys.NOTI_ALARM));
		if(checked){
			Intent intent = new Intent(getApplicationContext(), ReceiveActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);
	
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_launcher).setContentTitle(title).setStyle(new NotificationCompat.BigTextStyle().bigText(msg)).setContentText(msg).setAutoCancel(true).setVibrate(new long[] { 0, 500 });
	
			mBuilder.setContentIntent(contentIntent);
			mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		}
	}
}