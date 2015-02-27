package com.foxrainbxm.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.internal.widget.IcsAdapterView;
import com.actionbarsherlock.internal.widget.IcsSpinner;
import com.brainyxlib.BrUtilManager;
import com.foxrainbxm.Common;
import com.foxrainbxm.R;
import com.foxrainbxm.WriteFileLog;
import com.foxrainbxm.base.BaseActivity;
import com.foxrainbxm.jjlim.base.HttpContentApi;
import com.foxrainbxm.jjlim.base.HttpContentTask.OnContentTaskResultListener;
import com.foxrainbxm.jjlim.content.ContentDetailView;
import com.foxrainbxm.skmin.base.Popup1Button;
import com.foxrainbxm.tab5.Scenario5_S00117;
import com.foxrainbxm.util.HttpUtil;
import com.foxrainbxm.util.HttpUtil.OnAfterParsedData;
import com.foxrainbxm.util.HttpUtil.PageVO;
import com.foxrainbxm.util.ImageCache;
import com.foxrainbxm.util.RecycleUtils;
import com.foxrainbxm.volley.ImageCacheManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

/**
 * 
 * @author 이병훈 <Br/>
 * 화면ID : S000019<Br/>
 * 화면명: 마이스크랩 <Br/>
 */
public class Scen000019 extends BaseActivity {
	private ListView myScrapList;
	private MyScrapAdapter adapter;

	private boolean isDelSelectMode = false;
	private ArrayList<Map<String, String>> scrabData = new ArrayList<Map<String,String>>();
	private int currPage = 0;
	private int totalCount = 0;

	private boolean mLock;

	private String KEY_DEL = "del";

	private ImageView profileImgTmb;

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
	protected void onRestart() {

		super.onRestart();

		currPage = 0;
		scrabData.clear();
		requestMyscrabData();

		
	}

