package com.foxrainbxm.jjlim.content;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.brainyxlib.BrUtilManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxrainbxm.Common;
import com.foxrainbxm.R;
import com.foxrainbxm.WriteFileLog;
import com.foxrainbxm.base.BaseActivity;
import com.foxrainbxm.base.ResizeImageView;
import com.foxrainbxm.base.ResizeVideoImageView;
import com.foxrainbxm.jjlim.base.ArticleVO;
import com.foxrainbxm.jjlim.base.ContentMessageParser;
import com.foxrainbxm.jjlim.base.ContentVO;
import com.foxrainbxm.jjlim.base.HttpContentApi;
import com.foxrainbxm.jjlim.base.HttpContentTask.OnContentTaskResultListener;
import com.foxrainbxm.jjlim.base.NanumFont;
import com.foxrainbxm.jjlim.base.ReplyVO;
import com.foxrainbxm.jjlim.libary.CustomEditText;
import com.foxrainbxm.jjlim.libary.ExpandableHeightListView;
import com.foxrainbxm.profile.Scen000013;
import com.foxrainbxm.skmin.base.Popup1Button;
import com.foxrainbxm.skmin.base.SharedValues;
import com.foxrainbxm.skmin.base.SharedValues.SharedKeys;
import com.foxrainbxm.tab2.Scenario2_1;
import com.foxrainbxm.tab3.Scenario3_1;
import com.foxrainbxm.tab4.Scenario4_1;
import com.foxrainbxm.tab7.PopupLinkButton;
import com.foxrainbxm.util.Request;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class ContentDetailView extends BaseActivity {

	Context mContext;
	TextView scrapView;
	int mPostNo = 0; // 상세페이지 포스트번호
	int mReplyIndex = 0; // 현재 댓글 페이지 (1페이지는 10개)
	String mMyId; // 내 아이디

	String mMyPhoto = ""; // 내 사진
	String mMyNickname = ""; // 내 별명
	String mMyName = "";
	String mArticleText = "";
	
	// 각종 이벤트 핸들링용 뷰
	Button mBackButton;
	Button mLinkButton;
	Button mMenuMoreButton;
	TextView mScrapButton;
	CheckBox mHeartSendButton;
	TextView mHeartButton;
	TextView mHeartPlusButton;
	Button mReplySendButton;
	TextView mReplyView;
	CustomEditText mReplyInput;
	TextView mMoreButton;

	ScrollView mMainScrollView;

	// 데이타
	ContentVO mContent; // 포스트 정보
	ArrayList<ArticleVO> mArticleList; // 포스트내 내용 정보
	ArrayList<ReplyVO> mReplyList; // 댓글 정보
	boolean mIsReplyEnd = true; // 댓글의 마지막 인지 정보 (더 있는지)

	CustomListAdapter mListAdapter;
	// CTextView view = null;
	TextView view = null;
	ArrayList<String> filename = new ArrayList<String>();
	Button playButton = null;
	TextView detail_id;
	int viewtype = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scen00114);

		mContext = this;

		Intent intent = getIntent();
		mPostNo = intent.getIntExtra("postNo", 0);
		viewtype = intent.getIntExtra("viewtype", 0);
		mContent = new ContentVO();
		mArticleList = new ArrayList<ArticleVO>();
		mReplyList = new ArrayList<ReplyVO>();

		mMyId = SharedValues.getSharedValue(mContext, SharedKeys.EMAIL);

		initView();
		initEvent();
