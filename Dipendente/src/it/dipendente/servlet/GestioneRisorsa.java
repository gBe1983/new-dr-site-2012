package it.dipendente.servlet;

import it.dipendente.dao.RisorsaDAO;
import it.dipendente.dto.RisorsaDTO;
import it.dipendente.util.MyLogger;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class GestioneRisorsa extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private MyLogger log;

	public GestioneRisorsa() {
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
		RisorsaDAO rDAO = new RisorsaDAO(conn.getConnection());
		RequestDispatcher rd = null;
		//recupero il parametro azione
		String azione = request.getParameter("azione");
		
		if(azione.equals("login")){
			
			int idRisorsa = Integer.parseInt(request.getParameter("parametro"));
			
			log.debug(metodo, "login idRisorsa" + idRisorsa);
			
			RisorsaDTO risorsa = rDAO.loginRisorsa(idRisorsa);
			sessione.setAttribute("utenteLoggato",risorsa);
			response.sendRedirect("./index.jsp?azione=benvenuto");
			
		}
		if(sessione.getAttribute("utenteLoggato") != null){
			if(azione.equals("visualizzaProfilo") || azione.equals("aggiornaProfilo")){
				
				
				RisorsaDTO risorsa = rDAO.visualizzaProfilo(((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa());
				request.setAttribute("risorsa", risorsa);
				if(azione.equals("visualizzaProfilo")){
					rd = getServletContext().getRequestDispatcher("/index.jsp?azione=visualizzaProfilo&dispositiva=areaPrivata");
				}else{
					rd = getServletContext().getRequestDispatcher("/index.jsp?azione=aggiornaProfilo&dispositiva=areaPrivata");
				}
				rd.forward(request, response);
				
			}else if(azione.equals("modificaRisorsa")){
				
				RisorsaDTO risorsa = new RisorsaDTO();
				
				//parte anagrafica
				risorsa.setCognome(request.getParameter("cognome"));
				risorsa.setNome(request.getParameter("nome"));
				risorsa.setDataNascita(request.getParameter("dataNascita"));
				risorsa.setLuogoNascita(request.getParameter("luogoNascita"));
				risorsa.setSesso(request.getParameter("sesso"));
				risorsa.setCodiceFiscale(request.getParameter("codiceFiscale"));
				risorsa.setEmail(request.getParameter("mail"));
				risorsa.setTelefono(request.getParameter("telefono"));
				risorsa.setCellulare(request.getParameter("cellulare"));
				risorsa.setFax(request.getParameter("fax"));
				
				//parte residenza
				risorsa.setIndirizzo(request.getParameter("indirizzo"));
				risorsa.setCitta(request.getParameter("citta"));
				risorsa.setProvincia(request.getParameter("provincia"));
				risorsa.setCap(request.getParameter("cap"));
				risorsa.setNazione(request.getParameter("nazione"));
				risorsa.setServizioMilitare(request.getParameter("militare"));
				
				//parte altri dati
				risorsa.setPatente(request.getParameter("patente"));
				if(request.getParameter("occupato").equals("si")){
					risorsa.setOccupato(true);
				}else{
					risorsa.setOccupato(false);
				}
				risorsa.setFiguraProfessionale(request.getParameter("figuraProfessionale"));
				risorsa.setSeniority(request.getParameter("seniority"));
				
				risorsa.setIdRisorsa(((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa());
				
				String messaggio = rDAO.modificaRisorsa(risorsa);
				if(messaggio.equals("ok")){
					request.setAttribute("messaggio", "La modifica del profilo è avvenuta correttamente");
					rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio&dispositiva=areaPrivata");
					rd.forward(request, response);
				}else{
					request.setAttribute("messaggio", messaggio);
					rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio&dispositiva=areaPrivata");
					rd.forward(request, response);
				}
				
			}else if(azione.equals("modificaPassword")){
				
				String messaggio = rDAO.modificaPassword(((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa(),request.getParameter("nuovaPassword"));
				if(messaggio.equals("ok")){
					request.setAttribute("messaggio", "La modifica della password è avvenuta correttamente");
					rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio&dispositiva=areaPrivata");
					rd.forward(request, response);
				}else{
					request.setAttribute("messaggio", messaggio);
					rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio&dispositiva=areaPrivata");
					rd.forward(request, response);
				}
				
			}else if(azione.equals("logout")){
				
				Enumeration attrNames = sessione.getAttributeNames();
				while (attrNames.hasMoreElements())	{		  
					String valoriSessione = (String) attrNames.nextElement();// Stampa i nomi di tutti gli attributi di sessione
					if(valoriSessione.equals("modalitaDiConnessione")){
						continue;
					}else{
						sessione.removeAttribute(valoriSessione);
						log.debug(metodo, "logout " + valoriSessione);
					}
				}
				
				if(sessione.getAttribute("modalitaDiConnessione").equals("cvonline")){;
					response.sendRedirect("http://cvonline.tv");
				}else{
					response.sendRedirect("http://drconsulting.tv");
				}
			
			}
		}else{
			sessioneScaduta(response);
		}
	}
}
