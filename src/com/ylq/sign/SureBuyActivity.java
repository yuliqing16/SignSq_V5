package com.ylq.sign;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.ylq.model.BuyHistory;
import com.ylq.model.ShopItem;
import com.ylq.model.User;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetServerTimeListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class SureBuyActivity extends Activity {
	private ShopItem item = null;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_surebuy);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		final TextView title = (TextView)findViewById(R.id.title);
		final BootstrapEditText edit_in = (BootstrapEditText)findViewById(R.id.edit_in);
		BootstrapButton   btSure = (BootstrapButton)findViewById(R.id.btSure);
		BootstrapButton   btCancle = (BootstrapButton)findViewById(R.id.btCancle);
		
		Intent intent = getIntent();
		String name = intent.getStringExtra("name");
		BmobQuery<ShopItem> query = new BmobQuery<ShopItem>();
		query.addWhereEqualTo("name", name);
		query.findObjects(this, new FindListener<ShopItem>() {
			
			@Override
			public void onSuccess(List<ShopItem> arg0) {
				// TODO Auto-generated method stub
				if (arg0 != null && arg0.size() > 0) 
				{
					item = arg0.get(0);
					if (!arg0.get(0).getTitle().contains(":")) 
					{
						edit_in.setVisibility(View.GONE);
					}
					SureBuyActivity.this.setTitle(arg0.get(0).getName());
					title.setText(arg0.get(0).getTitle());
				}
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		
		btCancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				User user = BmobUser.getCurrentUser(SureBuyActivity.this, User.class);
				Log.d("time", "adtime:"+user.getAdtime());
		        SimpleDateFormat formatter = new SimpleDateFormat(
		                "yyyy-MM-dd HH:mm:ss");
		        String times = formatter.format(new Date(user.getAdtime()));
		        Log.d("time","adtime:" + times);
				SureBuyActivity.this.finish();
			}
		});
		
		btSure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				if (item == null)
					return;
				final User cu_user = BmobUser.getCurrentUser(SureBuyActivity.this, User.class);
				if (cu_user == null) {
					toast("未登录,请登陆后再次操作!");
					return;
				}
				BmobQuery<User> user_new = new BmobQuery<User>();
				user_new.addWhereEqualTo("username", cu_user.getUsername());
				user_new.findObjects(SureBuyActivity.this, new FindListener<User>() {

					@Override
					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(List<User> arg0) {
						// TODO Auto-generated method stub
						if (arg0 != null || arg0.size() > 0) 
						{
							if (arg0.get(0).getScore() < item.getScore()) {
								toast("积分不足!!!");
								return;
							}
							final User uer_new___ = arg0.get(0);
							final BuyHistory buyHistory = new BuyHistory();
							cu_user.setScore(uer_new___.getScore());
							cu_user.setAdtime(uer_new___.getAdtime());
							cu_user.setScore(uer_new___.getScore());
							
							
							buyHistory.setUser(cu_user);
							buyHistory.setBuyitem(item);
							//查询时间
							Bmob.getServerTime(SureBuyActivity.this, new GetServerTimeListener() {
								
								@Override
								public void onSuccess(long arg0) {
									// TODO Auto-generated method stub
									buyHistory.setTime(arg0 * 1000);
									if (item.getTitle().contains(":")) 
									{
										if (edit_in.getText().toString().length() >0) 
										{
											buyHistory.setBuyinfo(edit_in.getText().toString());
											buyHistory.setHashpay(Boolean.FALSE);
											cu_user.setScore(cu_user.getScore() - item.getScore());
										}
										else 
										{
											toast("输入不能为空!!!");
											return;
										}
										
									}
									else 
									{
										//处理去广告
										int time_add = Integer.parseInt(item.getName().replace("去广告", "").replace("天", ""));
										cu_user.setScore(cu_user.getScore() - item.getScore());
										if (cu_user.getAdtime() < arg0 * 1000) 
										{
											long time_now = (long)arg0 * 1000 + (long)24 * 3600 * 1000 * time_add;
											cu_user.setAdtime(time_now);
										}else {
											long time_now = (long)cu_user.getAdtime() + (long)24 * 3600 * 1000 * time_add;
											cu_user.setAdtime(time_now);
										}
										buyHistory.setHashpay(true);
									}
									
									cu_user.update(SureBuyActivity.this, new UpdateListener() {
										
										@Override
										public void onSuccess() {
											// TODO Auto-generated method stub
											buyHistory.save(SureBuyActivity.this, new SaveListener() {
												
												@Override
												public void onSuccess() {
													// TODO Auto-generated method stub
													toast("购买成功!!!");
													
													myHandler.post(new Runnable() {
														
														@Override
														public void run() {
															// TODO Auto-generated method stub
															finish();
															return;
														}
													});
												}
												
												@Override
												public void onFailure(int arg0, String arg1) {
													// TODO Auto-generated method stub
													toast("购买失败:"+arg1);
												}
											});
										}
										
										@Override
										public void onFailure(int arg0, String arg1) {
											// TODO Auto-generated method stub
											toast("购买失败:"+arg1);
										}
									});
								}
								
								@Override
								public void onFailure(int arg0, String arg1) {
									// TODO Auto-generated method stub
									toast("购买失败:"+arg1);
								}
							});
							
							

						}
					}
				});

				
			}
		});
	}
	Handler myHandler = new Handler();
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:             
                    finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    } 
	private void toast(final String msg)
	{
		myHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(SureBuyActivity.this , msg, Toast.LENGTH_SHORT).show();
			}
		});

	}
}
