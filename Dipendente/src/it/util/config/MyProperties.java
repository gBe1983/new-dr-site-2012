/**
 * 
 */
package it.util.config;

import it.exception.config.Config;
import it.exception.config.ConfigFileCreateEx;
import it.exception.config.ConfigFileNotFound;
import it.exception.config.ConfigFileNotValid;
import it.util.log.MyLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/**
 * @author Dr
 *
 */
public class MyProperties {
	private MyLogger logger;
	private Properties properties=null;
	private FileInputStream inputStream=null;
	private FileOutputStream outputStream=null;
	private String pathFile;

	/**
	 * 
	 */
	public MyProperties(String pathFile) {
		logger=new MyLogger(this.getClass());
		final String metodo="costruttore";
		logger.start(metodo);
		this.pathFile=pathFile;
		logger.end(metodo);
	}

	public String getPropertyValue(String key) throws Config {
		final String metodo="getPropertyValue";
		logger.start(metodo);
		if(properties==null && inputStream==null){
			try {
				inputStream=new FileInputStream(pathFile);
			} catch (FileNotFoundException e) {
				logger.fatal(metodo, "tentativo connessione file di properties", e);
				throw new ConfigFileNotFound("file di configurazione non trovato");
			}
		}
		if(properties==null){
			properties=new Properties();
			if(pathFile.toUpperCase().endsWith(".XML")){
				try {
					properties.loadFromXML(inputStream);
				} catch (InvalidPropertiesFormatException e) {
					logger.fatal(metodo, "file corrotto", e);
					throw new ConfigFileNotValid( "file corrotto");
				} catch (IOException e) {
					logger.fatal(metodo, "impossibile leggere il file di properties.xml", e);
					throw new ConfigFileNotValid( "impossibile leggere il file di properties.xml");
				}
			}else if(pathFile.toUpperCase().endsWith(".PROPERTIES")){
				try {
					properties.load(inputStream);
				} catch (IOException e) {
					logger.fatal(metodo, "impossibile leggere il file di properties.properties", e);
					throw new ConfigFileNotValid( "impossibile leggere il file di properties.properties");
				}
			}
			try {
				inputStream.close();
			} catch (IOException e) {
				logger.warn(metodo, "fallito tentativo chiusura configInputStream", e);
			}
		}
		logger.end(metodo);
		return properties.getProperty(key);
	}

	public void writeConfigFile() throws Config{
		final String metodo="writeConfigFile";
		logger.start(metodo);
		if(outputStream==null){
			try {
				outputStream= new FileOutputStream(pathFile);
			} catch (FileNotFoundException e) {
				logger.fatal(metodo, "impossibile esportare il file di properties", e);
				throw new ConfigFileCreateEx("impossibile esportare il file di properties");
			}
		}
		File f= new File(pathFile);
		if(pathFile.toUpperCase().endsWith(".XML")){
			try {
				properties.storeToXML(outputStream, f.getName());
			} catch (IOException e) {
				logger.fatal(metodo, "fallita costruzione file di properties.xml", e);
				throw new ConfigFileCreateEx("fallita costruzione file di properties.xml");
			}
		}else if(pathFile.toUpperCase().endsWith(".PROPERTIES")){
			try {
				properties.store(outputStream, f.getName());
			} catch (IOException e) {
				logger.fatal(metodo, "fallita costruzione file di properties.properties", e);
				throw new ConfigFileCreateEx("fallita costruzione file di properties.properties");
			}
		}
	}

	public void setProperty(String key, String value){
		if(properties==null){
			properties=new Properties();
		}
		properties.setProperty(key, value);
	}
}
