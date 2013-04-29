package it.dipendente.dto;

import java.sql.Date;
import java.util.Calendar;

public class PlanningDTO {

	private int id_planning;
	private Calendar data;
	private double numeroOre;
	private double straordinari;
	private String descr_attivita;
	private String orario;
	private int id_associazione;
	private String note;
	private boolean attivo;
	private boolean commessaAttiva;
	private String codiceCommessa;
	private double assenze;
	private boolean ferie;
	private boolean permessi;
	private boolean mutua;
	private boolean permessiNonRetribuiti;
	
	private int numeroSettimana;
	
	public PlanningDTO() {}
	public PlanningDTO(int id_planning,
						Date data,
						double numeroOre,
						double straordinari,
						String orario,
						String note,
						int id_associazione,
						String descr_attivita,
						String codiceCommessa,
						int numeroSettimana,
						double assenze,
						boolean ferie,
						boolean permessi,
						boolean mutua,
						boolean permessiNonRetribuiti){
		this.id_planning=id_planning;
		this.data=Calendar.getInstance();
		this.data.setTime(data);
		this.numeroOre=numeroOre;
		this.straordinari=straordinari;
		this.orario=orario;
		this.note=note;
		this.id_associazione=id_associazione;
		this.descr_attivita=descr_attivita;
		this.codiceCommessa=codiceCommessa;
		this.numeroSettimana=numeroSettimana;
		this.assenze = assenze;
		this.ferie = ferie;
		this.permessi = permessi;
		this.mutua = mutua;
		this.permessiNonRetribuiti = permessiNonRetribuiti;
	}

	public double getAssenze() {
		return assenze;
	}
	public void setAssenze(double assenze) {
		this.assenze = assenze;
	}
	public boolean isFerie() {
		return ferie;
	}
	public void setFerie(boolean ferie) {
		this.ferie = ferie;
	}
	public boolean isPermessi() {
		return permessi;
	}
	public void setPermessi(boolean permessi) {
		this.permessi = permessi;
	}
	public boolean isMutua() {
		return mutua;
	}
	public void setMutua(boolean mutua) {
		this.mutua = mutua;
	}
	public boolean isPermessiNonRetribuiti() {
		return permessiNonRetribuiti;
	}
	public void setPermessiNonRetribuiti(boolean permessiNonRetribuiti) {
		this.permessiNonRetribuiti = permessiNonRetribuiti;
	}
	public void setCodiceCommessa(String codiceCommessa) {
		this.codiceCommessa = codiceCommessa;
	}
	public int getNumeroSettimana() {
		return numeroSettimana;
	}
	public void setNumeroSettimana(int numeroSettimana) {
		this.numeroSettimana = numeroSettimana;
	}
	/**
	 * @return the id_planning
	 */
	public int getId_planning() {
		return id_planning;
	}
	/**
	 * @param id_planning the id_planning to set
	 */
	public void setId_planning(int id_planning) {
		this.id_planning = id_planning;
	}
	/**
	 * @return the data
	 */
	public Calendar getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(Calendar data) {
		this.data = data;
	}
	/**
	 * @return the numeroOre
	 */
	public double getNumeroOre() {
		return numeroOre;
	}
	/**
	 * @param numeroOre the numeroOre to set
	 */
	public void setNumeroOre(double numeroOre) {
		this.numeroOre = numeroOre;
	}
	/**
	 * @return the straordinari
	 */
	public double getStraordinari() {
		return straordinari;
	}
	/**
	 * @param straordinari the straordinari to set
	 */
	public void setStraordinari(double straordinari) {
		this.straordinari = straordinari;
	}
	/**
	 * @return the descr_attivita
	 */
	public String getDescr_attivita() {
		return descr_attivita;
	}
	/**
	 * @param descr_attivita the descr_attivita to set
	 */
	public void setDescr_attivita(String descr_attivita) {
		this.descr_attivita = descr_attivita;
	}
	/**
	 * @return the orario
	 */
	public String getOrario() {
		return orario;
	}
	/**
	 * @param orario the orario to set
	 */
	public void setOrario(String orario) {
		this.orario = orario;
	}
	/**
	 * @return the id_associazione
	 */
	public int getId_associazione() {
		return id_associazione;
	}
	/**
	 * @param id_associazione the id_associazione to set
	 */
	public void setId_associazione(int id_associazione) {
		this.id_associazione = id_associazione;
	}
	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}
	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}
	/**
	 * @return the attivo
	 */
	public boolean isAttivo() {
		return attivo;
	}
	/**
	 * @param attivo the attivo to set
	 */
	public void setAttivo(boolean attivo) {
		this.attivo = attivo;
	}
	/**
	 * @return the commessaAttiva
	 */
	public boolean isCommessaAttiva() {
		return commessaAttiva;
	}
	/**
	 * @param commessaAttiva the commessaAttiva to set
	 */
	public void setCommessaAttiva(boolean commessaAttiva) {
		this.commessaAttiva = commessaAttiva;
	}
	/**
	 * @return the codiceCommessa
	 */
	public String getCodiceCommessa() {
		return codiceCommessa;
	}

	public boolean isInterna(){
		return codiceCommessa.toUpperCase().contains("IN");
	}
	public String toString(){
		return new StringBuilder("id_associazione[")
						.append(id_associazione)
						.append("]data[")
						.append(data)
						.append("]numeroOre[")
						.append(numeroOre)
						.append("]").toString();
	}
}
