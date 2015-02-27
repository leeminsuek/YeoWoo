package com.foxrainbxm.tab7;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.brainyxlib.BrUtilManager;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.foxrainbxm.Common;
import com.foxrainbxm.R;
import com.foxrainbxm.WriteFileLog;
import com.foxrainbxm.kakao.KakaoStoryLoginActivity;
import com.foxrainbxm.kakao.SharedActivity;
import com.foxrainbxm.kakao.SharedKakaoTalk;
import com.foxrainbxm.kakao.StoryLink;
import com.foxrainbxm.skmin.base.Popup1Button;
import com.kakao.APIErrorResult;
import com.kakao.BasicKakaoStoryPostParamBuilder;
import com.kakao.BasicKakaoStoryPostParamBuilder.PERMISSION;
import com.kakao.KakaoLink;
import com.kakao.KakaoParameterException;
import com.kakao.KakaoStoryHttpResponseHandler;
import com.kakao.KakaoStoryLinkInfo;
import com.kakao.KakaoStoryService;
import com.kakao.KakaoStoryService.StoryType;
import com.kakao.KakaoTalkLinkMessageBuilder;
import com.kakao.LinkKakaoStoryPostParamBuilder;
import com.kakao.MyStoryInfo;
import com.kakao.NoteKakaoStoryPostParamBuilder;
import com.kakao.PhotoKakaoStoryPostParamBuilder;
import com.kakao.helper.Logger;

public class PopupLinkButton extends Dialog {

	public static PopupLinkButton instance = null;
	public static PopupLinkButton getinstance(){
		return instance;
	}
	Activity mContext;
	String mTitle="";
	String mText="";
	String thumnailurl="";
	String videorink="";
	String userid="";
	ArrayList<String>  mImgurl  = new ArrayList<String>();
	int kakaoAuth = 0 ;
	int type= 0;
	private KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder;
	private KakaoLink kakaoLink;
	/////
	private final String execParam = "place=1111";
	private final String marketParam = "referrer=kakaostory";
	public ArrayList<String>uploadimg = new ArrayList<String>();
	
	ProgressDialog dialog =null;
			
	public PopupLinkButton(Activity context, String title, String text,String _userid){
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.popup_link_button);
		 type = 1;
		 mContext = context;
		 mTitle = title;
		 mText = text;
		 userid = _userid;
		 instance = this;
		init();
	}
	public PopupLinkButton(Activity context,String title,String content,ArrayList<String>imgfile,String _userid){
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.popup_link_button);
		 mContext = context;
		 mTitle = title;
		 mText = content;
		 mImgurl = imgfile;
		 type = 2;
		 userid = _userid;
		 instance = this;
		 init();
	}
	
	public PopupLinkButton(Activity context,String title,String content,String thumnail, String videolink,String _userid){
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.popup_link_button);
		 mContext = context;
		 mTitle = title;
		 mText = content;
		 thumnailurl =thumnail ;
		videorink =videolink ;
		userid = _userid;
		instance = this;
		 type = 3;
		init();
	}
	
	private void init(){
		try {
		     kakaoLink = KakaoLink.getKakaoLink(mContext);
	         kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
	         findViewById(R.id.kakaostory_link).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext,KakaoStoryLoginActivity.class);
					mContext.startActivity(intent);
				}
			});
			
			findViewById(R.id.kakaotalk_link).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					switch(type){
					case 1:
						SharedKakaoTalk.SendKaKaoTalkLink(mContext,kakaoLink,kakaoTalkLinkMessageBuilder,mTitle, mText,userid);
						break;
					case 2:
						SharedKakaoTalk.SendKaKaoTalkLink(mContext,kakaoLink,kakaoTalkLinkMessageBuilder,mTitle, mText, mImgurl,userid);
						break;
					case 3:
						SharedKakaoTalk.SendKaKaoTalkLink(mContext,kakaoLink,kakaoTalkLinkMessageBuilder,mTitle, mText, thumnailurl, videorink,userid);
						break;
					}
				}
			});
			
			findViewById(R.id.facebook_link).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
