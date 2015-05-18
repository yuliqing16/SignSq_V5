package com.ylq.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.GetServerTimeListener;
import cn.bmob.v3.listener.UpdateListener;

import com.baidu.mobads.AdView;
import com.baidu.mobads.InterstitialAd;
import com.baidu.mobads.InterstitialAdListener;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.ylq.adapter.TiebaListAdapter;
import com.ylq.config.SharePath;
import com.ylq.model.LogHistory;
import com.ylq.model.TiebaDb;
import com.ylq.model.TiebaItem;
import com.ylq.model.User;
import com.ylq.sign.NavigationDrawerFragment;
import com.ylq.sign.R;
import com.ylq.signtool.SignTool;

public class QianDaoFragment_sign extends Fragment {

	private static QianDaoFragment_sign mInstance = null;
	public static boolean HashChange = false;
	private boolean isDistory;
	public static QianDaoFragment_sign newInstance() {
		if (mInstance == null) {
			mInstance = new QianDaoFragment_sign();
		}
		return mInstance;
	}
	InterstitialAd interAd;
	public FrameLayout framelayout;
	TiebaListAdapter adapter;
	RadioGroup thredchoice;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_qiandao_sign, container,
				false);	

		interAd = interAd=new InterstitialAd(getActivity());
		interAd.setListener(new InterstitialAdListener(){

			@Override
			public void onAdClick(InterstitialAd arg0) {
				Log.i("InterstitialAd","onAdClick");
			}

			@Override
			public void onAdDismissed() {
				Log.i("InterstitialAd","onAdDismissed");
				interAd.loadAd();
			}

			@Override
			public void onAdFailed(String arg0) {
				Log.i("InterstitialAd","onAdFailed");
			}

			@Override
			public void onAdPresent() {
				Log.i("InterstitialAd","onAdPresent");
			}

			@Override
			public void onAdReady() {
				Log.i("InterstitialAd","onAdReady");
			}
			
		});
		interAd.loadAd();
		
		framelayout = (FrameLayout)rootView.findViewById(R.id.aaaview);
		framelayout.setVisibility(View.GONE);
		isDistory = false;
		thredchoice = (RadioGroup)rootView.findViewById(R.id.choice);
		ExpandableListView myEx = (ExpandableListView)rootView.findViewById(R.id.tb_list);
		adapter = new TiebaListAdapter(getActivity());
		myEx.setAdapter(adapter);
		myEx.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				final TiebaDb db = (TiebaDb)adapter.getGroup(groupPosition);
				final TiebaItem item = (TiebaItem) adapter.getChild(groupPosition, childPosition);
				myHandler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(),"签到:"+db.getName()+":"+item.getTbname(), 300).show();
					}
				});
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						SignTool sg = new SignTool(getActivity());
						final String result  = sg.signEachTieba(db.getCookie(), item.getTbname(), 1);
						myHandler.post(new Runnable() 
						{
							@Override
							public void run() 
							{
								// TODO Auto-generated method stub
								Toast.makeText(getActivity(),result, 300).show();
							}
						});
						
					}
				}).start();
				return false;
			}
		});
		BootstrapButton bt_log = (BootstrapButton)rootView.findViewById(R.id.bt_log);
		BootstrapButton bbsign = (BootstrapButton)rootView.findViewById(R.id.bt_sign);
		BootstrapButton bt_flush = (BootstrapButton)rootView.findViewById(R.id.bt_flush);
		bt_flush.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myHandler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), "正在刷新列表,请稍后...", 300).show();
					}
				});
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						List<TiebaDb> tbsDbs = adapter.getGroupList();
						DbUtils dbUtils =DbUtils.create(getActivity());
						for (int i = 0; i < tbsDbs.size(); i++) 
						{
							ArrayList<TiebaItem>  items = SignTool.getTiebaList(SignTool.TurnToHash(tbsDbs.get(i).getCookie()));
							tbsDbs.get(i).setTblist(items);
							try {
								tbsDbs.get(i).getTblist();
								dbUtils.update(tbsDbs.get(i), "tblist");
								
							} catch (DbException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						myHandler.post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(getActivity(), "刷新完成", 300).show();
								adapter.UpdateIdList();
							}
						});
						
					}
				}).start();
			}
		});
		bbsign.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//((TiebaItem)adapter.getChild(0, 0)).setSuccess(true).setScoreadd(7);
				//((TiebaDb)adapter.getGroup(0)).setTbListItem(((TiebaItem)adapter.getChild(0, 0)).setTbname("123"), 0);
				//adapter.notifyDataSetChanged();
				//Log.d("TAG", ((TiebaItem)adapter.getChild(0, 0)).toString());
				//((TiebaItem)adapter.getChild(0, 0)).setSuccess(true).setScoreadd(7).setTbname("啊啊啊啊");
				//Log.d("TAG", ((TiebaItem)adapter.getChild(0, 0)).toString());

				try {
					switch (thredchoice.getCheckedRadioButtonId()) 
					{
					case R.id.radioButton1:
						signThread(1,adapter);
						break;
					case R.id.radioButton2:
						signThread(2,adapter);
						break;
					case R.id.radioButton3:
						signThread(3,adapter);
						break;
					case R.id.radioButton4:
						signThread(4,adapter);
						break;
					case R.id.radioButton5:
						signThread(5,adapter);
						break;
					default:
						signThread(5,adapter);
						break;
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
			}
		});
		
		bt_log.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), LogActivity.class);
				getActivity().startActivity(intent);
			}
		});
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) 
				{
					if (HashChange) 
					{
						myHandler.post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								adapter.UpdateIdList();
							}
						});
						HashChange = false;
					}
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if (isDistory) 
					{
						return;
					}
				}
			}
		}).start();
		return rootView;
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.d("QianDaoFragment_sign", "onDestroy");
		isDistory = true;
		super.onDestroy();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isDistory = false;
		
		User cu_user = BmobUser.getCurrentUser(getActivity(), User.class);
		if (cu_user == null) 
		{
			framelayout.setVisibility(View.VISIBLE);
			framelayout.removeAllViews();
			AdView adView = new AdView(getActivity());
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, 
	    			FrameLayout.LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER;
			framelayout.addView(adView, params);
		}
		else 
		{
			Bmob.getServerTime(getActivity(), new GetServerTimeListener() {
			    @Override
			    public void onSuccess(long time) {
			        // TODO Auto-generated method stub
			        SimpleDateFormat formatter = new SimpleDateFormat(
			                "yyyy-MM-dd HH:mm:ss");
			        String times = formatter.format(new Date(time * 1000L));
			        Log.d("bmob","当前服务器时间为:" + times);
			        final User c_temp = User.getCurrentUser(getActivity(),User.class);
			        if (c_temp.getAdtime() > time * 1000) 
			        {
			        	framelayout.setVisibility(View.GONE);
					}
			        else {
			        	framelayout.setVisibility(View.VISIBLE);
			        	framelayout.removeAllViews();
						AdView adView = new AdView(getActivity());
						FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, 
				    			FrameLayout.LayoutParams.WRAP_CONTENT);
						params.gravity = Gravity.CENTER;
						framelayout.addView(adView, params);
					}
			    }

			    @Override
			    public void onFailure(int code, String msg) {
			        // TODO Auto-generated method stub
			    }
			});
		}
	}

	private ProgressDialog mProgressDialog;
	List<TagMessage> queueList;
	int _total_all = 1;
	int _process = 0;
	int _endnum = 0;
	int _tnum = 0;
	long start_time,end_time;
	StringBuilder sb_log;
	@SuppressLint("HandlerLeak")
	Handler myHandler=new Handler()
	{

		@Override
		public void handleMessage(Message msg) 
		{
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) 
			{
			case SharePath.SEND_TREAD_NUM:
				_tnum = msg.arg1;
				_total_all = 1;
				_process = 0;
				_endnum = 0;
				start_time = System.currentTimeMillis();
				sb_log = new StringBuilder();
				if (queueList != null) {
					queueList.clear();
					queueList = null;
				}
				queueList= new ArrayList<TagMessage>();
				for (int i = 0; i < _tnum; i++) 
				{
					queueList.add(new TagMessage(i+1));
				}
				break;
			case SharePath.SEND_TB_ALL_NUM:
				_total_all = msg.arg1;
				break;
			case SharePath.DIALOG_SHOW_WHAT:
				mProgressDialog = new ProgressDialog(getActivity());
				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mProgressDialog.setTitle("签到中:0/"+_total_all);
				//mProgressDialog.setMessage("正在获取贴吧列表、贴吧用户名、头像中。。。");
				mProgressDialog.setIcon(R.drawable.ic_launcher);
				mProgressDialog.setIndeterminate(false);
				mProgressDialog.setCancelable(false);
				
				sb_log.append("<h1>"+longToDataString(start_time)+"</h1>");
				sb_log.append("<p><strong><font color=\"#00bbaa\">开始签到:</strong></p><p></p>");
				mProgressDialog.show();
				break;
			case SharePath.DIALOG_CLOSE_WHAT:
				_endnum++;
				if (_endnum >= _tnum) 
				{
					mProgressDialog.cancel();
					mProgressDialog.dismiss();
							
					Toast.makeText(getActivity(), "签到完成,可以查看日志!!!", Toast.LENGTH_SHORT).show();
					sb_log.append("<p>签到完成,花费时间:"+((System.currentTimeMillis() - start_time)/1000)+"s</p>");
					sb_log.append("<p>平均耗时:"+(int)(((System.currentTimeMillis() - start_time)/1000.0)/(float)_total_all*1000)/1000.0+"s/个</p>");
					DbUtils dbUtils =DbUtils.create(getActivity());
					try {
						List<LogHistory> logs =dbUtils.findAll(LogHistory.class);
						if (logs == null || logs.size() == 0) 
						{//无数据,进行插入
							LogHistory logsss = new LogHistory();
							logsss.setLog(sb_log.toString());
							dbUtils.save(logsss);
						}else {
							dbUtils.dropTable(LogHistory.class);
							LogHistory logsss = new LogHistory();
							logsss.setLog(sb_log.toString());
							dbUtils.save(logsss);
						}
					} catch (DbException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					User cu_user = BmobUser.getCurrentUser(getActivity(), User.class);
					if (cu_user == null) 
					{
						myHandler.postDelayed(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(interAd.isAdReady()){
									interAd.showAd(QianDaoFragment_sign.this.getActivity());
								}else{
									interAd.loadAd();
								}
							}
						},200);
					}
					else 
					{
						Bmob.getServerTime(getActivity(), new GetServerTimeListener() {
						    @Override
						    public void onSuccess(long time) {
						        // TODO Auto-generated method stub
						        SimpleDateFormat formatter = new SimpleDateFormat(
						                "yyyy-MM-dd HH:mm:ss");
						        String times = formatter.format(new Date(time * 1000L));
						        Log.d("bmob","当前服务器时间为:" + times);
						        final User c_temp = User.getCurrentUser(getActivity(),User.class);
						        if (c_temp.getAdtime() > time * 1000) 
						        {
						        	
								}
						        else {
									myHandler.postDelayed(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											if(interAd.isAdReady()){
												interAd.showAd(QianDaoFragment_sign.this.getActivity());
											}else{
												interAd.loadAd();
											}
										}
									},200);
								}
						    }

						    @Override
						    public void onFailure(int code, String msg) {
						        // TODO Auto-generated method stub
						    }
						});
					}

				}

				break;
			case SharePath.DIALOG_CHANGE_TEXT_WHAT:
				//msg.arg1为线程id
				queueList.set(msg.arg1 - 1, new TagMessage(msg.what,(String)msg.obj));
				_process++;
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < queueList.size(); i++) 
				{
					sb.append(queueList.get(i).msg.split("####")[0]);
					sb.append("\n");
				}
				String []buf = ((String)msg.obj).split("####");
				if (buf[0].contains("成功") || buf[0].contains("跳过") || buf[0].contains("已签到")) 
				{
					sb_log.append("<p>["+longToTimeOnly(System.currentTimeMillis())+"]"+buf[1]+":"+buf[0]+"</p>");
				}
				else {
					sb_log.append("<p>["+longToTimeOnly(System.currentTimeMillis())+"]<font color=\"#ff0000\">"+buf[1]+":"+buf[0]+"</p>");
				}
				mProgressDialog.setMessage(sb.toString());
				mProgressDialog.setTitle("签到中:"+_process+"/"+_total_all);
				break;
			}
			
		}
		
	};
	int ti=0;
	private void signThread(final int tnum,TiebaListAdapter adapter)
	{
		Message msg = myHandler.obtainMessage();
		msg.what = SharePath.SEND_TREAD_NUM;
		msg.arg1 = tnum;
		myHandler.sendMessage(msg);
		try {
			Thread.sleep(1);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		final List<TiebaDb> dbtb = adapter.getGroupList();
		if (dbtb == null || dbtb.size() == 0) {
			return ;
		}
		int total_all = 0;
		for (int j = 0; j < dbtb.size(); j++) 
		{
			try {
				total_all += dbtb.get(j).getTiebaItemList().size();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
		msg = null;
		msg = myHandler.obtainMessage();
		msg.arg1 = total_all;
		msg.what = SharePath.SEND_TB_ALL_NUM;
		myHandler.sendMessage(msg);
		
		
		if (dbtb.size() >0) 
		{
			msg = null;
			msg = myHandler.obtainMessage();
			msg.what = SharePath.DIALOG_SHOW_WHAT;
			myHandler.sendMessage(msg);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		
		
		for (ti = 1; ti <= tnum; ti++) 
		{//线程切分,按照账号分,每个账号切分给各个线程
			final int tpti = ti;
			new Thread(new Runnable()
			{		
				@Override
				public void run() 
				{
					
					// TODO Auto-generated method stub
					for (int j = 0; j < dbtb.size(); j++) 
					{
						int tbnum = dbtb.get(j).getTiebaItemList().size();
						/*if (tpti == tnum && tpti != 1) 
						{
							Log.d("签到线程:"+tpti,"签到账号"+dbtb.get(j).getName()+":从"+((tpti-1)*tbnum/tnum)+"到"+(tbnum-1));
						}
						else {
							Log.d("签到线程:"+tpti,"签到账号"+dbtb.get(j).getName()+":从"+((tpti-1)*tbnum/tnum)+"到"+(tpti * tbnum / tnum -1));
						}*/

						//( (ti-1) * tbnum / tnum , ti * tbnum / tnum)  (1 ~ tnum-1)
						//( (ti-1) * tbnum / tnum , tbnum)  (1 ~ tnum-1)
						 
						if (tpti == tnum && tpti != 1) 
						{
							for (int kk = ((tpti-1)*tbnum/tnum); kk <= (tbnum-1); kk++) 
							{
								try {
									/*Message msg  = myHandler.obtainMessage();
									msg.what = SharePath.DIALOG_CHANGE_TEXT_WHAT;
									msg.arg1 = tpti;
									msg.obj = "线程"+tpti+":开始签到"+dbtb.get(j).getTiebaItemList().get(kk).getTbname();
									myHandler.sendMessage(msg);*/
									
									SignTool sg = new SignTool(QianDaoFragment_sign.this.getActivity());
									final String resultString= sg.signEachTieba(dbtb.get(j).getCookie(), dbtb.get(j).getTiebaItemList().get(kk).getTbname(), 1);
									/*myHandler.post(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											Toast.makeText(QianDaoFragment_sign.this.getActivity(), resultString, Toast.LENGTH_SHORT).show();
										}
									});*/
									Message msg = null;
									msg  = myHandler.obtainMessage();
									msg.what = SharePath.DIALOG_CHANGE_TEXT_WHAT;
									msg.arg1 = tpti;
									msg.obj = "线程"+tpti+":\n\t"+resultString+"####"+dbtb.get(j).getName();
									myHandler.sendMessage(msg);
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}

							}
						}
						else 
						{
							for (int kk = ((tpti-1)*tbnum/tnum); kk <= (tpti * tbnum / tnum -1); kk++) 
							{
								try {
									/*Message msg  = myHandler.obtainMessage();
									msg.what = SharePath.DIALOG_CHANGE_TEXT_WHAT;
									msg.arg1 = tpti;
									msg.obj = "线程"+tpti+"(进度)"+kk+"/"+(tpti * tbnum / tnum -1)+":开始签到   "+dbtb.get(j).getTiebaItemList().get(kk).getTbname();
									myHandler.sendMessage(msg);*/
									
									SignTool sg = new SignTool(QianDaoFragment_sign.this.getActivity());
									final String resultString= sg.signEachTieba(dbtb.get(j).getCookie(), dbtb.get(j).getTiebaItemList().get(kk).getTbname(), 1);
									/*myHandler.post(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											Toast.makeText(QianDaoFragment_sign.this.getActivity(), resultString, Toast.LENGTH_SHORT).show();
										}
									});*/
									
									Message msg = null;
									msg  = myHandler.obtainMessage();
									msg.what = SharePath.DIALOG_CHANGE_TEXT_WHAT;
									msg.arg1 = tpti;
									msg.obj = "线程"+tpti+":\n\t"+resultString+"####"+dbtb.get(j).getName();
									myHandler.sendMessage(msg);
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
								}
							}
						}
					}
					//单线程签到完成
					Message msg = myHandler.obtainMessage();
					msg.what = SharePath.DIALOG_CLOSE_WHAT;
					myHandler.sendMessage(msg);
				}
			}).start();
			
		}
		
	}
	class TagMessage
	{
		int threadnum;
		public int getThreadnum() {
			return threadnum;
		}
		public void setThreadnum(int threadnum) {
			this.threadnum = threadnum;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		String msg;
		public TagMessage(int t)
		{
			this.threadnum = t;
			msg = "";
		}
		public TagMessage(int t,String _msg)
		{
			this.threadnum = t;
			msg = _msg;
		}
	}
	
	@SuppressLint("SimpleDateFormat")
	private String longToDataString(long time)
	{
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");
		 //前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
		java.util.Date dt = new Date(time);  
		 String sDateTime = sdf.format(dt);  //得到精确到秒的表示：08/31/2006 21:08:00
		return sDateTime;
	}
	@SuppressLint("SimpleDateFormat")
	private String longToTimeOnly(long time)
	{
		SimpleDateFormat sdf= new SimpleDateFormat("HH:mm:ss");
		 //前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
		java.util.Date dt = new Date(time);  
		 String sDateTime = sdf.format(dt);  //得到精确到秒的表示：08/31/2006 21:08:00
		return sDateTime;
	}
}
