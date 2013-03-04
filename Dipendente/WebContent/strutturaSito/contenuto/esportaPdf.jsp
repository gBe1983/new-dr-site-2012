<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<form action="./GestioneCurriculum" method="post" name="pdf">
	<input type="hidden" name="azione" value="esportaPdf" >
	<input type="hidden" name="parametro" value="" >
	<input type="hidden" name="lastMovimento" value="">
	<input type="hidden" name="area" value="" >
	<label>In che modalità vuoi esportare il Curriculum Vitae?</label><br>
	<input type="radio" name="sceltaCurriculum" value="europeo" checked="checked" onClick="visualizzazione(this.value);"><label>Europeo</label><br>
	<input type="radio" name="sceltaCurriculum" value="aziendale" onClick="visualizzazione(this.value);"><label>Aziendale</label><br><br>
	<input type="submit" value="esporta" onclick="return closeWindows()">
	<button value="chiudi" onclick="return closeFinestra()">chiudi</button>
</form>