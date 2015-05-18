package com.ylq.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.drawable.shapes.ArcShape;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.android.vip.feng.model.DevSoftModel;
import cn.android.vip.feng.ui.DevInstance;
import cn.android.vip.feng.ui.utils.DevListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindCallback;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetServerTimeListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.baidu.mobads.AdView;
import com.baidu.mobads.AdViewListener;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.ylq.model.CheatControl;
import com.ylq.model.User;
import com.ylq.model.UserAdClick;
import com.ylq.sign.R;

public class JifenFragment_get extends Fragment implements DevListener{
	private DevInstance		geInstance;
	private CheatControl g_control;
	protected void initGEDate()
	{

		geInstance = DevInstance.getInstance();
		// 初始函数
//		geInstance.initialize(this, "10000", "T10001");// 每次程序启动只要初始化一次(设置开发者应用UID和PID)
		geInstance.initialize(getActivity(), "361e2b6bbe82801dP2R5UmJiilvtsonfn4K0xc7ckdzU27LpnbOf3sdHPjCkGDkq2Q", "木蚂蚁");// 每次程序启动只要初始化一次(设置开发者应用UID和PID)
		geInstance.setDownProgressStyle(DevInstance.DEV_DOWN_STYLE_SYSTEM);
		geInstance.setTestMode(true);// （必加）开启测试模式(默认是关闭的,测试的时候可以开启,方便调试并查看广告后台错误信息)
		geInstance.setOnDevListener(this);// （必加） 让调用GEInstance的主类实现GEListener接口(1.监听自定义广告数据 2.监听是否获取金币成功)
		geInstance.setNotificationIcon(R.drawable.ic_launcher);// (选加)设置状态栏图标
		geInstance.setOpenIntegralWall(true);// (选加)设置积分墙是否有积分功能
		geInstance.setScoreRemind(true);// (选加)每次下载是否有“本次下载有积分”提示
		geInstance.openFunsByAnyClick(false);// (选加)是否开启点击任意(自定义广告除外)广告都打开积分墙(默认不开启)
		// 虚拟积分
		geInstance.setSocreUnit("积分");// （必加）设置积分的单位(例如:金币或金蛋或人民币等)
		geInstance.setScoreParam(0.08f);// (选加)设置金币的转换率(例如:如果默认下载一款软件得到的积分是50,若设置转换率为10,则最终显示并赋予用户的积分是50*10=500)

		geInstance.setListName("获取积分");// 修改积分墙标题名称
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_jifen_get, container,
				false);
		initGEDate();
		g_control = new CheatControl();
		g_control.setIs_show(true);
		g_control.setMax_score(10);		
		
		final TextView info = (TextView)rootView.findViewById(R.id.info);
		
		final FrameLayout framelayout = (FrameLayout)rootView.findViewById(R.id.adview1);
		AdView adView = new AdView(getActivity());
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, 
    			FrameLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		framelayout.addView(adView, params);

		
		BmobQuery<CheatControl> cheat_query = new BmobQuery<CheatControl>();
		cheat_query.findObjects(getActivity(), new FindListener<CheatControl>() {

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(List<CheatControl> arg0) {
				// TODO Auto-generated method stub
				if(arg0!= null && arg0.size() >0)
				{
					g_control.setIs_show(arg0.get(0).getIs_show());
					g_control.setMax_score(arg0.get(0).getMax_score());
					
					if ( arg0.get(0).getIs_show() == false)
					{
						framelayout.setVisibility(View.GONE);
						info.setVisibility(View.GONE);
					}
					else
					{
						framelayout.setVisibility(View.VISIBLE);
						info.setVisibility(View.VISIBLE);
						info.setText(arg0.get(0).getInfo());
					}
				}
			}
		});
		
