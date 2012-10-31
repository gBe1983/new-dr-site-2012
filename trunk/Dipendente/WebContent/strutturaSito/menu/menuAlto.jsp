<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="it.dipendente.dto.RisorsaDTO"%>

<div id="header">
	<%
	HttpSession sessioneUtente = request.getSession();
	if(sessioneUtente.getAttribute("utenteLoggato") != null){
			RisorsaDTO risorsa = (RisorsaDTO) sessioneUtente.getAttribute("utenteLoggato");
	%>
			<div id="benvenuto">
				<span id="scrittaBenvenuto"> Benvenuto: </span> <span><%=risorsa.getNome() + " " + risorsa.getCognome() %> </span>
			</div>	
	<%
		}
	%>
	<div class="logo">	
		<img src="images/logo_DierreConsulting.bmp">	
	</div>
	<div class="space">&nbsp;</div><nav>	
	<div class="menu">	
		<ul>
			<li><a href="index.jsp?azione=benvenuto" <% if(request.getParameter("azione") != null && request.getParameter("azione").equals("benvenuto")){ out.print("id='on'");} %>>Home</a></li>
			<li><a href="./GestioneCurriculum?azione=verificaCreazioneCurriculum&dettaglio=false" <% if(request.getParameter("azione") != null && request.getParameter("azione").equals("cv")){ out.print("id='on'");} %>>Curriculum Vitae</a></li>		   
			<li><a href="./GestioneReport?azione=caricamentoCommesse" <% if(request.getParameter("azione") != null && request.getParameter("azione").equals("TimeReport")){ out.print("id='on'");} %>>Commesse</a></li>           
			<li><a href="index.jsp?azione=areaPrivata" <% if(request.getParameter("azione") != null && request.getParameter("azione").equals("areaPrivata")){ out.print("id='on'");} %>>Area Privata</a></li>
			<li><a href="./GestioneRisorsa?azione=logout">Logout</a></li>
		</ul>
	</div>	
</div>