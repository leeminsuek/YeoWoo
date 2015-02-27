package com.foxrainbxm.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ImageView;

import com.foxrainbxm.WriteFileLog;
import com.foxrainbxm.kakao.SharedActivity;
import com.foxrainbxm.tab7.PopupLinkButton;

public class ImageDownloader {
	private Context _context;
	private Object _callTarget;
	private String _callMethod;

	public ImageDownloader(Context $context) {
		super();
		_context = $context;
	}

	public ImageDownloader(Context $context, Object $target, String $method) {
		super();
		_context = $context;
		registerCallback($target, $method);
	}

	public void registerCallback(Object $target, String $method) {
		_callTarget = $target;
		_callMethod = $method;
	}

	protected void invokeCallback() {
		invokeCallback(_callTarget, _callMethod);
	}

	protected void invokeCallback(Object $target, String $method) {
		if ($target == null || $method == null)
			return;

		try {
			Class<?> kClass = $target.getClass();
			Method kInvocation = kClass.getMethod($method);
			kInvocation.invoke($target);
		} catch (Exception $e) {
			WriteFileLog.writeException($e);
		}
	}

	private boolean isNull(String $str) {
		boolean result = false;

		try {
			if ($str == null || $str.trim().length() == 0)
				result = true;
		} catch (Exception e) {
			result = true;
		}
		return result;
	}

	/**
	 * 이미지 불러오기
	 */
	public void loadImage(ImageView $imageview) {
		// tag가 없을 때
		if ($imageview.getTag() == null) {
			if (_callMethod != null && _callTarget != null) {
				invokeCallback();
			}
			return;
		}

		// url이 없을
		String kUrl = $imageview.getTag().toString();
		if (isNull(kUrl)) {
			return;
		}

		if (checkFile(kUrl)) {
			loadFile($imageview);

			if (_callMethod != null && _callTarget != null) {
				invokeCallback();
			}
		} else {
			// new ImageDownloadTask().execute($imageview);
		}
	}

	/**
	 * 이미지를 다운로드만 해놓기
	 * 
	 * @param $url
	 */
	public void downloadImage(String $url) {
			if (!checkFile($url)) { // 캐쉬파일이 없으면 다운로드
				new ImageDownloadOnlyTask().execute($url);
			}else{ //있으면 경로전달
				String path = getFilePath($url);
				if(PopupLinkButton.getinstance() != null){
					PopupLinkButton.getinstance().UploadImageKakao(path);
				}
			}
	}
	
	/**
	 * 이미지를 다운로드만 해놓기
	 * 
	 * @param $url
	 */
	public void FAcebookdownloadImage(String $url) {
			if (!checkFile($url)) { // 캐쉬파일이 없으면 다운로드
				new ImageDownloadOnlyFaceTask().execute($url);
			}else{ //있으면 경로전달
				String path = getFilePath($url);
				if(SharedActivity.getinstance() != null) {
					SharedActivity.getinstance().UploadFacebook(path);
				}
//				if(PopupLinkButton.getinstance() != null){
//					PopupLinkButton.getinstance().UploadFacebook(path);
//				}
			}
	}

	// 파일이 있는지 검사
	private boolean checkFile(String $url) {
		File kFile = new File(convertUrlToFileName($url));
		return kFile.canRead();
	}
	
	private String getFilePath(String $url){
		File kFile = new File(convertUrlToFileName($url));
		return kFile.getAbsolutePath();
	}

	// url에서 / : . 을 _로 변경
	private String convertUrlToFileName(String $url) {
		String kResult = $url;

		kResult = kResult.replaceAll("\\/", "_");
		kResult = kResult.replaceAll("\\:", "_");
		kResult = kResult.replaceAll("\\.", "_");
		kResult = _context.getCacheDir() + "/" + kResult + ".jpg";
		return kResult;
	}

	// 이미지 가져오기
	private void loadFile(ImageView $imageView) {
		String kName = convertUrlToFileName($imageView.getTag().toString());
		Log.d("TAG", kName);
		$imageView.setImageDrawable(Drawable.createFromPath(kName));
	}

	 public void saveRemoteFile(InputStream is, OutputStream os) throws IOException    
	    {
	        int c = 0;
	        while((c = is.read()) != -1)
	            os.write(c);
	        os.flush();    
	    }  
		public void StrickModePolicy() {
			int nSDKVersion = Integer.parseInt(Build.VERSION.SDK);
			if (nSDKVersion > 10) // 2.1?�꾨릭
			{
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}
		}
		public  String StringEncoding(String value){
			String encoding = null;
			try {
				 encoding = java.net.URLEncoder.encode(new String(value.getBytes("UTF-8"))); 
			} catch (UnsupportedEncodingException e) {
				WriteFileLog.writeException(e);
			}
			return encoding;
		}
	// 이미지 다운받기
	private String asyncImageDownload(String $url) {
		/*try {
			StrickModePolicy();
			 InputStream inputStream = new URL($url).openStream();
	         
	         File file = new File(convertUrlToFileName($url.toString()));
	         OutputStream out = new FileOutputStream(file);
	         saveRemoteFile(inputStream, out);
	         out.close();
	         inputStream.close();
	     	kResult = file.toString();
		} catch (Exception e) {
			WriteFileLog.writeException(e);
			// TODO: handle exception
		}
		*/
         
		String kResult = null;
		try {
			URL kUrl = new URL($url);

			HttpURLConnection kConn = (HttpURLConnection) kUrl.openConnection();
			kConn.connect();
			InputStream kIs = kConn.getInputStream();

			byte[] buffer = new byte[1024];
			int bufferLength = 0;

			File kFile = new File(convertUrlToFileName(kUrl.toString()));
			FileOutputStream kFileOutput = new FileOutputStream(kFile);

			while ((bufferLength = kIs.read(buffer)) > 0) {
				kFileOutput.write(buffer, 0, bufferLength);
			}
			kFileOutput.close();
			kConn.disconnect();
			kIs.close();

			kResult = kFile.toString();
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}

		return kResult;
	}


	// 다운로드만 하는 용도
	private class ImageDownloadOnlyFaceTask extends
			AsyncTask<String, Integer, String> {
		
		@Override
		protected void onPreExecute() { //시작
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(String result) { //종료
			super.onPostExecute(result);
			
//			if(PopupLinkButton.getinstance() != null){
//				PopupLinkButton.getinstance().UploadFacebook(result);
//			}
			if(SharedActivity.getinstance() != null){
				SharedActivity.getinstance().UploadFacebook(result);
			}
			
		}
		@Override
		protected String doInBackground(String... urls) {
			return asyncImageDownload(urls[0]);
		}
	}
	
	// 다운로드만 하는 용도
	private class ImageDownloadOnlyTask extends
			AsyncTask<String, Integer, String> {
		
		@Override
		protected void onPreExecute() { //시작
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(String result) { //종료
			super.onPostExecute(result);
			
			if(PopupLinkButton.getinstance() != null){
				PopupLinkButton.getinstance().UploadImageKakao(result);
			} 
			
		}
		@Override
		protected String doInBackground(String... urls) {
			return asyncImageDownload(urls[0]);
		}
	}
}
