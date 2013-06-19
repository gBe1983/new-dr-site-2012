package it.dipendente.connessione;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

public class Connessione {
	private Logger log;
	private Connection connection;
	private String url;
	private String userName;
	private String password;

	public Connessione(ServletContext servletContext) {
		log = Logger.getLogger(Connessione.class);
		
		log.info("metodo: Connessione - classe: Connessione");
		try {
			Class.forName(servletContext.getInitParameter("DB.driver"));
		} catch (ClassNotFoundException e) {
			log.error("metodo: Connessione - driver non caricati - " + e);
		}
		url=servletContext.getInitParameter("DB.url");
		userName=servletContext.getInitParameter("DB.userName");
		password=servletContext.getInitParameter("DB.password");
		
		log.info("url: " + url);
		log.info("username: " + userName);
		log.info("password: " + password);
		
		try {
			connection = DriverManager.getConnection(url,userName,password);
		} catch (SQLException e) {
			log.error("metodo: Connessione - connessione fallita - " + e);
		}
		log.info("Endmetodo: Connessione - classe Connessione");
	}


	/**
	 *  close the Connection
	 */
	public void closeConnection(){
		
		log.info("metodo: closeConnection - classe Connessione");
		if (connection!=null) {
			try {
				connection.close();
				log.info("connessione chiusa");
			} catch (SQLException e) {
				log.error("metodo: closeConnection - tentativo chiusura connessione db - " + e);
			}
		}
		log.info("Endmetodo: closeConnection - classe Connessione");
	}

	/**
	 * @return the connection
	 */
	public Connection getConnection() {
		
		log.info("metodo: getConnection - classe Connessione");
		if(connection!=null){
			try {
				if(connection.isClosed()){
					log.info("metodo: closeConnection - la connessione è chiusa");
					try {
						connection = DriverManager.getConnection(url,userName,password);
					} catch (SQLException e) {
						log.error("metodo: closeConnection - connessione fallita - " + e);
					}
				}
				if(!connection.isValid(60)){
					log.info("metodo: closeConnection - la connessione non è valida");
					try {
						connection = DriverManager.getConnection(url,userName,password);
					} catch (SQLException e) {
						log.error("metodo: closeConnection - connessione fallita - " + e);
					}
				}
			} catch (SQLException e) {
				log.error("metodo: closeConnection - verifica consistenza connessione - " + e);
			}
		}else{
			log.warn("metodo: closeConnection - connessione null");
			try {
				connection = DriverManager.getConnection(url,userName,password);
			} catch (SQLException e) {
				log.error("metodo: closeConnection - connessione fallita - " + e);
			}
		}
		log.info("Endmetodo: closeConnection - classe Connessione");
		return connection;
	}
}