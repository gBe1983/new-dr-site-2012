<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.util.HashMap"%>
<%@page import="it.dipendente.dto.RisorsaDTO"%>
<%@page import="it.dipendente.enums.Months"%>
<%@page import="java.util.List"%>
<%@page import="it.dipendente.dto.PlanningDTO"%>
<%@page import="java.util.Calendar"%>
<%@page import="it.dipendente.bo.Month"%>
<%@page import="it.dipendente.bo.Day"%>
<%@page import="it.dipendente.bo.Week"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.dipendente.dto.CommessaDTO"%>
<%@page import="it.util.log.MyLogger"%>

<%
SimpleDateFormat sdf=new SimpleDateFormat("d");
ArrayList<ArrayList> calendario = (ArrayList<ArrayList>) request.getAttribute("caricamentoGiornate");
ArrayList<PlanningDTO> giorni = (ArrayList<PlanningDTO>) calendario.get(1);
ArrayList<CommessaDTO> commesse = (ArrayList<CommessaDTO>) calendario.get(0);
	
Calendar meseAnno = (Calendar) request.getAttribute("meseAnno");
HashMap elencoGiorni = (HashMap) request.getAttribute("elencoGiorni");
	
boolean elencoGiornate = Boolean.parseBoolean(request.getAttribute("flagVisualizzaElencoGiornate").toString());
int settimaneTotali = Integer.parseInt(request.getAttribute("settimaneTotali").toString());
Month m = (Month) request.getAttribute("mese");

double ordinarie = 0.0,straordinario = 0.0,ferie = 0.0,permessi = 0.0,mutua = 0.0,ferieNonRetribuite = 0.0, permessiNonRetribuiti = 0.0, mutuaNonRetribuita = 0.0;
	
String intestazioneMeseAnno = "";
	
