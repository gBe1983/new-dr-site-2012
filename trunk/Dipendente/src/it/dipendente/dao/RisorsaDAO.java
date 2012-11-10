package it.dipendente.dao;

import it.dipendente.dto.RisorsaDTO;
import it.dipendente.util.MyLogger;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RisorsaDAO extends BaseDao{
	private MyLogger log;

	public RisorsaDAO(Connection connessione) {
		super(connessione);
		log =new MyLogger(this.getClass());
	}

	public RisorsaDTO loginRisorsa(int idRisorsa){
		PreparedStatement ps=null;
		ResultSet rs=null;
		RisorsaDTO risorsa = null;
		String sql = "select * from tbl_risorse where id_risorsa = ?";
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idRisorsa);
			rs = ps.executeQuery();
			if(rs.next()){
				risorsa = new RisorsaDTO();
				risorsa.setIdRisorsa(rs.getInt(1));
				risorsa.setCognome(rs.getString(2));
				risorsa.setNome(rs.getString(3));
				risorsa.setDataNascita(rs.getString(4));
				risorsa.setLuogoNascita(rs.getString(5));
				risorsa.setSesso(rs.getString(6));
				risorsa.setCodiceFiscale(rs.getString(7));
				risorsa.setEmail(rs.getString(8));
				risorsa.setTelefono(rs.getString(9));
				risorsa.setCellulare(rs.getString(10));
				risorsa.setFax(rs.getString(11));
				risorsa.setIndirizzo(rs.getString(12));
				risorsa.setCitta(rs.getString(13));
				risorsa.setProvincia(rs.getString(14));
				risorsa.setCap(rs.getString(15));
				risorsa.setNazione(rs.getString(16));
				risorsa.setServizioMilitare(rs.getString(17));
				risorsa.setPatente(rs.getString(18));
				risorsa.setCosto(rs.getString(19));
				risorsa.setOccupato(rs.getBoolean(20));
				risorsa.setTipoContratto(rs.getString(21));
				risorsa.setFiguraProfessionale(rs.getString(22));
				risorsa.setSeniority(rs.getString(23));
				risorsa.setVisible(rs.getBoolean(24));
				risorsa.setFlaCreazioneCurriculum(rs.getBoolean(25));
				risorsa.setCv_visibile(rs.getBoolean(26));
			}
		} catch (SQLException e) {
			// TODO add log4j
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		return risorsa;
	}

	/**
	 * @param idRisorsa
	 * @return profilo della risorsa in parametro
	 */
	public RisorsaDTO visualizzaProfilo(int idRisorsa){
		PreparedStatement ps=null;
		ResultSet rs=null;
		RisorsaDTO risorsa = null;
		String sql = "select * from tbl_risorse where id_risorsa = ?";
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idRisorsa);
			rs = ps.executeQuery();
			if(rs.next()){
				risorsa = new RisorsaDTO();
				risorsa.setIdRisorsa(rs.getInt(1));
				risorsa.setCognome(rs.getString(2));
				risorsa.setNome(rs.getString(3));
				risorsa.setDataNascita(rs.getString(4));
				risorsa.setLuogoNascita(rs.getString(5));
				risorsa.setSesso(rs.getString(6));
				risorsa.setCodiceFiscale(rs.getString(7));
				risorsa.setEmail(rs.getString(8));
				risorsa.setTelefono(rs.getString(9));
				risorsa.setCellulare(rs.getString(10));
				risorsa.setFax(rs.getString(11));
				risorsa.setIndirizzo(rs.getString(12));
				risorsa.setCitta(rs.getString(13));
				risorsa.setProvincia(rs.getString(14));
				risorsa.setCap(rs.getString(15));
				risorsa.setNazione(rs.getString(16));
				risorsa.setServizioMilitare(rs.getString(17));
				risorsa.setPatente(rs.getString(18));
				risorsa.setCosto(rs.getString(19));
				risorsa.setOccupato(rs.getBoolean(20));
				risorsa.setTipoContratto(rs.getString(21));
				risorsa.setFiguraProfessionale(rs.getString(22));
				risorsa.setSeniority(rs.getString(23));
			}
		} catch (SQLException e) {
			// TODO add log4j
			e.printStackTrace();
		}finally{
			close(ps,rs);
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