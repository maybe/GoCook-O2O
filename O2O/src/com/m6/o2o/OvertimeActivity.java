package com.m6.o2o;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class OvertimeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overtime);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.overtime, menu);
		return true;
	}

}
