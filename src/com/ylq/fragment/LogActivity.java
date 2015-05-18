package com.ylq.fragment;

import java.util.List;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.ylq.model.LogHistory;
import com.ylq.sign.MainActivity;
import com.ylq.sign.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

public class LogActivity extends Activity{

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_log);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		TextView tx = (TextView)findViewById(R.id.show_log);
		DbUtils dbUtils = DbUtils.create(this);
		try {
			List<LogHistory> logs = dbUtils.findAll(LogHistory.class);
			if (logs == null || logs.size() <= 0) {
				return;
			}
			tx.setText(Html.fromHtml(logs.get(0).getLog()));      
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

    @SuppressWarnings("deprecation")
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:             
                Intent upIntent = new Intent(this, MainActivity.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {                   
                    TaskStackBuilder.from(this)
                            //如果这里有很多原始的Activity,它们应该被添加在这里
                            .addNextIntent(upIntent)
                            .startActivities();
                    finish();
                } else {                   
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    } 
}
