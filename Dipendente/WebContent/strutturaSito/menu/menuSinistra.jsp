<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.dipendente.dto.Associaz_Risors_Comm_DTO"%>

<%
	HttpSession sessioneMenuLaterale = request.getSession();
	String dispositiva = "";
	
	if(request.getParameter("dispositiva") != null){
			dispositiva = request.getParameter("dispositiva");
	}
	
	if(request.getParameter("azione") != null){
		if(request.getParameter("azione").equals("TimeReport") || dispositiva.equals("TimeReport")){
			ArrayList listaCommesseAttive = (ArrayList) request.getAttribute("listaCommesseAttive");
			ArrayList listaCommesseNonAttive = (ArrayList) request.getAttribute("listaCommesseNonAttive");
	%>
			
			
			<div class="newsbox">
			    <div class="subtitle">Gestione Area</div><br/>
			    	<ul>
				    	<li>
				    		<label>Commessa Aperta</label>
				    		<ul>
				    	<%
				    		Calendar calendario = Calendar.getInstance();
				    		int anno = calendario.get(Calendar.YEAR);
				    		int mese = calendario.get(Calendar.MONTH)+1;
				    		for(int x = 0; x < listaCommesseAttive.size(); x++){
				    			Associaz_Risors_Comm_DTO asscommessa = (Associaz_Risors_Comm_DTO)listaCommesseAttive.get(x);
				    	%>
				    			<li>
				    				<a href="./GestioneReport?azione=visualizzaMese&dispositiva=TimeReport&mese=<%=mese %>&anno=<%=anno %>&parametro=<%=asscommessa.getId_associazione() %>" ><%=asscommessa.getDescrizioneCommessa() %></a>
				    			</li>
				    	<%
				    		}
				    	%>
				    		</ul>
				    	</li>
				    </ul>
				    <ul>
				    	<li>
				    		<label>Commessa Chiusa</label>
				    		<ul>
				    	<%
				    		for(int x = 0; x < listaCommesseNonAttive.size(); x++){
				    			Associaz_Risors_Comm_DTO asscommessa = (Associaz_Risors_Comm_DTO)listaCommesseNonAttive.get(x);
				    	%>
				    			<li>
				    				<a href="./GestioneReport?azione=visualizzaMese&dispositiva=TimeReport&mese=<%=mese %>&anno=<%=anno %>&parametro=<%=asscommessa.getId_associazione() %>" ><%=asscommessa.getDescrizioneCommessa() %></a>
				    			</li>
				    	<%
				    		}
				    	%>
				    		</ul>
				    	</li>
				    </ul>
				<br><br><br><br><br>
			</div>
			<div class="blank"></div>
	<%
		}
		if(request.getParameter("azione").equals("modificaCurriculumRisorsa") || dispositiva.equals("modificaCurriculumRisorsa") || dispositiva.equals("modificaSingoliCampiCurriculum")){
	%>
					<div class="newsbox">
					    <div class="subtitle">Gestione Curriculum</div><br/>
					    	<li><a href="./GestioneCurriculum?azione=caricamentoCv&tipoCreazione=esperienze&dispositiva=cv">Visualizza Esperienze</a></li>
					    	<li><a href="./GestioneCurriculum?azione=caricamentoCv&tipoCreazione=dettaglioCv&dispositiva=cv">Visualizza Dettaglio Cv</a></li>
						<div class="blank"></div>
					</div>
					<div class="blank"></div>	
					
			<%	
		}
		
		if(request.getParameter("azione").equals("creazioneCv")){
			%>
					<div class="newsbox">
					    <div class="subtitle">Gestione Curriculum</div><br/>
					    	<li><a href="index.jsp?azione=creazioneCv&tipoCreazione=esperienze&dettaglio=<%=request.getParameter("dettaglio") %>&dispositiva=cv">Aggiungi Esperienze</a></li>
					    	<%
					    	if(request.getParameter("dettaglio") != null){
								if(!Boolean.parseBoolean(request.getParameter("dettaglio").toString())){
					    	%>
					    			<li><a href="index.jsp?azione=creazioneCv&tipoCreazione=dettaglioCv&dettaglio=<%=request.getParameter("dettaglio") %>&dispositiva=cv" >Aggiungi Dettaglio</a></li>
					    	<%
					    		}
					    	}
					    	%>
						<div class="blank"></div>
					</div>
					<div class="blank"></div>
			<%
		} 
		
		if(request.getParameter("azione").equals("areaPrivata") || dispositiva.equals("areaPrivata")){
	%>
			<div class="newsbox">
			    <div class="subtitle">Gestione Area</div><br/>
			    	<li><a href="./GestioneRisorsa?azione=visualizzaProfilo" >Visualizza Profilo</a></li>
			    	<li><a href="./GestioneRisorsa?azione=aggiornaProfilo">Modifica Profilo</a></li>
			    	<li><a href="index.jsp?azione=cambioPassword&dispositiva=areaPrivata">Cambia Password</a></li>
				<div class="blank"></div>
				<br><br><br><br><br>
			</div>
			<div class="blank"></div>
<%
		}
		
		if(request.getParameter("azione").equals("consulenzaOnline") || dispositiva.equals("consulenzaOnline")){
		%>
				<div class="newsbox">
				    <div class="subtitle">Gestione Area</div><br/>
				    	<li><a href="index.jsp?azione=inserisciEvento" >Inserisci Evento</a></li>
					<div class="blank"></div>
					<br><br><br><br><br>
				</div>
				<div class="blank"></div>
		<%
			}
			
		if(request.getParameter("azione").equals("inserisciEvento") || request.getParameter("azione").equals("visualizzaEvento") || request.getParameter("azione").equals("modificaEvento") || dispositiva.equals("inserisciEvento")){
		%>
				<div class="newsbox">
				    <div class="subtitle">Gestione Area</div><br/>
				    	<li><a href="index.jsp?azione=consulenzaOnline" >Visualizza Calendario</a></li>
					<div class="blank"></div>
					<br><br><br><br><br>
				</div>
				<div class="blank"></div>
		<%
			}
		
		if(request.getParameter("azione").equals("cv") || dispositiva.equals("cv")){
%>
			<div class="newsbox">
			    <div class="subtitle">Gestione Area</div><br/>
			    	<%
			    		if(sessioneMenuLaterale.getAttribute("verificaCreazioneCurriculum") != null){
				    		boolean verificaCreazioneCurriculum = Boolean.parseBoolean(sessioneMenuLaterale.getAttribute("verificaCreazioneCurriculum").toString());
				    		if(verificaCreazioneCurriculum){
				    	%>
				    			<li><a href="./GestioneCurriculum?azione=caricamentoCv&dispositiva=cv" >Modifica Curriculum</a></li>
				    	<%
				    		}else{
				    	%>
				    			<li><a href="./index.jsp?azione=creazioneCv&&dettaglio=false&dispositiva=cv" >Crea Curriculum</a></li>		
				    	<%	
				    		}
			    		}
				    	%>
			    	<li><a href="./GestioneCurriculum?azione=caricamentoCv&tipoCreazione=''&page=visualizzaCurriculum" >Visualizza Curriculum</a></li>
				<div class="blank"></div>
				<br><br><br><br><br>
			</div>
			<div class="blank"></div>	
		<%
		}else if(request.getParameter("azione").equals("benvenuto")){
		%>	
			<div class="newsbox">
			    <div class="subtitle">News</div><br/>
			    	Tenetevi liberi nei giorni
			    	<b><p align="center">27-11-2012</p></b>
			    	<b><p align="center">30-11-2012</p></b>
			    	grande evento Cena Aziendale e/o Aperativo.
			    	Non mancate.
			    	<br>
			    	
				<div class="blank"></div>
				<br><br><br><br><br>
			</div>
			<div class="blank"></div>
			
		<%	
		}
	}
%>

