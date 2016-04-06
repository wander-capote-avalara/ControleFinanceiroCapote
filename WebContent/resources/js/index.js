CFINAC.index = new Object();

$(document).ready(function() {

	CFINAC.index.getUserInfo = function() {
		var cfg = {
			type : "POST",
			url : "../rest/usuario/getUserInfo",
			success : function(userInfo) {
				$("#username").html(userInfo.usuario);
				$("#actualbalance").html("Saldo Atual(R$): "+userInfo.saldoAtual);
				$("#family").html(userInfo.nomeFamilia);
				$("#next").html("Próx. fatura: "+userInfo.next);
			},
			error : function(e) {
				alertPopUp("Erro na ação!")
			}
		};
		CFINAC.ajax.post(cfg);
	}

	//# sourceURL=sourceWTF.js
})