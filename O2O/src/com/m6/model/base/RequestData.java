package com.m6.model.base;

import org.json.JSONException;
import org.json.JSONObject;


public class RequestData {

	private static final int AppId = 1;
	
	private static final String Key = "DAB578EC-6C01-4180-939A-37E6BE8A81AF";
	
	private static final String IV = "117A5C0F-7036-476f-B789-01BBA998D0CF";
	
	private int cmd;
	
	private String data;
	
	private String md5;
	

	public RequestData(int cmd, String data) {
		this.cmd = cmd;
		this.data = data;
		this.md5 = md5Encry();
	}

	public int getCmd() {
		return cmd;
	}

	public void setCmd(int cmd) {
		this.cmd = cmd;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}
	
	public String md5Encry() {
		return null;
	}
	
	public String getPostData() {
		try {
			JSONObject postData = new JSONObject();
			postData.put("cmd", cmd);
			postData.put("data", data);
			postData.put("md5", md5);
			return postData.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
