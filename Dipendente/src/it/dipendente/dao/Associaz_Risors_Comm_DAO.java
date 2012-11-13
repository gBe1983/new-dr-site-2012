package it.dipendente.dao;

import it.dipendente.dto.Associaz_Risors_Comm_DTO;
import it.util.log.MyLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

public class Associaz_Risors_Comm_DAO extends BaseDao{
	private MyLogger log;

	public Associaz_Risors_Comm_DAO(Connection connessione) {
		super(connessione);
		log=new MyLogger(this.getClass());
	}

	/**
	 * @param idRisorsa
	 * @return commesse attive legata alla risorsa solo con tipologia "Altro"
	 */
	public ArrayList<Associaz_Risors_Comm_DTO> caricamentoCommesseAssociateRisorse(int id_risorsa){
		final String metodo="caricamentoCommesseAssociateRisorse";
		log.start(metodo);
		String sql = "select asscommessa.id_commessa,commessa.descrizione from tbl_associaz_risor_comm as asscommessa, tbl_commesse as commessa where asscommessa.id_commessa=commessa.id_commessa and asscommessa.id_risorsa=? and commessa.id_tipologia_commessa=4 order by commessa.descrizione";
		log.debug(metodo,"sql:"+sql);
		ArrayList<Associaz_Risors_Comm_DTO> listaCommesse = new ArrayList<Associaz_Risors_Comm_DTO>();
		try {
			PreparedStatement ps = connessione.prepareStatement(sql);
			log.debug(metodo, "id_risorsa:"+id_risorsa);
			ps.setInt(1,id_risorsa);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				listaCommesse.add(new Associaz_Risors_Comm_DTO(rs.getInt(1),rs.getString(2)));
			}
		} catch (SQLException e) {
			log.error(metodo, "select tbl_associaz_risor_comm,tbl_commesse for id_risorsa:"+id_risorsa, e);
		}
		log.info(metodo, "trovate "+listaCommesse.size()+" Commesse associate alla risorsa:"+id_risorsa);
		log.end(metodo);
		return listaCommesse;
	}

	/**
	 * @param idRisorsa
	 * @return commesse attive legata alla risorsa escludendo quelle con tipologia "Altro"
	 */
	public ArrayList<Associaz_Risors_Comm_DTO> caricamentoCommesseAttive(int idRisorsa){
		final String metodo="caricamentoCommesseAttive";
		log.start(metodo);
		PreparedStatement ps=null;
		ResultSet rs=null;
		ArrayList<Associaz_Risors_Comm_DTO> listaCommesse = new ArrayList<Associaz_Risors_Comm_DTO>();
		String sql = "select asscommessa.*,commessa.descrizione from tbl_associaz_risor_comm as asscommessa,tbl_commesse as commessa where asscommessa.id_commessa=commessa.id_commessa and asscommessa.id_risorsa=? and commessa.id_tipologia_commessa<>4 and asscommessa.attiva=true";
		log.debug(metodo,"sql:"+sql);
		try {
			ps = connessione.prepareStatement(sql);
			log.debug(metodo, "idRisorsa:"+idRisorsa);
			ps.setInt(1, idRisorsa);
			rs = ps.executeQuery();
			while(rs.next()){
				listaCommesse.add(new Associaz_Risors_Comm_DTO(rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getBoolean(8),rs.getString(9)));
			}
		} catch (SQLException e) {
			log.error(metodo, "select tbl_associaz_risor_comm,tbl_commesse for idRisorsa:"+idRisorsa, e);
		}finally{
			close(ps,rs);
		}
		log.info(metodo, "trovate "+listaCommesse.size()+" Commesse attive, escluse tipologia Altro associate alla risorsa:"+idRisorsa);
		log.end(metodo);
		return listaCommesse;
	}

	/**
	 * @param idRisorsa
	 * @return commesse non attive legata alla risorsa escludendo quelle con tipologia "Altro"
	 */
	public ArrayList<Associaz_Risors_Comm_DTO> caricamentoCommesseNonAttive(int idRisorsa){
		final String metodo="caricamentoCommesseNonAttive";
		log.start(metodo);
		PreparedStatement ps=null;
		ResultSet rs=null;
		ArrayList<Associaz_Risors_Comm_DTO> listaCommesse = new ArrayList<Associaz_Risors_Comm_DTO>();
		String sql = "select asscommessa.*,commessa.descrizione from tbl_associaz_risor_comm as asscommessa,tbl_commesse as commessa where asscommessa.id_commessa=commessa.id_commessa and asscommessa.id_risorsa=? and commessa.id_tipologia_commessa<>4 and asscommessa.attiva=false";
		log.debug(metodo,"sql:"+sql);
		try{
			ps = connessione.prepareStatement(sql);
			log.debug(metodo, "idRisorsa:"+idRisorsa);
			ps.setInt(1, idRisorsa);
			rs = ps.executeQuery();
			while(rs.next()){
				listaCommesse.add(new Associaz_Risors_Comm_DTO(rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getBoolean(8),rs.getString(9)));
			}
		}catch(SQLException e){
			log.error(metodo, "select tbl_associaz_risor_comm,tbl_commesse for idRisorsa:"+idRisorsa, e);
		}finally{
			close(ps,rs);
		}
		log.info(metodo, "trovate "+listaCommesse.size()+" Commesse non attive, escluse tipologia Altro associate alla risorsa:"+idRisorsa);
		log.end(metodo);
		return listaCommesse;
	}

	/**
	 * @param idCommessa
	 * @return Descrizione Commessa
	 */
	public String caricamentoDescrizioneCommessa(int idCommessa){
		final String metodo="caricamentoDescrizioneCommessa";
		log.start(metodo);
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql = "select id_cliente,descrizione as ragione_sociale from tbl_associaz_risor_comm as asscommessa, tbl_commesse as commessa where asscommessa.id_commessa=commessa.id_commessa and asscommessa.id_commessa=?";
		log.debug(metodo,"sql:"+sql);
		String descrizioneCommessa = "";
		try {
			ps = connessione.prepareStatement(sql);
			log.debug(metodo, "idCommessa:"+idCommessa);
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
			log.error(metodo,"select tbl_associaz_risor_comm,tbl_commesse for idCommessa"+idCommessa,e);
		}finally{
			close(ps,rs);
		}
		log.end(metodo);
		return descrizioneCommessa;
	}

	private String descrizioneCliente(String id_cliente){
		final String metodo="descrizioneCliente";
		log.start(metodo);
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql = "select ragione_sociale from tbl_clienti where id_cliente=?";
		log.debug(metodo,"sql:"+sql);
		String descrizioneCliente = "";
		try {
			ps = connessione.prepareStatement(sql);
			log.debug(metodo, "id_cliente:"+id_cliente);
			ps.setString(1, id_cliente);
			rs = ps.executeQuery();
			if(rs.next()){
				descrizioneCliente = rs.getString(1);
			}
		}catch(SQLException e){
			log.error(metodo,"select tbl_clienti for id_cliente:"+id_cliente,e);
		}finally{
			close(ps,rs);
		}
		log.end(metodo);
		return descrizioneCliente;
	}
}