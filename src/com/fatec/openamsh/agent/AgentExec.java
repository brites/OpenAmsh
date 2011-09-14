package com.fatec.openamsh.agent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class AgentExec extends Thread {

	public static final String fileToken = "¢";
	
	private int idcheck;
	private String basedir;
	private String logfile;
	private String filename;
	private String parameter;
	private long sleep;
	private Date time;
	private boolean stop;
	private AgentManager agentManager;
	
	public AgentExec(AgentManager agentManager) {
		this.agentManager = agentManager;
	}
	
	public int getIdcheck() {
		return idcheck;
	}
	
	public void setIdcheck(int idcheck) {
		this.idcheck = idcheck;
	}
	
	public String getBasedir() {
		return basedir;
	}
	
	public void setBasedir(String basedir) {
		this.basedir = basedir;
	}
	
	public String getLogfile() {
		return logfile;
	}
	
	public void setLogfile(String logfile) {
		this.logfile = logfile;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public String getParameter() {
		return parameter;
	}
	
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	
	public long getSleep() {
		return sleep;
	}
	
	public void setSleep(long sleep) {
		this.sleep = sleep;
	}
	
	public Date getTime() {
		return time;
	}
	
	public void setTime(Date time) {
		this.time = time;
	}
	
	@Override
	public void run() {
		while (!stop) {
			try {
				String command = getBasedir() + "/" + getFilename() + " " + getParameter();
				Runtime runtime = Runtime.getRuntime();
				Process process = runtime.exec(command);
				InputStream is = process.getInputStream();
				
				String str = "";
				int r;
				while ((r=is.read()) > -1) {
					if (r == 10) {
						break;
					}
					str += (char) r;
				}
				if (!str.isEmpty()) {
					System.out.println(str);
				}
				
				int index1 = str.indexOf("-");
				int index2 = str.indexOf(":");
				int index = -1;
				if (index1 > -1 && index2 > -1) {
					if (index1 < index2) {
						index = index1;
					} else {
						index = index2;
					}
				} else if (index1 > -1) {
					index = index1;
				} else if (index2 > -1) {
					index = index2;
				}
								
				if (index > -1) { 
					String status = str.substring(0 , index);
					String result = str.substring(index + 1).trim();
					log(status, result);
					
					sendLogFile();
				}
			} catch (ConnectException ex) {
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			try {
				Thread.sleep(getSleep() * 1000);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		
		interrupt();
	}
	
	public Object log(String status, String str) throws Exception {
		return log("2", String.valueOf(getIdcheck()), new Date(), status, str);
	}
	
	public Object log(String type, String idcheck, Date date, String status, String str) throws Exception {
		return log(new Object[] {type, idcheck, date, status, str});
	}
	
	public Object log(Object data) throws Exception {
		Object result = null;
		boolean sent = false;
	
		try {
			String u = agentManager.getProtocol() + agentManager.getServerIp() + agentManager.getPort() + agentManager.getContext() + agentManager.getServletName();
			URL url = new URL(u);
			URLConnection urlcon = url.openConnection();
			urlcon.setDoOutput(true);
			urlcon.setDoInput(true);
	
			OutputStream os = urlcon.getOutputStream();
			try {
				if (agentManager.writeStream(os, data)) {
					sent = true;
				
					InputStream is = urlcon.getInputStream();
					try {
						result = agentManager.readStream(is);
					} finally {
						is.close();
					}
				}
			} finally {
				os.close();
			}
		} finally {
			if (!sent) {
				saveLog(data);
			}
		}
		
		return result;
	}
	
	public void destroy() {
		stop = true;
	}
	
	protected boolean saveLog(Object data) {
		boolean saved = false;
		
		System.out.print("Gravando resultado do monitoramento em \"" + logfile + "\"...");
		
		try {
			File file = new File(logfile);
			if (!file.exists()) {
				file.createNewFile();
			}
			
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
			try {
				Object[] d = (Object[]) data;
				String type = (String) d[0];
				String idcheck = (String) d[1];
				Date date = (Date) d[2];
				String status = (String) d[3];
				String result = (String) d[4];
				
				String str = type + fileToken + idcheck + fileToken + date.getTime() + fileToken + status + fileToken + result + "\n";
				
				bw.write(str, 0, str.length());
				
				saved = true;
				
			} finally {
				bw.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		if ( saved ){
			System.out.println("OK");
		} else {
			System.out.println("ERRO");
		}
			
		
		return saved;
	}
	
	protected List<Object> getLogDatas() {
		List<Object> logDatas = new ArrayList<Object>();
		
		try {
			File file = new File(logfile);
			if (file.exists()) {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
				try {
					String line;
					while ((line=br.readLine()) != null) {
						StringTokenizer st = new StringTokenizer(line, fileToken);
						
						String type = (String) st.nextElement();
						String idcheck = (String) st.nextElement();
						Date time = new Date(Long.parseLong(((String) st.nextElement())));
						String status = (String) st.nextElement();
						String result = (String) st.nextElement();
					
						logDatas.add(new Object[] {type, idcheck, time, status, result});
					}
				} finally {
					br.close();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return logDatas;
	}
	
	protected boolean removeLogFile() {
		File file = new File(logfile);
		
		return file.delete();
	}
	
	protected void sendLogFile() throws Exception {
		// enviando os dados gravados no log
		List<Object> logDatas = getLogDatas();
		if (logDatas != null && !logDatas.isEmpty()) {
			Object res = log(new Object[] {"3", logDatas});
			if (res != null) {
				removeLogFile();
			}
		}
	}
}
