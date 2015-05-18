package com.ylq.fragment;

import java.util.List;

import org.json.JSONArray;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindCallback;
import cn.bmob.v3.listener.FindListener;

import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.ylq.adapter.AccountAdapter;
import com.ylq.adapter.FunAdapter;
import com.ylq.model.FunDetial;
import com.ylq.sign.R;

public class SourceFragment_fun extends Fragment {

	private AbPullToRefreshView mAbPullToRefreshView = null;
	private ListView mListView = null;
	FunAdapter adapter ;
	private int cunrrent_page = 0;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_sourcefun, container,
				false);
		
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
		
        //设置adapter
        adapter = new FunAdapter(getActivity(),null);
        mListView.setAdapter(adapter);
        
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				refreshTask();
			}
		}).start();
				
        
		return rootView;
	}
	Handler myhHandler=new Handler();
	public void refreshTask()
	{
		BmobQuery<FunDetial> fun_query = new BmobQuery<FunDetial>();
		fun_query.setLimit(10);
		fun_query.setSkip(0);
		fun_query.include("post_user");
		fun_query.order("-createdAt");
		fun_query.findObjects(getActivity(),new FindListener<FunDetial>() {
			
			@Override
			public void onSuccess(List<FunDetial> arg0) {
				// TODO Auto-generated method stub
				adapter.setList(arg0);
				mAbPullToRefreshView.onHeaderRefreshFinish();
				cunrrent_page = 1;
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
		});
    }
    
    public void loadMoreTask(){
        
		BmobQuery<FunDetial> fun_query = new BmobQuery<FunDetial>();
		fun_query.setLimit(10);
		fun_query.setSkip(cunrrent_page*10);
		fun_query.include("post_user");
		fun_query.order("-createdAt");
		fun_query.findObjects(getActivity(),new FindListener<FunDetial>() {
			
			@Override
			public void onSuccess(List<FunDetial> arg0) {
				// TODO Auto-generated method stub
				adapter.addList(arg0);
				mAbPullToRefreshView.onFooterLoadFinish();
				cunrrent_page++;
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
		});
    }
}
