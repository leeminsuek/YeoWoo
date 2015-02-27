package com.foxrainbxm.base;

import com.foxrainbxm.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;


public class ResizeImageView extends ImageView
{
	Context con;
	boolean flag;
	boolean bitmapYn = true;
	int margin = 10;
	int scale = 1;
	int widthScale = 1;
	int maxHeight = 0;
	public ResizeImageView(Context context)
	{
		super(context);
		con = context;
		flag = true;
		//		this.setBackgroundColor(getResources().getColor(R.color.abs__bright_foreground_holo_dark));
	}

	public ResizeImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		con = context;
		flag = true;
//				this.setBackgroundColor(getResources().getColor(R.color.wallet_dim_foreground_disabled_holo_dark));
	}
	
	public void setScale(int scale) {
		this.scale = scale;
	}

	public void setWidthScale(int scale) {
		this.widthScale = scale;
	}
	public void setMarginValue(int margin) {
		this.margin = margin;
	}
	@Override
	public void setImageBitmap(Bitmap bm) {
		if(bitmapYn) { 
			float screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
			//			screenWidth = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, screenWidth, getResources().getDisplayMetrics());
			
			int margin10 = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, margin, getResources().getDisplayMetrics());
			screenWidth = (screenWidth/widthScale) - (margin10 * 1);
			Bitmap resized = null;

			int width = bm.getWidth();
			int height = bm.getHeight();

			if( screenWidth > width ) {
				float percent = screenWidth / width;

				if(width > height) {
					height = (int) (height * percent);
					width = (int) screenWidth - margin10;
				}
				else {
					height = (int) (height * percent);
					width = (int) screenWidth - margin10;
				}

			}
			else {
				float percent = width / screenWidth;

				if(width > height) {
					height = (int) (height / percent);
					width = (int) screenWidth- margin10;
				}
				else {
					height = (int) (height / percent);
					width = (int) screenWidth- margin10;
				}
			}

			//			width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
			//			height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics());
			resized = Bitmap.createScaledBitmap(bm, width, height, true);
			bitmapYn = false;
			super.setImageBitmap(resized);

		}

	}

	//	@Override
	//	public void setImageBitmap(Bitmap bm) {
	//		
	//			Display display = ((WindowManager) con.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
	//			int screenWidth = display.getWidth();
	//			int margin10 = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
	//			
	//			screenWidth = screenWidth - (margin10 * 2);
	//
	//			
	//			Bitmap resized = null;
	//
	//			int width = bm.getWidth();
	//			int height = bm.getHeight();
	//
	//			if( screenWidth > width ) {
	//				float percent = screenWidth / width;
	//
	//				height = (int) (height * percent);
	//				width = (int) (width * percent);
	//			}
	//			else {
	//				float percent = width / screenWidth;
	//				height = (int) (height * percent);
	//				width = (int) (width / percent);
	//			}
	//			width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
	//			height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics());
	//			resized = Bitmap.createScaledBitmap(bm, width, height, true);
	//			super.setImageBitmap(resized);
	//	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Drawable d = getDrawable();
		if(d != null){			
			int width = d.getIntrinsicWidth();
			int height = d.getIntrinsicHeight();

			int screenWidth =getContext().getResources().getDisplayMetrics().widthPixels;
			//				screenWidth = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, screenWidth, getResources().getDisplayMetrics());
			int margin10 = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, margin, getResources().getDisplayMetrics());
			screenWidth = (screenWidth/widthScale) - (margin10 * scale);

			if(width > screenWidth)
			{
				float percent = width / screenWidth;

				if(width > height) {
					height = (int) (height / percent);
					width = (int) screenWidth - margin10;
				}
				else {
					height = (int) (height / percent);
					width = (int) screenWidth- margin10;
				}
				//						height = (int) (height * percent);
				//						width = (int) (screenWidth / percent);
				setMeasuredDimension(width, height);
			}
			else
			{
				float percent = screenWidth / width;
				//						height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics());

				if(width > height) {
					height = (int) (height * percent);
					width = (int) screenWidth - margin10;
				}
				else {
					height = (int) (height * percent);
					width = (int) screenWidth - margin10;
				}
				//						height = (int) (height * percent);
				//						width = screenWidth;

				setMeasuredDimension(width, height);
			}

			LayoutParams params = getLayoutParams();
			setLayoutParams(params);
		}else{
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}
}