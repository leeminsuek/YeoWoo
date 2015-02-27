package com.foxrainbxm.skmin.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedValues {
	public enum SharedKeys {
		EMAIL,
		PASSWORD,
		LOGIN_TYPE,
		PUSH_KEY,
		NOTI_ALARM,
		ID,
		MYPOINT,
		NICKNAME,
		NAME,
		PROFILEPHOTO,
		AUTO_LOGIN
	}
	private static SharedPreferences getPreference(Context context){
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		return pref;
	}
	
	public static String getSharedValue(Context context, SharedKeys key){
		SharedPreferences pref = getPreference(context);
		return pref.getString(key.name(), "");
	}
	
	public static void setSharedValue(Context context, SharedKeys key, String value){
		SharedPreferences pref = getPreference(context);
        SharedPreferences.Editor editor = pref.edit();
       	editor.putString(key.name(), value);
        editor.commit();
	}
}
