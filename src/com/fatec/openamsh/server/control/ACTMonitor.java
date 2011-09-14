package com.fatec.openamsh.server.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fatec.openamsh.server.model.Check;

public class ACTMonitor extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	public static final String GETCONFIG = "1";
	public static final String PUTLOG = "2";
	public static final String PUTLOGLOT = "3";
	
	private Controller controller;
	
	@Override
	public void init() {
		controller = createController();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.setContentType("application/octet-stream");
		
			InputStream is = request.getInputStream();
			Object[] object = (Object[]) readStream(is);
			
			if (String.valueOf(object[0]).equals(GETCONFIG)) {
				List<String[]> result = convert(controller.getChecks(Integer.parseInt(String.valueOf(object[1]))));
				writeStream(response.getOutputStream(), result);
			} else if (String.valueOf(object[0]).equals(PUTLOG)) {
				controller.insertLog(Integer.parseInt(String.valueOf(object[1])), (Date) object[2], (String) object[3], (String) object[4]);
				writeStream(response.getOutputStream(), createListResult());
			} else if (String.valueOf(object[0]).equals(PUTLOGLOT)) {
				List<Object> logDatas = (List<Object>) object[1];
				for (Object data: logDatas) {
					Object[] obj = (Object[]) data;
					controller.insertLog(Integer.parseInt(String.valueOf(obj[1])), (Date) obj[2], (String) obj[3], (String) obj[4]);
				}
				writeStream(response.getOutputStream(), createListResult());
			}
		} catch (ServletException ex) {
			throw ex;
		} catch (IOException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	}
	
	protected Controller createController() {
		return new Controller();
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
		} finally {
			os.close();
		}
		
		return write;
	}
	
	public List<String[]> convert(List<Check> checks) {
		List<String[]> result = createListResult();
		
		for (Check check: checks) {
			String[] line = new String[5];
			
			line[0] = String.valueOf(check.getId());
			line[1] = check.getPlugin().getFile();
			line[2] = check.getParameter();
			line[3] = String.valueOf(check.getSleep());
			line[4] = String.valueOf(check.getAgent().getSleep());
			
			result.add(line);
		}
		
		return result;
	}
	
	protected List<String[]> createListResult() {
		return new ArrayList<String[]>();
	}
}
