package com.ylq.fragment;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.ab.view.pullview.AbPullToRefreshView;
import com.ylq.adapter.BankAdapter;
import com.ylq.commonui.AbSlidingTabView;
import com.ylq.model.User;
import com.ylq.sign.MainActivity;
import com.ylq.sign.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class JifenFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static JifenFragment newInstance(int sectionNumber) {
		JifenFragment fragment = new JifenFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public JifenFragment() {
	}
	private AbPullToRefreshView mAbPullToRefreshView = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_jifen, container,
				false);
		AbSlidingTabView mAbSlidingTabView = (AbSlidingTabView)rootView.findViewById(R.id.mAbSlidingTabView);
		mAbSlidingTabView.SetFm(getChildFragmentManager());
		mAbSlidingTabView.getViewPager().setOffscreenPageLimit(3);
		
		mAbSlidingTabView.setTabTextColor(Color.BLACK);
		mAbSlidingTabView.setTabSelectColor(Color.rgb(30, 168, 131));
		mAbSlidingTabView.setTabBackgroundResource(R.drawable.tab_bg);
		mAbSlidingTabView.setTabLayoutBackgroundResource(R.drawable.slide_top);

		mAbSlidingTabView.addItemView("商城",new JifenFragment_shop());
		mAbSlidingTabView.addItemView("排名",new JifenFragment_bank());
		mAbSlidingTabView.addItemView("获取积分",new JifenFragment_get());
		
		
		mAbSlidingTabView.setTabPadding(20, 8, 20, 8);
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
	}
}
