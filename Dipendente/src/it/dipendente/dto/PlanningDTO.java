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

	public PlanningDTO() {}
	public PlanningDTO(int id_planning,
						Date data,
						double numeroOre,
						double straordinari,
						String orario,
						String note,
						int id_associazione,
						String descr_attivita
						){
		this.id_planning=id_planning;
		this.data=Calendar.getInstance();
		this.data.setTime(data);
		this.numeroOre=numeroOre;
		this.straordinari=straordinari;
		this.orario=orario;
		this.note=note;
		this.id_associazione=id_associazione;
		this.descr_attivita=descr_attivita;
	}
	public int getId_planning() {
		return id_planning;
	}

	public void setId_planning(int id_planning) {
		this.id_planning = id_planning;
	}

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
	}

	public double getNumeroOre() {
		return numeroOre;
	}

	public void setNumeroOre(double numeroOre) {
		this.numeroOre = numeroOre;
	}

	public double getStraordinari() {
		return straordinari;
	}

	public void setStraordinari(double straordinari) {
		this.straordinari = straordinari;
	}

	public String getDescr_attivita() {
		return descr_attivita;
	}

	public void setDescr_attivita(String descr_attivita) {
		this.descr_attivita = descr_attivita;
	}

	public String getOrario() {
		return orario;
	}

	public void setOrario(String orario) {
		this.orario = orario;
	}

	public int getId_associazione() {
		return id_associazione;
	}

	public void setId_associazione(int id_associazione) {
		this.id_associazione = id_associazione;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public boolean isAttivo() {
		return attivo;
	}

	public void setAttivo(boolean attivo) {
		this.attivo = attivo;
	}

	public boolean isCommessaAttiva() {
		return commessaAttiva;
	}

	public void setCommessaAttiva(boolean commessaAttiva) {
		this.commessaAttiva = commessaAttiva;
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
