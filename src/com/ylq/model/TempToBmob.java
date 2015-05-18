package com.ylq.model;

import cn.bmob.v3.BmobObject;

public class TempToBmob extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String temp;
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
}
