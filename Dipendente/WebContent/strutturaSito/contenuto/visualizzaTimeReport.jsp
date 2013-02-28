<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.dipendente.dto.Associaz_Risors_Comm_DTO"%>
<%@page import="it.dipendente.dto.CommessaDTO"%>
<%@page import="it.dipendente.dto.PlanningDTO"%>

<%
	Calendar calendar = Calendar.getInstance();
	int anno = calendar.get(Calendar.YEAR);
	HttpSession controlloUtenteLoggato = request.getSession();
	if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
%>

<div class="subtitle">Consultivazione Commesse</div>
<p>
	In questa sezione potrete gestire la consultivazione delle ore del mese corrente e sia dei mesi precedenti. 
	In caso in cui i mesi precedenti non fossero consultivabili contattare l'amministratore per ulteriori dettagli in merito.
	Inoltre trovete due gruppi "Commessa aperta" e "Commessa Chiusa". La differenza che distingue questi due gruppi sono che 
	nel gruppo delle "Commesse Aperte" troverete tutte le commesse con cui attualmente avete un rapporto lavorativo, invece le
	"Commesse Chiuse" si riferiscono a tutti i rapporti lavorativi che avete avuto nell'anno solare.
</p>



<form action="./GestioneReport" method="post">
	<input type="hidden" name="azione" value="visualizzaMese" />
	<input type="hidden" name="parametro" value="<%=request.getParameter("parametro") %>" />
	<table>
		<tr>
			<td>Mese: </td>
			<td>
				<select name="mese">
					<option value="1">Gennaio</option>
					<option value="2">Febbraio</option>
					<option value="3">Marzo</option>
					<option value="4">Aprile</option>
					<option value="5">Maggio</option>
					<option value="6">Giugno</option>
					<option value="7">Luglio</option>
					<option value="8">Agosto</option>
					<option value="9">Settembre</option>
					<option value="10">Ottobre</option>
					<option value="11">Novembre</option>
					<option value="12">Dicembre</option>
				</select>
			</td>
			<td>Anno: </td>
			<td>
				<select name="anno">
					<%
						for(int z = anno; z >= 1900; z--){
					%>		
							<option value="<%=z %>"><%=z %></option>
					<%	
						}
					%>
				</select>
			</td>
			<td>
				<input type="submit" value="cerca" />
			</td>
		</tr>
	</table>
</form>

