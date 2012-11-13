package it.dipendente.bo;

import it.dipendente.dto.PlanningDTO;
import it.util.log.MyLogger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Month {
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
	 * cerco la settimana a cui aggiungere la commessa, una volta trovata la gli passo la commessa da aggiungere
	 * @param p
	 */
	public void addPlanningDTO(PlanningDTO p){
		for (Week w : getWeeks()) {
			if(p.getData().get(Calendar.WEEK_OF_YEAR)==w.getWeekOfYear()){
				w.addPlanningDTO(p);
				break;
			}
		}
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

}