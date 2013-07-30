package com.m6.o2o;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.client.android.CaptureActivity;
import com.m6.model.biz.BizModel;

public class OvertimeActivity extends Activity {

	private String mResultContainerNo;
	
	private static final int RESULT_BOX = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_overtime);
		
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
				new TimeOutTask(OvertimeActivity.this, mResultContainerNo).execute((Void) null);
				timeout.setClickable(false);
				timeout.setEnabled(false);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && data != null) {
			if (requestCode == RESULT_BOX) {
				TextView result = (TextView) findViewById(R.id.result_box);
				mResultContainerNo = data.getCharSequenceExtra(BizModel.ACTIVITY_RESULT).toString();
				result.setText(mResultContainerNo);
				
				result.setVisibility(View.VISIBLE);
				findViewById(R.id.tip_box).setVisibility(View.VISIBLE);
			}
		}
	}
	
	private void bindData(String resultInfo) {
		if (!TextUtils.isEmpty(resultInfo)) {
			TextView error = (TextView) findViewById(R.id.error);
			error.setText(resultInfo);
			
			error.setVisibility(View.VISIBLE);
			findViewById(R.id.tip_error).setVisibility(View.VISIBLE);
		}
	}
	
	private static class TimeOutTask extends AsyncTask<Void, Void, String> {

		private WeakReference<OvertimeActivity> mActivity;
		private String mContainerNo;
		
		public TimeOutTask(OvertimeActivity activity, String containerNo) {
			mActivity = new WeakReference<OvertimeActivity>(activity);
			mContainerNo = containerNo;
		}
		
		@Override
		protected String doInBackground(Void... params) {
			return BizModel.timeOut(BizModel.getStaffId(mActivity.get()), mContainerNo);
		}
		
		@Override
		protected void onPostExecute(String result) {
			if (mActivity != null) {
				OvertimeActivity activity = mActivity.get();
				if (activity != null) {
					activity.bindData(result);
				}
			}
		}
		
	}
}
