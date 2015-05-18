package com.ylq.model;

public class AccountNative 
{
	private String mid,mcookie;
	public AccountNative(String id,String cookie)
	{
		this.mid = id;
		this.mcookie = cookie;
	}
	public String GetId() {
		return this.mid;
	}
	public String GetCookie() {
		return this.mcookie;
	}
}
