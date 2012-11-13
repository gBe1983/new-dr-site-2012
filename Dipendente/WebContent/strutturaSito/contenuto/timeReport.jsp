<%@page import="java.util.List"%>
<%@page import="it.dipendente.dto.PlanningDTO"%>
<%@page import="java.util.Calendar"%>
<%@page import="it.dipendente.bo.Month"%>
<%@page import="it.dipendente.bo.Day"%>
<%@page import="it.dipendente.bo.Week"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>



<%
Month m=(Month)request.getAttribute("month");
SimpleDateFormat sdf=new SimpleDateFormat("d");
//TODO RIPRISTINARE CONTROLLO UTENTE LOGGATO
//HttpSession controlloUtenteLoggato = request.getSession();
//if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
%>

<div class="subtitle"><h2>Consuntivazione<%=m.getMonthLabel()%></h2></div>
<%for(Week w:m.getWeeks()){%>
<table class="timeReport"><tr><th class="weekHeader" colspan="8">Settimana <%=w.getWeekOfYear()%></th></tr><tr><th class="commesseHeader">Commesse Abilitate</th>
<%for(Day d:w.getDays()){%>
<th class="<%=d.getCssStyle()%>">
<%=d.getDayLabel()%>
<%if(d.getDay()!=null){%>
<br>
<%=sdf.format(d.getDay().getTime())%>
<%}%>
</th>
<%}%>
</tr>
<%if(w.getCommesse().isEmpty()){%>
<tr><th class="noCommesse" colspan="8">ATTENZIONE, PER TALE SETTIMANA NON RISULTA ALCUNA COMMESSA ASSOCIATA</th></tr>
<%}else{%>
<%for(String commessaKey:w.getCommesse().keySet()){%>
<tr><th class="commesse"><%=commessaKey%></th>
<%for(Day d:w.getDays()){%>
<td class="<%=d.getCssStyle()%>">
<%if(d.getDay()!=null){
	for(PlanningDTO p:(List<PlanningDTO>)w.getCommesse().get(commessaKey)){
		if(p.getData().get(Calendar.DAY_OF_MONTH)==d.getDay().get(Calendar.DAY_OF_MONTH)){%>
<input type="text" name="<%=d.getDayKey()%>" maxlength="3" class="<%=d.getCssStyle()%>" alt="ore ordinarie" >
			<%
			break;
		}
	}
%>
<br>
<%=sdf.format(d.getDay().getTime())%>
<%}%>
</td>
<%}%>
</tr>
<%}%>
<%}%>
</table>
<%}%>