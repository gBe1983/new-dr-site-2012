<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="it.dipendente.dto.CommessaDTO"%>
<%@page import="it.dipendente.dto.RisorsaDTO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.dipendente.dto.PlanningDTO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.HashMap"%>

<%	
	SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
	ArrayList<ArrayList> calendar = (ArrayList<ArrayList>) request.getAttribute("caricamentoGiornate");
	ArrayList<PlanningDTO> elencoGiorniCommessa = (ArrayList<PlanningDTO>) calendar.get(1);
	HashMap listDay = (HashMap) request.getAttribute("elencoGiorni");
	String nomeCommessa = "";
	if(request.getAttribute("nomeCommessa") != null){
		nomeCommessa = request.getAttribute("nomeCommessa").toString();
	}
	int parametro = 0;
	if(request.getAttribute("planning") != null){
		parametro = Integer.parseInt(request.getAttribute("planning").toString());
	}
%>


<form action="#" method="post" name="listGiornate">
	<input type="hidden" name="azione" value="salvaGiornate">
	<input type="hidden" name="parametri" value="">
	<input type="hidden" name="parametro" value="<%=parametro %>" id="parameter">
	<table border="1" align="center" id="listGiornate">
		<tr>
			<th colspan="3"><h4 align="center">Commessa <%=nomeCommessa %></h4></th>
		</tr>
		<tr>
			<th>Gg Settimana</th>
			<th>Giorno</th>
			<th><input type="checkbox" name="checkAll" id="checkboxAll" onclick="checkedAll(this)"></th>
		</tr>

	<%
		for(int x = 0; x < elencoGiorniCommessa.size(); x++){
			PlanningDTO giorno = elencoGiorniCommessa.get(x);
			if(giorno.getDescr_attivita().equals(nomeCommessa)){
				if(giorno.getNumeroOre() > 0.0 || giorno.getNumeroOre() == 0.0 && giorno.getAssenze() > 0.0){
					if(giorno.getData().get(Calendar.DAY_OF_WEEK) == 1 || giorno.getData().get(Calendar.DAY_OF_WEEK) == 7){
						if(giorno.getData().get(Calendar.DAY_OF_WEEK) == 1){
			%>
							<tr>
								<td style="color: red; background: #CCC; font-size: 13px;"><%=listDay.get(8) %></td>
								<td style="color: red; background: #CCC; font-size: 13px;"><%=format.format(giorno.getData().getTime()) %></td>
								<td style="background: #CCC;"><input type="checkbox" name="giorno<%=giorno.getId_planning() %>" value="<%=giorno.getId_planning() %>" disabled="disabled" onclick="selezioneGiornate(this)" class="case"></td>
							</tr>

			<%		
						}else{
			%>				
							<tr>
								<td style="color: red; background: #CCC; font-size: 13px;"><%=listDay.get(giorno.getData().get(Calendar.DAY_OF_WEEK)) %></td>
								<td style="color: red; background: #CCC; font-size: 13px;"><%=format.format(giorno.getData().getTime()) %></td>
								<td style="background: #CCC;"><input type="checkbox" name="giorno<%=giorno.getId_planning() %>" value="<%=giorno.getId_planning() %>" disabled="disabled" onclick="selezioneGiornate(this)" class="case"></td>
							</tr>			
			<%			
						}
					}else{
			%>			
						<tr>
							<td style="color: #000; background: #CCC; font-size: 13px;"><%=listDay.get(giorno.getData().get(Calendar.DAY_OF_WEEK)) %></td>
							<td style="color: #000; background: #CCC; font-size: 13px;"><%=format.format(giorno.getData().getTime()) %></td>
							<td style="background: #CCC;"><input type="checkbox" name="giorno<%=giorno.getId_planning() %>" value="<%=giorno.getId_planning() %>" disabled="disabled" onclick="selezioneGiornate(this)" class="case"></td>
						</tr>
			<%			
					}
				}else{
					if(giorno.getData().get(Calendar.DAY_OF_WEEK) == 7 || giorno.getData().get(Calendar.DAY_OF_WEEK) == 1){
						if(giorno.getData().get(Calendar.DAY_OF_WEEK) == 1){
		%>
							<tr>
								<td style="color: red; background: #CCD; font-size: 13px;"><%=listDay.get(8) %></td>
								<td style="color: red; background: #CCD; font-size: 13px;"><%=format.format(giorno.getData().getTime()) %></td>
								<td style="background: #CCD;"><input type="checkbox" name="giorno<%=giorno.getId_planning() %>" value="<%=giorno.getId_planning() %>" onclick="selezioneGiornate(this)" class="case"></td>
							</tr>
		<%
							}else{
		%>
							<tr>
								<td style="color: red; background: #CCD; font-size: 13px;"><%=listDay.get(giorno.getData().get(Calendar.DAY_OF_WEEK)) %></td>
								<td style="color: red; background: #CCD; font-size: 13px;"><%=format.format(giorno.getData().getTime()) %></td>
								<td style="background: #CCD;"><input type="checkbox" name="giorno<%=giorno.getId_planning() %>" value="<%=giorno.getId_planning() %>" onclick="selezioneGiornate(this)" class="case"></td>
							</tr>
		<%
							}
						}else{
		%>
							<tr>
								<td style="color: #000; background: #E7D6B6; font-size: 13px;"><%=listDay.get(giorno.getData().get(Calendar.DAY_OF_WEEK)) %></td>
								<td style="color: #000; background: #E7D6B6; font-size: 13px;"><%=format.format(giorno.getData().getTime()) %></td>
								<td style="background: #E7D6B6;"><input type="checkbox" name="giorno<%=giorno.getId_planning() %>" value="<%=giorno.getId_planning() %>" onclick="selezioneGiornate(this)" class="case"></td>
							</tr>
		<%
						}		
				}
			}
		}
	%>
		<tr>
			<td colspan="3" class="save"><input type="submit" value="Salva Giorni" onclick=" return caricamentoGiornate()"></td>
		</tr>
	</table>
</form>