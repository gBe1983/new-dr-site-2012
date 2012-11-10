package it.dipendente.bo;

import it.dipendente.dto.PlanningDTO;
import it.dipendente.util.MyLogger;

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

	public Month(){
		log =new MyLogger(this.getClass());
		final String metodo="costruttore";
		log.start(metodo);
		Calendar day=Calendar.getInstance();
		year = day.get(Calendar.YEAR);//???
		month = day.get(Calendar.MONTH);//???
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
	
	
	public void addPlanningDTO(PlanningDTO p){
		//TODO
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

}