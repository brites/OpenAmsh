package com.fatec.openamsh.server.control;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fatec.openamsh.server.model.Agent;
import com.fatec.openamsh.server.model.Check;
import com.fatec.openamsh.server.model.Log;
import com.fatec.openamsh.server.model.ModelCreator;
import com.fatec.openamsh.server.persistent.PersistentFacade;
import com.fatec.openamsh.server.view.ViewFacade;

public class Controller {
	
	private ModelCreator modelCreator;
	private ViewFacade viewFacade;
	private PersistentFacade persistentFacade;
	
	public Controller() {
		try {
			viewFacade = createViewFacade();
			modelCreator = createModel();
			persistentFacade = createPersistentFacade(modelCreator);
		} catch (Exception ex) {
			error(ex.getMessage());
		}
	}
	
	protected ModelCreator createModel() {
		return new ModelCreator();
	}
	
	protected ViewFacade createViewFacade() {
		return new ViewFacade();
	}
	
	protected PersistentFacade createPersistentFacade(ModelCreator modelCreator) throws Exception {
		return new PersistentFacade(modelCreator);
	}
	
	protected void error(String message) {
		if (viewFacade != null) {
			viewFacade.message(getTime() + " " + message);
		}
	}
	
	protected String getTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");
		return "(" + sdf.format(new Date()) + ")";
	}

	public List<Check> getChecks(int idagent) throws Exception {
		try {
			Agent agent = modelCreator.createAgent(idagent);
		
			return persistentFacade.getChecks(agent);
		} catch (Exception ex) {
			error(ex.getMessage());
		}
		
		return null;
	}
	
	public void insertLog(int idcheck, Date time, String status, String result) {
		try {
			Check check = modelCreator.createCheck(idcheck);
			Log log = modelCreator.createLog(time, status, result, check);
			persistentFacade.insertLog(log);
		} catch (Exception ex) {
			error(ex.getMessage());
		}
	}
	
	public List<Log> getLogs(int idcheck) throws Exception {
		try {
			Check check = modelCreator.createCheck(idcheck);
		
			return persistentFacade.getLogs(check);
		} catch (Exception ex) {
			error(ex.getMessage());
		}
		
		return null;
	}
	
	public List<Log> getLogsSumary() throws Exception {
		try {
			return persistentFacade.getLogsSumary();
		} catch (Exception ex) {
			error(ex.getMessage());
		}
		
		return null;
	}
	
	public List<Agent> getLogAgentSumary() throws Exception {
		try {
			return persistentFacade.getLogAgentSumary();
		} catch (Exception ex) {
			error(ex.getMessage());
		}
		
		return null;
	}
}
