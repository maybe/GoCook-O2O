package com.m6.o2o;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
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

public class DeliverActivity extends Activity {

	private String mResultContainerNo;
	private String mResultDeliveryNo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deliver);
		
		findViewById(R.id.scan).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent openCameraIntent = new Intent(DeliverActivity.this, CaptureActivity.class);  
                startActivityForResult(openCameraIntent, 0);
			}
		});
		
		findViewById(R.id.exit).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DeliverActivity.this.finish();
			}
		});
		
		final Button unPacking = (Button) findViewById(R.id.unpacking); 
		unPacking.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new OpenBoxTask(DeliverActivity.this, mResultContainerNo, mResultDeliveryNo).execute((Void) null);
				unPacking.setClickable(false);
				unPacking.setEnabled(false);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && data != null) {
			TextView result = (TextView) findViewById(R.id.result);
			mResultContainerNo = data.getCharSequenceExtra(BizModel.ACTIVITY_RESULT).toString();
			result.setText(mResultContainerNo);
			
			result.setVisibility(View.VISIBLE);
			findViewById(R.id.tip).setVisibility(View.VISIBLE);
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
	
	private static class OpenBoxTask extends AsyncTask<Void, Void, String> {

		private WeakReference<DeliverActivity> mActivity;
		private String mContainerNo;
		private String mDeliveryNo;
		
		public OpenBoxTask(DeliverActivity activity, String containerNo, String deliveryNo) {
			mActivity = new WeakReference<DeliverActivity>(activity);
			mContainerNo = containerNo;
			mDeliveryNo = deliveryNo;
		}
		
		@Override
		protected String doInBackground(Void... params) {
			return BizModel.openBox(BizModel.getStaffId(mActivity.get()), mContainerNo, mDeliveryNo);
		}
		
		@Override
		protected void onPostExecute(String result) {
			if (mActivity != null) {
				DeliverActivity activity = mActivity.get();
				if (activity != null) {
					activity.bindData(result);
				}
			}
		}
		
	}
}
