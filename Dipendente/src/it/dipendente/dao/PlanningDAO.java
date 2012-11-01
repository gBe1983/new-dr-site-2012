package it.dipendente.dao;

import it.dipendente.dto.PlanningDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PlanningDAO extends BaseDao{
	private SimpleDateFormat formatDate = null;

	public PlanningDAO(Connection connessione) {
		super(connessione);
		formatDate = new SimpleDateFormat("dd-MM-yyyy");
		// TODO add log4j
	}

	public ArrayList caricamentoGiornate(String mese,int anno,int id_associazione){
		PreparedStatement ps=null;
		ResultSet rs=null;
		String sql = "select planning.*,asscommessa.attiva from tbl_planning as planning, tbl_associaz_risor_comm asscommessa where planning.id_associazione = asscommessa.id_associazione and planning.data like ? and planning.id_associazione = ?";
		ArrayList caricamentoGiornate = new ArrayList();
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, anno+"-"+mese+"-%");
			ps.setInt(2, id_associazione);
			rs = ps.executeQuery();
			while (rs.next()){
				PlanningDTO planning = new PlanningDTO();
				planning.setId_associazione(rs.getInt(1));
				planning.setData(formatDate.format(rs.getDate(2)));
				planning.setNumeroOre(rs.getDouble(3));
				planning.setStraordinari(rs.getInt(4));
				planning.setDescr_attivita(rs.getString(5));
				planning.setOrario(rs.getString(6));
				planning.setId_associazione(rs.getInt(7));
				planning.setNote(rs.getString(8));
				planning.setAttivo(rs.getBoolean(9));
				planning.setGiorno(calculateDay(formatDate.parse(formatDate.format(rs.getDate(2)))));
				planning.setCommessaAttiva(rs.getBoolean(10));
				caricamentoGiornate.add(planning);
			}
		} catch (SQLException e) {
			// TODO  add log4j
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO  add log4j
			e.printStackTrace();
		}finally{
			close(ps,rs);
		}
		return caricamentoGiornate;
	}

	private static String calculateDay(java.util.Date data) {
		String res = "nessuna";
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		int day = c.get(Calendar.DAY_OF_WEEK);
		if (day == Calendar.MONDAY)
			res = "lun";
		if (day == Calendar.TUESDAY)
			res = "mar";
		if (day == Calendar.WEDNESDAY)
			res = "mer";
		if (day == Calendar.THURSDAY)
			res = "gio";
		if (day == Calendar.FRIDAY)
			res = "ven";
		if (day == Calendar.SATURDAY)
			res = "sab";
		if (day == Calendar.SUNDAY)
			res = "dom";
		return res;
	}
}