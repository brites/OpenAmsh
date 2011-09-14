package com.fatec.openamsh.server.persistent;

import java.sql.SQLException;
import java.util.List;

import com.fatec.openamsh.server.model.Agent;
import com.fatec.openamsh.server.model.Check;
import com.fatec.openamsh.server.model.Log;

public interface Persistent {

	List<Check> getChecks(Agent agent) throws SQLException, Exception;
	boolean insertLog(Log log) throws SQLException, Exception;
	List<Log> getLogs(Check check) throws SQLException, Exception;
	List<Log> getLogsSumary() throws SQLException, Exception;
	List<Agent> getLogAgentSumary() throws SQLException, Exception;
}
