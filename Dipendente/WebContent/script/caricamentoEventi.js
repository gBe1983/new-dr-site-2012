var xmlHttp = getXmlHttpObject();

function getXmlHttpObject(){
	  var xmlHttp=null;
  try{
	// Firefox, Opera 8.0+, Safari
	xmlHttp=new XMLHttpRequest();
  }	catch (e){
	// Internet Explorer
	  try{
		  xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
	  }catch (e){
		  xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
	  }
  }
  return xmlHttp;
}

function caricamentoEventi(){ 
	var url="./GestioneCalendarioEventi?azione=caricamentoEventi";
	xmlHttp.open("GET", url , true);
	xmlHttp.send(null);
	setTimeout('stateChanged()',1000);
}
	
function stateChanged() {
	
	if(xmlHttp.readyState == 4) {
		//Stato OK
		if (xmlHttp.status == 200) {
			var resp = xmlHttp.responseText;
			alert(resp);
		} else {
			alert(xmlHttp.responseText);
		}
	}
}
	