package com.ylq.signtool;


public interface ISign 
{
	//void getTbList(IGetTbCallBack tb);
	void signAlong(String cookie,String tb_url,ISignTbCallBack sign);
	void signAll(String cookie,String tb_url,ISignTbCallBack sign);
	
}
