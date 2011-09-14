package com.fatec.openamsh.server.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fatec.openamsh.server.control.Controller;
import com.fatec.openamsh.server.model.Agent;
import com.fatec.openamsh.server.model.Check;
import com.fatec.openamsh.server.model.Log;

/**
 * Servlet implementation class ACTReport
 */
public class ACTReport extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Controller controller;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ACTReport() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			
			String pidcheck = request.getParameter("idcheck");
			
			Controller controller = new Controller();
			
			if (pidcheck != null) {
				int idcheck = Integer.parseInt(pidcheck);
				
				List<Log> result = controller.getLogs(idcheck);
				
				 response.setContentType("text/html");
				       PrintWriter out = response.getWriter();
				       Check check = result.get(0).getCheck();
				       
				       out.print("<html>" +
				       			"<head>" +
								"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">" +
								/*"<META HTTP-EQUIV=\"REFRESH\" CONTENT=\"10; url=/OpenAmsh/ACTReport?idcheck=" + idcheck + "\">" +*/
						           "<link rel=\"STYLESHEET\" type=\"text/css\" href=\"/OpenAmsh/js/dhtmlx/dhtmlxgrid.css\"/> " +
						           "<link rel=\"stylesheet\" type=\"text/css\" href=\"/OpenAmsh/js/dhtmlx/skins/dhtmlxgrid_dhx_skyblue.css\"/> " +
						           "<link rel=\"stylesheet\" type=\"text/css\" href=\"/OpenAmsh/js/dhtmlx/skins/dhtmlxaccordion_dhx_skyblue.css\"> " +

						           "<script type=\"text/javascript\" src=\"/OpenAmsh/js/dhtmlx/dhtmlxaccordion.js\"></script> " +
						           "<script type=\"text/javascript\" src=\"/OpenAmsh/js/dhtmlx/dhtmlxcontainer.js\"></script> " +
						           "<script src=\"/OpenAmsh/js/dhtmlx/dhtmlxGrid/dhtmlxcommon.js\"></script> " +
						           "<script src=\"/OpenAmsh/js/dhtmlx/dhtmlxGrid/dhtmlxgrid.js\"></script> " +
						           "<script src=\"/OpenAmsh/js/dhtmlx/dhtmlxGrid/dhtmlxgridcell.js\"></script> " +
						           "<script src=\"/OpenAmsh/js/dhtmlx/dhtmlxGrid/dhtmlxgrid_start.js\"></script> " +
						           
								    "<script>" +
								    "function criaTabela() {" +
								        "dhtmlx.skin = \"dhx_skyblue\";" +
									"myGrid1 = new dhtmlXGridFromTable('tblToGrid1');" +
									"myGrid2 = new dhtmlXGridFromTable('tblToGrid2');" +
									"myGrid3 = new dhtmlXGridFromTable('tblToGrid3');" +
								
									"for(li = 1; li <= myGrid1.getRowsNum(); li++){" +
								
									  "if(myGrid1.cells(li,1).getValue().indexOf(\"OK\") != -1 ){" +
									  "myGrid1.setCellTextStyle(li,1,\"background-color:#35F251; color:black; font-weight:bold;\");" +
									  "}" +
									  "if(myGrid1.cells(li,1).getValue().indexOf(\"CRITICAL\") != -1 ){" +
									  "myGrid1.setCellTextStyle(li,1,\"background-color:#FF0000; color:black; font-weight:bold;\");" +
									  "}" +
									  "if(myGrid1.cells(li,1).getValue().indexOf(\"UNKNOWN\") != -1 ){" +
									  "myGrid1.setCellTextStyle(li,1,\"background-color:#FF0000; color:black; font-weight:bold;\");" +
									  "}" +
									  "if(myGrid1.cells(li,1).getValue().indexOf(\"WARNING\") != -1 ){" +
									  "myGrid1.setCellTextStyle(li,1,\"background-color:#FFFF00; color:black; font-weight:bold;\");" +
									  "}" +
									 "}" +	
									 
									 "myGrid1.enableLightMouseNavigation(false);" +
									 "myGrid2.enableLightMouseNavigation(false);" +
									 "myGrid3.enableLightMouseNavigation(false);" +
								     "}" +
								
								     "function createUI() {" +
									"criaTabela();" +
								     "}" +
								    "</script>" +    
								  
								    
								"</head>" +
								"<body style=\"margin:0px; text-align:center;\" onload=\"createUI();\">" +
								"<div style=\"text-align:left; margin-left:auto; margin-right:auto;\">"+
								"<table id=\"tblToGrid2\" name=\"grid2\" gridHeight=\"auto\" style=\"width:959px;\" imgpath=\"/tcc/codebase/imgs/\" border=\"1\" lightnavigation=\"true\">" +
									"<tr>" +
									"<td type=\"ro\"><b>Host: " + check.getAgent().getName() + "</b></td>" +
									"</tr>" +  
								"</table>" +
								"<table id=\"tblToGrid3\" name=\"grid3\" gridHeight=\"auto\" style=\"width:959px\" imgpath=\"/tcc/codebase/imgs/\" border=\"1\" lightnavigation=\"true\">" +
									"<tr>" +
									"<td type=\"ro\">Verificação: " + check.getName() + "</td>" +
									"</tr>" +  
								"</table>" +
								"<table id=\"tblToGrid1\" name=\"grid1\" gridHeight=\"auto\" style=\"width:959px\" imgpath=\"/tcc/codebase/imgs/\" border=\"1\" lightnavigation=\"true\">" +
									"<tr>" +
									"<td type=\"ro\">Horario</td>" +
									"<td type=\"ro\">Status</td>" +
									"<td type=\"ro\">Resultado</td>" +
									"</tr>");
									for ( Log log : result ) {
										out.print(
									"<tr>" +
									"<td>" + log.getTime()  + "</td>" +
									"<td>" + log.getStatus() + "</td>" +
									"<td>" + log.getResult() + "</td>" +
									"</tr>");
									}
									out.print(
								"</table>" +
								"</div>" +
								"</body>" +
								"</html>");
						       out.close();
			

			
			} else {
			
				List<Log> result = controller.getLogsSumary();
				List<Agent> agents = controller.getLogAgentSumary();
				
				PrintWriter out = response.getWriter();
				response.setContentType("text/html");       
				if (!result.isEmpty()) {
					  
				       out.print("<html> " +
				       "<head> " +
				       "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"> " +
				       /*"<META HTTP-EQUIV=\"REFRESH\" CONTENT=\"10; url=/OpenAmsh/ACTReport\">" +*/
				           "<link rel=\"STYLESHEET\" type=\"text/css\" href=\"/OpenAmsh/js/dhtmlx/dhtmlxgrid.css\"/> " +
				           "<link rel=\"stylesheet\" type=\"text/css\" href=\"/OpenAmsh/js/dhtmlx/skins/dhtmlxgrid_dhx_skyblue.css\"/> " +
				           "<link rel=\"stylesheet\" type=\"text/css\" href=\"/OpenAmsh/js/dhtmlx/skins/dhtmlxaccordion_dhx_skyblue.css\"> " +

				           "<script type=\"text/javascript\" src=\"/OpenAmsh/js/dhtmlx/dhtmlxaccordion.js\"></script> " +
				           "<script type=\"text/javascript\" src=\"/OpenAmsh/js/dhtmlx/dhtmlxcontainer.js\"></script> " +
				           "<script src=\"/OpenAmsh/js/dhtmlx/dhtmlxGrid/dhtmlxcommon.js\"></script> " +
				           "<script src=\"/OpenAmsh/js/dhtmlx/dhtmlxGrid/dhtmlxgrid.js\"></script> " +
				           "<script src=\"/OpenAmsh/js/dhtmlx/dhtmlxGrid/dhtmlxgridcell.js\"></script> " +
				           "<script src=\"/OpenAmsh/js/dhtmlx/dhtmlxGrid/dhtmlxgrid_start.js\"></script> " +
				           
				           "<script> " +
				           "function criaTabela() { " +
				               	"dhtmlx.skin = \"dhx_skyblue\"; ");
				       
								for ( Agent agent : agents ) {
									out.print(
								"myGrid" + agent.getName() + " = new dhtmlXGridFromTable('tblToGrid" + agent.getName() + "'); "
							        );
					       	  	} 

								for ( Agent agent : agents ) {
									out.print(
				               	"for(li = 1; li <= myGrid" + agent.getName() + ".getRowsNum(); li++){ " +

				               	"if(myGrid" + agent.getName() + ".cells(li,2).getValue().indexOf(\"OK\") != -1 ){ " +
				               		"myGrid" + agent.getName() + ".setCellTextStyle(li,2,\"background-color:#35F251; color:black; font-weight:bold;\"); " +
				               	"} " +
				               	"if(myGrid" + agent.getName() + ".cells(li,2).getValue().indexOf(\"CRITICAL\") != -1 ){ " +
				       	  			"myGrid" + agent.getName() + ".setCellTextStyle(li,2,\"background-color:#FF0000; color:black; font-weight:bold;\"); " +
				       	  		"} " +
				               	"if(myGrid" + agent.getName() + ".cells(li,2).getValue().indexOf(\"UNKNOWN\") != -1 ){ " +
			       	  			"myGrid" + agent.getName() + ".setCellTextStyle(li,2,\"background-color:#FF0000; color:black; font-weight:bold;\"); " +
			       	  			"} " +
				       	  		"if(myGrid" + agent.getName() + ".cells(li,2).getValue().indexOf(\"WARNING\") != -1 ){ " +
				       	  			"myGrid" + agent.getName() + ".setCellTextStyle(li,2,\"background-color:#FFFF00; color:black; font-weight:bold;\"); " +
				       	  		"} " +
				       	  		"}");
								}
								
								for ( Agent agent : agents ) {
									out.print(
				       	  		"myGrid" + agent.getName() + ".enableLightMouseNavigation(false); " +
				       	  		"myGrid" + agent.getName() + ".enableLightMouseNavigation(false); ");
								}
								
								out.print(
				       	  		"} " +
				       	  		"function createUI() { " +
				       	  		"dhxAccord = new dhtmlXAccordion(\"accordObj\"); ");
				       			
								for ( Agent agent : agents ) {
									out.print("dhxAccord.addItem(\"a" + agent.getName() + "\",\"  Host: " + agent.getName() + "\"); " +
						            	"dhxAccord.cells(\"a" + agent.getName() + "\").attachObject(\"formHost" + agent.getName() + "\"); "
									);
				       	  		} 
								
								out.print(
								"dhxAccord.openItem(\"a" + agents.get(0).getName() + "\");"+
						        "criaTabela(); " +
						        "dhxAccord.setEffect(true); " +
						        "} " +
					    	"</script> " +		            
				       "</head> " +
				       "<body onload=\"createUI();\"> " +
				       "<div id=\"accordObj\" style=\"width:1009px; height: 300px; margin-left:auto; margin-right:auto;\"></div> ");
								for ( Agent agent : agents ) {
									out.print(
					   "<div id=\"formHost" + agent.getName() + "\" style=\"position: relative; height: 270px; z-index:1; overflow: auto\"> " +
				       "<table id=\"tblToGrid" + agent.getName() + "\" name=\"grid" + agent.getName() + "\" gridHeight=\"auto\" style=\"width:1004px\" imgpath=\"/tcc/codebase/imgs/\" border=\"1\" lightnavigation=\"true\"> " +
				       	"<tr> " +
				       	"<td type=\"ro\">Verificação</td> " +
				       	"<td type=\"ro\">Horario</td> " +
				       	"<td type=\"ro\">Status</td> " +
				       	"</tr>   " 
								);
								for ( Log log : result ) {
									if ( log.getCheck().getAgent().getName().equals(agent.getName()) ) {
									out.print(
						"<tr> " +
				       	"<td><a href=\"/OpenAmsh/ACTReport?idcheck=" + log.getCheck().getId() + "\">" + log.getCheck().getName() + "</a></td> " +
				       	"<td>" + log.getTime() + "</td> " +
				       	"<td>" + log.getStatus() + "</td> " +
				       	"</tr>   "); 
									}
								}
								out.print(
				       "</table> " +
				       "</div> ");
							}
							out.print(
				       "</body> " +
				       "</html>");
				       out.close();
				} else {
					out.println(
						"<html> " +
						"<head> " +
						"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"> " +
						"<script>" +
						"function semRegistros(){" +
						"	alert(\"Não há registros de verificações.\");" +
						"}" +
						"</script>" +
						"</head>" +
						"<body onload=\"semRegistros();\">" +
						"</body>" +
						"</html>"
					);
					out.close();
				}
			
		}
		} catch (Exception e) {
			throw new ServletException(e);		
			
		}
			}
			
	public List<String[]> convert(List<Log> logs) {
		List<String[]> result = createListLogs();
		
		for (Log log: logs) {
			String[] line = new String[5];
			
			line[0] = String.valueOf(log.getCheck().getName());
			line[1] = String.valueOf(log.getTime());
			line[2] = String.valueOf(log.getStatus());
			line[3] = String.valueOf(log.getResult());
						
			result.add(line);
		}
		
		return result;
	}
	
	protected List<String[]> createListLogs() {
		return new ArrayList<String[]>();
	}
	
}
