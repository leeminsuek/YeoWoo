package com.foxrainbxm.profile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brainyxlib.BrUtilManager;
import com.brainyxlib.image.BrImageUtilManager;
import com.foxrainbxm.Common;
import com.foxrainbxm.R;
import com.foxrainbxm.WriteFileLog;
import com.foxrainbxm.base.BaseActivity;
import com.foxrainbxm.skmin.setting.Setting00020;
import com.foxrainbxm.util.HttpUtil;
import com.foxrainbxm.util.HttpUtil.OnAfterParsedData;
import com.foxrainbxm.util.HttpUtil.PageVO;
import com.foxrainbxm.util.ImageCache;
import com.foxrainbxm.util.RecycleUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

/**
 * 
 * @author 이병훈<Br/>
 * 화면ID : S00013<Br/>
 * 화면명: 프로필관리<Br/>
 */
public class Scen000013 extends BaseActivity implements OnClickListener{
	
	private ImageView profileImgTmb;
	private ImageView profileImg;
	String cropPath ;
	private TextView nickname, nickname_cnt;
	private TextView address, address_cnt;
	private TextView greeting, greeting_cnt;
	private TextView moto, moto_cnt;
	private TextView myPoint;
	File newProfileImage = null;
	Uri cropFromUri = null;
	Bitmap photo;
	private final int CROP_FROM_CAMERA = 1111;
	private LinearLayout mLoadingLayout;
	@Override
	protected void onDestroy() {
		
		try {
			newProfileImage = null;
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
		setContentView(R.layout.scen00013);
		
		if (myApp.getUserInfo().getId().isEmpty()) {
			return;
		}
		
		initialize();
	};
	private void initialize(){
		
		ImageCache cache = ImageCache.getInstance();
		
		profileImgTmb = (ImageView) findViewById(R.id.profile_image);
		profileImg = (ImageView) findViewById(R.id.imageView1);
		mLoadingLayout = (LinearLayout) findViewById(R.id.linearLayout_loading);
		profileImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//앨범 호출
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
				startActivityForResult(intent, 0);
			}
		});
		
