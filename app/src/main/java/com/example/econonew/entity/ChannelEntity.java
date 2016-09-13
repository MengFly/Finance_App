package com.example.econonew.entity;


import android.text.TextUtils;

import com.example.econonew.activity.BaseActivity;

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
		if (o instanceof ChannelEntity) {
			// 检查是否所有的属性是不是都为空
			boolean isAllNull = this.name == null && this.type == null && this.attribute == null;
			boolean codeIsNull = this.code == null;
			ChannelEntity objEntity = (ChannelEntity) o;
			String thisName = this.name == null ? "" : this.name;
			String thisType = this.type == null ? "" : this.type;
			String thisAttr = this.attribute == null ? "" : this.attribute;
			String thisCode = this.code == null ? "" : this.code;

			String objName = objEntity.name == null ? "" : objEntity.name;
			String objType = objEntity.type == null ? "" : objEntity.type;
			String objAttr = objEntity.attribute == null ? "" : objEntity.attribute;
			String objCode = objEntity.code == null ? "" : objEntity.code;
			// 判断除了code的属性是不是所有的属性都相同
			boolean notCodeIsEquals = thisName.equals(objName) && thisAttr.equals(objAttr) && thisType.equals(objType);
			if (!isAllNull && notCodeIsEquals) {
				return true;
			} else if (!codeIsNull && thisCode.equals(objCode)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
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
		if (code != null) {
			return "code:" + code;
		}
		return name + " " + type + " " + attribute + " ";
	}

}
