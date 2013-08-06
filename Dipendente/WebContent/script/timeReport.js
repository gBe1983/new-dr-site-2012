function validate(evt) {
	var theEvent = evt || window.event;
	var key = theEvent.keyCode || theEvent.which;
	key = String.fromCharCode(key);
	var regex = /[0-9]|\,/;
	if (!regex.test(key)) {
		theEvent.returnValue = false;
		if (theEvent.preventDefault){
			theEvent.preventDefault();
		}
	}
}
function checkOrario(input) {
	
	var regex = /[0-9]/;
	
	if (input.value.length>3) {
		alert("Attenzione! Il numero di ore inserito:"+input.value+" non è valido.");
		input.value="0.0";
		input.focus();
		return false;
	}else{
		if(input.value.length == 1){
			if(!regex.test(input.value)){
				alert("Attenzione! Il formato del numero ore inserito non è valido.");
				input.value="0.0";
				input.focus();
				return false;
			}
		}else if(input.value.length == 2){
			
			for(var x = 0; x < input.value.length; x++){
				var singoloCarattere = input.value.substring(x,x+1);
				if(!regex.test(singoloCarattere)){
					alert("Attenzione! Il formato del numero ore inserito non è valido.");
					input.value="0.0";
					input.focus();
					return false;
				}		
			}
			if(regex.test(input.value)){
				if(input.value > 8){
					alert("Attenzione! Il numero di ore inserito: "+input.value+" non è valido.");
					input.value="0.0";
					input.focus();
					return false;
				}
			}
			
			
		}else if(input.value.length == 3){
			if(input.value.indexOf(".") == -1){
				alert("Attenzione! Il formato del numero ore inserito non è valido. Formato corretto 0.0");
				input.value="0.0";
				input.focus();
				return false;
			}
			for(var y = 0; y < input.value.length; y++){
				var singleCarattere = input.value.substring(y,y+1);
				if(singleCarattere == "."){
					continue;
				}else if(!regex.test(singleCarattere)){
					alert("Attenzione! Il formato del numero ore inserito non è valido.");
					input.value="0.0";
					input.focus();
					return false;
				}		
			}
		}
		
	}
	if(input.value>24){
		alert("Attenzione! Superato il numero massimo di ore per commessa.");
		input.value="0.0";
		input.focus();
		return false;
	}
	return true;
}

function cambiaVisibilitaCommesseInterne(weekOfYear) {
	var e=null;
	for(var s=0;s<10;s++){
		e = document.getElementById("interna_"+weekOfYear+"_"+s);
		if(e!=null){
			e.style.display=e.style.display == "none"?"":"none";
		}else{
			break;
		}
	}
}


