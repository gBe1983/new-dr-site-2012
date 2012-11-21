package it.dipendente.dao;

import it.dipendente.dto.RisorsaDTO;
import it.util.log.MyLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RisorseDAO extends BaseDao{
	private MyLogger log;

	public RisorseDAO(Connection connessione) {
		super(connessione);
		log=new MyLogger(this.getClass());
	}

	public List<RisorsaDTO> getRisorse(){
		final String metodo="getRisorse";
		log.start(metodo);
		PreparedStatement ps=null;
		ResultSet rs=null;
		StringBuilder sql = new StringBuilder("SELECT ");
		sql	.append("id_risorsa,cognome,nome ")
				.append("FROM tbl_risorse ")
				.append("ORDER BY cognome");
		log.debug(metodo,"sql:"+sql.toString());
		List<RisorsaDTO>ris=new ArrayList<RisorsaDTO>();
		try {
			ps = connessione.prepareStatement(sql.toString());
			rs = ps.executeQuery();
			while (rs.next()){
				ris.add(new RisorsaDTO(rs.getInt("id_risorsa"),rs.getString("cognome"),rs.getString("nome")));
			}
		} catch (SQLException e) {
			log.error(metodo, "select tbl_planning,tbl_associaz_risor_comm,tbl_commesse for risorsa:", e);
		}finally{
			close(ps,rs);
			log.end(metodo);
		}
		return ris;
	}
}