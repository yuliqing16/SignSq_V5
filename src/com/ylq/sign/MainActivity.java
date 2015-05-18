package com.ylq.sign;

import java.io.File;

import cn.bmob.v3.Bmob;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.ylq.config.SharePath;
import com.ylq.config.UiConfig;
import com.ylq.fragment.ChatFragment;
import com.ylq.fragment.JifenFragment;
import com.ylq.fragment.QianDaoFragment;
import com.ylq.fragment.SourceFragment;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		

		
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		switch (position) {
		case UiConfig.FRAGMENT_CHART:
			fragmentManager
			.beginTransaction()
			.replace(R.id.container,
					ChatFragment.newInstance(position)).commit();
			break;
		case UiConfig.FRAGMENT_JIFEN:
			fragmentManager
			.beginTransaction()
			.replace(R.id.container,
					JifenFragment.newInstance(position)).commit();
			break;
		case UiConfig.FRAGMENT_QIANDAO:
			fragmentManager
			.beginTransaction()
			.replace(R.id.container,
					QianDaoFragment.newInstance(position)).commit();
			break;
		case UiConfig.FRAGMENT_SOURCE:	
			fragmentManager
			.beginTransaction()
			.replace(R.id.container,
					SourceFragment.newInstance(position)).commit();
			break;
		case UiConfig.FRAGMENT_QUIT:	
			System.exit(0);
			break;
		}

/*		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(position + 1)).commit();*/
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case UiConfig.FRAGMENT_QIANDAO:
			mTitle = getString(R.string.title_section_qiandao);
			break;
		case UiConfig.FRAGMENT_SOURCE:
			mTitle = getString(R.string.title_section_source);
			break;
		case UiConfig.FRAGMENT_CHART:
			mTitle = getString(R.string.title_section_chat);
			break;
		case UiConfig.FRAGMENT_JIFEN:
			mTitle = getString(R.string.title_section_jifen);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			//getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
	}

}
