<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%
HttpSession controlloUtenteLoggato = request.getSession();
if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
%>

<div class="subtitle ">
	<h2>Visualizza Calendario</h2>
</div>

<p class="spazio" align="center"><a href="http://drconsulting.tv/Chat/index.php?azione=login&tipoUtenza=admin" target="_blank"/>Entra in Chat</a></p>

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
			
			eventSources: [{
				url: './GestioneCalendarioEventi?azione=caricamentoEventi'
			}]
		});
		
	});

</script>


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