package com.foxrainbxm.skmin.setting;

import java.util.Date;

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
import com.foxrainbxm.jjlim.content.ContentDetailView;
import com.foxrainbxm.skmin.base.DBManager;
import com.foxrainbxm.skmin.base.NoticeAdapter;
import com.foxrainbxm.skmin.base.NoticeData;
import com.foxrainbxm.tab5.Scenario5_S00117;
import com.foxrainbxm.util.RecycleUtils;

/**
 * 2014. 10. 5.
 * MinKhun
 *
 * Setting00119
 */
public class Setting00119 extends BaseActivity {

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

		setContentView(R.layout.setting00119);
		View backbutton = findViewById(R.id.btnTitleBack);
		backbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		lstView = (ListView) findViewById(R.id.listView1);
		lstView.setDividerHeight(0);
		//		String[] menus = {"직종별컨텐츠", "오픈컨텐츠", "관련뉴스", "나만의꿀팁"};
		//*메뉴이름- 홈, 직종별컨텐츠, 오픈컨텐츠, 관련뉴스, 나만의꿀팁
		DBManager db = DBManager.getDBManager(this);
		long time = new Date().getTime();
		db.removeNotificationBefore(time - 1209600000);//
		adapter.setData(db.selectData());
		lstView.setAdapter(adapter);

		lstView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				NoticeData data = (NoticeData) adapter.getItem(arg2);
				DBManager db = DBManager.getDBManager(getApplicationContext());
				db.updateReadNotification(data.get_id());

				//1=홈,2=오픈컨텐츠,3=관련뉴스,4=나만의꿀팁,5=About 여우비,6=공지사항
				int type = data.getType();

				if(type == 5) {
					int postNo = data.getPostNo();
					Intent intent = new Intent(Setting00119.this,Scenario5_S00117.class);
					intent.putExtra("postno", postNo);
					startActivity(intent);

				}
				else if( type == 6){ 
					String title = data.getTitle();
					String time = data.getTimeAgoInWords();
					String contents = data.getContents();
					int postNo = data.getPostNo();
					Intent intent = new Intent(Setting00119.this, Setting01116.class);
					intent.putExtra("seq", postNo);
					startActivity(intent);
				}
				//				else if( type == 5) {
				//					
				//				}
				else {
					int postNo = data.getPostNo();
					Intent intent = new Intent(Setting00119.this, ContentDetailView.class);
					intent.putExtra("postNo", postNo);
					intent.putExtra("viewtype", (type-1));
					startActivity(intent);
				}


				//				Toast.makeText(getBaseContext(), "menu type : " + type, Toast.LENGTH_SHORT).show();

				adapter.setData(db.selectData());
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		adapter.notifyDataSetChanged();
	}
}
