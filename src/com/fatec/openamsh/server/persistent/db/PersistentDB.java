package com.fatec.openamsh.server.persistent.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.fatec.openamsh.server.model.Agent;
import com.fatec.openamsh.server.model.Check;
import com.fatec.openamsh.server.model.Log;
import com.fatec.openamsh.server.model.ModelCreator;
import com.fatec.openamsh.server.model.Plugin;
import com.fatec.openamsh.server.persistent.Persistent;

public class PersistentDB implements Persistent {
	
	private Connection connection;	
	private ModelCreator modelCreator;
	
	public PersistentDB(ModelCreator modelCreator) throws ClassNotFoundException, SQLException {
		this.modelCreator = modelCreator;
		
		String driver = "org.postgresql.Driver";
		String url = "jdbc:postgresql://localhost:5432/openamsh";
		String user = "openamsh";
		String pass = "openamsh";
		
		Class.forName(driver);
		connection = DriverManager.getConnection(url, user, pass);
	}
	
	public void destroy() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
		}
	}
	
	protected boolean close(Statement obj) throws SQLException {
		if (obj != null && !obj.isClosed()) {
			obj.close();
			
			return true;
		}
		
		return false;
	}
	
	protected boolean close(ResultSet obj) throws SQLException {
		if (obj != null && !obj.isClosed()) {
			obj.close();
			
			return true;
		}
		
		return false;
	}
	
	protected List<Check> createChecksList() {
		return new ArrayList<Check>();
	}
	
	protected List<Log> createLogsList() {
		return new ArrayList<Log>();
	}
	
	protected List<Agent> createAgentsList() {
		return new ArrayList<Agent>();
	}
		
	public List<Check> getChecks(Agent agent) throws SQLException, Exception {
		List<Check> checks = createChecksList();
		
		String sql = "SELECT idchecks,fileplugin,parameterchecks,sleepchecks,sleepagent" 
			+ " FROM viewchecks"
			+ " WHERE idagent=" + agent.getId()
			+ " ORDER BY idchecks";
		
		Statement sta = null;
		ResultSet res = null;
		
		try {
			sta = connection.createStatement();
			res = sta.executeQuery(sql);
			while (res.next())  {
				Agent ag = modelCreator.createAgent();
				ag.setSleep(res.getLong("sleepagent"));
				
				Plugin pl = modelCreator.createPlugin();
				pl.setFile(res.getString("fileplugin"));
				
				Check check = modelCreator.createCheck();
				check.setId(res.getInt("idchecks"));
				check.setParameter(res.getString("parameterchecks"));
				check.setSleep(res.getLong("sleepchecks"));
				
				check.setAgent(ag);
				check.setPlugin(pl);
				
				checks.add(check);
			}
		} finally {
			close(res);
			close(sta);
		}
		
		return checks;
	}

	public boolean insertLog(Log log) throws SQLException, Exception {
		String sql = "INSERT INTO logchecks (idcheck,time,status,result)"
			+ " VALUES(" + log.getCheck().getId() + ",'" + new Timestamp(log.getTime().getTime()) + "','" + log.getStatus() + "','" + log.getResult() + "')";
		
		//System.out.println("@@@ " + new Timestamp(log.getTime().getTime()));
		
		Statement sta = null;
		
		try {
			sta = connection.createStatement();
			
			return sta.executeUpdate(sql) > 0; 
		} finally {
			close(sta);
		}
	}	
	
	public List<Log> getLogs(Check check) throws SQLException, Exception{
		List<Log> logs = createLogsList();
		
		String sql = "SELECT agent.name as name_agent, checks.name , logchecks.time, logchecks.status, logchecks.result"
			 	+ " FROM logchecks, checks, agent"
			 	+ " WHERE checks.id=" + check.getId()
				+ " AND logchecks.idcheck=checks.id"
				+ " AND checks.id_agent = agent.id"
				+ " ORDER BY time DESC LIMIT 2880";
			
		Statement sta = null;
		ResultSet res = null;
		
		try {
			sta = connection.createStatement();
			res = sta.executeQuery(sql);
			while (res.next())  {
				Log log = modelCreator.createLog();
				log.setCheck(check);
				
				Agent agent = modelCreator.createAgent();
				check.setAgent(agent);
				
				check.setName(res.getString("name"));
				agent.setName(res.getString("name_agent"));
				log.setResult(res.getString("result"));
				log.setTime(res.getTimestamp("time"));
				log.setStatus(res.getString("status"));
				
				logs.add(log);
			}
		} finally {
			close(res);
			close(sta);
		}
		
		return logs;
	}
	
	public List<Log> getLogsSumary() throws SQLException, Exception{
		List<Log> logsSumary = createLogsList();
		
		String sql = "SELECT * FROM obter_logs() ORDER BY namecheck";
			
		Statement sta = null;
		ResultSet res = null;
		
		try {
			sta = connection.createStatement();
			res = sta.executeQuery(sql);
			while (res.next())  {
				Check check = modelCreator.createCheck();
				check.setId(res.getInt("idcheck"));
				
				Log log = modelCreator.createLog();
				log.setCheck(check);
				
				Agent agent = modelCreator.createAgent();
				check.setAgent(agent);
				
				agent.setName(res.getString("nameagent"));
				check.setName(res.getString("namecheck"));
				log.setTime(res.getTimestamp("time"));
				log.setStatus(res.getString("status"));
				
				logsSumary.add(log);
			}
		} finally {
			close(res);
			close(sta);
		}
		
		return logsSumary;
	}
	
	public List<Agent> getLogAgentSumary() throws SQLException, Exception{
		List<Agent> logAgentSumary = createAgentsList();
		
		String sql = "SELECT nameagent FROM obter_logs() GROUP BY nameagent ORDER BY nameagent;";
			
		Statement sta = null;
		ResultSet res = null;
		
		try {
			sta = connection.createStatement();
			res = sta.executeQuery(sql);
			while (res.next())  {

				Agent agent = modelCreator.createAgent();
								
				agent.setName(res.getString("nameagent"));

				logAgentSumary.add(agent);
			}
		} finally {
			close(res);
			close(sta);
		}
		
		return logAgentSumary;
	}
}
