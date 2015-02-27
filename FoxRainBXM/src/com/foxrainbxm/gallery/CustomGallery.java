package com.foxrainbxm.gallery;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.LayoutInflater;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brainyxlib.image.BrImageUtilManager;
import com.brainyxlib.image.ThumbImageInfo;
import com.foxrainbxm.FoxRainBXM;
import com.foxrainbxm.R;
import com.foxrainbxm.WriteFileLog;
import com.foxrainbxm.util.BrImageCacheManager;
import com.foxrainbxm.util.RecycleUtils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class CustomGallery extends Activity implements ListView.OnScrollListener, GridView.OnItemClickListener {
	boolean mBusy = false;
	ProgressDialog mLoagindDialog;
	GridView mGvImageList;
	ImageAdapter mListAdapter;
	ArrayList<ThumbImageInfo> mThumbImageInfoList;
	ArrayList<ArrayList<ThumbImageInfo>> imagebucketlist = new ArrayList<ArrayList<ThumbImageInfo>>();
	ArrayList<ThumbImageInfo> mThumbImageInfoList2 = new ArrayList<ThumbImageInfo>();
	Cursor imageCursor ;
	public HashMap<String, String> delmap = new HashMap<String, String>();
	boolean lastItemVisibleFlag;
	//int size = 0,result = 20;
	int resultsize = 0 ;
	ListView galleylist;
	LinearLayout gridlayout;
	RelativeLayout listlayout;
	GalleyAdapter gadapter;
	boolean gallryflag = false;
	int listselect = 0;
	FoxRainBXM myapp;
	ImageView title_close,btnSelectOk,title_back;
	TextView selectcount,totalcount;
	int max = 5;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_list_view);
		myapp = (FoxRainBXM)getApplicationContext();
		Intent intent = getIntent();
		resultsize = intent.getIntExtra("size", 0);
		galleylist = (ListView)findViewById(R.id.galleylist);
		mGvImageList = (GridView) findViewById(R.id.gvImageList);

		mThumbImageInfoList = new ArrayList<ThumbImageInfo>();

		listlayout = (RelativeLayout)findViewById(R.id.listlayout);
		gridlayout = (LinearLayout)findViewById(R.id.gridlayout);

		galleylist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try {
					listselect =arg2;
					mThumbImageInfoList = imagebucketlist.get(arg2);
					mListAdapter = new ImageAdapter(CustomGallery.this, R.layout.image_cell,mThumbImageInfoList);
					mGvImageList.setAdapter(mListAdapter);
					mListAdapter.notifyDataSetChanged();
					listlayout.setVisibility(View.GONE);
					gridlayout.setVisibility(View.VISIBLE);
					gallryflag = true;
					setView();
				} catch (Exception e) {
					WriteFileLog.writeException(e);
				}
			}
		});

		mGvImageList.setOnScrollListener(this);
		mGvImageList.setOnItemClickListener(this);

		mThumbImageInfoList2.clear();

		btnSelectOk = (ImageView) findViewById(R.id.btnSelectOk);
		btnSelectOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(delmap.size() > 0){
					Intent intent = new Intent();
					intent.putExtra("simg", SelectImg());
					setResult(RESULT_OK, intent);
					finish();
				}else{
					ShowDialog(CustomGallery.this, null, "이미지를 선택하지 않으셨습니다. 종료하시겠습니까?");
				}
			}
		});

		title_close = (ImageView)findViewById(R.id.title_close);
		title_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();				
			}
		});

		title_back = (ImageView)findViewById(R.id.title_back);
		title_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listlayout.setVisibility(View.VISIBLE);
				gridlayout.setVisibility(View.GONE);
				gallryflag = false;
				setView();
			}
		});

		selectcount = (TextView)findViewById(R.id.selectcount);
		selectcount.setText(String.valueOf(delmap.size()));
		totalcount = (TextView)findViewById(R.id.totalcount);
		totalcount.setText("/"+resultsize);

		findViewById(R.id.camerabtn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					setResult(RESULT_FIRST_USER);
					finish();
				} catch (Exception e) {
					WriteFileLog.writeException(e);
				}
			}
		});

		new DoFindImageList().execute();
	}

	public void setView(){
		if(!gallryflag){
			btnSelectOk.setVisibility(View.GONE);
			title_close.setVisibility(View.VISIBLE);
			title_back.setVisibility(View.GONE);
		}else{
			btnSelectOk.setVisibility(View.VISIBLE);
			title_close.setVisibility(View.GONE);
			title_back.setVisibility(View.VISIBLE);
		}
	}
	public void ShowDialog(Context context, String title, String msg) { // 다이얼로그
		AlertDialog.Builder a_builder;
		a_builder = new AlertDialog.Builder(context);
		a_builder.setTitle(title).setMessage(msg).setCancelable(false)
		.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		}).setNegativeButton("취소", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		a_builder.show();
	}

	public ArrayList<String> SelectImg() {
		int count = 0;
		ArrayList<String>mSimg = new ArrayList<String>();
		Iterator it = delmap.entrySet().iterator();
		while (it.hasNext()) {
			Entry entry = (Entry) it.next();
			String name = (String) entry.getValue();
			mSimg.add(name);
			count ++;
		}
		return mSimg;
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		BrImageCacheManager.getInstance().resume();
	}
	@Override
	protected void onPause() {
		super.onPause();
	}
	@Override
	protected void onDestroy() {
		try {
			imageCursor.close();

			System.gc();
			RecycleUtils.recursiveRecycle(getWindow().getDecorView());	
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
		super.onDestroy();

	}
	@Override
	protected void onResume() {
		BrImageCacheManager.getInstance().resume();
		super.onResume();
	}
	@SuppressWarnings("deprecation")
	private long findThumbList() {
		long returnValue = 0;

		// Select 하고자 하는 컬럼
		String[] projection = { MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DATA ,MediaStore.Images.Media.BUCKET_DISPLAY_NAME ,MediaStore.Images.Media.BUCKET_ID };

		// 쿼리 수행
		imageCursor = managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
				null, MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " desc ," + MediaStore.Images.Media.DATE_ADDED + " desc " );

		String bucket_name= "";
		imagebucketlist.clear();
		if (imageCursor != null && imageCursor.getCount() > 0) {
			// 컬럼 인덱스
			int imageIDCol = imageCursor
					.getColumnIndex(MediaStore.Images.Media._ID);
			int imageDataCol = imageCursor
					.getColumnIndex(MediaStore.Images.Media.DATA);
			int bucket_display_name = imageCursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
			int bucket_id  = imageCursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
			// 커서에서 이미지의 ID와 경로명을 가져와서 ThumbImageInfo 모델 클래스를 생성해서
			// 리스트에 더해준다.
			try{
				while (imageCursor.moveToNext()) {
					ThumbImageInfo thumbInfo = new ThumbImageInfo();

					thumbInfo.setId(imageCursor.getString(imageIDCol));
					thumbInfo.setData(imageCursor.getString(imageDataCol));
					thumbInfo.setCheckedState(false);
					thumbInfo.setBucket_id(imageCursor.getString(bucket_id));
					thumbInfo.setBucket_name(imageCursor.getString(bucket_display_name));

					if(bucket_name.equals("")){
						mThumbImageInfoList.add(thumbInfo);
					}else if(!bucket_name.trim().equals(thumbInfo.getBucket_name().trim())){
						imagebucketlist.add(mThumbImageInfoList);
						mThumbImageInfoList = new ArrayList<ThumbImageInfo>();
						mThumbImageInfoList.clear();
						mThumbImageInfoList.add(thumbInfo);
					}else if(bucket_name.equals(thumbInfo.getBucket_name())){
						mThumbImageInfoList.add(thumbInfo);
					}
					bucket_name = thumbInfo.getBucket_name();

					returnValue++;
				}
			}catch(Exception e){
				WriteFileLog.writeException(e);
			}finally{
				imagebucketlist.add(mThumbImageInfoList);
			}

		}
		return returnValue;
	}

	// 화면에 이미지들을 뿌려준다.
	private void updateUI() {
		/*for(int i = size; i < result;i++){
			mThumbImageInfoList2.add(mThumbImageInfoList.get(i));
		}*/
		gadapter = new GalleyAdapter(CustomGallery.this, R.layout.cell_galley_row, imagebucketlist);
		galleylist.setAdapter(gadapter);
		gadapter.notifyDataSetChanged();
		//mListAdapter.notifyDataSetChanged();
	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
	}

	// 스크롤 상태를 판단한다.
	// 스크롤 상태가 IDLE 인 경우(mBusy == false)에만 이미지 어댑터의 getView에서
	// 이미지들을 출력한
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		/*	if(lastItemVisibleFlag && scrollState==OnScrollListener.SCROLL_STATE_IDLE){
			mBusy = false;
			size = size + 20;
			result = result + 20;
			updateUI();
			//mListAdapter.notifyDataSetChanged();
		}else{
			mBusy = true;
		}*/
		switch (scrollState) {
		case OnScrollListener.SCROLL_STATE_IDLE:
			mBusy = false;
			mListAdapter.notifyDataSetChanged();
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			mBusy = true;
			break;
		case OnScrollListener.SCROLL_STATE_FLING:
			mBusy = true;
			break;
		}
	}

	// 아이템 체크시 현재 체크상태를 가져와서 반대로 변경(true -> false, false -> true)시키고
	// 그 결과를 다시 ArrayList의 같은 위치에 담아준다
	// 그리고 어댑터의 notifyDataSetChanged() 메서드를 호출하면 리스트가 현재 보이는
	// 부분의 화면을 다시 그리기 시작하는데(getView 호출) 이러면서 변경된 체크상태를
	// 파악하여 체크박스에 체크/언체크를 처리한다.
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {

		ImageAdapter adapter = (ImageAdapter) arg0.getAdapter();
		ThumbImageInfo rowData = (ThumbImageInfo) adapter.getItem(position);
		boolean curCheckState = rowData.getCheckedState();

		if (curCheckState) {
			rowData.setCheckedState(!curCheckState);
			setChecked(listselect + "_" + position, rowData);
			mThumbImageInfoList.set(position, rowData);
			adapter.notifyDataSetChanged();
		} else {
			if (delmap.size() < resultsize) {
				rowData.setCheckedState(!curCheckState);
				setChecked(listselect + "_" + position, rowData);

				mThumbImageInfoList.set(position, rowData);
				adapter.notifyDataSetChanged();
			} else {
				Toast.makeText(CustomGallery.this, "더이상 선택이 불가능 합니다.", Toast.LENGTH_SHORT).show();
			}
		}
		selectcount.setText(String.valueOf(delmap.size()));
	}

	public void setChecked(String key, ThumbImageInfo rowData) {
		if (!delmap.containsKey(key)) {
			delmap.put(key, rowData.getData());
		} else {
			delmap.remove(key);
		}
	}

	// *****************************************************************************************
	// //
	// Image ListAdapter Class
	// *****************************************************************************************
	// //
	private class GalleyAdapter extends ArrayAdapter<ArrayList<ThumbImageInfo>>{
		ArrayList<ArrayList<ThumbImageInfo>> objects;
		Context context;
		int textViewResourceId;


		public GalleyAdapter(Context context, int textViewResourceId,
				ArrayList<ArrayList<ThumbImageInfo>> objects) {
			super(context, textViewResourceId, objects);

			this.context = context;
			this.objects = objects;
			this.textViewResourceId = textViewResourceId;
		}

		public class ViewHolder {
			ImageView thumanail;
			TextView name;
			TextView count;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = layoutInflater.inflate(textViewResourceId, null);
				holder = new ViewHolder();
				holder.thumanail = (ImageView)convertView.findViewById(R.id.thumnail);
				holder.name= (TextView)convertView.findViewById(R.id.bucket_name);
				holder.count= (TextView)convertView.findViewById(R.id.bucket_count);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}

			if(objects != null && objects.size() > 0){
				ArrayList<ThumbImageInfo> array = objects.get(position);
				holder.name.setText(array.get(0).getBucket_name());
				holder.count.setText(Integer.toString(array.size()));
				try {
					myapp.imageloder.displayImage("file://"+ array.get(0).getData(),holder.thumanail, myapp.options, new ImageLoadingListener() {
						@Override
						public void onLoadingStarted(String arg0, View arg1) {
						}

						@Override
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						}

						@Override
						public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
							int rotate = BrImageUtilManager.getInstance().GetExifOrientation(arg0);
							holder.thumanail.setImageBitmap(BrImageUtilManager.getInstance().rotate(arg2,rotate));
						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
						}
					});

					holder.thumanail.setVisibility(View.VISIBLE);
					setProgressBarIndeterminateVisibility(false);
					holder.thumanail.setTag(position);
				} catch (Exception e) {
					WriteFileLog.writeException(e);
					setProgressBarIndeterminateVisibility(false);
				}
				//BrImageCacheManager.getInstance().SetImageLoader("file://"+ array.get(0).getData(), holder.thumanail);
				//BrImageCacheManager.getInstance().displayImage("file://"+ array.get(0).getData(),holder.thumanail, myapp.options);
			}
			return convertView;

		}
	}



	// *****************************************************************************************
	// //
	// Image Adapter Class
	// *****************************************************************************************
	// //
	static class ImageViewHolder {
		ImageView ivImage;
		CheckBox chkImage;
	}

	private class ImageAdapter extends BaseAdapter {
		static final int VISIBLE = 0x00000000;
		static final int INVISIBLE = 0x00000004;
		private Context mContext;
		private int mCellLayout;
		private LayoutInflater mLiInflater;
		private ArrayList<ThumbImageInfo> mThumbImageInfoList;
		private SparseArray<WeakReference<View>> viewArray;


		@SuppressWarnings("unchecked")
		public ImageAdapter(Context c, int cellLayout,
				ArrayList<ThumbImageInfo> thumbImageInfoList) {
			mContext = c;
			mCellLayout = cellLayout;
			mThumbImageInfoList = thumbImageInfoList;
			this.viewArray = new SparseArray<WeakReference<View>>(thumbImageInfoList.size());
			mLiInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return mThumbImageInfoList.size();
		}

		public Object getItem(int position) {
			return mThumbImageInfoList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}
		public void update(){
			viewArray.clear();
			notifyDataSetChanged();
		}

		@SuppressWarnings("unchecked")
		public View getView(final int position, View convertView, ViewGroup parent) {
			if(viewArray != null && viewArray.get(position) != null) {
				convertView = viewArray.get(position).get();
				if(convertView != null){
					View view = (LinearLayout)convertView.getTag();
					if(view instanceof LinearLayout){			
						if(mThumbImageInfoList.get(position).getCheckedState()){
							view.setVisibility(View.VISIBLE);
						}else{
							view.setVisibility(View.GONE);
						}
					}
					return convertView;
				}
			}
			try{
				convertView = mLiInflater.inflate(mCellLayout, parent, false);

				final ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
				LinearLayout image_selected = (LinearLayout)convertView.findViewById(R.id.image_selected);
				final ProgressBar bar = (ProgressBar)convertView.findViewById(R.id.progressbar);
				
				if (((ThumbImageInfo) mThumbImageInfoList.get(position))
						.getCheckedState())
					image_selected.setVisibility(View.VISIBLE);
				else
					image_selected.setVisibility(View.GONE);
				//if (!mBusy) {
					try {
						myapp.imageloder.displayImage("file://"+ mThumbImageInfoList.get(position).getData(),
								ivImage, myapp.options, new ImageLoadingListener() {

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
								ivImage.setImageBitmap(BrImageUtilManager.getInstance().rotate(arg2,rotate));
								ivImage.setScaleType(ScaleType.CENTER_CROP);
							}

							@Override
							public void onLoadingCancelled(String arg0, View arg1) {
								bar.setVisibility(View.GONE);
							}
						});


						ivImage.setVisibility(VISIBLE);
						setProgressBarIndeterminateVisibility(false);
						ivImage.setTag(position);
					} catch (Exception e) {
						WriteFileLog.writeException(e);
						setProgressBarIndeterminateVisibility(false);
					}
					
					convertView.setTag(image_selected);
			}catch(Exception e){
				WriteFileLog.writeException(e);
			}finally{
				viewArray.put(position, new WeakReference<View>(convertView));
			}
			/*ImageViewHolder holder; 

			if (convertView == null) {
				convertView = mLiInflater.inflate(mCellLayout, parent, false);
				holder = new ImageViewHolder();

				holder.ivImage = (ImageView) convertView
						.findViewById(R.id.ivImage);
				holder.chkImage = (CheckBox) convertView
						.findViewById(R.id.chkImage);

				convertView.setTag(holder);
			}else{
				holder = (ImageViewHolder) convertView.getTag();
			}

				if (((ThumbImageInfo) mThumbImageInfoList.get(position))
						.getCheckedState())
					holder.chkImage.setChecked(true);
				else
					holder.chkImage.setChecked(false);

				if (!mBusy) {
					try {

						ImageLoader.getInstance().displayImage("file://"
								+ mThumbImageInfoList.get(position).getData(),
								holder.ivImage, UCSApplication.getOptions());

						holder.ivImage.setVisibility(VISIBLE);
						setProgressBarIndeterminateVisibility(false);
					} catch (Exception e) {
						WriteFileLog.writeException(e);
						setProgressBarIndeterminateVisibility(false);
					}
				} else {
					setProgressBarIndeterminateVisibility(true);
					holder.ivImage.setVisibility(INVISIBLE);
				}*/
//			DisplayMetrics displayMetrics = new DisplayMetrics();
//			getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//
//			int width = displayMetrics.widthPixels/3;// 가로
//			LayoutParams params = new LayoutParams(width, LayoutParams.MATCH_PARENT);
////			
////			convertView.setLayoutParams(params);
//			LayoutParams ss = convertView.getLayoutParams();
//			ss = params;
//			convertView.setLayoutParams(ss);
			return convertView;
		}
	}

	/*DisplayImageOptions options = null;
	public DisplayImageOptions getImageOptions() {
		if(options == null) {
			options = new DisplayImageOptions.Builder()
			//.showStubImage(R.drawable.img_copic)
			//.showImageForEmptyUrl(R.drawable.img_copic)
			.cacheInMemory()
			.cacheOnDisc()	
			.decodingType(DecodingType.MEMORY_SAVING) 
			.build();
		}
		return options;
	}	*/

	// *****************************************************************************************
	// //
	// Image Adapter Class End
	// *****************************************************************************************
	// //

	// *****************************************************************************************
	// //
	// AsyncTask Class
	// *****************************************************************************************
	// //
	private class DoFindImageList extends AsyncTask<String, Integer, Long> {
		@Override
		protected void onPreExecute() {
			mLoagindDialog = ProgressDialog.show(CustomGallery.this, null,"이미지를 불러오는 중입니다.", true, true);
			super.onPreExecute();
		}

		@Override
		protected Long doInBackground(String... arg0) {
			long returnValue = 0;
			returnValue = findThumbList();
			return returnValue;
		}

		@Override
		protected void onPostExecute(Long result) {
			updateUI();
			mLoagindDialog.dismiss();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
	}
	// *****************************************************************************************
	// //
	// AsyncTask Class End
	// *****************************************************************************************
	// //

	@Override
	public void onBackPressed() {
		if(gallryflag){
			listlayout.setVisibility(View.VISIBLE);
			gridlayout.setVisibility(View.GONE);
			gallryflag = false;
			setView();
		}else{
			finish();
		}
	}
}