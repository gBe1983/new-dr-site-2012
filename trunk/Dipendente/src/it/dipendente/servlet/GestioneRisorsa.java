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

public class GestioneRisorsa extends BaseServlet {
	private static final long serialVersionUID = -9074701279617210593L;
	private MyLogger log;

	public GestioneRisorsa() {
		super();
		log =new MyLogger(this.getClass().getName());
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
		RisorsaDAO rDAO = new RisorsaDAO(conn.getConnection());
		String azione = request.getParameter("azione");
		
		if(azione.equals("login")){
			int idRisorsa = Integer.parseInt(request.getParameter("parametro"));
			log.debug(metodo, "login idRisorsa" + idRisorsa);
			sessione.setAttribute("utenteLoggato",rDAO.loginRisorsa(idRisorsa));
			response.sendRedirect("./index.jsp?azione=benvenuto");
		}
		
		if(sessione.getAttribute("utenteLoggato") != null){
			if(azione.equals("visualizzaProfilo") || azione.equals("aggiornaProfilo")){
				RisorsaDTO risorsa = rDAO.visualizzaProfilo(((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa());
				request.setAttribute("risorsa", risorsa);
				getServletContext()
					.getRequestDispatcher(azione.equals("visualizzaProfilo")?
											"/index.jsp?azione=visualizzaProfilo&dispositiva=areaPrivata":
											"/index.jsp?azione=aggiornaProfilo&dispositiva=areaPrivata")
						.forward(request, response);
			}else if(azione.equals("modificaRisorsa")){
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
				getServletContext()
					.getRequestDispatcher("/index.jsp?azione=messaggio&dispositiva=areaPrivata")
						.forward(request, response);
			}else if(azione.equals("modificaPassword")){
				String messaggio = rDAO.modificaPassword(	((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa(),
															request.getParameter("nuovaPassword"));
				request.setAttribute("messaggio", messaggio.equals("ok")?
													"La modifica della password è avvenuta correttamente":
													messaggio);
				getServletContext()
					.getRequestDispatcher("/index.jsp?azione=messaggio&dispositiva=areaPrivata")
						.forward(request, response);
			}else if(azione.equals("logout")){
				clearSession(sessione);
				response.sendRedirect(prop.getProperty("siteUrl"));
			}
		}else{
			sessioneScaduta(response);
		}
	}
}