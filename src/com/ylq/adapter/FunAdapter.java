package com.ylq.adapter;

import java.util.ArrayList;
import java.util.List;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.ylq.adapter.AccountAdapter.MyViewHolder;
import com.ylq.config.SharePath;
import com.ylq.fragment.QianDaoFragment_sign;
import com.ylq.model.FunDetial;
import com.ylq.model.TiebaDb;
import com.ylq.sign.ImgShowActivity;
import com.ylq.sign.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FunAdapter extends BaseAdapter{

	private LayoutInflater layoutInflater;
	private List<FunDetial> m_list;
	private Context m_Context;
	public FunAdapter(Context mContext, List<FunDetial> mlist)
	{
		layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		m_Context = mContext;
		m_list = mlist;
		if (m_list == null) 
		{
			m_list = new ArrayList<FunDetial>();
		}
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return m_list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return m_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public void addList(List<FunDetial> m_add)
	{
		if (m_list == null) 
		{
			m_list = new ArrayList<FunDetial>();
		}
		m_list.addAll(m_add);
		notifyDataSetChanged();
	}
	public void setList(List<FunDetial> m_new)
	{
		if (m_list == null) 
		{
			m_list = new ArrayList<FunDetial>();
		}
		m_list.clear();
		notifyDataSetChanged();
		m_list.addAll(m_new);
		notifyDataSetChanged();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		MyViewHolder viewHolder;
		Log.d("position", ""+position);
		if (null ==convertView) 
		{
			convertView= layoutInflater.inflate(R.layout.funitem,null);
			viewHolder=new MyViewHolder();
			viewHolder.img_head = (ImageView)convertView.findViewById(R.id.img_head);
			viewHolder.tv_uname = (TextView)convertView.findViewById(R.id.tv_uname);
			viewHolder.tv_time = (TextView)convertView.findViewById(R.id.tv_time);
			viewHolder.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
			viewHolder.tv_info =  (TextView)convertView.findViewById(R.id.tv_info);
			viewHolder.img_show = (ImageView)convertView.findViewById(R.id.img_show);
			
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder=(MyViewHolder)convertView.getTag();
			viewHolder.tv_title.setVisibility(View.GONE);
			viewHolder.tv_info.setVisibility(View.GONE);
			viewHolder.img_show.setVisibility(View.GONE);
		}
		if (m_list.get(position).getPost_user() != null && m_list.get(position).getPost_user().getUsername() !=null) {
			viewHolder.tv_uname.setText(m_list.get(position).getPost_user().getUsername());
		}
		else {
			viewHolder.tv_uname.setText("游客");
		}
		
		viewHolder.tv_time.setText(m_list.get(position).getCreatedAt());
		if (m_list.get(position).getTitle() != null && m_list.get(position).getTitle().length() > 0) 
		{
			viewHolder.tv_title.setVisibility(View.VISIBLE);
			viewHolder.tv_title.setText(m_list.get(position).getTitle());
		}
		if (m_list.get(position).getImg() != null && m_list.get(position).getImg().length() > 2) 
		{
			viewHolder.img_show.setVisibility(View.VISIBLE);
			SharePath.G_IMAGELOADER.displayImage(m_list.get(position).getImg(), viewHolder.img_show, SharePath.IMG_OPTIONS);
			final int pos = position;
			viewHolder.img_show.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(m_Context, ImgShowActivity.class);
					intent.putExtra("img_url", m_list.get(pos).getImg());
					m_Context.startActivity(intent);
				}
			});
		}
		if (m_list.get(position).getInfo() != null && m_list.get(position).getInfo().length() > 0) 
		{
			viewHolder.tv_info.setVisibility(View.VISIBLE);
			viewHolder.tv_info.setText(m_list.get(position).getInfo());
		}
		return convertView;
	}
	
	class MyViewHolder
	{
		public ImageView img_head;
		public TextView  tv_uname;
		public TextView  tv_time;
		public TextView  tv_title;
		public TextView  tv_info;
		public ImageView img_show;
	}
}
