package com.ylq.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ylq.commonui.AbSlidingTabView;
import com.ylq.sign.MainActivity;
import com.ylq.sign.R;

public class QianDaoFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static QianDaoFragment minstance=null;
	
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	
	public static QianDaoFragment newInstance(int sectionNumber) {
		QianDaoFragment fragment = new QianDaoFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public QianDaoFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_qiandao, null,
				false);
		//RelativeLayout rel=(RelativeLayout)rootView.findViewById(R.id.qdlayout);
		//mAbSlidingTabView = new AbSlidingTabView(getActivity(), null,getChildFragmentManager());
		AbSlidingTabView mAbSlidingTabView = (AbSlidingTabView)rootView.findViewById(R.id.mAbSlidingTabView);
		mAbSlidingTabView.SetFm(getChildFragmentManager());
		mAbSlidingTabView.getViewPager().setOffscreenPageLimit(2);
		
		//������ʽ
		mAbSlidingTabView.setTabTextColor(Color.BLACK);
		mAbSlidingTabView.setTabSelectColor(Color.rgb(30, 168, 131));
		mAbSlidingTabView.setTabBackgroundResource(R.drawable.tab_bg);
		mAbSlidingTabView.setTabLayoutBackgroundResource(R.drawable.slide_top);

		mAbSlidingTabView.addItemView("签到",QianDaoFragment_sign.newInstance() );
		mAbSlidingTabView.addItemView("账号管理",new QianDaoFragment_admin());
		//��ʾ����һ��

		mAbSlidingTabView.setTabPadding(20, 8, 20, 8);
		//rel.addView(mAbSlidingTabView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		return rootView;

	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}
	
	
}
