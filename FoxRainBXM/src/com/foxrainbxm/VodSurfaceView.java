package com.foxrainbxm;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;

public class VodSurfaceView extends SurfaceView {
	
	public static int w, h ;
	
	private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
		public boolean onSingleTapUp(MotionEvent paramMotionEvent) {
			Iterator localIterator = VodSurfaceView.this.listeners.iterator();
			while (true) {
				if (!localIterator.hasNext())
					return true;
				((VodSurfaceView.TapListener) localIterator.next())
						.onTap(paramMotionEvent);
			}
		}
	};
	private ArrayList<TapListener> listeners = new ArrayList();
	public VodSurfaceView(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
	}
	
	public void addTapListener(TapListener paramTapListener) {
		this.listeners.add(paramTapListener);
	}

	public boolean onTouchEvent(MotionEvent paramMotionEvent) {
		if (paramMotionEvent.getAction() == 1)
			this.gestureListener.onSingleTapUp(paramMotionEvent);
		return true;
	}

	public void removeTapListener(TapListener paramTapListener) {
		this.listeners.remove(paramTapListener);
	}

	public static abstract interface TapListener {
		public abstract void onTap(MotionEvent paramMotionEvent);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
}