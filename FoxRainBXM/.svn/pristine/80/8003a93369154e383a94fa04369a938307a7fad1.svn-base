package com.foxrainbxm.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Patterns;

public class TUrlHelper {
	public static boolean isValidUrl(String url) {
		url = url.toLowerCase();
	    Pattern p = Patterns.WEB_URL;
	    Matcher m = p.matcher(url);
	    if(m.matches())
	        return true;
	    else
	    return false;
	}
}