//metodo richiamato nella pagina timeReport.jsp
function compilazione(elm,commessa,name,note){
	
	var ore = "ore"+name;
	var straordinario = "straordinario"+name;
	var assenze = "assenze"+name;
	var tipologiaAssenze = "tipologiaAssenze"+name;
	var parametri = "parametri"+name;
	
	
	document.compilaOre.oreOrd.style.border = "1px solid #CCC";
	document.compilaOre.oreStrao.style.border = "1px solid #CCC";
	document.compilaOre.assenze.style.border = "1px solid #CCC";
	
	// effettuo questo tipo di controllo per effettuare la modifica o inserimento ore
	if($("#"+ore).val() > 0.0 || $("#"+assenze).val() > 0.0){
		
		//carico l'azione da compiere
		document.compilaOre.azione.value = "modificaOre";
		
		document.compilaOre.parametro.value = name;
		
		// carico le ore ordinare
		document.compilaOre.oreOrd.value = $("#"+ore).val();
		
		//carico le ore straordinarie
		document.compilaOre.oreStrao.value = $("#"+straordinario).val();
		
		//carico le ore assenze
		document.compilaOre.assenze.value = $("#"+assenze).val();
		
		if($("#"+assenze).val() != 0.0){
			$("#oreOrdinare select").css("display","inline");
            	
            	if($("#"+tipologiaAssenze).text() == "(Fe)"){
            		var $select = $('#oreOrdinare select');
                    var $options = $('option', $select);
                    for(var x = 0; x < $options.length; x++){
	            		if($options[x].value == "ferie"){
	                		$options[x].selected = true;
	                	}
                    }
    			}else if($("#"+tipologiaAssenze).text() == "(Pr)"){
    				var $selectPermessi = $('#oreOrdinare select');
    	            var $optionsPermessi = $('option', $selectPermessi);
    	            for(var xPermessi = 0; xPermessi < $optionsPermessi.length; xPermessi++){
	    				if($optionsPermessi[xPermessi].value == "permessi"){
	    					$optionsPermessi[xPermessi].selected = true;
	                	}
    	            }
    			}else if($("#"+tipologiaAssenze).text() == "(M)"){
    				var $selectMutua = $('#oreOrdinare select');
    	            var $optionsMutua = $('option', $selectMutua);
    	            for(var xMutua = 0; xMutua < $optionsMutua.length; xMutua++){
	    				if($optionsMutua[xMutua].value == "mutua"){
	    					$optionsMutua[xMutua].selected = true;
	                	}
    	            }
    			}else if($("#"+tipologiaAssenze).text() == "(PNR)"){
    				var $selectPeN = $('#oreOrdinare select');
    	            var $optionsPeN = $('option', $selectPeN);
    	            for(var xPeN = 0; xPeN < $optionsPeN.length; xPeN++){
	    				if($optionsPeN[xPeN].value == "permessiNonRetribuiti"){
	                		$optionsPeN[xPeN].selected = true;
	                	}
    	            }
    			}
		}else{
			$("#oreOrdinare select").css("display","none");
		}
		
		// carico le note
		if(note != "" && note != "null"){
			document.compilaOre.note.value = note;
		}else{
			document.compilaOre.note.value = "Inserisci qui le Note";
		}
		
		//carico la voce del bottone
		$(".save input").val("modifica ore");
		
		
		$('#compilade').dialog({
			modal: true,
			autoOpen: true,
			height: 400,
			width: 550,
			position: [350,200],
			show: {
				effect: "blind",
				duration: 1000
			},
			hide: {
				effect: "explode",
				duration: 1000
			}
		});
		
	}else{
		
		document.compilaOre.azione.value = "salvaBozza";
		
		document.compilaOre.parametro.value = commessa;
		
		document.compilaOre.parametri.value = $("#parametri"+name).val();
		
		// carico le ore ordinare
		document.compilaOre.oreOrd.value = $("#"+ore).val();
		
		//carico le ore straordinarie
		document.compilaOre.oreStrao.value = $("#"+straordinario).val();
		
		//carico le ore assenze
		document.compilaOre.assenze.value = $("#"+assenze).val();
		
		$("#oreOrdinare select").css("display","none");
		
		//faccio questo ciclo per resettare il menu di gestione delle assenze.
		var $selectDefault = $('#oreOrdinare select');
        var $optionsDefault = $('option', $selectDefault);
        for(var xDefault = 0; xDefault < $optionsDefault.length; xDefault++){
			if($optionsDefault[xDefault].value == ""){
        		$optionsDefault[xDefault].selected = true;
        	}
        }
		
		// carico le note
		if(note != "" && note != "null"){
			document.compilaOre.note.value = note;
		}else{
			document.compilaOre.note.value = "Inserisci qui le Note";
		}
		
		//carico la voce del bottone
		$(".save input").val("salva bozza");
		
		$('#compilade').dialog({
			modal: true,
			autoOpen: true,
			height: 400,
			width: 550,
			position: [350,200],
			show: {
				effect: "blind",
				duration: 1000
			},
			hide: {
				effect: "explode",
				duration: 1000
			}
		});
	}
	
	return false;
}

/* mi creo questa variabile per caricare le giornate selezionate */ 
var giornateSelezionate = "";

function selezioneGiornate(elm){
	
	if(elm.checked){
		giornateSelezionate += elm.value + ";";
	}else{
		giornateSelezionate = giornateSelezionate.replace(elm.value, "");
	}
	
}

function caricamentoGiornate(){
	
	var options = $(".case");
	
	var selezione = false;
	
	for (var i = 0; i < options.length; i++){
			
		if(options[i].checked){
			selezione = true;
		}
	}
	
	if(!selezione){
		alert("Attenzione! Selezionare almeno un giorno");
		return false;
	}
	
	document.listGiornate.parametri.value = document.listGiornate.parametro.value + ";" + giornateSelezionate;
	//alert(giornateSelezionate);
	
	giornateSelezionate = "";
	return true;
}

