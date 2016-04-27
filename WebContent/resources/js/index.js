CFINAC.index = new Object();


$(document).ready(
		function() {

			CFINAC.index.getUserInfo = function() {
				var cfg = {
					type : "POST",
					url : "../rest/usuario/getUserInfo",
					success : function(userInfo) {
						$("#username").html(userInfo.usuario);
						$("#actualbalance").html(
								"Saldo Atual(R$): " + userInfo.saldoAtual);
						$("#family").html(userInfo.nomeFamilia);
						$("#next").html("Próx. fatura: " + userInfo.next);
						$("#nextbalance").html(
								"Saldo próx. mês(R$): " + userInfo.saldoProx)
					},
					error : function(e) {
						alert("errou");
					}
				};
				CFINAC.ajax.post(cfg);
			}

			CFINAC.index.endSession = function() {
				var cfg = {
					type : "POST",
					url : "EndSession",
					success : function(userInfo) {
						$("#username").html(userInfo.usuario);
						$("#actualbalance").html(
								"Saldo Atual(R$): " + userInfo.saldoAtual);
						$("#family").html(userInfo.nomeFamilia);
						$("#next").html("Próx. fatura: " + userInfo.next);
						$("#nextbalance").html(
								"Saldo próx. mês(R$): " + userInfo.saldoProx)
					},
					error : function(e) {
						alert("errou");
					}
				};
				CFINAC.ajax.post(cfg);
			}

			// # sourceURL=sourceWTF.js
		})