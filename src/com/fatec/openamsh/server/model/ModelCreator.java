package com.fatec.openamsh.server.model;

import java.util.Date;

public class ModelCreator {

	public Agent createAgent() {
		return createAgent(0);
	} 
	
	public Agent createAgent(int id) {
		return createAgent(id, null, null, 0);
	}
	
	public Agent createAgent(int id, String name, String description, int sleep) {
		Agent agent = new Agent();
		agent.setId(id);
		agent.setName(name);
		agent.setDescription(description);
		agent.setSleep(sleep);
		
		return agent;
	}
	
	public Plugin createPlugin() {
		return createPlugin(0);
	}
	
	public Plugin createPlugin(int id) {
		return createPlugin(id, null, null, null);
	}
	
	public Plugin createPlugin(int id, String name, String file, String description) {
		Plugin plugin = new Plugin();
		plugin.setId(id);
		plugin.setName(name);
		plugin.setFile(file);
		plugin.setDescription(description);
		
		return plugin;
	}
	
	public Check createCheck() {
		return createCheck(0);
	}
	
	public Check createCheck(int id) {
		return createCheck(id, null, 0, null, null);
	}
	
	public Check createCheck(int id, String parameter, int sleep, Agent agent, Plugin plugin) {
		Check check = new Check();
		check.setId(id);
		check.setParameter(parameter);
		check.setSleep(sleep);
		check.setAgent(agent);
		check.setPlugin(plugin);
		
		return check;
	}
	
	public Log createLog() {
		return createLog(new Date(), null, null, null);
	}
	
	public Log createLog(Date time, String status, String result, Check check) {
		Log log = new Log();
		log.setTime(time);
		log.setStatus(status);
		log.setResult(result);
		log.setCheck(check);
		
		return log;
	}
}
