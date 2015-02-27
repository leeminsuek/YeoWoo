package com.foxrainbxm.skmin.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.foxrainbxm.R;

public class Popup2Button extends Dialog implements android.view.View.OnClickListener{
	OnClickListener listener;
	
	public Popup2Button(Context context, String message, OnClickListener l) {
		super(context);
		listener = l;
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.popup2button);
		
		TextView txtMessage = (TextView) findViewById(R.id.txtMessage);
		txtMessage.setText(message);
		
		Button btnPopup0 = (Button) findViewById(R.id.btnPopup0);
		Button btnPopup1 = (Button) findViewById(R.id.btnPopup1);
		
		btnPopup0.setOnClickListener(this);
		btnPopup1.setOnClickListener(this);
	}
	
	public void setPopupButton(int index, String text){
		switch (index) {
		case 0:
			Button btnPopup0 = (Button) findViewById(R.id.btnPopup0);
			btnPopup0.setText(text);
			break;
		case 1:
			Button btnPopup1 = (Button) findViewById(R.id.btnPopup1);
			btnPopup1.setText(text);
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnPopup0:
			if(listener != null)
				listener.onClick(this, 0);
			break;
		case R.id.btnPopup1:
			if(listener != null)
				listener.onClick(this, 1);
			break;
		default:
			break;
		}
		
		dismiss();
	}

}
