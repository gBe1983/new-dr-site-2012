<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="it.dipendente.dto.RisorsaDTO"%>

<div id="header">
	<%
	RisorsaDTO risorsaDTO = (RisorsaDTO)request.getSession().getAttribute("utenteLoggato");
	if(risorsaDTO!=null){
	%>
	<div id="benvenuto">
		<span id="scrittaBenvenuto"> Benvenuto: </span> <span><%=risorsaDTO.getNome() + " " + risorsaDTO.getCognome() %> </span>
	</div>	
	<%
		}
	%>
	<div class="logo">	
		<!-- <img src="images/logo_DierreConsulting.bmp">  -->
	</div>
	<div class="space">&nbsp;</div>
	<%
	String azione=request.getParameter("azione");
	%>
	<div id="nav">
		<div class="menu">
			<ul>
				<li><a href="index.jsp?azione=benvenuto"<%if("benvenuto".equals(azione)){out.print(" id='on'");}%>>Home</a></li>
				<li><a href="index.jsp?azione=curriculum" <%if("cv".equals(azione)){out.print(" id='on'");}%>>Curriculum Vitae</a></li>
				<li><a href="index.jsp?azione=report"<%if("TimeReport".equals(azione)){out.print(" id='on'");}%>>Time Report</a></li>
				<li><a href="index.jsp?azione=agenda" <% if(request.getParameter("azione") != null && request.getParameter("azione").equals("agenda")){ out.print("id='on'");} %>>Agenda</a></li>
				<li><a href="index.jsp?azione=areaPrivata"<%if("areaPrivata".equals(azione)){out.print(" id='on'");}%>>Area Privata</a></li>
			</ul>
		</div>
	</div>	
</div>