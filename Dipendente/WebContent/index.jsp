<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>

<html>
	<head>
		<title>DiErre Consulting Srl</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" type="text/css" href="css/Dr_Style.css">
		<link rel="stylesheet" type="text/css" href="css/Dr_Demo.css">
		<link rel="stylesheet" type="text/css" href="css/jquery-ui-1.7.2.custom.css">
		<link rel="stylesheet" type="text/css" href="css/menuRisorsa.css">
		<link rel="stylesheet" type="text/css" href="css/visualizzaCurriculum.css">
		<link rel="stylesheet" type="text/css" href="css/timeReport.css">
		<script type="text/javascript" src="script/jquery-1.3.2.min.js"></script>
		<script type="text/javascript" src="script/controlloForm.js"></script>
	</head>
	<body class="home">
		<div id="container" class="shadow">
			<div id="header">
				<%@include file="strutturaSito/menu/menuAlto.jsp" %> 
			</div>
			<div id="content">
				<div id="extra">
					<%@include file="strutturaSito/menu/menuSinistra.jsp" %> 
				</div>
				<div id="content_right">
			<%
				if(!StringUtils.isEmpty(azione)){
					if(azione.equals("benvenuto")){
			%>
					<%@include file="strutturaSito/contenuto/benvenuto.jsp" %>
			<%
					}else if(azione.equals("TimeReport")){
			%>
					<%@include file="strutturaSito/contenuto/areaTimeReport.jsp" %>
			<%
					}else if(azione.equals("visualizzaReport")){
			%>
					<%@include file="strutturaSito/contenuto/visualizzaTimeReport.jsp" %>
			<%
					}else if(azione.equals("compilaTimeReport")){
			%>
					<%@include file="strutturaSito/contenuto/timeReport.jsp" %>
			<%
					}else if(azione.equals("visualizzaProfilo") || azione.equals("aggiornaProfilo")){
			%>
					<%@include file="strutturaSito/contenuto/risorsa.jsp" %>
			<%
					}else if(azione.equals("areaPrivata")){
			%>
					<%@include file="strutturaSito/contenuto/areaPrivata.jsp" %>
			<%
					}else if(azione.equals("messaggio")){
			%>
					<%@include file="strutturaSito/contenuto/messaggio.jsp" %>
			<%
					}else if(azione.equals("cv")){
			%>
					<%@include file="strutturaSito/contenuto/curriculumVitae.jsp" %>
			<%
					}else if(azione.equals("cambioPassword")){
			%>
					<%@include file="strutturaSito/contenuto/cambioPassword.jsp" %>
			<%
					}else if(azione.equals("creazioneCv")){
			%>
					<%@include file="strutturaSito/contenuto/creazioneCv.jsp" %>
			<%
					}else if(azione.equals("modificaCurriculumRisorsa")){
			%>
					<%@include file="strutturaSito/contenuto/modificaCurriculum.jsp" %>
			<%
					}else if(azione.equals("modificaSingoliCampiCurriculum")){
			%>
					<%@include file="strutturaSito/contenuto/modificaSingoliCampiCurriculum.jsp" %>
			<%
					}else if(azione.equals("visualizzaCurriculumRisorsa")){
			%>	
					
			<%
					}else if(azione.equals("dettaglioSingoliCampiCurriculum")){
			%>	
					<%@include file="strutturaSito/contenuto/dettaglioSingoliCampiCurriculum.jsp" %>
			<%
					}
				}else{
%>
<%@include file="strutturaSito/contenuto/benvenuto.jsp" %>
<%
}
%>
				</div>
				<div class="blank"></div>
			</div>
			<div id="footer" class="space">
				<span id="Copyright"> &copy; Copyright - All Rights Reserved - DiErre Consulting Srl - <a href="index.jsp?azione=mappaSito">Mappa del Sito</a></span>
			</div>
		</div>
	</body>
</html>