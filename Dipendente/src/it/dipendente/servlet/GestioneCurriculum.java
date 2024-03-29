package it.dipendente.servlet;

import it.dipendente.dao.CurriculumDAO;
import it.dipendente.dto.CurriculumDTO;
import it.dipendente.dto.Dettaglio_Cv_DTO;
import it.dipendente.dto.EsperienzeDTO;
import it.dipendente.dto.RisorsaDTO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class GestioneCurriculum
 */
public class GestioneCurriculum extends BaseServlet {
	private static final long serialVersionUID = 1L;

	Logger log = null;
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		log = Logger.getLogger(GestioneCurriculum.class);
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
		
		//recupero la sessione
		HttpSession sessione = request.getSession();

		CurriculumDAO cDAO = new CurriculumDAO(conn.getConnection());
		
		if(sessione.getAttribute("utenteLoggato") != null){
			
			//recupero il parametro azione
			String azione = request.getParameter("azione");
			
			if(azione.equals("caricamentoCv")){
				
				log.info("-------------------------------------------------------------------------------------------------------");
				log.info("azione: "+ azione);
				
				
				/**
				 * in questa sezione carico il curriculum selezionato 
				 * della singola risorsa
				 */
				int id_risorsa = Integer.parseInt(request.getParameter("parametro"));
				
				if(request.getParameter("creazioneCv") != null){
					if(!Boolean.parseBoolean(request.getParameter("creazioneCv"))){
						cDAO.creazioneFlagCreazioneCurriculum(id_risorsa);
					}
				}
				
				CurriculumDTO curriculumVitae = cDAO.caricamentoCurriculum(id_risorsa);
				curriculumVitae.setId_risorsa(id_risorsa);
				
				request.setAttribute("curriculumVitae", curriculumVitae);
				
				if(request.getParameter("area").equals("all")){
					log.info("url = /index.jsp?azione=curriculum");
					getServletContext().getRequestDispatcher("/index.jsp?azione=curriculum").forward(request, response);
				}else{
					log.info("url = /index.jsp?azione=dettaglioCurriculum");
					getServletContext().getRequestDispatcher("/index.jsp?azione=dettaglioCurriculum").forward(request, response);
				}
				
				
			}else if(azione.equals("modificaIntestazione") || azione.equals("modificaEsperienza") || azione.equals("modificaDettaglio")){
				
				log.info("-------------------------------------------------------------------------------------------------------");
				log.info("azione: "+ azione);
				
				/**
				 * in questa sezione gestisco le modifiche delle varie sezioni
				 */
		
				if(azione.equals("modificaIntestazione")){
					
					int id_risorsa = Integer.parseInt(request.getParameter("parametro"));
					//mi carico l'intestazione della singola risorsa
					RisorsaDTO risorsa = cDAO.caricamentoIntestazioneRisorsa(id_risorsa);
					
					request.setAttribute("intestazioneRisorsa", risorsa);
					
					getServletContext().getRequestDispatcher("/index.jsp?azione=gestioneSingoleSezioniCurriculum&sezione=intestazione&dispositiva=dettaglioCurriculum").forward(request, response);
					
				}else if(azione.equals("modificaEsperienza")){
					
					int id_esperienza = Integer.parseInt(request.getParameter("parametro0"));
					
					EsperienzeDTO exp = cDAO.caricamentoEsperienza(id_esperienza);
					
					request.setAttribute("esperienza", exp);
					
					getServletContext().getRequestDispatcher("/index.jsp?azione=gestioneSingoleSezioniCurriculum&sezione=esperienza&dispositiva=dettaglioCurriculum").forward(request, response);

				}else if(azione.equals("modificaDettaglio")){
					
					int id_risorsa = Integer.parseInt(request.getParameter("parametro"));
					
					Dettaglio_Cv_DTO dettaglio = cDAO.caricamentoDettaglio(id_risorsa);
					
					request.setAttribute("dettaglio", dettaglio);
					
					getServletContext().getRequestDispatcher("/index.jsp?azione=gestioneSingoleSezioniCurriculum&sezione=dettaglio&dispositiva=dettaglioCurriculum").forward(request, response);

				}
				
				
			}else if(azione.equals("salvaIntestazione") || azione.equals("salvaEsperienza") || azione.equals("salvaDettaglio")){
				
				log.info("-------------------------------------------------------------------------------------------------------");
				log.info("azione: "+ azione);
				
				/**
				 * in questa sezione salvo tutte le modifiche avvenute alle varie sezioni
				 */
				int id_risorsa = Integer.parseInt(request.getParameter("parametro"));
				
				int esitoModifica = 0;
				
				if(azione.equals("salvaIntestazione")){
					
					//recupero i valori
					RisorsaDTO intestazioneRisorsa = new RisorsaDTO();
					
					intestazioneRisorsa.setIdRisorsa(id_risorsa);
					intestazioneRisorsa.setCognome(request.getParameter("cognome"));
					intestazioneRisorsa.setNome(request.getParameter("nome"));
					intestazioneRisorsa.setIndirizzo(request.getParameter("indirizzo"));
					intestazioneRisorsa.setTelefono(request.getParameter("telefono"));
					intestazioneRisorsa.setCellulare(request.getParameter("cellulare"));
					intestazioneRisorsa.setFax(request.getParameter("fax"));
					intestazioneRisorsa.setEmail(request.getParameter("email"));
					intestazioneRisorsa.setDataNascita(request.getParameter("dataNascita"));
					
					esitoModifica = cDAO.updateIntestazione(intestazioneRisorsa);
			
				}else if(azione.equals("salvaEsperienza")){
					
					EsperienzeDTO esperienza = new EsperienzeDTO();
					
					esperienza.setIdEsperienze(Integer.parseInt(request.getParameter("parametroId")));
					esperienza.setPeriodo(request.getParameter("annoInizio") + request.getParameter("meseInizio") 
						+ "_" +  request.getParameter("annoFine") + request.getParameter("meseFine"));
					esperienza.setAzienda(request.getParameter("azienda"));
					esperienza.setLuogo(request.getParameter("luogo"));
					esperienza.setDescrizione(request.getParameter("descrizione"));
					esperienza.setId_risorsa(id_risorsa);
					
					esitoModifica = cDAO.aggiornamentoEsperienza(esperienza);
					
				}else if(azione.equals("salvaDettaglio")){
					
					//recupero i valori della modifica del dettaglio
					Dettaglio_Cv_DTO dettaglio = new Dettaglio_Cv_DTO();
					dettaglio.setCapacita_professionali(request.getParameter("capacitaProfessionali"));
					dettaglio.setCompetenze_tecniche(request.getParameter("competenzeTecniche"));
					dettaglio.setLingue_Straniere(request.getParameter("lingue"));
					dettaglio.setIstruzione(request.getParameter("istruzione"));
					dettaglio.setFormazione(request.getParameter("formazione"));
					dettaglio.setInteressi(request.getParameter("interessi"));
					dettaglio.setId_risorsa(id_risorsa);
					dettaglio.setId_dettaglio(Integer.parseInt(request.getParameter("parametroId")));
					
					esitoModifica = cDAO.aggiornamentoDettaglio(dettaglio);
				}
				
				CurriculumDTO curriculumVitae = cDAO.caricamentoCurriculum(id_risorsa);
				curriculumVitae.setId_risorsa(id_risorsa);
				
				request.setAttribute("curriculumVitae", curriculumVitae);
				
				if(esitoModifica == 1){
					
					log.info("url: /index.jsp?azione=dettaglioCurriculum&esito=1");
					
					getServletContext().getRequestDispatcher("/index.jsp?azione=dettaglioCurriculum&esito=1").forward(request, response);
				}else{
					
					log.info("url: /index.jsp?azione=dettaglioCurriculum&esito=0");
					
					getServletContext().getRequestDispatcher("/index.jsp?azione=dettaglioCurriculum&esito=0").forward(request, response);
				}
			
			}else if(azione.equals("anteprimaIntestazione") || azione.equals("anteprimaEsperienza") || azione.equals("anteprimaDettaglio") || azione.equals("anteprimaGlobale")){
				
				log.info("-------------------------------------------------------------------------------------------------------");
				log.info("azione: "+ azione);
				
				/**
				 * in questa sezione effettuo l'anteprima delle singole sezioni
				 */
				
				int id_risorsa = 0;

				if(request.getParameter("parametro") != null){
					id_risorsa = Integer.parseInt(request.getParameter("parametro"));
				}else{
					id_risorsa = Integer.parseInt(request.getParameter("parametro0"));
				}
				
				if(azione.equals("anteprimaIntestazione")){
					
					//mi carico l'intestazione per poi effettuare l'anteprima
					RisorsaDTO risorsa = cDAO.caricamentoIntestazioneRisorsa(id_risorsa);
					
					request.setAttribute("anteprimaIntestazione", risorsa);
					
					log.info("url: /index.jsp?azione=gestioneAnteprimeSezioniCurriculum&sezione=intestazione&dispositiva=dettaglioCurriculum");
					
					getServletContext().getRequestDispatcher("/index.jsp?azione=gestioneAnteprimeSezioniCurriculum&sezione=intestazione&dispositiva=dettaglioCurriculum").forward(request, response);
				
				}else if(azione.equals("anteprimaEsperienza")){
					
					int indice = Integer.parseInt(request.getParameter("indice"));
					
					ArrayList<EsperienzeDTO> listaEsperienze = new ArrayList<EsperienzeDTO>();
					
					CurriculumDTO curriculum = new CurriculumDTO();
					
					for(int x = 0; x < indice; x++){
						
						int id_esperienza = Integer.parseInt(request.getParameter("parametro"+x));
						
						EsperienzeDTO esperienza = cDAO.caricamentoEsperienza(id_esperienza);
						//effettuo questa operazione per formattare la data in MM/YYYY
						esperienza.setPeriodo(cDAO.formattazionePeriodo(esperienza.getPeriodo().split("_")));
						
						listaEsperienze.add(esperienza);
					}
					
					curriculum.setListaEsperienze(listaEsperienze);
					curriculum.setId_risorsa(id_risorsa);
					
					request.setAttribute("listaEsperienze", curriculum);
					
					log.info("url: /index.jsp?azione=gestioneAnteprimeSezioniCurriculum&sezione=esperienze&dispositiva=dettaglioCurriculum");
					
					getServletContext().getRequestDispatcher("/index.jsp?azione=gestioneAnteprimeSezioniCurriculum&sezione=esperienze&dispositiva=dettaglioCurriculum").forward(request, response);
				
				}else if(azione.equals("anteprimaDettaglio")){
										
					Dettaglio_Cv_DTO dettaglio = cDAO.caricamentoDettaglio(id_risorsa);
					
					request.setAttribute("dettaglio", dettaglio);
				
					log.info("url: /index.jsp?azione=gestioneAnteprimeSezioniCurriculum&sezione=dettaglio&dispositiva=dettaglioCurriculum");
					
					getServletContext().getRequestDispatcher("/index.jsp?azione=gestioneAnteprimeSezioniCurriculum&sezione=dettaglio&dispositiva=dettaglioCurriculum").forward(request, response);
					
				}else if(azione.equals("anteprimaGlobale")){
					
					CurriculumDTO curriculumVitae = cDAO.caricamentoCurriculum(id_risorsa);
					curriculumVitae.setId_risorsa(id_risorsa);
					
					String sceltaTipologia = request.getParameter("sceltaCurriculumAnteprima");
					
					request.setAttribute("curriculumVitae", curriculumVitae);
					
					log.info("url: /index.jsp?azione=anteprimaCurriculum&tipologia="+sceltaTipologia+"&area=all");
					
					getServletContext().getRequestDispatcher("/index.jsp?azione=anteprimaCurriculum&tipologia="+sceltaTipologia+"&area=all").forward(request, response);
					
				}
				
			}else if (azione.equals("aggiungiEsperienza") || azione.equals("aggiungiDettaglio")) {
				
				log.info("-------------------------------------------------------------------------------------------------------");
				log.info("azione: "+ azione);
				
				int id_risorsa = Integer.parseInt(request.getParameter("parametroId"));
				
				int esitoAggiungi = 0;
				
				if(azione.equals("aggiungiEsperienza")){
					EsperienzeDTO esperienza = new EsperienzeDTO();
					esperienza.setPeriodo(request.getParameter("annoInizio") + request.getParameter("meseInizio") 
							+ "_" +  request.getParameter("annoFine") + request.getParameter("meseFine"));
					esperienza.setAzienda(request.getParameter("azienda"));
					esperienza.setLuogo(request.getParameter("luogo"));
					esperienza.setDescrizione(request.getParameter("descrizione"));
					esperienza.setId_risorsa(id_risorsa);
					
					esitoAggiungi = cDAO.inserimentoEsperienze(esperienza);
				}else{
					
					Dettaglio_Cv_DTO dettaglio = new Dettaglio_Cv_DTO();
					dettaglio.setCapacita_professionali(request.getParameter("capacitaProfessionali"));
					dettaglio.setCompetenze_tecniche(request.getParameter("competenzeTecniche"));
					dettaglio.setLingue_Straniere(request.getParameter("lingue"));
					dettaglio.setIstruzione(request.getParameter("istruzione"));
					dettaglio.setFormazione(request.getParameter("formazione"));
					dettaglio.setInteressi(request.getParameter("interessi"));
					dettaglio.setId_risorsa(id_risorsa);
					
					esitoAggiungi = cDAO.inserimentoDettaglio(dettaglio);
					
				}
				
				CurriculumDTO curriculumVitae = cDAO.caricamentoCurriculum(id_risorsa);
				curriculumVitae.setId_risorsa(id_risorsa);
				
				request.setAttribute("curriculumVitae", curriculumVitae);
				
				if(esitoAggiungi == 1){
					
					log.info("url: /index.jsp?azione=dettaglioCurriculum&esito=1");
					
					getServletContext().getRequestDispatcher("/index.jsp?azione=dettaglioCurriculum&esito=1").forward(request, response);
				}else{
					log.info("url: /index.jsp?azione=dettaglioCurriculum&esito=0");
					
					getServletContext().getRequestDispatcher("/index.jsp?azione=dettaglioCurriculum&esito=0").forward(request, response);
				}
				
				
				//esperienza.setPeriodo(periodo)
				
			}else if (azione.equals("eliminaEsperienza") || azione.equals("eliminaDettaglio") || azione.equals("eliminazioneGlobale")){
				/**
				 *  in questa sezione effettuo l'eliminazione delle singole 
				 *  parti del curriculum
				 */
				log.info("-------------------------------------------------------------------------------------------------------");
				log.info("azione: "+ azione);
				
				int id_risorsa = 0;
				
				if(request.getParameter("parametro") != null){
					id_risorsa = Integer.parseInt(request.getParameter("parametro"));
				}
				
				int esitoEliminazione = 0;
				
				if(azione.equals("eliminaEsperienza")){
					
					int indice = Integer.parseInt(request.getParameter("indice"));
					
					for(int x = 0; x < indice; x++){
						
						int id_esperienza = Integer.parseInt(request.getParameter("parametro"+x));
						
						esitoEliminazione = cDAO.eliminazioneEsperienza(id_esperienza);
						
					}
				}else if(azione.equals("eliminaDettaglio")){
						
					int id_dettaglio = Integer.parseInt(request.getParameter("parametroId"));
						
					esitoEliminazione = cDAO.eliminazioneDettaglio(id_dettaglio);
					
				}else if(azione.equals("eliminazioneGlobale")){
					
 					int indice = Integer.parseInt(request.getParameter("indice"));
					
					for(int x = 0; x < indice; x++){
						
						int idRisorsa = Integer.parseInt(request.getParameter("parametro"+x));
						
						cDAO.eliminaEsperienzaGlobale(idRisorsa);
						cDAO.eliminazioneDettaglioGlobale(idRisorsa);
						cDAO.disabilitazioneFlagCreazioneCurriculum(idRisorsa);
					}
					
					ArrayList<CurriculumDTO> curriculumVitae = cDAO.caricamentoAllCurriculum();
					
					request.setAttribute("listaCurriculum", curriculumVitae);
					
					log.info("url: /index.jsp?azione=visualizzaCurriculum");
					
					getServletContext().getRequestDispatcher("/index.jsp?azione=visualizzaCurriculum").forward(request, response);

					
				}
				
				CurriculumDTO curriculumVitae = cDAO.caricamentoCurriculum(id_risorsa);
				curriculumVitae.setId_risorsa(id_risorsa);
				
				request.setAttribute("curriculumVitae", curriculumVitae);
				
				if(!azione.equals("eliminazioneGlobale")){
					if(esitoEliminazione == 1){
						log.info("url: /index.jsp?azione=dettaglioCurriculum&esito=1");
						
						getServletContext().getRequestDispatcher("/index.jsp?azione=dettaglioCurriculum&esito=1").forward(request, response);
					}else{
						log.info("url: /index.jsp?azione=dettaglioCurriculum&esito=2");
						
						getServletContext().getRequestDispatcher("/index.jsp?azione=dettaglioCurriculum&esito=2").forward(request, response);
					}
				}
				
				
			}else if(azione.equals("esportaPdf")){
				
				log.info("-------------------------------------------------------------------------------------------------------");
				log.info("azione: "+ azione);
				
				//recupero l'id della risorsa
				int id_risorsa = Integer.parseInt(request.getParameter("parametro")); 
				
				CurriculumDTO curriculum =  cDAO.caricamentoCurriculum(id_risorsa);
				
				String sceltaTipoCurriculum = request.getParameter("sceltaCurriculum");
				
				File filePdf = null;
				
				if(sceltaTipoCurriculum.equals("aziendale")){
					filePdf = cDAO.creazioneCurriculumVitaeFormatoAziendale(getServletContext().getRealPath("/"), curriculum, new File(getServletContext().getRealPath("/")+"CurriculumVitae_"+ curriculum.getRisorsa().getCognome() + "_" + curriculum.getRisorsa().getNome() +".pdf"),true);
				}else{
					filePdf = cDAO.creazioneCurriculumVitaeFormatoEuropeo(getServletContext().getRealPath("/"), curriculum, new File(getServletContext().getRealPath("/")+"CurriculumVitae_"+ curriculum.getRisorsa().getCognome() + "_" + curriculum.getRisorsa().getNome() +".pdf"));
				}
				
					
				response.setContentType("application/octet-stream; name=\"" + filePdf.getName() + "\"");
				response.setCharacterEncoding("UTF-8");
				response.addHeader("content-disposition", "attachment; filename=\"" + filePdf.getName() + "\"");
				
				FileInputStream fileInputStream = new FileInputStream(filePdf);
				ServletOutputStream out = response.getOutputStream();
				int i;
				while ((i=fileInputStream.read()) != -1)
					out.write(i);
				fileInputStream.close();
				out.close();
				boolean fileCancellato= new File(getServletContext().getRealPath("/")+"CurriculumVitae"+ curriculum.getRisorsa().getCognome() + curriculum.getRisorsa().getNome() +".pdf").delete();
				log.info("esito della cancellazione del file: " + fileCancellato);
				
			 }else if(azione.equals("selezionaRisorsa")){
				 
				 log.info("-------------------------------------------------------------------------------------------------------");
				 log.info("azione: "+ azione);
				 
				 ArrayList<RisorsaDTO> listaRisorsa = cDAO.caricamentoRisorseSenzaCurriculum();
				 
				 request.setAttribute("listaRisorsa", listaRisorsa);
				 
				 log.info("url: /index.jsp?azione=selezionaRisorsa");
				 
				 getServletContext().getRequestDispatcher("/index.jsp?azione=selezionaRisorsa").forward(request, response);
				 
			 }
		}else{
			
			log.info("Sessione Scaduta - GestioneCurriculum");
			
			sessioneScaduta(response);
		}
	}
}