//		initDataFromServer();
	}
	
	@Override
	protected void onDestroy() {
		
		try {
			int scrapCnt = Integer.parseInt(scrapView.getText().toString());
			int replyCnt = Integer.parseInt(mReplyView.getText().toString());
			
			int heartCnt = 0;
			if(mHeartButton.getVisibility() == View.VISIBLE) 
				heartCnt = Integer.parseInt(mHeartButton.getText().toString());
			else if(mHeartPlusButton.getVisibility() == View.VISIBLE)
				heartCnt = Integer.parseInt(mHeartPlusButton.getText().toString());
			
			if(myApp.mCurrentIndex == 1)
				Scenario2_1.Scenario2.updateRow(mPostNo, heartCnt, replyCnt, scrapCnt);
			else if(myApp.mCurrentIndex == 2)
				Scenario3_1.Scenario3.updateRow(mPostNo, heartCnt, replyCnt, scrapCnt);
			else if(myApp.mCurrentIndex == 3)
				Scenario4_1.Scenario4.updateRow(mPostNo, heartCnt, replyCnt, scrapCnt);
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
		
		super.onDestroy();
	}

	private void initView() {

		try {
			mMainScrollView = (ScrollView) findViewById(R.id.scrollView1);
			// 로딩전에 콘텐츠를 가려줌
			hideView();

			// 폰트 초기화
			if (!NanumFont.getInstance().isInited()) {
				NanumFont.getInstance().init(getAssets());
			}

			// 이미지 로더 초기화
			/*
			 * if (!ImageLoader.getInstance().isInited()) { Log.i("TEST",
			 * "ImageLoader Init"); DisplayImageOptions option = new
			 * DisplayImageOptions.Builder() .cacheOnDisk(true) .cacheInMemory(true)
			 * //.bitmapConfig(Bitmap.Config.RGB_565) //.considerExifParams(true)
			 * .displayer(new FadeInBitmapDisplayer(500)) .build();
			 * ImageLoaderConfiguration config = new
			 * ImageLoaderConfiguration.Builder(this)
			 * .defaultDisplayImageOptions(option)
			 * .threadPriority(Thread.NORM_PRIORITY - 2)
			 * .denyCacheImageMultipleSzizesInMemory()
			 * .diskCacheFileNameGenerator(new Md5FileNameGenerator())
			 * .diskCacheSize(50 * 1024 * 1024)
			 * .tasksProcessingOrder(QueueProcessingType.LIFO) .build();
			 * ImageLoader.getInstance().init(config); }
			 */

			detail_id = (TextView) findViewById(R.id.detail_id);
			if (viewtype == 1) {
				detail_id.setText("하이힐스토리");
			} else if (viewtype == 2) {
				detail_id.setText("고무장갑스토리");
			} else if (viewtype == 3) {
				detail_id.setText("아이를부탁해");
			}

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
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
		

		/*
		 * ImageCache.getInstance().loadAsync(Common.ImageUrl +
		 * myApp.getUserInfo().getProfilephoto(), new ImageCallback() {
		 * 
		 * @Override public void onImageLoaded(Drawable image, String url,
		 * String path) { //ImageCache.setImageFitImageView(profile_image,
		 * image); } }, this);
		 */
	}

	private void showView() {
		mMainScrollView.setVisibility(View.VISIBLE);
	}

	private void hideView() {
		mMainScrollView.setVisibility(View.INVISIBLE);
	}

	private void initEvent() {

		// 뒤로가기 이벤트
		mBackButton = (Button) findViewById(R.id.title_back);
		mBackButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// 공유 이벤트
		mLinkButton = (Button) findViewById(R.id.title_scrap);
		mLinkButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*
				 * TODO : 공유로 보낼 콘텐츠를 여기에 넣어 주어야함 !!!
				 */
				try {
					
					String userid = myApp.getUserInfo().getName();
					if(userid == null || userid.length() == 0){
						userid = myApp.getUserInfo().getId();
					}
					
					PopupLinkButton dialogbtn = null;
					if (mContent.getBbstype() == 1) {
						dialogbtn = new PopupLinkButton(ContentDetailView.this,mContent.getTitle(), mArticleText,userid);
					} else if (mContent.getBbstype() == 2) {
						dialogbtn = new PopupLinkButton(ContentDetailView.this,mContent.getTitle(), mArticleText,filename,userid);
					} else if (mContent.getBbstype() == 3) {
						dialogbtn = new PopupLinkButton(ContentDetailView.this,mContent.getTitle(), mArticleText,filename.get(0), (String) playButton.getTag(),userid);
					}
					dialogbtn.show();
				} catch (Exception e) {
					WriteFileLog.writeException(e);
				}
				// new PopupLinkButton(mContext, "제목", "내용", null).show();
			}
		});

		// 스크랩 이벤트
		mScrapButton = (TextView) findViewById(R.id.scrap_button);
		mScrapButton.setVisibility(View.INVISIBLE);
		mScrapButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mContent.isScrapUsed()) {
					Toast.makeText(mContext, "이미 스크랩 되어있습니다", Toast.LENGTH_SHORT)
							.show();
					mScrapButton.setVisibility(View.INVISIBLE);
				} else {
					HttpContentApi httpApi = new HttpContentApi(mContext);
					httpApi.contentScrap(mPostNo, mMyId,
							new OnContentTaskResultListener() {
								@Override
								public void onTaskResult(boolean success,
										String result) {
									if (success) {
										Log.i("TEST", "scrap insert success");

										Toast.makeText(mContext, "스크랩 되었습니다",
												Toast.LENGTH_SHORT).show();
										mContent.setScrapUsed(true);
										mScrapButton
												.setVisibility(View.INVISIBLE);
										int scrapCnt = Integer.parseInt(scrapView.getText().toString());
										scrapCnt = scrapCnt +1;
										scrapView.setText(String.valueOf(scrapCnt));
									}
								}
							});
				}
			}
		});

		// More 메뉴 이벤트
		mMenuMoreButton = (Button) findViewById(R.id.title_more);
		mMenuMoreButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mScrapButton.getVisibility() == View.VISIBLE)
					mScrapButton.setVisibility(View.INVISIBLE);
				else
					mScrapButton.setVisibility(View.VISIBLE);
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

		

		// 하트 이벤트
		mHeartButton = (TextView) findViewById(R.id.sml_heart);
		mHeartPlusButton = (TextView) findViewById(R.id.sml_heart_plus);
		mHeartSendButton = (CheckBox) findViewById(R.id.heart_sender);

		// 댓글 이벤트
		mReplyInput = (CustomEditText) findViewById(R.id.reply_input);
		mReplySendButton = (Button) findViewById(R.id.reply_sender);
		mReplySendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				final String replyText = mReplyInput.getText().toString();
				if (replyText.length() != 0) {

					Toast.makeText(mContext, "댓글 등록", Toast.LENGTH_SHORT)
							.show();

					HttpContentApi httpApi = new HttpContentApi(mContext);
					httpApi.contentReply(mPostNo, mMyId, replyText,
							new OnContentTaskResultListener() {
								@Override
								public void onTaskResult(boolean success,
										String result) {
									if (success) {
										
										ReplyVO reply = new ReplyVO();
										reply.setPhoto(mMyPhoto);
										reply.setNick(mMyNickname);
										reply.setText(replyText);
										reply.setTime(new Date());
										reply.setName(SharedValues.getSharedValue(mContext, SharedKeys.NAME));
										mReplyList.add(0, reply);

										mListAdapter.notifyDataSetChanged();

										mReplyInput.setText("");

										int reply_cnt = Integer
												.valueOf(mReplyView.getText()
														.toString());
										reply_cnt++;
										mReplyView.setText(String
												.valueOf(reply_cnt));
									}
								}
							});

				} else {
					Log.i("TEST", "reply empty");
				}

			}
		});

		// 댓글 더보기 이벤트
		mMoreButton = (TextView) findViewById(R.id.more_button);
		mMoreButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				HttpContentApi httpApi = new HttpContentApi(mContext);
				httpApi.contentDetail(mPostNo, mMyId, mReplyIndex + 1,
						new OnContentTaskResultListener() {

							@Override
							public void onTaskResult(boolean success,
									String result) {

								if (success) {
									// Log.i("TEST", "success: " + success +
									// "result: " + result);

									mReplyIndex++;
									setMoreDataFromJson(result);

								} else {
									new Popup1Button(mContext,
											"데이타를 가져오지 못했습니다.", null).show();
								}

							}
						});
			}
		});

	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		
		Log.e("ref", ">>>>>>>>>>");
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		
		mArticleList.clear();
		mReplyList.clear();
		filename.clear();
		Log.e("ref2", ">>>>>>>>>>>");
		initDataFromServer();
		

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
	}

	private void initDataFromServer() {

		HttpContentApi httpApi = new HttpContentApi(this);
		httpApi.contentDetail(mPostNo, mMyId, mReplyIndex,
				new OnContentTaskResultListener() {

					@Override
					public void onTaskResult(boolean success, String result) {

						if (success) {

							// Log.i("TEST", "success: " + success + "result: "
							// + result);

							setDataFromJson(result);
							setView();
						} else {
							new Popup1Button(mContext, "삭제된 게시글입니다.", new android.content.DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									finish();
									
								}
							})
									.show();
						}

					}
				});
	}
	

	// 상세페이지 초기정보 셋팅
	private void setDataFromJson(String jsonStr) {

		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");

		ObjectMapper m = new ObjectMapper();
		try {

			JsonNode rootNode = m.readTree(jsonStr);

			mContent.setPostNo(rootNode.path("postno").asInt());
			mContent.setTitle(rootNode.path("title").asText());
			mContent.setHeartCount(rootNode.path("c_heart").asInt());
			mContent.setReplyCount(rootNode.path("c_reply").asInt());
			mContent.setScrapCount(rootNode.path("c_scrap").asInt());
			mContent.setHeartUsed(rootNode.path("u_heart").asBoolean());
			mContent.setScrapUsed(rootNode.path("u_scrap").asBoolean());
			mContent.setCurTime(dataFormat.parse(rootNode.path("cur_time")
					.asText()));
			mContent.setType(rootNode.path("type").asInt());
			mIsReplyEnd = rootNode.path("replys_end").asBoolean();

			ArrayList<HashMap<String, String>> articles = ContentMessageParser
					.getObjectsFromMessage(rootNode.path("contents").asText());
			for (HashMap<String, String> map : articles) {

				ArticleVO article = new ArticleVO();
				article.setType(Integer.parseInt(map.get("type")));
				if (article.getType() == 1) {
					article.setText(map.get("value1"));
					if (mContent.getBbstype() == 0
							|| mContent.getBbstype() == 1) {
						mContent.setBbstype(1);
					}

				} else if (article.getType() == 2) {
					article.setImage(map.get("value1"));
					if (mContent.getBbstype() == 0
							|| mContent.getBbstype() == 1
							|| mContent.getBbstype() == 2) {
						mContent.setBbstype(2);
					}
				} else if (article.getType() == 3) {
					article.setVideo(map.get("value1"));
					article.setThumb(map.get("value2"));

					Log.d("TAG", map.get("value1").toString());
					Log.d("TAG", map.get("value2").toString());
					if (mContent.getBbstype() == 0
							|| mContent.getBbstype() == 1
							|| mContent.getBbstype() == 3) {
						mContent.setBbstype(3);
					}

				}
				mArticleList.add(article);
			}

			for (JsonNode node : rootNode.path("replys")) {

				ReplyVO reply = new ReplyVO();
				reply.setNo(node.path("replyno").asInt());
				reply.setText(node.path("replytext").asText());
				reply.setNick(node.path("nickname").asText());
				reply.setName(node.path("name").asText());
				reply.setPhoto(node.path("profilephoto").asText());
				reply.setTime(dataFormat.parse(node.path("replytime").asText()));
				mReplyList.add(reply);
			}

			JsonNode profileNode = rootNode.path("profile");
			mMyPhoto = profileNode.path("profilephoto").asText();
			mMyNickname = profileNode.path("nickname").asText();
			mMyName = profileNode.path("name").asText();

			Log.i("TEST", "reply size: " + mReplyList.size());
		} catch (Exception e) {
			WriteFileLog.writeException(e);
			Log.e("TEST", e.getMessage());
		}

	}

	// 댓글 더보기 시 정보 셋팅
	private void setMoreDataFromJson(String jsonStr) {

		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");

		ObjectMapper m = new ObjectMapper();
		try {

			JsonNode rootNode = m.readTree(jsonStr);

			mIsReplyEnd = rootNode.path("replys_end").asBoolean();

			for (JsonNode node : rootNode.path("replys")) {

				ReplyVO reply = new ReplyVO();
				reply.setNo(node.path("replyno").asInt());
				reply.setText(node.path("replytext").asText());
				reply.setNick(node.path("nickname").asText());
				reply.setPhoto(node.path("profilephoto").asText());
				reply.setName(node.path("name").asText());
				reply.setTime(dataFormat.parse(node.path("replytime").asText()));
				mReplyList.add(reply);
			}
		} catch (Exception e) {
			WriteFileLog.writeException(e);
			Log.e("TEST", e.getMessage());
		}

		if (mIsReplyEnd) {
			mMoreButton.setVisibility(View.GONE);
		}

		mListAdapter.notifyDataSetChanged();
	}

	public void DeleteReply(final String replyNumber) {
		
		AsyncTask<Void, Void, Void> getDataWorker = new AsyncTask<Void, Void, Void>() {
			JSONObject rgb = null;
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				try {
					BrUtilManager.getInstance().InputKeybordHidden(ContentDetailView.this, mReplyInput);
				} catch (Exception e) {
					WriteFileLog.writeException(e);
				}
			}
			@Override
			protected Void doInBackground(Void... params) {
				try {
					List<NameValuePair> dataList = new ArrayList<NameValuePair>();
					dataList.add(new BasicNameValuePair("replyNo",String.valueOf(replyNumber)));

					rgb = Request.requestService(ContentDetailView.this,Common.DELETEREPLY, dataList);
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
//							if(rgb.getString("message")!= null && rgb.getString("message").equals("1")){
								BrUtilManager.getInstance().showToast(ContentDetailView.this, "댓글이 삭제되었습니다.");
//								TipVo.setScrap(TipVo.getScrap() + 1);
//							}else if(rgb.getString("message") == null){
//								BrUtilManager.getInstance().ShowDialog(Scenario5_S00117.this,getString(R.string.app_name), "잠시후 다시 시도하세요");	
//							}else{
//								BrUtilManager.getInstance().showToast(Scenario5_S00117.this, "이미 스크랩목록에 저장되어있습니다");
//							}
						}else {
							BrUtilManager.getInstance().ShowDialog(ContentDetailView.this,getString(R.string.app_name), "잠시후 다시 시도하세요");
						}
					}
				} catch (Exception e) {
					WriteFileLog.writeException(e);
				} finally {
//					linearLayout_loading.setVisibility(View.GONE);
//					SetView();
//					arraylist.remove(index)
					int replyNum = 0;
					for(int i = 0 ; i < mReplyList.size() ; i ++) {
						if(String.valueOf(mReplyList.get(i).getNo()).equals(replyNumber)) {
							replyNum = i;
							break;
						}
					}
					
					mReplyList.remove(replyNum);
					mListAdapter.notifyDataSetChanged();
					
					int replyCnt = Integer.parseInt(mReplyView.getText().toString());
					replyCnt = replyCnt - 1;
					mReplyView.setText(String.valueOf(replyCnt));
					
				}				
				super.onPostExecute(result);		
			}		
		};
		getDataWorker.execute();
	}
	
	private void setView() {

		// 1. Add Articles
		String title = mContent.getTitle();
		
		final ViewGroup viewGroup = (ViewGroup) findViewById(R.id.detail_container);
		viewGroup.removeAllViews();
		view = (TextView) LayoutInflater.from(this).inflate(R.layout.s00114_texttitle, null);
		// tv = (TextView)view.findViewById(R.id.content_text);
		view.setText("\n"+title);
//		view.setLinkTextColor(Color.RED);
//		Linkify.addLinks(view, Linkify.ALL);
		viewGroup.addView(view);
		
		mArticleText = "";
		

		for (final ArticleVO article : mArticleList) {
			// 텍스트
			if (article.getType() == 1) {
				
				view = (TextView) LayoutInflater.from(this).inflate(R.layout.s00114_textl, null);
				// tv = (TextView)view.findViewById(R.id.content_text);
//				Log.d("TAG_2", article.getText());
				
				mArticleText =mArticleText+ article.getText();
				
				view.setText(article.getText());
//				view.setLinkTextColor(Color.RED);
//				Linkify.addLinks(view, Linkify.ALL);
				viewGroup.addView(view);

			}
			// 이미지
			else if (article.getType() == 2) {

				final RelativeLayout view = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.s00114_image, null);
				ResizeImageView imgview = (ResizeImageView)view.findViewById(R.id.content_image);
				//final ResizeImageView view = (ResizeImageView) LayoutInflater.from(this).inflate(R.layout.s00114_image, null);
				final ProgressBar progressbar = (ProgressBar)view.findViewById(R.id.progressbar);
				String urlStr = Common.ImageUrl + article.getImage();
				
				filename.add(urlStr);
				ImageLoader.getInstance().displayImage(urlStr, imgview, new ImageLoadingListener() {
							@Override
							public void onLoadingStarted(String arg0, View arg1) {
								// TODO Auto-generated method stub
								progressbar.setVisibility(View.VISIBLE);
							}

							@Override
							public void onLoadingFailed(String arg0, View arg1,
									FailReason arg2) {
								// TODO Auto-generated method stub
								progressbar.setVisibility(View.GONE);
							}

							@Override
							public void onLoadingComplete(String arg0,
									View arg1, Bitmap arg2) {
								// TODO Auto-generated method stub
								progressbar.setVisibility(View.GONE);
							}

							@Override
							public void onLoadingCancelled(String arg0,
									View arg1) {
								// TODO Auto-generated method stub
								progressbar.setVisibility(View.GONE);
							}
						});
				/*
				 * ImageLoader.getInstance().displayImage(urlStr, iv, new
				 * SimpleImageLoadingListener() {
				 * 
				 * @Override public void onLoadingFailed(String imageUri, View
				 * view, FailReason failReason) { view.setVisibility(View.GONE);
				 * } });
				 */
				viewGroup.addView(view);

			}
			// 링크
			else if (article.getType() == 3) {

				final View view = LayoutInflater.from(this).inflate(R.layout.s00114_video, null);

				ResizeVideoImageView iv = (ResizeVideoImageView) view.findViewById(R.id.content_video);
				filename.add(article.getThumb());

				Log.e("TAG", article.getThumb());

				myApp.imageloder.displayImage(article.getThumb(), iv,
						myApp.options, new ImageLoadingListener() {
							
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
								
							}
							
							@Override
							public void onLoadingCancelled(String arg0, View arg1) {
								// TODO Auto-generated method stub
								
							}
						});

				// 유투브 링크 클릭시 이벤트
				playButton = (Button) view.findViewById(R.id.video_play);
				playButton.setTag(article.getVideo());
				playButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.i("TEST", "play: " + article.getVideo());

						// 웹 링크로 변경!
						Intent i = new Intent(Intent.ACTION_VIEW, Uri
								.parse(article.getVideo()));
						startActivity(i);

						// 웹뷰에서 보기
						// WebView webView =
						// (WebView)view.findViewById(R.id.webView1);
						// webView.getSettings().setJavaScriptEnabled(true);
						// webView.getSettings().setPluginState(PluginState.ON);
						// webView.setWebChromeClient(new WebChromeClient());
						// webView.setWebViewClient(new CustomViewClient());
						//
						// String[] strArr = article.getVideo().split("[/=]");
						// String ytTitle = strArr[strArr.length-1];
						//
						// String htmlPfx =
						// "<iframe width=\"100%\" height=\"95%\" frameborder=\"0\" src=\"http://www.youtube.com/embed/";
						// String htmlSfx = "\"></iframe>";
						// String html = htmlPfx + ytTitle + htmlSfx;
						//
						// webView.loadData(html , "text/html", "utf-8");
						//
						// AspectRatioImageView view1 =
						// (AspectRatioImageView)view.findViewById(R.id.content_video);
						// Button view2 =
						// (Button)view.findViewById(R.id.video_play);
						// view1.setVisibility(View.INVISIBLE);
						// view2.setVisibility(View.INVISIBLE);
						// webView.setVisibility(View.VISIBLE);
					}
				});

				viewGroup.addView(view);

			}
		}

		// 2. Count Setting

		mReplyView = (TextView) findViewById(R.id.sml_reply);
		scrapView = (TextView) findViewById(R.id.sml_scrap);

		if (mContent.isHeartUsed()) {
			mHeartSendButton.setChecked(true);
			mHeartButton.setVisibility(View.GONE);
			mHeartPlusButton.setVisibility(View.VISIBLE);
			mHeartButton.setText(String.valueOf(mContent.getHeartCount() - 1));
			mHeartPlusButton.setText(String.valueOf(mContent.getHeartCount()));
		} else {
			mHeartButton.setText(String.valueOf(mContent.getHeartCount()));
			mHeartPlusButton
					.setText(String.valueOf(mContent.getHeartCount() + 1));
		}

		mHeartSendButton
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Log.i("TEST", "heart sender is: " + isChecked);

						HttpContentApi httpApi = new HttpContentApi(mContext);
						if (isChecked) {
							httpApi.contentHeart(1, mPostNo, mMyId,
									new OnContentTaskResultListener() {
										@Override
										public void onTaskResult(
												boolean success, String result) {
											if (success) {
												Log.i("TEST",
														"heart insert success");

												mHeartButton
														.setVisibility(View.GONE);
												mHeartPlusButton
														.setVisibility(View.VISIBLE);
											}
										}
									});
						} else {
							httpApi.contentHeart(2, mPostNo, mMyId,
									new OnContentTaskResultListener() {
										@Override
										public void onTaskResult(
												boolean success, String result) {
											if (success) {
												Log.i("TEST",
														"heart delete success");

												mHeartButton
														.setVisibility(View.VISIBLE);
												mHeartPlusButton
														.setVisibility(View.GONE);
											}
										}
									});
						}
					}
				});

		mReplyView.setText(String.valueOf(mContent.getReplyCount()));
		scrapView.setText(String.valueOf(mContent.getScrapCount()));

		// 3. Reply List Setting

		if (!mIsReplyEnd) {
			mMoreButton.setVisibility(View.VISIBLE);
		}

		ExpandableHeightListView listView = (ExpandableHeightListView) findViewById(R.id.reply_container);
		listView.setFocusable(false);
		listView.setExpanded(true);

		mListAdapter = new CustomListAdapter(this, mReplyList);
		listView.setAdapter(mListAdapter);
		listView.setSelection(0);

		// 포커스를 위로 올림
		CustomEditText focusView = (CustomEditText) findViewById(R.id.editText1);
		focusView.requestFocus();

		// 로딩후에 콘텐츠를 보여줌
		showView();

	}

	// 유투브 링크를 위한 커스텀 웹뷰 클라이언트
	class CustomViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}

	}

	// 댓글 리스트 아답터
	private class CustomListAdapter extends BaseAdapter {

		private Context mContext;
		private ArrayList<ReplyVO> items;

		public CustomListAdapter(Context context, ArrayList<ReplyVO> items) {
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder vh;
			
			if (convertView == null) {
				convertView = View.inflate(mContext, R.layout.scen00114_rep,
						null);

				vh = new ViewHolder();
				vh.photo = (ImageView) convertView.findViewById(R.id.sml_image);
				vh.nickname = (TextView) convertView.findViewById(R.id.sml_title);
				vh.content = (TextView) convertView.findViewById(R.id.sml_content);
				vh.time = (TextView) convertView.findViewById(R.id.delay_time);
				vh.delBtn = (ImageButton) convertView.findViewById(R.id.btn_list_delete);
				
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}

			if (items.get(position).getPhoto().length() != 0) {
				String urlStr = Common.ImageUrl+ items.get(position).getPhoto();
				ImageLoader.getInstance().displayImage(urlStr, vh.photo);
			} else {
				ImageLoader.getInstance().displayImage("", vh.photo, new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						Log.e("", "");
					}
					
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						vh.photo.setImageResource(R.drawable.img_reply_prf_n);
					}
					
					@Override
					public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
						Log.e("", "");
						if(arg2 == null){
							vh.photo.setImageResource(R.drawable.img_reply_prf_n);
						}
					}
					
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						vh.photo.setImageResource(R.drawable.img_reply_prf_n);
					}
				});
				//ImageLoader.getInstance().displayImage("", vh.photo);
				//
			}

			if(items.get(position).getNick().equals("")) {
				vh.nickname.setText(items.get(position).getName());
			}
			else {
				vh.nickname.setText(items.get(position).getNick());
			}
			
			
			vh.content.setText(items.get(position).getText());
			
			if(items.get(position).getName().equals(SharedValues.getSharedValue(mContext, SharedKeys.NAME))) {
				vh.delBtn.setVisibility(View.VISIBLE);
				vh.delBtn.setTag(items.get(position).getNo());
				vh.delBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						final int seq = (Integer)v.getTag();
						//int seq =  Integer.valueOf((String)v.getTag()) ;
//						BrUtilManager.getInstance().ShowDialog1btn(mContext, "안내", "\, callback)" +
//								"
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
			}
			else {
				vh.delBtn.setVisibility(View.GONE);
			}

			String timeStr = "";
			long diffTime = (mContent.getCurTime().getTime() - items.get(position).getTime().getTime()) / 1000;

			if (diffTime < 60) {
				timeStr = "방금전";
			} else if (diffTime < 60 * 60) {
				timeStr = String.valueOf(diffTime / 60) + "분전";
			} else if (diffTime < 60 * 60 * 24) {
				timeStr = String.valueOf(diffTime / 60 / 60) + "시간전";
			} else if (diffTime < 60 * 60 * 24 * 30) {
				timeStr = String.valueOf(diffTime / 60 / 60 / 24) + "일전";
			} else if (diffTime < 60 * 60 * 24 * 30 * 12) {
				timeStr = String.valueOf(diffTime / 60 / 60 / 24 / 30) + "달전";
			} else {
				timeStr = "오래전";
			}
			vh.time.setText(timeStr);

			return convertView;
		}

		class ViewHolder {
			public ImageView photo;
			public TextView nickname;
			public TextView content;
			public TextView time;
			public ImageButton delBtn;
		}

	}

}