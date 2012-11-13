package it.dipendente.bo;

import it.util.log.MyLogger;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Day {
	private MyLogger log=null;
	private int dayOfWeek;
	private String dayLabel=null;
	private Calendar day=null;

	private static SimpleDateFormat sdf=null;

	public Day(int dayOfWeek, String dayLabel) {
		log =new MyLogger(this.getClass());
		final String metodo="costruttore";
		log.start(metodo);
		this.dayOfWeek = dayOfWeek;
		this.dayLabel = dayLabel;
		if(sdf!=null){
			sdf=new SimpleDateFormat("_YYYY_MM_DD");
		}
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

	public String getDayKey(){
		return sdf.format(day.getTime());
	}

	public String getCssStyle() {
		return (day!=null)?((day.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||day.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)?"holiday":"workDay"):"disabledDay";
	}
}