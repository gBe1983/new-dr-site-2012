package it.dipendente.bo;

import it.dipendente.dto.PlanningDTO;
import it.util.log.MyLogger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;



public class Month{
	private Logger log = null;
	private int year;
	private int month;
	private String monthLabel;
	private int weeksCnt=0;
	private List<Week>weeks;

	public Month(Calendar c){
		log = Logger.getLogger(Month.class);
		log.info("costruttore Month");
		Calendar day = (Calendar)c.clone();
		year = day.get(Calendar.YEAR);
		month = day.get(Calendar.MONTH);
		monthLabel=new SimpleDateFormat(" MMMMMMMMM yyyy",Locale.ITALIAN).format(day.getTime());
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
		
		log.info("addPlanningDTO");
		for (int i=0;i<weeks.size();i++) {
			if(p.getData().get(Calendar.WEEK_OF_YEAR)==weeks.get(i).getWeekOfYear()){
				weeks.get(i).addPlanningDTO(p);
				break;
			}
		}
	}

	public boolean isSavable(){
		//TODO IMPLEMENTARE LA MODIFICA CHE VERIFICA SE IL MESE RISULTA CHIUSO
		Calendar today=Calendar.getInstance();
		int y = today.get(Calendar.YEAR);
		if (year>y){
			return true;
		}
		int m = today.get(Calendar.MONTH);
		if(year==y){
			if(month>=m){
				return true;
			}
			if(m-month<=2){
				return true;
			}
			return false;
		}
		if(year<=y && y-year==1){
			if(month>m && month-m<=2){
				return true;
			}
			if(month<m && m-month<=2){
				return true;
			}
			return false;
		}
		return false;
	}

	public String getBodyMail(){
		double totOreOrd=0;
		double totOreStr=0;
		SimpleDateFormat sdf=new SimpleDateFormat("d");
		StringBuilder sb = new StringBuilder("<table align='center' border='1'>");
		String dayInLineStyle;
		for(Week w:weeks){
			sb	.append("<tr><th colspan='8' style='font:bold 12px verdana, arial, sans-serif;color:#A1A1A1;background:#CCC;'>Settimana")
				.append(w.getWeekOfYear())
				.append("</th></tr><tr><th style='font:normal 12px verdana, arial, sans-serif;color:#A1A1A1;'>Commesse Abilitate</th>");
			for(Day d:w.getDays()){
				dayInLineStyle=d.getInLineStyle();
				sb	.append("<th style='")
					.append(dayInLineStyle)
					.append("'><table align='center'><tr><td align='center' style='")
					.append(dayInLineStyle)
					.append("'>")
					.append(d.getDayLabel())
					.append("</td></tr><tr><td align='center' style='")
					.append(dayInLineStyle)
					.append("'>");
				if(d.getDay()!=null){
					sb.append(sdf.format(d.getDay().getTime()));
				}else{
					sb.append("&nbsp;");
				}
				sb.append("</td></tr></table></th>");
			}
			sb.append("</tr>");
			if(w.getCommesse().isEmpty()){
				sb.append("<tr><th colspan='8' style='font:bold 12px verdana,arial,sans-serif;color:red;background:#CCC;'>ATTENZIONE, PER TALE SETTIMANA NON RISULTA ALCUNA COMMESSA ASSOCIATA</th></tr>");
			}else{
				for(String commessaKey:w.getCommesse().keySet()){
					sb	.append("<tr><th style='font:normal 12px verdana, arial, sans-serif;color:blue;background:#E7D6B6;'>")
						.append(commessaKey)
						.append("</th>");
					for(Day d:w.getDays()){
						dayInLineStyle=d.getInLineStyle();
						sb	.append("<td style='")
							.append(dayInLineStyle)
							.append("'>");
						if(d.getDay()!=null){
							for(PlanningDTO p:(List<PlanningDTO>)w.getCommesse().get(commessaKey)){
								if(p.getData().get(Calendar.DAY_OF_MONTH)==d.getDay().get(Calendar.DAY_OF_MONTH)){
									totOreOrd+=p.getNumeroOre();
									totOreStr+=p.getStraordinari();
									sb	.append("<table align='center'><tr><td align='center' style='")
										.append(dayInLineStyle)
										.append("'>")
										.append(p.getNumeroOre())
										.append("</td></tr><tr><td align='center' style='")
										.append(dayInLineStyle)
										.append("'>")
										.append(p.getStraordinari())
										.append("</td></tr></table>");
									break;
								}
							}
						}
						sb.append("</td>");
					}
					sb.append("</tr>");
				}
			}
		}
		String riepilogoInLineStyle="font:bold 12px verdana, arial, sans-serif;color:red;background:#DDD;";
		sb	.append("<tr><td style='")
			.append(riepilogoInLineStyle)
			.append("'>Ore ordinarie:</td><td style='")
			.append(riepilogoInLineStyle)
			.append("'>")
			.append(totOreOrd)
			.append("</td><td style='")
			.append(riepilogoInLineStyle)
			.append("'>Ore straordinarie:</td><td style='")
			.append(riepilogoInLineStyle)
			.append("'>")
			.append(totOreStr)
			.append("</td><td style='")
			.append(riepilogoInLineStyle)
			.append("'>Totale:</td><td colspan='3' style='")
			.append(riepilogoInLineStyle)
			.append("'>")
			.append(totOreOrd+totOreStr)
			.append("</td></tr></table>");
		return sb.toString();
	}
}