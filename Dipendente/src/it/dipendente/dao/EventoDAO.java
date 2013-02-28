package it.dipendente.dao;

import it.dipendente.dto.EventoDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class EventoDAO extends BaseDao{

	public EventoDAO(Connection connessione) {
		super(connessione);
	}

	PreparedStatement ps = null;
	
	public void inserisciEvento(EventoDTO evento, int idRisorsa){
		
		String sql = "insert into tbl_eventi(title,start,end,id_risorsa) values (?,?,?,?)";
		
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, evento.getTitle());
			ps.setString(2, evento.getStart());
			ps.setString(3, evento.getEnd());
			ps.setInt(4, idRisorsa);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void modificaEvento(EventoDTO evento, int idRisorsa){
		
		String sql = "update tbl_eventi set title = ?,start = ?,end = ?, id_risorsa = ? where id = ?";
		
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, evento.getTitle());
			ps.setString(2, evento.getStart());
			ps.setString(3, evento.getEnd());
			ps.setInt(4, idRisorsa);
			ps.setInt(5, evento.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public ArrayList caricamentoEventi(int id_risorsa){
		
		SimpleDateFormat formattazioneSql = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		//String eventi = "";
		ArrayList eventi = new ArrayList();
		
		String sql = "select * from tbl_eventi where visibile = true";
		
		try {
			ps = connessione.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				EventoDTO evento = new EventoDTO();
				evento.setId(rs.getInt(1));
				evento.setTitle(rs.getString(2));
				evento.setStart(formattazioneSql.format(formattazioneSql.parse(rs.getString(3))));
				evento.setEnd(formattazioneSql.format(formattazioneSql.parse(rs.getString(4))));
				evento.setNominativo(caricamentoNominativo(rs.getInt(7)));
				if(id_risorsa == rs.getInt(7)){
					evento.setUrl("./GestioneCalendarioEventi?azione=visualizzaEvento&evento="+rs.getInt(1));
				}
				evento.setAllDay(rs.getBoolean(5));
				evento.setClassName("evento");
				
				eventi.add(evento);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return eventi;
	}
	
	public EventoDTO caricamentoEvento(int idEvento){
		
		SimpleDateFormat formattazioneSql = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat formattazioneWeb = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat formattazioneOra = new SimpleDateFormat("HH:mm");
		
		//String eventi = "";
		EventoDTO evento = null;
		
		String sql = "select * from tbl_eventi where id = ? and visibile = true";
		
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idEvento);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				evento = new EventoDTO();
				evento.setId(rs.getInt(1));
				evento.setTitle(rs.getString(2));
				evento.setStart(formattazioneWeb.format(formattazioneSql.parse(rs.getString(3))));
				evento.setEnd(formattazioneWeb.format(formattazioneSql.parse(rs.getString(4))));
				evento.setOraInizio(formattazioneOra.format(formattazioneSql.parse(rs.getString(3))));
				evento.setOraFine(formattazioneOra.format(formattazioneSql.parse(rs.getString(4))));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return evento;
	}
	
	public String caricamentoNominativo(int id_risorsa){
		
		String nominativo = "";
		
		String sql = "select cognome,nome from tbl_risorse where id_risorsa = ?";
		
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, id_risorsa);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				nominativo = rs.getString(1) + " " + rs.getString(2);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return nominativo;
	}
	
	public void eliminaEvento(int id_evento){
		
		String sql = "update tbl_eventi set visibile = false where id = ?";
		
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, id_evento);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
}
