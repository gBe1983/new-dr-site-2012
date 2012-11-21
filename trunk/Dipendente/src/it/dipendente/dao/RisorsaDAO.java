package it.dipendente.dao;

import it.dipendente.dto.RisorsaDTO;
import it.util.log.MyLogger;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RisorsaDAO extends BaseDao{//TODO ATTENZIONE UTILIZZARE RisorseDAO per reperire la risorsa
	private MyLogger log;

	public RisorsaDAO(Connection connessione) {
		super(connessione);
		log =new MyLogger(this.getClass());
	}

	public RisorsaDTO loginRisorsa(int idRisorsa){
		final String metodo="loginRisorsa";
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
			if(rs.next()){
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
			log.error(metodo, "select tbl_risorse for risorsa:"+idRisorsa, e);
		}finally{
			close(ps,rs);
			log.end(metodo);
		}
		return risorsa;
	}

	/**
	 * @param idRisorsa
	 * @return profilo della risorsa in parametro
	 */
	public RisorsaDTO visualizzaProfilo(int idRisorsa){
		final String metodo="visualizzaProfilo";
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
			if(rs.next()){
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
			log.error(metodo, "select tbl_risorse for risorsa:"+idRisorsa, e);
		}finally{
			close(ps,rs);
			log.end(metodo);
		}
		return risorsa;
	}

	public String modificaRisorsa(RisorsaDTO risorsa){
		PreparedStatement ps=null;
		int esitoModificaRisorsa = 0;
		String sql = "update tbl_risorse set cognome = ?, nome = ?, data_nascita = ?, luogo_nascita = ?, sesso = ?, cod_fiscale = ?, mail = ?, telefono = ?, cellulare = ?, fax = ?, indirizzo = ?, citta = ?, provincia = ?, cap = ?, nazione = ?, servizio_militare = ?, patente = ?, occupato = ?, figura_professionale = ?, seniority = ? where id_risorsa = ?";
		try {
			ps = connessione.prepareStatement(sql);

			//dati anagrafici
			ps.setString(1, risorsa.getCognome());
			ps.setString(2, risorsa.getNome());
			ps.setString(3, risorsa.getDataNascita());
			ps.setString(4, risorsa.getLuogoNascita());
			ps.setString(5, risorsa.getSesso());
			ps.setString(6, risorsa.getCodiceFiscale());
			ps.setString(7, risorsa.getEmail());
			ps.setString(8, risorsa.getTelefono());
			ps.setString(9, risorsa.getFax());
			ps.setString(10, risorsa.getCellulare());

			//residenza
			ps.setString(11, risorsa.getIndirizzo());
			ps.setString(12, risorsa.getCitta());
			ps.setString(13, risorsa.getProvincia());
			ps.setString(14, risorsa.getCap());
			ps.setString(15, risorsa.getNazione());
			ps.setString(16, risorsa.getServizioMilitare());

			//altri dati
			ps.setString(17, risorsa.getPatente());
			ps.setBoolean(18, risorsa.isOccupato());
			ps.setString(19, risorsa.getFiguraProfessionale());
			ps.setString(20, risorsa.getSeniority());
			ps.setInt(21, risorsa.getIdRisorsa());

			esitoModificaRisorsa = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO log4j
			e.printStackTrace();
		}finally{
			close(ps);
		}
		return (esitoModificaRisorsa == 1)? "ok":"Siamo spiacenti la modifica della risorsa non è avvenuta con successo. Contattare l'amministrazione";
	}

	public String modificaPassword(int idRisorsa, String password){
		PreparedStatement ps=null;
		int esitoModificaPassword = 0;
		String sql = "update tbl_utenti set password = ? where id_risorsa = ?";
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, MD5(password));
			ps.setInt(2, idRisorsa);
			esitoModificaPassword = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO log4j
			e.printStackTrace();
		}finally{
			close(ps);
		}
		return (esitoModificaPassword == 1)? "ok":"Siamo spiacenti la modifica della password non è avvenuta con successo. Contattare l'amministrazione";
	}

	public String MD5(String md5) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
			// TODO add log4j
		}
		return null;
	}
}