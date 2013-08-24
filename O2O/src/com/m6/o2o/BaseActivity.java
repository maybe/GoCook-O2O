package com.m6.o2o;

import android.app.Activity;
import android.app.ProgressDialog;

public class BaseActivity extends Activity {
	
	private ProgressDialog mProgressDialog;
	
	/**
	 * Show progress dialog
	 * 
	 * @param resId
	 */
	public void showProgressDialog(int resId) {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setCancelable(true);
		}
		mProgressDialog.setMessage(getString(resId));
		mProgressDialog.show();
	}
	
	/**
	 * Dismiss progress dialog
	 */
	public void dismissProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}

}
