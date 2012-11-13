package it.dipendente.connessione;

import it.exception.config.Config;
import it.util.config.MyProperties;
import it.util.log.MyLogger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connessione {
	private MyLogger log;
	private Connection connection;
	private String url = null;
	private String dbName = null;
	private String driver = null;
	private String userName = null; 
	private String password = null;

	public Connessione(MyProperties properties) throws Config {
		log =new MyLogger(this.getClass());
		final String metodo="costruttore";
		log.start(metodo);

		try {
			url =				properties.getPropertyValue("url");
			dbName =		properties.getPropertyValue("dbName");
			driver =			properties.getPropertyValue("driver");
			userName =	properties.getPropertyValue("userName");
			password =	properties.getPropertyValue("password");
		} catch (Config e) {
			log.fatal(metodo,"tentativo recupero dati di configurazione da properties",e);
			throw e;
		}

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			log.fatal(metodo, "driver non caricati", e);
		}
		try {
			connection = DriverManager.getConnection(url+dbName,userName,password);
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