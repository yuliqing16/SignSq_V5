package com.ylq.sign;

import cn.bmob.v3.listener.SaveListener;

import com.ylq.model.User;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginRegisterActivity extends Activity {

	public static final int START_PROCESS=0x1;
	public static final int END_PROCESS=0;
	
	private Button btlogin,btreg;
	private EditText username,password;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lgre);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		btlogin = (Button)findViewById(R.id.btlogin);
		btreg = (Button)findViewById(R.id.btreg);
		username = (EditText)findViewById(R.id.username);
		password = (EditText)findViewById(R.id.password);
		
		btreg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (username.getText().toString() != null && username.getText().toString().length() > 0 && 
					password.getText().toString() != null && password.getText().toString().length() > 0)
				{
					User user = new User();
					user.setUsername(username.getText().toString());
					user.setPassword(password.getText().toString());
					user.signUp(LoginRegisterActivity.this, new SaveListener() {
						
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							toast("注册成功!!!");
							User bu2 = new User();
							bu2.setUsername(username.getText().toString());
							bu2.setPassword(password.getText().toString());
							bu2.login(LoginRegisterActivity.this, new SaveListener() {
							    @Override
							    public void onSuccess() {
							        // TODO Auto-generated method stub
							        toast("登录成功:");
							        LoginRegisterActivity.this.finish();
							    }
							    @Override
							    public void onFailure(int code, String msg) {
							        // TODO Auto-generated method stub
							        toast("登录失败:"+msg);
							    }
							});
						}
						
						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							toast("注册失败,"+arg1+"!!!");
						}
					});
				}
			}
		});
		btlogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (username.getText().toString() != null && username.getText().toString().length() > 0 && 
						password.getText().toString() != null && password.getText().toString().length() > 0)
					{
						final User bu2 = new User();
						bu2.setUsername(username.getText().toString());
						bu2.setPassword(password.getText().toString());
						bu2.login(LoginRegisterActivity.this, new SaveListener() {
						    @Override
						    public void onSuccess() {
						        // TODO Auto-generated method stub
						        toast("登录成功,adtime:" + bu2.getAdtime());
						        LoginRegisterActivity.this.finish();
						    }
						    @Override
						    public void onFailure(int code, String msg) {
						        // TODO Auto-generated method stub
						        toast("登录失败:"+msg);
						    }
						});
					}
			}
		});
		
	}
	public void startProgressReg()
	{
		Message msg = mHandler.obtainMessage();
		msg.what = START_PROCESS;
		msg.obj = "正在注册中,请稍后...";
		mHandler.sendMessage(msg);
	}
	public void startProgressLogin()
	{
		Message msg = mHandler.obtainMessage();
		msg.what = START_PROCESS;
		msg.obj = "正在登录中,请稍后...";
		mHandler.sendMessage(msg);
	}
	public void endProgress()
	{
		Message msg = mHandler.obtainMessage();
		msg.what = END_PROCESS;
		mHandler.sendMessage(msg);
	}
	private ProgressDialog mProgressDialog;
	public Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// TODO Auto-generated method stub
				switch (msg.what) 
				{
				case START_PROCESS:
					mProgressDialog = new ProgressDialog(LoginRegisterActivity.this);
					mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					mProgressDialog.setTitle("温馨提示");
					mProgressDialog.setMessage((String)msg.obj);
					mProgressDialog.setIcon(R.drawable.ic_launcher);
					mProgressDialog.setIndeterminate(false);
					mProgressDialog.setCancelable(true);
					mProgressDialog.show();
					
					break;
				case END_PROCESS:
					mProgressDialog.cancel();
					mProgressDialog.dismiss();
					
					break;
				}
			}
		
	};
	
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
    private void toast(String s)
    {
    	Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
