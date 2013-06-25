package it.dipendente.dao;

import it.dipendente.dto.Associaz_Risors_Comm_DTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class Associaz_Risors_Comm_DAO extends BaseDao{
	private Logger log = null;

	public Associaz_Risors_Comm_DAO(Connection connessione) {
		super(connessione);
		log= Logger.getLogger(Associaz_Risors_Comm_DAO.class);
	}

	/**
	 * @param idRisorsa
	 * @return commesse attive legata alla risorsa solo con tipologia "Altro"
	 */
	public ArrayList<Associaz_Risors_Comm_DTO> caricamentoCommesseAssociateRisorse(int id_risorsa,Date dataInizio, Date dataFine){
		
		SimpleDateFormat formatSql = new SimpleDateFormat("yyyy-MM-dd");
		
		log.info("caricamentoCommesseAssociateRisorse");
		
		String sql = "select asscommessa.id_commessa,commessa.descrizione from tbl_associaz_risor_comm as asscommessa, tbl_commesse as commessa,tbl_planning as planning " +
				"where asscommessa.id_commessa=commessa.id_commessa and planning.id_associazione = asscommessa.id_associazione and commessa.id_tipologia_commessa<>4 " +
				"and asscommessa.id_risorsa=? and planning.data >= ? and planning.data <= ? " +
				"group by commessa.descrizione order by commessa.data_inizio,commessa.descrizione";
		
		log.debug("sql: select asscommessa.id_commessa,commessa.descrizione from tbl_associaz_risor_comm as asscommessa, tbl_commesse as commessa,tbl_planning as planning " +
				"where asscommessa.id_commessa=commessa.id_commessa and planning.id_associazione = asscommessa.id_associazione and commessa.id_tipologia_commessa<>4 " +
				"and asscommessa.id_risorsa="+id_risorsa+" and planning.data >= "+formatSql.format(dataInizio)+" and planning.data <= "+formatSql.format(dataFine)+ 
				" group by commessa.descrizione order by commessa.data_inizio,commessa.descrizione");
		
		ArrayList<Associaz_Risors_Comm_DTO> listaCommesse = new ArrayList<Associaz_Risors_Comm_DTO>();
		try {
			PreparedStatement ps = connessione.prepareStatement(sql);
			ps.setInt(1,id_risorsa);
			ps.setString(2, formatSql.format(dataInizio));
			ps.setString(3, formatSql.format(dataFine));
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				listaCommesse.add(new Associaz_Risors_Comm_DTO(rs.getInt(1),rs.getString(2)));
			}
		} catch (SQLException e) {
			log.error("errore sql: " +  e);
		}
		return listaCommesse;
	}

	/**
	 * @param idRisorsa
	 * @return commesse attive legata alla risorsa escludendo quelle con tipologia "Altro"
	 */
	public ArrayList<Associaz_Risors_Comm_DTO> caricamentoCommesseAttive(int idRisorsa){
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		ArrayList<Associaz_Risors_Comm_DTO> listaCommesse = new ArrayList<Associaz_Risors_Comm_DTO>();
		
		String sql = "select asscommessa.*,commessa.descrizione from tbl_associaz_risor_comm as asscommessa,tbl_commesse as commessa where asscommessa.id_commessa=commessa.id_commessa and asscommessa.id_risorsa=? and commessa.id_tipologia_commessa<>4 and asscommessa.attiva=true";
		
		log.debug("sql: select asscommessa.*,commessa.descrizione from tbl_associaz_risor_comm as asscommessa,tbl_commesse as commessa where asscommessa.id_commessa=commessa.id_commessa and asscommessa.id_risorsa="+idRisorsa+" and commessa.id_tipologia_commessa<>4 and asscommessa.attiva=true");
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idRisorsa);
			rs = ps.executeQuery();
			while(rs.next()){
				listaCommesse.add(new Associaz_Risors_Comm_DTO(rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getBoolean(8),rs.getString(9)));
			}
		} catch (SQLException e) {
			log.error("errore sql: " +  e);
		}finally{
			close(ps,rs);
		}
		return listaCommesse;
	}

	/**
	 * @param idRisorsa
	 * @return commesse non attive legata alla risorsa escludendo quelle con tipologia "Altro"
	 */
	public ArrayList<Associaz_Risors_Comm_DTO> caricamentoCommesseNonAttive(int idRisorsa){
		
		log.info("caricamentoCommesseNonAttive");
		PreparedStatement ps=null;
		ResultSet rs=null;
		ArrayList<Associaz_Risors_Comm_DTO> listaCommesse = new ArrayList<Associaz_Risors_Comm_DTO>();
		String sql = "select asscommessa.*,commessa.descrizione from tbl_associaz_risor_comm as asscommessa,tbl_commesse as commessa where asscommessa.id_commessa=commessa.id_commessa and asscommessa.id_risorsa=? and commessa.id_tipologia_commessa<>4 and asscommessa.attiva=false";
		log.debug("sql: select asscommessa.*,commessa.descrizione from tbl_associaz_risor_comm as asscommessa,tbl_commesse as commessa where asscommessa.id_commessa=commessa.id_commessa and asscommessa.id_risorsa="+idRisorsa+" and commessa.id_tipologia_commessa<>4 and asscommessa.attiva=false");
		try{
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idRisorsa);
			rs = ps.executeQuery();
			while(rs.next()){
				listaCommesse.add(new Associaz_Risors_Comm_DTO(rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getBoolean(8),rs.getString(9)));
			}
		}catch(SQLException e){
			log.error("errore sql: "+ e);
		}finally{
			close(ps,rs);
		}
		return listaCommesse;
	}

	/**
	 * @param idCommessa
	 * @return Descrizione Commessa
	 */
	public String caricamentoDescrizioneCommessa(int idCommessa){
		log.info("caricamentoDescrizioneCommessa");
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql = "select id_cliente,descrizione as ragione_sociale from tbl_associaz_risor_comm as asscommessa, tbl_commesse as commessa where asscommessa.id_commessa=commessa.id_commessa and asscommessa.id_commessa=?";
		log.debug("sql: select id_cliente,descrizione as ragione_sociale from tbl_associaz_risor_comm as asscommessa, tbl_commesse as commessa where asscommessa.id_commessa=commessa.id_commessa and asscommessa.id_commessa="+idCommessa);
		String descrizioneCommessa = "";
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idCommessa);
			rs = ps.executeQuery();
			if(rs.next()){
				if(!StringUtils.isEmpty(rs.getString(1))){
					descrizioneCommessa = descrizioneCliente(rs.getString(1));
				}else{
					descrizioneCommessa = rs.getString(2);
				}
			}
		}catch(SQLException e){
			log.error("errore sql: " + e);
		}finally{
			close(ps,rs);
		}
		return descrizioneCommessa;
	}

	private String descrizioneCliente(String id_cliente){
		
		log.info("descrizioneCliente");
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql = "select ragione_sociale from tbl_clienti where id_cliente=?";
		log.debug("sql: select ragione_sociale from tbl_clienti where id_cliente="+id_cliente);
		String descrizioneCliente = "";
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, id_cliente);
			rs = ps.executeQuery();
			if(rs.next()){
				descrizioneCliente = rs.getString(1);
			}
		}catch(SQLException e){
			log.error("errore sql: " + e);
		}finally{
			close(ps,rs);
		}
		return descrizioneCliente;
	}
}