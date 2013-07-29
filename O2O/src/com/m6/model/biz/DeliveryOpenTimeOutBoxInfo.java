package com.m6.model.biz;

import org.json.JSONException;
import org.json.JSONObject;

import com.m6.model.base.BaseData;

public class DeliveryOpenTimeOutBoxInfo extends BaseData {

	private String staffId;
	
	private String containerNo;

	
	public DeliveryOpenTimeOutBoxInfo(String staffId, String containerNo) {
		this.staffId = staffId;
		this.containerNo = containerNo;
	}
	
	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	@Override
	public String getJsonData() {
		try {
			JSONObject postJsonObject = new JSONObject();
			postJsonObject.put("staffid", staffId);
			postJsonObject.put("containerno", containerNo);
			return postJsonObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
