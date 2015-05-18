package com.ylq.sign;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetServerTimeListener;
import cn.bmob.v3.listener.SaveListener;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.ylq.model.BuyHistory;
import com.ylq.model.User;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UserinfoActivity extends Activity
{
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userinfo);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		BootstrapCircleThumbnail head_img = (BootstrapCircleThumbnail)findViewById(R.id.head_img);
		TextView tv_user_name = (TextView)findViewById(R.id.tv_user_name);
		TextView tv_score     = (TextView)findViewById(R.id.tv_score);
		final TextView time_last    = (TextView)findViewById(R.id.time_last);
		final TextView buy_history = (TextView)findViewById(R.id.buy_history);
		
		User c_User = User.getCurrentUser(this, User.class);
		
		if (c_User != null) 
		{
			if (c_User.getUsername() != null) 
			{
				tv_user_name.setText(c_User.getUsername());
			}
			tv_score.setText(""+c_User.getScore());
			
			BmobQuery<BuyHistory> buyquert = new BmobQuery<BuyHistory>();
			buyquert.include("buyitem");
			buyquert.order("-createdAt");
			buyquert.setLimit(50);
			buyquert.addWhereEqualTo("user", c_User);
			final StringBuilder sb = new StringBuilder();
			sb.append("购买记录:\n");
			buyquert.findObjects(UserinfoActivity.this, new FindListener<BuyHistory>() {

				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub
					Log.d("bmob", arg1);
				}

				@Override
				public void onSuccess(List<BuyHistory> arg0) {
					// TODO Auto-generated method stub
					if (arg0 != null && arg0.size() > 0)
					{
						for (int i = 0; i < arg0.size(); i++) 
						{
							sb.append(LongTimeToString(arg0.get(i).getTime())+"  ");
							sb.append(arg0.get(i).getBuyitem().getName()+":");
							if (arg0.get(i).getHashpay() == true) 
							{
								sb.append("已经兑换成功!\n");
							}
							else {
								sb.append("待核审...\n");
							}
						}
						
						buy_history.setText(sb.toString());
					}
				}
			});
			
			Bmob.getServerTime(UserinfoActivity.this, new GetServerTimeListener() {
			    @Override
			    public void onSuccess(long time) {
			        // TODO Auto-generated method stub
			        SimpleDateFormat formatter = new SimpleDateFormat(
			                "yyyy-MM-dd HH:mm:ss");
			        String times = formatter.format(new Date(time * 1000L));
			        Log.d("bmob","当前服务器时间为:" + times);
			        final User c_temp = User.getCurrentUser(UserinfoActivity.this,User.class);
			        if (c_temp.getAdtime() > time * 1000) 
			        {
			        	Log.d("bmob", "c_temp:"+c_temp.getAdtime());
			        	time_last.setText("去广告还剩余:"+((c_temp.getAdtime() - time * 1000)/1000/3600/24)+"天");
					}
			        else {
			        	time_last.setText("去广告过期或者未购买");
					}
			    }

				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		
		
		BootstrapButton bt_un_sign = (BootstrapButton)findViewById(R.id.bt_un_sign);
		bt_un_sign.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					User.logOut(UserinfoActivity.this);
					toast("成功登出!!!");
					finish();
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		});
		
		

	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:             
                    finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    } 
    private void toast(String s)
    {
    	Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
    
    
    @SuppressLint("SimpleDateFormat")
	private String LongTimeToString(long time)
    {
	    SimpleDateFormat sdf= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	  //前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
	    Date dt = new Date(time);  
	    return sdf.format(dt);  //得到精确到秒的表示：08/31/2006 21:08:00
    }
}
