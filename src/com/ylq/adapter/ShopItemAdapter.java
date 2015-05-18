package com.ylq.adapter;

import java.util.ArrayList;
import java.util.List;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.ylq.model.ShopItem;
import com.ylq.sign.R;
import com.ylq.sign.SureBuyActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ShopItemAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private List<ShopItem> shopItems;
	private Context mContext;
	public class ViewHolder {
		public TextView name;
		public TextView score;
		public BootstrapButton btBuy;
	}

	public ShopItemAdapter(Context context)
	{
		this.mContext = context;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.shopItems = new ArrayList<ShopItem>();
	}
	public void loadData(List<ShopItem> items) {
		if (shopItems == null) 
		{
			shopItems =  new ArrayList<ShopItem>();
		}
		else {
			shopItems.clear();
		}
		
		shopItems.addAll(items);
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return shopItems.size();
	}

	@Override
	public Object getItem(int position) {
		return shopItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = new ViewHolder();

		if (convertView == null){
			convertView = inflater.inflate(R.layout.shop_list_item,null);
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.score = (TextView)convertView.findViewById(R.id.score);
			viewHolder.btBuy = (BootstrapButton)convertView.findViewById(R.id.btBuy);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.name.setText(shopItems.get(position).getName());
		viewHolder.score.setText(shopItems.get(position).getScore()+"");
		viewHolder.btBuy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, SureBuyActivity.class);
				intent.putExtra("name", shopItems.get(position).getName());
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}
}
