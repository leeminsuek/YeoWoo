package com.foxrainbxm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.MediaController;
import android.widget.VideoView;

import com.foxrainbxm.base.BaseActivity;

public class TestVideoView extends BaseActivity {

	CVideoView VideoView;
	ProgressDialog pDialog;
	int count = 1;
	 int duration ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testvideo);

		init();
	}
	public void ProgressBarShow(String msg){
		pDialog = new ProgressDialog(TestVideoView.this);
		// Set progressbar title
		// Set progressbar message
		pDialog.setMessage(msg);
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		// Show progressbar
		pDialog.show();
	}
	
	private void init() {
		Intent intent = getIntent();
		String videourl = intent.getStringExtra("vdourl");
		VideoView = (CVideoView) findViewById(R.id.VideoView);
		// Execute StreamVideo AsyncTask
		// Create a progressbar
		ProgressBarShow("연결중...");
	
		try {
			// Start the MediaController
			MediaController mediacontroller = new MediaController(
					TestVideoView.this);
			mediacontroller.setAnchorView(VideoView);
			// Get the URL from String VideoURL
			Uri video = Uri.parse(videourl);
			VideoView.setMediaController(mediacontroller);
			VideoView.setVideoURI(video);

		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			WriteFileLog.writeException(e);
		}

		VideoView.requestFocus();
		VideoView.setOnPreparedListener(new OnPreparedListener() {
			// Close the progress bar and play the video
			public void onPrepared(MediaPlayer mp) {
				pDialog.dismiss();
				duration = mp.getDuration() / 1000;
				Log.e("onPrepared", "onPrepared = " + mp.getDuration());
				mp.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
					@Override
					public void onBufferingUpdate(MediaPlayer mp, int percent) {
						if(percent < 100){
							if(percent > ((100/duration) * count)){
								pDialog.dismiss();
								mp.start();
								count ++;
							}else{
								ProgressBarShow("버퍼링...." + String.valueOf(percent) + "%");
								mp.pause();	
							}
						}else{
							//mp.stop();
						}
						
						//Log.e("onBufferingUpdate", "onBufferingUpdate = " + percent);
						//Log.e("onBufferingUpdate", "onBufferingUpdate = " + mp.isPlaying());
						
					}
				});
				mp.setOnPreparedListener(new OnPreparedListener() {
					@Override
					public void onPrepared(MediaPlayer mp) {
						// TODO Auto-generated method stub
						Log.e("onPrepared", "onPrepared = " );
					}
				});
				mp.setOnCompletionListener(new OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer mp) {
						// TODO Auto-generated method stub
						finish();
						Log.e("onCompletion", "onCompletion = " );
					}
				});
				mp.start();
			}
		});

	}
}
