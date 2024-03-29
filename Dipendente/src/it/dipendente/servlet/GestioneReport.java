package it.dipendente.servlet;

import it.dipendente.bo.Day;
import it.dipendente.bo.Month;
import it.dipendente.dao.Associaz_Risors_Comm_DAO;
import it.dipendente.dao.PlanningDAO;
import it.dipendente.dto.Associaz_Risors_Comm_DTO;
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
				
				if((request.getParameter("mese") != null && !request.getParameter("mese").equals("")) && (request.getParameter("anno") != null && !request.getParameter("anno").equals(""))){
					sessione.setAttribute("mese", request.getParameter("mese"));
					sessione.setAttribute("anno", request.getParameter("anno"));
				}

				Calendar when = Calendar.getInstance();
				if(sessione.getAttribute("mese") != null && sessione.getAttribute("anno") != null){
					timeAdjust(when,Calendar.MONTH,Integer.parseInt(sessione.getAttribute("mese").toString()));
					timeAdjust(when,Calendar.YEAR,Integer.parseInt(sessione.getAttribute("anno").toString()));
				}
				
				int maxGiorniMensili = when.getActualMaximum(Calendar.DAY_OF_MONTH);
				int minGiorniMensili = when.getActualMinimum(Calendar.DAY_OF_MONTH);
				
				
				/*
				 * carico le due date con il primo giorno del mese e con l'ultimo giorno
				 */
				Calendar dataInizio = Calendar.getInstance();
				dataInizio.setTime(when.getTime());
				
				dataInizio.set(Calendar.DAY_OF_MONTH, minGiorniMensili);
				
				Calendar dataFine = Calendar.getInstance();
				dataFine.setTime(when.getTime());
				
				dataFine.set(Calendar.DAY_OF_MONTH, maxGiorniMensili);
				
				
				Associaz_Risors_Comm_DAO asscomm = new Associaz_Risors_Comm_DAO(conn.getConnection());
				
				ArrayList<Associaz_Risors_Comm_DTO> associazioneRisorsaCommesse = asscomm.caricamentoCommesseAssociateRisorse(idRis, dataInizio.getTime(), dataFine.getTime());
				
				if(associazioneRisorsaCommesse.size() > 1 && request.getParameter("parametroCommessa") == ""){
					
					request.setAttribute("listaCommesse", associazioneRisorsaCommesse);
					request.setAttribute("parametroCommessa", request.getParameter("parametroCommessa"));
					getServletContext().getRequestDispatcher("/main.jsp?azione=compilaTimeReport").forward(request, response);
					
				}else{

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
					
					/*
					 * verifico il caricamento della commessa avviene tramite la scelta
					 * oppure direttamente dalla ricerca delle associazioni
					 */
					ArrayList<ArrayList> caricamentoGiornate = new ArrayList<ArrayList>();
					int settimaneTotali = 0;
					HashMap<String, Boolean> elencoFlagAssenze = new HashMap<String, Boolean>();
					Month mex = new Month(when);
					/*if(associazioneRisorsaCommesse.size() > 1){*/
						if(associazioneRisorsaCommesse.size() == 1){
							caricamentoGiornate = planningDAO.getGiornate(idRis,dataInizio,dataFine,getServletContext().getInitParameter("parametro"),String.valueOf(associazioneRisorsaCommesse.get(0).getId_commessa()));
						}else{
							if(request.getParameter("parametroCommessa").equals("tutte")){
								caricamentoGiornate = planningDAO.getGiornate(idRis,dataInizio,dataFine,getServletContext().getInitParameter("parametro"),request.getParameter("parametroCommessa"));
							}else{
								caricamentoGiornate = planningDAO.getGiornate(idRis,dataInizio,dataFine,getServletContext().getInitParameter("parametro"),request.getParameter("parametroCommessa"));
							}
						}
					
					
						ArrayList<PlanningDTO> giornate = caricamentoGiornate.get(1);
						
						elencoFlagAssenze = planningDAO.caricamentoFlagAssenze(((RisorsaDTO)sessione.getAttribute("utenteLoggato")).getIdRisorsa());
						
						/*
						 * mi calcolo il numero delle settimane che ci sono in un mese
						 */
						if(giornate.size() > 0){
							settimaneTotali = giornate.get(0).getNumeroSettimana();
							int contatore = giornate.get(0).getNumeroSettimana();
							for(int x = 0; x < giornate.size(); x++){
								if(giornate.get(x).getNumeroSettimana() == contatore){
									 continue;
								}else{
									contatore = giornate.get(x).getNumeroSettimana();
									settimaneTotali++;
								}
							}
						}
						log.info("numeroSettimane " + settimaneTotali);
					/*}*/
					
					/*
					 * azzero il giorno della dataInizio
					 */
					dataInizio.set(Calendar.DAY_OF_MONTH, minGiorniMensili);
					
					HashMap<Integer,String> elencoGiorni = planningDAO.elencoGiorniDellaSettimana();
					
					request.setAttribute("dataInizio", dataInizio);
					request.setAttribute("maxGiorniMensili", maxGiorniMensili);
					request.setAttribute("caricamentoGiornate", caricamentoGiornate);//4 display
					request.setAttribute("elencoGiorni", elencoGiorni);
					request.setAttribute("settimaneTotali", settimaneTotali);
					request.setAttribute("mese", mex);
					request.setAttribute("flagVisualizzaElencoGiornate", elencoGiornate);
					request.setAttribute("flagAssenze", elencoFlagAssenze);
					request.setAttribute("listaCommesse", associazioneRisorsaCommesse);
					if(associazioneRisorsaCommesse.size() == 1){
						request.setAttribute("parametroCommessa", associazioneRisorsaCommesse.get(0).getId_commessa());
					}else{
						request.setAttribute("parametroCommessa", request.getParameter("parametroCommessa"));
					}
				
					
					
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