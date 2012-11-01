package it.dipendente.servlet;

import it.dipendente.connessione.Connessione;
import it.dipendente.dao.RisorsaDAO;
import it.dipendente.dto.RisorsaDTO;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ServletUtente
 */
public class GestioneRisorsa extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public GestioneRisorsa() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//recupero l'oggetto sessione
		HttpSession sessione = request.getSession();
		
		//creo l'istanza della classe Connessione
		Connessione connessione = new Connessione();
		
		Connection conn = null;
		//recupero l'oggetto Connection dal metodo connessione presente nella classe Connessione
		if(request.getParameter("connessione") != null){
			conn = connessione.connessione(request.getParameter("connessione"));
			sessione.setAttribute("modalitaDiConnessione", request.getParameter("connessione"));
		}else{
			conn = (Connection) sessione.getAttribute("connessione");
		}
		
		sessione.setAttribute("connessione", conn);
		
		//creo l'istanza della classe 
		RisorsaDAO rDAO = new RisorsaDAO(conn);
		
		RequestDispatcher rd = null;
		
		
		//recupero il parametro azione
		String azione = request.getParameter("azione");
		
		if(azione.equals("login")){
			
			int idRisorsa = Integer.parseInt(request.getParameter("parametro"));
			
			System.out.println(idRisorsa);
			
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
						System.out.println(valoriSessione);
					}
				}
				
				if(sessione.getAttribute("modalitaDiConnessione").equals("cvonline")){;
					response.sendRedirect("http://cvonline.tv");
				}else{
					response.sendRedirect("http://drconsulting.tv");
				}
			
			}
		}else{
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			try {
				out = response.getWriter();
				out.print("<html>" +
						"<head>" +
						"</head>" +
						"<body>" +
						"<script type=\"text/javascript\">" +
						"alert(\"La sessione è scaduta. Rieffettuare la login\");" +
						"url = window.location.href;" +
						"var variabiliUrl = url.split(\"/\");" +
						"for(a=0; a < variabiliUrl.length; a++){" +
						"		if(a == 2){" +
						"			var localVariabili = variabiliUrl[a].split(\":\");" +
						"			for(x=0; x < localVariabili.length; x++){" +
						"				if(localVariabili[x] == \"localhost\"){" +
						"					window.location = \"http://localhost/dr\";" +
						"				}if(localVariabili[x] == \"cvonline\"){" +
						"					window.location.href = \"http://cvonline.tv\";" +
						"				}if(localVariabili[x] == \"drconsulting\"){" +
						"					window.location.href= \"http://drconsulting.tv\";" +
						"				}" +
						"			}" +
						"		}else{" +
						"			continue;" +
						"		}" +
						"}" +
						"</script>" +
						"</body>" +
						"</html>");
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
