package com.foxrainbxm.skmin.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.foxrainbxm.R;
import com.foxrainbxm.WriteFileLog;
import com.foxrainbxm.base.BaseActivity;
import com.foxrainbxm.skmin.base.HttpTask.OnTaskResultListener;
import com.foxrainbxm.skmin.base.NoticeAdapter;
import com.foxrainbxm.skmin.base.NoticeData;
import com.foxrainbxm.util.RecycleUtils;

/**
 * 2014. 10. 5.
 * MinKhun
 *
 * Setting00120
 */
public class Setting00120 extends BaseActivity {

	ListView lstView;
	NoticeAdapter adapter = new NoticeAdapter();
	
	@Override
	protected void onDestroy() {
		try {
			 System.gc();
			 RecycleUtils.recursiveRecycle(getWindow().getDecorView());
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
		super.onDestroy();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.setting00120);
		View backbutton = findViewById(R.id.btnTitleBack);
		backbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		lstView = (ListView) findViewById(R.id.listView1);
		lstView.setDividerHeight(0);
		
		getApi().noticeList(new OnTaskResultListener() {
			@Override
			public void onTaskResult(boolean success, Map<String, Object> result, String message) {
				if(success){
					@SuppressWarnings("unchecked")
					List<Map<String, Object>> list = (List<Map<String, Object>>) result.get("list");
					if(list != null){
						List<NoticeData> arr = new ArrayList<NoticeData>();
						for (int i = 0; i < list.size(); i++) {
							Map<String, Object> data = list.get(i);
							NoticeData notice = new NoticeData();
							notice.setPostNo(Integer.valueOf(data.get("postno").toString()));
							notice.setTitle(data.get("title").toString());
							notice.setPostTime(data.get("posttime").toString());
							notice.setContents(data.get("contents").toString());
							arr.add(notice);
						}
						
						adapter.setData(arr);
						lstView.setAdapter(adapter);
					}
				}
			}
		});
		
		lstView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				NoticeData data = (NoticeData) adapter.getItem(arg2);
				Intent indent = new Intent(Setting00120.this, Setting01116.class);
//				indent.putExtra("title", data.getTitle());
//				indent.putExtra("time", data.getTimeAgoInWords());
//				indent.putExtra("contents", data.getContents());
				indent.putExtra("seq", data.getPostNo());
				startActivity(indent);
			}
		});
		
		boolean flag = getIntent().getBooleanExtra("yn", false);
		if(flag) {
			int postNo = getIntent().getIntExtra("seq", -1);
			
			if(postNo != -1) {
				Intent intent = new Intent(this, Setting01116.class);
				intent.putExtra("seq", postNo);
				startActivity(intent);
			}
		}
	}

}