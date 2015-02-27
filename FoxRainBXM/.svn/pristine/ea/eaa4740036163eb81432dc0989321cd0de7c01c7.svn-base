package com.foxrainbxm.tab2;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxrainbxm.Common;
import com.foxrainbxm.FoxRainBXM;
import com.foxrainbxm.R;
import com.foxrainbxm.WriteFileLog;
import com.foxrainbxm.base.BaseFragment;
import com.foxrainbxm.jjlim.base.ContentMessageParser;
import com.foxrainbxm.jjlim.base.ContentVO;
import com.foxrainbxm.jjlim.base.HttpContentApi;
import com.foxrainbxm.jjlim.base.HttpContentTask.OnContentTaskResultListener;
import com.foxrainbxm.jjlim.base.MainRefresh;
import com.foxrainbxm.jjlim.base.NanumFont;
import com.foxrainbxm.jjlim.base.TextUtil;
import com.foxrainbxm.jjlim.content.ContentDetailView;
import com.foxrainbxm.skmin.base.Popup1Button;
import com.foxrainbxm.util.Request;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

/*
 * CATEGORY TAB
 */
public class Scenario2_1 extends BaseFragment implements OnRefreshListener, MainRefresh{

	private static Scenario2_1 instance = null;

	public static Scenario2_1 Scenario2 = null;

	public static boolean R_YN = true;

	private View headerView = null;
	public boolean loadingYN;

	private int mPrevPosition = 0;
	private int mScrollY = 0;

	View view = null;
	FoxRainBXM myapp = null;
	SwipeRefreshLayout swipeLayout = null;
	Context mContext;

	int mCatType;					// 카테고리 종류 (1: 직종별인터뷰, 2: 오픈컨텐츠, 3: 관련뉴스)
	int mContentIndex = 0;			// 현재 콘텐츠 페이지 (1페이지는 20개)	

	Typeface mTypeface;

	ArrayList<ContentVO> mTopItems;		// 위쪽 2개 포스트 데이타
	ArrayList<ContentVO> mListItems;	// 아래쪽 나머지 데이타
	boolean mIsContentsEnd = true;		// 마지막 콘텐츠 인지 정보 (더 있는지)

	LinearLayout mTopLeftLayout;
	LinearLayout mTopRightLayout;
	//	LinearLayout mLoadingLayout ;
	
	TextView mMoreButton;
	ListView mListView;
	CustomListAdapter mListAdapter;

	public static Scenario2_1 getInstance(int type) {
		if(instance == null) 
			instance = new Scenario2_1(type);
		return instance;
	}

	public Scenario2_1(int type) {
		super();
		mContext = getBaseActivity();
		mCatType = type;
		mContentIndex = 0;
	}

