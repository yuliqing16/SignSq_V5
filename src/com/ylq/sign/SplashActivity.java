package com.ylq.sign;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.baidu.mobads.AdView;
import com.baidu.mobads.SplashAd;
import com.baidu.mobads.SplashAdListener;
import com.gotye.sdk.GotyeSDK;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.ylq.config.SharePath;
import com.ylq.model.RandonImg;

public class SplashActivity extends Activity {

	ImageView img_show;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		
		Bmob.initialize(this, "e4a78ace4da0e1864669e7f611f12c9f");
		
		initImageLoader(this);  
		
		SharePath.G_IMAGELOADER = ImageLoader.getInstance();  
		SharePath.IMG_OPTIONS = new DisplayImageOptions.Builder()
								        .showStubImage(R.drawable.loading)
								        .showImageForEmptyUri(R.drawable.loading)
								        .showImageOnFail(R.drawable.errorpic)
								        .cacheInMemory(true)
								        .cacheOnDisc(true)
								        .resetViewBeforeLoading()
								        .build();
		
		AdView. setAppSid(this,"c14cf230");//其中的debug需改为您的APP ID
		AdView. setAppSec(this,"c14cf230");//其中的debug需改为您的APP ID
		
		img_show = (ImageView)findViewById(R.id.random_img);
		BmobQuery<RandonImg> img_Query = new BmobQuery<RandonImg>();
		img_Query.order("-createdAt");
		img_Query.findObjects(this, new FindListener<RandonImg>() {
			
			@Override
			public void onSuccess(List<RandonImg> arg0) {
				// TODO Auto-generated method stub
				if (arg0 != null && arg0.size() != 0) 
				{
					SharePath.G_IMAGELOADER.displayImage(arg0.get(0).getImg_url(), img_show, SharePath.IMG_OPTIONS);
				}
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		
		RelativeLayout adsParent=(RelativeLayout)this.findViewById(R.id.adsRl);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(2000);
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							jump();//跳转至您的应用主界面
						}
					});
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		/*
		SplashAd splashAd=new SplashAd(this,adsParent,new SplashAdListener(){
			@Override
			public void onAdDismissed() {
				Log.i("SplashActivity", "onAdDismissed1");

			}
			@Override
			public void onAdFailed(String arg0) {
				Log.i("SplashActivity", "onAdDismissed2");

			}
			@Override
			public void onAdPresent() {
				Log.i("SplashActivity", "onAdDismissed3");
			}
		});*/
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Bundle bundle = new Bundle();
						bundle.putString(GotyeSDK.PRO_APP_KEY, "0512559b-539e-4a52-8c34-f2c8a0375aa8");
						GotyeSDK.getInstance().initSDK(SplashActivity.this, bundle);
					}
				});

			}
		}).start();

	}
	Handler mHandler = new Handler();
	private void jump() {
		this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
		this.finish();
	}
	 /**
     *  Configuration of ImageLoader:
     *  This configuration tuning is custom.
     *  You can tune every option, you may tune some of them,
     *  or you can create default configuration by
     *  ImageLoaderConfiguration.createDefault(this) method.
     *  
     *  Note:
     *  1 enableLogging() // Not necessary in common
     *  2 实际项目中可将其放到Application中
     */
    public static void initImageLoader(Context context) {
    	try {
        	File cacheDir = StorageUtils.getOwnCacheDirectory(context, "qiandao/pic");  
    		ImageLoaderConfiguration config = new ImageLoaderConfiguration
    											.Builder(context)
    									        .threadPriority(Thread.NORM_PRIORITY - 2)
    											.denyCacheImageMultipleSizesInMemory()
    											.discCache(new UnlimitedDiscCache(cacheDir))
    											.discCacheFileNameGenerator(new Md5FileNameGenerator())
    											.tasksProcessingOrder(QueueProcessingType.LIFO)
    											.build();
    		ImageLoader.getInstance().init(config);
		} catch (Exception e) {
			// TODO: handle exception
	        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);  
	        //Initialize ImageLoader with configuration.
	        ImageLoader.getInstance().init(configuration);  

		}

	}
	
}
