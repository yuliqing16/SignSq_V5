package com.ylq.sign;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.polites.android.GestureImageView;
import com.ylq.config.SharePath;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

public class ImgShowActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	setContentView(R.layout.standard_image);
    	String urlString = this.getIntent().getStringExtra("img_url");
    	final GestureImageView image = (GestureImageView)findViewById(R.id.image);
    	//SharePath.G_IMAGELOADER.displayImage(urlString, image, SharePath.IMG_OPTIONS);
    	SharePath.G_IMAGELOADER.loadImage(urlString, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@SuppressLint("NewApi")
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				// TODO Auto-generated method stub

				
				Display mDisplay = getWindowManager().getDefaultDisplay();
				int W = mDisplay.getWidth();
				int H = mDisplay.getHeight();
				Log.d("Length_screen:", "屏幕:"+W+"*"+H);
				Log.d("Length_bitmap:", "图像:"+arg2.getWidth()+"*"+arg2.getHeight());
				Matrix matrix = new Matrix(); 
				matrix.postScale((float)W / (float)arg2.getWidth(), (float)W / (float)arg2.getWidth()); //长和宽放大缩小的比例
				Bitmap bitmap = Bitmap.createBitmap(arg2,0,0,arg2.getWidth(),arg2.getHeight(),matrix,true);
				Log.d("Length_bitmap:", "图像扩展后:"+bitmap.getWidth()+"*"+bitmap.getHeight());
				image.setImageBitmap(bitmap);
				
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	public static int dp2px(Context context, int dp)
	{
	    float scale = context.getResources().getDisplayMetrics().density;
	    return (int) (dp * scale + 0.5f);
	}
}
