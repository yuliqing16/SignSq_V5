package com.ylq.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.ylq.config.SharePath;
import com.ylq.fragment.QianDaoFragment_sign;
import com.ylq.model.TiebaDb;
import com.ylq.sign.MainActivity;
import com.ylq.sign.R;

public class AccountAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater layoutInflater;
	private ArrayList<TiebaDb> mlist;
	public AccountAdapter(Context context)
	{
		this.mContext= context;
		mlist=new ArrayList<TiebaDb>();
		layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		/*SharedPreferences dbSharedPreferences = context.getSharedPreferences(
				SharePath.SHARE_REF, Activity.MODE_PRIVATE);
		Map<String,?> map=dbSharedPreferences.getAll();
		Iterator<?> iter = map.entrySet().iterator();  //���map��Iterator
		while(iter.hasNext()) 
		{
			@SuppressWarnings("unchecked")
			Entry<String, ?> entry = (Entry<String, ?>)iter.next();
			mlist.add(new AccountNative(entry.getKey().toString(),entry.getValue().toString()));
		}*/
		DbUtils dbUtils = DbUtils.create(context);
		try {
			List<TiebaDb> tp =dbUtils.findAll(TiebaDb.class);
			if (tp != null) {
				mlist.clear();
				mlist.addAll(tp);
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		
		return mlist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		MyViewHolder viewHolder;
		if (null ==convertView) 
		{
			convertView= layoutInflater.inflate(R.layout.accountitem,null);
			viewHolder=new MyViewHolder();
			viewHolder.img = (ImageView)convertView.findViewById(R.id.accout_img);
			viewHolder.account_name = (TextView)convertView.findViewById(R.id.account_name);
			viewHolder.bt_del = (BootstrapButton)convertView.findViewById(R.id.account_del);
			viewHolder.m_data = mlist.get(position);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder=(MyViewHolder)convertView.getTag();
		}

		viewHolder.account_name.setText(mlist.get(position).getName());
		SharePath.G_IMAGELOADER.displayImage(mlist.get(position).getHeadurl(), viewHolder.img, SharePath.IMG_OPTIONS);
		viewHolder.bt_del.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog add=new AlertDialog.Builder(mContext).setTitle("删除").setMessage("确定删除账号?").
					setPositiveButton("确定",new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
							DbUtils dbUtils = DbUtils.create(mContext);
							try {
								dbUtils.delete(TiebaDb.class, WhereBuilder.b("name", "=", mlist.get(position).getName()));
							} catch (DbException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							UpdateIdList();
							QianDaoFragment_sign.HashChange = true;
						}
					}).setNegativeButton("取消", 
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								
							}
						}).create();
				add.show();
				add.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);					
			}
		});
		return convertView;
	}
	
	class MyViewHolder
	{
		public ImageView img;
		public TextView account_name;
		public BootstrapButton bt_del;
		public TiebaDb m_data;
	}
	public void UpdateIdList()
	{
		mlist = null;
		mlist = new ArrayList<TiebaDb>();
		DbUtils dbUtils = DbUtils.create(mContext);
		try {
			List<TiebaDb> tp =dbUtils.findAll(TiebaDb.class);
			if (tp != null) {
				mlist.clear();
				mlist.addAll(tp);
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		notifyDataSetChanged();
	}
}
