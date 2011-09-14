package com.fatec.openamsh.agent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class AgentMain {

	public void init() throws FileNotFoundException, IOException, Exception {
		String[] conf = getConf();
		
		if (conf != null && conf.length == 4) {
			new AgentManager(Integer.parseInt(conf[0]), conf[1], conf[2], conf[3]).init();
		} else {
			throw new Exception("Algum erro ocorreu!");
		}
	}
	
	protected String getBaseDir() {
		//return "../..";
		return "";
	}
	
	protected String[] getConf() throws FileNotFoundException, IOException {
		File file = new File(getBaseDir() + "/etc/openamsh.conf");
		
		if (!file.exists()) {
			throw new FileNotFoundException("File \'" + file.toString() + "\' does not exists!");
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String line = br.readLine();
		StringTokenizer st = new StringTokenizer(line, "|");
		String[] conf = new String[st.countTokens()];
		int count = 0;
		while (st.hasMoreElements()) {
			conf[count++] = (String) st.nextElement();
		}
		
		return conf;
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException, Exception {
		new AgentMain().init();
	}
}
