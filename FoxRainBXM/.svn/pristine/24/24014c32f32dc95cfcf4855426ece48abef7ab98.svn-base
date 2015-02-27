package com.foxrainbxm.jjlim.libary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ListView;

/*
 * 높이가 동적으로 변경되는 ListView
 */
public class ExpandableHeightListView extends ListView {

	boolean expanded = false;
	private boolean enableScrolling = true;
	
	public ExpandableHeightListView(Context context) {
		super(context);
	}

	public ExpandableHeightListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public ExpandableHeightListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public boolean isExpanded() {
		return expanded;
	}
	
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (isExpanded()) {
			int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
			super.onMeasure(widthMeasureSpec, expandSpec);
			
			ViewGroup.LayoutParams params = getLayoutParams();
			params.height = getMeasuredHeight();
		}
		else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	public boolean isEnableScrolling() {
		return enableScrolling;
	}

	public void setEnableScrolling(boolean enableScrolling) {
		this.enableScrolling = enableScrolling;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

//		if (isEnableScrolling()) {
//			return super.onInterceptTouchEvent(ev);
//		} else {
//			return false;
//		}
		
		if (!isEnableScrolling()) {
			getParent().requestDisallowInterceptTouchEvent(true);  
		}
		return super.onInterceptTouchEvent(ev);
	}

}
