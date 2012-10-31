function controlloSessioneAttiva(){
	alert("La sessione è scaduta. Rieffettuare la login");
	url = window.location.href;
	var variabiliUrl = url.split("/");
	for(a=0; a < variabiliUrl.length; a++){
			if(a == 2){
				var localVariabili = variabiliUrl[a].split(":");
				for(x=0; x < localVariabili.length; x++){
					if(localVariabili[x] == "localhost"){
						window.location = "http://localhost/dr";
					}if(localVariabili[x] == "cvonline"){
						window.location.href = "http://cvonline.tv";
					}if(localVariabili[x] == "drconsulting"){
						window.location.href= "http://drconsulting.tv";
					}
				}
			}else{
				continue;
			}
	}
}