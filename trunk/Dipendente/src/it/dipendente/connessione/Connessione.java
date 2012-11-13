package it.dipendente.connessione;

import it.util.log.MyLogger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;

public class Connessione {
	private MyLogger log;
	private Connection connection;

	public Connessione(ServletContext servletContext) {
		log =new MyLogger(this.getClass());
		final String metodo="costruttore";
		log.start(metodo);
		try {
			Class.forName(servletContext.getInitParameter("DB.driver"));
		} catch (ClassNotFoundException e) {
			log.fatal(metodo, "driver non caricati", e);
		}
		try {
			connection = DriverManager.getConnection(
				servletContext.getInitParameter("DB.url"),
				servletContext.getInitParameter("DB.userName"),
				servletContext.getInitParameter("DB.password"));
		} catch (SQLException e) {
			log.fatal(metodo, "connessione fallita", e);
		}
		log.end(metodo);
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