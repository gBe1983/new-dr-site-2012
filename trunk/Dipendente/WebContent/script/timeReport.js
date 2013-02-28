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