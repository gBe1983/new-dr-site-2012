package it.dipendente.dao;

import it.dipendente.dto.Dettaglio_Cv_DTO;
import it.dipendente.dto.EsperienzeDTO;
import it.dipendente.dto.RisorsaDTO;
import it.util.log.MyLogger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CurriculumDAO extends BaseDao{
	private MyLogger log;

	public CurriculumDAO(Connection connessione) {
		super(connessione);
		log =new MyLogger(this.getClass());
	}

	public boolean verificaCreazioneCurriculum(int idRisorsa){
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql = "select flag_creazione_cv from tbl_risorse where id_risorsa=?";
		boolean controlloCreazioneCurriculum = false;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idRisorsa);
			rs = ps.executeQuery();
			if(rs.next()){
				controlloCreazioneCurriculum = rs.getBoolean(1);
			}
		} catch (SQLException e) {
			// TODO add log4j
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		return controlloCreazioneCurriculum;
	}	

	/**
	 * abilitazione del flag curriculum
	 * @param idRisorsa
	 */
	public void creazioneFlagCreazioneCurriculum(int idRisorsa){
		PreparedStatement ps=null;
		String sql = "update tbl_risorse set flag_creazione_cv=? where id_risorsa=?";
		try {
			ps = connessione.prepareStatement(sql);
			ps.setBoolean(1, true);
			ps.setInt(2, idRisorsa);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO add log4j
			e.printStackTrace();
		}finally{
			close(ps);
		}
	}

	/**
	 * inserimento delle esperienza nella tabella "Tbl_Esperienze_Professionali_Cv"
	 * @param esperienze
	 */
	public void inserimentoEsperienze(EsperienzeDTO esperienze){
		PreparedStatement ps=null;
		String sql = "insert into tbl_esperienze_professionali_cv(periodo,azienda,luogo,descrizione,id_risorsa)values(?,?,?,?,?)";
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, esperienze.getPeriodo());
			ps.setString(2, esperienze.getAzienda());
			ps.setString(3, esperienze.getLuogo());
			ps.setString(4, esperienze.getDescrizione());
			ps.setInt(5, esperienze.getId_risorsa());
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO add log4j
			e.printStackTrace();
		}finally{
			close(ps);
		}
	}

	public void inserimentoDettaglio(Dettaglio_Cv_DTO dettaglio){
		PreparedStatement ps=null;
		String sql = "insert into tbl_dettaglio_cv(capacita_professionali,competenze_tecniche,lingue_straniere,istruzione,formazione,interessi,id_risorsa)values(?,?,?,?,?,?,?)";
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, dettaglio.getCapacita_professionali());
			ps.setString(2, dettaglio.getCompetenze_tecniche());
			ps.setString(3, dettaglio.getLingue_Straniere());
			ps.setString(4, dettaglio.getIstruzione());
			ps.setString(5, dettaglio.getFormazione());
			ps.setString(6, dettaglio.getInteressi());
			ps.setInt(7, dettaglio.getId_risorsa());
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO add log4j
			e.printStackTrace();
		}finally{
			close(ps);
		}
	}

	/**
	 * caricamento dei dati del curriculum
	 * @param idRisorsa
	 * @return
	 * @throws IOException
	 */
	public ArrayList caricamentoCurriculum(int idRisorsa) throws IOException{
		ArrayList curriculum = new ArrayList();
		curriculum.add(caricamentoProfiloRisorsa(idRisorsa));
		curriculum = caricamentoEsperienze(idRisorsa, curriculum);
		curriculum = caricamentoDettaglio(idRisorsa, curriculum);
		return curriculum;
	}

	public RisorsaDTO caricamentoProfiloRisorsa(int idRisorsa){//TODO ELIMINARE QUESTO METODO... 
		final String metodo="caricamentoProfiloRisorsa";
		log.start(metodo);
		PreparedStatement ps=null;
		ResultSet rs=null;
		RisorsaDTO risorsa = null;
		StringBuilder sql = new StringBuilder("SELECT ");
		sql	.append("id_risorsa,cognome,nome,data_nascita,luogo_nascita,sesso,cod_fiscale,mail,telefono")
				.append("cellulare,fax,indirizzo,citta,provincia,cap,nazione,servizio_militare,patente,costo,occupato,")
				.append("tipo_contratto,figura_professionale,seniority,visible,flag_creazione_cv,cv_visibile ")
				.append("FROM tbl_risorse WHERE id_risorsa=?");
		log.debug(metodo,"sql:"+sql.toString());
		try {
			ps = connessione.prepareStatement(sql.toString());
			ps.setInt(1, idRisorsa);
			rs = ps.executeQuery();
			while(rs.next()){
				risorsa = new RisorsaDTO(	rs.getInt("id_risorsa"),
														rs.getString("cognome"),
														rs.getString("nome"),
														rs.getString("data_nascita"),
														rs.getString("luogo_nascita"),
														rs.getString("sesso"),
														rs.getString("cod_fiscale"),
														rs.getString("mail"),
														rs.getString("telefono"),
														rs.getString("cellulare"),
														rs.getString("fax"),
														rs.getString("indirizzo"),
														rs.getString("citta"),
														rs.getString("provincia"),
														rs.getString("cap"),
														rs.getString("nazione"),
														rs.getString("servizio_militare"),
														rs.getString("patente"),
														rs.getString("costo"),
														rs.getBoolean("occupato"),
														rs.getString("tipo_contratto"),
														rs.getString("figura_professionale"),
														rs.getString("seniority"),
														rs.getBoolean("visible"),
														rs.getBoolean("flag_creazione_cv"),
														rs.getBoolean("cv_visibile"));
			}
		} catch (SQLException e) {
			// TODO add log4j
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		return risorsa;
	}

	public ArrayList caricamentoEsperienze(int idRisorsa, ArrayList curriculum) throws IOException{
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql = "select * from tbl_esperienze_professionali_cv where id_risorsa = ? and visibile = true";
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idRisorsa);
			rs = ps.executeQuery();
			while(rs.next()){
				EsperienzeDTO esperienze = new EsperienzeDTO();
				esperienze.setIdEsperienze(rs.getInt(1));
				esperienze.setPeriodo(rs.getString(2));
				esperienze.setAzienda(rs.getString(3));
				esperienze.setLuogo(rs.getString(4));
				esperienze.setDescrizione(rs.getString(5));
				esperienze.setId_risorsa(Integer.parseInt(rs.getString(6)));
				esperienze.setVisibile(rs.getBoolean(7));
				
				curriculum.add(esperienze);
			}
		} catch (SQLException e) {
			// TODO add log4j
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		return curriculum;
	}

	public ArrayList caricamentoDettaglio(int idRisorsa, ArrayList curriculum) throws IOException{
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql = "select * from tbl_dettaglio_cv where id_risorsa = ? and visible = true";
		
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idRisorsa);
			rs = ps.executeQuery();
			while(rs.next()){
				Dettaglio_Cv_DTO dettaglio = new Dettaglio_Cv_DTO();
				dettaglio.setId_dettaglio(rs.getInt(1));
				dettaglio.setCapacita_professionali(rs.getString(2));
				dettaglio.setCompetenze_tecniche(rs.getString(3));
				dettaglio.setLingue_Straniere(rs.getString(4));
				dettaglio.setIstruzione(rs.getString(5));
				dettaglio.setFormazione(rs.getString(6));
				dettaglio.setInteressi(rs.getString(7));
				dettaglio.setId_risorsa(rs.getInt(8));
				
				curriculum.add(dettaglio);
			}
		} catch (SQLException e) {
			// TODO add log4j
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		return curriculum;
	}

	/**
	 * aggiornamento della singola Esperinza
	 * @param esperienza
	 * @return messaggio per salvataggio avvenuto o meno
	 * @throws IOException
	 */
	public String aggiornamentoEsperienza(EsperienzeDTO esperienza) throws IOException{
		PreparedStatement ps=null;
		String sql = "update tbl_esperienze_professionali_cv set periodo=?,azienda=?,luogo=?,descrizione=?,id_risorsa=? where id_esperienza_professionale=?";
		int esitoModificaEsperienza = 0;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, esperienza.getPeriodo());
			ps.setString(2, esperienza.getAzienda());
			ps.setString(3, esperienza.getLuogo());
			ps.setString(4, esperienza.getDescrizione());
			ps.setInt(5, esperienza.getId_risorsa());
			ps.setInt(6, esperienza.getIdEsperienze());
			esitoModificaEsperienza = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO log4j
			e.printStackTrace();
		}finally{
			close(ps);
		}
		return (esitoModificaEsperienza == 1)? "ok":"Siamo spiacenti la modifica della singola esperienza non è avvenuta con successo. Contattare l'amministrazione.";
	}

	/**
	 * aggiornamento della singola Esperinza
	 * @param dettaglio
	 * @return messaggio per salvataggio avvenuto o meno
	 * @throws IOException
	 */
	public String aggiornamentoDettaglio(Dettaglio_Cv_DTO dettaglio) throws IOException{
		PreparedStatement ps=null;
		String sql = "update tbl_dettaglio_cv set capacita_professionali=?,competenze_tecniche=?,lingue_straniere=?,istruzione=?,formazione=?,interessi=? where id_risorsa=? and id_dettaglio=?";
		int esitoModificaDettaglio = 0;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, dettaglio.getCapacita_professionali());
			ps.setString(2, dettaglio.getCompetenze_tecniche());
			ps.setString(3, dettaglio.getLingue_Straniere());
			ps.setString(4, dettaglio.getIstruzione());
			ps.setString(5, dettaglio.getFormazione());
			ps.setString(6, dettaglio.getInteressi());
			ps.setInt(7, dettaglio.getId_risorsa());
			ps.setInt(8, dettaglio.getId_dettaglio());
			esitoModificaDettaglio = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO log4j
			e.printStackTrace();
		}finally{
			close(ps);
		}
		return (esitoModificaDettaglio == 1)? "ok":"Siamo spiacenti la modifica del dettaglio curriculum non è avvenuta con successo. Contattare l'amministrazione.";
	}

	/**
	 * eliminazione dell'esperienza
	 * @param idEsperienza
	 * @return messaggio per salvataggio avvenuto o meno
	 * @throws IOException
	 */
	public String eliminazioneEsperienza(int idEsperienza) throws IOException{
		PreparedStatement ps=null;
		String sql = "update tbl_esperienze_professionali_cv set visibile=? where id_esperienza_professionale=?";
		int esitoEliminazioneEsperienza = 0;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setBoolean(1, false);
			ps.setInt(2, idEsperienza);
			esitoEliminazioneEsperienza = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO log4j
			e.printStackTrace();
		}finally{
			close(ps);
		}
		return (esitoEliminazioneEsperienza == 1)? "ok":"Siamo spiacenti l'eliminazione della singola esperienza non è avvenuta con successo. Contattare l'amministrazione.";
	}
}