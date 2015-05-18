package com.ylq.adapter;

import java.util.ArrayList;
import java.util.List;

import com.ylq.model.User;
import com.ylq.sign.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BankAdapter extends BaseAdapter{

	private LayoutInflater inflater;
	private List<User> bankInfos;
	public class ViewHolder {
		public TextView index;
		public TextView name;
		public TextView score;
	}

	public BankAdapter(Context context)
	{
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		bankInfos = new ArrayList<User>();
	}
	public void loadData(List<User> items) {
		if (bankInfos == null)
		{
			bankInfos = new ArrayList<User>();
		}
		else {
			bankInfos.clear();
		}
		bankInfos.addAll(items);
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return bankInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return bankInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = new ViewHolder();

		if (convertView == null){
			convertView = inflater.inflate(R.layout.score_item_layout,null);
			viewHolder.name = (TextView) convertView.findViewById(R.id.tvname);
			viewHolder.index = (TextView)convertView.findViewById(R.id.tvid);
			viewHolder.score = (TextView)convertView.findViewById(R.id.tvscore);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		

		viewHolder.index.setText(""+(position+1));
		viewHolder.name.setText(bankInfos.get(position).getUsername()+"");
		viewHolder.score.setText(bankInfos.get(position).getScore()+"");
		
		return convertView;
	}

}
