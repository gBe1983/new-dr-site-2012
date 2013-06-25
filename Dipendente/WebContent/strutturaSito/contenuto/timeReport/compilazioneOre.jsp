<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.HashMap"%>

<%
	if(request.getAttribute("flagAssenze") != null){
		HashMap<String,Boolean> flagAssenze = (HashMap<String,Boolean>) request.getAttribute("flagAssenze");
%>


<form action="./GestioneReport" method="post" name="compilaOre">
	<input type="hidden" name="parametroCommessa" value="<%=request.getAttribute("parametroCommessa").toString() %>" id="parameter">
	<input type="hidden" name="azione" value="">
	<input type="hidden" name="parametro" value="">
	<input type="hidden" name="parametri" value="">
	<input type="hidden" name="mese" value="">
	<input type="hidden" name="anno" value="">
	<div id="oreOrdinare">
		<table border="0" align="center">
			<tr>
				<td>Ore Ordinarie</td>
				<td><input type="text" name="oreOrd" size="5" value="0.0"></td>
				<td></td>
			</tr>
			<tr>
				<td>Ore Straordinarie</td>
				<td><input type="text" name="oreStrao" size="5" value="0.0"></td>
				<td></td>
			</tr>
			<tr>
				<td>Assenze</td>
				<td><input type="text" name="assenze" size="5" value="0.0" onchange="oreInserite()"></td>
				<td>
					<select name="tipologiaAssenze">
						<option value="">-- Tipologia Assenza -- </option>
						<%
						if(flagAssenze.size() > 0){	
							if(flagAssenze.get("ferie")){
						%>
								<option value="ferie">Ferie</option>	
						<%
							}
							if(flagAssenze.get("permessi")){
						%>
								<option value="permessi">Permessi</option>	
						<%
							}
							if(flagAssenze.get("mutua")){
						%>
								<option value="mutua">Mutua</option>	
						<%
							}
							if(flagAssenze.get("permessiNonRetribuiti")){
						%>
								<option value="permessiNonRetribuiti">Permessi Non Retribuiti</option>	
						<%
							}
						}
						%>
					</select>				
				</td>
			</tr>
			<tr>
				<td>Note<br>(Max 1000 caratteri)</td>
				<td colspan="2"><textarea rows="10" cols="40" name="note">Inserisci qui le Note</textarea></td>
			</tr>
			<tr>
				<td colspan="3" class="save"><input type="submit" value="" onclick="return controlloOreInserite()"></td>
			</tr>
		</table>
	</div>
</form>
<%
}
%>