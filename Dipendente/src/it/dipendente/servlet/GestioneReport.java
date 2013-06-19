package it.dipendente.servlet;

import it.dipendente.bo.Day;
import it.dipendente.bo.Month;
import it.dipendente.dao.PlanningDAO;
import it.dipendente.dto.PlanningDTO;
import it.dipendente.dto.RisorsaDTO;
import it.util.log.MyLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class GestioneReport extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private Logger log;

	public GestioneReport() {
		super();
		log = Logger.getLogger(GestioneReport.class);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("metodo: doGet");
		processRequest(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("metodo: doPost");
		processRequest(request,response);
	}

	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		log.info("metodo: processRequest");
		HttpSession sessione = request.getSession();
		String azione = request.getParameter("azione");

		if(sessione.getAttribute("utenteLoggato") != null){
			int idRis=((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa();

			if(azione.equals("compilaTimeReport")||azione.equals("salvaTimeReport") || 
					azione.equals("salvaBozza") || azione.equals("salvaGiornate") ||
					azione.equals("modificaOre")){
				
				log.info("-------------------------------------------------------------------------------------------------------");
				log.info("azione: "+azione);
				
				PlanningDAO planningDAO = new PlanningDAO(conn.getConnection());

				String mese=null;
				String anno=null;

				boolean elencoGiornate = false;
				
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
										log.warn("Salvataggio time report anomalo");
									}
								}
							}
						}
						request.setAttribute("msg", saveOk?"Salvataggio riuscito.":"Attenzione, salvataggio fallito.");
					}
				}else if(azione.equals("salvaBozza")){
					
					String nomeCommessa = request.getParameter("parametro");
					int id_planning = Integer.parseInt(request.getParameter("parametri"));
					
					PlanningDTO pDTO = new PlanningDTO();
					pDTO.setNumeroOre(Double.parseDouble(request.getParameter("oreOrd")));
					pDTO.setStraordinari(Double.parseDouble(request.getParameter("oreStrao")));
					pDTO.setAssenze(Double.parseDouble(request.getParameter("assenze")));
					if(request.getParameter("tipologiaAssenze") != null){
						String assenze = request.getParameter("tipologiaAssenze");
						if(assenze.equals("ferie")){
							pDTO.setFerie(true);
						}else if(assenze.equals("permessi")){
							pDTO.setPermessi(true);
						}else if(assenze.equals("mutua")){
							pDTO.setMutua(true);
						}else if(assenze.equals("permessiNonRetribuiti")){
							pDTO.setPermessiNonRetribuiti(true);
						}
					}
					pDTO.setNote(request.getParameter("note"));
					
					elencoGiornate = true;
					
					request.setAttribute("planning", id_planning);
					request.setAttribute("nomeCommessa", nomeCommessa);
					sessione.setAttribute("bozza", pDTO);
					
				}else if(azione.equals("salvaGiornate")){
					
					boolean saveOk=true;
					
					String parametri = request.getParameter("parametri");
					String [] idplanning = parametri.split(";");
					for(int x = 0; x < idplanning.length; x++){
						PlanningDTO pDTO = (PlanningDTO)sessione.getAttribute("bozza");
						if(!idplanning[x].equals("")){
							pDTO.setId_planning(Integer.parseInt(idplanning[x]));
							if(planningDAO.aggiornamentoPlanning(pDTO) == 0){
								saveOk=false;
								log.warn("Salvataggio time report anomalo");
								break;
							}
						}
					}
					request.setAttribute("msg", saveOk?"Salvataggio riuscito.":"Attenzione, salvataggio fallito.");
				
				}else if(azione.equals("modificaOre")){
					
					boolean saveOk=true;
					
					PlanningDTO pDTO = new PlanningDTO();
					pDTO.setId_planning(Integer.parseInt(request.getParameter("parametro")));
					pDTO.setNumeroOre(Double.parseDouble(request.getParameter("oreOrd")));
					pDTO.setStraordinari(Double.parseDouble(request.getParameter("oreStrao")));
					pDTO.setAssenze(Double.parseDouble(request.getParameter("assenze")));
					if(request.getParameter("tipologiaAssenze") != null){
						String assenze = request.getParameter("tipologiaAssenze");
						if(assenze.equals("ferie")){
							pDTO.setFerie(true);
						}else if(assenze.equals("permessi")){
							pDTO.setPermessi(true);
						}else if(assenze.equals("mutua")){
							pDTO.setMutua(true);
						}else if(assenze.equals("permessiNonRetribuiti")){
							pDTO.setPermessiNonRetribuiti(true);
						}
					}
					pDTO.setNote(request.getParameter("note"));
					
					if(planningDAO.aggiornamentoPlanning(pDTO) == 0){
						saveOk=false;
						log.warn("Salvataggio time report anomalo");
					}
					request.setAttribute("msg", saveOk?"Modifica avvenuta con successo.":"Attenzione, modifica non avvenuta con successo.");
				}
				
				if((request.getParameter("mese") != null && !request.getParameter("mese").equals("")) && (request.getParameter("anno") != null && !request.getParameter("anno").equals(""))){
					sessione.setAttribute("mese", request.getParameter("mese"));
					sessione.setAttribute("anno", request.getParameter("anno"));
				}

				Calendar when = Calendar.getInstance();
				if(sessione.getAttribute("mese") != null && sessione.getAttribute("anno") != null){
					timeAdjust(when,Calendar.MONTH,Integer.parseInt(sessione.getAttribute("mese").toString()));
					timeAdjust(when,Calendar.YEAR,Integer.parseInt(sessione.getAttribute("anno").toString()));
				}

				ArrayList<ArrayList> caricamentoGiornate =planningDAO.getGiornate(idRis,when,getServletContext().getInitParameter("parametro"));
				ArrayList<PlanningDTO> giornate = caricamentoGiornate.get(1);
				
				HashMap<String, Boolean> elencoFlagAssenze = planningDAO.caricamentoFlagAssenze(((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa());
				
				Month mex = new Month(when);
				
				int settimaneTotali = 0;
				for(int x = 0; x < giornate.size(); x++){
					if(giornate.get(x).getNumeroSettimana() != settimaneTotali){
						settimaneTotali++;
					}
				}
				
				HashMap<Integer,String> elencoGiorni = planningDAO.elencoGiorniDellaSettimana();
				
				request.setAttribute("caricamentoGiornate", caricamentoGiornate);//4 display
				request.setAttribute("elencoGiorni", elencoGiorni);
				request.setAttribute("settimaneTotali", settimaneTotali);
				request.setAttribute("mese", mex);
				request.setAttribute("flagVisualizzaElencoGiornate", elencoGiornate);
				request.setAttribute("flagAssenze", elencoFlagAssenze);
				
				//sessione.setAttribute("month", month);//4 save

				/*if(azione.equals("salvaTimeReport")){
					try {
						mail.sendMail(	((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getEmail(),
										"Aggiornamento Time Report"+month.getMonthLabel(),
										month.getBodyMail());
					} catch (Exception e) {
						log.error(metodo, "invio mail time report", e);
					}
				}*/
				log.info("url: /main.jsp?azione=compilaTimeReport");
				
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