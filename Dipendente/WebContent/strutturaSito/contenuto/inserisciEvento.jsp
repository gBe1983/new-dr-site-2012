<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="it.dipendente.dto.EventoDTO"%>

<%
HttpSession controlloUtenteLoggato = request.getSession();
if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
	String azioneCorrente = request.getParameter("azione");
	if(azioneCorrente.equals("inserisciEvento")){
%>


<div class="subtitle">Inserimento Evento</div>


<form action="./GestioneCalendarioEventi" method="post">
	<input type="hidden" name="azione" value="inserimentoEvento">
	<table align="center" class="spazio">
		<tr>
			<td>Titolo</td>
			<td><input type="text" name="titolo" ></td>
		</tr>
		<tr>
			<td>Data Inizio</td>
			<td><input type="text" name="dataInizio" ><label>(dd-mm-yyyy)</label></td>
		</tr>
		<tr>
			<td>Data Fine</td>
			<td><input type="text" name="dataFine" ><label>(dd-mm-yyyy)</label></td>
		</tr>
		<tr>
			<td>Ora Inizio</td>
			<td>
				<select name="oraInizio">
					<%
						for(int x = 0; x < 24; x++){
							if(x < 10){
					%>
								<option value="<%="0"+x %>"><%="0"+x %></option>
					<%
							}else{
					%>
								<option value="<%=x %>"><%=x %></option>
					<%
							}
						}
					%>
				</select>
				<label>:</label>
				<select name="minutiInizio">
					<%
						for(int x = 0; x < 60; x++){
							if(x < 10){
					%>
								<option value="<%="0"+x %>"><%="0"+x %></option>
					<%
							}else{
					%>
								<option value="<%=x %>"><%=x %></option>
					<%
							}
						}
					%>
				</select>
			</td>
		</tr>
		<tr>
			<td>Ora Fine</td>
			<td>
				<select name="oraFine">
					<%
						for(int x = 0; x < 24; x++){
							if(x < 10){
					%>
								<option value="<%="0"+x %>"><%="0"+x %></option>
					<%
							}else{
					%>
								<option value="<%=x %>"><%=x %></option>
					<%
							}
						}
					%>
				</select>
				<label>:</label>
				<select name="minutiFine">
					<%
						for(int x = 0; x < 60; x++){
							if(x < 10){
					%>
								<option value="<%="0"+x %>"><%="0"+x %></option>
					<%
							}else{
					%>
								<option value="<%=x %>"><%=x %></option>
					<%
							}
						}
					%>
				</select>
			</td>
		</tr>
		<tr>
			<td><input type="submit" value="inserisci evento" /></td>
		</tr>
	</table>
	</form>
<%
		}else if(azioneCorrente.equals("visualizzaEvento")){
			EventoDTO evento = (EventoDTO) request.getAttribute("evento");
%>
			
			<div class="subtitle ">Visualizza Evento</div>
			
			<div id="bluemenu" class="bluetabs">
				<ul>
					<li><a href="./GestioneCalendarioEventi?azione=modificaEvento&evento=<%=evento.getId() %>">Modifica Evento</a></li>
					<li><a href="./GestioneCalendarioEventi?azione=eliminaEvento&evento=<%=evento.getId() %>" onclick="return confirm('Vuoi eliminare questo evento?');">Elimina Evento</a></li>
				</ul>
			</div>
			
			<fieldset>
				<legend>Visualizza Evento</legend>
				<table align="center">
					<tr>
						<td>Titolo</td>
						<td><label><%=evento.getTitle() %></td>
					</tr>
					<tr>
						<td>Data Inizio</td>
						<td><label><%=evento.getStart() %></label></td>
					</tr>
					<tr>
						<td>Data Fine</td>
						<td><label><%=evento.getEnd() %></label></td>
					</tr>
					<tr>
						<td>Ora Inizio</td>
						<td>
							<label><%=evento.getOraInizio() %></label>
						</td>
					</tr>
					<tr>
						<td>Ora Fine</td>
						<td>
							<label><%=evento.getOraFine() %></label>
						</td>
					</tr>
				</table>
			</fieldset>
<%			
		}else if(azioneCorrente.equals("modificaEvento")){
			EventoDTO evento = (EventoDTO) request.getAttribute("evento");
%>
			<div class="subtitle ">Modifica Evento</div>
			
			<div id="bluemenu" class="bluetabs">
				<ul>
					<li><a href="./GestioneCalendarioEventi?azione=visualizzaEvento&evento=<%=evento.getId() %>">Dettaglio Evento</a></li>
					<li><a href="./GestioneCalendarioEventi?azione=eliminaEvento&evento=<%=evento.getId() %>" onclick="return confirm('Vuoi eliminare questo evento?');">Elimina Evento</a></li>
				</ul>
			</div>
			
		<fieldSet>
			<legend>Modifica Evento</legend>
			<form action="./GestioneCalendarioEventi" method="post">
				<input type="hidden" name="azione" value="aggiornaEvento">
				<input type="hidden" name="evento" value="<%=evento.getId() %>">
				<table align="center">
					<tr>
						<td>Titolo</td>
						<td><input type="text" name="titolo" value="<%=evento.getTitle()%>"></td>
					</tr>
					<tr>
						<td>Data Inizio</td>
						<td><input type="text" name="dataInizio" value="<%=evento.getStart()%>"><label>(dd-mm-yyyy)</label></td>
					</tr>
					<tr>
						<td>Data Fine</td>
						<td><input type="text" name="dataFine" value="<%=evento.getEnd()%>"><label>(dd-mm-yyyy)</label></td>
					</tr>
					<tr>
						<td>Ora Inizio</td>
							<td>
								<select name="oraInizio">
									<%
										String [] tempoIniziale = evento.getOraInizio().split(":");
										for(int x = 0; x < 24; x++){
											if(x < 10){
												if(tempoIniziale[0].equals("0"+x)){ 
									%>
													<option value="<%="0"+x %>" selected="selected"><%="0"+x %></option>
									<%
												}else{
									%>				
													<option value="<%="0"+x %>"><%="0"+x %></option>
									<%			
												}
											}else{
												if(tempoIniziale[0].equals(String.valueOf(x))){ 
									%>
													<option value="<%=x %>" selected="selected"><%=x %></option>
									<%
												}else{
									%>				
													<option value="<%=x %>"><%=x %></option>
									<%			
												}
											}
										}
									%>
								</select>
								<label>:</label>
								<select name="minutiInizio">
									<%
										for(int y = 0; y < 60; y++){
											if(y < 10){
												if(tempoIniziale[1].equals("0"+y)){ 
									%>
													<option value="<%="0"+y %>" selected="selected"><%="0"+y %></option>
									<%
												}else{
									%>				
													<option value="<%="0"+y %>"><%="0"+y %></option>
									<%			
												}
											}else{
												if(tempoIniziale[1].equals(String.valueOf(y))){ 
									%>
													<option value="<%=y %>" selected="selected"><%=y %></option>
									<%
												}else{
									%>				
													<option value="<%=y %>"><%=y %></option>
									<%			
												}
											}
										}
									%>
								</select>
								</td>
							</tr>
							<tr>
								<td>Ora Fine</td>
								<td>
									<select name="oraFine">
										<%
										String [] tempoFine = evento.getOraFine().split(":");
										for(int x = 0; x < 24; x++){
											if(x < 10){
												if(tempoFine[0].equals("0"+x)){ 
									%>
													<option value="<%="0"+x %>" selected="selected"><%="0"+x %></option>
									<%
												}else{
									%>				
													<option value="<%="0"+x %>"><%="0"+x %></option>
									<%			
												}
											}else{
												if(tempoFine[0].equals(String.valueOf(x))){ 
									%>
													<option value="<%=x %>" selected="selected"><%=x %></option>
									<%
												}else{
									%>				
													<option value="<%=x %>"><%=x %></option>
									<%			
												}
											}
										}
									%>
								</select>
								<label>:</label>
								<select name="minutiFine">
									<%
										for(int y = 0; y < 60; y++){
											if(y < 10){
												if(tempoIniziale[1].equals("0"+y)){ 
									%>
													<option value="<%="0"+y %>" selected="selected"><%="0"+y %></option>
									<%
												}else{
									%>				
													<option value="<%="0"+y %>"><%="0"+y %></option>
									<%			
												}
											}else{
												if(tempoIniziale[1].equals(String.valueOf(y))){ 
									%>
													<option value="<%=y %>" selected="selected"><%=y %></option>
									<%
												}else{
									%>				
													<option value="<%=y %>"><%=y %></option>
									<%			
												}
											}
										}
									%>
									</select>
								</td>
							</tr>
					<tr>
						<td><input type="submit" value="modifica evento" /></td>
					</tr>
				</table>
				</form>
			</fieldSet>
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