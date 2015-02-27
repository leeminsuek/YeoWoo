package com.foxrainbxm.kakao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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
import com.foxrainbxm.base.BaseActivity;
import com.foxrainbxm.base.ResizeImageView;

public class SharedActivity extends BaseActivity{
	public static SharedActivity instance = null;
	public static SharedActivity getinstance(){
		return instance;
	}
	private TextView mSharedContextTxt;
	private Button mBackBtn;
	private EditText mSharedUserTxt;
	private ImageButton 	mSharedBtn;
	private ResizeImageView mSharedImgV;
	ProgressDialog dialog =null;
	private int type;
	String mTitle="";
	String mText="";
	String thumnailurl;
	String videorink;
	private Context mContext;
	String userid;
	ArrayList<String>  mImgurl  = new ArrayList<String>();
	public ArrayList<String>uploadimg = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shared_facebook);
		mContext = this;
		instance = this;
		mBackBtn = (Button) findViewById(R.id.title_back);
		mSharedBtn = (ImageButton) findViewById(R.id.shared_btn);
		mSharedImgV = (ResizeImageView) findViewById(R.id.shared_imgv);
		mSharedContextTxt = (TextView) findViewById(R.id.shared_content);
		mSharedUserTxt = (EditText) findViewById(R.id.shared_user_text);

		Intent intent = getIntent();
		mText = intent.getStringExtra("content");
		type = intent.getIntExtra("type", -1);
		 mTitle = intent.getStringExtra("title");
		 thumnailurl =intent.getStringExtra("thumnail");
		videorink =intent.getStringExtra("videolink");
		userid = intent.getStringExtra("userid");
		mImgurl = (ArrayList<String>) intent.getSerializableExtra("img");
//		mSharedUserTxt.setText(mTitle + "\n\n" + mText);
//		mSharedUserTxt.setSelection(mSharedUserTxt.getText().toString().length());
		mSharedUserTxt.setText("");

