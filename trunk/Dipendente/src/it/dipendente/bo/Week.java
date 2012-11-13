package it.dipendente.bo;

import it.dipendente.dto.PlanningDTO;
import it.util.log.MyLogger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Week {
	private MyLogger log;
	private int weekOfYear;
	private List<Day>days;

	private HashMap<String,List<PlanningDTO>>commesse;

	public Week(int weekOfYear){
		log =new MyLogger(this.getClass());
		final String metodo="costruttore";
		log.start(metodo);
		this.weekOfYear=weekOfYear;
		days= new ArrayList<Day>();
		days.add(new Day(Calendar.MONDAY,"Lunedì"));
		days.add(new Day(Calendar.TUESDAY,"Martedì"));
		days.add(new Day(Calendar.WEDNESDAY,"Mercoledì"));
		days.add(new Day(Calendar.THURSDAY,"Giovedì"));
		days.add(new Day(Calendar.FRIDAY,"Venerdì"));
		days.add(new Day(Calendar.SATURDAY,"Sabato"));
		days.add(new Day(Calendar.SUNDAY,"Domenica"));
		commesse=new HashMap<String,List<PlanningDTO>>();
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

	/**
	 * @return the commesse
	 */
	public HashMap<String, List<PlanningDTO>> getCommesse() {
		return commesse;
	}

	/**
	 * 1.se la settimana non contiene la commessa passata, viene creata la lista associata.
	 * 2.aggiungo la commessa nella lista associata
	 * @param p
	 */
	public void addPlanningDTO(PlanningDTO p){
		if(!commesse.containsKey(p.getDescr_attivita())){
			commesse.put(p.getDescr_attivita(),new ArrayList<PlanningDTO>());
		}
		commesse.get(p.getDescr_attivita()).add(p);
	}
}