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

public class DeliverActivity extends BaseActivity {

	private String mResultContainerNo;
	private String mResultDeliveryNo;
	
	private static final int RESULT_ORDER = 0;
	private static final int RESULT_BOX = 1;
	
	private OpenBoxTask mOpenBoxTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deliver);
		setTitle(R.string.activity_title_open_box);
		
		findViewById(R.id.scan_order).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent openCameraIntent = new Intent(DeliverActivity.this, CaptureActivity.class);  
                startActivityForResult(openCameraIntent, RESULT_ORDER);
			}
		});
		
		findViewById(R.id.scan_box).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent openCameraIntent = new Intent(DeliverActivity.this, CaptureActivity.class);  
                startActivityForResult(openCameraIntent, RESULT_BOX);
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
//				new OpenBoxTask(DeliverActivity.this, mResultContainerNo, mResultDeliveryNo).execute((Void) null);
				// TODO  Test
				Editable boxNo = ((EditText) findViewById(R.id.input_no_box)).getText();
				Editable deliveryNo = ((EditText) findViewById(R.id.input_no)).getText();
				if (boxNo != null && deliveryNo != null) {
					String deliveryNoText = deliveryNo.toString();
					String boxNoText = boxNo.toString();
					if (TextUtils.isEmpty(deliveryNoText)) {
						Toast.makeText(DeliverActivity.this, R.string.toast_deliveryno_empty, Toast.LENGTH_LONG).show();
						return;
					}
					
					if (TextUtils.isEmpty(boxNoText)) {
						Toast.makeText(DeliverActivity.this, R.string.toast_boxno_empty, Toast.LENGTH_LONG).show();
						return;
					}
					
					if (mOpenBoxTask == null) {
						showProgressDialog(R.string.open_box_message);
						mOpenBoxTask = new OpenBoxTask(DeliverActivity.this, boxNoText, deliveryNoText);
						mOpenBoxTask.execute((Void) null);
					}
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && data != null) {
			if (requestCode == RESULT_ORDER) {
				mResultDeliveryNo = data.getCharSequenceExtra(BizModel.ACTIVITY_RESULT).toString();
				((EditText) findViewById(R.id.input_no)).setText(mResultDeliveryNo);
//				TextView result = (TextView) findViewById(R.id.result_order);
//				result.setText(mResultDeliveryNo);
//				result.setVisibility(View.VISIBLE);
//				findViewById(R.id.tip_order).setVisibility(View.VISIBLE);
			} else if (requestCode == RESULT_BOX) {
				mResultContainerNo = data.getCharSequenceExtra(BizModel.ACTIVITY_RESULT).toString();
				((EditText) findViewById(R.id.input_no_box)).setText(mResultContainerNo);
//				TextView result = (TextView) findViewById(R.id.result_box);
//				result.setText(mResultContainerNo);
//				result.setVisibility(View.VISIBLE);
//				findViewById(R.id.tip_box).setVisibility(View.VISIBLE);
			}
		}
	}
	
	private void bindData(ResponseData responseData) {
		mOpenBoxTask = null;
		dismissProgressDialog();
		if (responseData.getFlag() == 1) { // success
			Toast.makeText(this, R.string.openbox_success, Toast.LENGTH_SHORT).show();
		} else {
			TextView error = (TextView) findViewById(R.id.error);
			error.setText(responseData.getMsg());
			
			error.setVisibility(View.VISIBLE);
			findViewById(R.id.tip_error).setVisibility(View.VISIBLE);
		}
	}
	
	private void onCancel() {
		mOpenBoxTask = null;
		dismissProgressDialog();
	}
	
	private static class OpenBoxTask extends AsyncTask<Void, Void, ResponseData> {

		private WeakReference<DeliverActivity> mActivity;
		private String mContainerNo;
		private String mDeliveryNo;
		
		public OpenBoxTask(DeliverActivity activity, String containerNo, String deliveryNo) {
			mActivity = new WeakReference<DeliverActivity>(activity);
			mContainerNo = containerNo;
			mDeliveryNo = deliveryNo;
		}
		
		@Override
		protected ResponseData doInBackground(Void... params) {
			return BizModel.openBox(BizModel.getStaffId(mActivity.get()), mContainerNo, mDeliveryNo);
		}
		
		@Override
		protected void onPostExecute(ResponseData result) {
			if (mActivity != null) {
				DeliverActivity activity = mActivity.get();
				if (activity != null) {
					activity.bindData(result);
				}
			}
		}
		
		@Override
		protected void onCancelled() {
			super.onCancelled();
			if (mActivity != null) {
				DeliverActivity activity = mActivity.get();
				if (activity != null) {
					activity.onCancel();
				}
			}
		}
		
	}
}
