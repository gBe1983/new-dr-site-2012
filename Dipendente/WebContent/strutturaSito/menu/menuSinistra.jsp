<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.dipendente.dto.Associaz_Risors_Comm_DTO"%>
<%@page import="it.dipendente.dto.RisorsaDTO"%>

<%
	HttpSession sessioneMenuLaterale = request.getSession();
	String dispositiva = "";
	
	if(request.getParameter("dispositiva") != null){
			dispositiva = request.getParameter("dispositiva");
	}
	
if(request.getSession().getAttribute("utenteLoggato") != null){
	if(request.getParameter("azione") != null){
	
%>
		<div class="newsbox">
			<div id="main"> 
				<ul class="menuLaterale">
					<li class="menuPrincipale">
						<ul>
							<li class="title"><a href="#">Curriculum</a></li>
							<li class="sub-menu">
								<ul>
									<li><a href="./GestioneCurriculum?azione=caricamentoCv&parametro=<%=((RisorsaDTO)request.getSession().getAttribute("utenteLoggato")).getIdRisorsa() %>&creazioneCv=<%=((RisorsaDTO)request.getSession().getAttribute("utenteLoggato")).isFlaCreazioneCurriculum() %>">Gestione C.V.</a></li>
									<li><a href="#">Esporta in Pdf</a></li> 
									<li><a href="#">Anteprima</a></li>
								</ul>
							</li>
						</ul>
					</li>
					<li class="menuPrincipale">
						<ul>
							<li class="title"><a href="#">Time Report</a></li>
							<li class="sub-menu">
								<ul>
									<li><a href="#" >Visualizza Report</a></li> 
								</ul>
							</li>
						</ul>
					</li>
					<li class="menuPrincipale">
						<ul>
							<li class="title"><a href="#">Area Privata</a></li>
							<li class="sub-menu">
								<ul>
									<li><a href="./GestioneRisorsa?azione=visualizzaProfilo">Visualizza Profilo</a></li>
				    				<li><a href="./GestioneRisorsa?azione=aggiornaProfilo">Modifica Profilo</a></li>
				    				<li><a href="index.jsp?azione=cambioPassword&dispositiva=areaPrivata">Cambia Password</a></li> 
								</ul>
							</li>
						</ul>
					</li>
					<li class="menuPrincipale">
						<ul>
							<li class="title"><a href="#">Consulenza</a></li>
							<li class="sub-menu">
								<ul>
									<li><a href="index.jsp?azione=inserisciEvento" >Inserisci Evento</a></li>
									<li><a href="index.jsp?azione=consulenzaOnline">Calendario</a></li>
									<li><a href="#" target="_blank"/>Entra in Chat</a></li> 
								</ul>
							</li>
						</ul>
					</li>
				</ul>
			</div>
		</div>
<%
	}
}
%>