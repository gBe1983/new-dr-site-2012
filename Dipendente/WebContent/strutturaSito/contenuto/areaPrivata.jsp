<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%
HttpSession controlloUtenteLoggato = request.getSession();
if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
%>


<div class="subtitle ">Area Privata</div>

<div class="spazioUltra">
	<a href="./GestioneRisorsa?azione=visualizzaProfilo">
		<div class="contenuti">
			<div class="titoloContenuti">Visualizza Profilo</div>
			<div class="corpoContenuti">
				<img src="images/dettaglio.gif" class="immagini"><p>In questa sezione hai la possibilità di visualizzare il tuo profilo.</p>
			</div>
		</div>
	</a>

	<a href="./GestioneRisorsa?azione=aggiornaProfilo">
		<div class="contenuti">
			<div class="titoloContenuti">Modifica Profilo</div>
			<div class="corpoContenuti">
				<img src="images/gestione.gif" class="immagini"><p>In questa sezione puoi modificare il tuo profilo.</p>
			</div>
		</div>
	</a>

	<a href="index.jsp?azione=cambioPassword&dispositiva=areaPrivata">
		<div class="contenuti">
			<div class="titoloContenuti">Modifica Password</div>
			<div class="corpoContenuti">
				<img src="images/cambia_password.gif" class="immagini"><p>In questa sezione puoi modificare la tua password d'accesso.</p>
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