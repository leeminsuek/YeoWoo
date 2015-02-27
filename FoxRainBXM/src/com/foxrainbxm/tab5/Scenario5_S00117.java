
package com.foxrainbxm.tab5;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.brainyxlib.BrDateManager;
import com.brainyxlib.BrIntentManager;
import com.brainyxlib.BrUtilManager;
import com.brainyxlib.Dataparsing;
import com.foxrainbxm.Common;
import com.foxrainbxm.R;
import com.foxrainbxm.WriteFileLog;
import com.foxrainbxm.base.BaseActivity;
import com.foxrainbxm.base.ResizeImageView;
import com.foxrainbxm.base.ResizeVideoImageView;
import com.foxrainbxm.jjlim.libary.CustomEditText;
import com.foxrainbxm.profile.Scen000013;
import com.foxrainbxm.skmin.base.Popup1Button;
import com.foxrainbxm.skmin.base.SharedValues;
import com.foxrainbxm.skmin.base.SharedValues.SharedKeys;
import com.foxrainbxm.tab7.PopupLinkButton;
import com.foxrainbxm.util.Request;
import com.foxrainbxm.vo.ReplyVo;
import com.foxrainbxm.vo.TipDetailVo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class Scenario5_S00117 extends BaseActivity {

	ArrayAdapter<CharSequence> spinnerAdapter;
	ImageView profile_img;
	Button playbtn;
	//	ImageView profile_img;
	TextView de_title,name,regdate,content,heart_count,reply_count,scrap_count;
	CustomEditText replyinput;
	LinearLayout linearLayout_loading;
	ListView reply;
	int postno;
	int mPageCount = 1;
	TipDetailVo TipVo  =null;
	ArrayList<ReplyVo>arraylist = new ArrayList<ReplyVo>();
	ReplyAdapter adapter;
	ArrayList<String>filename = new ArrayList<String>();
	ArrayList<Integer>fileType = new ArrayList<Integer>();
	ArrayList<String> contentFiles = new ArrayList<String>();
	boolean hasmoredata = false;
	boolean isScrap = false;
	RelativeLayout modifylayout;
	TextView mEditbtn,mDelbtn;
	boolean ismodify = false;
	boolean isdataloading = false;
	boolean videoYn = false;
	String videoPath = "";
	String videoThumbPath = "";
	//int mIndex = -1;
	LinearLayout imglayout;
	LinearLayout imgview;
	String message = "";
	ScrollView scrollView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BrUtilManager.getInstance().onActivityNoTitle(this);
		setContentView(R.layout.s00117);
		init();
		//		getPushList();
	}

	@Override
	protected void onDestroy() {
		if(Scenario5_1.getinstance() != null){
			Scenario5_1.getinstance().Update();
		}
		super.onDestroy();
	}

	public void setHeaderView(boolean flag){
		try {

		} catch (Exception e) {
		}
	}

	private void init() {
		Intent intent = getIntent();
		postno = intent.getIntExtra("postno", 0);
		findViewById(R.id.profile_image).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						startActivity(new Intent(getApplicationContext(),
								Scen000013.class));
					}
				});

		findViewById(R.id.title_share).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					PopupLinkButton dialogbtn = null;

					String userid = myApp.getUserInfo().getName();
					if(userid == null || userid.length() == 0){
						userid = myApp.getUserInfo().getId();
					}

					if(filename.size() == 0){
						dialogbtn = new PopupLinkButton(Scenario5_S00117.this	, de_title.getText().toString(), content.getText().toString(),userid);	
						//					}else if(playbtn.getVisibility() == View.VISIBLE){
					}else if(videoYn == true) {
						dialogbtn = new PopupLinkButton(Scenario5_S00117.this	, de_title.getText().toString(), content.getText().toString(),videoThumbPath,videoPath,userid);
					}else if(filename.size() > 0){
						dialogbtn = new PopupLinkButton(Scenario5_S00117.this	, de_title.getText().toString(), content.getText().toString(),filename,userid);
					}
					dialogbtn.show();
				} catch (Exception e) {
					WriteFileLog.writeException(e);
				}
			}
		});
		findViewById(R.id.title_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		findViewById(R.id.replyheart).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					addHeart();
				} catch (Exception e) {
					WriteFileLog.writeException(e);
				}
			}
		});

		findViewById(R.id.replsendbtn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				try {
					if(BrUtilManager.getInstance().getEditTextNullCheck(replyinput)){
						AddReply();
					}else{
						BrUtilManager.getInstance().ShowDialog(Scenario5_S00117.this, getString(R.string.app_name), "댓글을 입력하세요");
					}
				} catch (Exception e) {
					WriteFileLog.writeException(e);
				}
			}
		});
		findViewById(R.id.spinnerBtn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(!isScrap){
					isScrap =! isScrap;
					((TextView)findViewById(R.id.btnscrap)).setVisibility(View.VISIBLE);
				}else{
					isScrap =! isScrap;
					((TextView)findViewById(R.id.btnscrap)).setVisibility(View.GONE);
				}
			}
		});

		findViewById(R.id.btnscrap).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					isScrap =! isScrap;
					((TextView)findViewById(R.id.btnscrap)).setVisibility(View.GONE);
					AddScrap();	
				} catch (Exception e) {
					WriteFileLog.writeException(e);
				}
			}
		});

		replyinput = (CustomEditText)findViewById(R.id.replyinput);
		linearLayout_loading = (LinearLayout)findViewById(R.id.linearLayout_loading);
		reply = (ListView )findViewById(R.id.reply);

		//	hederview= getLayoutInflater().inflate(R.layout.s001117_header, null); //
		playbtn = (Button)findViewById(R.id.playbtn);
		playbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String url = (String)v.getTag();
				if(url.startsWith("http") || url.contains("http")){
					BrIntentManager.getInstance().IntentWebPage(Scenario5_S00117.this, url);	
				}else {
					//BrIntentManager.getInstance().IntentCallMediaPlayer(Scenario5_S00117.this, Common.VIDEO_URL + url);
					BrIntentManager.getInstance().IntentCallMediaPlayer(Scenario5_S00117.this, Common.ImageUrl + url);
					/*Intent intent = new Intent(Scenario5_S00117.this,TestVideoView.class);
					intent.putExtra("vdourl", Common.ImageUrl + url);
					startActivity(intent);*/

				}
			}
		});

		imglayout = (LinearLayout)findViewById(R.id.imglayout);
		imgview = (LinearLayout)findViewById(R.id.imgv);
		imgview.setVisibility(View.GONE);

		profile_img = (ImageView)findViewById(R.id.profile_img);
		de_title = (TextView)findViewById(R.id.de_title);
		name = (TextView)findViewById(R.id.name);
		regdate = (TextView)findViewById(R.id.regdate);
		content = (TextView)findViewById(R.id.content);
		heart_count = (TextView)findViewById(R.id.heart_count);
		reply_count = (TextView)findViewById(R.id.reply_count);
		scrap_count = (TextView)findViewById(R.id.scrap_count);

		scrollView = (ScrollView) findViewById(R.id.scrollView);
		reply.setOnTouchListener(new OnTouchListener() {        //리스트뷰 터취 리스너
			@Override
			public boolean onTouch(View v, MotionEvent event) { 
				scrollView.requestDisallowInterceptTouchEvent(true);    // 리스트뷰에서 터취가되면 스크롤뷰만 움직이게
				return false;
			}
		});
		findViewById(R.id.scrap_options).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!ismodify){
					//reply.scrollBy(0, ListView.FOCUS_DOWN);
					//					scrollToEnd();
					ismodify =! ismodify;
					modifylayout.setVisibility(View.VISIBLE);
				}else{
					ismodify =! ismodify;
					modifylayout.setVisibility(View.GONE);
				}
			}
		});
		modifylayout =(RelativeLayout)findViewById(R.id.modifylayout);
		mEditbtn = (TextView)findViewById(R.id.btnedit);
		mEditbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ismodify =! ismodify;
				modifylayout.setVisibility(View.GONE);
				BrUtilManager.getInstance().ShowDialog2btn(Scenario5_S00117.this, getString(R.string.app_name),"수정하시겠습니까?", new BrUtilManager.dialogclick() {
					@Override
					public void setondialogokclick() {

						Intent intent = new Intent(Scenario5_S00117.this,Scenario5_S01111.class);
						intent.putExtra("modify", true);
						intent.putExtra("title", de_title.getText().toString());
						intent.putExtra("content", content.getText().toString());
						intent.putExtra("postno", postno);
						//TODO 수정하기
						//						contentFiles.clear();
						//						
						//						for(int i = 0 ; i < filename.size() ; i ++ ) { 
						//							contentFiles.put(fileType.get(i), filename.get(i));
						//						}

						ArrayList<String> temp = new ArrayList<String>();

						for(int i = 0 ; i < filename.size() ; i ++) {
							String imageUrl = "";
							if(filename.get(i).startsWith("http:")){
								imageUrl = filename.get(i);
							}else{
								imageUrl = Common.ImageUrl + filename.get(i);
							}
							//							imageUrl = filename.get(i);
							temp.add(imageUrl);
						}

						String type = playbtn.getVisibility() == View.VISIBLE ? "3" : "2";

						intent.putExtra("files", temp);
						intent.putExtra("type", type);
						startActivity(intent);
						finish();
					}
					@Override
					public void setondialocancelkclick() {}
				});
			}
		});
		mDelbtn = (TextView)findViewById(R.id.btndel);
		mDelbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ismodify =! ismodify;
				modifylayout.setVisibility(View.GONE);
				BrUtilManager.getInstance().ShowDialog2btn(Scenario5_S00117.this, getString(R.string.app_name),"삭제하시겠습니까?", new BrUtilManager.dialogclick() {
					@Override
					public void setondialogokclick() {
						DeleteTip();		
					}
					@Override
					public void setondialocancelkclick() {}
				});
			}
		});

		//reply.addHeaderView(hederview); //

		adapter = new ReplyAdapter(this);
		reply.setAdapter(adapter);
		reply.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view,
					int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				try {
					if(hasmoredata && !isdataloading){
						if (firstVisibleItem + visibleItemCount >= totalItemCount) {
							mPageCount = mPageCount + 1;
							getReplyList(false);
						}
					}
				} catch (Exception e) {
					WriteFileLog.writeException(e);
				}
			}
		});


		if(Common.profileBitmap != null) {
			final ImageView profile_image = (ImageView)findViewById(R.id.profile_image);
			profile_image.setImageBitmap(Common.profileBitmap);
		}
		else { 
			final ImageView profile_image = (ImageView)findViewById(R.id.profile_image);
			myApp.imageloder.displayImage(Common.ImageUrl + myApp.getUserInfo().getProfilephoto(), profile_image); 

		}


		/*ImageCache.getInstance().loadAsync(Common.ImageUrl + myApp.getUserInfo().getProfilephoto(), 
				new ImageCallback() {
			@Override
			public void onImageLoaded(Drawable image, String url, String path) {
				ImageCache.setImageFitImageView(profile_image, image);
			}
		}, this);*/
	}

	public void scrollToEnd(){
		scrollView.post(new Runnable() {       
			@Override
			public void run() {
				scrollView.fullScroll(View.FOCUS_DOWN);              
			}

		});

	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getPushList();
		if(Common.profileBitmap != null) {
			final ImageView profile_image = (ImageView)findViewById(R.id.profile_image);
			profile_image.setImageBitmap(Common.profileBitmap);
		}
		else { 
			final ImageView profile_image = (ImageView)findViewById(R.id.profile_image);
			myApp.imageloder.displayImage(Common.ImageUrl + myApp.getUserInfo().getProfilephoto(), profile_image); 

		}
	}

	private void listViewHeightSet(Adapter listAdapter, ListView listView)
	{
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++)
		{
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}


		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	public void DeleteTip(){
		AsyncTask<Void, Void, Void> getDataWorker = new AsyncTask<Void, Void, Void>() {
			JSONObject rgb = null;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				try {
					//	BrUtilManager.getInstance().InputKeybordHidden(Scenario5_S00117.this, replyinput);
					linearLayout_loading.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					WriteFileLog.writeException(e);
				}
			}
			@Override
			protected Void doInBackground(Void... params) {
				try {
					List<NameValuePair> dataList = new ArrayList<NameValuePair>();
					dataList.add(new BasicNameValuePair("postNo",String.valueOf( postno)));
					//  dataList.add(new BasicNameValuePair("id",myApp.getUserInfo().getId()));

					rgb = Request.requestService(Scenario5_S00117.this,Common.DELETETIP, dataList);
				} catch (Exception e) {
					WriteFileLog.writeException(e);

				}			
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				try {	
					if(rgb != null ) {
						String resultflag = rgb.getString("result");
						if(resultflag.equals("true")) {
							BrUtilManager.getInstance().ShowDialog1btn(Scenario5_S00117.this, getString(R.string.app_name),"게시글이 삭제되었습니다.", new BrUtilManager.dialogclick() {
								@Override
								public void setondialogokclick() {
									if(Scenario5_1.getinstance() != null){
										Scenario5_1.getinstance().getPushList();
										finish();
									}
								}

								@Override
								public void setondialocancelkclick() {}
							});

							/*	if(rgb.getString("message")!= null && rgb.getString("message").equals("1")){
									BrUtilManager.getInstance().showToast(Scenario5_S00117.this, "스크랩 되었습니다");
									TipVo.setScrap(TipVo.getScrap() + 1);
								}else if(rgb.getString("message") == null){
									BrUtilManager.getInstance().ShowDialog(Scenario5_S00117.this,getString(R.string.app_name), "잠시후 다시 시도하세요");	
								}else{
									BrUtilManager.getInstance().showToast(Scenario5_S00117.this, "이미 스크랩목록에 저장되어있습니다");
								}*/
						}else {
							BrUtilManager.getInstance().ShowDialog(Scenario5_S00117.this,getString(R.string.app_name), "잠시후 다시 시도하세요");
						}
					}
				} catch (Exception e) {
					WriteFileLog.writeException(e);
				} finally {
					linearLayout_loading.setVisibility(View.GONE);
					//finish();
				}				
				super.onPostExecute(result);		
			}		
		};
		getDataWorker.execute();
	}

	public void DeleteReply(final String replyNumber) {
		AsyncTask<Void, Void, Void> getDataWorker = new AsyncTask<Void, Void, Void>() {
			JSONObject rgb = null;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				try {
					BrUtilManager.getInstance().InputKeybordHidden(Scenario5_S00117.this, replyinput);
					linearLayout_loading.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					WriteFileLog.writeException(e);
				}
			}
			@Override
			protected Void doInBackground(Void... params) {
				try {
					List<NameValuePair> dataList = new ArrayList<NameValuePair>();
					dataList.add(new BasicNameValuePair("replyNo",String.valueOf(replyNumber)));

					rgb = Request.requestService(Scenario5_S00117.this,Common.DELETEREPLY, dataList);
				} catch (Exception e) {
					WriteFileLog.writeException(e);

				}			
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				try {	
					if(rgb != null ) {
						Log.d("TAG", rgb.toString());
						String resultflag = rgb.getString("result");
						if(resultflag.equals("true")) {
							BrUtilManager.getInstance().showToast(Scenario5_S00117.this, "댓글이 삭제되었습니다.");
						}else {
							BrUtilManager.getInstance().ShowDialog(Scenario5_S00117.this,getString(R.string.app_name), "잠시후 다시 시도하세요");
						}					}
				} catch (Exception e) {
					WriteFileLog.writeException(e);
				} finally {
					linearLayout_loading.setVisibility(View.GONE);
					//					runOnUiThread(new Runnable() {
					//						public void run() {
					int cnt = Integer.parseInt(reply_count.getText().toString());
					cnt = cnt - 1;
					//							reply_count.setText(String.valueOf(cnt));
					//						}
					//					});
					TipVo.setReply(cnt);
					getReplyList(true);
				}				
				super.onPostExecute(result);		
			}		
		};
		getDataWorker.execute();
	}

	public void AddScrap(){
		AsyncTask<Void, Void, Void> getDataWorker = new AsyncTask<Void, Void, Void>() {
			JSONObject rgb = null;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				try {
					BrUtilManager.getInstance().InputKeybordHidden(Scenario5_S00117.this, replyinput);
					linearLayout_loading.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					WriteFileLog.writeException(e);
				}
			}
			@Override
			protected Void doInBackground(Void... params) {
				try {
					List<NameValuePair> dataList = new ArrayList<NameValuePair>();
					dataList.add(new BasicNameValuePair("postNo",String.valueOf( postno)));
					dataList.add(new BasicNameValuePair("id",myApp.getUserInfo().getId()));

					rgb = Request.requestService(Scenario5_S00117.this,Common.ADDSCRAP, dataList);
				} catch (Exception e) {
					WriteFileLog.writeException(e);

				}			
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				try {	
					if(rgb != null ) {
						String resultflag = rgb.getString("result");
						if(resultflag.equals("true")) {
							if(rgb.getString("message")!= null && rgb.getString("message").equals("1")){
								BrUtilManager.getInstance().showToast(Scenario5_S00117.this, "스크랩 되었습니다");
								TipVo.setScrap(TipVo.getScrap() + 1);
							}else if(rgb.getString("message") == null){
								BrUtilManager.getInstance().ShowDialog(Scenario5_S00117.this,getString(R.string.app_name), "잠시후 다시 시도하세요");	
							}else{
								BrUtilManager.getInstance().showToast(Scenario5_S00117.this, "이미 스크랩목록에 저장되어있습니다");
							}
						}else {
							BrUtilManager.getInstance().ShowDialog(Scenario5_S00117.this,getString(R.string.app_name), "잠시후 다시 시도하세요");
						}
					}
				} catch (Exception e) {
					WriteFileLog.writeException(e);
				} finally {
					linearLayout_loading.setVisibility(View.GONE);
					SetView();
					scrap_count.setText(String.valueOf(TipVo.getScrap()));

				}				
				super.onPostExecute(result);		
			}		
		};
		getDataWorker.execute();
	}

	public void SetView(){
		try {
			if(TipVo != null){
				/*	Dataparsing paring = new Dataparsing();
				paring.setOnDataCall(new Dataparsing.SetDataCall() {
					@Override
					public void onSetDataCall(int arg0, String arg1, String arg2) {
						if(arg0 == 1){
							setText(arg1, content);
						}else if(arg0 ==2){
							filename.add(arg1);
							//setImage(Common.ImageUrl + arg1, filearea);
							playbtn.setVisibility(View.GONE);
							pager.setVisibility(View.VISIBLE);
						}else if(arg0 == 3){
							filename.add(arg1);
							//setImage(arg2, filearea);
							playbtn.setVisibility(View.VISIBLE);
							playbtn.setTag(arg2);
							pager.setVisibility(View.VISIBLE);
						}
					}
				});

				paring.DataParsing(TipVo.getContents());*/
				/*	ViewPagerThumbnail	viewPagerThumbnail = new ViewPagerThumbnail(Scenario5_S00117.this, pager);
				viewPagerThumbnail.setLpItem(filename);
				pager.setAdapter(viewPagerThumbnail);
				pager.setPageMargin(-80);//-200);
				pager.setAdapter(viewPagerThumbnail);
				OnListPageChangeListener oc = new OnListPageChangeListener(pager);						
				pager.setOnPageChangeListener(oc);	
				pager.setOnTouchListener(onPagerTouchListener);*/


				de_title.setText(TipVo.getTitle());
				regdate.setText(BrDateManager.getInstance().getPeriodStr(TipVo.getPosttime()));
				heart_count.setText(String.valueOf(TipVo.getHeart()));
				reply_count.setText(String.valueOf(TipVo.getReply()));
				scrap_count.setText(String.valueOf(TipVo.getScrap()));
				if(TipVo.getNickName().equals("")) {
					name.setText(TipVo.getId());
				}
				else {
					name.setText(TipVo.getNickName());
				}


				if(TipVo.getPhoto().length() > 0){
					myApp.imageloder.displayImage(Common.ImageUrl + TipVo.getPhoto(), profile_img, myApp.options);	
				}

				if(TipVo.getId().equals(myApp.getUserInfo().getId())){
					findViewById(R.id.scrap_options).setVisibility(View.VISIBLE);
				}else{
					findViewById(R.id.scrap_options).setVisibility(View.GONE);
				}

				if(filename.size() == 0){

					imgview.setVisibility(View.GONE);
					imglayout.setVisibility(View.GONE);

					return;
				}

				imgview.setVisibility(View.VISIBLE);
				imglayout.setVisibility(View.VISIBLE);
				//imglayout.removeAllViews();
				imglayout.removeViewsInLayout(0, imglayout.getChildCount());
				imglayout.removeAllViewsInLayout();

				Log.d("TAG_CON", TipVo.getContents());

				//					//TODO imageview set
				//					for(int i = 0;i<filename.size();i++){
				//						ResizeImageView imgview =  (ResizeImageView)LayoutInflater.from(this).inflate(R.layout.s00114_image, null); //
				////						final ResizeImageView imgview = (ResizeImageView)hederimg.findViewById(R.id.imageview);
				//						if(filename.get(i).startsWith("http:")){
				//							myApp.imageloder.displayImage(filename.get(i), imgview, myApp.options);	
				//						}else{
				//							myApp.imageloder.displayImage(Common.ImageUrl + filename.get(i), imgview, myApp.options);
				//						}
				//						imglayout.addView(imgview);
				//					}
				//				}
				//				else if(type == 3) {
				//TODO imageview set
				for(int i = 0;i<filename.size();i++){
					Log.d("TAG_CON", filename.get(i));
					if(fileType.get(i) == 2) {
						//	ResizeImageView imgview =  (ResizeImageView)LayoutInflater.from(this).inflate(R.layout.s00114_image, null); //
						final RelativeLayout view = (RelativeLayout) getLayoutInflater().inflate(R.layout.s00114_image, null);
						final ResizeImageView imgview = (ResizeImageView)view.findViewById(R.id.content_image);
						final ProgressBar progressbar = (ProgressBar)view.findViewById(R.id.progressbar);

						//						if(filename.get(i).startsWith("http:")){
						//							myApp.imageloder.displayImage(filename.get(i), imgview, myApp.options);	
						//						}else{
						//							myApp.imageloder.displayImage(Common.ImageUrl + filename.get(i), imgview, myApp.options);
						//						}

						//						imglayout.addView(imgview);
						if(filename.get(i).startsWith("http:")){
							//myApp.imageloder.displayImage(filename.get(i), imgview, myApp.options);
							ImageLoader.getInstance().displayImage(filename.get(i), imgview, myApp.options, new ImageLoadingListener() {
								@Override
								public void onLoadingStarted(String arg0, View arg1) {
									progressbar.setVisibility(View.VISIBLE);
								}

								@Override
								public void onLoadingFailed(String arg0, View arg1,
										FailReason arg2) {
									progressbar.setVisibility(View.GONE);
								}

								@Override
								public void onLoadingComplete(String arg0,
										View arg1, Bitmap arg2) {
									progressbar.setVisibility(View.GONE);
									imgview.setImageBitmap(arg2);
								}

								@Override
								public void onLoadingCancelled(String arg0,
										View arg1) {
									progressbar.setVisibility(View.GONE);
								}
							});

						}else{
							//myApp.imageloder.displayImage(Common.ImageUrl + filename.get(i), imgview, myApp.options);
							ImageLoader.getInstance().displayImage(Common.ImageUrl + filename.get(i), imgview,  myApp.options,new ImageLoadingListener() {
								@Override
								public void onLoadingStarted(String arg0, View arg1) {
									progressbar.setVisibility(View.VISIBLE);
								}

								@Override
								public void onLoadingFailed(String arg0, View arg1,
										FailReason arg2) {
									progressbar.setVisibility(View.GONE);
								}

								@Override
								public void onLoadingComplete(String arg0,
										View arg1, Bitmap arg2) {
									progressbar.setVisibility(View.GONE);
									imgview.setImageBitmap(arg2);
								}

								@Override
								public void onLoadingCancelled(String arg0,
										View arg1) {
									progressbar.setVisibility(View.GONE);
								}
							});
						}
						imglayout.addView(view);
					}
					else if(fileType.get(i) == 3) {
						videoYn = true;
						final View view = LayoutInflater.from(this).inflate(R.layout.s00114_video, null);
						final ResizeVideoImageView imgview = (ResizeVideoImageView)view.findViewById(R.id.content_video);
						final Button playBtn = (Button)view.findViewById(R.id.video_play);
						//						final ProgressBar progressbar = (ProgressBar)view.findViewById(R.id.progressbar);

						//						if(filename.get(i).startsWith("http:")){
						//							myApp.imageloder.displayImage(filename.get(i), imgview, myApp.options);	
						//						}else{
						//							myApp.imageloder.displayImage(Common.ImageUrl + filename.get(i), imgview, myApp.options);
						//						}

						//						imglayout.addView(imgview);
						String[] path = filename.get(i).split("::");
						videoThumbPath = path[0];
						videoPath = path[1];
						if(videoThumbPath.startsWith("http:")){
							//myApp.imageloder.displayImage(filename.get(i), imgview, myApp.options);
							ImageLoader.getInstance().displayImage(videoThumbPath, imgview, myApp.options, new ImageLoadingListener() {
								@Override
								public void onLoadingStarted(String arg0, View arg1) {
									//									progressbar.setVisibility(View.VISIBLE);
								}

								@Override
								public void onLoadingFailed(String arg0, View arg1,
										FailReason arg2) {
									//									progressbar.setVisibility(View.GONE);
								}

								@Override
								public void onLoadingComplete(String arg0,
										View arg1, Bitmap arg2) {
									//									progressbar.setVisibility(View.GONE);
									imgview.setImageBitmap(arg2);
								}

								@Override
								public void onLoadingCancelled(String arg0,
										View arg1) {
									//									progressbar.setVisibility(View.GONE);
								}
							});

						}else{
							//myApp.imageloder.displayImage(Common.ImageUrl + filename.get(i), imgview, myApp.options);
							ImageLoader.getInstance().displayImage(Common.ImageUrl + videoThumbPath, imgview,  myApp.options,new ImageLoadingListener() {
								@Override
								public void onLoadingStarted(String arg0, View arg1) {
									//									progressbar.setVisibility(View.VISIBLE);
								}

								@Override
								public void onLoadingFailed(String arg0, View arg1,
										FailReason arg2) {
									//									progressbar.setVisibility(View.GONE);
								}

								@Override
								public void onLoadingComplete(String arg0,
										View arg1, Bitmap arg2) {
									//									progressbar.setVisibility(View.GONE);
									imgview.setImageBitmap(arg2);
								}

								@Override
								public void onLoadingCancelled(String arg0,
										View arg1) {
									//									progressbar.setVisibility(View.GONE);
								}
							});
						}
						//						playbtn.setVisibility(View.VISIBLE)
						playBtn.setTag(videoPath);
						playBtn.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								String url = (String)v.getTag();
								if(url.startsWith("http") || url.contains("http")){
									BrIntentManager.getInstance().IntentWebPage(Scenario5_S00117.this, url);	
								}else {
									//BrIntentManager.getInstance().IntentCallMediaPlayer(Scenario5_S00117.this, Common.VIDEO_URL + url);
									BrIntentManager.getInstance().IntentCallMediaPlayer(Scenario5_S00117.this, Common.ImageUrl + url);
									/*Intent intent = new Intent(Scenario5_S00117.this,TestVideoView.class);
									intent.putExtra("vdourl", Common.ImageUrl + url);
									startActivity(intent);*/

								}

							}
						});
						imglayout.addView(view);
					}
					else {
						RelativeLayout hederimg =  (RelativeLayout)getLayoutInflater().inflate(R.layout.s00117_header_img, null); //
						final com.foxrainbxm.base.ResizeVideoImageView imgview = (ResizeVideoImageView)hederimg.findViewById(R.id.imageview);
						final ProgressBar progressbar = (ProgressBar)hederimg.findViewById(R.id.progressbar);

						if(filename.get(i).startsWith("http:")){
							//	myApp.imageloder.displayImage(filename.get(i), imgview, myApp.options);
							ImageLoader.getInstance().displayImage(filename.get(i), imgview, myApp.options, new ImageLoadingListener() {
								@Override
								public void onLoadingStarted(String arg0, View arg1) {
									progressbar.setVisibility(View.VISIBLE);
								}

								@Override
								public void onLoadingFailed(String arg0, View arg1,
										FailReason arg2) {
									progressbar.setVisibility(View.GONE);
								}

								@Override
								public void onLoadingComplete(String arg0,
										View arg1, Bitmap arg2) {
									progressbar.setVisibility(View.GONE);
								}

								@Override
								public void onLoadingCancelled(String arg0,
										View arg1) {
									progressbar.setVisibility(View.GONE);
								}
							});
						}else{
							//	myApp.imageloder.displayImage(Common.ImageUrl + filename.get(i), imgview, myApp.options);
							ImageLoader.getInstance().displayImage(Common.ImageUrl + filename.get(i), imgview,  myApp.options,new ImageLoadingListener() {
								@Override
								public void onLoadingStarted(String arg0, View arg1) {
									progressbar.setVisibility(View.VISIBLE);
								}

								@Override
								public void onLoadingFailed(String arg0, View arg1,
										FailReason arg2) {
									progressbar.setVisibility(View.GONE);
								}

								@Override
								public void onLoadingComplete(String arg0,
										View arg1, Bitmap arg2) {
									progressbar.setVisibility(View.GONE);
								}

								@Override
								public void onLoadingCancelled(String arg0,
										View arg1) {
									progressbar.setVisibility(View.GONE);
								}
							});
						}
						imglayout.addView(hederimg);
					}

				}
			}


			listViewHeightSet(adapter, reply);

			//			}
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
	}

	public void setImage(){
		if(TipVo != null){
			Dataparsing paring = new Dataparsing();
			filename.clear();
			fileType.clear();
			paring.setOnDataCall(new Dataparsing.SetDataCall() {
				@Override
				public void onSetDataCall(int arg0, String arg1, String arg2) {
					if(arg0 == 1){
						setText(arg1, content);
					}else if(arg0 ==2){
						filename.add(arg1);
						fileType.add(2);
						//						setImage(Common.ImageUrl + arg1, filearea);
						playbtn.setVisibility(View.GONE);
						imglayout.setVisibility(View.VISIBLE);
						imgview.setVisibility(View.VISIBLE);
					}else if(arg0 == 3){
						filename.add(arg2+"::"+arg1);
						fileType.add(3);
						//						setImage(arg2, filearea);
						//						playbtn.setVisibility(View.VISIBLE);
						//						playbtn.setTag(arg1);
						imglayout.setVisibility(View.VISIBLE);
						imgview.setVisibility(View.VISIBLE);
					}
				}
			});

			paring.DataParsing(TipVo.getContents());
			SetView();
		}
	}

	OnTouchListener  onPagerTouchListener = new View.OnTouchListener() {
		boolean isFirst = false;
		boolean isVertical = false;

		int f_x = 0;
		int f_y = 0;

		int c_x = 0;
		int c_y = 0;

		public boolean onTouch(View v, MotionEvent event) {

			if(MotionEvent.ACTION_DOWN == event.getAction()) {
				isFirst = true;
				isVertical = false;


				f_x = (int)event.getX();
				f_y = (int)event.getY();
				v.getParent().requestDisallowInterceptTouchEvent(false);
				////
				/*
        		event.setAction(MotionEvent.ACTION_CANCEL);					
        		v.dispatchTouchEvent(event);
        		listview.dispatchTouchEvent(event);					        		
        		return true;
				 */
			} else if(MotionEvent.ACTION_UP == event.getAction()) {

				c_x = (int)event.getX();
				c_y = (int)event.getY();

				int vX = Math.abs(f_x - c_x);
				int vY = Math.abs(f_y - c_y);

				if(vX < 10 && vY < 10) {
					if(v.getTag() != null) {
						//ImageViewZoom(v);
					}
				}        		
			} else if(MotionEvent.ACTION_MOVE == event.getAction()) {

				c_x = (int)event.getX();
				c_y = (int)event.getY();

				if(isFirst == true) {

					int vX = Math.abs(f_x - c_x);
					int vY = Math.abs(f_y - c_y);

					if(vY > vX) {
						if (vY > 30) {
							isVertical = true;
							v.getParent().requestDisallowInterceptTouchEvent(false);	
							isFirst = false;
						}
					} else {
						if (vX > 30) {
							isVertical = false;
							v.getParent().requestDisallowInterceptTouchEvent(true);
							isFirst = false;
						}
					}
				} else {

				}
			}  

			return false;
		}
	};

	public void AddReply(){
		AsyncTask<Void, Void, Void> getDataWorker = new AsyncTask<Void, Void, Void>() {
			JSONObject rgb = null;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				try {
					BrUtilManager.getInstance().InputKeybordHidden(Scenario5_S00117.this, replyinput);
					linearLayout_loading.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					WriteFileLog.writeException(e);
				}
			}
			@Override
			protected Void doInBackground(Void... params) {
				try {
					List<NameValuePair> dataList = new ArrayList<NameValuePair>();
					dataList.add(new BasicNameValuePair("postNo",String.valueOf( postno)));
					dataList.add(new BasicNameValuePair("id",myApp.getUserInfo().getId()));
					//dataList.add(new BasicNameValuePair("replyText",  BrUtilManager.getInstance().StringEncoding(replyinput.getText().toString())));
					dataList.add(new BasicNameValuePair("replyText",  replyinput.getText().toString()));

					rgb = Request.requestService(Scenario5_S00117.this,Common.ADDREPLY, dataList);
				} catch (Exception e) {
					WriteFileLog.writeException(e);

				}			
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				try {	
					if(rgb != null ) {
						String resultflag = rgb.getString("result");
						if(resultflag.equals("true")) {
							BrUtilManager.getInstance().showToast(Scenario5_S00117.this, "댓글이 등록되었습니다.");
							replyinput.setText("");
							mPageCount = 1;


						}else {
							BrUtilManager.getInstance().ShowDialog(Scenario5_S00117.this,getString(R.string.app_name), "잠시후 다시 시도하세요");
						}
					}
				} catch (Exception e) {
					WriteFileLog.writeException(e);
				} finally {
					linearLayout_loading.setVisibility(View.GONE);
					getReplyList(true);

					//					runOnUiThread(new Runnable() {
					//						public void run() {
					int cnt = Integer.parseInt(reply_count.getText().toString());
					cnt = cnt + 1;
					//							reply_count.setText(String.valueOf(cnt));
					//						}
					//					});

					TipVo.setReply(cnt);
					//	SetView();
				}				
				super.onPostExecute(result);		
			}		
		};
		getDataWorker.execute();
	}

	public void addHeart(){
		AsyncTask<Void, Void, Void> getDataWorker = new AsyncTask<Void, Void, Void>() {
			JSONObject rgb = null;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				try {
					//arraylist.clear();
					linearLayout_loading.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					WriteFileLog.writeException(e);
				}
			}
			@Override
			protected Void doInBackground(Void... params) {
				try {
					List<NameValuePair> dataList = new ArrayList<NameValuePair>();
					dataList.add(new BasicNameValuePair("postNo",String.valueOf( postno)));
					dataList.add(new BasicNameValuePair("id",myApp.getUserInfo().getId()));
					rgb = Request.requestService(Scenario5_S00117.this,Common.HEARTADD, dataList);
				} catch (Exception e) {
					WriteFileLog.writeException(e);

				}			
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				try {	
					if(rgb != null ) {
						String resultflag = rgb.getString("result");
						if(resultflag.equals("true")) {
							String message = rgb.getString("message");
							if(message != null){
								if(message.equals("1")){
									TipVo.setHeart(TipVo.getHeart() + 1);
									((ImageView)findViewById(R.id.replyheart)).setBackgroundResource(R.drawable.ico_list_reply_heart_dw);
									heart_count.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_list_heart_dw, 0, 0, 0);
									//heart_count.setCompoundDrawables(getResources().getDrawable(R.drawable.ico_list_heart_dw), null,  null,  null);
									//((ImageView)hederview.findViewById(R.id.heart_img)).setBackgroundResource(R.drawable.ico_list_heart_dw);
								}else if(message.equals("2")){
									TipVo.setHeart(TipVo.getHeart() - 1);
									heart_count.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_list_heart, 0, 0, 0);
									//heart_count.setCompoundDrawables(getResources().getDrawable(R.drawable.ico_list_heart), null,  null,  null);
									((ImageView)findViewById(R.id.replyheart)).setBackgroundResource(R.drawable.ico_list_reply_heart);
									//((ImageView)hederview.findViewById(R.id.heart_img)).setBackgroundResource(R.drawable.ico_list_heart);
								}
							}else {
								BrUtilManager.getInstance().ShowDialog(Scenario5_S00117.this,getString(R.string.app_name), "잠시후 다시시도해주세요");
							}
						}else {
							BrUtilManager.getInstance().ShowDialog(Scenario5_S00117.this,getString(R.string.app_name), "잠시후 다시시도해주세요");
						}
					}
				} catch (Exception e) {
					WriteFileLog.writeException(e);
				} finally {
					linearLayout_loading.setVisibility(View.GONE);
					SetView();
				}				
				super.onPostExecute(result);		
			}		
		};
		getDataWorker.execute();
	}
	public void getPushList(){
		AsyncTask<Void, Void, Void> getDataWorker = new AsyncTask<Void, Void, Void>() {
			JSONObject rgb = null;
			JSONObject rgb2 = null;
			boolean flag = false;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				try {
					//arraylist.clear();
					linearLayout_loading.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					WriteFileLog.writeException(e);
				}
			}

			@Override
			protected Void doInBackground(Void... params) {
				try {
					List<NameValuePair> dataList = new ArrayList<NameValuePair>();
					dataList.add(new BasicNameValuePair("postNo",String.valueOf( postno)));
					rgb = Request.requestService(Scenario5_S00117.this,Common.TIPDETAIL, dataList);

					List<NameValuePair> dataList2 = new ArrayList<NameValuePair>();
					dataList2.add(new BasicNameValuePair("postNo",String.valueOf( postno)));
					dataList2.add(new BasicNameValuePair("id",myApp.getUserInfo().getId()));
					rgb2 = Request.requestService(Scenario5_S00117.this,Common.ISHEART, dataList2);

				} catch (Exception e) {
					WriteFileLog.writeException(e);

				}			
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				try {	
					if(rgb != null ) {
						String resultflag = rgb.getString("result");
						if(resultflag.equals("true")) {
							TipVo = new TipDetailVo();
							String data = rgb.getString("data");
							
							JSONObject obj = new JSONObject(data);
							
							if( obj.length() < 1) {
								flag = false;
//								new Popup1Button(Scenario5_S00117.this, "삭제된 게시글입니다.", new android.content.DialogInterface.OnClickListener() {
//
//									@Override
//									public void onClick(DialogInterface dialog, int which) {
//										linearLayout_loading.setVisibility(View.GONE);
//										finish();
//
//									}
//								})
//								.show();
							}
							else {
								flag = true;
								//JSONObject obj =  data.getJSONObject(0);
								TipVo.setId(obj.getString("id"));
								TipVo.setNickName(obj.getString("nickName"));
								TipVo.setContents(obj.getString("contents"));
								TipVo.setHeart(obj.getInt("heart"));
								TipVo.setPostno(obj.getInt("postNo"));
								TipVo.setPosttime(obj.getString("postTime"));
								TipVo.setReply(obj.getInt("reply"));
								TipVo.setScrap(obj.getInt("scrap"));
								TipVo.setTitle(obj.getString("title"));
								TipVo.setType(obj.getInt("type"));
								TipVo.setPhoto(obj.getString("profilePhoto"));
								setImage();
							}
							
						}
						else {
//							new Popup1Button(Scenario5_S00117.this, "삭제된 게시글입니다.", new android.content.DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog, int which) {
//									linearLayout_loading.setVisibility(View.GONE);
//									finish();
//
//								}
//							})
//							.show();
						}
					}
					if(rgb2 != null && flag == true){
						String resultflag = rgb2.getString("result");
						if(resultflag.equals("true")) {
							String message = rgb2.getString("message");
							if(message != null){
								if(message.equals("1")){
									heart_count.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_list_heart_dw, 0, 0, 0);
									//heart_count.setCompoundDrawables(getResources().getDrawable(R.drawable.ico_list_heart_dw), null,  null,  null);
									//((ImageView)hederview.findViewById(R.id.heart_img)).setBackgroundResource(R.drawable.ico_list_heart_dw);
									((ImageView)findViewById(R.id.replyheart)).setBackgroundResource(R.drawable.ico_list_reply_heart_dw);
								}else if(message.equals("2")){
									heart_count.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_list_heart, 0, 0, 0);
									//heart_count.setCompoundDrawables(getResources().getDrawable(R.drawable.ico_list_heart),  null,  null,  null);
									//((ImageView)hederview.findViewById(R.id.heart_img)).setBackgroundResource(R.drawable.ico_list_heart);
									((ImageView)findViewById(R.id.replyheart)).setBackgroundResource(R.drawable.ico_list_reply_heart);
								}
							}
						}
						else {
//							new Popup1Button(Scenario5_S00117.this, "삭제된 게시글입니다.", new android.content.DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog, int which) {
//									linearLayout_loading.setVisibility(View.GONE);
//									finish();
//
//								}
//							})
//							.show();
						}
					}

					getReplyList(true);
				} catch (Exception e) {
					WriteFileLog.writeException(e);
				} finally {
					linearLayout_loading.setVisibility(View.GONE);
					Handler mHandler = new Handler();
					
					if(flag == false) {
						new Popup1Button(Scenario5_S00117.this, "삭제된 게시글입니다.", new android.content.DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								linearLayout_loading.setVisibility(View.GONE);
								finish();

							}
						})
						.show();
					}

					/*mHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							//refreshLayout.setRefreshing(false);
							//adapter.notifyDataSetChanged();
						}

					}, 1000);*/
				}				
				super.onPostExecute(result);		
			}		
		};
		getDataWorker.execute();
	}

	public void getReplyList(final boolean flag){
		AsyncTask<Void, Void, Void> getDataWorker = new AsyncTask<Void, Void, Void>() {
			JSONObject rgb = null;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				try {
					isdataloading = true;
					if(flag){
						arraylist.clear();
					}
					linearLayout_loading.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					WriteFileLog.writeException(e);
				}
			}
			@Override
			protected Void doInBackground(Void... params) {
				try {
					List<NameValuePair> dataList = new ArrayList<NameValuePair>();
					dataList.add(new BasicNameValuePair("postNo",String.valueOf( postno)));
					dataList.add(new BasicNameValuePair("page",String.valueOf(mPageCount)));
					dataList.add(new BasicNameValuePair("id",myApp.getUserInfo().getId()));
					rgb = Request.requestService(Scenario5_S00117.this,Common.DETAILREPLY, dataList);
				} catch (Exception e) {
					WriteFileLog.writeException(e);

				}			
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				try {	
					if(rgb != null ) {
						String resultflag = rgb.getString("result");
						if(resultflag.equals("true")) {
							arraylist = ReplyVo.LoadParseJson(rgb, arraylist);
						}
					}
				} catch (Exception e) {
					WriteFileLog.writeException(e);
				} finally {
					if(arraylist.size() == mPageCount *  10 ){
						hasmoredata = true;
					}else{
						hasmoredata = false;
					}

					linearLayout_loading.setVisibility(View.GONE);
					adapter.notifyDataSetChanged();
					isScrap =  true;
					if(flag){
						setImage();	
					}
					isdataloading =false;
				}				
				super.onPostExecute(result);		
			}		
		};
		getDataWorker.execute();
	}

	class ReplyAdapter extends ArrayAdapter<ReplyVo>{
		public Context mContext;

		public ReplyAdapter(Context context) {
			super(context, R.layout.s00117_item,arraylist);
			this.mContext = context;
		}

		@Override
		public int getViewTypeCount() {
			return 1;
		}

		public class ViewHolder{
			ImageView profileimg;
			TextView regtime;
			TextView title;
			TextView content;
			ImageButton delBtn;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			final ReplyVo data = arraylist.get(position);
			if (convertView == null) {
				LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = layoutInflater.inflate(R.layout.s00117_item,null);
				holder = new ViewHolder();
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (data != null ) {

				holder.profileimg = (ImageView)convertView.findViewById(R.id.profileimg);
				holder.regtime = (TextView)convertView.findViewById(R.id.regtime);
				holder.title = (TextView)convertView.findViewById(R.id.title);
				holder.content = (TextView)convertView.findViewById(R.id.content);
				holder.delBtn = (ImageButton) convertView.findViewById(R.id.btn_list_delete);

				//if(data.getProfilePhoto() != null && data.geㅉtProfilePhoto().length()> 5){
				//				setImage(Common.ImageUrl + data.getProfilePhoto(), holder.profileimg, myApp.options);

				//				if (data.getProfilePhoto().length() != 0) {
				//					String urlStr = Common.ImageUrl+ data.getProfilePhoto();
				//					ImageLoader.getInstance().displayImage(urlStr, holder.profileimg);
				//				} else {
				//					holder.profileimg.setImageResource(R.drawable.img_reply_prf_n);
				//				}

				if (data.getProfilePhoto().length() != 0) {
					String urlStr = Common.ImageUrl+ data.getProfilePhoto();
					ImageLoader.getInstance().displayImage(urlStr, holder.profileimg);
				} else {
					ImageLoader.getInstance().displayImage("", holder.profileimg, new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String arg0, View arg1) {
							Log.e("", "");
						}

						@Override
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
							holder.profileimg.setImageResource(R.drawable.img_reply_prf_n);
						}

						@Override
						public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
							Log.e("", "");
							if(arg2 == null){
								holder.profileimg.setImageResource(R.drawable.img_reply_prf_n);
							}
						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							holder.profileimg.setImageResource(R.drawable.img_reply_prf_n);
						}
					});
					//ImageLoader.getInstance().displayImage("", vh.photo);
					//
				}
				//	}else{
				//	setImage("", holder.profileimg);
				///	}

				if(data.getNickName().equals("")) {
					holder.title.setText(data.getName());
				}
				else {
					holder.title.setText(data.getNickName());
				}


				if(data.getId().equals(SharedValues.getSharedValue(mContext, SharedKeys.ID))) {
					holder.delBtn.setVisibility(View.VISIBLE);
					holder.delBtn.setTag(data.getReplyNo());
					holder.delBtn.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							final int seq = (Integer)v.getTag();
							////							int seq =  Integer.valueOf((String)v.getTag()) ;
							////							BrUtilManager.getInstance().ShowDialog1btn(mContext, "안내", "\, callback)" +
							////									"
							//							DeleteReply(String.valueOf(seq));

							BrUtilManager.getInstance().ShowDialog2btn(mContext, "댓글삭제", "댓글을 삭제하시겠습니까?",new BrUtilManager.dialogclick() {							
								@Override
								public void setondialogokclick() {
									DeleteReply(String.valueOf(seq));
								}

								@Override
								public void setondialocancelkclick() {
									// TODO Auto-generated method stub

								}
							});
						}
					});

					//					holder.delBtn.setVisibility(View.VISIBLE);
					//					holder.delBtn.setOnClickListener(new OnClickListener() {
					//						@Override
					//						public void onClick(View v) {
					//							DeleteReply(String.valueOf(data.getReplyNo()));
					//						}
					//					});
				}
				else {
					holder.delBtn.setVisibility(View.GONE);
				}


				//				holder.title.setText(data.getId());
				holder.content.setText(data.getReplyText());

				holder.regtime.setText(BrDateManager.getInstance().getPeriodStr(data.getReplyTime()));
			}
			return convertView;
		}
	}
	public void setText(String text, TextView textview){
		textview.setText(text);
	}
	public void setImage(String imgurl,ImageView view, DisplayImageOptions option){
		myApp.imageloder.displayImage(imgurl,
				view, option, new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String arg0, View arg1) {
			}

			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {

			}

			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
			}

			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
			}
		});
	}

}
