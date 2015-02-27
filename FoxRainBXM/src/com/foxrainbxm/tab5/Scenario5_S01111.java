package com.foxrainbxm.tab5;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.brainyxlib.BrUtilManager;
import com.brainyxlib.image.BrImageUtilManager;
import com.brainyxlib.image.ImageVo;
import com.brainyxlib.util.HttpRequestor;
import com.foxrainbxm.Common;
import com.foxrainbxm.R;
import com.foxrainbxm.WriteFileLog;
import com.foxrainbxm.base.BaseActivity;
import com.foxrainbxm.gallery.CustomGallery;
import com.foxrainbxm.jjlim.libary.CustomEditText;
import com.foxrainbxm.util.BrHttpManager;
import com.foxrainbxm.util.RecycleUtils;

public class Scenario5_S01111 extends BaseActivity {

	private int imgcount = 5;
	public int Camera = 1000;
	public int Video = 1001;
	public int Yutubu = 1002;
	public int PICK_FROM_CAMERA = 1003;
	public int CROP_FROM_CAMERA = 1004;
	private String mVideoPath = "";
	Bitmap mVideoThumbnail;
	String yutubulink = "", yutubuthum = "" ,  videothumpath = "";
	int position = 0;
	LinearLayout linearLayout_loading;
	ArrayList<Bitmap>bitarray = new ArrayList<Bitmap>();
	ArrayList<String> mSelectImg = new ArrayList<String>();
	ArrayList<ImageVo>filekey = new ArrayList<ImageVo>();
	RelativeLayout itemlay1,itemlay2,itemlay3,itemlay4,itemlay5;
	ImageView itemview1,itemview2,itemview3,itemview4,itemview5;
	ImageView itemdel1,itemdel2,itemdel3,itemdel4,itemdel5;

	int filetype = 0;
	CustomEditText titlearea, contentarea;
	boolean modify = false;
	boolean modifyDelYn = false;
	String title = "",content= "";
	int postno = -1;
	Uri mImageCaptureUri;
	Uri m_ImageUri;
	String m_resultUri;



