package com.ylq.signtool;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class NativeSignHttpHelper {
	public static final String BASE_URL="http://192.168.1.114/";
	private static AsyncHttpClient clinet = new AsyncHttpClient();
	
	public static void get(String url,RequestParams params,AsyncHttpResponseHandler response)
	{
		clinet.get(url, params,response);
	}
	
	public static void post(String url,RequestParams params,AsyncHttpResponseHandler response)
	{
		clinet.post(url, params,response);
	}
	
	public static String getAbsoulteUrl(String relativeUrl)
	{
		return BASE_URL+relativeUrl;
	}
}
