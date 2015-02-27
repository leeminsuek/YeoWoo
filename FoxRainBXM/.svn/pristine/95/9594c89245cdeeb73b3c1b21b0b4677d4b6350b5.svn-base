package com.foxrainbxm.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.brainyxlib.image.BrImageUtilManager;
import com.foxrainbxm.R;

public class ImageCache {

	public static interface ImageCallback {
		void onImageLoaded(Drawable image, String url, String path);
	}

	protected static ImageCache instance = null;

	public static ImageCache getInstance() {

		synchronized (java.lang.Object.class) {
			if (instance == null) {
				instance = new ImageCache();
			}
		}

		return instance;
	}

	private static final long CACHE_TIMEOUT = 1000 * 60;
	private final Object _lock = new Object();
	protected HashMap<String, WeakReference<Drawable>> cache;
	protected HashMap<String, List<ImageCallback>> callBacks;

	private ImageCache() {
		cache = new HashMap<String, WeakReference<Drawable>>();
		callBacks = new HashMap<String, List<ImageCallback>>();
	}

	private String getHash(String url) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(url.getBytes());
			return new BigInteger(digest.digest()).toString(16);
		} catch (NoSuchAlgorithmException ex) {
			return url;
		}
	}
	
	private Drawable drawableFromCache(String hash) {
		Drawable d = null;
		synchronized (_lock) {
			if (cache.containsKey(hash)) {
				WeakReference<Drawable> ref = cache.get(hash);
				if (ref != null) {
					d = ref.get();
					if (d == null)
						cache.remove(hash);
				}
			}
		}
		return d;
	}

	private Drawable loadSync(String url, String hash, Context context) {
		Drawable d = null;
		try {
			d = drawableFromCache(hash);

			File f = new File(context.getCacheDir(), hash);
			boolean timeout = f.lastModified() + CACHE_TIMEOUT < new Date().getTime();

			if (d == null || timeout) {
				if (timeout)
					f.delete();

				if (!f.exists()) {
					InputStream is = new URL(url).openConnection().getInputStream();
					if (f.createNewFile()) {
						FileOutputStream fo = new FileOutputStream(f);
						byte[] buffer = new byte[1024];
						int size = is.read(buffer);
						while (size > 0) {
							fo.write(buffer, 0, size);
							size = is.read(buffer);
						}
						fo.flush();
						fo.close();
					}
				}
				d = Drawable.createFromPath(f.getAbsolutePath());

				synchronized (_lock) {
					cache.put(hash, new WeakReference<Drawable>(d));
				}
			}
		} catch (IOException ex) {
			Log.e("Exception", "Exception Occur");
		}
		return d;
	}

	public void loadAsync(String tempUrl, final ImageCallback callback,
			final Context context) {
		final String url = tempUrl.replace(" ", "%20");
		
		if (url == null)
			return;
		final String hash = getHash(url);

		synchronized (_lock) {
			List<ImageCallback> callbacks = callBacks.get(hash);
			if (callbacks != null) {
				if (callback != null)
					callbacks.add(callback);
				return;
			}

			callbacks = new ArrayList<ImageCallback>();
			if (callback != null)
				callbacks.add(callback);
			callBacks.put(hash, callbacks);
		}

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				List<ImageCallback> callbacks;
				Drawable d = (Drawable) msg.obj;
				synchronized (_lock) {
					callbacks = callBacks.remove(hash);
				}

				for (ImageCallback iter : callbacks) {
					iter.onImageLoaded(d, url, context.getCacheDir() + "/" + hash);
				}
			}

		};
		new Thread(new Runnable() {
			@Override
			public void run() {
				Drawable d = loadSync(url, hash, context);
				if (callback != null) {
					Message msg = Message.obtain();
					msg.obj = d;
					handler.sendMessage(msg);
				}
			}
		}, "ImageCache loader: " + url).start();
	}
	
	public void loadAsyncView(String tempUrl, final ImageCallback callback,
			final Context context,final View v) {
		final String url = tempUrl.replace(" ", "%20");
		
		if (url == null)
			return;
		final String hash = getHash(url);
		
		synchronized (_lock) {
			List<ImageCallback> callbacks = callBacks.get(hash);
			if (callbacks != null) {
				if (callback != null)
					callbacks.add(callback);
				return;
			}
			
			callbacks = new ArrayList<ImageCallback>();
			if (callback != null)
				callbacks.add(callback);
			callBacks.put(hash, callbacks);
		}
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				List<ImageCallback> callbacks;
				Drawable d = (Drawable) msg.obj;
				synchronized (_lock) {
					callbacks = callBacks.remove(hash);
				}
				for (ImageCallback iter : callbacks) {
					iter.onImageLoaded(d, url, context.getCacheDir() + "/" + hash);
				}
				v.setBackgroundDrawable(d);
			}

		};
		new Thread(new Runnable() {
			@Override
			public void run() {
				Drawable d = loadSync(url, hash, context);
				Message msg = Message.obtain();
				msg.obj = d;
				handler.sendMessage(msg);
			}
		}, "ImageCache loader: " + url).start();
	}
	
	/**
	 * ImageView사이즈에 맞게 이미지 축소
	 * @param imageView
	 * @param bitmap
	 */
	public static void setImageResizeImageView(ImageView imageView, Bitmap bitmap,int width, int height){
		
		if (bitmap == null || width <= 0 || height <= 0) {
			return;
		}
		bitmap = BrImageUtilManager.getInstance().transform(imageView.getImageMatrix(), bitmap, width, height, false);
		imageView.setImageBitmap(bitmap);
	}
	/**
	 * ImageView사이즈에 맞게 이미지 축소
	 * @param imageView
	 * @param bitmap
	 */
	public static void setImageFitImageView(ImageView imageView, Bitmap bitmap){
		int width = imageView.getMeasuredWidth();
		int height = imageView.getMeasuredHeight();
		
		if (bitmap == null || width <= 0 || height <= 0) {
			return;
		}
		bitmap = BrImageUtilManager.getInstance().transform(imageView.getImageMatrix(), bitmap, width, height, false);
		imageView.setImageBitmap(bitmap);
	}
	/**
	 * ImageView사이즈에 맞게 이미지 축소
	 * @param imageView
	 * @param drawable
	 */
	public static void setImageFitImageView(ImageView imageView, Drawable drawable){
		setImageFitImageView(imageView, chgDrawableToBitmap(drawable));
	}
	
	/**
	 * Drawable > bitmap
	 * @param d
	 * @return
	 */
	public static Bitmap chgDrawableToBitmap(Drawable d){
		if (d == null) {
			return null;
		}
		return ((BitmapDrawable)d).getBitmap();
	}
}