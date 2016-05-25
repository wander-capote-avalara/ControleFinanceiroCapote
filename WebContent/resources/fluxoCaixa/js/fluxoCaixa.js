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
													pointFormat : '{series.name}: <b>{series.y}:</b>'
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
										            name: 'Fluxo de Caixa',
										            data: [
										                { name: 'Rendas', y: dataRenda },
										                { name: 'Contas', y: dataConta },
										            ]
										        }],
											});
						}

						CFINAC.fluxoCaixa.graphDetail = function(firstDate,
								secondDate) {
							var dates = {}, dateNow = new Date(),
								income = 0,
								rent = 0;

							if (firstDate == "" || firstDate == null
									& secondDate == "" || secondDate == null) {
								dates.firstDate = dateNow.getUTCMonth() + 1.
								"/" + dateNow.getUTCFullYear();
								dates.lastDate = dateNow.getUTCMonth() + 1.
								"/" + dateNow.getUTCFullYear();
							} else {
								dates.firstDate = fistDate.getUTCMonth() + 1.
								"/" + firstDate.getUTCFullYear();
								dates.lastDate = secondDate.getUTCMonth() + 1.
								"/" + secondDate.getUTCFullYear();
							}

							var cfg = {
								type : "GET",
								url : "../rest/cashFlow/getRentAndIncome/"
										+ data,
								data : "dates=" + data,
								success : function(data) {
									income = data;
								},
								error : function(e) {
									alertPopUp("Erro na ação!")
								}
							};
							CFINAC.ajax.post(cfg);
							
							cgf.url = "../rest/cashFlow/getRentAndIncome/"+data;
							cfg.success = function(data){rent = data};
							
							CFINAC.ajax.post(cfg);
							
							graph(rent, income);
							
						};

						// CFINAC.fluxoCaixa.graphDetail(null, null);
						graph(33,33);
						//graph([{name:"test", data:[{name:"WTF",y:33},{name:"lwl",y:123}]}]);
						// # sourceURL=sourcees.coffeee

					});
}