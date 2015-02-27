package com.foxrainbxm.tab1;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.antonyt.infiniteviewpager.InfinitePagerAdapter;
import com.antonyt.infiniteviewpager.InfiniteViewPager;
import com.brainyxlib.image.BrImageUtilManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxrainbxm.Common;
import com.foxrainbxm.FoxRainBXM;
import com.foxrainbxm.R;
import com.foxrainbxm.WriteFileLog;
import com.foxrainbxm.base.BaseFragment;
import com.foxrainbxm.base.ResizeImageView;
import com.foxrainbxm.jjlim.base.ContentMessageParser;
import com.foxrainbxm.jjlim.base.ContentVO;
import com.foxrainbxm.jjlim.base.HttpContentApi;
import com.foxrainbxm.jjlim.base.HttpContentTask.OnContentTaskResultListener;
import com.foxrainbxm.jjlim.base.NanumFont;
import com.foxrainbxm.jjlim.base.TextUtil;
import com.foxrainbxm.jjlim.content.ContentDetailView;
import com.foxrainbxm.jjlim.libary.ExpandableHeightGridView;
import com.foxrainbxm.skmin.base.Popup1Button;
import com.foxrainbxm.tab5.Scenario5_S00117;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/*
 * HOME TAB
 */
public class Scenario1_1 extends BaseFragment {
	//InfiniteCirclePageIndicator pageIndicator;
	View view = null;
	FoxRainBXM myapp = null;

	ArrayList<ContentVO> mPagerItems; // 위쪽 ViewPager 데이타
	ArrayList<ContentVO> mGridItems; // 아래쪽 GridView 데이타

	Typeface mTypeface; // 폰트

	// PagerContainer mPagerContainer;
	InfiniteViewPager mViewPager;
//	ViewPager mViewPager;
	InfinitePagerAdapter mPagerAdapter;
	SlideTimerTask mTimer; // ViewPager 자동 전환 타이버

