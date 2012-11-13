/**
 * 
 */
package it.dipendente.enums;

import java.util.Calendar;

/**
 * @author Dr
 *
 */
public enum Months {
	JANUARY(Calendar.JANUARY,"Gennaio"),
	FEBRUARY(Calendar.FEBRUARY,"Febbraio"),
	MARCH(Calendar.MARCH,"Marzo"),
	APRIL(Calendar.APRIL,"Aprile"),
	MAY(Calendar.MAY,"Maggio"),
	JUNE(Calendar.JUNE,"Giugno"),
	JULY(Calendar.JULY,"Luglio"),
	AUGUST(Calendar.AUGUST,"Agosto"),
	SEPTEMBER(Calendar.SEPTEMBER,"Settembre"),
	OCTOBER(Calendar.OCTOBER,"Ottobre"),
	NOVEMBER(Calendar.NOVEMBER,"Novembre"),
	DECEMBER(Calendar.DECEMBER,"Dicembre");
	private final int index;
	private final String label;
	private Months(int index,String label){
		this.index=index;
		this.label=label;
	}
	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
}