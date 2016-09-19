package com.example.econonew.entity;


import android.text.TextUtils;

import com.example.econonew.view.activity.BaseActivity;

public class ChannelEntity {

	private String name;// 频道名称

	private String type; // 频道类型

	private String attribute; // 频道属性

	private String code; // 交易代码

	private int id;// 频道的id。删除频道的时候要用到

	private int businessDomainId;

	private int businessTypeId;

	private int stairId;

	public void setBusinessDomainId(int businessDomainId) {
		this.businessDomainId = businessDomainId;
	}

	public void setBusinessTypeId(int businessTypeId) {
		this.businessTypeId = businessTypeId;
	}

	public void setStairId(int stairId) {
		this.stairId = stairId;
	}

	public int getBusinessDomainId() {
		return businessDomainId;
	}

	public int getBusinessTypeId() {
		return businessTypeId;
	}

	public int getStairId() {
		return stairId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getAttribute() {
		return attribute;
	}

	public String getCode() {
		return code;
	}

	@Override
	public int hashCode() {
		String thisName = this.name == null ? "" : this.name;
		String thisAttr = this.attribute == null ? "" : this.attribute;
		String thisCode = this.code == null ? "" : this.code;
		String thisType = this.type == null ? "" : this.type;
		return thisName.hashCode() + thisType.hashCode() + thisAttr.hashCode() + thisCode.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof ChannelEntity && o.toString().equals(this.toString());
	}

	/**
	 * 判断是否应该添加频道
	 *
	 * @param type 频道的类型
	 * @param attribute 频道的子类型
	 * @param code 频道的代码
	 */
	public boolean isAddChannel(String type, String attribute, String code, BaseActivity context) {
		if (TextUtils.isEmpty(code)) {
			if (!type.equals("请选择") && !attribute.equals("请选择")) {
				return true;
			} else {
				context.showTipDialog(null, "请先选择外汇信息或填写交易代码", null, null);
				return false;
			}
		} else {
			return true;
		}
	}

	@Override
	public String toString() {
		if (!TextUtils.isEmpty(code)) {
			if(TextUtils.isEmpty(code.trim())) {
				return "code:" + code;
			}
		}
		return name + " " + type + " " + attribute + " ";
	}

}
