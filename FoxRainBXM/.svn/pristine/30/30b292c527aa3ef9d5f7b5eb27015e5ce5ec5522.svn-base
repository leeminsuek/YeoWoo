package com.foxrainbxm;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.VideoView;

public class CVideoView extends VideoView {

public CVideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CVideoView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CVideoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heigthMeasureSpec) {
		DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
		if (displayMetrics != null) {
			setMeasuredDimension(displayMetrics.widthPixels,
					displayMetrics.heightPixels);
		}
	}
}
