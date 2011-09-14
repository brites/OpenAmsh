package com.fatec.openamsh.server.model;

public class Agent {

	private int id;
	private String name;
	private String description;
	private long sleep;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public long getSleep() {
		return sleep;
	}
	
	public void setSleep(long sleep) {
		this.sleep = sleep;
	}
}
