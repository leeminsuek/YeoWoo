package com.foxrainbxm.tab5;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView.ScaleType;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foxrainbxm.Common;
import com.foxrainbxm.FoxRainBXM;
import com.foxrainbxm.R;
import com.foxrainbxm.WriteFileLog;
import com.foxrainbxm.base.BaseFragment;
import com.foxrainbxm.jjlim.base.TextUtil;
import com.foxrainbxm.util.Request;
import com.foxrainbxm.vo.TipVo;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class Scenario5_1 extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

	public static Scenario5_1 intance = null;
	public static Scenario5_1 getinstance (){
		return intance;
	}
	View view = null;
	FoxRainBXM myapp = null;
	SwipeRefreshLayout refreshLayout;
	ListView listview;
	//	LinearLayout linearLayout_loading;
	ArrayList<TipVo> arraylist = new ArrayList<TipVo>();
	TipAdapter adapter;
	View hederview = null;
	LinearLayout top1layout,top2layout;
	ImageView top_img1,top_img2;
	TextView top_textview1,top_textview2;

	TextView top_title1,top_title2;
	TextView heart_count,heart_count2,reply_count,reply_count2,scrap_count,scrap_count2;
	TipVo headeritem ; 

	private int mPageIndex = 1;
	private boolean mListViewLock = true;
	private boolean endDate = false;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		} else {
			view = inflater.inflate(R.layout.scenario5_1, container, false);
			myapp = (FoxRainBXM) getActivity().getApplicationContext();
			initialize();
			getPushList();	
		}

		intance = this;
		return view;
	}

	public void Update(){
		try {
			getPushList();	
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	private void initialize(){
		try {
			listview = (ListView)view.findViewById(R.id.listview);
			refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swype);
			refreshLayout.setOnRefreshListener(this);
			refreshLayout.setColorScheme(R.color.blue, R.color.purple, R.color.green, R.color.orange);
			
			


			hederview= getActivity().getLayoutInflater().inflate(R.layout.scenario5_1_item1, null); //
			listview.addHeaderView(hederview); //


			listview.setOnScrollListener(new OnScrollListener() {
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					int count = totalItemCount - visibleItemCount;

					if(!endDate) {
						if(firstVisibleItem >= count && totalItemCount != 0   && mListViewLock == false)
						{
							mListViewLock = true;
							getMoreData();
							//						getPushList();
						}					
					}
	  
				}
			});

			top1layout = (LinearLayout)hederview.findViewById(R.id.top1layout);
			top1layout.setOnClickListener(top1click);
			top2layout = (LinearLayout)hederview.findViewById(R.id.top2layout);
			top2layout.setOnClickListener(top2click);
			top_img1 = (ImageView)hederview.findViewById(R.id.top_img1);
			top_img2 = (ImageView)hederview.findViewById(R.id.top_img2);
			top_title1 = (TextView)hederview.findViewById(R.id.top_title1);
			top_title2 = (TextView)hederview.findViewById(R.id.top_title2);

			top_textview1 = (TextView)hederview.findViewById(R.id.top_textview1);
			top_textview2 = (TextView)hederview.findViewById(R.id.top_textview2);


			heart_count = (TextView)hederview.findViewById(R.id.heart_count);
			heart_count2 = (TextView)hederview.findViewById(R.id.heart2_count);
			reply_count = (TextView)hederview.findViewById(R.id.reply_count);
			reply_count2 = (TextView)hederview.findViewById(R.id.reply2_count);
			scrap_count = (TextView)hederview.findViewById(R.id.scrap_count);
			scrap_count2 = (TextView)hederview.findViewById(R.id.scrap2_count);
			adapter = new TipAdapter(getActivity());
			listview.setAdapter(adapter);


			view.findViewById(R.id.writebtn).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						Intent intent = new Intent(getActivity(),Scenario5_S01111.class);
						startActivity(intent);
					} catch (Exception e) {
						WriteFileLog.writeException(e);
					}
				}
			});
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}

	}

	public void setHeaderView(){
		try {
			if(headeritem != null && headeritem.getTitle() != null && headeritem.getTitle().length() > 0){
				top1layout.setVisibility(View.VISIBLE);
				top_title1.setText(headeritem.getTitle());
				heart_count.setText(String.valueOf(headeritem.getHeart()));
				reply_count.setText(String.valueOf(headeritem.getReply()));
				scrap_count.setText(String.valueOf(headeritem.getScrap()));

				top_textview1.setVisibility(View.GONE);
				top_img1.setVisibility(View.VISIBLE);
				if(headeritem.getImg1()!=null && headeritem.getImg1().length() > 5){
					setImage(headeritem.getImg1(), top_img1);

				}else if(headeritem.getVideothum()!=null && headeritem.getVideothum().length() > 5){
					setImage(headeritem.getVideothum(), top_img1);	
				}else{
					top_textview1.setVisibility(View.VISIBLE);
					top_textview1.setText(headeritem.getContent());
					top_img1.setVisibility(View.GONE);
					//					setImage("", top_img1);
				}
				/*Dataparsing parsing = new Dataparsing();
				parsing.setOnDataCall(new SetDataCall() {
					@Override
					public void onSetDataCall(int arg0, String arg1, String arg2) {
						if(arg0 == 2){
							setImage(arg1, top_img1);
						}else if(arg0 == 3){
							setImage(arg2, top_img1);
						}
					}
				});
				parsing.DataParsing(headeritem.getContents());*/
			}else{
				top1layout.setVisibility(View.INVISIBLE);
			}

			if(headeritem != null && headeritem.getTitle2() != null && headeritem.getTitle2().length() > 0){
				top2layout.setVisibility(View.VISIBLE);
				top_title2.setText(headeritem.getTitle2());
				heart_count2.setText(String.valueOf(headeritem.getHeart2()));
				reply_count2.setText(String.valueOf(headeritem.getReply2()));
				scrap_count2.setText(String.valueOf(headeritem.getScrap2()));
				top_textview2.setVisibility(View.GONE);
				if(headeritem.getImg2()!=null && headeritem.getImg2().length() > 5){
					setImage(headeritem.getImg2(), top_img2);	
				}else if(headeritem.getVideothum2()!=null && headeritem.getVideothum2().length() > 5){
					setImage(headeritem.getVideothum2(), top_img2);	
				}else{
					top_textview2.setVisibility(View.VISIBLE);
					top_textview2.setText(headeritem.getContent2());
					//setImage("", top_img2);
				}
				/*Dataparsing parsing2 = new Dataparsing();
				parsing2.setOnDataCall(new SetDataCall() {
					@Override
					public void onSetDataCall(int arg0, String arg1, String arg2) {
						if(arg0 == 2){
							setImage(arg1, top_img2);
						}else if(arg0 == 3){
							setImage(arg2, top_img2);
						}
					}
				});
				parsing2.DataParsing(headeritem.getContents2());*/
			}else{
				top2layout.setVisibility(View.INVISIBLE);
			}
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
	}
	public void setImage(String imgurl,ImageView view){
		try {
			String url = "";
			final ImageView imageView = view;
			if(imgurl.startsWith("http")){
				url = imgurl;
			}else{
				url = Common.ImageUrl + imgurl;
			}
			myapp.imageloder.displayImage(url,
					view, myapp.options, new ImageLoadingListener() {

				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					//bar.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					//bar.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
					//bar.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					//	bar.setVisibility(View.GONE);
				}
			});
		} catch (Exception e) {
			if(progress != null)
				progress.dismiss();
			WriteFileLog.writeException(e);
		}

	}

	public void getPushList(){
		try {
			top_img1.setImageBitmap(null);
			top_img2.setImageBitmap(null);
			arraylist.clear();
			mPageIndex = 1;
			AsyncTask<Void, Void, Void> getDataWorker = new AsyncTask<Void, Void, Void>() {
				JSONObject rgb = null;
				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					try {
						//arraylist.clear();
						refreshLayout.setRefreshing(true);
						//						linearLayout_loading.setVisibility(View.VISIBLE);
					} catch (Exception e) {
						if(progress != null)
							progress.dismiss();
						WriteFileLog.writeException(e);
					}
				}

				@Override
				protected Void doInBackground(Void... params) {
					try {
						List<NameValuePair> dataList = new ArrayList<NameValuePair>();
						dataList.add(new BasicNameValuePair("page",String.valueOf(mPageIndex)));
						rgb = Request.requestService(getActivity(),Common.TIPBBSURL, dataList);
					} catch (Exception e) {
						if(progress != null)
							progress.dismiss();
						WriteFileLog.writeException(e);
						refreshLayout.setRefreshing(false);
					}			
					return null;
				}
				@Override
				protected void onPostExecute(Void result) {
					try {	
						if(rgb != null ) {
							String resultflag = rgb.getString("result");
							arraylist = TipVo.LoadParseJson(rgb, arraylist);
							if(resultflag.equals("true")) {
								if(arraylist.size() > 0 ) {
									arraylist = TipVo.LoadParseJson(rgb, arraylist);
									headeritem = arraylist.get(0);
									arraylist.remove(0);
								}
								else {
									headeritem = null;
									top1layout.setVisibility(View.INVISIBLE);
									top2layout.setVisibility(View.INVISIBLE);
								}

							}
						}
						else {
							top1layout.setVisibility(View.INVISIBLE);
							top2layout.setVisibility(View.INVISIBLE);
						}
					} catch (Exception e) {
						if(progress != null)
							progress.dismiss();
						refreshLayout.setRefreshing(false);
						WriteFileLog.writeException(e);
					} finally {
						//						linearLayout_loading.setVisibility(View.GONE);
						
						Handler mHandler = new Handler();

						mHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								if(headeritem != null && headeritem.getTitle().length() > 0) {
									setHeaderView();
									adapter.update();
									//							adapter.notifyDataSetChanged();
								}
								else {	
									adapter.update();
									//							adapter.notifyDataSetChanged();
									top1layout.setVisibility(View.INVISIBLE);
									top2layout.setVisibility(View.INVISIBLE);
								}
								if(progress != null)
									progress.dismiss();
								mListViewLock = false;
								refreshLayout.setRefreshing(false);
								mListViewLock = false;
								endDate = false;
							}
						}, 1000);
						listview.setSelectionFromTop(0, 0);
					}				
					super.onPostExecute(result);		
				}		
			};
			getDataWorker.execute();
		} catch (Exception e) {
			if(progress != null)
				progress.dismiss();
			refreshLayout.setRefreshing(false);
			WriteFileLog.writeException(e);
		}

	}


	Dialog progress = null;
	public void getMoreData(){
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
//						progress.show();

					} catch (Exception e) {
						if(progress != null)
							progress.dismiss();
						WriteFileLog.writeException(e);
					}
				}
				@Override
				protected Void doInBackground(Void... params) {
					try {
						List<NameValuePair> dataList = new ArrayList<NameValuePair>();
						dataList.add(new BasicNameValuePair("page",String.valueOf((mPageIndex+1))));
						rgb = Request.requestService(getActivity(),"tip/main/tipList", dataList);

						if(rgb != null ) {
							String resultflag = rgb.getString("result");
							if(resultflag.equals("true")) {
								//								JSONArray data = null;
								//									object = new JSONObject(json.toString());
								//									data = rgb.getJSONArray("data");

								arraylist = TipVo.LoadMoreParseJson(rgb, arraylist);
								ArrayList<TipVo> temp = new ArrayList<TipVo>();
								temp = TipVo.LoadMoreParseJson(rgb, temp);
								mPageIndex ++;
								if(temp.size() % 20 == 0) 
									endDate = false;
								else 
									endDate = true;

								//								setMoreDataFromJson(data.toString());
							}else{

							}
						}else {
							progress.dismiss();
							mListViewLock = true;
						}

					} catch (Exception e) {
						if(progress != null)
							progress.dismiss();
						WriteFileLog.writeException(e);

					}			
					return null;
				}
				@Override
				protected void onPostExecute(Void result) {
					progress.dismiss();
					try {	

						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								
								adapter.update();
								//								mListAdapter.notifyDataSetChanged();
								//								mListAdapter.update();
								//
								//								if(mPrevPosition != 0 && mScrollY != 0)
								//									mListView.setSelectionFromTop(mPrevPosition, mScrollY);
								//								//								mLoadingLayout.setVisibility(View.GONE);

							}
						});
					} catch (Exception e) {
						if(progress != null)
							progress.dismiss();
						WriteFileLog.writeException(e);
					} finally {

						//mListAdapter.update();
						Handler mHandler = new Handler();
						mHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								refreshLayout.setRefreshing(false);
								mListViewLock = false;
							}
						}, 1000);
					}				
					super.onPostExecute(result);		
				}		
			};
			getDataWorker.execute();
		} catch (Exception e) {
			progress.dismiss();
			refreshLayout.setRefreshing(false);
			WriteFileLog.writeException(e);
		}
	}


	OnClickListener top1click = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(),Scenario5_S00117.class);
			intent.putExtra("postno", headeritem.getPostNo());
			startActivity(intent);
		}
	};


	OnClickListener top2click = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(),Scenario5_S00117.class);
			intent.putExtra("postno", headeritem.getPostNo2());
			startActivity(intent);
		}
	};


	public class TipAdapter extends ArrayAdapter<TipVo> {
		public Context mContext;
		//ViewHoldertype1 holder1 = null;
				ViewHoldertype2 holder2  = null;
//		private SparseArray<WeakReference<View>> viewArray;

		int positions = -1;
		public TipAdapter(Context context) {
			super(context, R.layout.scemario5_1_item2,arraylist);
			this.mContext = context;
//			this.viewArray = new SparseArray<WeakReference<View>>(arraylist.size());
		}
		public void update(){
//			viewArray.clear();
			notifyDataSetChanged();
		}
		/*@Override
		public int getItemViewType(int position) {
			TipVo tmpItem = arraylist.get(position);
			return tmpItem.getViewtype();
		};

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		public  class ViewHoldertype1{
			ImageView top_img1;
			ImageView top_img2;
			TextView top_title1;
			TextView top_title2;
			TextView heart_count;
			TextView reply_count;
			TextView scrap_count;
			TextView heart_count2;
			TextView reply_count2;
			TextView scrap_count2;
			LinearLayout top1layout;
			LinearLayout top2layout;
		}*/

		public  class ViewHoldertype2{
			ImageView item_img;
			TextView title;
			TextView content;
			TextView heart_count;
			TextView reply_count;
			TextView scrap_count;
			LinearLayout rowlayout;
			TextView sml_textview;
		}

		OnClickListener rowclick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					int position = (Integer)v.getTag();
					Intent intent = new Intent(mContext,Scenario5_S00117.class);
					intent.putExtra("postno", arraylist.get(position).getPostNo());
					startActivity(intent);
				} catch (Exception e) {
					WriteFileLog.writeException(e);
				}

			}
		};

		public void DataParsing(String data,TextView
				textview, ImageView imageview){
			try {
				String[]  values = data.split("]");
				for(int i = 0 ; i < values.length ;i++){
					String[]  values2 = values[i].split(";");
					values2[0] = values2[0].substring(1);
					if(values2[0].equals("1")){
						setText(values2[1], textview);
					}else if(values2[0].equals("2")){
						setImage(values2[1], imageview);	
						break;
					}else if(values2[0].equals("3")){
						setImage(values2[2], imageview);	
					}
				}
			} catch (Exception e) {
				WriteFileLog.writeException(e);
			}
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

//			if(viewArray != null && viewArray.get(position) != null) {
//				convertView = viewArray.get(position).get();
//				if(convertView != null){
//					return convertView;
//				}
//			}
//			try{
//
//				TipVo tmpItem = arraylist.get(position);
//
//				//LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				//int itemType = getItemViewType(position);
//				LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				convertView = layoutInflater.inflate(R.layout.scemario5_1_item2,null);
//				//					convertView.setTag(holder2);
//
//				LinearLayout rowlayout = (LinearLayout)convertView.findViewById(R.id.rowlayout);
//
//				rowlayout.setOnClickListener(rowclick);
//				rowlayout.setTag(position);
//				ImageView item_img = (ImageView)convertView.findViewById(R.id.item_img);
//				TextView title = (TextView)convertView.findViewById(R.id.title);
//				TextView content = (TextView)convertView.findViewById(R.id.content);
//				TextView heart_count = (TextView)convertView.findViewById(R.id.heart_count3);
//				TextView reply_count = (TextView)convertView.findViewById(R.id.reply_count3);
//				TextView scrap_count = (TextView)convertView.findViewById(R.id.scrap_count3);
//
//				TextView sml_textview = (TextView)convertView.findViewById(R.id.sml_textview);
//
//
//				setText(tmpItem.getTitle(), title);
//				setText(String.valueOf(tmpItem.getHeart()), heart_count);
//				setText(String.valueOf(tmpItem.getReply()), reply_count);
//				setText(String.valueOf(tmpItem.getScrap()), scrap_count );
//				setText(tmpItem.getContent(), content);
//				if(tmpItem.getImg1()!=null && tmpItem.getImg1().length() > 5){
//					sml_textview.setVisibility(View.GONE);
//					item_img.setVisibility(View.VISIBLE);
//					setImage(tmpItem.getImg1(), item_img);	
//				}else if(tmpItem.getVideothum()!=null && tmpItem.getVideothum().length() > 5){
//					sml_textview.setVisibility(View.GONE);
//					item_img.setVisibility(View.VISIBLE);
//					setImage(tmpItem.getVideothum(), item_img);	
//				}else{
//					sml_textview.setVisibility(View.VISIBLE);
//					item_img.setVisibility(View.GONE);
//					sml_textview.setText(tmpItem.getContent());
//
//
//					//setImage("", holder2.item_img);
//				}
//			}catch(Exception e){
//				WriteFileLog.writeException(e);
//			}finally{
//				viewArray.put(position, new WeakReference<View>(convertView));
//			}




			//			
						try {
							
							TipVo tmpItem = arraylist.get(position);
			
							//LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
							//int itemType = getItemViewType(position);
							if (convertView == null) {
								LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
								convertView = layoutInflater.inflate(R.layout.scemario5_1_item2,null);
								holder2 = new ViewHoldertype2();
								convertView.setTag(holder2);
							} else {
								holder2 = (ViewHoldertype2) convertView.getTag();
							}
			
							try {
								holder2.rowlayout = (LinearLayout)convertView.findViewById(R.id.rowlayout);
			
								holder2.rowlayout.setOnClickListener(rowclick);
								holder2.rowlayout.setTag(position);
								holder2.item_img = (ImageView)convertView.findViewById(R.id.item_img);
								holder2.title = (TextView)convertView.findViewById(R.id.title);
								holder2.content = (TextView)convertView.findViewById(R.id.content);
								holder2.heart_count = (TextView)convertView.findViewById(R.id.heart_count3);
								holder2.reply_count = (TextView)convertView.findViewById(R.id.reply_count3);
								holder2.scrap_count = (TextView)convertView.findViewById(R.id.scrap_count3);
			
								holder2.sml_textview = (TextView)convertView.findViewById(R.id.sml_textview);
			
			
								setText(tmpItem.getTitle(), holder2.title);
								setText(String.valueOf(tmpItem.getHeart()), holder2.heart_count);
								setText(String.valueOf(tmpItem.getReply()), holder2.reply_count);
								setText(String.valueOf(tmpItem.getScrap()), holder2.scrap_count );
								setText(tmpItem.getContent(), holder2.content);
								if(tmpItem.getImg1()!=null && tmpItem.getImg1().length() > 5){
									holder2.sml_textview.setVisibility(View.GONE);
									holder2.item_img.setVisibility(View.VISIBLE);
									setImage(tmpItem.getImg1(), holder2.item_img);	
								}else if(tmpItem.getVideothum()!=null && tmpItem.getVideothum().length() > 5){
									holder2.sml_textview.setVisibility(View.GONE);
									holder2.item_img.setVisibility(View.VISIBLE);
									setImage(tmpItem.getVideothum(), holder2.item_img);	
								}else{
									holder2.sml_textview.setVisibility(View.VISIBLE);
									holder2.item_img.setVisibility(View.GONE);
									holder2.sml_textview.setText(tmpItem.getContent());
			
			
									//setImage("", holder2.item_img);
								}
			
								//DataParsing(tmpItem.getContents(),holder2.content,holder2.item_img);
							} catch (Exception e) {
								WriteFileLog.writeException(e);
							}
						} catch (Exception e) {
							WriteFileLog.writeException(e);
						}

			return convertView;
		}

		public void setText(String text, TextView textview){
			textview.setText(text);
		}
		public void setImage(String imgurl,final ImageView view){

			try {
				String url = "";
				if(imgurl.startsWith("http")){
					url = imgurl;
				}else{
					url =  Common.ImageUrl + imgurl;
				}
				myapp.imageloder.displayImage(url,
						view, myapp.options, new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String arg0, View arg1) {
					}

					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					}

					@Override
					public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
						//ImageCache.getInstance().setImageFitImageView(view, arg2);
					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
					}
				});
			} catch (Exception e) {
				WriteFileLog.writeException(e);
			}
			/*	ImageCache.getInstance().loadAsync(Common.ImageUrl + imgurl, new ImageCallback() {
				@Override
				public void onImageLoaded(Drawable image, String url, String path) {
					ImageCache.getInstance().setImageFitImageView(view, image);
					//ImageCache.getInstance().setImageResizeImageView(imageView, bitmap, width, height)
				}
			}, getActivity());*/

		}
	}

	@Override
	public void onRefresh() {
		getPushList();
	}

}
