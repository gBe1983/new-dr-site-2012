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

<!-- fogli di stile -->
<link rel="stylesheet" type="text/css" href="css/Dr_Style.css">
<link rel="stylesheet" type="text/css" href="css/Dr_Demo.css">
<link rel="stylesheet" type="text/css" href="css/jquery-ui-1.7.2.custom.css">
<link rel="stylesheet" type="text/css" href="css/menuRisorsa.css">
<link rel="stylesheet" type="text/css" href="css/visualizzaCurriculum.css">
<link rel="stylesheet" type="text/css" href="css/timeReport.css">
<link rel="stylesheet" type="text/css" href="css/fullcalendar.css">

<!-- javascript -->
<script type="text/javascript" src="script/jquery-1.8.1.min.js"></script>
<script type="text/javascript" src="script/controlloForm.js"></script>
<script type="text/javascript" src="script/jquery-ui-1.8.23.custom.min.js"></script>
<script type="text/javascript" src="script/fullcalendar.min.js"></script>
<script type="text/javascript" src="script/caricamentoEventi.js"></script>
<script type="text/javascript">
function msg(message){
	if( message != null && "" != message && "null" != message){
		alert(message);
	}
}
</script>
</head>
<body class="home" onload="msg('<%=(String)request.getAttribute("msg")%>')">
	<div id="container" class="shadow">
		<div id="header">
			<%@include file="strutturaSito/menu/menuAlto.jsp"%>
		</div>
		<div id="content">
			<%
if(!StringUtils.isEmpty(azione)){
	if(azione.equals("benvenuto")){
			%>
			<jsp:include page="strutturaSito/contenuto/benvenuto.jsp"/>
			<%
	}else if(azione.equals("compilaTimeReport")){
			%>
			<jsp:include page="strutturaSito/contenuto/timeReport.jsp"/>
			<%
	}
}else{
%>
			<jsp:include page="strutturaSito/contenuto/benvenuto.jsp"/>
			<%
}
%>
		</div>
		<div id="footer" class="space">
			<span id="Copyright"> &copy; Copyright - All Rights Reserved - DiErre Consulting Srl - <a href="index.jsp?azione=mappaSito">Mappa del Sito</a>
			</span>
		</div>
	</div>
</body>
</html>