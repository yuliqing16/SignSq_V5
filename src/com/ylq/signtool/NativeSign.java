package com.ylq.signtool;

import java.net.URLEncoder;
import java.util.Iterator;

import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;


public class NativeSign implements ISign{
	private AsyncHttpClient mClinet_signurl =null; 
	private AsyncHttpClient mClient_sign = null;
	private String mCookie = null;
	
	public String getmCookie() {
		return mCookie;
	}

	public NativeSign(String cookie)
	{
		this.mClinet_signurl = new AsyncHttpClient();
		this.mClient_sign = new AsyncHttpClient();
		this.mCookie = cookie;
		this.mClinet_signurl.addHeader("cookie", this.mCookie);
		this.mClinet_signurl.addHeader("Referer", "http://wapp.baidu.com/");
		this.mClinet_signurl.addHeader("Connection", "keep-alive");
		this.mClinet_signurl.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0");
		
		this.mClient_sign.addHeader("User-Agent","User-Agent:Mozilla/5.0 (Linux; U; Android 2.3.4; zh-cn; W806 Build/GRJ22) AppleWebKit/530.17 (KHTML, like Gecko) FlyFlow/2.4 Version/4.0 Mobile Safari/530.17 baidubrowser/042_1.8.4.2_diordna_008_084/AIDIVN_01_4.3.2_608W/1000591a/9B673AC85965A58761CF435A48076629%7C880249110567268/1");
		this.mClient_sign.addHeader("cookie", this.mCookie);
	}

	@Override
	public void signAlong(final String cookie, String tb_url, ISignTbCallBack sign) {
		// TODO Auto-generated method stub

		mClinet_signurl.get("http://wapp.baidu.com/f?kw="+URLEncoder.encode(tb_url),null,new AsyncHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub	
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				String string = new String(arg2);
				Document document = Jsoup.parse(string);
				//style=";"[style=text-align:right]
				Elements element = document.select("td[style=text-align:right;]");//.select("a").first();
				for (Iterator iterator = element.iterator(); iterator
						.hasNext();) {
					Element element2 = (Element) iterator.next();
					
				}
				if (element.text().equals("签到")) 
				{


						mClient_sign.get("http://tieba.baidu.com"+element.select("a").attr("href").replace("amp",""),null,new AsyncHttpResponseHandler() {
						
						@Override
						public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
							// TODO Auto-generated method stub
							Log.d("TAg", new String(arg2));
							Log.d("TAg", "签到成功");
						}
						
						@Override
						public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							Log.e("error", new String(arg2));
						}
					});
				}else if (element.text().equals("已签到")) 
				{
					Log.d("tag", "已签到");
				}
				else {
					Log.d("tag", "没有登陆或没关注此贴吧");
				}
			}
		});
	}

	@Override
	public void signAll(String cookie, String tb_url, ISignTbCallBack sign) {
		// TODO Auto-generated method stub
		
	}


}
