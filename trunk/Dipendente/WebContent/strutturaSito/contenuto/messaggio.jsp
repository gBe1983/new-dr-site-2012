<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="it.dipendente.dto.RisorsaDTO"%>


<p align="center"><%=request.getAttribute("messaggio").toString() %> <br><br>
<% 
HttpSession sessioneMessaggio = request.getSession();

if((RisorsaDTO)sessioneMessaggio.getAttribute("utenteLoggato") != null){ %>
		<a href="index.jsp?azione=benvenuto">Home</a>
<%
	}
%>
</p>