	public static Scenario2_1 create(int pageNumber) {
		Scenario2_1 fragment = new Scenario2_1(pageNumber);
		Bundle args = new Bundle();
		args.putInt("page", pageNumber);
		fragment.setArguments(args);
		return fragment;
	}
	public void Update(){
		try {
			//			getDataFromServer();	
			onRefresh();
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		try {
			if (view != null) {
				ViewGroup parent = (ViewGroup) view.getParent();
				if (parent != null)
					parent.removeView(view);

			} else {
				view = inflater.inflate(R.layout.scen00014, container, false);
				myapp = (FoxRainBXM) getActivity().getApplicationContext();
				
//				getDataFromServer();
//				getDataFromServer();
//				Update();
			}
			initView();
			getDataFromServer();
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}

		return view;
	}

	@Override
	public void onResume() {
		//		mTopItems.clear();
		//		mListItems.clear();
//				getDataFromServer();
		
		super.onResume();

	}

	@Override
	public void onStart() {
		//		if(FragmentMain.mCurrentFragmentIndex == 1) 
//		Update();
		super.onStart();

	}
	Dialog progress = null;
	public void getPushList(final int type,final  int page){
		try {

			AsyncTask<Void, Void, Void> getDataWorker = new AsyncTask<Void, Void, Void>() {
				JSONObject rgb = null;

				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					try {
						progress = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
						RelativeLayout layout = new RelativeLayout(getActivity());
						layout.setGravity(Gravity.CENTER);
						layout.addView(new ProgressBar(getActivity()), new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
						progress.setContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
						progress.show();

					} catch (Exception e) {
						progress.dismiss();
						WriteFileLog.writeException(e);
					}
				}

				@Override
				protected Void doInBackground(Void... params) {
					try {
						List<NameValuePair> dataList = new ArrayList<NameValuePair>();
						dataList.add(new BasicNameValuePair("type",String.valueOf( type)));
						dataList.add(new BasicNameValuePair("idx",String.valueOf(page)));
						rgb = Request.requestService(getActivity(),"content/category", dataList);

						if(rgb != null ) {
							String resultflag = rgb.getString("result");
							if(resultflag.equals("true")) {

								JSONObject data = null;
								try {
									//									object = new JSONObject(json.toString());
									data = rgb.getJSONObject("data");
								} catch (JSONException e1) {
									e1.printStackTrace();
								}
								mContentIndex ++;
								setMoreDataFromJson(data.toString());
							}else{
								progress.dismiss();
							}
						}else {
							progress.dismiss();
						}

					} catch (Exception e) {
						progress.dismiss();
						WriteFileLog.writeException(e);

					}			
					return null;
				}
				@Override
				protected void onPostExecute(Void result) {
					progress.dismiss();
					try {	

//						getActivity().runOnUiThread(new Runnable() {
//							@Override
//							public void run() {
//								//								mListAdapter.notifyDataSetChanged();
//								mListAdapter.update();
//
////								if(mPrevPosition != 0 && mScrollY != 0)
////									mListView.setSelectionFromTop(mPrevPosition, mScrollY);
//								//								mLoadingLayout.setVisibility(View.GONE);
//								
//							}
//						});
					} catch (Exception e) {
						WriteFileLog.writeException(e);
					} finally {

						//mListAdapter.update();
						Handler mHandler = new Handler();
						mHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								mListAdapter.update();
								swipeLayout.setRefreshing(false);
							}
						}, 1000);
					}				
					super.onPostExecute(result);		
					
				}		
			};
			getDataWorker.execute();
		} catch (Exception e) {
			progress.dismiss();
			WriteFileLog.writeException(e);
		}

	}

	// 뷰 초기화 (데이터 로딩 전)
	private void initView() {
		Log.d("ref3", "init");
		//		instance = getInstance();
		Scenario2 = this;
		headerView = getActivity().getLayoutInflater().inflate(R.layout.scen00014_header, null);

		mTopLeftLayout = (LinearLayout)headerView.findViewById(R.id.contentLeft);
		mTopRightLayout = (LinearLayout)headerView.findViewById(R.id.contentRight);
		mListView = (ListView)view.findViewById(R.id.listView1);
		swipeLayout  = (SwipeRefreshLayout) view.findViewById(R.id.swype1);
		//		mLoadingLayout = (LinearLayout) view.findViewById(R.id.linearLayout_loading);
		swipeLayout.setOnRefreshListener(this);
		////		swipeLayout.setRefreshing(true);
		swipeLayout.setColorScheme(R.color.blue, R.color.purple, R.color.green, R.color.orange);
		// 로딩전에 콘텐츠를 가려줌
		mListView.addHeaderView(headerView);

		mListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int count = totalItemCount - visibleItemCount;

				if(firstVisibleItem >= count && totalItemCount != 0 && loadingYN == false) {
					if(!mIsContentsEnd) {
						loadingYN = true;
						//						mLoadingLayout.setVisibility(View.VISIBLE);
						getPushList(mCatType, mContentIndex + 1);


						/*HttpContentApi httpApi = new HttpContentApi(getActivity());
						httpApi.contentCategory(mCatType, mContentIndex + 1, new OnContentTaskResultListener() {
							@Override
							public void onTaskResult(boolean success, String result) {
								if (success) {							
									mContentIndex ++;
									setMoreDataFromJson(result);
								}
								else {
//									mLoadingLayout.setVisibility(View.GONE);
									new Popup1Button(mContext, "데이타를 가져오지 못했습니다.",null).show(); 
								}

							}
						});*/
					}

				}

			}
		});
		hideView(); 

		// 콘텐츠 변수 초기화
		mTopItems = new ArrayList<ContentVO>();
		mListItems = new ArrayList<ContentVO>();

		// 폰트 초기화
		if (!NanumFont.getInstance().isInited()) {
			NanumFont.getInstance().init(getActivity().getAssets());
		}

		// 이미지 로더 초기화
		mListItems = new ArrayList<ContentVO>();
		mListAdapter = new CustomListAdapter(getActivity(), mListItems);
		mListView.setAdapter(mListAdapter);	
		// 하단 리스트뷰 초기화
