package it.dipendente.dto;

public class CommessaDTO {

	private int id_commessa;
	private String data;
	private int numeroOre;
	private int id_risorsa;
	private String descrizione;
	private String codiceCommessa;
	private String codiceCliente;
	private String ragioneSociale;
	
	public int getId_commessa() {
		return id_commessa;
	}
	public void setId_commessa(int id_commessa) {
		this.id_commessa = id_commessa;
	}
	public String getRagioneSociale() {
		return ragioneSociale;
	}
	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public int getNumeroOre() {
		return numeroOre;
	}
	public void setNumeroOre(int numeroOre) {
		this.numeroOre = numeroOre;
	}
	public int getId_risorsa() {
		return id_risorsa;
	}
	public void setId_risorsa(int id_risorsa) {
		this.id_risorsa = id_risorsa;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getCodiceCommessa() {
		return codiceCommessa;
	}
	public void setCodiceCommessa(String codiceCommessa) {
		this.codiceCommessa = codiceCommessa;
	}
	public String getCodiceCliente() {
		return codiceCliente;
	}
	public void setCodiceCliente(String codiceCliente) {
		this.codiceCliente = codiceCliente;
	}
	
}
