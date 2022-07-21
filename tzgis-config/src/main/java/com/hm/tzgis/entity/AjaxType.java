package com.hm.tzgis.entity;

import lombok.Data;

@Data
public class AjaxType {

	private String code;

	private boolean success;

	private String msg;

	private Object data;

	public static AjaxType success(){
		return new AjaxType("200", true, "success","");
	}

	public static AjaxType success(Object Data){
		return new AjaxType("200", true, "success",Data);
	}

	public static AjaxType error(String msg){
		return new AjaxType("500", false, msg, "");
	}

	public static AjaxType error(String msg, Object Data){
		return new AjaxType("500", false, msg, Data);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	@Override
	public String toString() {
		return "AjaxType [code=" + code + ", success=" + success + ", msg=" + msg + ", data=" + data + ", hashCode()="
				+ hashCode() + ", getCode()=" + getCode() + ", isSuccess()=" + isSuccess() + ", getMsg()=" + getMsg()
				+ ", getData()=" + getData() + ", getClass()=" + getClass() + ", toString()=" + super.toString() + "]";
	}
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public AjaxType(String code, boolean success, String msg, Object data) {
		super();
		this.code = code;
		this.success = success;
		this.msg = msg;
		this.data = data;
	}
	
	
	
}
