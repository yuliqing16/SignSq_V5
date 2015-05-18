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

import com.ylq.adapter.BankAdapter;
import com.ylq.model.User;
import com.ylq.sign.R;

public class JifenFragment_bank extends Fragment {


	BankAdapter bankAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_jifen_bank, container,
				false);
		ListView listView = (ListView)rootView.findViewById(R.id.banklist);
		bankAdapter = new BankAdapter(getActivity());
		
		listView.setAdapter(bankAdapter);
		BmobQuery<User> userQuery = new BmobQuery<User>();
		userQuery.setLimit(30);
		userQuery.order("-score");
		userQuery.findObjects(getActivity(), new FindListener<User>() {
			
			@Override
			public void onSuccess(List<User> arg0) {
				// TODO Auto-generated method stub
				if (arg0 != null) 
				{
					bankAdapter.loadData(arg0);
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
