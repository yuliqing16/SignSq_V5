package com.ylq.model;

import android.R.integer;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

public class UserAdClick extends BmobObject {
	
	private String username;
	private int before_score;
	private int after_score;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getBefore_score() {
		return before_score;
	}
	public void setBefore_score(int before_score) {
		this.before_score = before_score;
	}
	public int getAfter_score() {
		return after_score;
	}
	public void setAfter_score(int after_score) {
		this.after_score = after_score;
	}
	
}
