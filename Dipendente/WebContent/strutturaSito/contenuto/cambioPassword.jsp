<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%
HttpSession controlloUtenteLoggato = request.getSession();
if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
%>

<div class="subtitle">Modifica Password</div>

<fieldset class="spazio">
	<legend>Modifcica Password</legend>
	<form action="./GestioneRisorsa" method="post" >
		<input type="hidden" name="azione" value="modificaPassword" />
		<table>
			<tr>
				<td><label>Nuova Password</label></td>
				<td><input type="password" name="nuovaPassword"></td>
			</tr>
			<tr>
				<td><label>Conferma Nuova Password</label></td>
				<td><input type="password" name="confermaNuovaPassword"></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="cambia Password" />
				<input type="reset" value="svuota campi"></td>
			</tr>
		</table>
	</form>
</fieldset>
<%
}else{
%>
	<script type="text/javascript">
		controlloSessioneAttiva();
	</script>
<%
}
%>