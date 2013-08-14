package com.m6.model.biz;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.m6.model.base.Cmd;
import com.m6.model.base.Flag;
import com.m6.model.base.RequestData;
import com.m6.model.base.ResponseData;
import com.m6.util.NetUtils;

public class BizModel {
	
	private static final String PREFERENCES_NAME = "o2o_preferences";
	
	private static final String PREFERENCES_KEY_STAFFID = "preferences_key_staffid";
	
	public static final String ACTIVITY_RESULT = "result_barcode";
	
	private static final String REQUEST_URL = "http://o.m6fresh.com/m6o2o/ws/app.ashx";

	
	public static String getStaffId(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		return preferences.getString(PREFERENCES_KEY_STAFFID, "");
	}
	
	public static void saveStaffId(Context context, String staffId) {
		SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(PREFERENCES_KEY_STAFFID, staffId);
		editor.commit();
	}
	
	public static void clearStaffId(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(PREFERENCES_KEY_STAFFID, "");
		editor.commit();
	}
	
	public static String login(String account, String password) {
		CLoginInfo cLoginInfo = new CLoginInfo(account, password);
		RequestData requestData = new RequestData(Cmd.DISPATCHER_AUTHEN, cLoginInfo);
		String result = NetUtils.httpPost(REQUEST_URL, requestData.getPostData());
		ResponseData responseData = new ResponseData(result);
		if (responseData.getFlag() == 1) { // success
			return "";
		} else {
			return Flag.getFlagString(responseData.getFlag());
		}
	}
	
	public static String openBox(String staffId, String containerNo, String deliveryNo) {
		DeliveryOpenBoxInfo openBoxInfo = new DeliveryOpenBoxInfo(staffId, containerNo, deliveryNo);
		RequestData requestData = new RequestData(Cmd.UNPACKING, openBoxInfo);
		String result = NetUtils.httpPost(REQUEST_URL, requestData.getPostData());
		ResponseData responseData = new ResponseData(result);
		if (responseData.getFlag() == 1) { // success
			return "";
		} else {
			return Flag.getFlagString(responseData.getFlag());
		}
	}
	
	public static String timeOut(String staffId, String containerNo) {
		DeliveryOpenTimeOutBoxInfo timeOutBoxInfo = new DeliveryOpenTimeOutBoxInfo(staffId, containerNo);
		RequestData requestData = new RequestData(Cmd.OVERTIME, timeOutBoxInfo);
		String result = NetUtils.httpPost(REQUEST_URL, requestData.getPostData());
		ResponseData responseData = new ResponseData(result);
		if (responseData.getFlag() == 1) { // success
			return "";
		} else {
			return Flag.getFlagString(responseData.getFlag());
		}
	}
}
