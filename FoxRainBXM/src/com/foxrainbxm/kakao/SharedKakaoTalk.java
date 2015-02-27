package com.foxrainbxm.kakao;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.foxrainbxm.Common;
import com.foxrainbxm.R;
import com.foxrainbxm.WriteFileLog;
import com.kakao.AppActionBuilder;
import com.kakao.AppActionInfoBuilder;
import com.kakao.KakaoLink;
import com.kakao.KakaoParameterException;
import com.kakao.KakaoTalkLinkMessageBuilder;
import com.kakao.internal.AppActionInfo;

public class SharedKakaoTalk {
	static Bitmap mBitmap = null;
	/**
	 * 카톡링크 텍스트만.
	 * @param title
	 * @param content
	 * @param imageType
	 * @param buttonType
	 */
	public static void SendKaKaoTalkLink(Activity mContext, KakaoLink kakaoLink,KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder,String title, String content,String userid){
		try {
			String msg = "꿈꾸는 워킹맘 커뮤니티 – 여우비에서 " + userid +"님이 보낸 메세지 입니다.\n" + title;

			kakaoTalkLinkMessageBuilder.addText(msg);
			/*kakaoTalkLinkMessageBuilder.addText("[여우비]"+title+"\n"+content+"\n워킹맘들의 커뮤니티\n"+"market://details?id=com.foxrainbxm");*/
			/*  kakaoTalkLinkMessageBuilder.addAppLink("카카오톡 으로이동",
	                    new AppActionBuilder()
	                        .addActionInfo(AppActionInfoBuilder.createAndroidActionInfoBuilder().setExecuteParam("execparamkey1=1111").setMarketParam("referrer=kakaotalklink").build())
	                        .addActionInfo(AppActionInfoBuilder.createiOSActionInfoBuilder(AppActionBuilder.DEVICE_TYPE.PHONE).setExecuteParam("execparamkey1=1111").build()).build()
	                );*/market://details?id=com.foxrainbxm
	                	kakaoTalkLinkMessageBuilder.addAppButton("여우비 앱으로이동",  new AppActionBuilder()
	                	.addActionInfo(AppActionInfoBuilder.createAndroidActionInfoBuilder().setExecuteParam("execparamkey1=1111").setMarketParam("market://details?id=com.foxrainbxm").build())
	                	.addActionInfo(AppActionInfoBuilder.createiOSActionInfoBuilder(AppActionBuilder.DEVICE_TYPE.PHONE).setExecuteParam("execparamkey1=1111").build()).build());
			/*      kakaoTalkLinkMessageBuilder.addAppLink("여우비 앱으로이동",
	                    new AppActionBuilder()
	                        .addActionInfo(AppActionInfoBuilder.createAndroidActionInfoBuilder().setExecuteParam("execparamkey1=1111").setMarketParam("market://details?id=com.foxrainbxm").build())
	                        .addActionInfo(AppActionInfoBuilder.createiOSActionInfoBuilder(AppActionBuilder.DEVICE_TYPE.PHONE).setExecuteParam("execparamkey1=1111").build()).build()
	                );*/
			kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder.build(), mContext);
		} catch (Exception e) {
			alert(mContext,e.getMessage());
		}
	}

	//	private static void ResizeImage(String url,  KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder) {
	//		Bitmap bitmap = getImageFromURL(url);
	//		int height = bitmap.getHeight();
	//		int width = bitmap.getWidth();
