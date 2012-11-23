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
	if (input.value.length>3) {
		alert("Attenzione! Il numero di ore inserito:"+input.value+" non è valido.");
		input.value="0.0";
		input.focus();
		return false;
	}else{
		if(input.value.indexOf(".") == -1){
			alert("Attenzione! Il formato del numero ore inserito non è valido. Formato corretto 0.0");
			input.value="0.0";
			input.focus();
			return false;
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