package com.fatec.openamsh.server.persistent;

import java.util.List;

import com.fatec.openamsh.server.model.Agent;
import com.fatec.openamsh.server.model.Check;
import com.fatec.openamsh.server.model.Log;
import com.fatec.openamsh.server.model.ModelCreator;
import com.fatec.openamsh.server.persistent.db.PersistentDB;

public class PersistentFacade {
	
	private Persistent persistent;
	
	public PersistentFacade(ModelCreator modelCreator) throws Exception {
		persistent = createPersistent(modelCreator);
	}
	
	public Persistent createPersistent(ModelCreator modelCreator) throws Exception {
		return new PersistentDB(modelCreator);
	}

	public List<Check> getChecks(Agent agent) throws Exception {
		return persistent.getChecks(agent);
	}
	
	public void insertLog(Log log) throws Exception {
		persistent.insertLog(log);
	}
	
	public List<Log> getLogs(Check check) throws Exception {
		return persistent.getLogs(check);
	}
	
	public List<Log> getLogsSumary() throws Exception {
		return persistent.getLogsSumary();
	}
	
	public List<Agent> getLogAgentSumary() throws Exception {
		return persistent.getLogAgentSumary();
	}
}