//			float scale = 0.0f;
//			if(width > 300) {
//				scale = width / 300;
//				width = (int) (width / scale);
//				if(width > height) {
//					height = (int) (height / scale);
//				}
//				else {
//					height = (int) (height * scale);
//				}
//			}
	//
	//		try {
	//			kakaoTalkLinkMessageBuilder.addImage(url, width, height);
	//		} catch (KakaoParameterException e) {
	//			WriteFileLog.writeException(e);
	//		}
	//
	//	}
	public static void urlTobitmap(final String _photoURL, final KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder, final KakaoLink kakaoLink, final Activity mContext) {
		AsyncTask<String, Void, Bitmap> asyn = new AsyncTask<String, Void, Bitmap> () {

			@Override
			protected Bitmap doInBackground(String... params) {
				URL url = null;
				Bitmap bitmap = null;
				try {
					url = new URL(params[0]);
					HttpGet httpRequest = null;
					try {
						httpRequest = new HttpGet(url.toURI());
					} catch (Exception e) {
						WriteFileLog.writeException(e);
					}
					HttpClient httpclient = new DefaultHttpClient();
					HttpResponse response = (HttpResponse) httpclient
							.execute(httpRequest);
					HttpEntity entity = response.getEntity();
					BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
					InputStream instream = bufHttpEntity.getContent();
					bitmap = BitmapFactory.decodeStream(instream);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					WriteFileLog.writeException(e);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					WriteFileLog.writeException(e);
				}
				
				return bitmap;
			}
			
			@Override
			protected void onPostExecute(Bitmap result) {
				super.onPostExecute(result);
				
				mBitmap = result;
				
				try {
					int height = mBitmap.getHeight();
					int width = mBitmap.getWidth();
					
					float scale = 0.0f;
					if(width > 300) {
						scale = width / 300;
						width = (int) (width / scale);
						if(width > height) {
							height = (int) (height / scale);
						}
						else {
							height = (int) (height / scale);
						}
					}
					
					kakaoTalkLinkMessageBuilder.addImage(_photoURL, width, height);
//					kakaoTalkLinkMessageBuilder.addAppButton("여우비 앱으로 이동");
					kakaoTalkLinkMessageBuilder.addAppButton("여우비 앱으로이동",  new AppActionBuilder()
					.addActionInfo(AppActionInfoBuilder.createAndroidActionInfoBuilder().setExecuteParam("execparamkey1=1111").setMarketParam("market://details?id=com.foxrainbxm").build())
					.addActionInfo(AppActionInfoBuilder.createiOSActionInfoBuilder(AppActionBuilder.DEVICE_TYPE.PHONE).setExecuteParam("execparamkey1=1111").build()).build());
					kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder.build(), mContext);
				} catch (KakaoParameterException e) {
					WriteFileLog.writeException(e);
				}
			}
		};
		asyn.execute(_photoURL);
	}
		/**
		 * 카톡링크 이미지, 비디오
		 * @param title
		 * @param content
		 * @param imgurl
		 */
		public static void SendKaKaoTalkLink(Activity mContext, KakaoLink kakaoLink,KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder,String title, String content, ArrayList<String> imgurl,String userid){
			try {
				//kakaoTalkLinkMessageBuilder.addText("[여우비]"+"\n"+title+"\n"+content + "\n워킹맘들의 커뮤니티\n"/*+"market://details?id=com.foxrainbxm"*/);

				String msg = "꿈꾸는 워킹맘 커뮤니티 – 여우비에서 " + userid +"님이 보낸 메세지 입니다.\n\n" + title;

				kakaoTalkLinkMessageBuilder.addText(msg);

				if(imgurl.get(0).startsWith("http")){
					//				 ResizeImage(imgurl.get(0), kakaoTalkLinkMessageBuilder);


					urlTobitmap(imgurl.get(0), kakaoTalkLinkMessageBuilder, kakaoLink, mContext);
//					if(mBitmap == null) {
//						Toast.makeText(mContext, "비트맵이 널이에요!!", 1).show();
//					}
//					else {
//						Toast.makeText(mContext, "비트맵이 널이아니에요!!", 1).show();
//					}
					//					 int height = bitmap.getHeight();
					//						int width = bitmap.getWidth();
					//						float scale = 0.0f;
					//						if(width > 300) {
					//							scale = width / 300;
					//							width = (int) (width / scale);
					//							if(width > height) {
					//								height = (int) (height / scale);
					//							}
					//							else {
					//								height = (int) (height * scale);
					//							}
					//						}
					//
					//						try {
					//							kakaoTalkLinkMessageBuilder.addImage(imgurl.get(0), width, height);
					//						} catch (KakaoParameterException e) {
					//							WriteFileLog.writeException(e);
					//						}
					//				 }


//					kakaoTalkLinkMessageBuilder.addImage(imgurl.get(0), 300, 200);
				}else{
					urlTobitmap(Common.ImageUrl + imgurl.get(0), kakaoTalkLinkMessageBuilder, kakaoLink, mContext );
//					kakaoTalkLinkMessageBuilder.addImage(Common.ImageUrl + imgurl.get(0), 300, 200);
				}
				/*  kakaoTalkLinkMessageBuilder.addAppLink("카카오톡 으로이동",
		                    new AppActionBuilder()
		                        .addActionInfo(AppActionInfoBuilder.createAndroidActionInfoBuilder().setExecuteParam("execparamkey1=1111").setMarketParam("referrer=kakaotalklink").build())
		                        .addActionInfo(AppActionInfoBuilder.createiOSActionInfoBuilder(AppActionBuilder.DEVICE_TYPE.PHONE).setExecuteParam("execparamkey1=1111").build()).build()
		                );*/

				//  kakaoTalkLinkMessageBuilder.addAppButton("여우비 앱으로이동");
				
			} catch (Exception e) {
				WriteFileLog.writeException(e);
				alert(mContext,e.getMessage());
			}
		}

		/**
		 * 카카오톡 비디오
		 * @param title
		 * @param content
		 * @param videothum
		 * @param videourl
		 */
		public static void SendKaKaoTalkLink(Activity mContext, KakaoLink kakaoLink,KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder,String title, String content,String videothum, String videourl,String userid){
			try {

				String msg = "꿈꾸는 워킹맘 커뮤니티 – 여우비에서 " + userid +"님이 보낸 메세지 입니다.\n" + title;

				kakaoTalkLinkMessageBuilder.addText(msg);
				//kakaoTalkLinkMessageBuilder.addText("[여우비]"+"\n"+title+"\n"+content+"\n워킹맘들의 커뮤니티\n"/*+"market://details?id=com.foxrainbxm"*/);
				if(videothum.startsWith("http")){
					kakaoTalkLinkMessageBuilder.addImage(videothum, 300, 200);
				}else{
					kakaoTalkLinkMessageBuilder.addImage(Common.ImageUrl + videothum, 300, 200);
				}

				/*  kakaoTalkLinkMessageBuilder.addAppLink("카카오톡 으로이동",
		                    new AppActionBuilder()
		                        .addActionInfo(AppActionInfoBuilder.createAndroidActionInfoBuilder().setExecuteParam("execparamkey1=1111").setMarketParam("referrer=kakaotalklink").build())
		                        .addActionInfo(AppActionInfoBuilder.createiOSActionInfoBuilder(AppActionBuilder.DEVICE_TYPE.PHONE).setExecuteParam("execparamkey1=1111").build()).build()
		                );*/
				//kakaoTalkLinkMessageBuilder.addWebLink("동영상으로 이동", Common.ImageUrl + videourl);
				kakaoTalkLinkMessageBuilder.addAppButton("여우비 앱으로이동",  new AppActionBuilder()
				.addActionInfo(AppActionInfoBuilder.createAndroidActionInfoBuilder().setExecuteParam("execparamkey1=1111").setMarketParam("market://details?id=com.foxrainbxm").build())
				.addActionInfo(AppActionInfoBuilder.createiOSActionInfoBuilder(AppActionBuilder.DEVICE_TYPE.PHONE).setExecuteParam("execparamkey1=1111").build()).build());
				//kakaoTalkLinkMessageBuilder.addAppButton("여우비 앱으로이동");
				kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder.build(), mContext);
			} catch (Exception e) {
				WriteFileLog.writeException(e);
				alert(mContext,e.getMessage());
			}
		}

		public static void alert(Activity mContext,String message) {
			new AlertDialog.Builder(mContext)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle(R.string.app_name)
			.setMessage(message)
			.setPositiveButton(android.R.string.ok, null)
			.create().show();
		}
	}
