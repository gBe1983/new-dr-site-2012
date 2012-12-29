package it.dipendente.servlet;

import it.dipendente.bo.Day;
import it.dipendente.bo.Month;
import it.dipendente.dao.PlanningDAO;
import it.dipendente.dto.RisorsaDTO;
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

		if(sessione.getAttribute("utenteLoggato") != null){
			int idRis=((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa();

			if(azione.equals("compilaTimeReport")||azione.equals("salvaTimeReport")){

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
						boolean saveOk=true;
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
									if(planningDAO.aggiornamentoPlanning(month.getWeeks().get(w).getCommesse().get(descrAttivita).get(p))==0){
										saveOk=false;
										log.warn(metodo, "Salvataggio time report anomalo");
									}
								}
							}
						}
						request.setAttribute("msg", saveOk?"Salvataggio riuscito.":"Attenzione, salvataggio fallito.");
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

				Month month =planningDAO.getGiornate(idRis,when);

				request.setAttribute("month", month);//4 display
				sessione.setAttribute("month", month);//4 save

				if(azione.equals("salvaTimeReport")){
					try {
						mail.sendMail(((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getEmail(), "Aggiornamento Time Report "+month.getMonthLabel(), month.getBodyMail());
					} catch (Exception e) {
						log.error(metodo, "invio mail time report", e);
					}
				}
				getServletContext().getRequestDispatcher("/main.jsp?azione=compilaTimeReport").forward(request, response);
			}
		}else{
			sessioneScaduta(response);
		}
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