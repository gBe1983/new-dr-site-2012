<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%
HttpSession controlloUtenteLoggato = request.getSession();
if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
%>

<div class="subtitle">Visualizza Calendario</div>


<div class="spazio"><p></p></div>

<div id='calendar'>
	
</div>

<%
	}else{
%>
		<script type="text/javascript">
			controlloSessioneAttiva();
		</script>
<%
	}
%>