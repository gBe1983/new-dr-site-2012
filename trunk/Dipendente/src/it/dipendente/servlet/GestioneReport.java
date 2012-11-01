package it.dipendente.servlet;

import it.dipendente.dao.Associaz_Risors_Comm_DAO;
import it.dipendente.dao.PlanningDAO;
import it.dipendente.dto.PlanningDTO;
import it.dipendente.dto.RisorsaDTO;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Report
 */
public class GestioneReport extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GestioneReport() {
		super();
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
		
		Connection conn = (Connection) sessione.getAttribute("connessione");

		Associaz_Risors_Comm_DAO rDAO = new Associaz_Risors_Comm_DAO(conn);
		
		RequestDispatcher rd = null;
		
		PlanningDAO planningDAO = new PlanningDAO(conn);
		
		String azione = request.getParameter("azione");
		
		if(sessione.getAttribute("utenteLoggato") != null){
			
			if(azione.equals("visualizzaMese")){
				
				int mese = Integer.parseInt(request.getParameter("mese"));
				int anno = Integer.parseInt(request.getParameter("anno"));
				int id_associazione = Integer.parseInt(request.getParameter("parametro"));
				
				ArrayList listaGiornate = new ArrayList();
				if(mese < 10){
					listaGiornate = planningDAO.caricamentoGiornate("0"+mese, anno, id_associazione);
				}else{
					listaGiornate = planningDAO.caricamentoGiornate(String.valueOf(mese), anno, id_associazione);
				}
				
				/*
				 * nella variabile meseScelto effettuo il caricamento del mese corrente
				 * per la risorsa
				 */
				//ArrayList meseScelto = rDAO.caricamentoCalendario(mese, anno);
				
				/*
				 * nella variabile commesseRisorse troviamo tutte le commesse associate alla risorsa
				 */
				ArrayList commesseRisorse = rDAO.caricamentoCommesseAssociateRisorse(((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa());
				
				System.out.println(mese + " " + anno);
				
				//request.setAttribute("listaGiorni", meseScelto);
				ArrayList listaCommesseAttive = rDAO.caricamentoCommesseAttive(((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa());
				ArrayList listaCommesseNonAttive = rDAO.caricamentoCommesseNonAttive(((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa());
				
				
				request.setAttribute("listaCommesseAttive", listaCommesseAttive);
				request.setAttribute("listaCommesseNonAttive", listaCommesseNonAttive);
				request.setAttribute("commesseRisorse", commesseRisorse);
				request.setAttribute("listaGiorni", listaGiornate);
				
				
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=visualizzaReport&mese="+mese+"&anno="+anno+"&parametro="+id_associazione+"&dispositiva=TimeReport");
				rd.forward(request, response);
				
			}else if(azione.equals("inserisciMese")){
				
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
							planning.setData(anno+"-0"+mese+"-0"+(x+1));
							if(request.getParameter("numeroOre_"+x).indexOf(",") != -1){
								planning.setNumeroOre(Double.parseDouble(request.getParameter("numeroOre_"+x).replace(",", ".")));
							}else{
								planning.setNumeroOre(Double.parseDouble(request.getParameter("numeroOre_"+x)));
							}
							
							planning.setOrario(request.getParameter("orario_"+x));
							planning.setDescr_attivita(request.getParameter("descrizione_"+x));
							planning.setNote(request.getParameter("note_"+x));
						}else{
							planning.setData(anno+"-0"+mese+"-"+(x+1));
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
							planning.setData(anno+"-"+mese+"-0"+(x+1));
							if(request.getParameter("numeroOre_"+x).indexOf(",") != -1){
								planning.setNumeroOre(Double.parseDouble(request.getParameter("numeroOre_"+x).replace(",", ".")));
							}else{
								planning.setNumeroOre(Double.parseDouble(request.getParameter("numeroOre_"+x)));
							}
							planning.setOrario(request.getParameter("orario_"+x));
							planning.setDescr_attivita(request.getParameter("descrizione_"+x));
							planning.setNote(request.getParameter("note_"+x));
						}else{
							planning.setData(anno+"-0"+mese+"-"+(x+1));
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
					rDAO.aggiornamentoGiorniMensili(planning);
				}
				
				request.setAttribute("messaggio", "L'aggiornamento delle ore è avvenuto correttamente.");
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio&dispositiva=areaPrivata");
				rd.forward(request, response);
				
			}else if(azione.equals("caricamentoCommesse")){
				
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
