package com.ylq.fragment;

import java.util.ArrayList;

import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.ylq.adapter.AccountAdapter;
import com.ylq.adapter.TiebaListAdapter;
import com.ylq.commonui.AbSlidingTabView;
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

public class SourceFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static SourceFragment newInstance(int sectionNumber) {
		SourceFragment fragment = new SourceFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public SourceFragment() {
	}

	private AbPullToRefreshView mAbPullToRefreshView = null;
	private ListView mListView = null;
	AccountAdapter adapter ;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_source, container,
				false);
		//RelativeLayout rel=(RelativeLayout)rootView.findViewById(R.id.qdlayout);
		//mAbSlidingTabView = new AbSlidingTabView(getActivity(), null,getChildFragmentManager());
		AbSlidingTabView mAbSlidingTabView = (AbSlidingTabView)rootView.findViewById(R.id.mAbSlidingTabView);
		mAbSlidingTabView.SetFm(getChildFragmentManager());
		mAbSlidingTabView.getViewPager().setOffscreenPageLimit(3);
		
		//������ʽ
		mAbSlidingTabView.setTabTextColor(Color.BLACK);
		mAbSlidingTabView.setTabSelectColor(Color.rgb(30, 168, 131));
		mAbSlidingTabView.setTabBackgroundResource(R.drawable.tab_bg);
		mAbSlidingTabView.setTabLayoutBackgroundResource(R.drawable.slide_top);

		mAbSlidingTabView.addItemView("趣图",new SourceFragment_fun());
		//mAbSlidingTabView.addItemView("分享",new SourceFragment_sharesrc());
		mAbSlidingTabView.addItemView("我要发布",new SourceFragment_sharetie());

		mAbSlidingTabView.setTabPadding(20, 8, 20, 8);
		
		/*
		
        mAbPullToRefreshView = (AbPullToRefreshView)rootView.findViewById(R.id.mPullRefreshView);
        mListView = (ListView)rootView.findViewById(R.id.mListView);
        
        //设置监听器
        mAbPullToRefreshView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			
			@Override
			public void onHeaderRefresh(AbPullToRefreshView view) {
				refreshTask();
			}
		});
        mAbPullToRefreshView.setOnFooterLoadListener(new OnFooterLoadListener() {
			
			@Override
			public void onFooterLoad(AbPullToRefreshView view) {
				loadMoreTask();
				
			}
		});
        //设置进度条的样式
        mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
        mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
		
        adapter = new AccountAdapter(getActivity());
        mListView.setAdapter(adapter);
        
        refreshTask();*/
        return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
	}
	
	public void refreshTask(){
        AbTask mAbTask = new AbTask();
    	AbTaskItem item = new AbTaskItem();
    	item.setListener(new AbTaskListener() {

			@Override
			public void update() {
				mAbPullToRefreshView.onHeaderRefreshFinish();
			}

			@Override
			public void get() {
	   		    try {
	   		    	Thread.sleep(1000);
	   		    } catch (Exception e) {
	   		    }
		  };
		});
        
        mAbTask.execute(item);
    }
    
    public void loadMoreTask(){
    	
        AbTask mAbTask = new AbTask();
        AbTaskItem item = new AbTaskItem();
        item.setListener(new AbTaskListener() {

			@Override
			public void update() {
				mAbPullToRefreshView.onFooterLoadFinish();
			}

			@Override
			public void get() {
	   		    try {
	   		    	Thread.sleep(1000);
	   		    	
	   		    } catch (Exception e) {
	   		    }
		  };
		});
        
        mAbTask.execute(item);
    }
}
