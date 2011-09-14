package com.fatec.openamsh.server.model;

import java.util.Date;

public class Log {

	private Date time;
	private String status;
	private String result;
	private Check check;
	
	public Date getTime() {
		return time;
	}
	
	public void setTime(Date time) {
		this.time = time;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
	public Check getCheck() {
		return check;
	}
	
	public void setCheck(Check check) {
		this.check = check;
	}	
}
