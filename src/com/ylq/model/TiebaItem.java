package com.ylq.model;



public class TiebaItem{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String level;
	String tbname;
	String score;
	
	public boolean isSuccess() {
		return success;
	}
	public TiebaItem setSuccess(boolean success) {
		this.success = success;
		return this;
	}
	public int getScoreadd() {
		return scoreadd;
	}
	public TiebaItem setScoreadd(int scoreadd) {
		this.scoreadd = scoreadd;
		return this;
	}
	boolean success;
	int scoreadd;
	public TiebaItem(String tbname,String score,String level)
	{
		this.level = level;
		this.tbname = tbname;
		this.score = score;
		
		success = false;
		scoreadd = 0;
	}
	public String getLevel() {
		return level;
	}
	public TiebaItem setLevel(String level) {
		this.level = level;
		return this;
	}
	public String getTbname() {
		return tbname;
	}
	public TiebaItem setTbname(String tbname) {
		this.tbname = tbname;
		return this;
	}
	public String getScore() {
		return score;
	}
	public TiebaItem setScore(String score) {
		this.score = score;
		return this;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.tbname+"  "+
				this.level+"  "+
				this.score;
	}
	
}
