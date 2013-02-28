<%@page import="it.dipendente.dto.RisorsaDTO"%>
<%@page import="it.dipendente.enums.Months"%>
<%@page import="java.util.List"%>
<%@page import="it.dipendente.dto.PlanningDTO"%>
<%@page import="java.util.Calendar"%>
<%@page import="it.dipendente.bo.Month"%>
<%@page import="it.dipendente.bo.Day"%>
<%@page import="it.dipendente.bo.Week"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<script type="text/javascript" src="script/timeReport.js"></script>

<%
Month m=(Month)request.getAttribute("month");
SimpleDateFormat sdf=new SimpleDateFormat("d");
if(request.getSession().getAttribute("utenteLoggato") != null){
%>

<div class="subtitle">Consuntivazione<%=m.getMonthLabel()%></div>

<form name="navigatore" action="./GestioneReport" method="post">
	<input type="hidden" name="azione" value="compilaTimeReport"/>
	<table class="timeReportNavigator" align="center">
		<tr>
			<td>Mese:</td>
			<td>
				<select name="mese">
<%
for(Months month:Months.values()){%>
					<option value="<%=month.getIndex()%>"<%if(month.getIndex()==m.getMonth()){%> selected="selected"<%}%>><%=month.getLabel()%></option>
<%	
}
%>
				</select>
			</td>
			<td>Anno:</td>
			<td>
				<select name="anno">
<%
Calendar c = Calendar.getInstance();
for(int z=c.get(Calendar.YEAR)-5;z<=c.get(Calendar.YEAR)+5;z++){
%>
					<option value="<%=z%>"<%if(m.getYear()==z){%> selected="selected"<%}%>><%=z%></option>
<%	
}
%>
				</select>
			</td>
			<td>
				<input type="submit" value="Cerca" class="search" title="Cerca la consuntivazione"/>
			</td>
		</tr>
	</table>
</form>
<form name="timeDetail" action="./GestioneReport" method="post">
<input type="hidden" name="azione" value="salvaTimeReport"/>
<table class="timeReport" align="center">
<%
double totOreOrd=0;
double totOreStr=0;
int cnt=0;
for(Week w:m.getWeeks()){%>
<tr><th class="weekHeader" colspan="8" onclick="cambiaVisibilitaCommesseInterne('<%=w.getWeekOfYear()%>');">Settimana <%=w.getWeekOfYear()%></th></tr><tr><th class="commesseHeader">Commesse Abilitate</th>
<%
	for(Day d:w.getDays()){%>
<th class="<%=d.getCssStyle()%>">
		<%=d.getDayLabel()%>
<%
		if(d.getDay()!=null){%>
<br>
			<%=sdf.format(d.getDay().getTime())%>
<%
		}%>
</th>
<%
	}%>
</tr>
<%
	if(w.getCommesse().isEmpty()){%>
<tr><th class="noCommesse" colspan="8">ATTENZIONE, PER TALE SETTIMANA NON RISULTA ALCUNA COMMESSA ASSOCIATA</th></tr>
<%
	}else{
		cnt=0;
		for(String commessaKey:w.getCommesse().keySet()){
			boolean isInterna=false;
			List<PlanningDTO>tmps=(List<PlanningDTO>)w.getCommesse().get(commessaKey);
			if(!tmps.isEmpty()){
				isInterna=tmps.get(0).isInterna();
			}
%>
<tr <%if(isInterna){%>id="interna_<%=w.getWeekOfYear()%>_<%=cnt++%>" style="display:none;"<%}%>>
<th class="commesse"><%=commessaKey%></th>
<%
			for(Day d:w.getDays()){%>
<td class="<%=d.getCssStyle()%>">
<%
				if(d.getDay()!=null){
					for(PlanningDTO p:(List<PlanningDTO>)w.getCommesse().get(commessaKey)){
						if(p.getData().get(Calendar.DAY_OF_MONTH)==d.getDay().get(Calendar.DAY_OF_MONTH)){
							totOreOrd+=p.getNumeroOre();
							totOreStr+=p.getStraordinari();
%>

<!-- onkeypress="validate(event)" -->

<table align="center">
<tr><td><input type="number" name="<%=commessaKey%>_ord<%=d.getDayKey()%>" value="<%=p.getNumeroOre()%>" max="24" min="0" class="<%=d.getCssStyle()%>"  title="ore ordinarie" onchange="checkOrario(this)" onblur="checkOrario(this)"></td></tr>
<tr><td><input type="number" name="<%=commessaKey%>_str<%=d.getDayKey()%>" value="<%=p.getStraordinari()%>" max="24" min="0" class="<%=d.getCssStyle()%>" title="ore straordinarie" onchange="checkOrario(this)" onblur="checkOrario(this)"></td></tr>
</table>
<%
							break;
						}
					}
				}%>
</td>
<%
			}%>
</tr>
<%
		}
	}%>
<%
}
%>
	<tr>
		<td class="totaliDx">Ore ordinarie:</td>
		<td class="totaliSx"><%=totOreOrd%></td>
		<td class="totaliDx">Ore straordinarie:</td>
		<td class="totaliSx"><%=totOreStr%></td>
		<td class="totaliDx">Totale:</td>
		<td class="totaliSx"><%=totOreOrd+totOreStr%></td>
		<td class="save" colspan="2">
<%
if(m.isSavable()){
%>
			<input type="submit" value="Salva" class="save" title="Salva la consuntivazione di<%=m.getMonthLabel()%>"/>
<%
}
%>
		</td>
	</tr>
</table>
</form>
<%
}else{
%>
	<script type="text/javascript">
		controlloSessioneAttiva();
	</script>
<%
}
%>