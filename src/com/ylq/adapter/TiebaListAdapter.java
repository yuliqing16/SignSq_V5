package com.ylq.adapter;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.ylq.config.SharePath;
import com.ylq.model.TiebaDb;
import com.ylq.model.TiebaItem;
import com.ylq.sign.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TiebaListAdapter extends BaseExpandableListAdapter{
	
	private List<TiebaDb> m_list;
	private Context m_context;
	private LayoutInflater layoutInflater;
	private DbUtils dbUtils;
	public TiebaListAdapter(Context context)
	{
		layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		m_list = new ArrayList<TiebaDb>();
		m_context = context;
		dbUtils = DbUtils.create(context);
		try {
			List<TiebaDb> tp =dbUtils.findAll(TiebaDb.class);
			if (tp != null) {
				m_list.clear();
				m_list.addAll(tp);
				
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<TiebaDb> getGroupList()
	{
		return m_list;
	}
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return m_list.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return m_list.get(groupPosition).getTiebaItemList().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return m_list.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return m_list.get(groupPosition).getTiebaItemList().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		AccountHolder groupHolder = null;
		if (convertView == null) {
			groupHolder = new AccountHolder();
			convertView = layoutInflater.inflate(R.layout.tieba_account_group, null);
			groupHolder.name = (TextView) convertView
					.findViewById(R.id.group);
			groupHolder.head = (ImageView)convertView
					.findViewById(R.id.header);
			groupHolder.switc = (ImageView) convertView
					.findViewById(R.id.image);
			convertView.setTag(groupHolder);
		} else {
			groupHolder = (AccountHolder) convertView.getTag();
		}

		groupHolder.name.setText(((TiebaDb)getGroup(groupPosition)).getName());
		SharePath.G_IMAGELOADER.displayImage(((TiebaDb)getGroup(groupPosition)).getHeadurl(), groupHolder.head, SharePath.IMG_OPTIONS);
		
		if (isExpanded)// ture is Expanded or false is not isExpanded
			groupHolder.switc.setImageResource(R.drawable.expanded);
		else
			groupHolder.switc.setImageResource(R.drawable.collapse);
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TbHolder tbHolder = null;
		if (convertView == null) {
			tbHolder = new TbHolder();
			
			convertView = layoutInflater.inflate(R.layout.tieba_item, null);
			tbHolder.img_level = (TextView) convertView
					.findViewById(R.id.img_level);
			tbHolder.tb_name = (TextView) convertView
					.findViewById(R.id.tb_name);
			tbHolder.hash_sign = (ImageView) convertView
					.findViewById(R.id.hash_sign);
			tbHolder.level_txt = (TextView) convertView
					.findViewById(R.id.level_txt);
			tbHolder.score_txt = (TextView) convertView
					.findViewById(R.id.score_txt);
			convertView.setTag(tbHolder);
		} else {
			tbHolder = (TbHolder) convertView.getTag();
		}
		String num_lev = "";
		String txt_lev = "";
		try {
			String[] levs = ((TiebaItem)getChild(groupPosition, childPosition)).getLevel().split(" ");
			num_lev = levs[1];
			txt_lev = levs[0];		
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		tbHolder.img_level.setText(num_lev);
		int num_lev_int = Integer.parseInt(num_lev);
		switch (num_lev_int) {
			case 1:
				tbHolder.img_level.setBackgroundResource(R.drawable.level1);
				break;
			case 2:
			case 3:
				tbHolder.img_level.setBackgroundResource(R.drawable.level2_3);
				break;
			case 4:
			case 5:
				tbHolder.img_level.setBackgroundResource(R.drawable.level4_5);
				break;
			case 6:
				tbHolder.img_level.setBackgroundResource(R.drawable.level6);
				break;
			case 7:
				tbHolder.img_level.setBackgroundResource(R.drawable.level7);
				break;
			case 8:
				tbHolder.img_level.setBackgroundResource(R.drawable.level8);
				break;
			case 9:
				tbHolder.img_level.setBackgroundResource(R.drawable.level9);
				break;
			case 10:
				tbHolder.img_level.setBackgroundResource(R.drawable.level10);
				break;
			case 11:
				tbHolder.img_level.setBackgroundResource(R.drawable.level11_18);
				break;
			default:
				tbHolder.img_level.setBackgroundResource(R.drawable.level11_18);
				break;
		}
		tbHolder.tb_name.setText(((TiebaItem)getChild(groupPosition, childPosition)).getTbname());
		tbHolder.level_txt.setText(txt_lev);
		tbHolder.score_txt.setText(((TiebaItem)getChild(groupPosition, childPosition)).getScore());
		
		/*try {
			if (((TiebaItem)getChild(groupPosition, childPosition)).isSuccess()) 
			{
				if (((TiebaItem)getChild(groupPosition, childPosition)).getScoreadd() != 0) 
				{
					tbHolder.score_txt.setText(((TiebaItem)getChild(groupPosition, childPosition)).getScore()+"+"+((TiebaItem)getChild(groupPosition, childPosition)).getScoreadd()+"↑");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}*/

		
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
	
	class AccountHolder
	{
		ImageView head;
		TextView  name;
		ImageView switc;
	}
	class TbHolder
	{
		TextView img_level;
		TextView tb_name;
		ImageView hash_sign;
		TextView level_txt;
		TextView score_txt;
	}
	
	public void UpdateIdList()
	{
		if (m_list !=null) 
		{
			m_list.clear();
			m_list = null;
		}
		m_list = new ArrayList<TiebaDb>();
		try {
			List<TiebaDb> tp =dbUtils.findAll(TiebaDb.class);
			if (tp != null) {
				m_list.clear();
				m_list.addAll(tp);
				
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		notifyDataSetChanged();
	}
}