<%
if(request.getAttribute("listaGiorni") != null){
	ArrayList listaGiorni = (ArrayList) request.getAttribute("listaGiorni");
if(listaGiorni.size() > 0){
	ArrayList commesseACarico = null;
	if(request.getAttribute("commesseRisorse") != null){
		commesseACarico =(ArrayList) request.getAttribute("commesseRisorse");
	}
	int x = 0;
	int id_associazione = 0;
	boolean abilitazioneBottone = true;
	
	int mese = Integer.parseInt(request.getParameter("mese"));
	int annoCorrente = Integer.parseInt(request.getParameter("anno"));
	
	switch(mese){
	case 1:
		out.println("<br><h2 align=\"center\">Gennaio "+ annoCorrente +"</h2><br>");
		break;
	case 2:
		out.println("<br><h2 align=\"center\">Febbraio "+ annoCorrente +"</h2><br>");
		break;
	case 3:
		out.println("<br><h2 align=\"center\">Marzo "+ annoCorrente +"</h2><br>");
		break;
	case 4:
		out.println("<br><h2 align=\"center\">Aprile "+ annoCorrente +"</h2><br>");
		break;
	case 5:
		out.println("<br><h2 align=\"center\">Maggio "+ annoCorrente +"</h2><br>");
		break;
	case 6:
		out.println("<br><h2 align=\"center\">Giugno "+ annoCorrente +"</h2><br>");
		break;
	case 7:
		out.println("<br><h2 align=\"center\">Luglio "+ annoCorrente +"</h2><br>");
		break;
	case 8:
		out.println("<br><h2 align=\"center\">Agosto "+ annoCorrente +"</h2><br>");
		break;
	case 9:
		out.println("<br><h2 align=\"center\">Settembre "+ annoCorrente +"</h2><br>");
		break;
	case 10:
		out.println("<br><h2 align=\"center\">Ottobre "+ annoCorrente +"</h2><br>");
		break;
	case 11:
		out.println("<br><h2 align=\"center\">Novembre "+ annoCorrente +"</h2><br>");
		break;
	case 12:
		out.println("<br><h2 align=\"center\">Dicembre "+ annoCorrente +"</h2><br>");
		break;
}

%>

<form action="./GestioneReport" method="post">
<input type="hidden" name="azione" value="inserisciMese" />
<input type="hidden" name="mese" value="<%=Integer.parseInt(request.getParameter("mese")) %>" />
<input type="hidden" name="anno" value="<%=Integer.parseInt(request.getParameter("anno")) %>" />
<fieldset id="visualizzaReport">
	<legend>Time Report</legend>
	
	<table class="report" width="400">
		<th>Giorni</th>
		<th>Ore</th>
		<th>Orario</th>
		<th>Attività</th>
		<th>Note</th>
	<%
	
			/*
			*	qua effettuo il caricamento delle giornate a partire dal 16 in modo da gestire allineamento
			*	delle due tabelle.
			*/
			
			for(x = 0; x <  listaGiorni.size(); x++){
				PlanningDTO planning = (PlanningDTO)listaGiorni.get(x);
				if(id_associazione == 0){
					id_associazione = planning.getId_associazione();
				}
				/*
				if(planning.isCommessaAttiva()){
					abilitazioneBottone = true;
				}*/
				
				
				if(planning.getData().get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY || planning.getData().get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){ 
	%>		
					<tr>
						<td><input type="text" name="giorni" value="<%=planning.getData() %>" size="15" class="giorniFestivi" readonly="readonly"></td>
						<td><input type="text" name="numeroOre_<%=x %>" value="<%=planning.getNumeroOre() %>" size="3" class="giorniFestivi" <%if(!planning.isAttivo()){ %>readonly="readonly" <%}%>></td>
						<td>
							<%
								if(planning.getOrario() != null){
							%>
									<input type="text" name="orario_<%=x %>" value="<%=planning.getOrario() %>" size="10" class="giorniFestivi" <%if(!planning.isAttivo()){ %>readonly="readonly" <%}%>>
							<%
								}else{
							%>
									<input type="text" name="orario_<%=x %>" value="" size="10" class="giorniFestivi" <%if(!planning.isAttivo()){ %>readonly="readonly" <%}%>>
							<%
								}
							%>
						</td>
						<td>
							<select name="descrizione_<%=x %>" <%if(!planning.isAttivo()){ %> disabled="disabled" <%}%>>
								<option value="">-- Selezione Attività -- </option>
							<%
								if(commesseACarico != null){
									if(commesseACarico.size() > 0){
										for(int z = 0; z < commesseACarico.size(); z++){
											Associaz_Risors_Comm_DTO asscommessa = (Associaz_Risors_Comm_DTO)commesseACarico.get(z);
											if(planning.getDescr_attivita() != null){
												if(planning.getDescr_attivita().equals(asscommessa.getDescrizioneCommessa())){
								%>
													<option value="<%=asscommessa.getDescrizioneCommessa() %>" selected="selected"><%=asscommessa.getDescrizioneCommessa() %></option>
								<%
												}else{
								%>					
													<option value="<%=asscommessa.getDescrizioneCommessa() %>" ><%=asscommessa.getDescrizioneCommessa() %></option>
								<%					
												}
											}else{
									%>
												<option value="<%=asscommessa.getDescrizioneCommessa() %>" ><%=asscommessa.getDescrizioneCommessa() %></option>
									<%			
											}
										}
									}
								}
							%>
							</select>
						</td>
					<%
						if(planning.getNote() != null){
					%>
							<td><input type="text" name="note_<%=x %>" value="<%=planning.getNote() %>" size="20" class="giorniFestivi" <%if(!planning.isAttivo()){ %>readonly="readonly" <%}%>></td>
					<%
						}else{
					%>
							<td><input type="text" name="note_<%=x %>" size="20" <%if(!planning.isAttivo()){ %>readonly="readonly" <%}%> class="giorniFestivi"></td>
					<%
						}
					%>
					</tr>
<%			
				}else{
%>
					<tr>
						<td><input type="text" name="giorni" value="<%=planning.getData() %>" size="15" readonly="readonly"></td>
						<td><input type="text" name="numeroOre_<%=x %>" value="<%=planning.getNumeroOre() %>" size="3" <%if(!planning.isAttivo()){ %>readonly="readonly" <%}%>></td>
						<td>
							<%
								if(planning.getOrario() != null){
							%>
									<input type="text" name="orario_<%=x %>" value="<%=planning.getOrario() %>" size="10" <%if(!planning.isAttivo()){ %>readonly="readonly" <%}%>>
							<%
								}else{
							%>
									<input type="text" name="orario_<%=x %>" value="" size="10" <%if(!planning.isAttivo()){ %>readonly="readonly" <%}%>>
							<%
								}
							%>
						</td>
						<td>
							<select name="descrizione_<%=x %>" <%if(!planning.isAttivo()){ %> disabled="disabled" <%}%>>
								<option value="">-- Selezione Attività -- </option>
							<%
								if(commesseACarico != null){
									if(commesseACarico.size() > 0){
										for(int z = 0; z < commesseACarico.size(); z++){
											Associaz_Risors_Comm_DTO asscommessa = (Associaz_Risors_Comm_DTO)commesseACarico.get(z);
											if(planning.getDescr_attivita() != null){
													if(planning.getDescr_attivita().equals(asscommessa.getDescrizioneCommessa())){
									%>
														<option value="<%=asscommessa.getDescrizioneCommessa() %>" selected="selected"><%=asscommessa.getDescrizioneCommessa() %></option>
									<%
													}else{
									%>					
														<option value="<%=asscommessa.getDescrizioneCommessa() %>" ><%=asscommessa.getDescrizioneCommessa() %></option>
									<%					
													}
											}else{
									%>
												<option value="<%=asscommessa.getDescrizioneCommessa() %>" ><%=asscommessa.getDescrizioneCommessa() %></option>
									<%			
											}
										}
									}
								}
							%>
							</select>
						</td>
						<%
							if(planning.getNote() != null){
						%>
								<td><input type="text" name="note_<%=x %>" value="<%=planning.getNote() %>" size="20" <%if(!planning.isAttivo()){ %>readonly="readonly" <%}%>></td>
						<%
							}else{
						%>
								<td><input type="text" name="note_<%=x %>" size="20" <%if(!planning.isAttivo()){ %>readonly="readonly" <%}%>></td>
						<%
							}
						%>
					</tr>				
					
<%					
				}
			}
%>
				<tr>
					<td>
						<label>Totale Ore:</label>
					</td>
			<%
				/*
				*	qua effettuo la somma di tutte ore del mese corrente
				* 	caricate dalla risorsa
				*/
				double oreTotali = 0;
				for(int y = 0; y <  listaGiorni.size(); y++){
					PlanningDTO planning = (PlanningDTO)listaGiorni.get(y);
					
					oreTotali += planning.getNumeroOre();
				}
			%>
					
					<td>
						<label><%=oreTotali %></label>
					</td>
				</tr>
				<%
					if(abilitazioneBottone){
				%>
				<tr>
					<td>
						<input type="submit" value="salva report" />
					</td>
				</tr>
				<%
					}
				%>
			</table>
			<input type="hidden" name="contatore" value="<%=x %>" />
			<input type="hidden" name="associazione" value="<%=id_associazione %>" />
		</fieldset>
	</form>
	<%
		}else{
	%>
			<p align="center" class="spazio">Non ci sono giornate per questo mese </p>
	<%
		}
	}
}else{
%>
	<script type="text/javascript">
		controlloSessioneAttiva();
	</script>		
<%
}
%>
