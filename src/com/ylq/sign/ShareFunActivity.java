package com.ylq.sign;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.ylq.config.SharePath;
import com.ylq.model.FunDetial;
import com.ylq.model.ImageTable;
import com.ylq.model.User;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ShareFunActivity extends Activity
{
	
	BootstrapEditText edit_title;
	BootstrapEditText edit_info;

	ImageView img_show;
	Button bt_photo_img;
	Button bt_select_img;
	BootstrapButton bt_send;
	
	boolean isUpdate = false;
	FunDetial funDetial;
	
	String mpath;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_share_fun);
    	
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
    	
    	funDetial = new FunDetial();
		edit_title = (BootstrapEditText)findViewById(R.id.edit_title);
		edit_info = (BootstrapEditText)findViewById(R.id.edit_info);
		img_show = (ImageView)findViewById(R.id.img_show2);
		
		bt_photo_img = (Button)findViewById(R.id.bt_photo_img);
		bt_select_img = (Button)findViewById(R.id.bt_select_img);
		
		bt_send = (BootstrapButton)findViewById(R.id.bt_send);
		
		bt_select_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent getImage = new Intent(Intent.ACTION_GET_CONTENT); 
		        getImage.addCategory(Intent.CATEGORY_OPENABLE); 
		        getImage.setType("image/jpeg"); 
		        startActivityForResult(getImage, 1); 
			}
		});
		
		bt_photo_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String name=String.valueOf(System.currentTimeMillis())+".jpg";
				mpath="/mnt/sdcard/"+name;
				Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
				intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(mpath)));
				startActivityForResult(intent, 2);
			}
		});
		
		bt_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (edit_title.getText().toString().length() <= 0 && edit_info.getText().toString().length() <= 0) 
				{
					Toast("你要在标题或者内容中输入一些东东才能提交哦!!!");
					return;
				}
				Toast("正在上传中,请稍等哦...");
				bt_send.setEnabled(false);
				bt_send.setText("正在发布....");
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						while (isUpdate == true) {
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						funDetial.setTitle(edit_title.getText().toString());
						funDetial.setInfo(edit_info.getText().toString());
						funDetial.setPost_user(BmobUser.getCurrentUser(ShareFunActivity.this, User.class));
						funDetial.save(ShareFunActivity.this, new SaveListener() {
							
							@Override
							public void onSuccess() {
								// TODO Auto-generated method stub
								myHandler.post(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										Toast("发布成功!");
										ShareFunActivity.this.finish();
									}
								});
							}
							
							@Override
							public void onFailure(int arg0, String arg1) {
								// TODO Auto-generated method stub
								Toast("发布失败:"+arg1);
								myHandler.post(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										bt_send.setEnabled(true);
										bt_send.setText("发布");
									}
								});

							}
						});
						
					}
				}).start();
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
	      
        if (requestCode == 1) { 
            try { 
            	if(resultCode == RESULT_CANCELED){
           		 // 取消相机操作
           			 return;
           		} 
                //获得图片的uri 
                Uri originalUri = data.getData(); 
                
                SharePath.G_IMAGELOADER.displayImage(originalUri.toString(), img_show);
                //将图片内容解析成字节数组                 

                String[] proj = { MediaStore.Images.Media.DATA };

                Cursor actualimagecursor = managedQuery(originalUri,proj,null,null,null);

                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                actualimagecursor.moveToFirst();

                String img_path = actualimagecursor.getString(actual_image_column_index);

                File file = new File(img_path);        
                isUpdate = true;
                final BmobFile bmobFile = new BmobFile(ImageTable.class, file);
               bmobFile.uploadblock(this, new UploadFileListener() {

                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                    	
                    	final ImageTable img = new ImageTable();
                    	img.setPath(bmobFile.getFileUrl());
                    	img.setFile(bmobFile);
                        //也可以用movie.save(MainActivity.this)的方法插入，不回调
                    	img.save(ShareFunActivity.this, new SaveListener() {

                            @Override
                            public void onSuccess() {
                            	//Toast("上传文件成功.");
                            	funDetial.setImg(bmobFile.getFileUrl());
                            	isUpdate = false;
                            }

                            @Override
                            public void onFailure(int code, String arg0) {
                            	isUpdate = false;
                            }
                        });

                    }

                    @Override
                    public void onProgress(Integer value) {
                        // TODO Auto-generated method stub
                        // 返回的上传进度
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        // TODO Auto-generated method stub
                    	Toast("上传文件失败：" + msg);
                        isUpdate = false;
                    }
                });
 
            } catch (Exception e) { 
                isUpdate = false;
            } 
        }else if(requestCode == 2)
        {
    		if (resultCode == RESULT_OK) 
    		{
    		    	SharePath.G_IMAGELOADER.displayImage(Scheme.FILE.wrap(mpath), img_show);
    		    	final File fidel = new File(mpath);    
	                isUpdate = true;
	                final BmobFile bmobFile = new BmobFile(ImageTable.class, fidel);
	                bmobFile.uploadblock(this, new UploadFileListener() {

	                    @Override
	                    public void onSuccess() {
	                        // TODO Auto-generated method stub
	                    	
	                    	final ImageTable img = new ImageTable();
	                    	img.setPath(bmobFile.getFileUrl());
	                    	img.setFile(bmobFile);
	                        //也可以用movie.save(MainActivity.this)的方法插入，不回调
	                    	img.save(ShareFunActivity.this, new SaveListener() {

	                            @Override
	                            public void onSuccess() {
	                            	//Toast("上传文件成功.");
	                            	funDetial.setImg(bmobFile.getFileUrl());
	                            	isUpdate = false;
	                            	fidel.delete();
	                            }

	                            @Override
	                            public void onFailure(int code, String arg0) {
	                            	isUpdate = false;
	                            	fidel.delete();
	                            }
	                        });

	                    }

	                    @Override
	                    public void onProgress(Integer value) {
	                        // TODO Auto-generated method stub
	                        // 返回的上传进度
	                    }

	                    @Override
	                    public void onFailure(int code, String msg) {
	                        // TODO Auto-generated method stub
	                    	Toast("上传文件失败：" + msg);
	                        isUpdate = false;
	                        fidel.delete();
	                    }
	                });
    		 }
    		 else if(resultCode == RESULT_CANCELED){
    		 // 取消相机操作
    			 return;
    		} 
        }
	}
	
    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isUpdate = false;
	}

	@SuppressWarnings("deprecation")
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:             
                    finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    } 
    public void Toast(String msg)
    {
    	Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    Handler myHandler = new Handler();
}
