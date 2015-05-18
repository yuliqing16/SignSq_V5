package com.ylq.model;

import cn.bmob.v3.BmobObject;

public class BuyHistory extends BmobObject{
	private User user;
	private ShopItem buyitem;
	private Boolean hashpay;
	private long time;
	private String buyinfo;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public ShopItem getBuyitem() {
		return buyitem;
	}
	public void setBuyitem(ShopItem buyitem) {
		this.buyitem = buyitem;
	}
	public Boolean getHashpay() {
		return hashpay;
	}
	public void setHashpay(Boolean hashpay) {
		this.hashpay = hashpay;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getBuyinfo() {
		return buyinfo;
	}
	public void setBuyinfo(String buyinfo) {
		this.buyinfo = buyinfo;
	}
	
}
