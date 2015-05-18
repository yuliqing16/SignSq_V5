package com.ylq.model;

import android.R.integer;
import cn.bmob.v3.BmobObject;

public class CheatControl extends BmobObject{
	private int max_score;
	private boolean is_show;
	private String info;
	
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public int getMax_score() {
		return max_score;
	}
	public void setMax_score(int max_score) {
		this.max_score = max_score;
	}
	public boolean getIs_show() {
		return is_show;
	}
	public void setIs_show(boolean is_show) {
		this.is_show = is_show;
	}
	
}
