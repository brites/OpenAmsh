package com.fatec.openamsh.agent;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgentManager implements Runnable {
	
	private int idagent;
	private String serverip;
	private String basedir;
	private String logfile;
	private Thread thread;
	private boolean stop;
	private long sleep;
	private Map<Integer, AgentExec> agentsExec;
	
	public AgentManager(int idagent, String serverip, String basedir, String logfile) {
		this.idagent = idagent;
		this.serverip = serverip;
		this.basedir = basedir;
		this.logfile = logfile;
		this.agentsExec = createAgentsExecMap();
	}
	
	protected Map<Integer, AgentExec> createAgentsExecMap() {
		return new HashMap<Integer, AgentExec>();
	}
	
	public String getServerIp() {
		return serverip;
	}
	
	public void init() {
		thread = new Thread(this);
		thread.start();
	}

	public void run() {
		while (!stop) {
			try {
				Object data = new Object[] {"1", String.valueOf(idagent)};
				Object result = null;
			
				String u = getProtocol() + serverip + getPort() + getContext() + getServletName();
				URL url = new URL(u);
				URLConnection urlcon = url.openConnection();
				urlcon.setDoOutput(true);
				urlcon.setDoInput(true);
			
				OutputStream os = urlcon.getOutputStream();
				try {
					if (writeStream(os, data)) {
						InputStream is = urlcon.getInputStream();
						try {
							result = readStream(is);
							if (!configure(result)) {
								stop = true;
							}
						} finally {
							is.close();
						}
					}
				} finally {
					os.close();
				}
			} catch (ConnectException ex) {
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			try {
				Thread.sleep(sleep * 1000);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		
		thread.interrupt();
	}
	
	protected boolean configure(Object result) {
		if (result instanceof List) {
			List<String[]> list = (List<String[]>) result;
			if (!list.isEmpty()) {
				Date time = new Date();
				
				for (String[] line: list) {
					boolean newAgentExec = false;
					Integer idcheck = new Integer(line[0]);
					AgentExec agentExec = agentsExec.get(idcheck);
					if (agentExec == null) {
						agentExec = new AgentExec(this);
						newAgentExec = true;
						agentsExec.put(idcheck, agentExec);
					}
					
					agentExec.setBasedir(basedir);
					agentExec.setLogfile(logfile);
					agentExec.setIdcheck(idcheck);
					agentExec.setFilename(line[1]);
					agentExec.setParameter(line[2]);
					agentExec.setSleep(Long.parseLong(line[3]));
					agentExec.setTime(time);
					sleep = Long.parseLong(line[4]);
					
					if (newAgentExec) {
						agentExec.start();
					}
				}
				
				List<AgentExec> agentsToRemove = new ArrayList<AgentExec>();
				Collection<AgentExec> agents = agentsExec.values();
				for (AgentExec agent: agents) {
					if (!agent.getTime().equals(time)) {
						agent.destroy();
						agentsToRemove.add(agent);
					}
				}
				
				for (AgentExec agent: agentsToRemove) {
					agentsExec.remove(agent);
				}
				agentsToRemove.clear();
				agentsToRemove = null;
				
				return true;
			}
		}
		
		return false;
	}
	
	protected String getProtocol() {
		return "http://";
	}
	
	protected String getPort() {
		return "";
	}
	
	protected String getContext() {
		return "/OpenAmsh";
	}
	
	protected String getServletName() {
		return "/ACTMonitor";
	}
	
	/**
	 * Retorna o conteúdo da stream de entrada passada no argumento is.
	 */
	protected Object readStream(InputStream is) throws ClassNotFoundException,IOException, Exception {
		Object object = null;
		
		ObjectInputStream in = new ObjectInputStream(is);
		
		try {
			object = in.readObject();
		} finally {
			in.close();
		}
		
		return object;
	}
	
	/**
	 * Serializa o objeto passado no argumento object para a stream de saída
	 * passada no argumento out.
	 */
	protected boolean writeStream(OutputStream out, Object object)throws IOException, Exception {
		boolean write = false;

		ObjectOutputStream os = new ObjectOutputStream(out);
		
		try {
			os.writeObject(object);
			os.flush();
			
			write = true;
		} finally {
			os.close();
		}
		
		return write;
	}
	
	public void print(Object result) {
		List<String[]> res = (List<String[]>) result;
		
		for (String[] r: res) {
			System.out.println();
			for (String d: r) {
				System.out.print(d + "-");
			}
		}
	}
}
