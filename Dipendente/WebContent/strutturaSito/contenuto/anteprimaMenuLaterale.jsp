<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<form action="./GestioneCurriculum" method="post" name="previewMenuLaterale" id="anteprimaForm">
	<input type="hidden" name="azione" value="anteprimaGlobale" >
	<input type="hidden" name="parametro" value="" >
	<input type="hidden" name="lastMovimento" value="" >
	<label>In che modalit� d'anteprima vuoi visualizzare il Curriculum Vitae?</label><br>
	<input type="radio" name="sceltaCurriculumAnteprima" value="europeo" checked="checked" onClick="visualizzazione(this.value);"><label>Europeo</label><br>
	<input type="radio" name="sceltaCurriculumAnteprima" value="aziendale" onClick="visualizzazione(this.value);"><label>Aziendale</label><br><br>
	<input type="submit" value="anteprima" onclick="return closeWindowsAnteprimaMenuLaterale()">
	<button value="chiudi" onclick="return closeFinestraAnteprimaMenuLaterale()">chiudi</button>
</form>