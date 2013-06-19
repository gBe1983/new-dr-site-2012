package it.dipendente.dao;

import it.dipendente.bo.Month;
import it.dipendente.dto.CommessaDTO;
import it.dipendente.dto.PlanningDTO;
import it.util.log.MyLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class PlanningDAO extends BaseDao{
	private Logger log;

	public PlanningDAO(Connection connessione) {
		super(connessione);
		log= Logger.getLogger(BaseDao.class);
	}

	/**
	 * tramite questo metodo effettuo l'aggiornamento del planning
	 * @param planning
	 */
	public int aggiornamentoPlanning(PlanningDTO planning){
		
		log.info("metodo: aggiornamentoPlanning");
		
		String sql = "UPDATE tbl_planning SET num_ore=?,straordinari=?,orario=?,note=?,ferie = ?,permessi = ?,mutua = ?,permessiNonRetribuiti = ? WHERE id_planning=?";
		
		log.info("sql: UPDATE tbl_planning SET num_ore="+planning.getNumeroOre()+",straordinari="+planning.getStraordinari()+",orario="+planning.getOrario()+",note="+planning.getNote());
		if(planning.isFerie()){
			log.info("ferie = "+ planning.getAssenze());
		}else{
			log.info("ferie = 0.0");
		}
		if(planning.isPermessi()){
			log.info("permessi = "+ planning.getAssenze());
		}else{
			log.info("permessi = 0.0");
		}
		if(planning.isMutua()){
			log.info("mutua = "+ planning.getAssenze());
		}else{
			log.info("mutua = 0.0");
		}
		if(planning.isPermessiNonRetribuiti()){
			log.info("permessiNonRetribuiti = "+ planning.getAssenze());
		}else{
			log.info("permessiNonRetribuiti = 0.0");
		}
		log.info(" WHERE id_planning="+planning.getId_planning());
		
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setDouble(1, planning.getNumeroOre());
			ps.setDouble(2, planning.getStraordinari());
			ps.setString(3, planning.getOrario());
			ps.setString(4, planning.getNote());
			if(planning.isFerie()){
				ps.setDouble(5, planning.getAssenze());
			}else{
				ps.setDouble(5, 0.0);
			}
			if(planning.isPermessi()){
				ps.setDouble(6, planning.getAssenze());
			}else{
				ps.setDouble(6, 0.0);
			}
			if(planning.isMutua()){
				ps.setDouble(7, planning.getAssenze());
			}else{
				ps.setDouble(7, 0.0);
			}
			if(planning.isPermessiNonRetribuiti()){
				ps.setDouble(8, planning.getAssenze());
			}else{
				ps.setDouble(8, 0.0);
			}
			ps.setInt(9, planning.getId_planning());
			return ps.executeUpdate();
		} catch (SQLException e) {
			log.error("errore sql:"+ e);
		}finally{
			close(ps);
		}
		return 0;
	}

	public ArrayList getGiornate(int id_risorsa, Calendar day,String parametro){
		
		log.info("metodo: getGiornate");
		
		ArrayList caricamentoGiornate = new ArrayList();
		ArrayList<PlanningDTO> listaGiorni = new ArrayList<PlanningDTO>();
		ArrayList<CommessaDTO> listaCommesse = new ArrayList<CommessaDTO>();
		
		String now = new SimpleDateFormat("yyyy-MM-%").format(day.getTime());
		PreparedStatement ps=null;
		ResultSet rs=null;
		StringBuilder sql = new StringBuilder("select planning.id_planning,");
		sql	.append("planning.data,planning.num_ore,planning.straordinari,planning.orario,planning.note,")
				.append("planning.ferie, planning.permessi, planning.mutua, planning.permessiNonRetribuiti, ")
				.append("asscommessa.id_associazione,")
				.append("commessa.descrizione as descrizioneCommessa,commessa.codice_commessa as codice")
				.append(" from tbl_planning planning,tbl_associaz_risor_comm asscommessa,tbl_commesse commessa")
				.append(" where planning.id_associazione=asscommessa.id_associazione")
				.append(" and asscommessa.id_commessa=commessa.id_commessa")
				.append(" and planning.data like ? and asscommessa.id_risorsa=? and planning.attivo = true")
				.append(" order by data");
		
		log.info("sql: select planning.id_planning,planning.data,planning.num_ore,planning.straordinari,planning.orario,planning.note,planning.ferie, planning.permessi, planning.mutua, " +
				" planning.permessiNonRetribuiti,asscommessa.id_associazione,commessa.descrizione as descrizioneCommessa,commessa.codice_commessa as codice " +
				" from tbl_planning planning,tbl_associaz_risor_comm asscommessa,tbl_commesse commessa " +
				" where planning.id_associazione=asscommessa.id_associazione " +
				" and asscommessa.id_commessa=commessa.id_commessa " +
				" and planning.data like "+now+" and asscommessa.id_risorsa="+id_risorsa+" and planning.attivo = true order by data");
		
		String descrizioneCommessa = "";
		
		
		try {
			ps = connessione.prepareStatement(sql.toString());
			ps.setString(1,now);
			ps.setInt(2,id_risorsa);
			rs = ps.executeQuery();
			while (rs.next()){
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(rs.getDate("data"));
					
					double assenze = 0.0;
					boolean ferie = false;
					boolean permessi = false;
					boolean mutua = false;
					boolean permessiNonRetribuiti = false;
					
					if(rs.getDouble("ferie") != 0.0){
						assenze = rs.getDouble("ferie");
						ferie = true;
					}else if(rs.getDouble("permessi") != 0.0){
						assenze = rs.getDouble("permessi");
						permessi = true;
					}else if(rs.getDouble("mutua") != 0.0){
						assenze = rs.getDouble("mutua");
						mutua = true;
					}else if(rs.getDouble("permessiNonRetribuiti") != 0.0){
						assenze = rs.getDouble("permessiNonRetribuiti");
						permessiNonRetribuiti = true;
					}
					
					int numeroSettimana = 0;
					if(calendar.get(Calendar.DAY_OF_WEEK) == 1 && !parametro.equals("localhost")){
						numeroSettimana = calendar.get(Calendar.WEEK_OF_MONTH)-1;
					}else{
						numeroSettimana = calendar.get(Calendar.WEEK_OF_MONTH);
					}
					
					PlanningDTO planning = new PlanningDTO(rs.getInt("id_planning"),
									rs.getDate("data"),
									rs.getDouble("num_ore"),
									rs.getDouble("straordinari"),
									rs.getString("orario"),
									rs.getString("note"),
									rs.getInt("id_associazione"),
									rs.getString("descrizioneCommessa"),
									rs.getString("codice"),
									numeroSettimana,
									assenze,
									ferie,
									permessi,
									mutua,
									permessiNonRetribuiti);
					listaGiorni.add(planning);
					
					log.info("getGiornate giorno " + new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime()));
					log.info("getGiornate numeroSettimana " + numeroSettimana);
					
					
					if(listaCommesse.size() == 0){
						CommessaDTO commessa = new CommessaDTO();
						commessa.setDescrizione(rs.getString("descrizioneCommessa"));
						commessa.setCodiceCommessa(rs.getString("codice"));
						listaCommesse.add(commessa);
					}else{
						boolean controlloEsistenzaCommessa = false;
						for(int x = 0; x < listaCommesse.size(); x++){
							if(listaCommesse.get(x).getDescrizione().equals(rs.getString("descrizioneCommessa"))){
								controlloEsistenzaCommessa = true;
							}
						}
						if(!controlloEsistenzaCommessa){
							CommessaDTO commessa = new CommessaDTO();
							commessa.setDescrizione(rs.getString("descrizioneCommessa"));
							commessa.setCodiceCommessa(rs.getString("codice"));
							listaCommesse.add(commessa);
						}
					}
			}
		} catch (SQLException e) {
			log.error("errore sql: " + e);
		}finally{
			close(ps,rs);
		}
		
		caricamentoGiornate.add(listaCommesse);
		caricamentoGiornate.add(listaGiorni);
		
		return caricamentoGiornate;
	}
	
	public HashMap<Integer, String> elencoGiorniDellaSettimana(){
		
		log.info("metodo: elencoGiorniDellaSettimana");
		
		HashMap<Integer, String> elencoGiorni = new HashMap<Integer,String>();
		elencoGiorni.put(2,"Lunedi");
		elencoGiorni.put(3,"Martedi");
		elencoGiorni.put(4,"Mercoledi");
		elencoGiorni.put(5,"Giovedi");
		elencoGiorni.put(6,"Venerdi");
		elencoGiorni.put(7,"Sabato");
		elencoGiorni.put(8,"Domenica");
		
		return elencoGiorni;
	}
	
	public HashMap<String, Boolean> caricamentoFlagAssenze(int id_risorsa){
		
		log.info("metodo: caricamentoFlagAssenze");
		
		HashMap flagAssenze = new HashMap<String, Boolean>();
		
		String sql = "select altro.* from tbl_commesse as commessa, tbl_associaz_risor_comm as asscomm, tbl_altro as altro where id_tipologia_commessa = 4 and commessa.id_commessa = altro.id_commessa and commessa.data_inizio like ? and commessa.id_commessa = asscomm.id_commessa and asscomm.id_risorsa = ?";
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		Calendar calendar = Calendar.getInstance();
		String now = new SimpleDateFormat("yyyy-%").format(calendar.getTime());
		
		log.info("sql : select altro.* from tbl_commesse as commessa, tbl_associaz_risor_comm as asscomm, tbl_altro as altro where id_tipologia_commessa = 4 and commessa.id_commessa = altro.id_commessa and commessa.data_inizio like "+now+" and commessa.id_commessa = asscomm.id_commessa and asscomm.id_risorsa = "+id_risorsa);
		
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, now);
			ps.setInt(2, id_risorsa);
			rs = ps.executeQuery();
			if(rs.next()){
				flagAssenze.put("ferie", rs.getBoolean(3));
				flagAssenze.put("mutua", rs.getBoolean(4));
				flagAssenze.put("permessi", rs.getBoolean(5));
				flagAssenze.put("permessiNonRetribuiti", rs.getBoolean(6));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("errore sql: " + e);
		}
		
		return flagAssenze;
	}
}