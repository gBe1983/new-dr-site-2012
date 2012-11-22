package it.dipendente.dto;

public class EventoDTO {

	private int id;
	private String title;
	private String start;
	private String end;
	private boolean allDay;
	private String url;
	private String oraInizio;
	private String oraFine;
	
	public String getOraInizio() {
		return oraInizio;
	}
	public void setOraInizio(String oraInizio) {
		this.oraInizio = oraInizio;
	}
	public String getOraFine() {
		return oraFine;
	}
	public void setOraFine(String oraFine) {
		this.oraFine = oraFine;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public boolean isAllDay() {
		return allDay;
	}
	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
