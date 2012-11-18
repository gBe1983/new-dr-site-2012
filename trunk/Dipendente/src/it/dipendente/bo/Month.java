package it.dipendente.bo;

import it.dipendente.dto.PlanningDTO;
import it.util.log.MyLogger;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Month implements Serializable{
	private static final long serialVersionUID = -3196180487587957038L;
	private MyLogger log;
	private int year;
	private int month;
	private String monthLabel;
	private int weeksCnt=0;
	private List<Week>weeks;

	public Month(Calendar day){
		log =new MyLogger(this.getClass());
		final String metodo="costruttore";
		log.start(metodo);
		year = day.get(Calendar.YEAR);
		month = day.get(Calendar.MONTH);
		monthLabel=new SimpleDateFormat(" MMMMMMMMM YYYY",Locale.ITALIAN).format(day.getTime());
		while(day.get(Calendar.DAY_OF_MONTH)!=1){
			day.add(Calendar.DAY_OF_MONTH,-1);
		}
		weeks = new ArrayList<Week>();
		int cnt=0;
		int actualMaximum=day.getActualMaximum(Calendar.DAY_OF_MONTH);
		boolean search;
		for(int i=0;i<actualMaximum;i++){
			search=true;
			if(weeksCnt==0 || cnt==7){
				weeksCnt++;
				getWeeks().add(new Week(day.get(Calendar.WEEK_OF_YEAR)));
				cnt=0;
			}
			while(search){
				if(getWeeks().get(weeksCnt-1).getDays().get(cnt).getDayOfWeek()==day.get(Calendar.DAY_OF_WEEK)){
					getWeeks().get(weeksCnt-1).getDays().get(cnt).setDay((Calendar)day.clone());
					search=false;
				}
				cnt++;
			}
			day.add(Calendar.DAY_OF_MONTH,1);
		}
		log.end(metodo);
	}

	/**
	 * @return the monthLabel
	 */
	public String getMonthLabel() {
		return monthLabel;
	}

	/**
	 * @return the weeks
	 */
	public List<Week> getWeeks() {
		return weeks;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * cerco la settimana a cui aggiungere la commessa, una volta trovata la gli passo la commessa da aggiungere
	 * @param p
	 */
	public void addPlanningDTO(PlanningDTO p){
		final String metodo="addPlanningDTO";
		log.start(metodo);
		for (int i=0;i<weeks.size();i++) {
			if(p.getData().get(Calendar.WEEK_OF_YEAR)==weeks.get(i).getWeekOfYear()){
				//weeks.get(i).addPlanningDTO(p);
				//weeks.get(i).setCommesse(weeks.get(i).addPlanningDTO(weeks.get(i).getCommesse(),p));
				addPlanningDTO(weeks.get(i).getCommesse(), p);
				break;
			}
		}
		log.end(metodo);
	}

	/**
	 * 1.se la settimana non contiene la commessa passata, viene creata la lista associata.
	 * 2.aggiungo la commessa nella lista associata
	 * @param p
	 */
	public void addPlanningDTO(HashMap<String, List<PlanningDTO>>commesse, PlanningDTO p){
		final String metodo="addPlanningDTO";
		log.start(metodo);
		if(!commesse.containsKey(p.getDescr_attivita())){
			commesse.put(p.getDescr_attivita(),new ArrayList<PlanningDTO>());
		}
		commesse.get(p.getDescr_attivita()).add(p);
		log.end(metodo);
	}
}