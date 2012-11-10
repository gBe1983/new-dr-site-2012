package it.dipendente.bo;

import it.dipendente.util.MyLogger;

import java.util.Calendar;

public class Day {
	private MyLogger log;
	private int dayOfWeek;
	private String dayLabel;
	private Calendar day;

	public Day(int dayOfWeek, String dayLabel) {
		log =new MyLogger(this.getClass());
		final String metodo="costruttore";
		log.start(metodo);
		this.dayOfWeek = dayOfWeek;
		this.dayLabel = dayLabel;
		log.end(metodo);
	}

	public int getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public String getDayLabel() {
		return dayLabel;
	}
	public void setDayLabel(String dayLabel) {
		this.dayLabel = dayLabel;
	}
	public Calendar getDay() {
		return day;
	}
	public void setDay(Calendar d) {
		this.day = d;
	}

	public String getCssStyle() {
		return (day!=null)?((day.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||day.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)?"holiday":"workDay"):"disabledDay";
	}
}