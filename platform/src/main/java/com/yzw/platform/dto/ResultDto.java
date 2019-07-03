package com.yzw.platform.dto;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * 返回值对象
 */
public class ResultDto implements Serializable {

	private static final long serialVersionUID = -395497119575085711L;

	protected int code; // 返回状态

	protected String message = ""; // 返回信息

	protected String msg = ""; // 返回信息(同message,兼容用)

	protected Object data; // 返回数据

	protected Object value; // 返回值

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
		this.msg = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getMsg() {
		return msg;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
