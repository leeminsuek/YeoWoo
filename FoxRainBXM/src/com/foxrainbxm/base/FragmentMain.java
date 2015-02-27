package com.foxrainbxm.base;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.brainyxlib.BrUtilManager;
import com.foxrainbxm.Common;
import com.foxrainbxm.FoxRainBXM;
import com.foxrainbxm.R;
import com.foxrainbxm.WriteFileLog;
import com.foxrainbxm.jjlim.content.ContentDetailView;
import com.foxrainbxm.profile.Scen000013;
import com.foxrainbxm.profile.Scen000019;
import com.foxrainbxm.skmin.base.SharedValues;
import com.foxrainbxm.skmin.base.SharedValues.SharedKeys;
import com.foxrainbxm.skmin.setting.Setting00019;
import com.foxrainbxm.skmin.setting.Setting00020;
import com.foxrainbxm.skmin.setting.Setting00120;
import com.foxrainbxm.skmin.setting.Setting01116;
import com.foxrainbxm.tab1.Scenario1_1;
import com.foxrainbxm.tab2.Scenario2_1;
import com.foxrainbxm.tab3.Scenario3_1;
import com.foxrainbxm.tab4.Scenario4_1;
import com.foxrainbxm.tab5.Scenario5_1;
import com.foxrainbxm.tab5.Scenario5_S00117;
import com.foxrainbxm.tab6.Scenario6_1;
import com.foxrainbxm.vo.MemberVO;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class FragmentMain extends FragmentActivity {

	FoxRainBXM myapp = null;
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	private HorizontalScrollView tabScroller;
	private RadioGroup tabGroups;
	
	
	public static int mCurrentFragmentIndex = 0;
	final static int[] tabs = {
			R.id.buttonTab1,
			R.id.buttonTab2,
			R.id.buttonTab3,
			R.id.buttonTab4,
			R.id.buttonTab5,
			R.id.buttonTab6,
			R.id.buttonTab7};
	
	@Override
	protected void onCreate(Bundle arg0) {
		try {
			super.onCreate(arg0);
			setContentView(R.layout.fragment_main);
			printHashKey();
			myapp = (FoxRainBXM) getApplicationContext();
			initialize();
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
	}
	private MemberVO sampleUser(){
		MemberVO vo = new MemberVO();
		vo.setId(SharedValues.getSharedValue(getBaseContext(), SharedKeys.ID));
		vo.setProfilephoto(SharedValues.getSharedValue(getBaseContext(), SharedKeys.PROFILEPHOTO));
		return vo;
	}
	
	@Override
	protected void onResume() {
		try {
			if(Common.profileBitmap != null) {
				ImageView profile = (ImageView) findViewById(R.id.profile_image);
				profile.setImageBitmap(	Common.profileBitmap );
			}
			else { 
				ImageView profile = (ImageView) findViewById(R.id.profile_image);
				myapp.imageloder.displayImage(Common.ImageUrl + myapp.getUserInfo().getProfilephoto(), profile, new ImageLoadingListener() {
					
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
//				Common.profileBitmap = getImageFromURL(Common.ImageUrl + myapp.getUserInfo().getProfilephoto());
			}
			
			//ImageLoader.getInstance().displayImage(Common.ImageUrl + myapp.getUserInfo().getProfilephoto(), profile);	
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
		super.onResume();
	}
	
	public  Bitmap getImageFromURL(String imageURL){
        Bitmap imgBitmap = null;
        HttpURLConnection conn = null;
        BufferedInputStream bis = null;
        
        try
        {
            URL url = new URL(imageURL);
            conn = (HttpURLConnection)url.openConnection();
            conn.connect(); 
            
            int nSize = conn.getContentLength();
            bis = new BufferedInputStream(conn.getInputStream(), nSize);
            imgBitmap = BitmapFactory.decodeStream(bis);
        }
        catch (Exception e){
            WriteFileLog.writeException(e);
        } finally{
            if(bis != null) {
                try {bis.close();} catch (IOException e) {}
            }
            if(conn != null ) {
                conn.disconnect();
            }
        }
        
        return imgBitmap;
}
	private void initialize() {
		try {
			myapp.setUserInfo(sampleUser());
			
			tabScroller = (HorizontalScrollView) findViewById(R.id.tab_scroller);
			
			mSectionsPagerAdapter = new SectionsPagerAdapter( getSupportFragmentManager());
			mViewPager = (ViewPager) findViewById(R.id.pager);
			mViewPager.setAdapter(mSectionsPagerAdapter);
			tabGroups = (RadioGroup) findViewById(R.id.tab_groups);
//			final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
//			mViewPager.setPageMargin(pageMargin);

			fragmentReplaces(mCurrentFragmentIndex);


			//=========================================== 
					findViewById(R.id.profile_image).setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							startActivity(new Intent(getApplicationContext(), Scen000013.class));
						}
					});
					//===========================================
					findViewById(R.id.title_scrap).setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							startActivity(new Intent(getApplicationContext(), Scen000019.class));
						}
					});
					
			mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageSelected(int arg0) {
//					if(arg0  == 1) {
//						Scenario2_1 sc2_1 = Scenario2_1.getInstance();
////						Scenario2_1 sc2_1 = (Scenario2_1) findFragmentByPosition(arg0);
////						sc2_1 = new Scenario2_1(arg0);
////							sc2_1.onResumeFragment();
//					}
//					else if(arg0  == 2) {
//						Scenario3_1 sc2_1 = Scenario3_1.getInstance();
////						Scenario2_1 sc2_1 = (Scenario2_1) findFragmentByPosition(arg0);
////						sc2_1 = new Scenario2_1(arg0);
////							sc2_1.onResumeFragment();
//					}
//					if(arg0  == 3) {
//						Scenario4_1 sc2_1 = Scenario4_1.getInstance();
////						Scenario2_1 sc2_1 = (Scenario2_1) findFragmentByPosition(arg0);
////						sc2_1 = new Scenario2_1(arg0);
////							sc2_1.onResumeFragment();
//					}
//					else {
//						
//					}
					
					fragmentReplaces(arg0);
//					setTabImage(mCurrentFragmentIndex);
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
				}
			});
			tabGroups.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup arg0, int arg1) {
					
					int curIdx = -1;
					for (int i = 0; i < tabs.length; i++) {
						if (tabs[i] == arg1) {
							curIdx = i;
							break;
						}
					}
					fragmentReplaces(curIdx);
				}
			});
			
			if(!myapp.notiPushNo.equals("") && !myapp.notiPushType.equals("")){
				fragmentReplaces(getTypeindex(myapp.notiPushType));
				myapp.notiPushNo = "";
				myapp.notiPushType = "";
			}
			
			Intent resultIntent = getIntent();
			int postNo = resultIntent.getIntExtra("postNo", -1);
			if(postNo != -1) {
//				if(type == 1){ //직종별 인터뷰
//				else if(type == 2){ //오픈 컨텐츠
//				else if(type == 3){ //관련뉴스
//				else if(type == 4){ //나만의 꿀팁
//				else if(type == 5){ //사용 안함
//				else if(type == 6){ //공지사항

				String viewtype = resultIntent.getStringExtra("viewtype");
				int type = Integer.parseInt(viewtype);
				if(viewtype.equals("6")) {
					fragmentReplaces(getTypeindex("7"));
				}
				else {
					fragmentReplaces(getTypeindex(viewtype));
				}
				
				Intent intent = null;
				if( viewtype.equals("6")){
//					intent = new Intent(this, Setting01116.class);
//					intent.putExtra("seq", postNo);
					intent = new Intent(this, Setting00120.class);
					intent.putExtra("seq", postNo);
					intent.putExtra("yn", true);
					
				}
				else if( viewtype.equals("5")) {
					intent = new Intent(this,Scenario5_S00117.class);
					intent.putExtra("postno",postNo);
				}
				else {
					intent = new Intent(this,ContentDetailView.class);
					intent.putExtra("postNo", postNo);
					intent.putExtra("viewtype", getTypeindex(viewtype));
				}
				
				startActivity(intent);
			}
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
		
	}
	public void printHashKey() {
	    try {
	        PackageInfo info = getPackageManager().getPackageInfo("com.foxrainbxm",
	                PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	                     
	        // 페북에 적어야할 해쉬키.
	        String hash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
	        Log.d("facebook", hash);
	        }
	    } catch (NameNotFoundException e) {
	    } catch (NoSuchAlgorithmException e) {
	    }
	}
	
	public int getTypeindex(String type){
		if(type.equals("2")){
			return 1;
		}else if(type.equals("3")){
			return 2;
		}else if(type.equals("4")){
			return 3;
		}else if(type.equals("6")){
			return 5;
		}
		else if(type.equals("5")){
			return 4;
		}
		else if(type.equals("1")){
			return 0;
		}
		else if(type.equals("7")) {
			return 6;
		}
		return 0;
	}
	
	public void fragmentReplaces(int reqNewFragmentIndex) {
		try {
			if (mCurrentFragmentIndex == reqNewFragmentIndex) {
				return;
			}
			if (reqNewFragmentIndex >= 5) {
				tabScroller.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
			}else {
				tabScroller.fullScroll(HorizontalScrollView.FOCUS_LEFT);
			}
			myapp.mCurrentIndex = reqNewFragmentIndex;
			mCurrentFragmentIndex = reqNewFragmentIndex;
			mViewPager.setCurrentItem(reqNewFragmentIndex);
			Log.d("TAG_F", "myapp.mCurrentIndex :: " + myapp.mCurrentIndex);
			Log.d("TAG_F","reqNewFragmentIndex :: " + reqNewFragmentIndex);
			Log.d("TAG_F", "ViewPager.count :: " + mViewPager.getCurrentItem());
			((RadioButton)tabGroups.getChildAt(mCurrentFragmentIndex)).setChecked(true);
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
	}

	public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
		Context mContext;
		SparseArray< View > views = new SparseArray< View >();
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);

		}
		
		public int getItemPosition(Object object) {
		    return POSITION_NONE;
		}

		
