<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="it.dipendente.dto.RisorsaDTO"%>

<%
HttpSession controlloUtenteLoggato = request.getSession();
if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
	RisorsaDTO risorsa = (RisorsaDTO) controlloUtenteLoggato.getAttribute("utenteLoggato");
%>

<div class="subtitle ">
	<h2>Benvenuto <%=risorsa.getNome() %> <%=risorsa.getCognome() %></h2>
</div>
<p>
	In questa area, dedicata a tutti i dipendenti, 
	potrai gestire varie sezioni tra cui <b><i>"Curriculum Vitae"</i></b> dove ti permette di personalizzare il tuo curriculum
	con tutte le esperienze conseguite nell'arco della carriera lavorativa, 
	<b><i>"Commesse"</i></b> ti permette di caricare le ore lavorative conseguite nel mese corrente e infine abbiamo l'<b><i>"Area Privata"</i></b> 
	dove troverai tutte le voce legate al proprio profilo. 
</p>


<%
}else{
%>
	<script type="text/javascript">
		controlloSessioneAttiva();
	</script>
<%
}
%>