if(request.getSession().getAttribute("utenteLoggato") != null){
		for(Months month:Months.values()){
			if(month.getIndex()==m.getMonth()){ 
				intestazioneMeseAnno += month.getLabel() + " - ";
			}
		}
		Calendar calendar = Calendar.getInstance();
		for(int z=calendar.get(Calendar.YEAR)-5;z<=calendar.get(Calendar.YEAR)+5;z++){
			if(m.getYear()==z){ 
				intestazioneMeseAnno += z; 
			}	
		}
		
%>

	<div class="subtitle">Consuntivazione <%=intestazioneMeseAnno %></div>
		<form name="navigatore" action="./GestioneReport" method="post" class="spazioUltra">
		<input type="hidden" name="azione" value="compilaTimeReport"/>
		<table class="timeReportNavigator" align="center">
			<tr>
				<td>Mese:</td>
				<td>
					<select name="mese">
					<%
						for(Months month:Months.values()){%>
							<option value="<%=month.getIndex()%>"<%if(month.getIndex()==m.getMonth()){%> selected="selected"<%}%>><%=month.getLabel()%></option>
					<%	
						}
					%>
					</select>
				</td>
				<td>Anno:</td>
				<td>
					<select name="anno">
				<%
				Calendar c = Calendar.getInstance();
				for(int z=c.get(Calendar.YEAR)-5;z<=c.get(Calendar.YEAR)+5;z++){
				%>
						<option value="<%=z%>"<%if(m.getYear()==z){ %> selected="selected"<%}%>><%=z%></option>
				<%	
				}
				%>
					</select>
				</td>
				<td>
					<input type="submit" value="Cerca" class="search" title="Cerca la consuntivazione"/>
				</td>
			</tr>
		</table>
	</form>
	<%
			if(giorni.size() > 0){
	%>
	<form name="timeDetail" action="#" method="post" class="spazioUltra" name="visualizzaTimeReport">
		<input type="hidden" name="azione" value="salvaTimeReport"/>
		<table class="timeReport" >
			<%
				for(int x = giorni.get(0).getNumeroSettimana(); x <= settimaneTotali; x++){
					
			%>
					<tr>
						<th class="weekHeader" colspan="8">Settimana <% if(x==0){out.print(x+1);}else{out.print(x);} %></th>
					</tr>
					<tr>
						<th class="commesseHeader">Commesse Abilitate</th>
						<%
							for(int z = 1; z <= elencoGiorni.size(); z++){
						%>
								<th <% if((z+1)== 7 || (z+1)== 8) {%>class="holiday" <%}else{%>class="workDay"<%}%>><%=elencoGiorni.get(z+1) + "<br>"%>
								<%
								for(int y = 0; y < giorni.size(); y++){
										PlanningDTO giorno = (PlanningDTO)giorni.get(y);
							
										if(giorno.getData().get(Calendar.DAY_OF_WEEK) != 1 && giorno.getNumeroSettimana() == x && z != 7){
											
											if(giorno.getData().get(Calendar.WEEK_OF_MONTH) == x && giorno.getData().get(Calendar.DAY_OF_WEEK) == (z+1)){
												out.print(sdf.format(giorno.getData().getTime()));
												break;
											}
										}else{
											if(giorno.getNumeroSettimana() == x && giorno.getData().get(Calendar.DAY_OF_WEEK) == 1 && z == 7){
												out.print(sdf.format(giorno.getData().getTime()));
												break;
											}
										}
										
								}
						%>
								</th>
						<%
							}
						%>
					</tr>
					<%
						for(int  w = 0; w < commesse.size(); w++){
							CommessaDTO comm = (CommessaDTO) commesse.get(w);
					%>
					<tr>
						<th class="commesse" ><%=comm.getDescrizione()%></th>
					<%
							int q;
							int contatore = 1;
							for(int z = 1; z <= elencoGiorni.size(); z++){
								
								for(q = 0; q < giorni.size(); q++){
									PlanningDTO giorno = (PlanningDTO)giorni.get(q);
									String input = "";
									if(giorno.getNumeroSettimana() == x){
												
										if(giorno.getData().get(Calendar.WEEK_OF_MONTH) == x && giorno.getData().get(Calendar.DAY_OF_WEEK) == (z+1) && z < 6 && giorno.getCodiceCommessa().equals(comm.getCodiceCommessa())){
											
											ordinarie += giorno.getNumeroOre();
											straordinario += giorno.getStraordinari();
											
											input += "<td class=\"workDay\">"; 
											input += "<table>";
											input += "	<tr>";
											input += "		<td>";
											input += "		</td>";
											input += "		<td>";
											input += "			<input type=\"hidden\" name=\"parametro"+giorno.getId_planning()+"\" value=\""+giorno.getId_planning()+"\" id=\"parametri"+giorno.getId_planning()+"\" >";
											input += "		</td>";
											input += "		<td>";
											input += "		</td>";
											input += "	</tr>";
											input += "	<tr>";
											input += "		<td>";
											input += "			<span>Ord. </span>";
											input += "		</td>";
											input += "		<td>";
											input += "			<input type=\"text\" name=\"ore" + giorno.getId_planning() + "\" value=\""+giorno.getNumeroOre()+"\" title=\"ore ordinarie\" id=\"ore"+giorno.getId_planning()+"\" onclick=\"compilazione(this,'"+giorno.getDescr_attivita()+"','"+giorno.getId_planning()+"','"+giorno.getNote()+"')\">";
											input += "		</td>";
											input += "		<td>";
											input += "		</td>";
											input += "	</tr>";
											input += "	<tr>";
											input += "		<td>";
											input += "			<span>Strao. </span>";
											input += "		</td>";
											input += "		<td>";
											input += "			<input type=\"text\" name=\"straordinario" + giorno.getId_planning() + "\" value=\""+giorno.getStraordinari()+"\" title=\"ore straordinarie\" id=\"straordinario"+giorno.getId_planning()+"\" onclick=\"compilazione(this,'"+giorno.getDescr_attivita()+"','"+giorno.getId_planning()+"','"+giorno.getNote()+"')\">";
											input += "		</td>";
											input += "		<td>";
											input += "		</td>";
											input += "	</tr>";
											input += "	<tr>";
											input += "		<td>";
											input += "			<span>Assen. </span>";
											input += "		</td>";
											input += "		<td>";
											input += "			<input type=\"text\" name=\"assenze" + giorno.getId_planning() + "\" value=\""+giorno.getAssenze()+"\" title=\"ore assenze\" id=\"assenze"+giorno.getId_planning()+"\" onclick=\"compilazione(this,'"+giorno.getDescr_attivita()+"','"+giorno.getId_planning()+"','"+giorno.getNote()+"')\">";
											input += "		</td>";
											input += "		<td>";
											
											if(giorno.isFerie()){
												ferie += giorno.getAssenze();	
												input += "<span id=\"tipologiaAssenze"+giorno.getId_planning()+"\">(Fe)</span>";
											}else if(giorno.isPermessi()){
												permessi += giorno.getAssenze();
												input += "<span id=\"tipologiaAssenze"+giorno.getId_planning()+"\">(Pr)</span>";
											}else if(giorno.isMutua()){
												mutua += giorno.getAssenze();
												input += "<span id=\"tipologiaAssenze"+giorno.getId_planning()+"\">(M)</span>";
											}else if(giorno.isPermessiNonRetribuiti()){
												permessiNonRetribuiti += giorno.getAssenze();
												input += "<span id=\"tipologiaAssenze"+giorno.getId_planning()+"\">(PNR)</span>";
											}
											
											
											input += "		</td>";
											input += "	</tr>";
											input += "	<tr>";
											input += "		<td>";
											input += "			<span>Note: </span>";
											input += "		</td>";
											if(giorno.getNote() != null && !giorno.getNote().equals("")){
												input += "		<td>";
												input += "			<input type=\"checkbox\" name=\"note\" title=\"note\" checked=\"checked\" disabled=\"disable\">";
												input += "		</td>";
											}else{
												input += "		<td>";
												input += "			<input type=\"checkbox\" name=\"note\" title=\"note\" disabled=\"disable\">";
												input += "		</td>";
											}
											input += "		<td>";
											input += "		</td>";
											input += "	</tr>";
											input += "</table>";
											input += "</td>";
											out.print(input);
											contatore++;
											break;
										}else if(giorno.getNumeroSettimana() == x && giorno.getData().get(Calendar.DAY_OF_WEEK) == 7 && z == 6 && giorno.getCodiceCommessa().equals(comm.getCodiceCommessa())){
											
											ordinarie += giorno.getNumeroOre();
											straordinario += giorno.getStraordinari();
											
											input += "<td class=\"holiday\">"; 
											input += "<table>";
											input += "	<tr>";
											input += "		<td>";
											input += "		</td>";
											input += "		<td>";
											input += "			<input type=\"hidden\" name=\"parametro"+giorno.getId_planning()+"\" value=\""+giorno.getId_planning()+"\" id=\"parametri"+giorno.getId_planning()+"\" >";
											input += "		</td>";
											input += "		<td>";
											input += "		</td>";
											input += "	</tr>";
											input += "	<tr>";
											input += "		<td>";
											input += "			<span>Ord. </span>";
											input += "		</td>";
											input += "		<td>";
											input += "			<input type=\"text\" name=\"ore" + giorno.getId_planning() + "\" value=\""+giorno.getNumeroOre()+"\" title=\"ore ordinarie\" id=\"ore"+giorno.getId_planning()+"\" onclick=\"compilazione(this,'"+giorno.getDescr_attivita()+"','"+giorno.getId_planning()+"','"+giorno.getNote()+"')\">";
											input += "		</td>";
											input += "		<td>";
											input += "		</td>";
											input += "	</tr>";
											input += "	<tr>";
											input += "		<td>";
											input += "			<span>Strao. </span>";
											input += "		</td>";
											input += "		<td>";
											input += "			<input type=\"text\" name=\"straordinario" + giorno.getId_planning() + "\" value=\""+giorno.getStraordinari()+"\" title=\"ore straordinarie\" id=\"straordinario"+giorno.getId_planning()+"\" onclick=\"compilazione(this,'"+giorno.getDescr_attivita()+"','"+giorno.getId_planning()+"','"+giorno.getNote()+"')\">";
											input += "		</td>";
											input += "		<td>";
											input += "		</td>";
											input += "	</tr>";
											input += "	<tr>";
											input += "		<td>";
											input += "			<span>Assen. </span>";
											input += "		</td>";
											input += "		<td>";
											input += "			<input type=\"text\" name=\"assenze" + giorno.getId_planning() + "\" value=\""+giorno.getAssenze()+"\" title=\"ore assenze\" id=\"assenze"+giorno.getId_planning()+"\" onclick=\"compilazione(this,'"+giorno.getDescr_attivita()+"','"+giorno.getId_planning()+"','"+giorno.getNote()+"')\">";
											input += "		</td>";
											input += "		<td>";
											
											if(giorno.isFerie()){
												ferie += giorno.getAssenze();	
												input += "<span id=\"tipologiaAssenze"+giorno.getId_planning()+"\">(Fe)</span>";
											}else if(giorno.isPermessi()){
												permessi += giorno.getAssenze();
												input += "<span id=\"tipologiaAssenze"+giorno.getId_planning()+"\">(Pr)</span>";
											}else if(giorno.isMutua()){
												mutua += giorno.getAssenze();
												input += "<span id=\"tipologiaAssenze"+giorno.getId_planning()+"\">(M)</span>";
											}else if(giorno.isPermessiNonRetribuiti()){
												permessiNonRetribuiti += giorno.getAssenze();
												input += "<span id=\"tipologiaAssenze"+giorno.getId_planning()+"\">(PNR)</span>";
											}
											
											input += "		</td>";
											input += "	</tr>";
											input += "	<tr>";
											input += "		<td>";
											input += "			<span>Note: </span>";
											input += "		</td>";
											if(giorno.getNote() != null && !giorno.getNote().equals("")){
												input += "		<td>";
												input += "			<input type=\"checkbox\" name=\"note\" title=\"note\" checked=\"checked\" disabled=\"disable\">";
												input += "		</td>";
											}else{
												input += "		<td>";
												input += "			<input type=\"checkbox\" name=\"note\" title=\"note\" disabled=\"disable\">";
												input += "		</td>";
											}
											input += "		<td>";
											input += "		</td>";
											input += "	</tr>";
											input += "</table>";
											input += "</td>";
											out.print(input);
											contatore++;
											break;	
										}else if(giorno.getNumeroSettimana() == x && giorno.getData().get(Calendar.DAY_OF_WEEK) == 1 && z == 7 && giorno.getCodiceCommessa().equals(comm.getCodiceCommessa())){
											
											ordinarie += giorno.getNumeroOre();
											straordinario += giorno.getStraordinari();
											
											input += "<td class=\"holiday\">"; 
											input += "<table>";
											input += "	<tr>";
											input += "		<td>";
											input += "		</td>";
											input += "		<td>";
											input += "			<input type=\"hidden\" name=\"parametro"+giorno.getId_planning()+"\" value=\""+giorno.getId_planning()+"\" id=\"parametri"+giorno.getId_planning()+"\" >";
											input += "		</td>";
											input += "		<td>";
											input += "		</td>";
											input += "	</tr>";
											input += "	<tr>";
											input += "		<td>";
											input += "			<span>Ord. </span>";
											input += "		</td>";
											input += "		<td>";
											input += "			<input type=\"text\" name=\"ore" + giorno.getId_planning() + "\" value=\""+giorno.getNumeroOre()+"\" title=\"ore ordinarie\" id=\"ore"+giorno.getId_planning()+"\" onclick=\"compilazione(this,'"+giorno.getDescr_attivita()+"','"+giorno.getId_planning()+"','"+giorno.getNote()+"')\">";
											input += "		</td>";
											input += "		<td>";
											input += "		</td>";
											input += "	</tr>";
											input += "	<tr>";
											input += "		<td>";
											input += "			<span>Strao. </span>";
											input += "		</td>";
											input += "		<td>";
											input += "			<input type=\"text\" name=\"straordinario" + giorno.getId_planning() + "\" value=\""+giorno.getStraordinari()+"\" title=\"ore straordinarie\" id=\"straordinario"+giorno.getId_planning()+"\" onclick=\"compilazione(this,'"+giorno.getDescr_attivita()+"','"+giorno.getId_planning()+"','"+giorno.getNote()+"')\">";
											input += "		</td>";
											input += "		<td>";
											input += "		</td>";
											input += "	</tr>";
											input += "	<tr>";
											input += "		<td>";
											input += "			<span>Assen. </span>";
											input += "		</td>";
											input += "		<td>";
											input += "			<input type=\"text\" name=\"assenze" + giorno.getId_planning() + "\" value=\""+giorno.getAssenze()+"\" title=\"ore assenze\" id=\"assenze"+giorno.getId_planning()+"\" onclick=\"compilazione(this,'"+giorno.getDescr_attivita()+"','"+giorno.getId_planning()+"','"+giorno.getNote()+"')\">";
											input += "		</td>";
											input += "		<td>";
											
											if(giorno.isFerie()){
												ferie += giorno.getAssenze();	
												input += "<span id=\"tipologiaAssenze"+giorno.getId_planning()+"\">(Fe)</span>";
											}else if(giorno.isPermessi()){
												permessi += giorno.getAssenze();
												input += "<span id=\"tipologiaAssenze"+giorno.getId_planning()+"\">(Pr)</span>";
											}else if(giorno.isMutua()){
												mutua += giorno.getAssenze();
												input += "<span id=\"tipologiaAssenze"+giorno.getId_planning()+"\">(M)</span>";
											}else if(giorno.isPermessiNonRetribuiti()){
												permessiNonRetribuiti += giorno.getAssenze();
												input += "<span id=\"tipologiaAssenze"+giorno.getId_planning()+"\">(PNR)</span>";
											}
											
											input += "		</td>";
											input += "	</tr>";
											input += "	<tr>";
											input += "		<td>";
											input += "			<span>Note: </span>";
											input += "		</td>";
											if(giorno.getNote() != null && !giorno.getNote().equals("")){
												input += "		<td>";
												input += "			<input type=\"checkbox\" name=\"note\" title=\"note\" checked=\"checked\" disabled=\"disable\">";
												input += "		</td>";
											}else{
												input += "		<td>";
												input += "			<input type=\"checkbox\" name=\"note\" title=\"note\" disabled=\"disable\">";
												input += "		</td>";
											}
											input += "		<td>";
											input += "		</td>";
											input += "	</tr>";
											input += "</table>";
											input += "</td>";
											out.print(input);
											contatore++;
											break;	
										}
										
									}
								}
								if((x == settimaneTotali && q == giorni.size()) || q == giorni.size()){
									out.print("<td class=\"disabledDay\"></td>");
								}
							}
						}
					%>
					</tr>
			<%	
				}
			%>
			<tr> 
				<td class="totaliDx" colspan="8"><span>Ore ordinarie: <%=ordinarie %></span><span>Ore straordinarie: <%=straordinario %></span><span>Ferie: <%=ferie %></span><span>Permessi: <%=permessi %></span><span>Mutua: <%=mutua %></span><span>Permessi Non Retribuiti: <%=permessiNonRetribuiti %></span><span>Totale: <%=ordinarie+straordinario+ferie+permessi+mutua+ferieNonRetribuite+permessiNonRetribuiti+mutuaNonRetribuita %></span></td>
				<td class="save" colspan="2">
			</tr>

		</table>
	</form>		
<%
		}else{
%>
			<p class="spazioUltra" align="center"> Non ci sono commesse associate questo mese </p>
					
<%			
		}
	}
%>

<div id="compilade" title="Compila Ore">
	<%@include file="compilazioneOre.jsp" %>
</div>

<div id="elencoGiornate" title="Elenco Giornate">
	<%@include file="elencoGiornate.jsp" %>
</div> 

<%
	if(elencoGiornate){
%>
		<script type="text/javascript">
			alert("Bozza Salvata con Successo");
			 
			$('#elencoGiornate').dialog({
				modal: true,
				autoOpen: true,
				height: 580,
				width: 550,
				position: [350,30],
				show: {
					effect: "blind",
					duration: 1000
				},
				hide: {
					effect: "explode",
					duration: 1000
				}
			});
		</script>
<%	
	}
	if(request.getAttribute("msg") != null){
%>
		<script type="text/javascript">
			alert(<%=request.getAttribute("msg") %>);
		</script>
<%	
	}
%>
