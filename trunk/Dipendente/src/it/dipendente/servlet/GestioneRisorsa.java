package it.dipendente.servlet;

import it.dipendente.dao.RisorsaDAO;
import it.dipendente.dto.RisorsaDTO;
import it.util.log.MyLogger;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class GestioneRisorsa extends BaseServlet {
	private static final long serialVersionUID = -9074701279617210593L;
	private Logger log;

	public GestioneRisorsa() {
		super();
		log = Logger.getLogger(GestioneRisorsa.class);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("metodo: get");
		processRequest(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("metodo: Post");
		processRequest(request,response);
	}

	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		log.info("metodo: processRequest");
		
		HttpSession sessione = request.getSession();
		RisorsaDAO rDAO = new RisorsaDAO(conn.getConnection());
		String azione = request.getParameter("azione");
		
		if(azione.equals("login")){
			
			log.info("-------------------------------------------------------------------------------------------------------");
			log.info("azione: login");
			
			int idRisorsa = Integer.parseInt(request.getParameter("parametro"));
			sessione.setAttribute("utenteLoggato",rDAO.loginRisorsa(idRisorsa));
			
			log.info("url: ./index.jsp?azione=benvenuto");
			
			response.sendRedirect("./index.jsp?azione=benvenuto");
		}
		
		if(sessione.getAttribute("utenteLoggato") != null){
			
			if(azione.equals("visualizzaProfilo") || azione.equals("aggiornaProfilo")){
				
				log.info("-------------------------------------------------------------------------------------------------------");
				log.info("azione: "+azione);
				
				RisorsaDTO risorsa = rDAO.visualizzaProfilo(((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa());
				request.setAttribute("risorsa", risorsa);
				getServletContext()
					.getRequestDispatcher(azione.equals("visualizzaProfilo")?
											"/index.jsp?azione=visualizzaProfilo&dispositiva=areaPrivata":
											"/index.jsp?azione=aggiornaProfilo&dispositiva=areaPrivata")
						.forward(request, response);
			}else if(azione.equals("modificaRisorsa")){
				
				log.info("-------------------------------------------------------------------------------------------------------");
				log.info("azione: "+azione);
				
				RisorsaDTO risorsa =
					new RisorsaDTO(	((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa(),
									request.getParameter("cognome"),
									request.getParameter("nome"),
									request.getParameter("dataNascita"),
									request.getParameter("luogoNascita"),
									request.getParameter("sesso"),
									request.getParameter("codiceFiscale"),
									request.getParameter("mail"),
									request.getParameter("telefono"),
									request.getParameter("cellulare"),
									request.getParameter("fax"),
									request.getParameter("indirizzo"),
									request.getParameter("citta"),
									request.getParameter("provincia"),
									request.getParameter("cap"),
									request.getParameter("nazione"),
									request.getParameter("militare"),
									request.getParameter("patente"),
									request.getParameter("costo"),
									request.getParameter("occupato").equals("si"),
									request.getParameter("tipo_contratto"),
									request.getParameter("figuraProfessionale"),
									request.getParameter("seniority"),
									true,
									true,
									true);
				String messaggio = rDAO.modificaRisorsa(risorsa);
				request.setAttribute("messaggio", messaggio.equals("ok")?
													"La modifica del profilo è avvenuta correttamente":
													messaggio);
				log.info("url: /index.jsp?azione=messaggio&dispositiva=areaPrivata");
				
				getServletContext()
					.getRequestDispatcher("/index.jsp?azione=messaggio&dispositiva=areaPrivata")
						.forward(request, response);
			}else if(azione.equals("modificaPassword")){
				
				log.info("-------------------------------------------------------------------------------------------------------");
				log.info("azione: "+azione);
				
				String messaggio = rDAO.modificaPassword(	((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa(),
															request.getParameter("nuovaPassword"));
				request.setAttribute("messaggio", messaggio.equals("ok")?
													"La modifica della password è avvenuta correttamente":
													messaggio);
				
				log.info("url: /index.jsp?azione=messaggio&dispositiva=areaPrivata");
				
				getServletContext()
					.getRequestDispatcher("/index.jsp?azione=messaggio&dispositiva=areaPrivata")
						.forward(request, response);
			}else if(azione.equals("logout")){
				
				log.info("-------------------------------------------------------------------------------------------------------");
				log.info("azione: "+azione);
				
				clearSession(sessione);
				response.sendRedirect(prop.getProperty("siteUrl"));
			}
		}else{
			sessioneScaduta(response);
		}
	}
}