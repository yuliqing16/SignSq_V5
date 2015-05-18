package com.ylq.model;

import cn.bmob.v3.BmobObject;



public class FunDetial extends BmobObject
{
	
	/**
	 * 
	 */
	public String title;
	public String img;
	public String weburl;
	public String download;
	public String info;
	public User post_user;
	public User getPost_user() {
		return post_user;
	}
	public void setPost_user(User post_user) {
		this.post_user = post_user;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getWeburl() {
		return weburl;
	}
	public void setWeburl(String weburl) {
		this.weburl = weburl;
	}
	public String getDownload() {
		return download;
	}
	public void setDownload(String download) {
		this.download = download;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}

}
