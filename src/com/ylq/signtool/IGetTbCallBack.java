package com.ylq.signtool;

import java.util.List;

import com.ylq.model.TiebaItem;

public interface IGetTbCallBack {
	void Success(List<TiebaItem> tb_list);
	void Failed(String failed);
}
