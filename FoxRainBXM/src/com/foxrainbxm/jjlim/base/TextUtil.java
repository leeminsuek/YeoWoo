package com.foxrainbxm.jjlim.base;

import android.R.integer;
import android.graphics.Paint;
import android.widget.TextView;

public class TextUtil {

	public static String getTitleArrange(String txt) {
		
		String aTxt = txt;
//		
//		if (txt.length() > 20) {
//			aTxt = txt.substring(0, 19) + "...";
//		}
//		if (txt.length() > 10) {
//			aTxt = aTxt.substring(0, 10) + "\n" + aTxt.substring(10);
//		}
		
		return aTxt;
	}

	public static String getContentArrange(String txt) {
		
		String aTxt = txt;
//		
//		if (txt.length() > 30) {
//			aTxt = txt.substring(0, 29) + "...";
//		}
//		if (txt.length() > 15) {
//			aTxt = aTxt.substring(0, 15) + "\n" + aTxt.substring(15);
//		}
		
		return aTxt;
	}
	
	public static void applyNewLineStr(TextView textview) {
		Paint paint = textview.getPaint();
		String text = (String) textview.getText();
		
		int maxLine = 2;
		int frameWidth = textview.getMeasuredWidth();
		int startIndex = 0;
		int endIndex = paint.breakText(text, true, frameWidth, null);
		String save = text.substring(startIndex, endIndex);
		
		int lines = 0;
		
		while(true) {
			
			startIndex = endIndex;
			
			text = text.substring(startIndex);
			
			if(text.length() == 0 ) break;
			else lines++;
			
			
			if(lines == maxLine) {
				save = save.substring(0,save.length()-2) + "...";
				break;
			}
			
			endIndex = paint.breakText(text, true, frameWidth, null);
			
			save += "\n" + text.substring(0, endIndex);
			
		}
		
		textview.setText(save);
	}
}
