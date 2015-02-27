package com.foxrainbxm.jjlim.libary;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

public class CustomEditText extends EditText{

	private String mHintText = "";
	private CustomEditText mEdit;
	
	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		mEdit = this;
		mHintText = this.getHint().toString();
		this.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
//				String tmp = s.toString();
//				if(tmp.length() == 0 || tmp.equals("")) {
//					mEdit.setHint(mHintText);
//				}
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				String tmp = s.toString();
				if(tmp.length() == 0 || tmp.equals("")) {
					mEdit.setHint(mHintText);
				}
			}
		});
		
		this.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					CustomEditText edit = (CustomEditText) v;
					edit.setHint("");
				}
				else {
					CustomEditText edit = (CustomEditText) v;
					edit.setHint(mHintText);
				}
			}
		});
	}
	
	public void setHingText(String hint) {
		mHintText = hint;
	}

}
