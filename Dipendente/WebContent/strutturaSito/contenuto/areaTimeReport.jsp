<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.Calendar"%>

<%
HttpSession controlloUtenteLoggato = request.getSession();
if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
%>

<div class="subtitle ">
	<h2>Area Commesse</h2>
</div>
<p>
	In questa sezione potrete gestire tutto quello ha a che fare con la gestione del Time Sheet
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