function oreInserite(){
	
	if(document.compilaOre.assenze.value != 0.0){
		$("#oreOrdinare select").css("display","inline");
	}else{
		$("#oreOrdinare select").css("display","none");
	}
	
	return true;
}

function controlloOreInserite(){
	
	var regex = /[0-9]|\./;
	
	var oreOrdinarie =  parseFloat(document.compilaOre.oreOrd.value);
	var straordinarie = parseFloat(document.compilaOre.oreStrao.value);
	var valoreAssenze = parseFloat(document.compilaOre.assenze.value);
	var tipologiaAssenze = document.compilaOre.tipologiaAssenze.value;
	
	for(var x = 0; x < document.compilaOre.oreOrd.value.length; x++){
		var singoloCarattere = document.compilaOre.oreOrd.value.substring(x,x+1);
		if(!regex.test(singoloCarattere)){
			document.compilaOre.oreOrd.value = "0.0";
			document.compilaOre.oreOrd.style.border = "1px solid #FF0000";
			alert("Attenzione! Il formato delle ore ordinarie inserite non è valido.");
			return false;
		}		
	}
	
	for(var y = 0; y < document.compilaOre.oreStrao.value.length; y++){
		var singleChar = document.compilaOre.oreStrao.value.substring(y,y+1);
		if(!regex.test(singleChar)){
			document.compilaOre.oreStrao.value = "0.0";
			document.compilaOre.oreStrao.style.border = "1px solid #FF0000";
			alert("Attenzione! Il formato delle ore straordinarie inserite non è valido.");
			return false;
		}		
	}
	
	for(var z = 0; z < document.compilaOre.assenze.value.length; z++){
		var singleChar1 = document.compilaOre.assenze.value.substring(z,z+1);
		if(!regex.test(singleChar1)){
			document.compilaOre.assenze.value = "0.0";
			document.compilaOre.assenze.style.border = "1px solid #FF0000";
			alert("Attenzione! Il formato delle ore d'assenza inserite non è valido.");
			return false;
		}		
	}
	
	if(oreOrdinarie > 8){
		alert("Attenzione! Superato il massimo delle ore ordinarie");
		return false;
	}
	if(oreOrdinarie < 8 && straordinarie > 0.0){
		alert("Attenzione! Valorizzare a 8 ore le ore Ordinarie");
		return false;
	}
	if((oreOrdinarie + valoreAssenze) > 8){
		alert("Attenzione! Le ore ordinarie + assenze non posso superare le 8 ore. Cambiare i valori inseriti");
		return false;
	}
	if(valoreAssenze > 0.0 && tipologiaAssenze == ""){
		alert("Attenzione! Valorizzare il menu tipologia assenze con una voce.");
		return false;
	}
	
	return true;

}

function checkedAll(elm){
	
	// if all checkbox are selected, check the selectall checkbox
    // and viceversa
	/*$(".case").click(function(){
	    $("#checkboxAll").removeAttr("checked");
	});*/
   
	var options = $(".case");
	
	for (var i = 0; i < options.length; i++){	
		if(options[i].checked && !options[i].disabled && elm.checked == false){
			options[i].checked = elm.checked;
			giornateSelezionate = "";
		}else{
			if(!options[i].disabled){
				options[i].checked = elm.checked;
				giornateSelezionate += options[i].value + ";";
			}
		}
	}
}

function controlloSelezionaCommessa(valore){
	
	if(valore == "annulla"){
		window.location.href = "./index.jsp?azione=report";
		$("#sceltaCommesse").dialog("close");
	}else{
		var selezionato = false;
		for(var i = 0; i < document.FormScelteCommesse.parametroCommessa.length; i++){
			if(document.FormScelteCommesse.parametroCommessa[i].checked){
				selezionato = true;
			}
		}
		
		if(!selezionato){
			alert("Selezionare una commessa")
			return false;
		}
		document.FormScelteCommesse.action = "./GestioneReport";
	}
	return true;
}