	ExpandableHeightGridView mGridView;
	CustomGridAdapter mGridAdapter;
	private LinearLayout mPageMark; // 현재 몇 페이지 인지 나타내는 뷰
	private int mPrevPosition; // 이전에 선택되었던 포지션 값
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		} else {
			view = inflater.inflate(R.layout.scen00012, container, false);
			myapp = (FoxRainBXM) getActivity().getApplicationContext();
			initView();
		}
		return view;
	}
	

	// 뷰 초기화 (데이터 로딩 전)
	private void initView() {
		try {
			// mPagerContainer =
			// (PagerContainer)view.findViewById(R.id.pager_container);
			mViewPager = (InfiniteViewPager) view.findViewById(R.id.viewPager1);
//			mViewPager = (ViewPager) view.findViewById(R.id.viewPager1);
			mGridView = (ExpandableHeightGridView) view.findViewById(R.id.gridView1);

			// 로딩전에 콘텐츠를 가려줌
			hideView();

			// 콘텐츠 변수 초기화
			mPagerItems = new ArrayList<ContentVO>();
			mGridItems = new ArrayList<ContentVO>();

			// 폰트 초기화
			if (!NanumFont.getInstance().isInited()) {
				Log.i("TEST", "NanumFont Init");
				NanumFont.getInstance().init(getActivity().getAssets());
			}
			// 홈 상단 ViewPager 초기화
			// 무한 페이지, 인디케이터, 자동 전환, 다음꺼 살짝 보이는 기능을 가진 ViewPager
//			mViewPager = (ViewPager) view.findViewById(R.id.viewPager1);
			mViewPager.setOffscreenPageLimit(3);
//			mViewPager.setPageMargin(-30);
			mViewPager.setClipToPadding(false);
			mViewPager.setPadding(30,0,30,0);
			mViewPager.setPageMargin(10);
			
//			mViewPager.setClipChildren(false);

			// 홈 하단 GridView 초기화
			mGridView = (ExpandableHeightGridView) view
					.findViewById(R.id.gridView1);
			mGridView.setFocusable(false);
			mGridView.setExpanded(true);

			// 서버로 부터 Home 콘텐츠 받아옴
			getDataFromServer();
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
	}

	private void showView() {
		// mPagerContainer.setVisibility(View.VISIBLE);
		mGridView.setVisibility(View.VISIBLE);
	}

	private void hideView() {
		// mPagerContainer.setVisibility(View.INVISIBLE);
		mGridView.setVisibility(View.INVISIBLE);
	}

	private void getDataFromServer() {
		HttpContentApi httpApi = new HttpContentApi(getBaseActivity());
		httpApi.contentHome(new OnContentTaskResultListener() {
			@Override
			public void onTaskResult(boolean success, String result) {
				if (success) {
					setDataFromJson(result);
					setView();
				} else {
					new Popup1Button(getBaseActivity(), "데이타를 가져오지" +
							" 못했습니다.",
							null).show();
				}
			}
		});
	}

	// 홈탭 초기정보 셋팅
	private void setDataFromJson(String jsonStr) {
		ObjectMapper m = new ObjectMapper();
		try {
			JsonNode rootNode = m.readTree(jsonStr);
			for (JsonNode node : rootNode.path("top")) {
				ContentVO content = new ContentVO();
				content.setPostNo(node.path("postno").asInt());
				content.setCoverPhoto(node.path("coverphoto").asText());
				content.setToptitle(node.path("title").asText());
				content.setBbstype(node.path("type").asInt());
				String temp = node.path("contents").asText();
				temp = temp.replaceAll("\n", "");
				content.setContent(ContentMessageParser.getContentFromMessage(temp));
				mPagerItems.add(content);
			}
			for (JsonNode node : rootNode.path("bottom")) {
				ContentVO content = new ContentVO();
				content.setPostNo(node.path("postno").asInt());
				content.setTitle(node.path("title").asText());
				content.setCoverPhoto(ContentMessageParser
						.getCoverImageFromMessage(node.path("contents")
								.asText()));
				content.setBbstype(node.path("type").asInt());
				mGridItems.add(content);
			}
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}

		if (mPagerItems.size() == 0) {
			ContentVO dummy = new ContentVO();
			mPagerItems.add(dummy);
		}
	}

	private void setView() {

		try {
			CustomPagerAdapter adapter = new CustomPagerAdapter(getActivity(),
					mPagerItems);
			mPagerAdapter = new InfinitePagerAdapter(adapter);
			mPagerAdapter.setOneItemMode();

			mViewPager.setAdapter(mPagerAdapter);
			mPageMark = (LinearLayout) view.findViewById(R.id.page_mark); // 상단의 현재 페이지
			
			mViewPager.setOnPageChangeListener(new OnPageChangeListener() { // 아이템이
				// 변경되면,
				// gallery나
				// listview의
				// onItemSelectedListener와
				// 비슷
					@Override
					public void onPageSelected(int position) {
						if(position % mPagerItems.size() == 0){
							Log.e("", "");
						}else if(position % mPagerItems.size() == 1){
							Log.e("", "");
						}else if(position % mPagerItems.size() == 2){
							Log.e("", "");
						}else if(position % mPagerItems.size() == 3){
							Log.e("", "");
						}else if(position % mPagerItems.size() == 4){
							Log.e("", "");
						}
						mPageMark.getChildAt(mPrevPosition).setBackgroundResource(R.drawable.page_not); // 이전 페이지에 해당하는 페이지 표시 이미지 변경
						mPageMark.getChildAt(position % mPagerItems.size()).setBackgroundResource(R.drawable.page_select); // 현재 페이지에 해당하는 페이지 표시 이미지
						
						mPrevPosition = (position % mPagerItems.size()); // 이전 포지션 값을 현재로 변경
						
					/*if (position < mPagerItems.size()) // 3배의 아이템중 앞쪽이면 뷰페이져의 포지션을 가운데 범위로 이동시킨다
						mViewPager.setCurrentItem(position + mPagerItems.size(), false);
					else if (position >= mPagerItems.size() * 2) // 3배의 아이템 중 뒤쪽이면 뷰페이져의 포지션을 가운데
					// 범위로 이동시킨다
						mViewPager.setCurrentItem(position - mPagerItems.size(), false);
					else { // 가운데 범위이면 상단의 페이지 표시를 변경한다
					position -= mPagerItems.size();
					// 아이템이 선택이 되었으면
					mPageMark.getChildAt(mPrevPosition).setBackgroundResource(
					R.drawable.page_not); // 이전 페이지에 해당하는 페이지 표시 이미지 변경
					mPageMark.getChildAt(position).setBackgroundResource(
					R.drawable.page_select); // 현재 페이지에 해당하는 페이지 표시 이미지
						// 변경
					mPrevPosition = position; // 이전 포지션 값을 현재로 변경
					}*/
					}
					
					@Override
					public void onPageScrolled(int position, float positionOffest,
					int positionOffsetPixels) {
					}
					
					@Override
					public void onPageScrollStateChanged(int state) {
					}
					});
			/*pageIndicator = (InfiniteCirclePageIndicator) view
					.findViewById(R.id.indicator1);*/
//			pageIndicator.setSnap(true);
			/*pageIndicator.setViewPager(mViewPager);
			pageIndicator.setPageColor(getResources().getColor(R.color.circle_default_color));
			pageIndicator.setFillColor(getResources().getColor(R.color.circle_selected_color));
			pageIndicator.setCurrentItem(4);
			pageIndicator.setOnPageChangeListener(new OnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {
					Log.e("", "" + position);
				}

				@Override
				public void onPageScrolled(int position, float positionOffset,
						int positionOffsetPixels) {
				}

				@Override
				public void onPageScrollStateChanged(int state) {

					if (state == 0) {
						if (mTimer != null) {
							mTimer.stop();
							mTimer = new SlideTimerTask();
							mTimer.start();
						}
					}
				}
			});
*/		

			// ViewPager Slide 타이머 (상단 콘텐츠가 2개 이상일 경우만
		/*	if (mPagerItems.size() > 1) {
				mTimer = new SlideTimerTask();
				mTimer.start();
			}*/

			// GridView 로드
			mGridAdapter = new CustomGridAdapter(getActivity(), mGridItems);
			mGridView.setAdapter(mGridAdapter);
			mGridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					startDetailView(mGridItems.get(position).getPostNo(),
							mGridItems.get(position).getBbstype());
				}
			});

			// 로딩후에 콘텐츠를 보여줌
			showView();
			initPageMark();
			//pageIndicator.bringToFront(); 
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
		// PagerView 로드
	}

	private void initPageMark() {
		for (int i = 0; i < mPagerItems.size(); i++) {
			ImageView iv = new ImageView(getActivity()); // 페이지 표시 이미지
																	// 뷰 생성
			iv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));

			// 첫 페이지 표시 이미지 이면 선택된 이미지로
			if (i == 0)
				iv.setBackgroundResource(R.drawable.page_select);
			else
				// 나머지는 선택안된 이미지로
				iv.setBackgroundResource(R.drawable.page_not);

			// LinearLayout에 추가
			mPageMark.addView(iv);
		}
		mPrevPosition = 0; // 이전 포지션 값 초기화
	}
	
	private void startDetailView(int postNo, int viewtype) {
		if (postNo == 0) {
			return;
		}
		if(viewtype == 4) {
			Intent intent = new Intent(getActivity(), Scenario5_S00117.class);
			intent.putExtra("postno", postNo);
			startActivity(intent);	
		}
		else {
			Intent intent = new Intent(getActivity(), ContentDetailView.class);
			intent.putExtra("postNo", postNo);
			intent.putExtra("viewtype", viewtype);
			startActivity(intent);	
		}
	}

	class SlideTimerTask extends TimerTask {
		Timer mTimer;
		int mIndex;

		public SlideTimerTask() {
			mTimer = new Timer();
		}

		public void start() {
			mTimer.schedule(this, 5000, 5000);
		}

		public void stop() {
			cancel();
		}

		@Override
		public void run() {
			mIndex = mViewPager.getCurrentItem() + 1;

			/*
			 * getBaseActivity().runOnUiThread(new Runnable() {
			 * 
			 * @Override public void run() { mViewPager.setCurrentItem(mIndex,
			 * true); } });
			 */
		}
	}

	// 홈탭 위쪽 ViewPager 아답터
	private class CustomPagerAdapter extends PagerAdapter {

		private Context mContext;
		private ArrayList<ContentVO> items;

		public CustomPagerAdapter(Context context, ArrayList<ContentVO> items) {
			super();
			this.mContext = context;
			this.items = items;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			
			FrameLayout layout= null;
			try {
				
				layout = (FrameLayout) View.inflate(mContext,
						R.layout.scen00012_pager_item, null);

				if (items.get(position).getCoverPhoto().length() != 0) {
					String urlStr = HttpContentApi.imghost
							+ items.get(position).getCoverPhoto();
					// String urlStr =
					// HttpContentApi.getContentImageUrl(items.get(position).getCoverPhoto(),
					// 2);
					final ResizeImageView iv = (ResizeImageView) layout.findViewById(R.id.imageView1);
					iv.setScale(2);
					iv.setMarginValue(10);
					iv.setScaleType(ScaleType.FIT_XY);
					final ProgressBar bar = (ProgressBar)layout.findViewById(R.id.progressbar);
					
					myapp.imageloder.displayImage(urlStr,iv, myapp.options, new ImageLoadingListener() {
								@Override
								public void onLoadingStarted(String arg0, View arg1) {
									bar.setVisibility(View.VISIBLE);
								}
								
								@Override
								public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
									bar.setVisibility(View.GONE);
								}
								
								@Override
								public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
									bar.setVisibility(View.GONE);
									int rotate = BrImageUtilManager.getInstance().GetExifOrientation(arg0);
									iv.setImageBitmap(BrImageUtilManager.getInstance().rotate(arg2,rotate));
								}
								
								@Override
								public void onLoadingCancelled(String arg0, View arg1) {
									bar.setVisibility(View.GONE);
								}
							});
					//myapp.imageloder.displayImage(urlStr, iv,  myapp.options);
					
					} else {
						
					}
				
				TextView textview = (TextView) layout.findViewById(R.id.title);
				
				TextView contextView = (TextView)layout.findViewById(R.id.content);
				textview.setText(getTypeindex(items.get(position).getBbstype()));
				contextView.setText(items.get(position).getToptitle());
				
				container.addView(layout);
				layout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						startDetailView(items.get(position).getPostNo(),
								items.get(position).getBbstype());
					}
				});
				
			} catch (Exception e) {
				WriteFileLog.writeException(e);
			}
			return layout;
		
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public int getCount() {
			return items.size() ;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

	}
	public String getTypeindex(int type){
		
		if(type == 1) return "하이힐스토리";
		else if(type == 2) return "고무장갑스토리";
		else if(type == 3) return "아이를부탁해";
		else if(type == 4) return "즐거운육아";
		else if(type == 6) return "공지사항";
		else return "";
	}
	// 홈탭 아래쪽 GridView 아답터
	private class CustomGridAdapter extends BaseAdapter {

		private Context mContext;
		private ArrayList<ContentVO> items;

		public CustomGridAdapter(Context context, ArrayList<ContentVO> items) {
			super();
			this.mContext = context;
			this.items = items;
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
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder vh;
			
			try {
				if (convertView == null) {
					convertView = View.inflate(mContext,R.layout.scen00012_grid_item, null);

					vh = new ViewHolder();
					vh.image = (ImageView) convertView.findViewById(R.id.imageView1);
//					vh.image.setWidthScale(2);
//					vh.image.setScale(1);
//					vh.image.setMarginValue(5);
//					vh.image.setMaxHeight(true);
					vh.background = (ImageView) convertView.findViewById(R.id.imageView2);
					vh.title = (TextView) convertView.findViewById(R.id.textView1);
					vh.title.setTypeface(mTypeface);
					vh.progressbar = (ProgressBar)convertView.findViewById(R.id.progressbar);
					convertView.setTag(vh);
				} else {
					vh = (ViewHolder) convertView.getTag();
				}

				if (items.get(position).getCoverPhoto().length() != 0) {
					String urlStr = "";
					if(items.get(position).getCoverPhoto().startsWith("http://")) {
						urlStr = items.get(position).getCoverPhoto(); 
					}
					else {
						urlStr = Common.ImageUrl + items.get(position).getCoverPhoto();
					}
//					String urlStr = Common.ImageUrl + items.get(position).getCoverPhoto();
					  DisplayImageOptions options = new DisplayImageOptions.Builder()
//						.imageScaleType(ImageScaleType.EXACTLY)
						.resetViewBeforeLoading(true)
						.cacheInMemory(true)
						.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
						.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).showStubImage(R.drawable.main_ctt_list_bg_n).showImageOnFail(R.drawable.main_ctt_list_bg_n)
						.build();
					  
					myapp.imageloder.displayImage(urlStr,vh.image, options, new ImageLoadingListener() {
						@Override
						public void onLoadingStarted(String arg0, View arg1) {
							vh.progressbar.setVisibility(View.VISIBLE);
						}
						
						@Override
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
							vh.progressbar.setVisibility(View.GONE);
						}
						
						@Override
						public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
							vh.progressbar.setVisibility(View.GONE);
							int rotate = BrImageUtilManager.getInstance().GetExifOrientation(arg0);
							vh.image.setImageBitmap(BrImageUtilManager.getInstance().rotate(arg2,rotate));
							
							if(arg2.getHeight() != arg2.getWidth()) {
								vh.image.setScaleType(ScaleType.CENTER_CROP);
							}
//							else {
//								vh.image.setScaleType(ScaleType.FIT_XY);
//							}
						}
						
						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							vh.progressbar.setVisibility(View.GONE);
						}
					});
					
					//myapp.imageloder.displayImage(urlStr, vh.image, myapp.options);
				} else {
					vh.background.setVisibility(View.INVISIBLE);
				}
				vh.title.setText(TextUtil.getTitleArrange(items.get(position).getTitle()));

			
			} catch (Exception e) {
				WriteFileLog.writeException(e);
			}
			return convertView;
			
		}

		class ViewHolder {
			public ImageView image;
			public ImageView background;
			public TextView title;
			public ProgressBar progressbar;
		}
	}
}