	ArrayList<String> contentFiles = new ArrayList<String>();


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		try {
			if(resultCode != RESULT_OK){
				if(resultCode == RESULT_FIRST_USER){
					doTakePhotoAction();
				}else{
					filetype = 0;
					return;	 
				}
			}else{
				if(requestCode == Camera){
					mSelectImg.clear();
					mSelectImg = data.getStringArrayListExtra("simg");
					//					int size = getUriSize(data.getData());
					filetype = 1;
					SaveImglist(true);
				}
				//				else if(requestCode == Video){
				//					linearLayout_loading.setVisibility(View.VISIBLE);
				//					mSelectImg.clear();
				//					Uri uri = data.getData();
				//					String path = getPath(uri);
				//					//String path2 = GalleryUtil.getPath(this, null);
				//					
				//					//String path = getPath(uri);
				//					//String realpath = getRealPathFromURI(uri);
				//					/*String repath = path.substring(1);
				//					String name = getName(uri);
				//					String urlname = repath.substring(0, repath.lastIndexOf("/"));
				//					String mp4 = name.substring(name.lastIndexOf("."),
				//							name.length());
				//					String rename = "/tmp_"+ String.valueOf(System.currentTimeMillis()) + mp4;
				//
				//					String uriId = getUriId(uri);
				//					Log.e("###", "실제경로 : " + path + "\n파일명 : " + name + "\nuri : "
				//							+ uri.toString() + "\nuri id : " + uriId);*/
				//
				////					mVideoPath = urlname + rename;
				//					mVideoPath=path;
				//					
				//					//VideoEncoder(mVideoPath);
				//					
				//					
				//					File videoFile = new File(mVideoPath);
				//				/*	
				//					AvcEncoder encoder = new AvcEncoder(mVideoPath);
				//					encoder.close();
				//					 MediaExtractor ex =  new  MediaExtractor();
				//					 ex.setDataSource(mVideoPath);
				//					 
				//					MediaCodec codec = MediaCodec.createEncoderByType("video/mp4v-es");
				//					//codec.setVideoScalingMode(MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT)
				//					codec.start();*/
				//					double size = 0;
				//					//파일 있는지 체크후 파일 크기 구함
				//					if(videoFile.exists()) {
				//						size = videoFile.length();
				//					}
				//				
				//					Log.e("TAG", "video file size :: " +  size);
				//					
				//					//10mbx
				//					long mb10 = 10485760;
				//					if(size <= (mb10*5)) {
				//						Log.e("TAG", "mVideoPath ::  " + mVideoPath);
				//						mVideoThumbnail = ThumbnailUtils.createVideoThumbnail(mVideoPath, Thumbnails.MINI_KIND);
				//						//TODO 비디오 썸네일 생성
				//						if(mVideoThumbnail == null) {
				//							Log.e("TAG", "Thumnail null");
				//						}
				//						else {
				//							Log.e("TAG", "Thumnail not null");
				//							File testfile = new File(myApp.getFileDir_Ex() + Common.getFileKey() + ".jpg");
				//							boolean flag = BrImageUtilManager.getInstance().saveOutput(this,mVideoThumbnail,getImageUri(testfile.getAbsolutePath()));
				//							videothumpath = testfile.getAbsolutePath();
				//							
				//							//썸네일 이미지 사이즈 출력
				////							File file = new File(videothumpath);
				////							double thumbSize = 0;
				////							//파일 있는지 체크후 파일 크기 구함
				////							if(file.exists()) {
				////								thumbSize = file.length();
				////							}
				////							Log.e("TAG", "Thumb Image Size :: " +  thumbSize);
				//							
				//							itemview1.setImageBitmap(mVideoThumbnail);
				//							itemdel1.setVisibility(View.VISIBLE);
				//							itemdel1.setTag(0);
				//							filetype = 2;
				//							linearLayout_loading.setVisibility(View.GONE);
				//						}
				//					}
				//					else {
				//						Log.e("TAG", "용량 초과");
				//						BrUtilManager.getInstance().showToast(Scenario5_S01111.this, "동영상 용량은 50MB로 제한됩니다.");
				//						linearLayout_loading.setVisibility(View.GONE);
				//					}
				//					//Thumbnails.MICRO_KIND 작은사이즈 MINI_KIND중간사이즈 FULL_SCREEN_KIND 큰사이즈
				//				}else if(requestCode == Yutubu){
				//					mSelectImg.clear();
				//					yutubulink = data.getStringExtra("yurl");
				//					yutubuthum = data.getStringExtra("thumb");
				//					ImageLoader.getInstance().displayImage(yutubuthum,
				//							itemview1, myApp.options, new ImageLoadingListener() {
				//								
				//								@Override
				//								public void onLoadingStarted(String arg0, View arg1) {
				//								}
				//								
				//								@Override
				//								public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				//								}
				//								
				//								@Override
				//								public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				//								}
				//								
				//								@Override
				//								public void onLoadingCancelled(String arg0, View arg1) {
				//								}
				//							});
				//
				//					itemdel1.setVisibility(View.VISIBLE);
				//					itemdel1.setTag(0);
				//					setProgressBarIndeterminateVisibility(false);
				//					filetype = 3;
				//				}
				else if(requestCode == PICK_FROM_CAMERA){
					// 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.
					// 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.
					File outFilePath = new File(myApp.getFileDir_Ex() + "/tmp_"+ System.currentTimeMillis() + ".jpg");

					m_ImageUri = Uri.fromFile(outFilePath);

					Intent intent = new Intent("com.android.camera.action.CROP");
					intent.setDataAndType(mImageCaptureUri, "image/*");

					//intent.putExtra("outputX", 480);
					//	intent.putExtra("outputY", 480);
					/*	intent.putExtra("aspectX", 5);
					intent.putExtra("aspectY", 10);*/
					intent.putExtra("scale", true);
					intent.putExtra("outputFormat", "JPEG");
					intent.putExtra("output", m_ImageUri); // 파일로 저장
					// intent.putExtra("scale", true);
					// intent.putExtra("return-data", true); //bundle로 저장
					try {
						startActivityForResult(intent, CROP_FROM_CAMERA);
					} catch (Exception e) {
						e.getMessage();
					}
				}
				else if(requestCode == CROP_FROM_CAMERA){
					if (m_ImageUri != null) {
						mSelectImg.clear();
						filetype = 1;
						Bitmap photo = null;
						m_resultUri = m_ImageUri.getPath();
						try {
							photo = Images.Media.getBitmap(getContentResolver(),m_ImageUri);
						} catch (FileNotFoundException e) {
							WriteFileLog.writeException(e);
						} catch (IOException e) {
							WriteFileLog.writeException(e);
						}
						ByteArrayOutputStream byteArr = new ByteArrayOutputStream();
						Bitmap curThumb = photo;
						curThumb.compress(CompressFormat.JPEG, 70, byteArr);
						mSelectImg.add(m_resultUri);
						ClearImg();
						SaveImglist(false);
					}
				}
			}
		} catch (Exception e) {
			WriteFileLog.writeException(e);
			linearLayout_loading.setVisibility(View.GONE);

		}


	}

	private void doTakePhotoAction() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		// 임시로 사용할 파일의 경로를 생성
		String url = "tmp_" + String.valueOf(System.currentTimeMillis())+ ".jpg";
		mImageCaptureUri = Uri.fromFile(new File(myApp.getFileDir_Ex(), url));

		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,mImageCaptureUri);
		intent.putExtra("return-data", true);
		intent.putExtra("passok", "ok");
		startActivityForResult(intent, PICK_FROM_CAMERA);

		// finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sc5_s01111);
		init();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}


	private void init() {
		try {

			Intent intent = getIntent();
			modify =intent.getBooleanExtra("modify", false);


			//			findViewById(R.id.urllink).setOnClickListener(new OnClickListener() {
			//						@Override
			//						public void onClick(View v) {
			//							if(filekey.size() == 0 && mVideoThumbnail == null){
			//								Intent intent = new Intent(Scenario5_S01111.this,Scenario5_S11113.class);
			//								startActivityForResult(intent, Yutubu);	
			//							}else{
			//								BrUtilManager.getInstance().showToast(Scenario5_S01111.this, "여러타입을 한번에 업로드 하실수 없습니다.");
			//							}
			//						}
			//					});
			//			findViewById(R.id.video).setOnClickListener(new OnClickListener() {
			//						@Override
			//						public void onClick(View v) {
			//							
			//							if(filekey.size() == 0 && yutubuthum.length() == 0){
			//								// 앨범 호출
			//								try {
			//									/*Intent i = new Intent(Intent.ACTION_GET_CONTENT);*/
			//									Intent i = new Intent(Intent.ACTION_PICK);
			//									i.setType("video/*");
			//									startActivityForResult(i, Video);
			//									// finish();
			//								} catch (android.content.ActivityNotFoundException e) {
			//									WriteFileLog.writeException(e);
			//								}
			////								BrUtilManager.getInstance().ShowDialog1btn(Scenario5_S01111.this, getString(R.string.app_name), "동영상 올리는거 때문에 너무 고민하지 마세요.\n동영상 올리기 누르면 30초까지 가능합니다.",new BrUtilManager.dialogclick(){
			////									@Override
			////									public void setondialocancelkclick() {}
			////
			////									@Override
			////									public void setondialogokclick() {
			////										try {
			////											/*Intent i = new Intent(Intent.ACTION_GET_CONTENT);*/
			////											Intent i = new Intent(Intent.ACTION_PICK);
			////											i.setType("video/*");
			////											startActivityForResult(i, Video);
			////											// finish();
			////										} catch (android.content.ActivityNotFoundException e) {
			////											WriteFileLog.writeException(e);
			////										}
			////									}
			////								});
			//							}else{
			//								BrUtilManager.getInstance().showToast(Scenario5_S01111.this, "여러타입을 한번에 업로드 하실수 없습니다.");
			//							}
			//						}
			//					});

			findViewById(R.id.title_back).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			findViewById(R.id.camera).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if( yutubuthum.length() == 0 && mVideoThumbnail == null){
						Intent intent = new Intent(Scenario5_S01111.this,CustomGallery.class);
						intent.putExtra("size", imgcount - filekey.size());//선택가능한갯수
						startActivityForResult(intent, 1000);	
					}else{
						BrUtilManager.getInstance().showToast(Scenario5_S01111.this, "여러타입을 한번에 업로드 하실수 없습니다.");
					}
				}
			});
			linearLayout_loading = (LinearLayout)findViewById(R.id.linearLayout_loading);
			itemlay1 = (RelativeLayout)findViewById(R.id.thumbnail1);
			itemlay2 = (RelativeLayout)findViewById(R.id.thumbnail2);
			itemlay3 = (RelativeLayout)findViewById(R.id.thumbnail3);
			itemlay4 = (RelativeLayout)findViewById(R.id.thumbnail4);
			itemlay5 = (RelativeLayout)findViewById(R.id.thumbnail5);

			itemview1 = (ImageView)findViewById(R.id.btnAdd);
			itemview2 = (ImageView)findViewById(R.id.btnAdd2);
			itemview3 = (ImageView)findViewById(R.id.btnAdd3);
			itemview4 = (ImageView)findViewById(R.id.btnAdd4);
			itemview5 = (ImageView)findViewById(R.id.btnAdd5);
			itemdel1 = (ImageView)findViewById(R.id.btnAddDel);
			itemdel1.setOnClickListener(delclick);
			itemdel2 = (ImageView)findViewById(R.id.btnAddDel2);
			itemdel2.setOnClickListener(delclick);
			itemdel3 = (ImageView)findViewById(R.id.btnAddDel3);
			itemdel3.setOnClickListener(delclick);
			itemdel4 = (ImageView)findViewById(R.id.btnAddDel4);
			itemdel4.setOnClickListener(delclick);
			itemdel5 = (ImageView)findViewById(R.id.btnAddDel5);
			itemdel5.setOnClickListener(delclick);

			titlearea = (CustomEditText)findViewById(R.id.titlearea);
			contentarea = (CustomEditText)findViewById(R.id.contentarea);
			if(modify){
				//TODO 수정하기 받기
				title = intent.getStringExtra("title");
				titlearea.setText(title);
				content = intent.getStringExtra("content");
				contentarea.setText(content);
				postno = intent.getIntExtra("postno", -1);

				contentFiles.clear();
				//					filetype = 1;
				mSelectImg = intent.getStringArrayListExtra("files");
				int type = Integer.parseInt(intent.getStringExtra("type"));
				if(type == 2) filetype = 1;
				else filetype = 2;

				SaveImglist();
				//					for(int i = 0 ; i < mSelectImg.size() ; i ++) { 
				////						loadingImg(i);
				//						
				//						bitarray.add(getImageFromURL(mSelectImg.get(i)));
				//						loadingImg(i);
				////						loadingImg(bitarray.get(i), i);
				//						ImageVo vo = new ImageVo();
				//						vo.setImg_url(mSelectImg.get(i));
				//						//vo.setImg_url(mSelectImg.get(i).toString());
				//						vo.setImg_flag(false);
				//						filekey.add(vo);
				//						Thread.sleep(1000);
				//					}
				//					SaveImglist(true);
				//					for ( int i = 0 ; i < contentFiles.size() ; i ++) {
				//						Log.d("TAG", contentFiles.get(i));
				//					}

			}
			findViewById(R.id.title_ok).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						if(BrUtilManager.getInstance().getEditTextNullCheck(titlearea)){
							if(BrUtilManager.getInstance().getEditTextNullCheck(contentarea)){
								BrUtilManager.getInstance().InputKeybordHidden(Scenario5_S01111.this, titlearea);
								BrUtilManager.getInstance().InputKeybordHidden(Scenario5_S01111.this, contentarea);

								//								if(filetype == 2) {
								AddTip();
								//								}
								//									UpdateService
								//									if(modify) { 
								//										AddTip();
								//									}
								//									else {
								//										Intent serviceIntent = new Intent(getBaseContext(), UpdateService.class);
								//										serviceIntent.putExtra("postNo", String.valueOf(postno));
								//										serviceIntent.putExtra("type", String.valueOf(filetype));
								//										serviceIntent.putExtra("title", titlearea.getText().toString());
								//										serviceIntent.putExtra("contents", contentarea.getText().toString());
								//										serviceIntent.putExtra("id", myApp.getUserInfo().getId());
								//										serviceIntent.putExtra("localVideo", mVideoPath);
								//										serviceIntent.putExtra("localThumbnail", videothumpath);
								//										startService(serviceIntent);
								//										finish();
								//									}
								//								}
								//								else { 
								//									AddTip();
								//								}

							}else{
								BrUtilManager.getInstance().showToast(Scenario5_S01111.this, "내용을 입력하세요");	
							}
						}else{
							BrUtilManager.getInstance().showToast(Scenario5_S01111.this, "제목을 입력하세요");
						}
					} catch (Exception e) {
						WriteFileLog.writeException(e);
					}
				}
			});
			//	addItemView("");
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
	}

	public OnClickListener delclick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v.getTag() != null){
				int position= (Integer)v.getTag();
				v.setTag(null);
				DeleteImg(position);
			}
		}
	};

	public void DeleteImg(final int position){
		BrUtilManager.getInstance().ShowDialog2btn(Scenario5_S01111.this, getString(R.string.app_name), "이미지를 삭제하시겠습니까?", new BrUtilManager.dialogclick() {
			@Override
			public void setondialogokclick() {
				try {
					if(filetype == 1){
						filekey.remove(position);
						bitarray.remove(position);	
					}else if(filetype == 2){
						mVideoPath= "";
						videothumpath = "";
						mVideoThumbnail = null;
					}else if(filetype ==3){
						yutubulink = "";
						yutubuthum  = "";
					}

					ClearImg();
					SelectImg();
				} catch (Exception e) {
					WriteFileLog.writeException(e);
				}

			}
			@Override
			public void setondialocancelkclick() {
			}
		});

	}

	public void ClearImg(){
		try{
			Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.btn_add);
			itemview1.setImageBitmap(img);
			itemview1.setTag(null);
			itemlay1.setVisibility(View.VISIBLE);
			itemlay2.setVisibility(View.GONE);
			itemlay3.setVisibility(View.GONE);
			itemlay4.setVisibility(View.GONE);
			itemlay5.setVisibility(View.GONE);
			itemdel1.setVisibility(View.GONE);
			itemview2.setImageBitmap(img);
			itemview2.setTag(null);
			itemdel2.setVisibility(View.GONE);
			itemview3.setImageBitmap(img);
			itemview3.setTag(null);
			itemdel3.setVisibility(View.GONE);
			itemview4.setImageBitmap(img);
			itemview4.setTag(null);
			itemdel4.setVisibility(View.GONE);
			itemview5.setImageBitmap(img);
			itemview5.setTag(null);
			itemdel5.setVisibility(View.GONE);
		}catch(Exception e){
			WriteFileLog.writeException(e);
		}
	}

	public void SaveImglist() {
		try {
			AsyncTask<Context, Void, Void> SendFavor = new AsyncTask<Context, Void, Void>() {
				@Override
				protected void onPreExecute() {
					linearLayout_loading.setVisibility(View.VISIBLE);
					super.onPreExecute();
				}

				@Override
				protected void onPostExecute(Void result) {
					SelectImg();
					//linearLayout_loading.setVisibility(View.GONE);
					super.onPostExecute(result);
				}

				@Override
				protected Void doInBackground(Context... params) {
					cacheImg();
					return null;
				}

				@Override
				protected void onCancelled() {
					super.onCancelled();
				}

			};
			SendFavor.execute();
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
	}
	public void SaveImglist(final boolean flag) {
		try {
			AsyncTask<Context, Void, Void> SendFavor = new AsyncTask<Context, Void, Void>() {
				@Override
				protected void onPreExecute() {
					linearLayout_loading.setVisibility(View.VISIBLE);
					super.onPreExecute();
				}

				@Override
				protected void onPostExecute(Void result) {
					SelectImg();
					//linearLayout_loading.setVisibility(View.GONE);
					super.onPostExecute(result);
				}

				@Override
				protected Void doInBackground(Context... params) {
					SaveImg(flag);
					return null;
				}

				@Override
				protected void onCancelled() {
					super.onCancelled();
				}

			};
			SendFavor.execute();
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
	}

	public void loadingImg() {
	}

	public void SelectImg() {
		try {
			int j = 0;
			for (int i = 0; i < filekey.size(); i++) {
				if (filekey.get(i) != null && !filekey.get(i).equals("")) {
					//						loadingImg(i);
					if (filekey.get(i).isImg_flag()) {
						if(filekey.get(i).getImg_url().startsWith("http")) {
							loadingImg(i);
						}
						else {
							SetImg(i, null, filekey.get(i).getImg_url(), filekey.get(i).isImg_flag());
						}


					} else {
						if(filekey.get(i).getImg_url().startsWith("http")) {
							loadingImg(i);
						}
						else {
							SetImg(i, bitarray.get(j), filekey.get(i).getImg_url(), filekey.get(i).isImg_flag());
							j++;
						}
					}
				}
			}
			linearLayout_loading.setVisibility(View.GONE);
			if(mVideoThumbnail != null) { 
				itemview1.setImageBitmap(mVideoThumbnail);
				itemdel1.setVisibility(View.VISIBLE);
				itemdel1.setTag(0);
				itemlay1.setVisibility(View.VISIBLE);
			}
			/*if (filekey == null || filekey.size() == 0) {
				setInvisible(linearLayoutLoading);
			} else {
				setVisible(linearLayoutLoading);
			}*/
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
	}

	@Override
	public void onDestroy() {
		try {
			System.gc();
			for(int i = 0 ; i< bitarray.size();i++){
				bitarray.get(i).recycle();
			}
			if(Scenario5_1.getinstance() != null){
				Scenario5_1.getinstance().Update();
			}

			if(mVideoThumbnail != null) mVideoThumbnail.recycle();


			RecycleUtils.recursiveRecycle(getWindow().getDecorView());	
		} catch (Exception e) {
		}


		super.onDestroy();
	}
	public void loadingImg(Bitmap url, int index) {
		try {
			switch (index) {
			case 0:
				itemview1.setImageBitmap(url);
				itemdel1.setVisibility(View.VISIBLE);
				itemdel1.setTag(index);
				itemlay1.setVisibility(View.VISIBLE);
				break;
			case 1:
				itemdel2.setVisibility(View.VISIBLE);
				itemview2.setImageBitmap(url);
				itemdel2.setTag(index);
				itemlay2.setVisibility(View.VISIBLE);
				break;
			case 2:
				itemview3.setImageBitmap(url);
				itemdel3.setVisibility(View.VISIBLE);
				itemdel3.setTag(index);
				itemlay3.setVisibility(View.VISIBLE);
				break;
			case 3:
				itemview4.setImageBitmap(url);
				itemdel4.setVisibility(View.VISIBLE);
				itemdel4.setTag(index);
				itemlay4.setVisibility(View.VISIBLE);
				break;
			case 4:
				itemview5.setImageBitmap(url);
				itemdel5.setVisibility(View.VISIBLE);
				itemdel5.setTag(index);
				itemlay5.setVisibility(View.VISIBLE);
				break;
			}
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
	}

	public void loadingImg(int index) {
		try {
			switch (index) {
			case 0:
				myApp.imageloder.displayImage(mSelectImg.get(index), itemview1, myApp.options);
				itemdel1.setVisibility(View.VISIBLE);
				itemdel1.setTag(index);
				itemlay1.setVisibility(View.VISIBLE);
				break;
			case 1:
				myApp.imageloder.displayImage(mSelectImg.get(index), itemview2, myApp.options);
				itemdel2.setVisibility(View.VISIBLE);
				itemdel2.setTag(index);
				itemlay2.setVisibility(View.VISIBLE);
				break;
			case 2:
				myApp.imageloder.displayImage(mSelectImg.get(index), itemview3, myApp.options);
				itemdel3.setVisibility(View.VISIBLE);
				itemdel3.setTag(index);
				itemlay3.setVisibility(View.VISIBLE);
				break;
			case 3:
				myApp.imageloder.displayImage(mSelectImg.get(index), itemview4, myApp.options);
				itemdel4.setVisibility(View.VISIBLE);
				itemdel4.setTag(index);
				itemlay4.setVisibility(View.VISIBLE);
				break;
			case 4:
				myApp.imageloder.displayImage(mSelectImg.get(index), itemview5, myApp.options);
				itemdel5.setVisibility(View.VISIBLE);
				itemdel5.setTag(index);
				itemlay5.setVisibility(View.VISIBLE);
				break;
			}
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
	}



	public void SetImg(int index, Bitmap url, String filename, boolean flag) {
		try {
			String fileurl = Common.ImageUrl + filename;
			switch (index) {
			case 0:
				itemview1.setImageBitmap(url);
				itemdel1.setVisibility(View.VISIBLE);
				itemdel1.setTag(index);
				itemlay1.setVisibility(View.VISIBLE);
				break;
			case 1:
				itemview2.setImageBitmap(url);
				itemdel2.setVisibility(View.VISIBLE);
				itemdel2.setTag(index);
				itemlay2.setVisibility(View.VISIBLE);
				break;
			case 2:
				itemview3.setImageBitmap(url);
				itemdel3.setVisibility(View.VISIBLE);
				itemdel3.setTag(index);
				itemlay3.setVisibility(View.VISIBLE);
				break;
			case 3:
				itemview4.setImageBitmap(url);
				itemdel4.setVisibility(View.VISIBLE);
				itemdel4.setTag(index);
				itemlay4.setVisibility(View.VISIBLE);
				break;
			case 4:
				itemview5.setImageBitmap(url);
				itemdel5.setVisibility(View.VISIBLE);
				itemdel5.setTag(index);
				itemlay5.setVisibility(View.VISIBLE);
				break;
			}
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
	}

	private Uri getImageUri(String path) {
		return Uri.fromFile(new File(path));
	}


	public void cacheImg() { 
		try {
			if(filetype == 1) {
				for (int i = 0; i < mSelectImg.size(); i++) {
					ImageVo vo = new ImageVo();
					Bitmap resize = getImageFromURL(mSelectImg.get(i));
					File testfile = new File(myApp.getFileDir_Ex() + Common.getFileKey() + ".jpg");
					boolean flag = BrImageUtilManager.getInstance().saveOutput(this,resize,getImageUri(testfile.getAbsolutePath()));
					if (!flag)
						break;
					bitarray.add(resize);

					vo.setImg_url(testfile.getAbsolutePath());
					vo.setImg_flag(false);
					filekey.add(vo);
					Thread.sleep(1000);
				}
			}
			else if(filetype == 2) {
				mVideoPath = mSelectImg.get(0);
				mVideoThumbnail = getImageFromURL(mSelectImg.get(0));
				File testfile = new File(myApp.getFileDir_Ex() + Common.getFileKey() + ".jpg");
				boolean flag = BrImageUtilManager.getInstance().saveOutput(this,mVideoThumbnail,getImageUri(testfile.getAbsolutePath()));
				videothumpath = testfile.getAbsolutePath();

				//				itemview1.setImageBitmap(mVideoThumbnail);
				//				itemdel1.setVisibility(View.VISIBLE);
				//				itemdel1.setTag(0);
				//				itemlay1.setVisibility(View.VISIBLE);

			}
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}


	}

	public void SaveImg(boolean album) {
		try {
			//			Looper.prepare();
			for (int i = 0; i < mSelectImg.size(); i++) {

				//				int size = getUriSize(Uri.parse(mSelectImg.get(i)));
				File f = new File(mSelectImg.get(i));
				double size = 0;
				if(f.exists()) {
					size = f.length();
				}
				//					double size = 0;
				//					//파일 있는지 체크후 파일 크기 구함
				//					if(videoFile.exists()) {
				//						size = videoFile.length();
				//					}
				//				
				//					Log.e("TAG", "video file size :: " +  size);
				//					
				//					//10mbx
				//					long mb10 = 10485760;
				Bitmap resize = null;
				if(size <= 51200) {
					resize = BitmapFactory.decodeFile(mSelectImg.get(i));
				}
				else {
					resize = BrImageUtilManager.getInstance().getBitmap(mSelectImg.get(i), album,800,600,true);
				}



				//TODO 리사이징
				File testfile = new File(myApp.getFileDir_Ex() + Common.getFileKey() + ".jpg");
				boolean flag = BrImageUtilManager.getInstance().saveOutput(this,resize,getImageUri(testfile.getAbsolutePath()));
				if (!flag)
					break;

				bitarray.add(resize);
				ImageVo vo = new ImageVo();
				vo.setImg_url(testfile.getAbsolutePath());
				//vo.setImg_url(mSelectImg.get(i).toString());
				vo.setImg_flag(false);
				filekey.add(vo);
				Thread.sleep(1000);
				testfile = null;
				resize = null;
			}

		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
	}

	public void AddTip(){
		AsyncTask<Void, Void, Void> getDataWorker = new AsyncTask<Void, Void, Void>() {
			String[] resultdata = null;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				try {
					linearLayout_loading.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					WriteFileLog.writeException(e);
				}
			}
			@Override
			protected Void doInBackground(Void... params) {
				try {
					InputStream input = null;
					HttpRequestor http =  null;
					if(modify){
						http =   BrHttpManager.getInstance().getHttpRequestor(Common.BaseUrl + Common.UPDATETIP);
						http.addParameter("postNo", String.valueOf(postno));
					}else{
						http =   BrHttpManager.getInstance().getHttpRequestor(Common.BaseUrl + Common.ADDTIP);	
					}

					http = addUpload(http);
					//	if(filetype == 3 || filetype == 0){
					//	input =http.sendPost();
					//	}else{
					input =http.sendMultipartPost();	
					//	}
					resultdata = BrHttpManager.getInstance().ResultToJson(input);

				} catch (Exception e) {
					WriteFileLog.writeException(e);

				}			
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				try {	

					linearLayout_loading.setVisibility(View.GONE);
					if(resultdata != null){
						if(resultdata[0].equals("true")){
							BrUtilManager.getInstance().showToast(Scenario5_S01111.this, resultdata[1]);
							finish();
						}else{
							BrUtilManager.getInstance().showToast(Scenario5_S01111.this, resultdata[1]);
						}
					}else{
						BrUtilManager.getInstance().showToast(Scenario5_S01111.this, "잠시후 다시 시도해주세요");
					}
				} catch (Exception e) {
					WriteFileLog.writeException(e);
				} finally {


				}				
				super.onPostExecute(result);		
			}		
		};
		getDataWorker.execute();
	}

	private HttpRequestor addUpload(HttpRequestor http){
		if(filetype == 1){
			http.addParameter("type", String.valueOf(filetype));
			http.addParameter("title", titlearea.getText().toString());
			http.addParameter("contents", contentarea.getText().toString());
			http.addParameter("id", myApp.getUserInfo().getId());
			for(int i = 0 ; i < filekey.size();i++){
				if(filekey.get(i).getImg_url().startsWith("http")) {
					http.addParameter("file"+i, filekey.get(i).getImg_url());
				}
				else {
					http.addFile("file"+i, new File(filekey.get(i).getImg_url()));
				}
			}
		}else if(filetype == 2){
			http.addParameter("type", String.valueOf(filetype));
			http.addParameter("title", titlearea.getText().toString());
			http.addParameter("contents", contentarea.getText().toString());
			http.addParameter("id", myApp.getUserInfo().getId());
			if(mVideoPath.startsWith("http")) {
				http.addParameter("localVideo", mVideoPath);
			}
			else {
				http.addFile("localVideo", new File(mVideoPath));
			}
			http.addFile("localThumbnail", new File(videothumpath));

		}else if(filetype == 3){
			http.addParameter("type", String.valueOf(filetype));
			http.addParameter("title", titlearea.getText().toString());
			http.addParameter("contents", contentarea.getText().toString());
			http.addParameter("id", myApp.getUserInfo().getId());
			http.addParameter("link", yutubulink);
			http.addParameter("linkThumbnail", yutubuthum);
		}else {
			http.addParameter("type", String.valueOf(filetype));
			http.addParameter("title", titlearea.getText().toString());
			http.addParameter("contents", contentarea.getText().toString());
			http.addParameter("id", myApp.getUserInfo().getId());
		}
		return http;
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


	// 파일명 찾기
	private String getName(Uri uri) {
		String[] projection = { MediaStore.Images.ImageColumns.DISPLAY_NAME };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	private int getUriSize(Uri uri) {
		String[] projection = { MediaStore.Images.ImageColumns.SIZE };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.SIZE);
		cursor.moveToFirst();
		return cursor.getInt(column_index);
	}
	// uri 아이디 찾기
	private String getUriId(Uri uri) {
		String[] projection = { MediaStore.Images.ImageColumns._ID };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	// 실제 경로 찾기
	private String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
}





