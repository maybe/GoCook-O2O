package com.m6.o2o;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.client.android.CaptureActivity;

public class DeliverActivity extends Activity {

	private String mResultCode;
	
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
				new UnpackingTask(DeliverActivity.this).execute((Void) null);
				unPacking.setClickable(false);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && data != null) {
			mResultCode = data.getCharSequenceExtra(BizModel.ACTIVITY_RESULT).toString();
			((TextView) findViewById(R.id.result)).setText(mResultCode);
		}
	}
	
	private void bindData() {
		
	}
	
	private static class UnpackingTask extends AsyncTask<Void, Void, Void> {

		private WeakReference<DeliverActivity> mActivity;
		public UnpackingTask(DeliverActivity activity) {
			mActivity = new WeakReference<DeliverActivity>(activity);
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			if (mActivity != null) {
				DeliverActivity activity = mActivity.get();
				if (activity != null) {
					activity.bindData();
				}
			}
		}
		
	}
}
