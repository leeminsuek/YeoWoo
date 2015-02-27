package com.foxrainbxm;

import java.io.File;
import java.security.MessageDigest;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.brainyxlib.BrUtilManager;
import com.foxrainbxm.util.BrImageCacheManager;
import com.foxrainbxm.vo.MemberVO;
import com.foxrainbxm.volley.ImageCacheManager;
import com.foxrainbxm.volley.ImageCacheManager.CacheType;
import com.foxrainbxm.volley.RequestManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FoxRainBXM extends Application {
	public int mCurrentIndex = 0;
	private Typeface typeface;
	private Typeface typefaceBold;
	public DisplayImageOptions options ;
	public DisplayImageOptions profileOptions;
	public String notiPushType = "";
	public String notiPushNo = "";
	
	String test = "rvewarvewa<div><br></div><img class=thumbnails id=thumbnail1 src=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFwAAABkCAYAAAALxleYAAAAGXRFW…S4Fw0xw6uET+AhZgyr/0S+r7UkrTVqLRMnlrBzMLB7/gowAApi+2zyGVYYAAAAAElFTkSuQmCC style=width: 200px; height: 200px; display: block;><div>gdsafgas<br><img class=thumbnails id=thumbnail2 src=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFwAAABkCAYAAAALxleYAAAAGXRFW…QaAiOhwuN4CIzf5ZFI5I9Z+GSa/kpXJU584dG0X6Z9/y/AAPsE/YYDCxXpAAAAAElFTkSuQmCC style=width: 200px; height: 200px; display: block;></div><div><br></div><div>fdasfdas</div><img class=thumbnails id=thumbnail3 src=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFwAAABkCAYAAAALxleYAAAAGXRFW…QaAiOhwuN4CIzf5ZFI5I9Z+GSa/kpXJU584dG0X6Z9/y/AAPsE/YYDCxXpAAAAAElFTkSuQmCC style=width: 200px; height: 200px; display: block;> ";
	String test2 = "<div>sdfgsdfadsfaf</div><img class=thumbnails id=thumbnail1 src=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFwAAABkCAYAAAALxleYAAAAGXRFW…S4Fw0xw6uET+AhZgyr/0S+r7UkrTVqLRMnlrBzMLB7/gowAApi+2zyGVYYAAAAAElFTkSuQmCC style=width: 200px; height: 200px; display: block;><div>gdsafgas<br><img class=thumbnails id=thumbnail2 src=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFwAAABkCAYAAAALxleYAAAAGXRFW…QaAiOhwuN4CIzf5ZFI5I9Z+GSa/kpXJU584dG0X6Z9/y/AAPsE/YYDCxXpAAAAAElFTkSuQmCC style=width: 200px; height: 200px; display: block;></div><div><br></div><div>fdasfdas</div><img class=thumbnails id=thumbnail3 src=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFwAAABkCAYAAAALxleYAAAAGXRFW…QaAiOhwuN4CIzf5ZFI5I9Z+GSa/kpXJU584dG0X6Z9/y/AAPsE/YYDCxXpAAAAAElFTkSuQmCC style=width: 200px; height: 200px; display: block;> ";
	public ImageLoader imageloder;
	
	
	private static int DISK_IMAGECACHE_SIZE = 1024*1024*10;
	private static CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = CompressFormat.PNG;
	private static int DISK_IMAGECACHE_QUALITY = 100;  //PNG is lossless so quality is ignored but must be provided
	@Override
	public void onCreate() {
		super.onCreate();
		loadTypeface();
		RequestManager.init(this);
		createImageCache();
		options = BrImageCacheManager.getInstance()
		.initImageLoader(getApplicationContext(), 
		R.drawable.main_ctt_list_bg_n, R.drawable.main_ctt_list_bg_n,
		BrUtilManager.getInstance().getScreenWidth(getApplicationContext()), 
		BrUtilManager.getInstance().getScreenHeight(getApplicationContext()));
		
		profileOptions = BrImageCacheManager.getInstance()
						.initImageLoader(getApplicationContext(), 
						R.drawable.profile_title_n, R.drawable.profile_title_n,
						BrUtilManager.getInstance().getScreenWidth(getApplicationContext()), 
						BrUtilManager.getInstance().getScreenHeight(getApplicationContext()));
		imageloder = BrImageCacheManager.getInstance().getImageLoader();
		//options = BrImageCacheManager.getInstance().ImageLoaderInit(getApplicationContext(), R.drawable.bg_scrap_list_prv_n, R.drawable.bg_scrap_list_prv_n);
		//listOptions = BrImageCacheManager.getInstance().ImageLoaderInit(getApplicationContext(), R.drawable.main_ctt_list_bg_n, R.drawable.main_ctt_list_bg_n);
		//HtmlToString(test2.replace("<br>", "\n"));
		getAppKeyHash();
	}
	
	private void createImageCache(){
		ImageCacheManager.getInstance().init(this,
				this.getPackageCodePath()
				, DISK_IMAGECACHE_SIZE
				, DISK_IMAGECACHE_COMPRESS_FORMAT
				, DISK_IMAGECACHE_QUALITY
				, CacheType.MEMORY ); //
	}	
	private void getAppKeyHash() {
	    try {
	        PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md;
	            md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            String something = new String(Base64.encode(md.digest(), 0));
	            Log.d("Hash key", something);
	        }
	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	        Log.e("name not found", e.toString());
	    }
	}


	/*public void HtmlToString(String test){
		String values = "";
		String var1 = "[1;";
		String var2= "[2;";
		String var3= "[3;";
		String var4 = "";
		String texttype = "";
		boolean flag = true;
		int imgindex = -1; 
		int aindex = -1;
		int divindex = -1;
		String rersult = test;
		int type = 0;
		String data = "";
		while(flag){
			imgindex = rersult.indexOf("<img");
			aindex = rersult.indexOf("<a");
			divindex = rersult.indexOf("<div");
			
			if(divindex == 0){ //시작이 div
				data = rersult.substring(0, rersult.indexOf("</div>")).replace("<div>", "").replace("</div>", ""); // 순수 텍스트
				if(type == 1){ // 기존에도 타입이 같았다면
					var4 += data += data;
				}else{ // 달랐다면
					var4 += var1 + data ;
				}
				rersult = rersult.substring(rersult.indexOf("</div>")+"</div>".length(),rersult.length());
				type = 1;
			}else if(aindex == 0){// 시작이 a
				type = 3;
			}else if(imgindex == 0){ // 시작이 이미지
				rersult.indexOf("id=thumbnail");
				data = rersult.substring(rersult.indexOf("id=thumbnail") + "id=thumbnail".length(), rersult.indexOf("id=thumbnail") + "id=thumbnail".length() + 1);
				//data = dumy.substring(0, 1);
				if(type != 2){
					var4 += "]";
					var4 += var2 + data + "]";
				}
				
				rersult = rersult.substring(rersult.indexOf(">")+">".length(),rersult.length());
				type = 2;
			}else{ // 시작이 없음
				type = 1;
				values = rersult.substring(0,rersult.indexOf("<")); // 태그시작지점까지 텍스트를 변수에저장
				rersult = rersult.substring(0, values.length());
			}
		}
		if(test.charAt(0) != '<'){ // 시작이 태그가 아니라면
			values = test.substring(0,test.indexOf("<")); // 태그시작지점까지 텍스트를 변수에저장
			// 그후 태그를 알아낸다.
			// 
			if(isStartDiv(test.substring(values.length())) || isStartP(test.substring(values.length()))){ // 시작태그가 div 거나P면
				
				String val = test.substring(values.length(), test.indexOf("<img")).replace("<div>", "");
				String val2 = val.substring(0, val.length()).replace("</div>","");
				int atag = val2.indexOf("<a");
				if(atag > 0){ // a태그가 0보다 크면 중간에 a태그가 있다고 판단
					String val3 = test.substring(values.length(), test.indexOf("<a")).replace("<div>", "");
					String val4 = val.substring(0, val.length()).replace("</div>","");
				}else{ // a태그가 없고 순수 텍스트
					values = values + val2;
				}
				
				values = values + test.substring(values.length(), test.indexOf("<img")).replace("<div>", "");
			}else if(isStartImg(test.substring(values.length()))){ // 시작태그가 img이면
				
			}else if(isStartA(test.substring(values.length()))){ // 시작태그가 a태그
				
			}
		}else{
			if(test.substring(0, test.indexOf(">")).equals("<div")){
				values = test.substring(0, test.indexOf("</div"));
			}
		}
		
		Log.e("values", "" + values);
	}
	
	public String returntoString(String str){
		return str.replace("<div>", "");
	}
	
	public boolean isStartDiv(String str){
		return str.substring(0, 4).equals("<div");
	}
	
	public boolean isStartImg(String str){
		return str.substring(0, 4).equals("<img");
	}
	
	public boolean isStartA(String str){
		return str.substring(0, 2).equals("<a");
	}
	
	public boolean isStartP(String str){
		return str.substring(0, 2).equals("<p");
	}*/
	
	
	
	public String getFileDir_Ex() {
		String result = "";
		try {
			int sdkVersion = Integer.parseInt(Build.VERSION.SDK);
			if (sdkVersion < 8) {
				File extSt = Environment.getExternalStorageDirectory();
				result = extSt.getAbsolutePath() + "/Android" + "/data"+ "/com.foxrainbxm";
			} else {
				result = getExternalFilesDir(null).getAbsolutePath();
			}
		} catch (Exception e) {
		}
		return result;
	}
	public void loadTypeface() {
		if (typeface == null)
			typeface = Typeface.createFromAsset(getAssets(),"NanumBarunGothic.otf");

		if (typefaceBold == null)
			typefaceBold = Typeface.createFromAsset(getAssets(),"NanumBarunGothicBold.otf");
	}

	/**
	 *
	 * @return
	 */
	public Typeface getFontNormal() {
		return typeface;
	}

	/**
	 * 
	 * @return
	 */
	public Typeface getFontBold() {
		return typefaceBold;
	}

	/**
	 * 
	 */
	private MemberVO userInfo;

	public MemberVO getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(MemberVO userInfo) {
		this.userInfo = userInfo;
	}
}
