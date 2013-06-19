package it.dipendente.servlet;

import it.dipendente.dao.EventoDAO;
import it.dipendente.dto.EventoDTO;
import it.dipendente.dto.RisorsaDTO;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

/**
 * Servlet implementation class GestioneCalendarioEventi
 */
public class GestioneCalendarioEventi extends BaseServlet {
	private static final long serialVersionUID = 1L;
	
	Logger log = Logger.getLogger(GestioneCalendarioEventi.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GestioneCalendarioEventi() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request,response);
	}
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//recupero l'oggetto sessione
		HttpSession sessione = request.getSession();
		
		//creo l'istanza della classe 
		EventoDAO eDAO = new EventoDAO(conn.getConnection());
		
		RequestDispatcher rd = null;
		
		String azione = request.getParameter("azione");
		
		if(azione.equals("inserimentoEvento") || azione.equals("aggiornaEvento")){
			
			log.info("-------------------------------------------------------------------------------------------------------");
			log.info("azione: " + azione);
		
			SimpleDateFormat formatoSql = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat formatoWeb = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			
			EventoDTO evento = new EventoDTO();
			evento.setTitle(request.getParameter("titolo"));
			try {
				evento.setStart(formatoSql.format(formatoWeb.parse(request.getParameter("dataInizio") + " " + request.getParameter("oraInizio") +":"+ request.getParameter("minutiInizio") + ":00")));
				evento.setEnd(formatoSql.format(formatoWeb.parse(request.getParameter("dataFine") + " " + request.getParameter("oraFine") +":"+ request.getParameter("minutiFine") + ":00")));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			if(azione.equals("inserimentoEvento")){
				
				eDAO.inserisciEvento(evento,((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa());
				
				log.info("url: ./index.jsp?azione=consulenzaOnline");
				response.sendRedirect("./index.jsp?azione=consulenzaOnline");
				
			}else{
				
				evento.setId(Integer.parseInt(request.getParameter("evento")));
				eDAO.modificaEvento(evento,((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa());
				
				EventoDTO caricamentoEvento = eDAO.caricamentoEvento(Integer.parseInt(request.getParameter("evento")));
				
				request.setAttribute("evento", caricamentoEvento);
				
				log.info("url: /index.jsp?azione=visualizzaEvento");
				
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=visualizzaEvento");
				rd.forward(request, response);
			}
			
		}else if(azione.equals("caricamentoEventi")){
			
			log.info("-------------------------------------------------------------------------------------------------------");
			log.info("azione: " + azione);
			
			response.setContentType("application/json");       
			Gson gson = new Gson();
			
			ArrayList eventi = eDAO.caricamentoEventi(((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa());
			
			response.getOutputStream().print(gson.toJson(eventi));
			response.getOutputStream().flush();
			
			
		}else if(azione.equals("visualizzaEvento") || azione.equals("modificaEvento")){
			
			log.info("-------------------------------------------------------------------------------------------------------");
			log.info("azione: " + azione);
			
			int idEvento = Integer.parseInt(request.getParameter("evento"));
			
			EventoDTO evento = eDAO.caricamentoEvento(idEvento);
			
			request.setAttribute("evento", evento);
			
			if(azione.equals("modificaEvento")){
				
				log.info("/index.jsp?azione=modificaEvento");
				
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=modificaEvento");
				rd.forward(request, response);
			}else{
				
				log.info("/index.jsp?azione=visualizzaEvento");
				
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=visualizzaEvento");
				rd.forward(request, response);
			}
			
		}else if(azione.equals("eliminaEvento")){
			
			log.info("-------------------------------------------------------------------------------------------------------");
			log.info("azione: " + azione);
			
			int idEvento = Integer.parseInt(request.getParameter("evento"));
			
			eDAO.eliminaEvento(idEvento);
			response.sendRedirect("./index.jsp?azione=consulenzaOnline");
		}
	}

}
