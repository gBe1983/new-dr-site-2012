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
List<RisorsaDTO>risorse=(List<RisorsaDTO>)request.getAttribute("risorse");

Month m=(Month)request.getAttribute("month");
SimpleDateFormat sdf=new SimpleDateFormat("d");
String idR=(String)request.getAttribute("idRis");
int idRis=57;
if(idR!=null){
	idRis=Integer.parseInt(idR);
}
//TODO RIPRISTINARE CONTROLLO UTENTE LOGGATO
//HttpSession controlloUtenteLoggato = request.getSession();
//if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
%>

<div class="subtitle"><h2>Consuntivazione<%=m.getMonthLabel()%></h2></div>

<form name="navigatore" action="./GestioneReport" method="post">
	<input type="hidden" name="azione" value="compilaTimeReport"/>
	<table class="timeReportNavigator">
		<tr>
<!--TODO START DA RIMUOVERE...-->
			<td>Risorsa:</td>
			<td>
				<select name="risorsa">
<%
for(int r=0;r<risorse.size();r++){%>
					<option value="<%=risorse.get(r).getIdRisorsa()%>"<%if(risorse.get(r).getIdRisorsa()==idRis){%> selected="selected"<%}%>><%=risorse.get(r).getCognome()%> <%=risorse.get(r).getNome()%></option>
<%	
}
%>
				</select>
			</td>
<!--TODO END DA RIMUOVERE...-->
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
<%
for(Week w:m.getWeeks()){%>
<table class="timeReport"><tr><th class="weekHeader" colspan="8">Settimana <%=w.getWeekOfYear()%></th></tr><tr><th class="commesseHeader">Commesse Abilitate</th>
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
		for(String commessaKey:w.getCommesse().keySet()){%>
<tr><th class="commesse"><%=commessaKey%></th>
<%
			for(Day d:w.getDays()){%>
<td class="<%=d.getCssStyle()%>">
<%
				if(d.getDay()!=null){
					for(PlanningDTO p:(List<PlanningDTO>)w.getCommesse().get(commessaKey)){
						if(p.getData().get(Calendar.DAY_OF_MONTH)==d.getDay().get(Calendar.DAY_OF_MONTH)){%>
<input type="number" name="<%=commessaKey%>_ord<%=d.getDayKey()%>" value="<%=p.getNumeroOre()%>" max="24" min="0" class="<%=d.getCssStyle()%>" onkeypress="validate(event)" title="ore ordinarie" onchange="checkOrario(this)">
<br>
<input type="number" name="<%=commessaKey%>_str<%=d.getDayKey()%>" value="<%=p.getStraordinari()%>" max="24" min="0" class="<%=d.getCssStyle()%>" onkeypress="validate(event)" title="ore straordinarie" onchange="checkOrario(this)">
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
</table>
<%
}
%>
<table class="timeReport">
	<tr>
		<td class="save">
			<input type="submit" value="Salva" class="save" title="Salva la consuntivazione di<%=m.getMonthLabel()%>"/>
		</td>
	</tr>
</table>
</form>