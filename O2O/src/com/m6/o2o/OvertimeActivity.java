package com.m6.o2o;

import java.lang.ref.WeakReference;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;
import com.m6.model.base.ResponseData;
import com.m6.model.biz.BizModel;
import com.m6.util.NetUtils;

public class OvertimeActivity extends BaseActivity {

	private String mResultContainerNo;
	
	private static final int RESULT_BOX = 1;
	
	private TimeOutTask mTimeOutTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overtime);
		setTitle(R.string.activity_title_overtime_open_box);
		
		findViewById(R.id.scan_box).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent openCameraIntent = new Intent(OvertimeActivity.this, CaptureActivity.class);  
                startActivityForResult(openCameraIntent, RESULT_BOX);
			}
		});

		findViewById(R.id.exit).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				OvertimeActivity.this.finish();
			}
		});
		
		final Button timeout = (Button) findViewById(R.id.timeout); 
		timeout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!NetUtils.isOnline(OvertimeActivity.this)) {
					Toast.makeText(OvertimeActivity.this, R.string.network_error_tip, Toast.LENGTH_SHORT).show();
					return;
				}
				
				// hide error info
				findViewById(R.id.error).setVisibility(View.GONE);
				findViewById(R.id.tip_error).setVisibility(View.GONE);
				
				Editable boxNo = ((EditText) findViewById(R.id.input_no)).getText();
				if (boxNo != null) {
					String boxNoText = boxNo.toString();
					if (TextUtils.isEmpty(boxNoText)) {
						Toast.makeText(OvertimeActivity.this, R.string.toast_boxno_empty, Toast.LENGTH_LONG).show();
						return;
					}
					
					if (mTimeOutTask == null) {
						showProgressDialog(R.string.open_box_message);
						mTimeOutTask = new TimeOutTask(OvertimeActivity.this, boxNo.toString());
						mTimeOutTask.execute((Void) null);
					}
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && data != null) {
			if (requestCode == RESULT_BOX) {
				mResultContainerNo = data.getCharSequenceExtra(BizModel.ACTIVITY_RESULT).toString();
				((EditText) findViewById(R.id.input_no)).setText(mResultContainerNo);
//				TextView result = (TextView) findViewById(R.id.result_box);
//				result.setText(mResultContainerNo);
//				result.setVisibility(View.VISIBLE);
//				findViewById(R.id.tip_box).setVisibility(View.VISIBLE);
			}
		}
	}
	
	private void bindData(ResponseData responseData) {
		mTimeOutTask = null;
		dismissProgressDialog();
		if (responseData.getFlag() == 1) { // success
			Toast.makeText(this, R.string.overtime_openbox_success, Toast.LENGTH_SHORT).show();
		} else {
			TextView error = (TextView) findViewById(R.id.error);
			error.setText(responseData.getMsg());
			
			error.setVisibility(View.VISIBLE);
			findViewById(R.id.tip_error).setVisibility(View.VISIBLE);
		}
	}
	
	private void onCancel() {
		mTimeOutTask = null;
		dismissProgressDialog();
	}
	
	private static class TimeOutTask extends AsyncTask<Void, Void, ResponseData> {

		private WeakReference<OvertimeActivity> mActivity;
		private String mContainerNo;
		
		public TimeOutTask(OvertimeActivity activity, String containerNo) {
			mActivity = new WeakReference<OvertimeActivity>(activity);
			mContainerNo = containerNo;
		}
		
		@Override
		protected ResponseData doInBackground(Void... params) {
			return BizModel.timeOut(BizModel.getStaffId(mActivity.get()), mContainerNo);
		}
		
		@Override
		protected void onPostExecute(ResponseData result) {
			if (mActivity != null) {
				OvertimeActivity activity = mActivity.get();
				if (activity != null) {
					activity.bindData(result);
				}
			}
		}
		
		@Override
		protected void onCancelled() {
			super.onCancelled();
			if (mActivity != null) {
				OvertimeActivity activity = mActivity.get();
				if (activity != null) {
					activity.onCancel();
				}
			}
		}
		
	}
}
