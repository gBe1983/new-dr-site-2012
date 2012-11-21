package it.dipendente.servlet;

import it.dipendente.bo.Day;
import it.dipendente.bo.Month;
import it.dipendente.dao.Associaz_Risors_Comm_DAO;
import it.dipendente.dao.PlanningDAO;
import it.dipendente.dao.RisorseDAO;
import it.util.log.MyLogger;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

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
		String azione = request.getParameter("azione");

		//if(sessione.getAttribute("utenteLoggato") != null){//TODO RIPRISTINARE!
			int idRis=57;//TODO RIPRISTINARE!((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa();
			if(azione.equals("compilaTimeReport")||azione.equals("salvaTimeReport")){

				//TODO START DA RIMUOVERE...
				request.setAttribute("risorse", new RisorseDAO(conn.getConnection()).getRisorse());
				//TODO END DA RIMUOVERE...

				PlanningDAO planningDAO = new PlanningDAO(conn.getConnection());

				String mese=null;
				String anno=null;

				if(azione.equals("salvaTimeReport")){
					Month month=(Month)sessione.getAttribute("month");
					if(month!=null){
						anno=month.getYear()+"";
						mese=month.getMonth()+"";
						String ord;
						String str;
						String keyDay;
						for(int w=0;w<month.getWeeks().size();w++){
							for(String descrAttivita:month.getWeeks().get(w).getCommesse().keySet()){
								for(int p=0;p<month.getWeeks().get(w).getCommesse().get(descrAttivita).size();p++){
									keyDay=Day.sdf.format(month.getWeeks().get(w).getCommesse().get(descrAttivita).get(p).getData().getTime());
									ord = request.getParameter(descrAttivita+"_ord"+keyDay);
									if(!StringUtils.isEmpty(ord)){
										month.getWeeks().get(w).getCommesse().get(descrAttivita).get(p).setNumeroOre(Double.parseDouble(ord.replace(",", ".")));
									}
									str = request.getParameter(descrAttivita+"_str"+keyDay);
									if(!StringUtils.isEmpty(str)){
										month.getWeeks().get(w).getCommesse().get(descrAttivita).get(p).setStraordinari(Double.parseDouble(str.replace(",", ".")));
									}
									planningDAO.aggiornamentoPlanning(month.getWeeks().get(w).getCommesse().get(descrAttivita).get(p));
								}
							}
						}
					}
				}else{
					mese = request.getParameter("mese");
					anno = request.getParameter("anno");
				}

				Calendar when = Calendar.getInstance();
				if(mese!=null&&anno!=null){
					timeAdjust(when,Calendar.MONTH,Integer.parseInt(mese));
					timeAdjust(when,Calendar.YEAR,Integer.parseInt(anno));
				}

				//TODO START DA RIMUOVERE...
				String risorsa = request.getParameter("risorsa");
				if(risorsa!=null){
					idRis = Integer.parseInt(risorsa);
				}
				request.setAttribute("idRis", ""+idRis);
				//TODO END DA RIMUOVERE...

				Month month =planningDAO.getGiornate(idRis,when);

				request.setAttribute("month", month);//4 display
				sessione.setAttribute("month", month);//4 save

				getServletContext().getRequestDispatcher("/index.jsp?azione=compilaTimeReport").forward(request, response);
			}else if(azione.equals("caricamentoCommesse")){//TODO ... CAPIRE DOVE UTILIZZATO E SE SERVE ANCORA
				Associaz_Risors_Comm_DAO rDAO = new Associaz_Risors_Comm_DAO(conn.getConnection());
				//qua mi carico le commesse attive legate alla risorsa
				request.setAttribute("listaCommesseAttive",
											rDAO.caricamentoCommesseAttive(idRis));
				// qua mi carico le commesse non attive legate alla risorsa
				request.setAttribute("listaCommesseNonAttive",
											rDAO.caricamentoCommesseNonAttive(idRis));
				//qua mi carico le commesse di tipologia "Altro" per veficare quali sono legate alla risorsa
				request.setAttribute("commesseRisorse",
											rDAO.caricamentoCommesseAssociateRisorse(idRis));

				getServletContext()
					.getRequestDispatcher("/index.jsp?azione=TimeReport&dispositiva=TimeReport")
						.forward(request,response);

			}
		//}else{//TODO RIPRISTINARE
		//	sessioneScaduta(response);
		//}
	}

/**
 * 
 * @param c
 * @param field
 * @param fieldValue
 */
	private void timeAdjust(Calendar c, int field, int fieldValue) {
		while(c.get(field)!=fieldValue){
			c.add(field,(c.get(field)<fieldValue)?1:-1);
		}
	}
}