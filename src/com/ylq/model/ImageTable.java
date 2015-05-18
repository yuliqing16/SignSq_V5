package com.ylq.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class ImageTable extends BmobObject{
	private String path;
	private BmobFile file;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public BmobFile getFile() {
		return file;
	}
	public void setFile(BmobFile file) {
		this.file = file;
	}
	
}