//		if (myApp.getUserInfo().getProfilephoto().isEmpty()) {
//			ImageCache.setImageFitImageView(profileImg, getResources().getDrawable(R.drawable.bg_profile_round_n));
//			ImageCache.setImageFitImageView(profileImgTmb, getResources().getDrawable(R.drawable.profile_title_n));
//		}else {
			
			myApp.imageloder.displayImage(Common.ImageUrl + myApp.getUserInfo().getProfilephoto(), profileImg, new ImageLoadingListener() {
				
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
					Common.profileBitmap = arg2;
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					// TODO Auto-generated method stub
					
				}
			});
			myApp.imageloder.displayImage(Common.ImageUrl + myApp.getUserInfo().getProfilephoto(), profileImgTmb, new ImageLoadingListener() {
				
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
			
		/*	cache.loadAsync(Common.ImageUrl + myApp.getUserInfo().getProfilephoto(), 
					new ImageCallback() {
				@Override
				public void onImageLoaded(Drawable image, String url, String path) {
					//profileImg.setBackground(image);
					//profileImgTmb.setBackground(image);
					ImageCache.setImageFitImageView(profileImg, image);
					ImageCache.setImageFitImageView(profileImgTmb, image);
				}
			}, this);*/
//		}
		
		nickname = (TextView) findViewById(R.id.nickname);
		nickname_cnt = (TextView) findViewById(R.id.nickname_cnt);
		bindTextCounter(nickname, nickname_cnt, 12);
		
		address = (TextView) findViewById(R.id.address);
		address_cnt = (TextView) findViewById(R.id.address_cnt);
		bindTextCounter(address, address_cnt, 10);
		
		greeting = (TextView) findViewById(R.id.greeting);
		greeting_cnt = (TextView) findViewById(R.id.greeting_cnt);
		bindTextCounter(greeting, greeting_cnt, 42);
		
		moto = (TextView) findViewById(R.id.moto);
		moto_cnt = (TextView) findViewById(R.id.moto_cnt);
		bindTextCounter(moto, moto_cnt, 44);
		
		myPoint = (TextView) findViewById(R.id.my_point);
		findViewById(R.id.title_ok).setOnClickListener(this);
		findViewById(R.id.title_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		findViewById(R.id.title_scrap).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//마이스크랩 화면으로 이동
				startActivity(new Intent(getApplicationContext(), Scen000019.class));
			}
		});
		
	
		
		//프로필 정보 로드
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(HttpUtil.KEY_URL, getString(R.string.url_profile));
		params.put("uid", myApp.getUserInfo().getId());

		HttpUtil util = HttpUtil.getInstance();
		util.requestHttp(Scen000013.this, new OnAfterParsedData() {


			@Override
			public void onResult(boolean result, Object resultData,
					PageVO pageResult) {
				// TODO Auto-generated method stub
				if (resultData == null) {
					//
					return;
				}
				
				ArrayList<Map<String, String>> data = (ArrayList<Map<String, String>>) resultData;
				if (data.size() == 0) {
					//조회 안됨
					return;
				}
				
				nickname.setText(data.get(0).get("nickName"));
				address.setText(data.get(0).get("region"));
				greeting.setText(data.get(0).get("greeting"));
				moto.setText(data.get(0).get("motto"));
				myPoint.setText(String.format("%s point", data.get(0).get("myPoint")!= null ? data.get(0).get("myPoint") : "0"));
				
				
				myApp.imageloder.displayImage(Common.ImageUrl + data.get(0).get("profilephoto"), profileImg, new ImageLoadingListener() {
					
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
				myApp.imageloder.displayImage(Common.ImageUrl + data.get(0).get("profilephoto"), profileImgTmb,  new ImageLoadingListener() {
					
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
				
				
			/*	ImageCache.getInstance().loadAsync(getUrl(R.string.url_img_down) + data.get(0).get("profilephoto"), 
						new ImageCallback() {
					@Override
					public void onImageLoaded(Drawable image, String url, String path) {
						applyPhotoImg(path);
//						ImageCache.setImageFitImageView(profileImg, image);
//						ImageCache.setImageFitImageView(profileImgTmb, image);
					}
				}, Scen000013.this);*/
				
				
			}
		}, params ,true);
	}
	
	public void settingView(View v){
		startActivity(new Intent(Scen000013.this, Setting00020.class));
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK && requestCode == 0) {
			Uri selectedImageURI = data.getData();
			String imgUri = getRealPathFromURI(selectedImageURI);
			

			File outFilePath = new File(myApp.getFileDir_Ex() + "/tmp_"+ System.currentTimeMillis() + ".jpg");

			cropFromUri = Uri.fromFile(outFilePath);

			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(selectedImageURI, "image/*");
			intent.putExtra("outputX", 200); // crop한 이미지의 x축 크기
			intent.putExtra("outputY", 200); // crop한 이미지의 y축 크기
			intent.putExtra("aspectX", 2); // crop 박스의 x축 비율 
			intent.putExtra("aspectY", 2); // crop 박스의 y축 비율
			intent.putExtra("scale", true);
			intent.putExtra("outputFormat", "JPEG");
			intent.putExtra("output", cropFromUri); // 파일로 저장
			try {
				startActivityForResult(intent, CROP_FROM_CAMERA);
			} catch (Exception e) {
				e.getMessage();
			}
	        
			
		}
		else if(resultCode == RESULT_OK && requestCode == CROP_FROM_CAMERA) {
			if (cropFromUri != null) {
//				mSelectImg.clear();
//				mPictureBitmapArr.clear();
				try {
				
					cropPath = cropFromUri.getPath();
					AsyncTask<Void, Void, Bitmap> loadImage = new AsyncTask<Void, Void, Bitmap>() {

						@Override
						protected Bitmap doInBackground(Void... params) {
							try {
								photo = Images.Media.getBitmap(getContentResolver(),cropFromUri);
							} catch (FileNotFoundException e) {
								WriteFileLog.writeException(e);
							} catch (IOException e) {
								WriteFileLog.writeException(e);
							}
							ByteArrayOutputStream byteArr = new ByteArrayOutputStream();
							Bitmap curThumb = photo;
							curThumb.compress(CompressFormat.JPEG, 70, byteArr);
							
							if (cropPath.startsWith("http")) {
//								myApp.imageloder.displayImage(cropPath, profileImg);
//								myApp.imageloder.displayImage(cropPath, profileImgTmb);
//								Common.profileBitmap = getImageFromURL(cropPath);
							}else {
								Bitmap resize = BrImageUtilManager.getInstance().getBitmap(cropPath, true,BrUtilManager.getInstance().getScreenWidth(Scen000013.this),BrUtilManager.getInstance().getScreenHeight(Scen000013.this),true);

								File testfile = new File(myApp.getFileDir_Ex() + Common.getFileKey() + ".jpg");
								boolean flag = BrImageUtilManager.getInstance().saveOutput(Scen000013.this,resize,getImageUri(testfile.getAbsolutePath()));
								newProfileImage = testfile;
								
								return resize;
//								applyPhotoImg(cropPath);
							}
							
							return null;
						}

						@Override
						protected void onPreExecute() {
							mLoadingLayout.setVisibility(View.VISIBLE);
							super.onPreExecute();
						}

						@Override
						protected void onPostExecute(Bitmap result) {
							super.onPostExecute(result);

							if (cropPath.startsWith("http")) {
								myApp.imageloder.displayImage(cropPath, profileImg);
								myApp.imageloder.displayImage(cropPath, profileImgTmb);
								Common.profileBitmap = getImageFromURL(cropPath);
							}else {
								profileImg.setImageBitmap(result);
								profileImgTmb.setImageBitmap(result);
//								applyPhotoImg(cropPath);
							}
							
							mLoadingLayout.setVisibility(View.GONE);
						}

						@Override
						protected void onProgressUpdate(Void... values) {
							super.onProgressUpdate(values);
						}
					};

					loadImage.execute();
				} catch (Exception e) {

				}
				
//				Bitmap photo = null;
//				String cropPath  = cropFromUri.getPath();
//				try {
//					photo = Images.Media.getBitmap(getContentResolver(),cropFromUri);
//				} catch (FileNotFoundException e) {
//					WriteFileLog.writeException(e);
//				} catch (IOException e) {
//					WriteFileLog.writeException(e);
//				}
//				ByteArrayOutputStream byteArr = new ByteArrayOutputStream();
//				Bitmap curThumb = photo;
//				curThumb.compress(CompressFormat.JPEG, 70, byteArr);
//				
//				if (cropPath.startsWith("http")) {
//					//웹상의 이미지 이므로 다운로드 필요.
//					
//					myApp.imageloder.displayImage(cropPath, profileImg);
//					myApp.imageloder.displayImage(cropPath, profileImgTmb);
//					Common.profileBitmap = getImageFromURL(cropPath);
//					
//				/*	ImageCache.getInstance().loadAsync(imgUri, new ImageCallback() {
//						@Override
//						public void onImageLoaded(Drawable image, String url, String path) {
//							newProfileImage = new File(path);
//							applyPhotoImg(path);
//						}
//					}, Scen000013.this);*/
//				}else {
//					//newProfileImage = new File(imgUri);
//					applyPhotoImg(cropPath);
//				}
			}
			
			
			
		}
	}
	
	public void applyPhotoImg(String imgUri){
		try {
		/*	BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			bmOptions.inDither = false;
			bmOptions.inSampleSize = 2;
//			bmOptions.inJustDecodeBounds = true;
		    Bitmap tmp = BitmapFactory.decodeFile(imgUri, bmOptions);
		    
		    ExifInterface exif = new ExifInterface(imgUri);
		    int exifOrientation = exif.getAttributeInt(
		 	ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
		    int exifDegree = exifOrientationToDegrees(exifOrientation);
		    tmp = BrImageUtilManager.getInstance().rotate(tmp, exifDegree);*/
		    
		    //임시용으로 서버에 전송 시 다운로드 후 사용되게됨
		
		    
			Bitmap resize = BrImageUtilManager.getInstance().getBitmap(imgUri, true,BrUtilManager.getInstance().getScreenWidth(this),BrUtilManager.getInstance().getScreenHeight(this),true);

			File testfile = new File(myApp.getFileDir_Ex() + Common.getFileKey() + ".jpg");
			boolean flag = BrImageUtilManager.getInstance().saveOutput(this,resize,getImageUri(testfile.getAbsolutePath()));
			newProfileImage = testfile;
			profileImg.setImageBitmap(resize);
			profileImgTmb.setImageBitmap(resize);
			
			/*ImageLoader.getInstance().displayImage(imgUri, profileImg);
			ImageLoader.getInstance().displayImage(imgUri, profileImgTmb);
			
			ImageCache.setImageFitImageView(profileImg, tmp);
			ImageCache.setImageFitImageView(profileImgTmb, tmp);*/
		} catch (OutOfMemoryError e) {
			WriteFileLog.write("Scen 000013.java // out of Memory::");
		} 
	}
	
	private Uri getImageUri(String path) {
		return Uri.fromFile(new File(path));
	}

	/**
	 * EXIF정보를 회전각도로 변환하는 메서드
	 * 
	 * @param exifOrientation EXIF 회전각
	 * @return 실제 각도
	 */
	public int exifOrientationToDegrees(int exifOrientation)
	{
		if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90)
		{
			return 90;
		}
		else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180)
		{
			return 180;
		}
		else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270)
		{
			return 270;
		}
		return 0;
	}
	
	/**
	 * 실제 경로 조회
	 * @param contentURI
	 * @return
	 */
	private String getRealPathFromURI(Uri contentURI) {
	    String result;
	    Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
	    if (cursor == null) { 
	        result = contentURI.getPath();
	    } else { 
	        cursor.moveToFirst(); 
	        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
	        result = cursor.getString(idx);
	        cursor.close();
	    }
	    return result;
	}

	/**
	 * 글자수 체크
	 * @param input 
	 * @param counting 
	 * @param max 
	 */
	private void bindTextCounter(TextView input, final TextView counting, final int max) {
		input.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				counting.setText(String.format("%d/%d", arg0.length(),max));
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.title_ok:
			saveUserInfo();
			break;
		default:
			break;
		}
	}
	/**
	 * 사용자 정보 저장
	 */
	private void saveUserInfo() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(HttpUtil.KEY_URL, getString(R.string.url_profileUpdate));
		params.put("nickname", nickname.getText().toString());
		params.put("address", address.getText().toString());
		params.put("greeting", greeting.getText().toString());
		params.put("moto", moto.getText().toString());
		params.put("userId", myApp.getUserInfo().getId());
		
		if (newProfileImage != null && newProfileImage.exists()) {
			params.put("profileImage", newProfileImage);
		}
		
		HttpUtil util = HttpUtil.getInstance();
		util.requestHttp(Scen000013.this, new OnAfterParsedData() {
			@Override
			public void onResult(boolean result, Object resultData,
					PageVO pageResult) {
				// TODO Auto-generated method stub
				if (resultData == null) {
					BrUtilManager.getInstance().ShowDialog(Scen000013.this, getString(R.string.alert_title), "프로필 수정 실패되었습니다. 잠시후 다시시도해주세요.");
				}else {
					ArrayList<Map<String, String>> data = (ArrayList<Map<String, String>>) resultData;
					if (data.size() == 0) {
						BrUtilManager.getInstance().ShowDialog(Scen000013.this, getString(R.string.alert_title), "프로필 수정 실패되었습니다. 잠시후 다시시도해주세요.");
						//조회 안됨
						return;
					}
					
					if(data.get(0).get("profilephoto") != null && data.get(0).get("profilephoto").length() > 0){
						ImageLoader.getInstance().displayImage(Common.ImageUrl + data.get(0).get("profilephoto"), profileImg);
						ImageLoader.getInstance().displayImage(Common.ImageUrl + data.get(0).get("profilephoto"), profileImgTmb);
						myApp.getUserInfo().setProfilephoto(data.get(0).get("profilephoto"));
						Common.profileBitmap = getImageFromURL(Common.ImageUrl + data.get(0).get("profilephoto"));
						
					
					}
					BrUtilManager.getInstance().ShowDialog1btn(Scen000013.this, getString(R.string.alert_title), getString(R.string.msg_modified),new BrUtilManager.dialogclick() {
						
						@Override
						public void setondialogokclick() {
							// TODO Auto-generated method stub
							finish();
						}
						
						@Override
						public void setondialocancelkclick() {
							// TODO Auto-generated method stub
							
						}
					});
//					BrUtilManager.getInstance().ShowDialog(Scen000013.this, getString(R.string.alert_title), getString(R.string.msg_modified));
					
				}
			}
		}, params,false);
	}
	
	OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	};
}