package com.foxrainbxm.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TYoutubeHelper {
	// http://androidsnippets.wordpress.com/2012/10/11/how-to-get-extract-video-id-from-an-youtube-url-in-android-java/
	public static String getVideoIdFromUrl(String youtubeUrl)
	{
		String video_id="";
		if (youtubeUrl != null && youtubeUrl.trim().length() > 0 && youtubeUrl.startsWith("http"))
		{

			String expression = "^.*((youtu.be"+ "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*"; // var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
			CharSequence input = youtubeUrl;
			Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(input);
			if (matcher.matches())
			{
				String groupIndex1 = matcher.group(7);
				if(groupIndex1!=null && groupIndex1.length()==11)
					video_id = groupIndex1;
			}
		}
		return video_id;
	}
	
	/*
	http://www.diaryofaninja.com/blog/2009/09/14/regular-expression-to-find-the-youtube-video-id-from-a-string
	private string GetYouTubeID(string youTubeUrl)
	{
	    //Setup the RegEx Match and give it 
	    Match regexMatch = Regex.Match(youTubeUrl, "^[^v]+v=(.{11}).*", 
	                       RegexOptions.IgnoreCase);
	    if (regexMatch.Success)
	    {
	        return "http://www.youtube.com/v/" + regexMatch.Groups[1].Value + 
	               "&hl=en&fs=1";
	    }
	    return youTubeUrl;
	}
	*/
	
	/*
	http://www.developerfeed.com/how-youtube-videoid-url-android
	public static String getVideoIdFromUrl(String url) {
		 
		 String vid = null;
		 try {
			 int vindex = url.indexOf("v=");
		   int ampIndex = url.indexOf("&", vindex);
		 
		  TLog.d("v= not found " + vindex);
		  if (ampIndex < 0) {
			  TLog.d("& not found " + ampIndex);
		                vid = url.substring(vindex + 2);
		  } else {
			  TLog.d("& found at " + ampIndex);
		        vid = url.substring(vindex + 2, ampIndex);
		  }
		} catch (Exception e) {
			TLog.e( e, "Failed to get the vid " + e.getMessage());
		}
		 
		 TLog.d("vid : " + vid);
		 
		 return vid;
		 
		}
	*/			
}
