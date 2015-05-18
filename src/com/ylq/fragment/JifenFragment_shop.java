package com.ylq.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.ylq.adapter.ShopItemAdapter;
import com.ylq.model.ShopItem;
import com.ylq.sign.R;

public class JifenFragment_shop extends Fragment {


	ShopItemAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_jifen_shop, container,
				false);
		ListView shop_listview = (ListView)rootView.findViewById(R.id.shop_listview);
		adapter = new ShopItemAdapter(getActivity());
		shop_listview.setAdapter(adapter);
		BmobQuery<ShopItem> item = new BmobQuery<ShopItem>();
		item.setLimit(200);
		item.findObjects(getActivity(), new FindListener<ShopItem>() {
			
			@Override
			public void onSuccess(List<ShopItem> arg0) {
				// TODO Auto-generated method stub
				if (arg0 != null) {
					adapter.loadData(arg0);
				}
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		return rootView;
	}

}
