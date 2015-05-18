package com.ylq.model;

import cn.bmob.v3.BmobUser;

public class User extends BmobUser {
	private String bdid;
	private Integer score=0;
	private long adtime;
	private boolean isshowad=true;
	private String userhead;
	private String iemi;
	public String getBdid() {
		return bdid;
	}
	public void setBdid(String bdid) {
		this.bdid = bdid;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public long getAdtime() {
		return adtime;
	}
	public void setAdtime(long adtime) {
		this.adtime = adtime;
	}
	public boolean GetIsshowad() {
		return isshowad;
	}
	public void setIsshowad(boolean isshowad) {
		this.isshowad = isshowad;
	}
	public String getUserhead() {
		return userhead;
	}
	public void setUserhead(String userhead) {
		this.userhead = userhead;
	}
	public String getIemi() {
		return iemi;
	}
	public void setIemi(String iemi) {
		this.iemi = iemi;
	}
	
	
}
