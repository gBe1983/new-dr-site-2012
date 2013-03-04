<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

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
		<link rel="stylesheet" type="text/css" href="css/menuRisorsa.css">
		<link rel="stylesheet" type="text/css" href="css/visualizzaCurriculum.css">
		<link rel="stylesheet" type="text/css" href="css/timeReport.css">
		<link rel="stylesheet" type="text/css" href="css/fullcalendar.css">
		<link rel="stylesheet" type="text/css" href="css/jquery-bubble-popup-v3.css">
		<link rel="stylesheet" type="text/css" href="css/menuLaterale.css">
		<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.1/themes/base/jquery-ui.css" />

		<!-- javascript -->
		<script type="text/javascript" src="script/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="script/jquery-ui-1.8.23.custom.min.js"></script>
		<script type="text/javascript" src="script/jquery-bubble-popup-v3.min.js"></script>
		<script type="text/javascript" src="script/jquery-1.9.1.js"></script>
		<script type="text/javascript" src="script/jquery-ui.js"></script>
		<script type="text/javascript" src="script/controlloForm.js"></script>
		<script type="text/javascript" src="script/fullcalendar.min.js"></script>
		<script type="text/javascript" src="script/caricamentoEventi.js"></script>
		<script type="text/javascript" src="script/curriculum.js"></script>
		
	</head>
	<script type='text/javascript'>

		$(document).ready(function() {
		
			var date = new Date();
			var d = date.getDate();
			var m = date.getMonth();
			var y = date.getFullYear();
	
			//caricamentoEventi();
			$('#calendar').fullCalendar({
				header: {
					left: 'prevYear,prev,next,nextYear',
					center: 'title',
					right: 'month'
				},
				editable: true,
				
				events: {
					url: './GestioneCalendarioEventi?azione=caricamentoEventi'
				},
				eventMouseover: function (event, element) {
					
					var dt = new Date(event.start);
			    	var giorno = dt.getDay()-1; 
					var mese = dt.getMonth() + 1;
					var anno = dt.getFullYear(); 
					var ora = dt.getHours(); 
					var minuti = dt.getMinutes(); 
					var secondi = dt.getSeconds();
	
					if(giorno < 10){
						giorno = "0" + giorno;
			    	}
			    	if(mese < 10){
						mese = "0" + mese;
			    	}
					if(ora < 10){
						ora = "0" + ora;
					}
					if(minuti < 10){
						minuti = "0" + minuti;
					}
	
					secondi = "0" + secondi;
	
					var dataInizio = giorno + "-" + mese + "-" + anno + " " + ora + ":" + minuti + ":" + secondi;
					
					var dtFine = new Date(event.end);
			    	var giornoFine = dtFine.getDay()-1; 
					var meseFine = dtFine.getMonth() + 1;
					var annoFine = dtFine.getFullYear(); 
					var oraFine = dtFine.getHours(); 
					var minutiFine = dtFine.getMinutes(); 
					var secondiFine = dtFine.getSeconds();
	
					if(giornoFine < 10){
						giornoFine = "0" + giornoFine;
			    	}
			    	if(meseFine < 10){
			    		meseFine = "0" + meseFine;
			    	}
					if(oraFine < 10){
						oraFine = "0" + oraFine;
					}
					if(minutiFine < 10){
						minutiFine = "0" + minutiFine;
					}
	
					secondiFine = "0" + secondiFine;
	
					var dataFine = giornoFine + "-" + meseFine + "-" + annoFine + " " + oraFine + ":" + minutiFine + ":" + secondiFine;
										
					$("."+event.className).CreateBubblePopup({
	
						position : 'top',
						align	 : 'center',
						
						innerHtml: 'Titolo: ' + event.title + "<br /> Inizio Cons.: " + dataInizio + "<br /> Fine Cons.: " + dataFine + "<br /> Consulente: " + event.nominativo,
	
						innerHtmlStyle: {
											color:'#00000', 
											'text-align':'center'
										},
						themeName: 'all-black',
						themePath: 'images'
					});
			    },
			    eventMouseout: function (event) {
			    	$("."+event.className).RemoveBubblePopup();	
			    }
			});

			var altezzaCorpo = document.getElementById("content_right").offsetHeight;
			
			if(altezzaCorpo <= 450){
				document.getElementById("extra").style.height='450px';
			}else{
				document.getElementById("extra").style.height=altezzaCorpo+'px';
			}
			
			/* Cambiare l'effetto da utilizzare */
			$.easing.def = "easeOutBounce";

			/* Associare una funzione all'evento click sul link */
			$('li.title a').click(function(e){
			
				/* Finding the drop down list that corresponds to the current section: */
				var subMenu = $(this).parent().next();
				
				/* Trovare il sotto menu che corrisponde alla voce cliccata */
				$('.sub-menu').not(subMenu).slideUp('slow');
				subMenu.stop(false,true).slideToggle('slow');
				
				/* Prevenire l'evento predefinito (che sarebbe di seguire il collegamento) */
				e.preventDefault();
			});
			
		});
	
	</script>
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
					}else if(azione.equals("cambioPassword")){
			%>
						<%@include file="strutturaSito/contenuto/cambioPassword.jsp" %>
			<%
					}else if(azione.equals("consulenzaOnline")){
		    %>	
						<%@include file="strutturaSito/contenuto/consulenzaOnline.jsp" %>
			<%  
					}else if(azione.equals("inserisciEvento") || azione.equals("visualizzaEvento") || azione.equals("modificaEvento")){
		    %>	
						<%@include file="strutturaSito/contenuto/inserisciEvento.jsp" %>
			<%  
					}else if(azione.equals("anteprimaCurriculum")){
			%>
						<jsp:include page="strutturaSito/contenuto/anteprimaCurriculum.jsp" />
			<%
					}else if(azione.equals("creaCv")){
			%>
						<jsp:include page="strutturaSito/contenuto/creaCv.jsp" />
			<%
					}else if(azione.equals("anteprimaCurriculum")){
			%>
						<jsp:include page="strutturaSito/contenuto/anteprimaCurriculum.jsp" />
			<%
					}else if(azione.equals("dettaglioCurriculum")){
			%>
						<jsp:include page="strutturaSito/contenuto/dettaglioCurriculum.jsp" />
			<%
					}else if(azione.equals("gestioneSingoleSezioniCurriculum")){
			%>
						<jsp:include page="strutturaSito/contenuto/gestioneSingoleSezioniCurriculum.jsp" />
			<%
					}else if(azione.equals("gestioneAnteprimeSezioniCurriculum")){
			%>
						<jsp:include page="strutturaSito/contenuto/gestioneAnteprimeSezioniCurriculum.jsp" />
			<%
					}else if(azione.equals("curriculum")){
			%>
						<jsp:include page="strutturaSito/contenuto/curriculum.jsp" />
			<%
					}else if(azione.equals("consulenza")){
			%>
						<jsp:include page="strutturaSito/contenuto/consulenza.jsp" />
			<%
					}else if(azione.equals("report")){
			%>
						<jsp:include page="strutturaSito/contenuto/report.jsp" />
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