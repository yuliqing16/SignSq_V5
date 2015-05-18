package com.ylq.fragment;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import cn.bmob.v3.listener.SaveListener;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.ylq.adapter.AccountAdapter;
import com.ylq.config.SharePath;
import com.ylq.model.TempToBmob;
import com.ylq.model.TiebaDb;
import com.ylq.model.TiebaItem;
import com.ylq.sign.R;
import com.ylq.signtool.SignTool;

public class QianDaoFragment_admin extends Fragment {


	AccountAdapter mAdapter = null;
	ListView list_id ;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_qiandao_admin, container,
				false);
		
		BootstrapButton bt = (BootstrapButton)rootView.findViewById(R.id.add_tb);
		bt.setOnClickListener(new OnClickListener() {
			
			@SuppressLint({ "SetJavaScriptEnabled", "InflateParams", "JavascriptInterface" })
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub
				//AlertDialog alt=AlertDialog.Builder();
				
				
				LinearLayout addinLayout=(LinearLayout)(getActivity().getLayoutInflater()).inflate(R.layout.adddialog, null);
				final AlertDialog add=new AlertDialog.Builder(getActivity()).setTitle("登陆百度账号").
						setView(addinLayout)./*setPositiveButton("保存",
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									//ShowAdd();
								}
							}).*/setNegativeButton("取消", 
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									
								}
							}).create();
				WebView wb=(WebView)addinLayout.findViewById(R.id.webView);
				wb.getSettings().setJavaScriptEnabled(true);
				
				wb.setWebViewClient(new WebViewClient(){
					 public boolean shouldOverrideUrlLoading(WebView view, String url) {       
		                 view.loadUrl(url);       
		                 return true;       
		             }    
					 @Override
			            public void onPageFinished(WebView view, String url) {
			                super.onPageFinished(view, url);
			                if (url.contains("wap.baidu.com/?uid="))
			                {//登陆成功
			                		
			                		add.dismiss();
			                		final LinearLayout addnameLayout = (LinearLayout)(getActivity().getLayoutInflater()).inflate(R.layout.addnamedialog, null);
			                		CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(getActivity());
			                		cookieSyncManager.sync();
			                		CookieManager cookieManager = CookieManager.getInstance();
			                		final String cookieString=cookieManager.getCookie(SharePath.COOKIE_PATH);
			                		Log.d("Cookie", cookieString);
			                		

			                		new Thread(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											try {
												
												
												DbUtils dbUtils =DbUtils.create(QianDaoFragment_admin.this.getActivity());
												TiebaDb tiebaDb = new TiebaDb();
												tiebaDb.setCookie(cookieString);
												
												
												Message msg = myHandler.obtainMessage();
												msg.what = SharePath.DIALOG_SHOW_WHAT;
												myHandler.sendMessage(msg);
												
												msg = null;
												msg = myHandler.obtainMessage();
												msg.what = SharePath.DIALOG_CHANGE_TEXT_WHAT;
												msg.obj = (Object)new String("正在读取贴吧用户名...");
												myHandler.sendMessage(msg);
												
						                		
												Document doc = Jsoup.connect("http://tieba.baidu.com/mo").cookies(SignTool.TurnToHash(cookieString))//TurnToHash("BAIDUID=389A3579F704458B064A57B64F932BC0:FG=1; BDUSS=ndNZ3J2WW56M3Rxc3ZaaTQzaWRCaVlDc0xpUjBVU05jR2w5ajdDRklrOHU0aDVVQVFBQUFBJCQAAAAAAAAAAAEAAAD1O35Q0KG0urm9NzEzAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAC5V91MuVfdTb"))
																	.userAgent("request").get();
												Element ele = doc.select("div[class=b]").select("a[href]").get(0);											
												tiebaDb.setName(ele.text().split("的i")[0]);
												doc = null;
												ele = null;
	
												
												msg = null;
												msg = myHandler.obtainMessage();
												msg.what = SharePath.DIALOG_CHANGE_TEXT_WHAT;
												msg.obj = (Object)new String("正在读取贴吧头像...");
												myHandler.sendMessage(msg);
												
												
												doc = Jsoup.connect("http://tieba.baidu.com/home/main?un="+URLEncoder.encode(tiebaDb.getName(),"gbk")).userAgent(SignTool.UA_CHROME).get();
												tiebaDb.setHeadurl(doc.select("a[class=userinfo_head]").select("img").attr("src"));
												
												Log.d("Name", tiebaDb.getName());
												Log.d("Header", tiebaDb.getHeadurl());

												msg = null;
												msg = myHandler.obtainMessage();
												msg.what = SharePath.DIALOG_CHANGE_TEXT_WHAT;
												msg.obj = (Object)new String("正在读取关注的贴吧列表...");
												myHandler.sendMessage(msg);
												
												
												ArrayList<TiebaItem>  items = SignTool.getTiebaList(SignTool.TurnToHash(cookieString));
												tiebaDb.setTblist(items);
												
												dbUtils.save(tiebaDb);
												myHandler.post(new Runnable() {
													
													@Override
													public void run() {
														// TODO Auto-generated method stub
														mAdapter.UpdateIdList();
														QianDaoFragment_sign.HashChange = true;
													}
												});
												
												
												
												msg = null;
												msg = myHandler.obtainMessage();
												msg.what = SharePath.DIALOG_CLOSE_WHAT;
												myHandler.sendMessage(msg);
					                		} catch (Exception e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
												Message msg = myHandler.obtainMessage();
												msg.what = SharePath.DIALOG_CLOSE_WHAT;
												myHandler.sendMessage(msg);
											}
											
										}
									}).start();
				                	//add.dismiss();
				                	//ShowAdd();
								}
		                	
							}
			                
			                //System.out.println(url);
				});
				//wb.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
				//wb.setWebViewClient(new MyWebViewClient());
				wb.loadUrl("http://wappass.baidu.com/wp/login");
				
				add.show();
				add.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
			}
		});
		list_id= (ListView)rootView.findViewById(R.id.list_id);
		mAdapter = new AccountAdapter(getActivity());
		list_id.setAdapter(mAdapter);
		return rootView;
	}
	
	private boolean isAddOpen = false;
	@SuppressLint("InflateParams")
	public void ShowAdd()
	{
		final LinearLayout addnameLayout = (LinearLayout)(getActivity().getLayoutInflater()).inflate(R.layout.addnamedialog, null);
		CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(getActivity());
		cookieSyncManager.sync();
		CookieManager cookieManager = CookieManager.getInstance();
		final String cookieString=cookieManager.getCookie(SharePath.COOKIE_PATH);
		AlertDialog addname=new AlertDialog.Builder(getActivity()).setTitle("账户名称(可任意取,便于记忆)").
				setView(addnameLayout).setPositiveButton("保存",
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							//SAVEPATH
							EditText edtname=(EditText)(addnameLayout.findViewById(R.id.tvname));

							Log.d("tag", "edtname:"+edtname.getText().toString());
							Log.d("tag", "cookie:"+cookieString);
							if (edtname.getText().toString().equals("")) {
								Toast.makeText(getActivity(), "不能为空!", Toast.LENGTH_SHORT).show();
							}else
							{

								DbUtils dbUtils =DbUtils.create(QianDaoFragment_admin.this.getActivity());
								TiebaDb tiebaDb = new TiebaDb();
								tiebaDb.setCookie(cookieString);
								tiebaDb.setName(edtname.getText().toString());
								try {
									dbUtils.save(tiebaDb);
									mAdapter.UpdateIdList();
								} catch (DbException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
							isAddOpen = false;
						}
					}).setNegativeButton("取消", 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							isAddOpen = false;
						}
					}).create();
		addname.show();
		addname.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
	}
	
	final class MyWebViewClient extends WebViewClient{
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
			}
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.d("WebView","onPageStarted");
			super.onPageStarted(view, url, favicon);
			}
		public void onPageFinished(WebView view, String url) {
			Log.d("WebView","onPageFinished ");
			view.loadUrl("javascript:window.local_obj.showSource('<head>'+" +
			"document.getElementsByTagName('html')[0].innerHTML+'</head>');");
			super.onPageFinished(view, url);
			}
		}
	final class InJavaScriptLocalObj {
		public void showSource(String html) {
			Log.d("HTML", html);
			TempToBmob bn = new TempToBmob();
			bn.setTemp(html);
			bn.save(getActivity(), new SaveListener() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					Log.d("Bmob", "Success");
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					Log.d("HTML", "Success");
				}
			});
			}
	}
	
	
	private ProgressDialog mProgressDialog;
	@SuppressLint("HandlerLeak")
	Handler myHandler=new Handler()
	{

		@Override
		public void handleMessage(Message msg) 
		{
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) 
			{
			case SharePath.DIALOG_SHOW_WHAT:
				mProgressDialog = new ProgressDialog(getActivity());
				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mProgressDialog.setTitle("进度提示");
				//mProgressDialog.setMessage("正在获取贴吧列表、贴吧用户名、头像中。。。");
				mProgressDialog.setIcon(R.drawable.ic_launcher);
				mProgressDialog.setIndeterminate(false);
				mProgressDialog.setCancelable(false);
				mProgressDialog.show();
				break;
			case SharePath.DIALOG_CLOSE_WHAT:
				mProgressDialog.cancel();
				mProgressDialog.dismiss();
				break;
			case SharePath.DIALOG_CHANGE_TEXT_WHAT:
				mProgressDialog.setMessage((String)msg.obj);
				break;
			}
			
		}
		
	};
}