//		mListView = (ListView)view.findViewById(R.id.listView1);
		mListView.setFocusable(false);
//		mListView.setExpanded(true);

		// 서버로 부터 Category 콘텐츠 받아옴
		//		getDataFromServer();

	}

	private void showView() {
		//mTopLeftLayout.setVisibility(View.VISIBLE);
		//mTopRightLayout.setVisibility(View.VISIBLE);
//		mListView.setVisibility(View.VISIBLE);
	}

	private void hideView() {
		//mTopLeftLayout.setVisibility(View.INVISIBLE);
		//mTopRightLayout.setVisibility(View.INVISIBLE);
//		mListView.setVisibility(View.INVISIBLE);
	}

	public void onResumeFragment() {
		mCatType = 1;
		getDataFromServer();
	}

	public void getDataFromServer() {
		try {
			//			mLoadingLayout.setVisibility(View.VISIBLE);
			HttpContentApi httpApi = new HttpContentApi(getBaseActivity());
			httpApi.contentCategory(mCatType, mContentIndex, new OnContentTaskResultListener() {
				@Override
				public void onTaskResult(boolean success, String result) {

					if (success) {
						setDataFromJson(result);



						Handler mHandler = new Handler();

						mHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								setView();
								swipeLayout.setRefreshing(false);
								//								mLoadingLayout.setVisibility(View.GONE);
							}
						}, 1000);
					}
					else {
						//						mLoadingLayout.setVisibility(View.GONE);
						new Popup1Button(getBaseActivity(), "데이타를 가져오지 못했습니다.",null).show(); 
					}
				}
			});

		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//		instance = null;
	}

	private void setDataFromJson(String jsonStr) {


		ObjectMapper m = new ObjectMapper();
		try {

			JsonNode rootNode = m.readTree(jsonStr);


			for (JsonNode node : rootNode.path("contents")) {

				ContentVO content = new ContentVO();
				content.setPostNo(node.path("postno").asInt());
				content.setTitle(node.path("title").asText());
				content.setContent(ContentMessageParser.getContentFromMessage(node.path("contents").asText()));
				content.setCoverPhoto(ContentMessageParser.getCoverImageFromMessage(node.path("contents").asText()));

				//				Log.e("TAG", "Image URL :: " + ContentMessageParser.getCoverImageFromMessage(node.path("contents").asText()));
				//				Log.e("TAG", "Message Text :: " + ContentMessageParser.getContentFromMessage(node.path("contents").asText()));

				content.setHeartCount(node.path("c_heart").asInt());
				content.setReplyCount(node.path("c_reply").asInt());
				content.setScrapCount(node.path("c_scrap").asInt());

				if (mTopItems.size() < 2) {
					//					Log.d("IMAGE_TAG", "Category Type :: " + content.getTitle());
					//					Log.d("IMAGE_TAG", "Cover Url  :: " + content.getCoverPhoto());
					mTopItems.add(content);
				}
				else {
					mListItems.add(content);
				}

			}	
			mIsContentsEnd = rootNode.path("contents_end").asBoolean();
			loadingYN = false;
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		} 
	}

	private void setMoreDataFromJson(String jsonStr) {

		ObjectMapper m = new ObjectMapper();
		try {

			JsonNode rootNode = m.readTree(jsonStr);

			for (JsonNode node : rootNode.path("contents")) {

				ContentVO content = new ContentVO();
				content.setPostNo(node.path("postno").asInt());
				content.setTitle(node.path("title").asText());
				content.setCoverPhoto(ContentMessageParser.getCoverImageFromMessage(node.path("contents").asText()));
				content.setContent(ContentMessageParser.getContentFromMessage(node.path("contents").asText()));
				content.setHeartCount(node.path("c_heart").asInt());
				content.setReplyCount(node.path("c_reply").asInt());
				content.setScrapCount(node.path("c_scrap").asInt());
				mIsContentsEnd = rootNode.path("contents_end").asBoolean();

				mListItems.add(content);

			}	
			mIsContentsEnd = rootNode.path("contents_end").asBoolean();
			loadingYN = false;

			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
//					mListAdapter.notifyDataSetChanged();
					mListAdapter.update();
				}
			});

		} catch (Exception e) {
			if(progress != null) progress.dismiss();
			WriteFileLog.writeException(e);
		}

		if (mIsContentsEnd) {
			mMoreButton.setVisibility(View.GONE);
		}






		//		mListView.setSelectionFromTop(mPrevPosition, mScrollY);
		//		mListView.setSelection(mContentIndex);
	}

	private void setView() {

		try {
			if (mTopItems.size() >= 1) {
				LinearLayout layout_t_l = (LinearLayout)headerView.findViewById(R.id.contentLeft);
				final ImageView imageView_t_l = (ImageView)headerView.findViewById(R.id.iv_interview_t_l);
				TextView tv_interview_t_t =  (TextView)headerView.findViewById(R.id.tv_interview_t_t);

				TextView textView_t_l = (TextView)headerView.findViewById(R.id.tv_interview_t_l);
				TextView heartCount_t_l = (TextView)headerView.findViewById(R.id.heart_t_l);
				TextView replyCount_t_l = (TextView)headerView.findViewById(R.id.reply_t_l);
				TextView scrapCount_t_l = (TextView)headerView.findViewById(R.id.scrap_t_l);

				final ContentVO topLeftContent = mTopItems.get(0);
				if (topLeftContent.getCoverPhoto().length() != 0) {
					String urlStr = "";
					tv_interview_t_t.setVisibility(View.GONE);
					imageView_t_l.setVisibility(View.VISIBLE);
					if(topLeftContent.getCoverPhoto().startsWith("http:")) {
						urlStr = topLeftContent.getCoverPhoto();
					}
					else { 
						urlStr = Common.ImageUrl + topLeftContent.getCoverPhoto();
					}
					//String urlStr = HttpContentApi.getContentImageUrl(topLeftContent.getCoverPhoto(), 4);
					myapp.imageloder.displayImage(urlStr, imageView_t_l, myapp.options, new ImageLoadingListener() {
						@Override
						public void onLoadingStarted(String arg0, View arg1) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
							// TODO Auto-generated method stub
							if(arg2.getHeight() != arg2.getWidth()) {
								imageView_t_l.setScaleType(ScaleType.CENTER_CROP);
							}
						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							// TODO Auto-generated method stub
						}
					});
				}
				else {
					tv_interview_t_t.setText(topLeftContent.getContent());
					imageView_t_l.setVisibility(View.GONE);
					tv_interview_t_t.setVisibility(View.VISIBLE);
					//myapp.imageloder.displayImage("temp", imageView_t_l, myapp.options);
				}
				textView_t_l.setTypeface(mTypeface);
				textView_t_l.setText(TextUtil.getTitleArrange(topLeftContent.getTitle()));
				//			TextUtil.applyNewLineStr(textView_t_l);
				heartCount_t_l.setText(String.valueOf(topLeftContent.getHeartCount()));
				replyCount_t_l.setText(String.valueOf(topLeftContent.getReplyCount()));
				scrapCount_t_l.setText(String.valueOf(topLeftContent.getScrapCount()));

				layout_t_l.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						startDetailView(topLeftContent.getPostNo());
					}
				});
			}

			// TopRightView 로드
			if (mTopItems.size() >= 2) {
				LinearLayout layout_t_r = (LinearLayout)headerView.findViewById(R.id.contentRight);
				final ImageView imageView_t_r = (ImageView)headerView.findViewById(R.id.iv_interview_t_r);
				TextView textView_t_r = (TextView)headerView.findViewById(R.id.tv_interview_t_r);
				TextView heartCount_t_r = (TextView)headerView.findViewById(R.id.heart_t_r);
				TextView replyCount_t_r = (TextView)headerView.findViewById(R.id.reply_t_r);
				TextView scrapCount_t_r = (TextView)headerView.findViewById(R.id.scrap_t_r);
				TextView tv_interview_r_t = (TextView)headerView.findViewById(R.id.tv_interview_r_t);

				final ContentVO topRightContent = mTopItems.get(1);
				if (topRightContent.getCoverPhoto().length() != 0) {
					imageView_t_r.setVisibility(View.VISIBLE);
					tv_interview_r_t.setVisibility(View.GONE);
					String urlStr = "";
					if(topRightContent.getCoverPhoto().startsWith("http:")) {
						urlStr = topRightContent.getCoverPhoto();
					}
					else { 
						urlStr = Common.ImageUrl + topRightContent.getCoverPhoto();
					}
					//String urlStr = HttpContentApi.getContentImageUrl(topRightContent.getCoverPhoto(), 4);
					myapp.imageloder.displayImage(urlStr, imageView_t_r, myapp.options, new ImageLoadingListener() {
						@Override
						public void onLoadingStarted(String arg0, View arg1) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
							// TODO Auto-generated method stub
							if(arg2.getHeight() != arg2.getWidth()) {
								imageView_t_r.setScaleType(ScaleType.CENTER_CROP);
							}
						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							// TODO Auto-generated method stub
						}
					});
				}
				else {
					tv_interview_r_t.setText(topRightContent.getContent());
					imageView_t_r.setVisibility(View.GONE);
					tv_interview_r_t.setVisibility(View.VISIBLE);
					//	myapp.imageloder.displayImage("temp", imageView_t_r, myapp.options);
				}
				textView_t_r.setTypeface(mTypeface);
				textView_t_r.setText(TextUtil.getTitleArrange(topRightContent.getTitle()));
				//			TextUtil.applyNewLineStr(textView_t_r);
				heartCount_t_r.setText(String.valueOf(topRightContent.getHeartCount()));
				replyCount_t_r.setText(String.valueOf(topRightContent.getReplyCount()));
				scrapCount_t_r.setText(String.valueOf(topRightContent.getScrapCount()));

				layout_t_r.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						startDetailView(topRightContent.getPostNo());
					}
				});
			}


			// ListView 로드
