package it.dipendente.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseDao {
	protected Connection connessione=null;

	public BaseDao(Connection connessione) {
		super();
		this.connessione = connessione;
	}
	
	public void close(PreparedStatement ps,ResultSet rs){
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				//TODO INSERIRE LOG4J
			}
		}
		if(ps!=null){
			try {
				ps.close();
			} catch (SQLException e) {
				//TODO INSERIRE LOG4J
			}
		}
	}

	public void close(PreparedStatement ps){
		close(ps,null);
	}
}
