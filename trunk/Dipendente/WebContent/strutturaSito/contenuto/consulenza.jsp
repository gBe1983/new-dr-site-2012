<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%
HttpSession controlloUtenteLoggato = request.getSession();
if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
%>


<div class="subtitle ">Area Consulenza</div>

<div class="spazioUltra">
	<a href="index.jsp?azione=inserisciEvento">
		<div class="contenuti">
			<div class="titoloContenuti">Inserisci Evento</div>
			<div class="corpoContenuti">
				<img src="images/add_evento.gif" class="immagini"><p>In questa sezione hai la possibilità prenotare il supporto tecnico che l'azienda mette a disposizione per le persone.</p>
			</div>
		</div>
	</a>

	<a href="index.jsp?azione=consulenzaOnline">
		<div class="contenuti">
			<div class="titoloContenuti">Visualizza Calendario</div>
			<div class="corpoContenuti">
				<img src="images/icona-calendario.jpg" class="immagini"><p>In questa sezione puoi visualizzare gli eventi di tutti i dipendenti dell'azienda.</p>
			</div>
		</div>
	</a>

	<a href="#" target="_blank">
		<div class="contenuti">
			<div class="titoloContenuti">Entra in Chat</div>
			<div class="corpoContenuti">
				<img src="images/chat.png" class="immagini"><p>In questa sezione entri nella chat per dare supporto tecnico alle persone.</p>
			</div>
		</div>
	</a>

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




