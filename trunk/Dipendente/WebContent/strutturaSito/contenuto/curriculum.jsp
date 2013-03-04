<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="it.dipendente.dto.RisorsaDTO"%>

<%
	HttpSession controlloUtenteLoggato = request.getSession();
	if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
%>


<div class="subtitle">Area Curriculum</div>

<div id="finestra" title="Esporta Pdf">
	<%@include file="esportaPdf.jsp" %>
</div>

<div id="anteprima" title="Anteprima Curriculum">
	<%@include file="anteprima.jsp" %>
</div>

<div class="spazioUltra">
	<a href="./GestioneCurriculum?azione=caricamentoCv&area=notAll&parametro=<%=((RisorsaDTO)request.getSession().getAttribute("utenteLoggato")).getIdRisorsa() %>&creazioneCv=<%=((RisorsaDTO)request.getSession().getAttribute("utenteLoggato")).isFlaCreazioneCurriculum() %>">
		<div class="contenuti">
			<div class="titoloContenuti">Gestione Curriculum Vitae</div>
			<div class="corpoContenuti">
				<img src="images/gestione.gif" class="immagini"><p>In questa sezione hai la possibilità di gestire il tuo "Curriculum Vitae".</p>
			</div>
		</div>
	</a>

	<a href="#" onclick="return openFinestra('<%=((RisorsaDTO)request.getSession().getAttribute("utenteLoggato")).getIdRisorsa() %>','<%=request.getParameter("azione") %>','all','esportaPDF')">
		<div class="contenuti">
			<div class="titoloContenuti">Esporta Pdf</div>
			<div class="corpoContenuti">
				<img src="images/icon_pdf.gif" class="immagini"><p>In questa sezione puoi esportare in formato Pdf il tuo "Curriculum Vitae".</p>
			</div>
		</div>
	</a>

	<a href="#" onclick="return openFinestra('<%=((RisorsaDTO)request.getSession().getAttribute("utenteLoggato")).getIdRisorsa() %>','<%=request.getParameter("azione") %>','all','anteprima')">
		<div class="contenuti">
			<div class="titoloContenuti">
				Anteprima C.V.
			</div>
			<div class="corpoContenuti">
				<img src="images/gestione_curriculum.gif" class="immagini"><p>In questa sezione puoi visualizzare l'anteprima a tua scelta del "Curriculum Vitae".</p>
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