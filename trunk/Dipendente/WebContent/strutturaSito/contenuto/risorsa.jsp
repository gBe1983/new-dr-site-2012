<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="it.dipendente.dto.RisorsaDTO"%>
    
<%
HttpSession controlloUtenteLoggato = request.getSession();
if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){

	if(request.getParameter("azione").equals("visualizzaProfilo")){
		RisorsaDTO risorsa = (RisorsaDTO) request.getAttribute("risorsa");
%>

<div class="subtitle ">Visualizza Profilo</div>
	
<fieldset class="spazio">
		<legend>Dati Anagrafici</legend>
		<table>
			<tr>
				<td><label>Cognome</label></td>
				<td><%=risorsa.getCognome() %></td>
			</tr>
			<tr>
				<td><label>Nome</label></td>
				<td><%=risorsa.getNome() %></td>
			</tr>
			<tr>
				<td><label>Data Nascita</label></td>
				<td><%=risorsa.getDataNascita() %></td>
			</tr>
			<tr>
				<td><label>Luogo Nascita</label></td>
				<td><%=risorsa.getLuogoNascita() %></td>
			</tr>
			<tr>
				<td><label>Sesso</label></td>
				<td><%=risorsa.getSesso() %></td>
			</tr>
			<tr>
				<td><label>Codice Fiscale</label></td>
				<td><%=risorsa.getCodiceFiscale() %></td>
			</tr>
			<tr>
				<td><label>Mail</label></td>
				<td><%=risorsa.getEmail() %></td>
			</tr>
			<tr>
				<td><label>Telefono</label></td>
				<td><%=risorsa.getTelefono() %></td>
			</tr>
			<tr>
				<td><label>Cellulare</label></td>
				<td><%=risorsa.getCellulare() %></td>
			</tr>
			<tr>
				<td><label>Fax</label></td>
				<td><%=risorsa.getFax() %></td>
			</tr>
		</table>
	</fieldset>	
	<fieldset>
		<legend>Residenza</legend>
		<table>
			<tr>
				<td><label>Indirizzo</label></td>
				<td><%=risorsa.getIndirizzo() %></td>
			</tr>
			<tr>
				<td><label>Città</label></td>
				<td><%=risorsa.getCitta() %></td>
			</tr>
			<tr>
				<td><label>Provincia</label></td>
				<td><%=risorsa.getProvincia() %></td>
			</tr>
			<tr>
				<td><label>Cap</label></td>
				<td><%=risorsa.getCap() %></td>
			</tr>
			<tr>
				<td><label>Nazione</label></td>
				<td><%=risorsa.getNazione() %></td>
			</tr>
			<tr>
				<td><label>Servizio Militare </label></td>
				<td><%=risorsa.getServizioMilitare() %></td>
			</tr>
		</table>
	</fieldset>
	<fieldset>
		<legend>Altri Dati</legend>
		<table>
			<tr>
				<td><label>Patente </label></td>
				<td><%=risorsa.getPatente() %></td>
			</tr>
			<tr>
				<td><label>Occupato</label></td>
				<td><%=risorsa.isOccupato() %></td>
			</tr>
			<tr>
				<td><label>Figura Professionale</label></td>
				<td>
					<%=risorsa.getFiguraProfessionale() %>
				</td>
			</tr>
			<tr>
				<td><label>Seniority</label></td>
				<td>
					<%=risorsa.getSeniority() %>
				</td>
			</tr>
		</table>
	</fieldset>
<%
	}else if(request.getParameter("azione").equals("aggiornaProfilo")){
		RisorsaDTO risorsa = (RisorsaDTO) request.getAttribute("risorsa");
%>

		<div class="subtitle ">Modifica Profilo</div>

		<form action="./GestioneRisorsa" method="post" class="spazio">
			<input type="hidden" name="azione" value="modificaRisorsa">
			<input type="hidden" name="idRisorsa" value="<%=risorsa.getIdRisorsa() %>">
			<fieldset>
				<legend>Dati Anagrafici</legend>
				<table>
					<tr>
						<td><label>Cognome</label></td>
						<td><input type="text" name="cognome" value="<%=risorsa.getCognome() %>"/></td>
					</tr>
					<tr>
						<td><label>Nome</label></td>
						<td><input type="text" name="nome" value="<%=risorsa.getNome() %>"/></td>
					</tr>
					<tr>
						<td><label>Data Nascita</label></td>
						<td><input type="text" name="dataNascita" value="<%=risorsa.getDataNascita() %>" maxlength="10" size="10"/></td>
					</tr>
					<tr>
						<td><label>Luogo Nascita</label></td>
						<td><input type="text" name="luogoNascita" value="<%=risorsa.getLuogoNascita() %>"/></td>
					</tr>
					<tr>
						<td><label>Sesso</label></td>
						<td>
							<%
								if(risorsa.getSesso().equals("m")){
							%>
								<input type="radio" name="sesso" value="m" checked="checked"/>Maschio
								<input type="radio" name="sesso" value="f"/>Femmina
							<%
								}else{
							%>
									<input type="radio" name="sesso" value="m"/>Maschio
									<input type="radio" name="sesso" value="f" checked="checked">Femmina
							<%
								}
							%>
						</td>
					</tr>
					<tr>
						<td><label>Codice Fiscale</label></td>
						<td><input type="text" name="codiceFiscale" value="<%=risorsa.getCodiceFiscale() %>"/></td>
					</tr>
					<tr>
						<td><label>Mail</label></td>
						<td><input type="text" name="mail" value="<%=risorsa.getEmail() %>"/></td>
					</tr>
					<tr>
						<td><label>Telefono</label></td>
						<td><input type="text" name="telefono" value="<%=risorsa.getTelefono() %>"/></td>
					</tr>
					<tr>
						<td><label>Cellulare</label></td>
						<td><input type="text" name="cellulare" value="<%=risorsa.getCellulare() %>"/></td>
					</tr>
					<tr>
						<td><label>Fax</label></td>
						<td><input type="text" name="fax" value="<%=risorsa.getFax() %>"/></td>
					</tr>
				</table>
			</fieldset>	
			<fieldset>
				<legend>Residenza</legend>
				<table>
					<tr>
						<td><label>Indirizzo</label></td>
						<td><input type="text" name="indirizzo" value="<%=risorsa.getIndirizzo() %>"/></td>
					</tr>
					<tr>
						<td><label>Città</label></td>
						<td><input type="text" name="citta" value="<%=risorsa.getCitta() %>"/></td>
					</tr>
					<tr>
						<td><label>Provincia</label></td>
						<td><input type="text" name="provincia" value="<%=risorsa.getProvincia() %>" maxlength="2" size="2"/></td>
					</tr>
					<tr>
						<td><label>Cap</label></td>
						<td><input type="text" name="cap" value="<%=risorsa.getCap() %>" maxlength="5" size="5"/></td>
					</tr>
					<tr>
						<td><label>Nazione</label></td>
						<td><input type="text" name="nazione" value="<%=risorsa.getNazione() %>"/></td>
					</tr>
					<tr>
						<td><label>Servizio Militare </label></td>
						<td>
							<select name="militare">
								<option value="">-- Seleziona Servizio Militare --</option>
								<option value="essente">Esente</option>
								<option value="esonerato">Esonerato</option>
								<option value="assolto">Assolto</option>
								<option value="obbligatorio">Obbligatorio</option>
								<option value="obiettore">Obiettore di Coscienza</option>
								<option value="altro">altro</option>
							</select>
						</td>
					</tr>
				</table>
			</fieldset>
			<fieldset>
				<legend>Altri Dati</legend>
				<table>
					<tr>
						<td><label>Patente </label></td>
						<td>
							<select   name="patente">
									<option value=" "> Seleziona</option>
									<option value="Nessuna">Nessuna</option>
									<option value="A">A</option>
									<option value="A1">A1</option>
									<option value="B">B</option>
									<option value="C">C</option>
									<option value="D">D</option>														
									<option value="BE">BE</option>
									<option value="CE">CE</option>														
									<option value="DE">DE</option>
							</select>
						</td>
					</tr>
					<tr>
						<td><label>Occupato</label></td>
						<td>
						<%
							if(risorsa.isOccupato()){ 
						%>
								<input type="radio" name="occupato" value="si" checked="checked">Si
								<input type="radio" name="occupato" value="no">No
						<%
							}else{
						%>
								<input type="radio" name="occupato" value="si">Si
								<input type="radio" name="occupato" value="no" checked="checked">No
						<%
							}
						%>		
						</td>
					</tr>
					<tr>
						<td><label>Figura Professionale</label></td>
						<td>
							<input type="text" name="figuraProfessionale" value="<%=risorsa.getFiguraProfessionale() %>">
						</td>
					</tr>
					<tr>
						<td><label>Seniority</label></td>
						<td>
							<select name="seniority">
						<%
							if(risorsa.getSeniority().equals("Junior")){
						%>
								<option value=" "> Seleziona</option>
								<option value="Junior" selected="selected">Junior</option>
								<option value="Senior">Senior</option>
						<%
							}else{
						%>
								<option value=" "> Seleziona</option>
								<option value="Junior" >Junior</option>
								<option value="Senior" selected="selected">Senior</option>
						<%
							}
						%>
							</select>
						</td>
					</tr>
				</table>
			</fieldset>
			<table>
				<tr>
					<td><input type="submit" value="Modifica Risorsa"></td>
					<td><input type="reset" value="svuota campi"></td>
				</tr>
			</table>
		</form>
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