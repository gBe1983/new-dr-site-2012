package it.dipendente.dao;

import it.dipendente.dto.Associaz_Risors_Comm_DTO;
import it.dipendente.dto.PlanningDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Associaz_Risors_Comm_DAO extends BaseDao{

	public Associaz_Risors_Comm_DAO(Connection connessione) {
		super(connessione);
		
	}

	/**
	 * tramite questo metodo effettuo l'aggiornamento delle date
	 * @param planning
	 */
	public void aggiornamentoGiorniMensili(PlanningDTO planning){
		String sql = "update tbl_planning set num_ore=?,orario=?,descr_attivita=?,note=? where data=? and id_associazione=?";
		int esitoInserimentoReport = 0;
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setDouble(1, planning.getNumeroOre());
			ps.setString(2, planning.getOrario());
			ps.setString(3, planning.getDescr_attivita());
			ps.setString(4, planning.getNote());
			ps.setString(5, planning.getData());
			ps.setInt(6, planning.getId_associazione());
			esitoInserimentoReport = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO add log4j
			e.printStackTrace();
		}
		finally{
			close(ps);
		}
		if(esitoInserimentoReport == 1){
			// TODO add log4j
			System.out.println("modifica effettuata con successo");
		}else{
			// TODO add log4j
			System.out.println("modifica non effettuata con successo");
		}
	}
	
	public ArrayList<Associaz_Risors_Comm_DTO> caricamentoCommesseAssociateRisorse(int id_risorsa, Connection conn){
		String sql = "select asscommessa.id_commessa,commessa.descrizione from tbl_associaz_risor_comm as asscommessa, tbl_commesse as commessa where asscommessa.id_commessa=commessa.id_commessa and asscommessa.id_risorsa=? and commessa.id_tipologia_commessa=4 order by commessa.descrizione";
		ArrayList<Associaz_Risors_Comm_DTO> listaCommesse = new ArrayList<Associaz_Risors_Comm_DTO>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1,id_risorsa);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				listaCommesse.add(new Associaz_Risors_Comm_DTO(rs.getInt(1),rs.getString(2)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idRisorsa);
			rs = ps.executeQuery();
			while(rs.next()){
				listaCommesse.add(new Associaz_Risors_Comm_DTO(rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getBoolean(8),rs.getString(9)));
			}
		} catch (SQLException e) {
			// TODO add log4j
			e.printStackTrace();
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
		PreparedStatement ps=null;
		ResultSet rs=null;
		ArrayList<Associaz_Risors_Comm_DTO> listaCommesse = new ArrayList<Associaz_Risors_Comm_DTO>();
		String sql = "select asscommessa.*,commessa.descrizione from tbl_associaz_risor_comm as asscommessa,tbl_commesse as commessa where asscommessa.id_commessa=commessa.id_commessa and asscommessa.id_risorsa=? and commessa.id_tipologia_commessa<>4 and asscommessa.attiva=false";
		try{
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idRisorsa);
			rs = ps.executeQuery();
			while(rs.next()){
				listaCommesse.add(new Associaz_Risors_Comm_DTO(rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getBoolean(8),rs.getString(9)));
			}
		}catch(SQLException e){
			// TODO add log4j
			e.printStackTrace();
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
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql = "select id_cliente,descrizione as ragione_sociale from tbl_associaz_risor_comm as asscommessa, tbl_commesse as commessa where asscommessa.id_commessa = commessa.id_commessa and asscommessa.id_commessa = ?";
		
		String descrizioneCommessa = "";
		
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idCommessa);
			rs = ps.executeQuery();
			if(rs.next()){
				if(rs.getString(1) != null && !rs.getString(1).equals("")){
					descrizioneCommessa = descrizioneCliente(rs.getString(1));
				}else{
					descrizioneCommessa = rs.getString(2);
				}
			}
		}catch(SQLException e){
			// TODO add log4j
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		return descrizioneCommessa;
	}
	
	private String descrizioneCliente(String id_cliente){
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql = "select ragione_sociale from tbl_clienti where id_cliente=?";
		String descrizioneCliente = "";
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, id_cliente);
			rs = ps.executeQuery();
			if(rs.next()){
				descrizioneCliente = rs.getString(1);
			}
		}catch(SQLException e){
			// TODO add log4j
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		return descrizioneCliente;
	}	
}