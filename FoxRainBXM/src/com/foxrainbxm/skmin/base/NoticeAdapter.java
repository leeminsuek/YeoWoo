package com.foxrainbxm.skmin.base;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.foxrainbxm.R;

public class NoticeAdapter extends BaseAdapter {
	ArrayList<NoticeData> data = new ArrayList<NoticeData>();
	
	public void setData(List<NoticeData> data){
		this.data.clear();
		this.data.addAll(data);
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return data.get(arg0).getPostNo();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NoticeData notice = this.data.get(position);
		View v = convertView;
		if(v == null){
			v = View.inflate(parent.getContext(), R.layout.setting_listitem, null);
		}
		TextView title = (TextView) v.findViewById(R.id.txtTitle);
		TextView time = (TextView) v.findViewById(R.id.txtTime);
		
		title.setText(notice.getTitle());
		time.setText(notice.getTimeAgoInWords());
		
		if(notice.isRead()){
			v.setBackgroundResource(R.drawable.bg_notice_list_n);
		}
		else {
			v.setBackgroundResource(R.drawable.selector_bg_notice_list);
		}
		return v;
	}
}