//						BrUtilManager.getInstance().showToast(mContext, "페이스북 승인 대기중입니다.");
//						return;
						
						((Activity)mContext).startActivity(new Intent(mContext, SharedActivity.class)
						.putExtra("content", mText)
						.putExtra("type", type)
						.putExtra("title", mTitle)
						.putExtra("thumnail", thumnailurl)
						.putExtra("videolink", videorink)
						.putStringArrayListExtra("img", mImgurl)
						.putExtra("userid", userid));
						
//						SendFaceBook();
					} catch (Exception e) {
						WriteFileLog.writeException(e);
					}
				}
			});
			findViewById(R.id.btnPopup0).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
				}
			});
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
	}
	
	public void SendFaceBook(){
		try {
			switch(type){
			case 1://페이스북 텍스트만
//				publishStory(null);
				//publishStory();
				break;
			case 2://페이스북 이미지
				dialog = BrUtilManager.getInstance().ShowProgressDialog(mContext, "이미지를 업로드 중입니다.");
//				BrUtilManager.getInstance().showToast(mContext	, mImgurl.get(0));
				Facebookload(mImgurl.get(0));	
				break;
				
			case 3://페이스북 비디오 썸
				dialog = BrUtilManager.getInstance().ShowProgressDialog(mContext, "이미지를 업로드 중입니다.");
				Facebookload(thumnailurl);	
				break;
			}			
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
	}
	
	/**
	 * 이미지를 다운후 전송
	 * @param url
	 */
	public void UploadFacebook(String url){
		try {
			if(url != null){
				uploadimg.add(url);
				if(type == 3){
//					publishStory(uploadimg);
					return;
				}
				
				if(uploadimg.size() == mImgurl.size()){
//					publishStory(uploadimg);
				}else{
					Facebookload(mImgurl.get(uploadimg.size()));
				}	
			}else{
				if(dialog!=null && dialog.isShowing()){
					dialog.dismiss();
				}
				BrUtilManager.getInstance().showToast(mContext	, "업로드 파일이 없습니다.");
			}
				
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
	}
	
	
	/**
	 * 카카오스토리 공유시작
	 */
	public void SendKakaoStory(){
		try {
			new Popup1Button(mContext, mContext.getResources().getStringArray(R.array.kakaostory_spinner), new Dialog.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					//0전체공개 , 1친구공개
					kakaoAuth = arg1;
					
					switch(type){
					case 1: //텍스트
						 requestPostNote();
//						try {
//							sendPostingLink();
//						} catch (NameNotFoundException e) {
//							// TODO Auto-generated catch block
//							WriteFileLog.writeException(e);
//						}
						break;
					case 2:// 이미지
						dialog = BrUtilManager.getInstance().ShowProgressDialog(mContext, "이미지를 업로드 중입니다.");
						load(mImgurl.get(0));			
						break;
					case 3: //비디오
						dialog = BrUtilManager.getInstance().ShowProgressDialog(mContext, "이미지를 업로드 중입니다.");
						load(thumnailurl);			
						break;
					}
				}
			}).show();
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
	}
	
//	public void sendPostingLink() throws NameNotFoundException {
//		Map<String, Object> urlInfoAndroid = new Hashtable<String, Object>(1);
//		urlInfoAndroid.put("title", mTitle);
//		urlInfoAndroid.put("desc", mText);
//		urlInfoAndroid.put("imageurl", new String[] {"http://183.111.148.117:8080/admin/board/homeDetail?postNo=809"});
//		urlInfoAndroid.put("type", "article");
//		
//		// Recommended: Use application context for parameter.
//		StoryLink storyLink = StoryLink.getLink(mContext);
//
//		// check, intent is available.
//		if (!storyLink.isAvailableIntent()) {
////			alert("Not installed KakaoStory.");			
//			return;
//		}
//
//		/**
//		 * @param activity
//		 * @param post (message or url)
//		 * @param appId
//		 * @param appVer
//		 * @param appName
//		 * @param encoding
//		 * @param urlInfoArray
//		 */
//		storyLink.openKakaoLink(mContext, 
//				"http://m.media.daum.net/entertain/enews/view?newsid=20120927110708426",
//				"com.foxrainbxm", 
//				mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName, 
//				"여우비",
//				"UTF-8", 
//				urlInfoAndroid);
//	}
	
	/**
	 * 이미지를 다운후 전송
	 * @param url
	 */
	public void UploadImageKakao(String url){
		try {
			uploadimg.add(url);
			if(type == 3){
				uploadImage(uploadimg);
				return;
			}
			
			if(uploadimg.size() == mImgurl.size()){
				uploadImage(uploadimg);
			}else{
				load(mImgurl.get(uploadimg.size()));
			}	
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
	}
	
	   private void getLinkInfo() {
	        KakaoStoryService.requestGetLinkInfo(new MyKakaoStoryHttpResponseHandler<KakaoStoryLinkInfo>() {
	            @Override
	            protected void onHttpSuccess(final KakaoStoryLinkInfo kakaoStoryLinkInfo) {
	                if (kakaoStoryLinkInfo != null && kakaoStoryLinkInfo.isValidResult()) {
	                    Toast.makeText(mContext, "succeeded to get link info.\n" + kakaoStoryLinkInfo, Toast.LENGTH_SHORT).show();
	                    requestPostLink(kakaoStoryLinkInfo);
	                } else {
	                    Toast.makeText(mContext, "failed to get link info.\nkakaoStoryLinkInfo=null", Toast.LENGTH_SHORT).show();
	                }
	            }
	        }, Common.ImageUrl + videorink);
	    }
	  
	   private void requestPostLink(final KakaoStoryLinkInfo kakaoStoryLinkInfo) {
		   	try {
		   		final LinkKakaoStoryPostParamBuilder postParamBuilder = new LinkKakaoStoryPostParamBuilder(kakaoStoryLinkInfo);
		        postParamBuilder.setContent("[여우비]\n"+mTitle+"\n"+mText);
		        if(kakaoAuth == 0) {
			        postParamBuilder.setPermission(PERMISSION.PUBLIC);
		        }
		        else if(kakaoAuth == 1) {
		        	 postParamBuilder.setPermission(PERMISSION.FRIENDS);
		        }
		        
		        requestPost(StoryType.LINK, postParamBuilder);	
			} catch (Exception e) {
				WriteFileLog.writeException(e);
			}
	    }
	   
	   private void requestPostPhoto(final String[] imageURLs) {
		   try {
			   final PhotoKakaoStoryPostParamBuilder postParamBuilder = new PhotoKakaoStoryPostParamBuilder(imageURLs);
		        postParamBuilder.setContent("[여우비]\n"+mTitle+"\n"+mText+"\n워킹맘들의 커뮤니티\n");//+"market://details?id=com.foxrainbxm");
		        if(kakaoAuth == 0) {
			        postParamBuilder.setPermission(PERMISSION.PUBLIC);
		        }
		        else if(kakaoAuth == 1) {
		        	 postParamBuilder.setPermission(PERMISSION.FRIENDS);
		        }
		        
		        requestPost(StoryType.PHOTO, postParamBuilder);
			} catch (Exception e) {
				WriteFileLog.writeException(e);
			}
	    }
	   
	   public void load(String url)
	   {
		   try {
			   String _url = "";
			   if(url.startsWith("http")){
				   _url = url;
			   }else{
				   _url = Common.ImageUrl + url;
			   }
				new com.foxrainbxm.util.ImageDownloader(mContext).downloadImage(_url);
			} catch (Exception e) {
				WriteFileLog.writeException(e);
			}
		   
		}
	   
	   
	   public void Facebookload(String url)
	   {
		   try {
			   String _url = "";
			   if(url.startsWith("http")){
				   _url = url;
			   }else{
				   _url = Common.ImageUrl + url;
			   }
				new com.foxrainbxm.util.ImageDownloader(mContext).FAcebookdownloadImage(_url);
			} catch (Exception e) {
				WriteFileLog.writeException(e);
			}
		 
	   }
		
		private void uploadImage(ArrayList<String> url) {
	        try {
	        	if(dialog != null){
	        		dialog.dismiss();
	        	}
	            final List<File> files = new ArrayList<File>();
	            if(url.size() > 5) {
	            	   for(int i = 0;i < 5;i++){
	  	            	 final File file2 = new File(url.get(i));
	  	 	            files.add(file2);
	  	            }
	            }
	            else {
	            	   for(int i = 0;i < url.size();i++){
	  	            	 final File file2 = new File(url.get(i));
	  	 	            files.add(file2);
	  	            }
	            }
	         
	            KakaoStoryService.requestMultiUpload(new MyKakaoStoryHttpResponseHandler<String[]>() {
	                @Override
	                protected void onHttpSuccess(final String[] imageURLs) {
	                    try {
	                        if (imageURLs != null && imageURLs.length != 0) {
	                            requestPostPhoto(imageURLs);
	                        } else {
	                        	BrUtilManager.getInstance().showToast(mContext, "이미지 전송이 실패되었습니다. 잠시후 다시시도해주세요");
	                        }
	                    } finally {
	                        deleteTempFiles();
	                    }
	                }

	                @Override
	                protected void onHttpSessionClosedFailure(final APIErrorResult errorResult) {
	                    try {
	                        super.onHttpSessionClosedFailure(errorResult);
	                    } finally {
	                        deleteTempFiles();
	                    }
	                }

	                @Override
	                protected void onNotKakaoStoryUser() {
	                    try {
	                        super.onNotKakaoStoryUser();
	                    } finally {
	                        deleteTempFiles();
	                    }
	                }

	                @Override
	                protected void onFailure(final APIErrorResult errorResult) {
	                    try {
	                        super.onFailure(errorResult);
	                    } finally {
	                        deleteTempFiles();
	                    }
	                }

	                private void deleteTempFiles() {
	                    if(files.size() > 0){
	                    	for(int i = 0 ; i < files.size();i++){
	                    		files.get(i).delete();	
	                    	}
	                    }
	                }

	            }, files);
	        } catch (Exception e) {
	            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
	        }
	    }
	
		
	/**
	 * 카스 텍스트
	 */
	 private void requestPostNote() {
//	        final NoteKakaoStoryPostParamBuilder postParamBuilder = new NoteKakaoStoryPostParamBuilder("[여우비]\n"+mTitle+"\n"+mText+"\n워킹맘들의 커뮤니티\n"+"market://details?id=com.foxrainbxm");
	        final NoteKakaoStoryPostParamBuilder postParamBuilder = new NoteKakaoStoryPostParamBuilder("[여우비]\n"+mTitle+"\n"+mText+"\n워킹맘들의 커뮤니티");
	        if(kakaoAuth == 0) {
		        postParamBuilder.setPermission(PERMISSION.PUBLIC);
	        }
	        else if(kakaoAuth == 1) {
	        	 postParamBuilder.setPermission(PERMISSION.FRIENDS);
	        }
	       

	        requestPost(StoryType.NOTE, postParamBuilder);
	    }
	
	   private void requestPost(final StoryType storyType, final BasicKakaoStoryPostParamBuilder postParamBuilder) {
	        // 앱이 설치되어 있는 경우 kakao<app_key>://kakaostory?place=1111 로 이동.
	        // 앱이 설치되어 있지 않은 경우 market://details?id=com.kakao.sample.kakaostory&referrer=kakaostory로 이동
//	        postParamBuilder.setAndroidExecuteParam(execParam).setIOSExecuteParam(execParam).setAndroidMarketParam(marketParam).setIOSMarketParam(marketParam);
//		   postParamBuilder.setAndroidExecuteParam(execParam).setAndroidMarketParam(marketParam);
//		   postParamBuilder.setAndroidExecuteParam(execParam).setAndroidMarketParam(marketParam);
		   
		   
//		   postParamBuilder.setAndroidExecuteParam(execParam).setAndroidMarketParam(marketParam);
	        try {
	            final Bundle parameters = postParamBuilder.build();
	            KakaoStoryService.requestPost(storyType, new MyKakaoStoryHttpResponseHandler<MyStoryInfo>() {
	                @Override
	                protected void onHttpSuccess(final MyStoryInfo myStoryInfo) {
	                    if(myStoryInfo.getId() != null) {
	                    	BrUtilManager.getInstance().showToast(mContext, "공유되었습니다.");
	                    	dismiss();
	                    } else{
	                    	BrUtilManager.getInstance().showToast(mContext, "공유가 실패되었습니다.");
	                    }
	                }
	            }, parameters);
	         
	        } catch (KakaoParameterException e) {
	            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
	        }
	    }
	   
	   private void redirectLoginActivity() {
	        final Intent intent = new Intent(mContext, KakaoStoryLoginActivity.class);
	        mContext.startActivity(intent);
	    }

	   private abstract class MyKakaoStoryHttpResponseHandler<T> extends KakaoStoryHttpResponseHandler<T> {

	        @Override
	        protected void onHttpSessionClosedFailure(final APIErrorResult errorResult) {
	            redirectLoginActivity();
	        }

	        @Override
	        protected void onNotKakaoStoryUser() {
	            Toast.makeText(mContext, "not KakaoStory user", Toast.LENGTH_SHORT).show();
	        }

	        @Override
	        protected void onFailure(final APIErrorResult errorResult) {
	            final String message = "MyKakaoStoryHttpResponseHandler : failure : " + errorResult;
	            Logger.getInstance().d(message);
	            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
	        }
	    }
	
	   
	   private void publishStory() {
	        Session session = Session.getActiveSession();
	        if (session != null) {
	             
	            // Check for publish permissions    
	            List<String> permissions = session.getPermissions();
	            if (!isSubsetOf(PERMISSIONS, permissions)) {
	                pendingPublishReauthorization = true;
	                Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(mContext, PERMISSIONS);
	                session.requestNewPublishPermissions(newPermissionsRequest);
	                return;
	           }
	 
	           /* Bundle postParams = new Bundle();
	            postParams.putString("name", "몽키3 추천음악");
	            postParams.putString("caption", "-girls day-");
	            postParams.putString("description", "걸스데이랑께");
	            postParams.putString("link", "http://www.monkey3.co.kr/#/etc.album&albumID=185582");
	            postParams.putString("picture", "http://imgtest.monkey3.co.kr/get_image.php?type=album&id=185582&w=100");*/
	            
	            Bundle postParams = new Bundle();
	            postParams.putString("name", "[여우비]");
	            postParams.putString("caption", "워킹맘들의 커뮤니티");
	            postParams.putString("description", "[여우비]"+"\n"+mTitle+"\n"+mText+"\n워킹맘들의 커뮤니티");//\n"+"market://details?id=com.foxrainbxm");
	           postParams.putString("link", "https://play.google.com/store/apps/details?id=com.foxrainbxm");
	           postParams.putString("message",  "[여우비]"+"\n"+mTitle+"\n"+mText+"\n워킹맘들의 커뮤니티");//\n"+"market://details?id=com.foxrainbxm");
	 
	            Request.Callback callback= new Request.Callback() {
	                public void onCompleted(Response response) {
	                    JSONObject graphResponse = response.getGraphObject().getInnerJSONObject();
	                    String postId = null;
	                    try {
	                        postId = graphResponse.getString("id");
	                    } catch (JSONException e) {
	                        //Log.i(TAG, "JSON error "+ e.getMessage());
	                    }
	                     
	                    FacebookRequestError error = response.getError();
	                    if (error != null) {
	                    	 Log.i("a", "JSON error ");
	                        //Toast.makeText(FacebookActivity.this, error.getErrorMessage(), Toast.LENGTH_SHORT).show();
	                    }
	                    else {
	                    	 Log.i("a", "JSON error ");
	                            //oast.makeText(FacebookActivity.this, postId, Toast.LENGTH_LONG).show();
	                    }
	                }
	            };
	 
	            Request request = new Request(session, "me", postParams, HttpMethod.POST, callback);
	            RequestAsyncTask task = new RequestAsyncTask(request);
	            task.execute();
	        }
	    }
	   
	 private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	    private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	    private boolean pendingPublishReauthorization = false;
	    ArrayList<Bitmap>bitmaparray = new ArrayList<Bitmap>();
//	    private void publishStory(final ArrayList<String> imgfile) {
//	        Session session = Session.getActiveSession();
//	        if (session != null) {
//	            // Check for publish permissions
//	            List<String> permissions = session.getPermissions();
//	            
//	            if (!isSubsetOf(PERMISSIONS, permissions)) {
//	                pendingPublishReauthorization = true;
//	                Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(mContext, PERMISSIONS);
//	                session.requestNewPublishPermissions(newPermissionsRequest);
//	                
//	                if(dialog != null){
//                    	dialog.dismiss();
//                    }
//                    	BrUtilManager.getInstance().showToast(mContext, "권한이 없습니다. 페이스북 계정을 확인해주세요.");
//	                return;
//	            }
//	 
//	            
//	            Bundle postParams = new Bundle();
//	            postParams.putString("name", "[여우비]"+"\n"+mTitle+"\n"+mText+"\n워킹맘들의 커뮤니티");//\n"+"market://details?id=com.foxrainbxm");
//	            postParams.putString("caption", "워킹맘들의 커뮤니티");
//	            postParams.putString("description", "[여우비]"+"\n"+mTitle+"\n"+mText+"\n워킹맘들의 커뮤니티\n"+"https://play.google.com/store/apps/details?id=com.foxrainbxm");
////	            postParams.putString("link", "https://play.google.com/store/apps/details?id=com.foxrainbxm");
//	            postParams.putString("link", "https://play.google.com/store/apps/details?id=com.foxrainbxm");
//	            
//	            postParams.putString("message",  "[여우비]"+"\n"+mTitle+"\n"+mText+"\n워킹맘들의 커뮤니티");//\n"+"market://details?id=com.foxrainbxm");
//	           // postParams.putString("picture", "http://imgtest.monkey3.co.kr/get_image.php?type=album&id=185582&w=100");
//	            
//	           /* Bundle postParams = new Bundle();
//	            postParams.putString("name", "[여우비]"+"\n"+mTitle+"\n"+mText+"\n워킹맘들의 커뮤니티\n"+"market://details?id=com.foxrainbxm");
//	            postParams.putString("link", "market://details?id=com.foxrainbxm");
//	            postParams.putString("message", "[여우비]"+"\n"+mTitle+"\n"+mText+"\n워킹맘들의 커뮤니티\n"+"market://details?id=com.foxrainbxm");*/
//	          //  postParams.putString("caption", "Build great social apps and get more installs.");
//	         //   postParams.putString("message", "test Msg");
//	 
//	            byte[] data = null;
//	 
//	            if(imgfile != null){
//	            	//for (int i = 0 ; i < imgfile.size();i++){
//	            		Bitmap bit = BitmapFactory.decodeFile(imgfile.get(0));
//	            		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			            bit.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//			            data = baos.toByteArray();
//			            postParams.putByteArray("picture", data);
//			            
//			            bitmaparray.add(bit);
//	            	//}
//	            //	Bitmap bit = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher);
//	            }
//	            Request.Callback callback = new Request.Callback() {
//	                public void onCompleted(Response response) {
//	                    if (response != null) {
//	                        JSONObject graphResponse = response.getGraphObject().getInnerJSONObject();
//	                        String postId = null;
//	                        try {
//	                            postId = graphResponse.getString("id");
//	                        } catch (JSONException e) {
//	                        	WriteFileLog.writeException(e);
//	                        }
//	                        FacebookRequestError error = response.getError();
//	                        if(dialog != null){
//	                        	dialog.dismiss();
//	                        }
//	                        if (error != null) {
//	                        	BrUtilManager.getInstance().showToast(mContext, "공유가 실패되었습니다.");
//	                        }else{
//	                        	BrUtilManager.getInstance().showToast(mContext, "공유되었습니다.");
//	                        	deleteTempFiles(imgfile);
//	                        	ClearBitmap(bitmaparray);
//		                    	dismiss();	
//	                        }
//	                    
//	                    }
//	                }
//	            };
//	            Request request = null;
//	            if(imgfile != null){
//	            	request = new Request(session, "me/photos", postParams,HttpMethod.POST, callback);	
//	            }else{
//	            	request = new Request(session,"me/feed", postParams,HttpMethod.POST, callback);
//	            }
//	            RequestAsyncTask task = new RequestAsyncTask(request);
//	            task.execute();
//	        }
//	    }
	    
	    private void ClearBitmap(ArrayList<Bitmap>arraylist){
	    	try {
	    		if(arraylist.size()>0){
		    		for(int i = 0 ; i < arraylist.size(); i++){
		    			arraylist.get(i).recycle();
		    		}
		    	}	
			} catch (Exception e) {
				WriteFileLog.writeException(e);
			}
	    	
	    }
	    private void deleteTempFiles(ArrayList<String>  imgfile) {
	    	try {
	    		if(imgfile.size() > 0){
		    		for(int i = 0 ; i < imgfile.size(); i++){
		    			File file = new File(imgfile.get(i));
		    			
		    			file.delete();
		    		}
		    	}	
			} catch (Exception e) {
				WriteFileLog.writeException(e);
			}
        }
	 
	    private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
	        for (String string : subset) {
	            if (!superset.contains(string)) {
	                return false;
	            }
	        }
	        return true;
	    }
	 
	/*
	 * public PopupLinkButton(Context context, String title, String text,
	 * OnClickListener l) { super(context);
	 * 
	 * this.mContext = context; this.mTitle = title; this.mText = text;
	 * 
	 * requestWindowFeature(Window.FEATURE_NO_TITLE);
	 * this.getWindow().setBackgroundDrawable(new
	 * ColorDrawable(android.graphics.Color.TRANSPARENT));
	 * setContentView(R.layout.popup_link_button);
	 * 
	 * // 카카오스토리 공유 TextView kakaoStory =
	 * (TextView)findViewById(R.id.kakaostory_link);
	 * kakaoStory.setOnClickListener(new View.OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { startActionSend("kakao.story",
	 * mTitle, mText); } });
	 * 
	 * // 카카오톡 공유 TextView kakaoTalk =
	 * (TextView)findViewById(R.id.kakaotalk_link);
	 * kakaoTalk.setOnClickListener(new View.OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { startActionSend("kakao.talk",
	 * mTitle, mText); } });
	 * 
	 * // 페이스북 공유 TextView facebook =
	 * (TextView)findViewById(R.id.facebook_link);
	 * facebook.setOnClickListener(new View.OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { startActionSend("facebook",
	 * mTitle, mText); } });
	 * 
	 * // 취소 Button btnPopup0 = (Button) findViewById(R.id.btnPopup0);
	 * btnPopup0.setOnClickListener(new View.OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { dismiss(); } }); }
	 */
	
	
	/*
	private void publishStory() {
	    Session session = Session.getActiveSession();

	    if (session != null){

	        // Check for publish permissions    
	        List<String> permissions = session.getPermissions();
	        if (!isSubsetOf(PERMISSIONS, permissions)) 
	        {
	         //   pendingPublishReauthorization = true;
	            Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(this, PERMISSIONS);
	            session.requestNewPublishPermissions(newPermissionsRequest);
	            return;
	        }

	        Bundle postParams = new Bundle();
	        postParams.putString("name", "Facebook SDK for Android");
	        postParams.putString("caption", "Build great social apps and get more installs.");
	        postParams.putString("description", "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
	        postParams.putString("link", "https://developers.facebook.com/android");
	        postParams.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

	        Request.Callback callback= new Request.Callback() {
	            public void onCompleted(Response response) {
	                JSONObject graphResponse = response
	                                           .getGraphObject()
	                                           .getInnerJSONObject();
	                String postId = null;
	                try {
	                    postId = graphResponse.getString("id");
	                } catch (JSONException e) {
	                    WriteFileLog.writeException(e);
	                }
	                FacebookRequestError error = response.getError();
	                if (error != null) {
	                    Toast.makeText(mContext.getApplicationContext(),error.getErrorMessage(),Toast.LENGTH_SHORT).show();
	                    } else {
	                        Toast.makeText(mContext.getApplicationContext(), postId,Toast.LENGTH_LONG).show();
	                }
	            }
	        };

	        Request request = new Request(session, "me/feed", postParams, HttpMethod.POST, callback);

	        RequestAsyncTask task = new RequestAsyncTask(request);
	        task.execute();
	    }

	}*/
	// ACTION_SEND 방식으로 공유 (API방식으로 변경?)
	private void startActionSend(String appName, String title, String text) {
		
		ComponentName appComponentName = null;
		
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, title);
		intent.putExtra(Intent.EXTRA_TEXT, text);

		PackageManager pm = mContext.getPackageManager();
		List<ResolveInfo> activityList = pm.queryIntentActivities(intent, 0);
		for (ResolveInfo app : activityList) {
			if (app.activityInfo.name.contains(appName)) {
				ActivityInfo activity = app.activityInfo;
				appComponentName = new ComponentName(activity.applicationInfo.packageName, activity.name);
				break;
			}else{
				Log.e("", "");
			}
		}
		
		if (appComponentName != null) {
			intent.setComponent(appComponentName);
			mContext.startActivity(intent);
		}
		
		dismiss();
	}
}
























