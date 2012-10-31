package it.dipendente.dto;

public class Associaz_Risors_Comm_DTO {

	private int id_associazione;
	private int id_risorsa;
	private int id_commessa;
	private String data_inizio;
	private String data_fine;
	private String importo;
	private String al;
	private boolean attiva;
	private String descrizioneCommessa;
	
	
	public String getDescrizioneCommessa() {
		return descrizioneCommessa;
	}
	public void setDescrizioneCommessa(String descrizioneCommessa) {
		this.descrizioneCommessa = descrizioneCommessa;
	}
	private String descrizioneCliente;
	
	public String getDescrizioneCliente() {
		return descrizioneCliente;
	}
	public void setDescrizioneCliente(String descrizioneCliente) {
		this.descrizioneCliente = descrizioneCliente;
	}
	public int getId_associazione() {
		return id_associazione;
	}
	public void setId_associazione(int id_associazione) {
		this.id_associazione = id_associazione;
	}
	public int getId_risorsa() {
		return id_risorsa;
	}
	public void setId_risorsa(int id_risorsa) {
		this.id_risorsa = id_risorsa;
	}
	public int getId_commessa() {
		return id_commessa;
	}
	public void setId_commessa(int id_commessa) {
		this.id_commessa = id_commessa;
	}
	public String getData_inizio() {
		return data_inizio;
	}
	public void setData_inizio(String data_inizio) {
		this.data_inizio = data_inizio;
	}
	public String getData_fine() {
		return data_fine;
	}
	public void setData_fine(String data_fine) {
		this.data_fine = data_fine;
	}
	public String getImporto() {
		return importo;
	}
	public void setImporto(String importo) {
		this.importo = importo;
	}
	public String getAl() {
		return al;
	}
	public void setAl(String al) {
		this.al = al;
	}
	public boolean isAttiva() {
		return attiva;
	}
	public void setAttiva(boolean attiva) {
		this.attiva = attiva;
	}
}
