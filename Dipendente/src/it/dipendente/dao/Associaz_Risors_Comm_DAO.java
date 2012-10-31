package it.dipendente.dao;

import it.dipendente.dto.Associaz_Risors_Comm_DTO;
import it.dipendente.dto.PlanningDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Associaz_Risors_Comm_DAO {

	PreparedStatement ps = null;
	
	/*
	 * tramite questo metodo effettuo l'aggiornamento delle date
	 */
	
	public void aggiornamentoGiorniMensili(PlanningDTO planning, Connection conn){
		
		String sql = "update tbl_planning set num_ore = ?, orario = ?, descr_attivita = ?, note = ? where data = ? and id_associazione = ?";
		
		int esitoInserimentoReport = 0;
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setDouble(1, planning.getNumeroOre());
			ps.setString(2, planning.getOrario());
			ps.setString(3, planning.getDescr_attivita());
			ps.setString(4, planning.getNote());
			ps.setString(5, planning.getData());
			ps.setInt(6, planning.getId_associazione());
			esitoInserimentoReport = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(esitoInserimentoReport == 1){
			System.out.println("modifica effettuata con successo");
		}else{
			System.out.println("modifica non effettuata con successo");
		}
	}
	
	public ArrayList caricamentoCommesseAssociateRisorse(int id_risorsa, Connection conn){
		
		String sql = "select asscommessa.id_commessa,commessa.descrizione from tbl_associaz_risor_comm as asscommessa, tbl_commesse as commessa where asscommessa.id_commessa = commessa.id_commessa and asscommessa.id_risorsa = ? and commessa.id_tipologia_commessa = 4 order by commessa.descrizione";
		
		ArrayList listaCommesse = new ArrayList();
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id_risorsa);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Associaz_Risors_Comm_DTO commessa = new Associaz_Risors_Comm_DTO();
				commessa.setId_commessa(rs.getInt(1));
				commessa.setDescrizioneCommessa(rs.getString(2));
				listaCommesse.add(commessa);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return listaCommesse;
	}
	
	/*
	 * tramite questo metodo effettuo il caricamento delle commesse attive legata alla risorsa
	 * escludendo quelle con tipologia "Altro"
	 */
	
	public ArrayList caricamentoCommesseAttive(int idRisorsa, Connection conn){
		
		ArrayList listaCommesse = new ArrayList();
		
		String sql = "select asscommessa.*,commessa.descrizione from tbl_associaz_risor_comm as asscommessa, tbl_commesse as commessa where asscommessa.id_commessa = commessa.id_commessa and asscommessa.id_risorsa = ? and commessa.id_tipologia_commessa <> 4 and asscommessa.attiva = true";
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, idRisorsa);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Associaz_Risors_Comm_DTO asscommessa = new Associaz_Risors_Comm_DTO();
				asscommessa.setId_associazione(rs.getInt(1));
				asscommessa.setId_risorsa(rs.getInt(2));
				asscommessa.setId_commessa(rs.getInt(3));
				asscommessa.setAttiva(rs.getBoolean(8));
				asscommessa.setDescrizioneCommessa(rs.getString(9));
				listaCommesse.add(asscommessa);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return listaCommesse;
	}
	
	/*
	 * tramite questo metodo effettuo il caricamento delle commesse attive legata alla risorsa
	 * escludendo quelle con tipologia "Altro"
	 */
	
	public ArrayList caricamentoCommesseNonAttive(int idRisorsa, Connection conn){
		
		ArrayList listaCommesse = new ArrayList();
		
		String sql = "select asscommessa.*,commessa.descrizione from tbl_associaz_risor_comm as asscommessa, tbl_commesse as commessa where asscommessa.id_commessa = commessa.id_commessa and asscommessa.id_risorsa = ? and commessa.id_tipologia_commessa <> 4 and asscommessa.attiva = false";
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, idRisorsa);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Associaz_Risors_Comm_DTO asscommessa = new Associaz_Risors_Comm_DTO();
				asscommessa.setId_associazione(rs.getInt(1));
				asscommessa.setId_risorsa(rs.getInt(2));
				asscommessa.setId_commessa(rs.getInt(3));
				asscommessa.setAttiva(rs.getBoolean(8));
				asscommessa.setDescrizioneCommessa(rs.getString(9));
				listaCommesse.add(asscommessa);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return listaCommesse;
	}
	
	
	/*
	 * tramite questo metodo carico la descrizione 
	 */
	
	public String caricamentoDescrizioneCommessa(int idCommessa,Connection conn){
		
		String sql = "select id_cliente,descrizione as ragione_sociale from tbl_associaz_risor_comm as asscommessa, tbl_commesse as commessa where asscommessa.id_commessa = commessa.id_commessa and asscommessa.id_commessa = ?";
		
		String descrizioneCommessa = "";
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, idCommessa);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				if(rs.getString(1) != null && !rs.getString(1).equals("")){
					descrizioneCommessa = descrizioneCliente(rs.getString(1), conn);
				}else{
					descrizioneCommessa = rs.getString(2);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return descrizioneCommessa;
	}
	
	private String descrizioneCliente(String id_cliente, Connection conn){
		
		String sql = "select ragione_sociale from tbl_clienti where id_cliente = ?";
		
		String descrizioneCliente = "";
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, id_cliente);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				descrizioneCliente = rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return descrizioneCliente;
	}
	
	
}
