package com.foxrainbxm.util;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.widget.ImageView;

import com.foxrainbxm.WriteFileLog;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class BrImageCacheManager {

	public final String TAG = "BrUtilManager";
	private static BrImageCacheManager instance = null;
	private DisplayImageOptions options = null;
	public synchronized static BrImageCacheManager getInstance() {
		if (instance == null) {
			synchronized (BrImageCacheManager.class) {
				if (instance == null) {
					instance = new BrImageCacheManager();
				}
			}
		}
		return instance;
	}
	
	public void resume(){
		ImageLoader.getInstance().resume();
	}

	public ImageLoader getImageLoader(){
		return ImageLoader.getInstance();
	}
	public void initImageLoader(Context context) {
		File cacheDir = StorageUtils.getCacheDirectory(context);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.threadPriority(Thread.NORM_PRIORITY - 1)
//				.discCacheExtraOptions(250, 790, CompressFormat.JPEG, 100, null)
				//.discCacheExtraOptions(790, 250, CompressFormat.JPEG, 100, null)
				.denyCacheImageMultipleSizesInMemory()
				.threadPoolSize(3)
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024)
				.discCacheSize(50 * 1024 * 1024)
				.discCacheFileCount(1000)
				.discCache(new UnlimitedDiscCache(cacheDir))
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() 
				.build();
		ImageLoader.getInstance().init(config);
	}

	public DisplayImageOptions initImageLoader(Context context,int defaultImg,int failimg,int mScreenWidth,int mScreenHeight) {
		
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		File cacheDir = StorageUtils.getCacheDirectory(context);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.threadPriority(Thread.NORM_PRIORITY - 1)
				.discCacheExtraOptions(mScreenWidth, mScreenHeight, CompressFormat.JPEG, 30, null)
				.denyCacheImageMultipleSizesInMemory()
				.threadPoolSize(3)
				.memoryCache(new LruMemoryCache(50 * 1024 * 1024))
				.memoryCacheSize(50 * 1024 * 1024)
				.discCacheSize(50 * 1024 * 1024)
				.discCacheFileCount(10000)
				.discCache(new UnlimitedDiscCache(cacheDir))
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
		options = getOptions(defaultImg, failimg);
		return options;
	}
	/**
	 * imageloader init
	 * @param context
	 */
	public DisplayImageOptions ImageLoaderInit(Context context,int defaultImg,int failimg){
		try {
			File cacheDir = StorageUtils.getCacheDirectory(context);
			ImageLoaderConfiguration config = new	 ImageLoaderConfiguration.Builder(context)	 
			//.httpConnectTimeout(100)//10000)		 
			//.httpReadTimeout(20000)		 
			//.maxImageHeightForMemoryCache(150)//720)//800)
			//.maxImageWidthForMemoryCache(150)//480) //800)
			.threadPoolSize(5)		 
			.threadPriority(Thread.NORM_PRIORITY - 2)//Thread.MIN_PRIORITY + 3)	 
			.denyCacheImageMultipleSizesInMemory()			                 
			//.memoryCache(new UsingFreqLimitedMemoryCache(2000000)) // You can pass your own memory cache implementation				
			.discCache(new UnlimitedDiscCache(cacheDir)) // You can pass your own disc cache implementation		 
			.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
			//.enableLogging()
			//.offOutOfMemoryHandling()
		
			//1.5.4 ?? ????. https://github.com/nostra13/Android-Universal-Image-Loader
			//.memoryCache(new WeakMemoryCache())
			.build();
			
			ImageLoader.getInstance().init(config);	
			
			options = getOptions(defaultImg, failimg);
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
		
		return options;
	}
	
	private DisplayImageOptions getOptions(int defaultImg,int failimg){
		  DisplayImageOptions options = new DisplayImageOptions.Builder()
			.imageScaleType(ImageScaleType.EXACTLY)
			.resetViewBeforeLoading(true)
			.cacheInMemory(true)
//			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
			.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).showStubImage(defaultImg).showImageOnFail(failimg)
			.build();
		  return options;
	}	
	
	
	/**
	 * image load Set
	 * @param photoPath
	 * @param v
	 * @param options
	 */
	public void SetImageLoader(String photoPath, ImageView v){
		try {
			if(photoPath.startsWith("http")){
				ImageLoader.getInstance().displayImage(photoPath,v,options);	
			}else{
				String urlRoot = "http://";
				ImageLoader.getInstance().displayImage(urlRoot + photoPath,v,options);
			}
		} catch (Exception e) {
			WriteFileLog.writeException(e);
		}
	}
	
	
}
