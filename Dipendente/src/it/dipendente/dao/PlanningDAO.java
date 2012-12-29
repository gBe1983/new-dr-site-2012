package it.dipendente.dao;

import it.dipendente.bo.Month;
import it.dipendente.dto.PlanningDTO;
import it.util.log.MyLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PlanningDAO extends BaseDao{
	private MyLogger log;

	public PlanningDAO(Connection connessione) {
		super(connessione);
		log=new MyLogger(this.getClass());
	}

	/**
	 * tramite questo metodo effettuo l'aggiornamento del planning
	 * @param planning
	 */
	public int aggiornamentoPlanning(PlanningDTO planning){
		final String metodo="aggiornamentoPlanning";
		log.start(metodo);
		String sql = "UPDATE tbl_planning SET num_ore=?,straordinari=?,orario=?,note=? WHERE id_planning=?";
		log.debug(metodo,"sql:"+sql);
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setDouble(1, planning.getNumeroOre());
			ps.setDouble(2, planning.getStraordinari());
			ps.setString(3, planning.getOrario());
			ps.setString(4, planning.getNote());
			ps.setInt(5, planning.getId_planning());
			return ps.executeUpdate();
		} catch (SQLException e) {
			log.error(metodo, "update tbl_planning for planning:"+planning.getId_planning(), e);
		}finally{
			close(ps);
			log.end(metodo);
		}
		return 0;
	}

	public Month getGiornate(int id_risorsa, Calendar day){
		final String metodo="getGiornate";
		log.start(metodo);
		Month m = new Month(day);
		String now = new SimpleDateFormat("yyyy-MM-%").format(day.getTime());
		PreparedStatement ps=null;
		ResultSet rs=null;
		StringBuilder sql = new StringBuilder("select planning.id_planning,");
		sql	.append("planning.data,planning.num_ore,planning.straordinari,planning.orario,planning.note,")
				.append("asscommessa.id_associazione,")
				.append("commessa.descrizione,commessa.codice_commessa")
				.append(" from tbl_planning planning,tbl_associaz_risor_comm asscommessa,tbl_commesse commessa")
				.append(" where planning.id_associazione=asscommessa.id_associazione")
				.append(" and asscommessa.id_commessa=commessa.id_commessa")
				.append(" and planning.data like? and asscommessa.id_risorsa=?")
				.append(" order by data");
		log.debug(metodo,"sql:"+sql.toString());
		try {
			ps = connessione.prepareStatement(sql.toString());
			ps.setString(1,now);
			log.debug(metodo,"now:"+now);
			ps.setInt(2,id_risorsa);
			log.debug(metodo,"id_risorsa:"+id_risorsa);
			rs = ps.executeQuery();
			while (rs.next()){
				m.addPlanningDTO(
					new PlanningDTO(rs.getInt("id_planning"),
									rs.getDate("data"),
									rs.getDouble("num_ore"),
									rs.getDouble("straordinari"),
									rs.getString("orario"),
									rs.getString("note"),
									rs.getInt("id_associazione"),
									rs.getString("descrizione"),
									rs.getString("codice_commessa")));
			}
		} catch (SQLException e) {
			log.error(metodo, "select tbl_planning,tbl_associaz_risor_comm,tbl_commesse for risorsa:"+id_risorsa, e);
		}finally{
			close(ps,rs);
			log.end(metodo);
		}
		return m;
	}
}