//		@Override
//		public void destroyItem(ViewGroup container, int position, Object object) {
//			super.destroyItem(container, position, object);
//			if(object instanceof Fragment){
//	            Fragment fragment    = (Fragment)object;
//	            FragmentManager fm    = fragment.getFragmentManager();
//	            FragmentTransaction ft    = fm.beginTransaction();
//	            ft.remove(fragment);
//	            ft.commit();
//	        }
//		}

		
		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				//Home
				return new Scenario1_1();
			case 1:
				return new Scenario2_1(position);
			case 2:
				return new Scenario3_1(position);
			case 3:
				return new Scenario4_1(position);
			case 4:
				return new Scenario5_1();
			case 5:
				return new Scenario6_1();
			case 6:
				//More
				return new Setting00019();
			}

			// fragmentReplace(position);
			return null;
		}

		@Override
		public int getCount() {
			return 7;
		}
	}
	
	

	public void profileView(View v){
		//startActivity(new Intent(getApplicationContext(), Scen00013.class));
	}
	
	public void settingView(View v){
		//startActivity(new Intent(getApplicationContext(), Scenario4_1.class));
		startActivity(new Intent(getApplicationContext(), Setting00020.class));
	}
	
	public void myScrab(View v){
		//startActivity(new Intent(getApplicationContext(), Setting00020.class));
	}
	
	@Override
	public void onBackPressed() {
		try {
			BrUtilManager.getInstance().ShowExitToast(this);
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
	}
	
	/**
	 * 
	 * @param listview
	 *//*
	public void removeListViewFooter(final PullToRefreshListView listview) {
		AsyncTask<Void, Void, Void> getDataWorker = new AsyncTask<Void, Void, Void>() {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				try {
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				try {
				} catch (Exception e) {
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				try {
					listview.setMode(Mode.PULL_FROM_START);
				} catch (Exception e) {
					// TODO: handle exception
				} finally {

				}

				super.onPostExecute(result);
			}
		};
		getDataWorker.execute();
	}
*/
}