//		if(mImgurl.size() == 0) {
//			mSharedImgV.setVisibility(View.GONE);
//		}
//		else {
//			myApp.imageloder.displayImage(mImgurl.get(0), mSharedImgV);
//			mSharedImgV.setVisibility(View.VISIBLE);
//		}
		mSharedBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SendFaceBook();
			}
		});
		
		mBackBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

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
	
	/**
	 * 이미지를 다운후 전송
	 * @param url
	 */
	public void UploadFacebook(String url){
		try {
			if(url != null){
				uploadimg.add(url);
				if(type == 3){
					publishStory(uploadimg);
					return;
				}
				//publishStory(uploadimg);
				if(uploadimg.size() == mImgurl.size()){
					publishStory(uploadimg);
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

	public void SendFaceBook(){
		try {
			switch(type){
			case 1://페이스북 텍스트만
				publishStory(null);
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

	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;
	ArrayList<Bitmap>bitmaparray = new ArrayList<Bitmap>();
	private void publishStory(final ArrayList<String> imgfile) {
		Session session = Session.getActiveSession();
		if (session != null) {
			// Check for publish permissions
			List<String> permissions = session.getPermissions();

			if (!isSubsetOf(PERMISSIONS, permissions)) {
				pendingPublishReauthorization = true;
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(SharedActivity.this, PERMISSIONS);
				session.requestNewPublishPermissions(newPermissionsRequest);

				if(dialog != null){
					dialog.dismiss();
				}
				BrUtilManager.getInstance().showToast(mContext, "권한이 없습니다. 페이스북 계정을 확인해주세요.");
				return;
			}


			Bundle postParams = new Bundle();
			postParams.putString("name", "[여우비]"+"\n"+mTitle+"\n"+ mText+ mSharedUserTxt.getText().toString() + "\n워킹맘들의 커뮤니티");//\n"+"market://details?id=com.foxrainbxm");
			postParams.putString("caption", "워킹맘들의 커뮤니티");
			postParams.putString("description", "[여우비]"+"\n"+mTitle+"\n"+ mText + mSharedUserTxt.getText().toString() + "\n워킹맘들의 커뮤니티\n"+"https://play.google.com/store/apps/details?id=com.foxrainbxm");
			//            postParams.putString("link", "https://play.google.com/store/apps/details?id=com.foxrainbxm");
			postParams.putString("link", "https://play.google.com/store/apps/details?id=com.foxrainbxm");

			postParams.putString("message",  "[여우비]워킹맘들의 커뮤니티");//\n"+"market://details?id=com.foxrainbxm");
			// postParams.putString("picture", "http://imgtest.monkey3.co.kr/get_image.php?type=album&id=185582&w=100");

			/* Bundle postParams = new Bundle();
            postParams.putString("name", "[여우비]"+"\n"+mTitle+"\n"+mText+"\n워킹맘들의 커뮤니티\n"+"market://details?id=com.foxrainbxm");
            postParams.putString("link", "market://details?id=com.foxrainbxm");
            postParams.putString("message", "[여우비]"+"\n"+mTitle+"\n"+mText+"\n워킹맘들의 커뮤니티\n"+"market://details?id=com.foxrainbxm");*/
			//  postParams.putString("caption", "Build great social apps and get more installs.");
			//   postParams.putString("message", "test Msg");
			
			byte[] data = null;
//			ByteArrayBuilder builer  = new ByteArrayBuilder();
//			StringBuilder builder= new StringBuilder();
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			if(imgfile != null){
/*				Bitmap bit = BitmapFactory.decodeFile(imgfile.get(0));
				bit.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				postParams.putByteArray("picture", baos.toByteArray());*/
//				ByteArrayOutputStream bos = new ByteArrayOutputStream();
//				for (int i = 0 ; i < imgfile.size();i++){
//					Bitmap bit = BitmapFactory.decodeFile(imgfile.get(i)); //파일을 비트맵으로
//					bit.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//					bos = CopyArray(baos.toByteArray(), bos);
//					
//					
//					/*ByteArrayInputStream is = new ByteArrayInputStream(baos.toByteArray());
//					builder.append(baos.toByteArray());*/
//					bitmaparray.add(bit);
//				}
//				postParams.putByteArray("picture", bos.toByteArray());
				
				
				//for (int i = 0 ; i < imgfile.size();i++){
				Bitmap bit = BitmapFactory.decodeFile(imgfile.get(0));
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bit.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				data = baos.toByteArray();
				postParams.putByteArray("picture", data);

				
				//}
				//	Bitmap bit = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher);
			}
			Request.Callback callback = new Request.Callback() {
				public void onCompleted(Response response) {
					if (response != null) {
						JSONObject graphResponse = response.getGraphObject().getInnerJSONObject();
						String postId = null;
						try {
							postId = graphResponse.getString("id");
						} catch (JSONException e) {
							WriteFileLog.writeException(e);
						}
						FacebookRequestError error = response.getError();
						if(dialog != null){
							dialog.dismiss();
						}
						if (error != null) {
							BrUtilManager.getInstance().showToast(mContext, "공유가 실패되었습니다.");
						}else{
							BrUtilManager.getInstance().showToast(mContext, "공유되었습니다.");
							deleteTempFiles(imgfile);
							ClearBitmap(bitmaparray);
							finish();
						}
					}else{
						if(dialog != null){
							dialog.dismiss();
						}
						BrUtilManager.getInstance().showToast(mContext, "공유가 실패되었습니다.");
					}
				}
				
				
				
			};
			Request request = null;
			if(imgfile != null){
				request = new Request(session, "me/photos", postParams,HttpMethod.POST, callback);	
			}else{
				request = new Request(session,"me/feed", postParams,HttpMethod.POST, callback);
			}
			RequestAsyncTask task = new RequestAsyncTask(request);
			task.execute();
		}else{
			Log.e("", "");
		}
	}
	
	
	public ByteArrayOutputStream CopyArray(byte[] bitmaparray,ByteArrayOutputStream bos){
		FileInputStream fis = null;
		ByteArrayOutputStream bos2 = null;
		ByteArrayInputStream bis = null;
		try {
			int readcount = 0;
	/*		bos2 = new ByteArrayOutputStream();
			
			byte[] inbuf = new byte[bitmaparray.length];
			for(int i = readcount ; i < bitmaparray.length ; i++){
				bos2.write(inbuf,0,readcount);
			}
			byte[] filearray = bos2.toByteArray();*/
			
			bis = new ByteArrayInputStream(bitmaparray);
			byte[] outbuf = new byte[bitmaparray.length];
			
			for(int i = readcount ; i < bitmaparray.length ; i++){
				bos.write(outbuf,bos.size(),bos.size()+i);
			}
			
	/*		while((readcount = bis.read(outbuf))!= -1){
				bos.write(outbuf,bos.size(),readcount);
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bos;
	}
	
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
}
