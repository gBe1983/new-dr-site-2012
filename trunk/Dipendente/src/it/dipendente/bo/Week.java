package it.dipendente.bo;

import it.dipendente.util.MyLogger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Week {
	private MyLogger log;
	private int weekOfYear;
	private List<Day>days;

	public Week(int weekOfYear){
		log =new MyLogger(this.getClass());
		final String metodo="costruttore";
		log.start(metodo);
		this.weekOfYear=weekOfYear;
		days= new ArrayList<Day>();
		days.add(new Day(Calendar.MONDAY,"Luned�"));
		days.add(new Day(Calendar.TUESDAY,"Marted�"));
		days.add(new Day(Calendar.WEDNESDAY,"Mercoled�"));
		days.add(new Day(Calendar.THURSDAY,"Gioved�"));
		days.add(new Day(Calendar.FRIDAY,"Venerd�"));
		days.add(new Day(Calendar.SATURDAY,"Sabato"));
		days.add(new Day(Calendar.SUNDAY,"Domenica"));
		log.end(metodo);
	}

	/**
	 * @return the weekOfYear
	 */
	public int getWeekOfYear() {
		return weekOfYear;
	}

	/**
	 * @return the days
	 */
	public List<Day> getDays() {
		return days;
	}
}