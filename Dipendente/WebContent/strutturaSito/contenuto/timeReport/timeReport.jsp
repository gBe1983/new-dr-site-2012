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
<%@page import="it.dipendente.dto.Associaz_Risors_Comm_DTO"%>

<%
	
if(request.getSession().getAttribute("utenteLoggato") != null){

	/*
	* parametroCommessa corrisponde al id_commessa.
	* Effettuo questo controllo in caso pi� commesse
	*/
	if(request.getAttribute("parametroCommessa").toString().equals("")){
		ArrayList<Associaz_Risors_Comm_DTO> listaCommesse = (ArrayList<Associaz_Risors_Comm_DTO>)request.getAttribute("listaCommesse");
	
		if(listaCommesse.size() >0){
%>
		<div id="sceltaCommesse" title="Scelta Commesse">
		

			<form action="./GestioneReport" method="post" id="FormScelteCommesse" name="FormScelteCommesse">
				<input type="hidden" name="azione" value="compilaTimeReport">
				<table align="center">
					<tr>
						<td colspan="2">Quale commessa vuoi selezionare?</td>
					</tr>
					<%
						for(int x = 0; x < listaCommesse.size(); x++){
							Associaz_Risors_Comm_DTO asscomm = (Associaz_Risors_Comm_DTO) listaCommesse.get(x);
					%>		
							<tr>
								<td><input type="radio" name="parametroCommessa" value="<%=asscomm.getId_commessa() %>"></td><td><label><%=asscomm.getDescrizioneCommessa() %></label>
							</tr>		
					<%	
						}
					%>
					<tr>
						<td><input type="radio" name="parametroCommessa" value="tutte"></td><td><label>Tutte</label>
					</tr>
					<tr>
						<td colspan="2">
							<input type="submit" value="Scegli Commessa" onclick="return controlloSelezionaCommessa('scegli')" />
							<input type="reset" value="Annulla" onclick="return controlloSelezionaCommessa('annulla')" />
						</td>
					</tr>
				</table>
			</form>
		</div>
		
		<script type="text/javascript">			 
			$('#sceltaCommesse').dialog({
				modal: true,
				autoOpen: true,
				height: 250,
				width: 300,
				position: [550,200],
				show: {
					effect: "blind",
					duration: 1000
				},
				hide: {
					effect: "explode",
					duration: 1000
				},
				close: function(event, ui) { location.href = 'http://www.drconsulting.tv/Dipendente/index.jsp?azione=report' }
			});
		</script>
<%
		//end if listaCommessa.size() > 0
		}else{
			
			SimpleDateFormat sdf=new SimpleDateFormat("d");
			ArrayList<ArrayList> calendario = (ArrayList<ArrayList>) request.getAttribute("caricamentoGiornate");
					
			Calendar meseAnno = (Calendar) request.getAttribute("meseAnno");
			HashMap elencoGiorni = (HashMap) request.getAttribute("elencoGiorni");
					
			Calendar dataInizio  = (Calendar) request.getAttribute("dataInizio");
			
			Month m = (Month) request.getAttribute("mese");
			
			String id_commessa = request.getAttribute("parametroCommessa").toString();
			
			double ordinarie = 0.0,straordinario = 0.0,ferie = 0.0,permessi = 0.0,mutua = 0.0,ferieNonRetribuite = 0.0, permessiNonRetribuiti = 0.0, mutuaNonRetribuita = 0.0;
				
			String intestazioneMeseAnno = "";
			
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
							<select name="parametroCommessa">
							<%
								/*
								* faccio questo controllo in quanto c� la possibilit� 
								* che l'utente possa scegliere la voce "tutte" dove non �
								* caricato nell'array
								*/
							if(listaCommesse.size() > 0){
								if(id_commessa == "tutte"){
							%>
									<option value="tutte" selected="selected">tutte</option>
							<%
								}else{
							%>
									<option value="tutte">tutte</option>
							<%
								}
								for(int i = 0; i < listaCommesse.size(); i++){
									if(String.valueOf(listaCommesse.get(i).getId_commessa()).equals(id_commessa)){
							%>
										<option value="<%=listaCommesse.get(i).getId_commessa() %>" selected="selected"><%=listaCommesse.get(i).getDescrizioneCommessa() %></option>	
							<%
									}else{
							%>
										<option value="<%=listaCommesse.get(i).getId_commessa() %>"><%=listaCommesse.get(i).getDescrizioneCommessa() %></option>
							<%
									}
								}
							}else{
							%>
								<option value="">Non ci sono commesse</option>	
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

			<tr>
				<td><p align="center"> In questo mese non ci sono commesse associate. </p></td>
			</tr>
<%
		}
	
	//end if parametroCommessa == ""
	}else{
	
		SimpleDateFormat sdf=new SimpleDateFormat("d");
		ArrayList<ArrayList> calendario = (ArrayList<ArrayList>) request.getAttribute("caricamentoGiornate");
				
		Calendar meseAnno = (Calendar) request.getAttribute("meseAnno");
		HashMap elencoGiorni = (HashMap) request.getAttribute("elencoGiorni");
		
		boolean elencoGiornate = Boolean.parseBoolean(request.getAttribute("flagVisualizzaElencoGiornate").toString());
		int settimaneTotali = Integer.parseInt(request.getAttribute("settimaneTotali").toString());
		int maxGiorniMensili = Integer.parseInt(request.getAttribute("maxGiorniMensili").toString());
		
		Calendar dataInizio  = (Calendar) request.getAttribute("dataInizio");
		
		Month m = (Month) request.getAttribute("mese");
		
		ArrayList<Associaz_Risors_Comm_DTO> listaCommesse = (ArrayList<Associaz_Risors_Comm_DTO>) request.getAttribute("listaCommesse");
		
		String id_commessa = request.getAttribute("parametroCommessa").toString();
		
		double ordinarie = 0.0,straordinario = 0.0,ferie = 0.0,permessi = 0.0,mutua = 0.0,ferieNonRetribuite = 0.0, permessiNonRetribuiti = 0.0, mutuaNonRetribuita = 0.0;
			
		String intestazioneMeseAnno = "";
		
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
						<select name="parametroCommessa">
						<%
							/*
							* faccio questo controllo in quanto c� la possibilit� 
							* che l'utente possa scegliere la voce "tutte" dove non �
							* caricato nell'array
							*/
						if(listaCommesse.size() > 0){
							if(id_commessa == "tutte"){
						%>
								<option value="tutte" selected="selected">tutte</option>
						<%
							}else{
						%>
								<option value="tutte">tutte</option>
						<%
							}
							for(int i = 0; i < listaCommesse.size(); i++){
								if(String.valueOf(listaCommesse.get(i).getId_commessa()).equals(id_commessa)){
						%>
									<option value="<%=listaCommesse.get(i).getId_commessa() %>" selected="selected"><%=listaCommesse.get(i).getDescrizioneCommessa() %></option>	
						<%
								}else{
						%>
									<option value="<%=listaCommesse.get(i).getId_commessa() %>"><%=listaCommesse.get(i).getDescrizioneCommessa() %></option>
						<%
								}
							}
						}else{
						%>
							<option value="">Non ci sono commesse</option>	
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
			if(calendario.size() > 0){
				
				ArrayList<PlanningDTO> giorni = (ArrayList<PlanningDTO>) calendario.get(1);
				ArrayList<CommessaDTO> commesse = (ArrayList<CommessaDTO>) calendario.get(0);
			
				if(giorni.size() > 0 && commesse.size() > 0){
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
												<th <% 
														if((z+1)== 7 || (z+1)== 8) {
													%>		
															class="holiday" 
													<%
														}else{
													%>
															class="workDay"
													<%
														}
													%>
												><%=elencoGiorni.get(z+1) + "<br>"%>
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
									</tr>
							<%
							}
							%>
						</table>
					</form>
				
					<div id="compilade" title="Compila Ore">
						<jsp:include page="compilazioneOre.jsp" />
					</div>
					
					<div id="elencoGiornate" title="Elenco Giornate">
						<jsp:include page="elencoGiornate.jsp" />
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
					
				} // end if giorni.size() > 0 && commesse.size() > 0
				
			} // end if calendario.size > 0
	}// end else	
	
	if(request.getAttribute("msg") != null){
%>
		<script type="text/javascript">
			alert(<%=request.getAttribute("msg") %>);
		</script>
<%	
	}
}else{
%>
	<script type="text/javascript">
		controlloSessioneAttiva();
	</script>
<%
}
%>