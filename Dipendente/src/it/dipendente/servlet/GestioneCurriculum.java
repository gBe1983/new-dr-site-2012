package it.dipendente.servlet;


import it.dipendente.dao.CurriculumDAO;
import it.dipendente.dto.Dettaglio_Cv_DTO;
import it.dipendente.dto.EsperienzeDTO;
import it.dipendente.dto.RisorsaDTO;
import it.dipendente.util.MyLogger;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class GestioneCurriculum extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private MyLogger log;

	public GestioneCurriculum() {
		super();
		log =new MyLogger(this.getClass());
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final String metodo="doGet";
		log.start(metodo);
		processRequest(request,response);
		log.end(metodo);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final String metodo="doPost";
		log.start(metodo);
		processRequest(request,response);
		log.end(metodo);
	}

	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final String metodo="processRequest";
		log.start(metodo);
		HttpSession sessione = request.getSession();
		
		CurriculumDAO curriculum = new CurriculumDAO(conn.getConnection());
		
		RequestDispatcher rd = null;

		//recupero il parametro azione
		String azione = request.getParameter("azione");
		if(sessione.getAttribute("utenteLoggato") != null){
			if(azione.equals("verificaCreazioneCurriculum")){
				//verifico con questo metodo che la risorsa non abbia creato il curriculum
				boolean verificaCreazioneCurriculum = curriculum.verificaCreazioneCurriculum(((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa());
				
				sessione.setAttribute("verificaCreazioneCurriculum", verificaCreazioneCurriculum);
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=cv&dispositiva=cv");
				rd.forward(request, response);
				
				
			}else if(azione.equals("creazioneCurriculum") || azione.equals("aggiornaCurriculum")){
				
				
				curriculum.creazioneFlagCreazioneCurriculum(((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa());
				
				boolean flag_dettaglio_cv = false;
				if(request.getParameter("dettaglio") != null){
					flag_dettaglio_cv = Boolean.parseBoolean(request.getParameter("dettaglio"));
				}
				
				//recupero il valore dal parametro "TipoCreazione" e verifico che valore assume
				String tipoCreazione = request.getParameter("tipoCreazione");
				
				if(tipoCreazione.equals("esperienze")){
					
					//carico le esperienze 
					EsperienzeDTO esperienze = new EsperienzeDTO();
					esperienze.setPeriodo(request.getParameter("periodo"));
					esperienze.setAzienda(request.getParameter("azienda"));
					esperienze.setLuogo(request.getParameter("luogo"));
					esperienze.setDescrizione(request.getParameter("descrizione"));
					esperienze.setId_risorsa(((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa());
					
					//effettuo l'inserimento dell'Esperienza
					curriculum.inserimentoEsperienze(esperienze);
					
					//valorizzo la variabile Esperienze a "TRUE" per l'avvenuto inserimento
					request.setAttribute("esperienze", "true");
					
				}else if(tipoCreazione.equals("dettaglioCv")){
					
					/*
					 * recupero i valori dal form creazione Cv le voci Dettaglio_Cv
					 */
					
					Dettaglio_Cv_DTO dettaglio = new Dettaglio_Cv_DTO();
					dettaglio.setCapacita_professionali(request.getParameter("capacitaProfessionali"));
					dettaglio.setCompetenze_tecniche(request.getParameter("competenzeTecniche"));
					dettaglio.setLingue_Straniere(request.getParameter("lingue"));
					dettaglio.setIstruzione(request.getParameter("istruzione"));
					dettaglio.setFormazione(request.getParameter("formazione"));
					dettaglio.setInteressi(request.getParameter("interessi"));
					dettaglio.setId_risorsa(((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa());
					
					curriculum.inserimentoDettaglio(dettaglio);
					
					if(azione.equals("creazioneCurriculum")){
						request.setAttribute("dettaglioCv", "true");
						flag_dettaglio_cv = true;
					}else{
						request.setAttribute("dettaglioModificato", "true");
					}
				}
				
				/*
				 * mi serve caricare in sessione il curriculum della risorsa 
				 * perchè quando effettuo l'aggiornamento del curriculum aggiorno
				 * il curriculum
				 */
				
				if(azione.equals("aggiornaCurriculum")){
					ArrayList curriculumVitae = curriculum.caricamentoCurriculum(((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa());
				
					//inserisco il curriculum caricato in sessione 
					sessione.setAttribute("curriculumVitae", curriculumVitae);
				}
				
				/*
				 * effettuo questi tipi di controllo per verificare con quale url
				 * utilizza. 
				 */
				
				
				if(azione.equals("creazioneCurriculum") && request.getParameter("modalita") == null){
					rd = getServletContext().getRequestDispatcher("/index.jsp?azione=creazioneCv&tipoCreazione="+tipoCreazione+"&dettaglio="+flag_dettaglio_cv+"&dispositiva=cv");
					rd.forward(request, response);
				
				}else if(azione.equals("aggiornaCurriculum")){
					rd = getServletContext().getRequestDispatcher("/index.jsp?azione=modificaCurriculumRisorsa&tipoCreazione="+tipoCreazione+"&dispositiva=cv");
					rd.forward(request, response);
					
				}else if(azione.equals("creazioneCurriculum") && request.getParameter("modalita") != null){
					
					request.setAttribute("altreCompetenzeModificate", "true");
					rd = getServletContext().getRequestDispatcher("/index.jsp?azione=modificaCurriculumRisorsa&tipoCreazione="+tipoCreazione+"&dispositiva=cv");
					rd.forward(request, response);
				}
				
			}else if(azione.equals("caricamentoCv")){
				//effettuo il caricamento del curriculum della singola risorsa
				
				ArrayList curriculumVitae = curriculum.caricamentoCurriculum(((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa());
				
				/*
				 * recupero questa tipo di parametro per differenziare la visualizzazione curriculum
				 * da il modifica curriculum
				 */
				String page = request.getParameter("page");
				
				//metto in sessione il curriculum
				sessione.setAttribute("curriculumVitae", curriculumVitae);
				
				if(request.getParameter("tipoCreazione") != null){
					if(page == null){
						if(request.getParameter("tipoCreazione").equals("esperienze")){
							response.sendRedirect("./index.jsp?azione=modificaCurriculumRisorsa&tipoCreazione=esperienze&dispositiva=cv");
						}else{
							response.sendRedirect("./index.jsp?azione=modificaCurriculumRisorsa&tipoCreazione=dettaglioCv&dispositiva=cv");
						}
					}else{
						response.sendRedirect("./strutturaSito/contenuto/visualizzaCurriculumRisorsa.jsp?azione=''&tipoCreazione=esperienze&dispositiva=risorsa");
					}
				}else{
					response.sendRedirect("./index.jsp?azione=modificaCurriculumRisorsa&tipoCreazione=esperienze&dispositiva=cv");
				}
			}else if(azione.equals("eliminaCampiCurriculum")){
				
				if(request.getParameter("tipologia").equals("esperienze")){
					
					int idEsperienze = Integer.parseInt(request.getParameter("idEsperienze"));
					
					curriculum.eliminazioneEsperienza(idEsperienze);
					
					ArrayList curriculumVitae = curriculum.caricamentoCurriculum(((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa());
					//inserisco il curriculum caricato in sessione
					sessione.setAttribute("curriculumVitae", curriculumVitae);
					
					request.setAttribute("esperienzeEliminata", "true");
					
					rd = getServletContext().getRequestDispatcher("/index.jsp?azione=modificaCurriculumRisorsa&tipoCreazione=esperienze&risorsa="+((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa());
					rd.forward(request, response);
					
					
				}
			}else if(azione.equals("modificaCurriculum")){
				
				String tipoModifica = request.getParameter("tipoModifica");
				
				if(tipoModifica.equals("esperienze")){
					
					
					//carico le esperienze 
					EsperienzeDTO esperienze = new EsperienzeDTO();
					esperienze.setPeriodo(request.getParameter("periodo"));
					esperienze.setAzienda(request.getParameter("azienda"));
					esperienze.setLuogo(request.getParameter("luogo"));
					esperienze.setDescrizione(request.getParameter("descrizione"));
					esperienze.setId_risorsa(((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa());
					esperienze.setIdEsperienze(Integer.parseInt(request.getParameter("idEsperienze")));
					
					//effettuo la modifica concreta dell'Esperienza
					String messaggio = curriculum.aggiornamentoEsperienza(esperienze);
					
					if(messaggio.equals("ok")){
						ArrayList curriculumVitae = curriculum.caricamentoCurriculum(((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa());
						//inserisco il curriculum caricato in sessione
						sessione.setAttribute("curriculumVitae", curriculumVitae);
						request.setAttribute("esperienzeModificata", "true");
						rd = getServletContext().getRequestDispatcher("/index.jsp?azione=modificaCurriculumRisorsa&tipoCreazione=esperienze&risorsa="+((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa()+"&dispositiva=modificaSingoliCampiCurriculum");
						rd.forward(request, response);
					}else{
						request.setAttribute("messaggio", messaggio);
						rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
						rd.forward(request, response);
					}
				}else if(tipoModifica.equals("dettaglioCv")){
					
					//recupero i valori della modifica del dettaglio
					Dettaglio_Cv_DTO dettaglio = new Dettaglio_Cv_DTO();
					dettaglio.setCapacita_professionali(request.getParameter("capacitaProfessionali"));
					dettaglio.setCompetenze_tecniche(request.getParameter("competenzeTecniche"));
					dettaglio.setLingue_Straniere(request.getParameter("lingue"));
					dettaglio.setIstruzione(request.getParameter("istruzione"));
					dettaglio.setFormazione(request.getParameter("formazione"));
					dettaglio.setInteressi(request.getParameter("interessi"));
					dettaglio.setId_risorsa(((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa());
					dettaglio.setId_dettaglio(Integer.parseInt(request.getParameter("id_dettaglio")));
					
					String messaggio = curriculum.aggiornamentoDettaglio(dettaglio);
					if(messaggio.equals("ok")){
						ArrayList curriculumVitae = curriculum.caricamentoCurriculum(((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa());
						//inserisco il curriculum caricato in sessione
						sessione.setAttribute("curriculumVitae", curriculumVitae);
						request.setAttribute("dettaglioModificato", "true");
						rd = getServletContext().getRequestDispatcher("/index.jsp?azione=modificaCurriculumRisorsa&tipoCreazione=dettaglioCv&risorsa="+((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa()+"&dispositiva=modificaSingoliCampiCurriculum");
						rd.forward(request, response);
					}else{
						request.setAttribute("messaggio", messaggio);
						rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
						rd.forward(request, response);
					}
					
				}
			}
		}else {
			sessioneScaduta(response);
		}
	}
}