	@Override
	protected void onResume() {
		
		if (Common.profileBitmap != null) {

			final ImageView profile_image = (ImageView) findViewById(R.id.profile_image);
			profile_image.setImageBitmap(Common.profileBitmap);
			// ImageView profile = (ImageView) findViewById(R.id.profile_image);
			// profile.setImageBitmap(myapp.profileBitmap);
		} else {
			final ImageView profile_image = (ImageView) findViewById(R.id.profile_image);
			// setImage(Common.ImageUrl + myApp.getUserInfo().getProfilephoto(),
			// profile_image);

			ImageLoader.getInstance().displayImage(
					Common.ImageUrl + myApp.getUserInfo().getProfilephoto(),
					profile_image, myApp.profileOptions, new ImageLoadingListener() {
						
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
							Common.profileBitmap = arg2;
							
						}
						
						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							// TODO Auto-generated method stub
							
						}
					});
		}
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BrUtilManager.getInstance().onActivityNoTitle(this);

		setContentView(R.layout.scen00019);

		if (myApp.getUserInfo().getId().isEmpty()) {
			// 발생하면 안되는상황
			return;
		}

		initialize();
	};

	IcsSpinner spinnerMainTran;
	ArrayAdapter<CharSequence> spinnerAdapter;

	private void initialize() {

		ImageCache cache = ImageCache.getInstance();

		profileImgTmb = (ImageView) findViewById(R.id.profile_image);

		if (myApp.getUserInfo().getProfilephoto().isEmpty()) {
			//	ImageCache.setImageFitImageView(profileImg, getResources().getDrawable(R.drawable.bg_profile_round_n));

			ImageCache.setImageFitImageView(profileImgTmb, getResources().getDrawable(R.drawable.profile_title_n));
		}else {
			final ImageView profile_image = (ImageView) findViewById(R.id.profile_image);
			profile_image.setImageBitmap(Common.profileBitmap);
//
//			myApp.imageloder.displayImage(Common.ImageUrl  + myApp.getUserInfo().getProfilephoto(), profileImgTmb, myApp.profileOptions, new ImageLoadingListener() {
//				
//				@Override
//				public void onLoadingStarted(String arg0, View arg1) {
//					// TODO Auto-generated method stub
//					
//				}
//				
//				@Override
//				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
//					// TODO Auto-generated method stub
//					
//				}
//				
//				@Override
//				public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
//					Common.profileBitmap = arg2;					
//				}
//				
//				@Override
//				public void onLoadingCancelled(String arg0, View arg1) {
//					// TODO Auto-generated method stub
//					
//				}
//			});

			/*cache.loadAsync(Common.ImageUrl + myApp.getUserInfo().getProfilephoto(), 
					new ImageCallback() {
				@Override
				public void onImageLoaded(Drawable image, String url, String path) {
					ImageCache.setImageFitImageView(profileImgTmb, image);
				}
			}, this);*/
		}

		spinnerMainTran = (IcsSpinner) findViewById(R.id.spinnerMainTran);
		spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.select_delete,R.layout.spinner_item);
		spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
		spinnerMainTran.setAdapter(spinnerAdapter);
		/*	findViewById(R.id.spinnerBtn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//영역이 작아 클릭이 안되는것 방지용.
				spinnerMainTran.performClick();
			}
		});*/
		spinnerMainTran.setOnItemSelectedListener(new IcsSpinner.OnItemSelectedListener() {
			@Override
			public void onItemSelected(IcsAdapterView<?> parent, View view,
					int position, long id) {
				try {
					switch (position) {
					case 0:
						changeCheckMode();
						break;
					case 1:
						final String msg = deleteCheckList();
						if (msg.trim().length() == 0) {
							isDelSelectMode = false;
							adapter.notifyDataSetChanged();
							return;
						}
						AlertDialog.Builder alert = new AlertDialog.Builder(Scen000019.this);
						alert.setTitle(R.string.alert_title);
						String[] cntChk = msg.split(",");
						alert.setMessage("선택하신[" + cntChk.length+ "]개를 삭제하시겠습니까?");
						alert.setPositiveButton("예",
								new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,int which) {
								dialog.dismiss();
								deleteMyScrab(msg);
								//데이터 초기화
								scrabData = new ArrayList<Map<String,String>>();
								currPage = 0;

								requestMyscrabData();
							}
						}).setNegativeButton("아니오",new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,int which) {
								dialog.dismiss();
							}
						}).show();
						break;

					default:
						break;
					}

				} catch (Exception e) {
					WriteFileLog.writeException(e);
				}
			}
			@Override
			public void onNothingSelected(IcsAdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		spinnerMainTran.setSelection(1);
		/*		String imgDownUrl = getUrl(R.string.url_img_down) + myApp.getUserInfo().getProfilephoto();
		//String imgDownUrl = getUrl(R.string.url_img_down) + myApp.getUserInfo().getProfilephoto();
		cache.loadAsync(imgDownUrl, new ImageCallback() {
			@Override
			public void onImageLoaded(Drawable image, String url, String path) {
				ImageView profile = (ImageView) findViewById(R.id.profile_image);
				ImageCache.setImageFitImageView(profile,ImageCache.chgDrawableToBitmap(image));
			}
		}, this);
		 */
		findViewById(R.id.title_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		myScrapList = (ListView) findViewById(R.id.my_scrap_list);
		adapter = new MyScrapAdapter();
		myScrapList.setAdapter(adapter);
		myScrapList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Map<String, String> tmp = scrabData.get(arg2);

				String postNo =  tmp.get("postNo");
				//TODO 하이힐스토리 타입만 나옴??
				String type =  tmp.get("type");

				if(type.equals("4")){
					Intent intent = new Intent(getApplicationContext(),Scenario5_S00117.class);
					intent.putExtra("postno", Integer.valueOf(postNo));
					startActivity(intent);
				}else{
					Intent intent = new Intent(getApplicationContext(), ContentDetailView.class);
					intent.putExtra("postNo", Integer.valueOf(postNo));
					intent.putExtra("viewtype", Integer.parseInt(type));
					startActivity(intent);	
				}


				/*	 Intent intent = new Intent(getApplicationContext(), Scen00114.class);
				 intent.putExtra("postNo", postNo);
				 intent.putExtra("type", type);
				 startActivity(intent);*/
				//				 finish();
				// 상세화면으로 이동				
			}
		});


		ImageView profile = (ImageView) findViewById(R.id.profile_image);
		profile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),
						Scen000013.class));
			}
		});
		
		if (Common.profileBitmap != null) {

			final ImageView profile_image = (ImageView) findViewById(R.id.profile_image);
			profile_image.setImageBitmap(Common.profileBitmap);
			// ImageView profile = (ImageView) findViewById(R.id.profile_image);
			// profile.setImageBitmap(myapp.profileBitmap);
		} else {
			final ImageView profile_image = (ImageView) findViewById(R.id.profile_image);
			// setImage(Common.ImageUrl + myApp.getUserInfo().getProfilephoto(),
			// profile_image);

			ImageLoader.getInstance().displayImage(
					Common.ImageUrl + myApp.getUserInfo().getProfilephoto(),
					profile_image, myApp.profileOptions);
			Common.profileBitmap = getImageFromURL(Common.ImageUrl
					+ myApp.getUserInfo().getProfilephoto());
		}

		myScrapList.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int count = totalItemCount - visibleItemCount;

				if(firstVisibleItem >= count && totalItemCount != 0 && mLock == false) {
					if(totalCount > scrabData.size()) {
						currPage ++;
						mLock = true;
						//					mContentIndex ++;
						requestMyscrabData();	
					}

				}

			}

		});
		//		myScrapList.setOnScrollListener(new OnScrollListener() {
		//			boolean isTouching = false;
		//			@Override
		//			public void onScrollStateChanged(AbsListView arg0, int arg1) {
		//				//터치하지 않고 있는 상태
		//				isTouching = arg1 == 0;
		//			}
		//
		//			@Override
		//			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		//				//int firstVisibleItem, int visibleItemCount, int totalItemCount) 
		//				if (arg0.isShown()) {
		//					if ((arg1 + arg2) % 20 == 0 && (currPage * 20 < totalCount)) {
		//						requestMyscrabData();
		//					}
		//				}
		//			}
		//		});
		requestMyscrabData();
	}

	/**
	 * 데이터 요청
	 */
	private void requestMyscrabData() {
		isDelSelectMode = false;
		mLock = true;
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(HttpUtil.KEY_URL, getString(R.string.url_myscrab));
		params.put("currPage", String.valueOf(currPage));
		params.put("userId", myApp.getUserInfo().getId());

		HttpUtil util = HttpUtil.getInstance();
		util.requestHttp(Scen000019.this, new OnAfterParsedData() {
			@Override
			public void onResult(boolean result, Object resultData,PageVO pageData) {
				if (result == false) {
					return;
				}

				//페이징을 위한 데이터 설정
				if (pageData != null) {
					totalCount = pageData.getTotalCnt();
				}

				//리스트 데이터 세팅
				if (scrabData == null || scrabData.size() == 0) {
					scrabData = (ArrayList<Map<String, String>>) resultData;
				}else {
					scrabData.addAll((ArrayList<Map<String, String>>) resultData);
				}

				adapter.notifyDataSetChanged();
				mLock = false;
				//다음 페이징을 위한 페이지요청 데이터 증가
				//				currPage ++;
			}

		}, params,true);
	}

	private void deleteMyScrab(String list) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(HttpUtil.KEY_URL, getString(R.string.url_myscrabDelete));
		params.put("userId", myApp.getUserInfo().getId());
		params.put("delList", list);

		HttpUtil util = HttpUtil.getInstance();
		util.requestHttp(Scen000019.this, new OnAfterParsedData() {
			@Override
			public void onResult(boolean result, Object resultData,PageVO pageData) {
				if (result == false) {
					// 오류
					return;
				}
				BrUtilManager.getInstance().ShowDialog(Scen000019.this,getString(R.string.alert_title),getString(R.string.msg_deleted));
			}
		}, params,true);
	}

	/**
	 * 목록 뷰 생성
	 * @return
	 */
	private View generateJView(final Map<String, String> map) {
		View v = getLayoutInflater().inflate(R.layout.scen00014_sub, null);
		final String postNo = map.get("postNo");
		final String type = map.get("type");

		CheckBox c = (CheckBox) v.findViewById(R.id.del_checke);
		boolean isChecked = map.get(KEY_DEL) == null ? false : Boolean.parseBoolean(map.get(KEY_DEL));
		c.setChecked(isChecked);
		c.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				map.put(KEY_DEL, String.valueOf(isChecked));
			}
		});
		final ImageView imageView = (ImageView) v.findViewById(R.id.sml_image);
		//final ImageView vidCover = (ImageView) v.findViewById(R.id.sml_image_vid_cover);

		TextView title = (TextView) v.findViewById(R.id.sml_title);
		title.setText(map.get("title"));
		final TextView content = (TextView) v.findViewById(R.id.sml_content);
		final TextView textContent = (TextView) v.findViewById(R.id.sml_textview);

		String contentTemp = map.get("content");
		int textCnt = contentTemp.indexOf("[1;");
		int photoCnt = contentTemp.indexOf("[2;");
		int videoCnt = contentTemp.indexOf("[3;");

		if((photoCnt == -1 && videoCnt == -1)) {
			imageView.setVisibility(View.GONE);
			textContent.setVisibility(View.VISIBLE);
			DataParsing(map.get("content"), textContent, imageView);
		}
		else {
			imageView.setVisibility(View.VISIBLE);
			textContent.setVisibility(View.GONE);
			DataParsing(map.get("content"), content, imageView);
		}

		DataParsing2(map.get("content"), content);

		//		if(contentTemp.indexOf("[2;") != -1 || contentTemp.indexOf("[3;") != -1) {
		//			DataParsing(map.get("content"), content, imageView);	
		//			textContent.setVisibility(View.GONE);
		//		}
		//		else if(contentTemp.indexOf("[2;") != -1 && contentTemp.indexOf("[3;") != -1) {
		//			textContent.setVisibility(View.GONE);
		//		}
		//		else {
		//			
		//			DataParsing(map.get("content"), textContent, imageView);	
		//			textContent.setVisibility(View.VISIBLE);
		//		}


		/*SetDataCall callback = new SetDataCall() {
			@Override
			public void onSetDataCall(int arg0, String data1, String data2) {
				switch (arg0) {
				case 1://Text
					content.setText(Util.emptyStr(data1));
					break;
				case 2: case 3:// IMAGE, Video
					if (data1.isEmpty() && data2.isEmpty()) {
						//아무것도 하지 않음.
					}else {
						//String imgDownUrl = getUrl(R.string.url_img_down) + data1;
						String imgDownUrl = Common.ImageUrl + data1;
						cache.loadAsync(imgDownUrl, new ImageCallback() {
							@Override
							public void onImageLoaded(Drawable image, String url, String path) {
								ImageCache.setImageFitImageView(imageView, image);							
							}
						}, Scen000019.this);
					}
					if (arg0 == 3) {
						vidCover.setVisibility(View.VISIBLE);
					}

					break;
				default:
					break;
				}
			}
		};*/
		//Dataparsing parasing = Dataparsing.getInstance();
		//parasing.setOnDataCall(callback);
		//parasing.DataParsing(map.get("content"));
		TextView heart = (TextView) v.findViewById(R.id.sml_heart);
		heart.setText(map.get("heartCnt"));
		TextView reply = (TextView) v.findViewById(R.id.sml_reply);
		reply.setText(map.get("replyCnt"));
		TextView scrap = (TextView) v.findViewById(R.id.sml_scrap);
		scrap.setText(map.get("scrapCnt"));

		return v;
	}
	public void setText(String text, TextView textview){
		textview.setText(text);
	}
	public void setImage(String imgurl,ImageView view){
//		view.setImageUrl( imgurl, ImageCacheManager.getInstance().getImageLoader());
		myApp.imageloder.displayImage(Common.ImageUrl + imgurl,
				view, myApp.options, new ImageLoadingListener() {
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

	public void setVideoImage(String imgurl, ImageView view) {
//		view.setImageUrl( imgurl, ImageCacheManager.getInstance().getImageLoader());
		myApp.imageloder.displayImage(imgurl,
				view, myApp.options, new ImageLoadingListener() {
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
					if(values2[2].startsWith("http:")) {
						setVideoImage(values2[2], imageview);
					}
					else {
						setImage(values2[2], imageview);
					}
				}
			}
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
	}

	public void DataParsing2(String data, TextView textview) {
		try {
			String[]  values = data.split("]");
			for(int i = 0 ; i < values.length ;i++){
				String[]  values2 = values[i].split(";");

				values2[0] = values2[0].substring(1);
				if(values2[0].equals("1")) {
					values2[1] = values2[1].replace("\n", "");
					setText(values2[1], textview);
				}
			}
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
	}

	//	public void DataParsing(String data,TextView
	//			 textview, ImageView imageview){
	//		try {
	//			String[]  values = data.split("]");
	//			for(int i = 0 ; i < values.length ;i++){
	//				String[]  values2 = values[i].split(";");
	//				values2[0] = values2[0].substring(1);
	//				if(values2[0].equals("1")){
	//					setText(values2[1], textview);
	//				}else if(values2[0].equals("2")){
	//					setImage(values2[1], imageview);	
	//					break;
	//				}else if(values2[0].equals("3")){
	//					setImage(values2[2], imageview);	
	//				}
	//			}
	//		} catch (Exception e) {
	//			
	//			
	//			WriteFileLog.writeException(e);
	//		}
	//	}


	/**
	 * CheckMode 변경
	 */
	private void changeCheckMode() {
		isDelSelectMode = true;
		adapter.notifyDataSetChanged();
	}

	/**
	 * 삭제목록 생성
	 */
	private String deleteCheckList() {
		StringBuilder sb = new StringBuilder();
		for (Map<String, String> tmp : scrabData) {
			boolean isChecked = tmp.get(KEY_DEL) == null ? false : Boolean.parseBoolean(tmp.get(KEY_DEL));
			if (isChecked) {
				sb.append(tmp.get("scrapNo") + ",");
			}
		}
		return sb.toString().substring(0, sb.length()-1);
	}

	@Override
	public void onBackPressed() {
		if (isDelSelectMode) {
			isDelSelectMode = false;
			changeCheckMode();
			return;
		}

		super.onBackPressed();
	}


	/**
	 * @author 이병훈
	 * 마이스크랩 목록 아답타
	 */
	class MyScrapAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return scrabData.size();
		}

		@Override
		public Object getItem(int arg0) {
			return scrabData.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			Map<String, String> tmp = scrabData.get(arg0);
			View v = generateJView(tmp);
			if (isDelSelectMode) {
				v.findViewById(R.id.del_checke).setVisibility(View.VISIBLE);
			}else {
				v.findViewById(R.id.del_checke).setVisibility(View.GONE);
			}
			return v;
		}
	}
}
