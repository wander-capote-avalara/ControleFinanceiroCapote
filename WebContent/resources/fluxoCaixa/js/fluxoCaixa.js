CFINAC.fluxoCaixa = new Object();

iniciaFluxoCaixa = function() {
	function alertPopUp(msg) {
		cfg = {
			title : "Mensagem",
			height : 250,
			width : 400,
			modal : true,
			buttons : {
				"OK" : function() {
					$(this).dialog("close");
				}
			}
		};
		$("#msg").html(msg);
		$("#msg").dialog(cfg);
	}

	$(document)
			.ready(
					function() {
						var tableConta = $('#exampleContas').DataTable({
							aLengthMenu : [ [ 5, 10, 100 ], [ 5, 10, 100 ] ],
							iDisplayLength : 5,
							sAjaxDataProp : "",
							language : {
								url : "js/Portuguese.json"
							},
							sPaginationType : "full_numbers",
							processing : true,
							ajax : {
								url : "../rest/conta/getBills/" + 0,
								data : "id=" + 0,
								type : "GET"
							},
							select : {
								style : 'os',
								selector : 'td:first-child'
							},
							columns : [ {
								data : "categoriaName",
								className : "center"
							}, {
								data : "description",
								className : "center"
							}, {
								data : "totalValue",
								className : "center",
							} ]
						// # sourceURL=sourcees.coffeee
						});

						var tableRenda = $('#exampleRendas').DataTable({
							aLengthMenu : [ [ 5, 10, 100 ], [ 5, 10, 100 ] ],
							iDisplayLength : 5,
							sAjaxDataProp : "",
							language : {
								url : "js/Portuguese.json"
							},
							sPaginationType : "full_numbers",
							processing : true,
							ajax : {
								url : "../rest/renda/getIncomes/" + 0,
								data : "id=" + 0,
								type : "GET"
							},
							select : {
								style : 'os',
								selector : 'td:first-child'
							},
							columns : [ {
								data : "categoriaName",
								className : "center"
							}, {
								data : "description",
								className : "center"
							}, {
								data : "totalValue",
								className : "center",
							} ]
						});

						var graph = function(dataRenda, dataConta) {
							$('#highchart')
									.highcharts(
											{
												chart : {
													plotBackgroundColor : null,
													plotBorderWidth : null,
													plotShadow : false,
													type : 'pie'
												},
												title : {
													text : 'Gráfico das rendas e contas do mes de: <div id="date"></div>'
												},
												tooltip : {
													  format: '<b>{point.name}</b> {point.y:.2f} Rs.',
												},
												plotOptions : {
													pie : {
														allowPointSelect : true,
														cursor : 'pointer',
														dataLabels : {
															enabled : false
														},
														showInLegend : true
													}
												},
												series :[{
										            name: 'Valor: (R$)',
										            data: [
										                { name: 'Rendas', y: dataRenda },
										                { name: 'Contas', y: dataConta },
										            ]
										        }],
											});
						}

						CFINAC.fluxoCaixa.graphDetail = function(firstDate,
								secondDate) {
							var dates = new Object(), dateNow = new Date(),
							income = 0, rent = 0;

							if (firstDate == "" || firstDate == null
									& secondDate == "" || secondDate == null) {
								dates.firstMonth = dateNow.getUTCMonth() + 1;
								dates.firstYear = dateNow.getUTCFullYear();
								dates.secondMonth = dateNow.getUTCMonth() + 1;
								dates.secondYear = dateNow.getUTCFullYear();
							} else {
								dates.firstMonth = firstDate.getUTCMonth() + 1;
								dates.firstYear = firstDate.getUTCFullYear();
								dates.secondMonth = secondDate.getUTCMonth() + 1;
								dates.secondYear = secondDate.getUTCFullYear();
							}

							var cfg = {
								type : "POST",
								url : "../rest/conta/getTotalValueBills/",
								data : dates,
								success : function(data) {
									income = data;
									cfg.url = "../rest/renda/getTotalValueIncome/";
									cfg.success = function(data){
									rent = data; 
									graph(rent, income);
									};								
									CFINAC.ajax.post(cfg);
								},
								error : function(e) {
									alertPopUp("Erro ao buscar informações sobre o gráfico!"+e)
								}
							};
							CFINAC.ajax.post(cfg);
							
							
							
						};

						CFINAC.fluxoCaixa.graphDetail(null, null);
						//graph(33,33);
						//graph([{name:"test", data:[{name:"WTF",y:33},{name:"lwl",y:123}]}]);
						// # sourceURL=sourcees.coffeee

					});
}