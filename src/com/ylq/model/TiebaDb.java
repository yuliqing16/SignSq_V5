package com.ylq.model;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

@Table(name = "tiebaDb")
public class TiebaDb {
    //@Id // 如果主键没有命名名为id或_id的时，需要为主键添加此注解
    //@NoAutoIncrement // int,long类型的id默认自增，不想使用自增时添加此注解
    private int id;

	@Column(column = "cookie")
	private String cookie;
	

    @Column(column = "tblist")
    private String tblist;
    
    @Column(column = "signtime")
    private long signtime;
    
    @Column(column = "headurl")
    private String headurl;
    
    @Column(column = "name")
    private String name;
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHeadurl() {
		return headurl;
	}

	public void setHeadurl(String headurl) {
		this.headurl = headurl;
	}

	public long getSigntime() {
		return signtime;
	}

	public void setSigntime(long signtime) {
		this.signtime = signtime;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}


	public String getTblist() {
		
		//this.tblist = new Gson().toJson(getTiebaItemList());
		return tblist;
	}

	public void setTblist(String tblist) {
		this.tblist = tblist;
	}
	public void setTblist(ArrayList<TiebaItem> item) {
		this.tblist = new Gson().toJson(item);
	}
	
	@Transient
    public ArrayList<TiebaItem> tbs=null;
	public ArrayList<TiebaItem> getTiebaItemList()
	{
		if (tbs == null) {
			Gson gson = new Gson();
			tbs=gson.fromJson(tblist, new TypeToken<ArrayList<TiebaItem>>() {}.getType());
			return tbs;
		}else {
			return tbs;
		}
	}
	public void setTbListItem(TiebaItem item,int index)
	{
		TiebaItem itemdest = getTiebaItemList().get(index);
		itemdest.setLevel(item.getLevel());
		itemdest.setScore(item.getScore());
		itemdest.setScoreadd(item.getScoreadd());
		itemdest.setSuccess(item.isSuccess());
		itemdest.setTbname(item.getTbname());
	}
	
	
}
