package com.ylq.model;

import cn.bmob.v3.BmobObject;

public class ShopItem extends BmobObject{
	private String name;
	private int score;
	private String title;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	

}