		adView.setListener(new AdViewListener() {
			
			@Override
			public void onVideoStart() {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onVideoFinish() {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onVideoError() {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onVideoClickReplay() {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onVideoClickClose() {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onVideoClickAd() {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onAdSwitch() {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onAdShow(JSONObject arg0) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onAdReady(AdView arg0) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onAdFailed(String arg0) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onAdClick(JSONObject arg0) {
				// TODO Auto-generated method stub
				Log.d("ad", "onAdClick");
				final User cu_user = BmobUser.getCurrentUser(JifenFragment_get.this.getActivity(), User.class);
				if (cu_user == null) {
					Toast("你还未登录账号,不能获取积分哦,赶快注册或者登录账号吧!");
					return ;
				}
				Bmob.getServerTime(JifenFragment_get.this.getActivity(), new GetServerTimeListener() {
					
					@Override
					public void onSuccess(long arg0) {
						// TODO Auto-generated method stub
						final String now_time = LongTimeToString(arg0 * 1000);
						Log.d("time", now_time);
						BmobQuery<UserAdClick> adclick = new BmobQuery<UserAdClick>();
						adclick.addWhereEqualTo("username", cu_user.getUsername());
						adclick.order("-createdAt");
						adclick.setLimit(3);
						adclick.findObjects(JifenFragment_get.this.getActivity(), new FindListener<UserAdClick>() {
							
							@Override
							public void onSuccess(List<UserAdClick> arg0) {
								// TODO Auto-generated method stub
								if (arg0 !=null && arg0.size() > 0) {
									String add_time = arg0.get(0).getCreatedAt().split(" ")[0];
									if (add_time.equals(now_time)) 
									{
										Toast("今天已经获取该途经获取过分数了,明天可以再次获取!!!");
										return;
									}
									else {
										UserAdClick adclikeAdClick = new UserAdClick();
										adclikeAdClick.setUsername(cu_user.getUsername());
										adclikeAdClick.setBefore_score(cu_user.getScore());
										final int addscore = 1 + (new Random().nextInt(g_control.getMax_score())); 
										adclikeAdClick.setAfter_score(cu_user.getScore() + addscore);
										cu_user.setScore(cu_user.getScore() +addscore);
										adclikeAdClick.save(JifenFragment_get.this.getActivity(), new SaveListener() {
											
											@Override
											public void onSuccess() {
												// TODO Auto-generated method stub
												cu_user.update(JifenFragment_get.this.getActivity(), new UpdateListener() {
													
													@Override
													public void onSuccess() {
														// TODO Auto-generated method stub
														Toast("哎哟,赚了"+addscore+"分!");
													}
													
													@Override
													public void onFailure(int arg0, String arg1) {
														// TODO Auto-generated method stub
														Toast(arg1);
													}
												});
											}
											
											@Override
											public void onFailure(int arg0, String arg1) {
												// TODO Auto-generated method stub
												Toast(arg1);
											}
										});
									}
								}
								else {
									UserAdClick adclikeAdClick = new UserAdClick();
									adclikeAdClick.setUsername(cu_user.getUsername());
									adclikeAdClick.setBefore_score(cu_user.getScore());
									final int addscore = 1 + (new Random().nextInt(g_control.getMax_score())); 
									adclikeAdClick.setAfter_score(cu_user.getScore() + addscore);
									cu_user.setScore(cu_user.getScore() +addscore);
									adclikeAdClick.save(JifenFragment_get.this.getActivity(), new SaveListener() {
										
										@Override
										public void onSuccess() {
											// TODO Auto-generated method stub
											cu_user.update(JifenFragment_get.this.getActivity(), new UpdateListener() {
												
												@Override
												public void onSuccess() {
													// TODO Auto-generated method stub
													Toast("哎哟,赚了"+addscore+"分!");
												}
												
												@Override
												public void onFailure(int arg0, String arg1) {
													// TODO Auto-generated method stub
													Toast(arg1);
												}
											});
										}
										
										@Override
										public void onFailure(int arg0, String arg1) {
											// TODO Auto-generated method stub
											Toast(arg1);
										}
									});
								}
							}
							
							@Override
							public void onError(int arg0, String arg1) {
								// TODO Auto-generated method stub
								Toast(arg1);
							}
						});
					}
					
					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast(arg1);
					}
				});
			}
		});
		
		BootstrapButton bt_get = (BootstrapButton)rootView.findViewById(R.id.bt_get);
		bt_get.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final User cu_user = BmobUser.getCurrentUser(JifenFragment_get.this.getActivity(), User.class);
				if (cu_user == null) {
					Toast("你还未登录账号,不能获取积分哦,赶快注册或者登录账号吧!");
				}
				else {
					geInstance.setScore(cu_user.getScore());
				}
				geInstance.setListSkin("black");
				geInstance.loadFuns();
				
			}
		});
		return rootView;
	}
	public void Toast(String msg)
	{
		Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
	}
	
    @SuppressLint("SimpleDateFormat")
	private String LongTimeToString(long time)
    {
	    SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
	  //前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
	    Date dt = new Date(time);  
	    return sdf.format(dt);  //得到精确到秒的表示：08/31/2006 21:08:00
    }

	@Override
	public void onDevFailed(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDevSucceed(final int arg0) {
		// TODO Auto-generated method stub
		final User cu_user = BmobUser.getCurrentUser(JifenFragment_get.this.getActivity(), User.class);
		if (cu_user == null) {
		}
		else {
			geInstance.setScore(cu_user.getScore() + arg0);
			cu_user.setScore(cu_user.getScore() + arg0);
			cu_user.update(JifenFragment_get.this.getActivity(),new UpdateListener() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					Toast("增加积分:"+arg0+"!!!");
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		
	}

	@Override
	public void onDumutipleInfo(List arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSingleInfo(DevSoftModel arg0) {
		// TODO Auto-generated method stub
		
	}
}
