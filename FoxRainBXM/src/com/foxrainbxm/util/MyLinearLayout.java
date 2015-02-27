package com.foxrainbxm.util;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class MyLinearLayout extends LinearLayout {
	public final static float BIG_SCALE = 1.0f;//1.0f;
	private float scale = BIG_SCALE;
	int position = 0;
	int ltype = 0;
	
	public MyLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyLinearLayout(Context context) {
		super(context);
	}

	public void setScaleBoth(float scale, int position, int ltype)
	{
		this.scale = scale;
		this.position = position;
		this.ltype = ltype;
		this.invalidate(); 	// If you want to see the scale every time you set
							// scale you need to have this line here, 
							// invalidate() function will call onDraw(Canvas)
							// to redraw the view for you
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// The main mechanism to display scale animation, you can customize it
		// as your needs
		//int w = this.getWidth();
		//int h = this.getHeight();
		//canvas.scale(scale, scale, w/2, h/2);
		
		
		super.onDraw(canvas);
	}
}
