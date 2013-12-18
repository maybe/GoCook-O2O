package com.m6.o2o;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.m6.model.biz.BizModel;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle(R.string.activity_title_main);
		setOnListeners();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		BizModel.clearStaffId(MainActivity.this);
	}
	
	private void setOnListeners() {
		findViewById(R.id.deliver).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, DeliverActivity.class));
			}
		});
		
		findViewById(R.id.overtime).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						startActivity(new Intent(MainActivity.this, OvertimeActivity.class));
					}
				});
		
		/*findViewById(R.id.log).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, LogActivity.class));
			}
		});*/
		
		findViewById(R.id.logout).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				logout();
			}
		});
	}
	
	private void logout() {
		new AlertDialog.Builder(this)
	        .setTitle(R.string.biz_main_exit_title)
	        .setMessage(R.string.biz_main_exit_message)
	        .setPositiveButton(R.string.positive, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	            	MainActivity.this.finish();
	            }
	        })
	        .setNegativeButton(R.string.cancel, null)
	        .create()
	        .show();
	}
}
