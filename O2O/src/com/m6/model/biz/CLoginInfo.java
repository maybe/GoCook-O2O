package com.m6.model.biz;

import org.json.JSONException;
import org.json.JSONObject;

import com.m6.model.base.BaseData;

public class CLoginInfo extends BaseData {

	private String account;
	
	private String password;

	
	public CLoginInfo(String account, String password) {
		this.account = account;
		this.password = password;
	}
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getJsonData() {
		try {
			JSONObject postJsonObject = new JSONObject();
			postJsonObject.put("account", account);
			postJsonObject.put("password", password);
			return postJsonObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
