package com.ylq.fragment;

import cn.bmob.v3.BmobUser;

import com.gotye.api.Gotye;
import com.gotye.api.bean.GotyeRoom;
import com.gotye.api.bean.GotyeSex;
import com.gotye.sdk.GotyeSDK;
import com.ylq.model.User;
import com.ylq.sign.MainActivity;
import com.ylq.sign.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChatFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static ChatFragment newInstance(int sectionNumber) {
		ChatFragment fragment = new ChatFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public ChatFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_chat, container,
				false);
		com.beardedhen.androidbootstrap.BootstrapButton bt_chat = (com.beardedhen.androidbootstrap.BootstrapButton)rootView.findViewById(R.id.bt_send);
		
		bt_chat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					User cu_user = BmobUser.getCurrentUser(ChatFragment.this.getActivity(), User.class);
					String username = cu_user == null ? ("游客" +System.currentTimeMillis()) : cu_user.getUsername();

					String nickName = username;

					GotyeSex sex = GotyeSex.MAN;
					Bitmap head = BitmapFactory.decodeResource(getResources(),
							R.drawable.unknown_head);
					
					GotyeSDK.getInstance().startGotyeSDK(ChatFragment.this.getActivity(), username, nickName, sex, head, null);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				// TODO Auto-generated method stub

			}
		});
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
	}
}
