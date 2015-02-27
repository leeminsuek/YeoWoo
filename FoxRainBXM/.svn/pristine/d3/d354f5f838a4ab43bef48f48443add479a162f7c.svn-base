package com.foxrainbxm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.VideoView;

import com.foxrainbxm.base.BaseActivity;

public class TestVideo extends BaseActivity implements
MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener,
MediaPlayer.OnBufferingUpdateListener, SurfaceHolder.Callback,
MediaPlayer.OnVideoSizeChangedListener {
	VideoView VideoView;
	ProgressDialog pDialog;
	
	///
	private VodSurfaceView m_Surface = null;
	private SurfaceHolder m_SurfaceHolder = null;
	private MediaPlayer m_MediaPlayer = null;
	String videourl = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testsurface);
		init();
	}
	
	private void init(){
		m_Surface = (VodSurfaceView) findViewById(R.id.vodview);
	//	m_Surface.addTapListener(this.onTap);
		m_SurfaceHolder = this.m_Surface.getHolder();
		m_SurfaceHolder.addCallback(this);
		m_SurfaceHolder.setType(3);
		AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		int iCurrentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		
		if (m_MediaPlayer == null) {
			m_MediaPlayer = new MediaPlayer();
			m_MediaPlayer.setScreenOnWhilePlaying(true);
		} else {
			m_MediaPlayer.stop();
			m_MediaPlayer.reset();
		}
		
		Intent intent =  getIntent();
		videourl  = intent.getStringExtra("vdourl");
		playVideo(videourl);
	}
	
	public void playVideo(String paramString) {
		try {
			if (m_MediaPlayer == null) {
				m_MediaPlayer = new MediaPlayer();
				m_MediaPlayer.setScreenOnWhilePlaying(true);
				//m_MediaPlayer.release();
			} else {
				m_MediaPlayer.stop();
				m_MediaPlayer.reset();
			}
			// isEnd = false;
			//m_MediaPlayer.setDataSource(myapp.savePath + "130322_094812.mp4");
			
			m_MediaPlayer.setDataSource(paramString);
			m_MediaPlayer.setDisplay(m_SurfaceHolder);
			m_MediaPlayer.setAudioStreamType(3);
			m_MediaPlayer.setOnPreparedListener(this);
			m_MediaPlayer.prepareAsync();
			m_MediaPlayer.setOnCompletionListener(this);
			m_MediaPlayer.setOnVideoSizeChangedListener(this);
			m_MediaPlayer.setOnBufferingUpdateListener(this);
			m_MediaPlayer.setOnErrorListener(errorListener);
			//m_MediaPlayer.setOnCompletionListener(completion);
			
		} catch (Throwable localThrowable) {
			localThrowable.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		m_Surface.postDelayed(onEverySecond, 1000L);
	}
	
	private Runnable onEverySecond = new Runnable() {
		public void run() {
		
				//m_Surface.postDelayed(onEverySecond, 1000L);
		}
	};
	
	private MediaPlayer.OnErrorListener errorListener = new MediaPlayer.OnErrorListener() {
		public boolean onError(MediaPlayer paramMediaPlayer, int paramInt1,
				int paramInt2) {
					return false;

		}
	};
	
	@Override
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub
		Log.e("", "");
	}
	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		int i = mp.getVideoWidth();
		int j = mp.getVideoHeight();
		m_SurfaceHolder.setFixedSize(i, j);
		mp.start();
	//	m_iVideoPlayTime = mp.getDuration();
		
	}
	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.e("", "");
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		Log.e("", "");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.e("", "");
	}
	
/*	private void init(){
		Intent intent =  getIntent();
		String videourl  = intent.getStringExtra("vdourl");
		VideoView = (VideoView) findViewById(R.id.VideoView);
	        // Execute StreamVideo AsyncTask
	 
	        // Create a progressbar
	        pDialog = new ProgressDialog(TestVideo.this);
	        // Set progressbar title
	        pDialog.setTitle("Android Video Streaming Tutorial");
	        // Set progressbar message
	        pDialog.setMessage("Buffering...");
	        pDialog.setIndeterminate(false);
	        pDialog.setCancelable(false);
	        // Show progressbar
	        pDialog.show();
	 
	        try {
	            // Start the MediaController
	            MediaController mediacontroller = new MediaController(TestVideo.this);
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
	                VideoView.start();
	            }
	        });
	        
	        
	}*/
}
