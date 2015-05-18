package com.ylq.adapter;

import java.util.List;

import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.ylq.config.UiConfig;
import com.ylq.sign.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MenuAdapter extends BaseAdapter{

	private LayoutInflater layoutInflater;
	private Context mContext;
	private List<MenuValueList> mList;
	public MenuAdapter(Context context,List<MenuValueList> list)
	{
		this.mContext = context;
		this.mList = list;
		layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		MyViewHolder viewHolder;
		if (convertView==null)
		{
			convertView= layoutInflater.inflate(R.layout.menu_item, null);
			viewHolder=new MyViewHolder();
			viewHolder.icon=(FontAwesomeText)convertView.findViewById(R.id.icon_tv);
			viewHolder.title=(TextView)convertView.findViewById(R.id.title);
			convertView.setTag(viewHolder);
			switch (position) {
			case UiConfig.FRAGMENT_SOURCE:
				viewHolder.icon.startRotate(this.mContext, true, FontAwesomeText.AnimationSpeed.SLOW);
				break;
			case UiConfig.FRAGMENT_CHART:
				//viewHolder.icon.startFlashing(this.mContext, true, FontAwesomeText.AnimationSpeed.SLOW);
				break;
			case UiConfig.FRAGMENT_JIFEN:		
				break;
			case UiConfig.FRAGMENT_QIANDAO:
				//viewHolder.icon.startFlashing(this.mContext, true, FontAwesomeText.AnimationSpeed.SLOW);
				break;
			default:
				break;

			}
			
		}
		else 
		{
			viewHolder=(MyViewHolder)convertView.getTag();
		}
		viewHolder.icon.setIcon(mList.get(position).getIcon());
		viewHolder.title.setText(mList.get(position).getTitle());
		return convertView;
	}
	class MyViewHolder
	{
		public FontAwesomeText icon;
		public TextView title;
	}
	public static class MenuValueList
	{
		public String getIcon() {
			return mIcon;
		}
		public void setIcon(String mIcon) {
			this.mIcon = mIcon;
		}
		public String getTitle() {
			return mTitle;
		}
		public void setTitle(String mTitle) {
			this.mTitle = mTitle;
		}
		private String mIcon;
		private String mTitle;
		public MenuValueList(String icon,String title)
		{
			this.mIcon = icon;
			this.mTitle = title;
		}
		
	}
}
