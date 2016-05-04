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
	CFINAC.index.getUserInfo();
	setInterval(CFINAC.index.getUserInfo, 15000);
	
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
		
		/*function ajax(config){
			return new Promise(function(resolve, reject){
				var xhttp = new Xhttpre();
				
				xhttp.open(config.url, config.type);
				
				chhtp.onload = funcitnon(e){
					e.type==200;
						resolve(e.resonposeText);
				};
				
				http.onerro = funcitn(e){
					reject(e);
				}
				
				http.send(config.data);
			});
		}
		
		ajax({url:""}).then(function(r){}).catch(function(e){})*/
