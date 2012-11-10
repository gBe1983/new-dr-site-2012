package it.dipendente.connessione;

import it.dipendente.util.MyLogger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connessione {
	private MyLogger log;
	private Connection connection;

	public Connessione() {
		log =new MyLogger(this.getClass());
		final String metodo="costruttore";
		log.start(metodo);
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			log.fatal(metodo, "driver non caricati", e);
		}
		try {
			connection = DriverManager.getConnection("jdbc:mysql://151.1.159.238:3306/drcon860_curriculum","drcon860_DiErre","DiErre2012");
//			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/drcon860_curriculum","root","root");
		} catch (SQLException e) {
			log.fatal(metodo, "connessione fallita", e);
		}
	}

	/**
	 * distruttore
	 */
	protected void finalize(){
		closeConnection();
	}

	/**
	 *  close the Connection
	 */
	@Deprecated
	public void closeConnection(){
		final String metodo="closeConnection";
		log.start(metodo);
		if (connection!=null) {
			try {
				connection.close();
			} catch (SQLException e) {
				log.warn(metodo, "tentativo chiusura connessione db", e);
			}
		}
		log.end(metodo);
	}

	/**
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}
}