package it.dipendente.servlet;

import it.dipendente.bo.Month;
import it.dipendente.dao.Associaz_Risors_Comm_DAO;
import it.dipendente.dao.PlanningDAO;
import it.dipendente.dto.PlanningDTO;
import it.dipendente.dto.RisorsaDTO;
import it.dipendente.util.MyLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class GestioneReport extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private MyLogger log;

	public GestioneReport() {
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
		
		
		
		RequestDispatcher rd = null;
		
		
		
		String azione = request.getParameter("azione");
		
		//if(sessione.getAttribute("utenteLoggato") != null){
			
			if(azione.equals("compilaTimeReport")){
				PlanningDAO planningDAO = new PlanningDAO(conn.getConnection());
				//request.setAttribute("month", planningDAO.getGiornate(((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa()));
				request.setAttribute("month", new Month());
				getServletContext().getRequestDispatcher("/index.jsp?azione=compilaTimeReport").forward(request, response);
			}else if(azione.equals("salvaTimeReport")){
				PlanningDAO planningDAO = new PlanningDAO(conn.getConnection());
				Calendar calendario = Calendar.getInstance();
				
				
				int giorni = Integer.parseInt(request.getParameter("contatore"));
				int id_associazione = Integer.parseInt(request.getParameter("associazione"));
				int mese = Integer.parseInt(request.getParameter("mese"));
				int anno = Integer.parseInt(request.getParameter("anno"));
				
				for(int x = 0; x < giorni; x++){
					String ore = request.getParameter(String.valueOf(x+1));
					
					/*
					 * in questa sezione effettuo il carimento delle ore, commesse e delle note
					 * a seconda delle giornate caricate
					 */
					PlanningDTO planning = new PlanningDTO();
					
					if(mese < 10){
						if(x < 9){
							//planning.setData(anno+"-0"+mese+"-0"+(x+1));
							if(request.getParameter("numeroOre_"+x).indexOf(",") != -1){
								planning.setNumeroOre(Double.parseDouble(request.getParameter("numeroOre_"+x).replace(",", ".")));
							}else{
								planning.setNumeroOre(Double.parseDouble(request.getParameter("numeroOre_"+x)));
							}
							
							planning.setOrario(request.getParameter("orario_"+x));
							planning.setDescr_attivita(request.getParameter("descrizione_"+x));
							planning.setNote(request.getParameter("note_"+x));
						}else{
							//planning.setData(anno+"-0"+mese+"-"+(x+1));
							if(request.getParameter("numeroOre_"+x).indexOf(",") != -1){
								planning.setNumeroOre(Double.parseDouble(request.getParameter("numeroOre_"+x).replace(",", ".")));
							}else{
								planning.setNumeroOre(Double.parseDouble(request.getParameter("numeroOre_"+x)));
							}
							planning.setOrario(request.getParameter("orario_"+x));
							planning.setDescr_attivita(request.getParameter("descrizione_"+x));
							planning.setNote(request.getParameter("note_"+x));
						}
					}else{
						if(x < 9){
							//planning.setData(anno+"-"+mese+"-0"+(x+1));
							if(request.getParameter("numeroOre_"+x).indexOf(",") != -1){
								planning.setNumeroOre(Double.parseDouble(request.getParameter("numeroOre_"+x).replace(",", ".")));
							}else{
								planning.setNumeroOre(Double.parseDouble(request.getParameter("numeroOre_"+x)));
							}
							planning.setOrario(request.getParameter("orario_"+x));
							planning.setDescr_attivita(request.getParameter("descrizione_"+x));
							planning.setNote(request.getParameter("note_"+x));
						}else{
							//planning.setData(anno+"-0"+mese+"-"+(x+1));
							if(request.getParameter("numeroOre_"+x).indexOf(",") != -1){
								planning.setNumeroOre(Double.parseDouble(request.getParameter("numeroOre_"+x).replace(",", ".")));
							}else{
								planning.setNumeroOre(Double.parseDouble(request.getParameter("numeroOre_"+x)));
							}
							planning.setOrario(request.getParameter("orario_"+x));
							planning.setDescr_attivita(request.getParameter("descrizione_"+x));
							planning.setNote(request.getParameter("note_"+x));
						}
					}
					planning.setId_associazione(id_associazione);
					planningDAO.aggiornamentoPlanning(planning);
				}
				
				request.setAttribute("messaggio", "L'aggiornamento delle ore è avvenuto correttamente.");
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio&dispositiva=areaPrivata");
				rd.forward(request, response);
				
			}else if(azione.equals("caricamentoCommesse")){
				Associaz_Risors_Comm_DAO rDAO = new Associaz_Risors_Comm_DAO(conn.getConnection());
				/*
				 * qua mi carico le commesse attive legate alla risorsa
				 */
				ArrayList listaCommesseAttive = rDAO.caricamentoCommesseAttive(((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa());
				
				/*
				 * qua mi carico le commesse non attive legate alla risorsa
				 */
				ArrayList listaCommesseNonAttive = rDAO.caricamentoCommesseNonAttive(((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa());
				
				/*
				 * qua mi carico le commesse di tipologia "Altro" per veficare quali sono legate alla risorsa
				 */
				ArrayList commesseRisorse = rDAO.caricamentoCommesseAssociateRisorse(((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa());
				/*
				for(int i = 0; i < listaCommesse.size(); i++){
					Associaz_Risors_Comm_DTO asscommessa = (Associaz_Risors_Comm_DTO)listaCommesse.get(i);
					asscommessa.setDescrizioneCliente(rDAO.caricamentoDescrizioneCommessa(asscommessa.getId_commessa()));
					listaCommesse.set(i, asscommessa);
				}*/
				
				request.setAttribute("listaCommesseAttive", listaCommesseAttive);
				request.setAttribute("listaCommesseNonAttive", listaCommesseNonAttive);
				request.setAttribute("commesseRisorse", commesseRisorse);
				
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=TimeReport&dispositiva=TimeReport");
				rd.forward(request, response);
			}
		//}else{
		//	sessioneScaduta(response);
		//}
	}
}