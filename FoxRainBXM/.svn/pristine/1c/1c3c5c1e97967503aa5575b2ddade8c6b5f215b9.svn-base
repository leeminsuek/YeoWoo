package com.foxrainbxm.jjlim.base;

import android.content.res.AssetManager;
import android.graphics.Typeface;

public class NanumFont {

	private Typeface mTypeface = null;					

	private volatile static NanumFont instance;
	
	public static NanumFont getInstance() {
		
		if (instance == null) {
			synchronized (NanumFont.class) {
				if (instance == null) {
					instance = new NanumFont();
				}
			}
		}
		return instance;
	}
	
	protected NanumFont() {
	
	}
	
	public synchronized void init(AssetManager assets) {
		
		mTypeface = Typeface.createFromAsset(assets, "NanumBarunGothicBold.ttf");
	}

	public boolean isInited() {
		
		return mTypeface != null;
	}
	
	public Typeface getTypeface() {
		
		return mTypeface;
	}
}
