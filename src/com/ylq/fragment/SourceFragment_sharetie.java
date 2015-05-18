package com.ylq.fragment;

import java.io.ByteArrayOutputStream;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.ylq.sign.R;
import com.ylq.sign.ShareFunActivity;

public class SourceFragment_sharetie extends Fragment {



	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_sourcetie, container,
				false);

		BootstrapButton bt_send = (BootstrapButton)rootView.findViewById(R.id.bt_send);
		bt_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SourceFragment_sharetie.this.getActivity(), ShareFunActivity.class);
				SourceFragment_sharetie.this.getActivity().startActivity(intent);
			}
		});
		return rootView;
	}
	

}