//			if(mListAdapter == null) {
//				mListAdapter = new CustomListAdapter(getActivity(), mListItems);
//				mListView.setAdapter(mListAdapter);
//			}
//			else {
				//				mListAdapter.notifyDataSetChanged();
				mListAdapter.update();
//				mListView.setSelectionFromTop(mPrevPosition, mScrollY);
//			}

			mListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					try {
						int postNo = mListItems.get(position).getPostNo();
						startDetailView(postNo);	
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			});

//			// 콘텐츠 더보기 이벤트
//			mMoreButton = (TextView)view.findViewById(R.id.more_button);
//			mMoreButton.setOnClickListener(new OnClickListener() {
//
//				FragmentMain context = getBaseActivity();
//				@Override
//				public void onClick(View v) {
//					HttpContentApi httpApi = new HttpContentApi(context);
//					httpApi.contentCategory(mCatType, mContentIndex + 1, new OnContentTaskResultListener() {
//						@Override
//						public void onTaskResult(boolean success, String result) {
//							if (success) {							
//								mContentIndex ++;
//								setMoreDataFromJson(result);
//							}
//							else {
//								if(progress != null) progress.dismiss();
//								new Popup1Button(mContext, "데이타를 가져오지 못했습니다.",null).show(); 
//							}
//
//						}
//					});
//				}
//			});

			// 더보기 버튼 셋팅
			if (!mIsContentsEnd) {
				mMoreButton.setVisibility(View.VISIBLE);
			}
			// 로딩후에 콘텐츠를 보여줌
			showView();
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
		// TopLeftView 로드
	}

	private void startDetailView(int postNo) {

		//		if (postNo == 0) {
		//			return;
		//		}

		Intent intent = new Intent(getActivity(), ContentDetailView.class);
		intent.putExtra("postNo", postNo);
		intent.putExtra("viewtype", mCatType);
		startActivity(intent);
	}

	private class CustomListAdapter extends BaseAdapter {

		private Context mContext;
		private ArrayList<ContentVO> items;
		//private SparseArray<WeakReference<View>> viewArray;


		public CustomListAdapter(Context context, ArrayList<ContentVO> items) {
			super();
			this.mContext = context;
			this.items = items;
			//this.viewArray = new SparseArray<WeakReference<View>>(items.size());
		}
		public void update(){
			//viewArray.clear();
			try {
				notifyDataSetChanged();	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder vh;
			/*if(viewArray != null && viewArray.get(position) != null) {
				convertView = viewArray.get(position).get();
				if(convertView != null){
					return convertView;
				}
			}*/
			/*try{
				convertView = LayoutInflater.from(mContext).inflate(R.layout.scen00014_sub, parent, false);
				TextView titlet = (TextView)convertView.findViewById(R.id.sml_title);
				TextView contentText = (TextView)convertView.findViewById(R.id.sml_content);
				TextView heartCount = (TextView)convertView.findViewById(R.id.sml_heart);
				TextView replyCount = (TextView)convertView.findViewById(R.id.sml_reply);
				TextView scrapCount = (TextView)convertView.findViewById(R.id.sml_scrap);
				//final ImageView coverImage = (ImageView)convertView.findViewById(R.id.sml_image);
				final NetworkImageView coverImage = (NetworkImageView)convertView.findViewById(R.id.sml_image);
				TextView sml_textview = (TextView)convertView.findViewById(R.id.sml_textview);


				String title = String.valueOf(TextUtil.getTitleArrange(items.get(position).getTitle()));
				String content = String.valueOf(TextUtil.getContentArrange(items.get(position).getContent()));

				title = title.replace("\n", "");
				content = content.replace("\n", "");

				titlet.setTypeface(mTypeface);
				titlet.setText(title);
//				vh.title.setTypeface(mTypeface);
				contentText.setText(content);

				heartCount.setText(String.valueOf(items.get(position).getHeartCount()));
				replyCount.setText(String.valueOf(items.get(position).getReplyCount()));
				scrapCount.setText(String.valueOf(items.get(position).getScrapCount()));

				String coverPhoto = items.get(position).getCoverPhoto().toString();
				coverImage.setTag(coverPhoto);


				if (items.get(position).getCoverPhoto().length() != 0) {

				sml_textview.setVisibility(View.GONE);
					coverImage.setVisibility(View.VISIBLE);

					//vh.coverImage.setTag(urlStr);
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							String urlStr = "";
							if(items.get(position).getCoverPhoto().startsWith("http")){
								urlStr = items.get(position).getCoverPhoto();
							}else{
								urlStr = Common.ImageUrl + items.get(position).getCoverPhoto();	
							}
							// TODO Auto-generated method stub
							coverImage.setImageUrl( urlStr, ImageCacheManager.getInstance().getImageLoader());
							myapp.imageloder.displayImage(urlStr, coverImage, myapp.options, new ImageLoadingListener() {
								@Override
								public void onLoadingStarted(String arg0, View arg1) {
								}

								@Override
								public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
								}

								@Override
								public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
									if(arg2.getHeight() != arg2.getWidth()) {
										coverImage.setScaleType(ScaleType.CENTER_CROP);
									}
								}

								@Override
								public void onLoadingCancelled(String arg0, View arg1) {
								}
							});		
						}
					});


					//ImageLoader.getInstance().displayImage
				}
				else {

					sml_textview.setText(items.get(position).getContent());
					sml_textview.setVisibility(View.VISIBLE);
					coverImage.setVisibility(View.GONE);
				}

				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						startDetailView(items.get(position).getPostNo());
					}
				});
			}catch(Exception e){
				WriteFileLog.writeException(e);
			}finally{
				viewArray.put(position, new WeakReference<View>(convertView));
			}*/
			try {
				if (convertView == null) {
					convertView = View.inflate(mContext, R.layout.scen00014_sub, null);

					vh = new ViewHolder();
					vh.title = (TextView)convertView.findViewById(R.id.sml_title);
					vh.content = (TextView)convertView.findViewById(R.id.sml_content);
					vh.heartCount = (TextView)convertView.findViewById(R.id.sml_heart);
					vh.replyCount = (TextView)convertView.findViewById(R.id.sml_reply);
					vh.scrapCount = (TextView)convertView.findViewById(R.id.sml_scrap);
					vh.coverImage = (ImageView)convertView.findViewById(R.id.sml_image);
					vh.sml_textview = (TextView)convertView.findViewById(R.id.sml_textview);

					//vh.title.setTypeface(mTypeface);

					convertView.setTag(vh);
				}
				else {	
					vh = (ViewHolder)convertView.getTag();
				}

				String title = String.valueOf(TextUtil.getTitleArrange(items.get(position).getTitle()));
				String content = String.valueOf(TextUtil.getContentArrange(items.get(position).getContent()));

				title = title.replace("\n", "");
				content = content.replace("\n", "");

				vh.title.setTypeface(mTypeface);
				vh.title.setText(title);
				vh.title.setTypeface(mTypeface);
				vh.content.setText(content);

				vh.heartCount.setText(String.valueOf(items.get(position).getHeartCount()));
				vh.replyCount.setText(String.valueOf(items.get(position).getReplyCount()));
				vh.scrapCount.setText(String.valueOf(items.get(position).getScrapCount()));

				String coverPhoto = items.get(position).getCoverPhoto().toString();
				vh.coverImage.setTag(coverPhoto);

				if (items.get(position).getCoverPhoto().length() != 0) {

					vh.sml_textview.setVisibility(View.GONE);
					vh.coverImage.setVisibility(View.VISIBLE);

					
//					vh.coverImage.setDefaultImageResId(R.drawable.main_ctt_list_bg_n);
//					vh.coverImage.setErrorImageResId(R.drawable.main_ctt_list_bg_n);
//					vh.coverImage.setImageUrl( urlStr, ImageCacheManager.getInstance().getImageLoader());

					//vh.coverImage.setTag(urlStr);

							// TODO Auto-generated method stub
							String urlStr = "";
							if(items.get(position).getCoverPhoto().startsWith("http")){
								urlStr = items.get(position).getCoverPhoto();
							}else{
								urlStr = Common.ImageUrl + items.get(position).getCoverPhoto();	
							}
							myapp.imageloder.displayImage(urlStr, vh.coverImage, myapp.options, new ImageLoadingListener() {
								@Override
								public void onLoadingStarted(String arg0, View arg1) {
								}

								@Override
								public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
								}

								@Override
								public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
									if(arg2.getHeight() != arg2.getWidth()) {
										vh.coverImage.setScaleType(ScaleType.CENTER_CROP);
									}
								}

								@Override
								public void onLoadingCancelled(String arg0, View arg1) {
								}
							});		
					 

					//ImageLoader.getInstance().displayImage
				}
				else {

					vh.sml_textview.setText(items.get(position).getContent());
					vh.sml_textview.setVisibility(View.VISIBLE);
					vh.coverImage.setVisibility(View.GONE);
				}

				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						startDetailView(items.get(position).getPostNo());
					}
				});

			} catch (Exception e) {
				WriteFileLog.writeException(e);
			}

			return convertView;
		}

		class ViewHolder {
			public TextView title; 
			public TextView content;
			public TextView heartCount; 
			public TextView replyCount; 
			public TextView scrapCount; 
			public ImageView coverImage;
			public TextView sml_textview;

		}

	}

	@Override
	public void onRefresh() {
		mTopItems.clear();
		mListItems.clear();
		mContentIndex = 0;

		mPrevPosition = mListView.getFirstVisiblePosition();
		View v = mListView.getChildAt(0);
		if(v != null) {
			mScrollY = (int) v.getTop();
		}
		else {
			mScrollY = 0;
		}

		getDataFromServer();
	}

	//	private void loading(final String path, final ImageView view) {
	//		this.getActivity().runOnUiThread(new Runnable() {
	//			@Override
	//			public void run() {
	//				myapp.imageloder.displayImage(path, view, myapp.options, new ImageLoadingListener() {
	//					@Override
	//					public void onLoadingStarted(String arg0, View arg1) {
	//					}
	//
	//					@Override
	//					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
	//					}
	//
	//					@Override
	//					public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
	//						if(arg2.getHeight() != arg2.getWidth()) {
	//							view.setScaleType(ScaleType.CENTER_CROP);
	//						}
	//					}
	//
	//					@Override
	//					public void onLoadingCancelled(String arg0, View arg1) {
	//					}
	//				});	
	//			}
	//		});
	//	}

	public void updateRow(final int postNo ,final int h, final int r,final int s) {
		
				ContentVO vo  = null;
				int i = 0;
				for(i = 0 ; i < mListItems.size() ; i ++) {
					if(mListItems.get(i).getPostNo() == postNo) {
						vo = mListItems.get(i);
						break;
					}
				}
				if(vo != null) {
					vo.setHeartCount(h);
					vo.setReplyCount(r);
					vo.setScrapCount(s);
					mListItems.set(i, vo);
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							mListAdapter.update();
						}
					});
					
				}
				else {
					int j = 0 ;
					if(vo == null) {
						for(j = 0 ; j < mTopItems.size() ; j++) {
							if(mTopItems.get(j).getPostNo() == postNo) {
								vo = mTopItems.get(j);
								break;
							}
						}
					}
					vo.setHeartCount(h);
					vo.setReplyCount(r);
					vo.setScrapCount(s);
					mTopItems.set(j, vo);
					if (mTopItems.size() >= 1) {
						LinearLayout layout_t_l = (LinearLayout)headerView.findViewById(R.id.contentLeft);
						final ImageView imageView_t_l = (ImageView)headerView.findViewById(R.id.iv_interview_t_l);
						TextView tv_interview_t_t =  (TextView)headerView.findViewById(R.id.tv_interview_t_t);

						TextView textView_t_l = (TextView)headerView.findViewById(R.id.tv_interview_t_l);
						TextView heartCount_t_l = (TextView)headerView.findViewById(R.id.heart_t_l);
						TextView replyCount_t_l = (TextView)headerView.findViewById(R.id.reply_t_l);
						TextView scrapCount_t_l = (TextView)headerView.findViewById(R.id.scrap_t_l);
						
						heartCount_t_l.setText(mTopItems.get(0).getHeartCount()+"");
						replyCount_t_l.setText(mTopItems.get(0).getReplyCount()+"");
						scrapCount_t_l.setText(mTopItems.get(0).getScrapCount()+"");
						
					}

					// TopRightView 로드
					if (mTopItems.size() >= 2) {
						LinearLayout layout_t_r = (LinearLayout)headerView.findViewById(R.id.contentRight);
						final ImageView imageView_t_r = (ImageView)headerView.findViewById(R.id.iv_interview_t_r);
						TextView textView_t_r = (TextView)headerView.findViewById(R.id.tv_interview_t_r);
						TextView heartCount_t_r = (TextView)headerView.findViewById(R.id.heart_t_r);
						TextView replyCount_t_r = (TextView)headerView.findViewById(R.id.reply_t_r);
						TextView scrapCount_t_r = (TextView)headerView.findViewById(R.id.scrap_t_r);
						TextView tv_interview_r_t = (TextView)headerView.findViewById(R.id.tv_interview_r_t);
						
						heartCount_t_r.setText(mTopItems.get(1).getHeartCount()+"");
						replyCount_t_r.setText(mTopItems.get(1).getReplyCount()+"");
						scrapCount_t_r.setText(mTopItems.get(1).getScrapCount()+"");
						
					}
				}
		
	}
	@Override
	public void onRefreshMain() {
		Update();
	}

	//	@Override
	//	public void onRefresh() {
	//		Toast.makeText(getBaseActivity(), "xtx", Toast.LENGTH_SHORT).show();
	//	}

}












