package com.m6.model.biz;

import org.json.JSONException;
import org.json.JSONObject;

import com.m6.model.base.BaseData;

public class DeliveryOpenBoxInfo extends BaseData {

	private String staffId;
	
	private String containerNo;
	
	private String deliveryNo;

	public DeliveryOpenBoxInfo(String staffId, String containerNo, String deliveryNo) {
		this.staffId = staffId;
		this.containerNo = containerNo;
		this.deliveryNo = deliveryNo;
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

	public String getDeliveryNo() {
		return deliveryNo;
	}

	public void setDeliveryNo(String deliveryNo) {
		this.deliveryNo = deliveryNo;
	}

	@Override
	public String getJsonData() {
		try {
			JSONObject postJsonObject = new JSONObject();
			postJsonObject.put("staffid", staffId);
			postJsonObject.put("containerno", containerNo);
			postJsonObject.put("deliveryno", deliveryNo);
			return postJsonObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
