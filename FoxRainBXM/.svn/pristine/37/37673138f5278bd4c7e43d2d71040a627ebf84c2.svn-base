package com.foxrainbxm.util;

import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class RecycleUtils {
	private RecycleUtils(){};
	public static void recursiveRecycle(View root) {
		if (root == null)
			return;
		root.setBackgroundDrawable(null);
		if (root instanceof ViewGroup) {
			ViewGroup group = (ViewGroup)root;
			int count = group.getChildCount();
			for (int i = 0; i < count; i++) {
				recursiveRecycle(group.getChildAt(i));
			}
			//adapterview not recycle
			if (!(root instanceof AdapterView)) {
				group.removeAllViews();
			}
		}
		
		if (root instanceof FrameLayout
				 || root instanceof LinearLayout) {
			ViewGroup group = (ViewGroup)root;
			int count = group.getChildCount();
			for (int i = 0; i < count; i++) {
				recursiveRecycle(group.getChildAt(i));
			}
			//adapterview not recycle
			if (!(root instanceof AdapterView)) {
				group.removeAllViews();
			}
		}
		
		//image recycle
		if (root instanceof ImageView) {
//			((BitmapDrawable)((ImageView)root).getDrawable()).getBitmap().recycle();
			((ImageView)root).setImageDrawable(null);
		
		}
		
		if(root instanceof Button) {
		}
		
		if(root instanceof ImageButton) {
//			((BitmapDrawable)((ImageButton)root).getDrawable()).getBitmap().recycle();
		}
		
		//webview recycle
		if(root instanceof WebView) {
			((WebView)root).destroy();
		}

		root = null;
		return;
	}
}