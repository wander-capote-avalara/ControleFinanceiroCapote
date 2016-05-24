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
						var tableConta = $('#exampleContas')
								.DataTable(
										{
											aLengthMenu : [ [ 5, 10, 100 ],
													[ 5, 10, 100 ] ],
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
											columns : [
													{
														data : "categoriaName",
														className : "center"
													},
													{
														data : "description",
														className : "center"
													},
													{
														data : "totalValue",
														className : "center",
													},
													{
														data : "startDate",
														className : "center",
													},
													{
														data : "hasDeadline",
														className : "center",
														mRender : function(data) {
															return data == 0 ? "Não"
																	: "Sim";
														}
													},
													{
														data : "times",
														className : "center",
														mRender : function(data) {
															return data == 0 ? "À vista"
																	: data;
														}
													} ]
										// # sourceURL=sourcees.coffeee
										});

						var tableRenda = $('#exampleRendas')
								.DataTable(
										{
											aLengthMenu : [ [ 5, 10, 100 ],
													[ 5, 10, 100 ] ],
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
											columns : [
													{
														data : "categoriaName",
														className : "center"
													},
													{
														data : "description",
														className : "center"
													},
													{
														data : "totalValue",
														className : "center",
													},
													{
														data : "startDate",
														className : "center",
													},
													{
														data : "isFixed",
														className : "center",
														mRender : function(data) {
															return data == 0 ? "Não"
																	: "Sim";
														}
													},
													{
														data : "times",
														className : "center",
														mRender : function(data) {
															return data == 0 ? "À vista"
																	: data;
														}
													} ]
										// # sourceURL=sourcees.coffeee
										});
						
						$(function () {

						    $(document).ready(function () {

						        // Build the chart
						        $('#highchart').highcharts({
						            chart: {
						                plotBackgroundColor: null,
						                plotBorderWidth: null,
						                plotShadow: false,
						                type: 'pie'
						            },
						            title: {
						                text: 'Browser market shares January, 2015 to May, 2015'
						            },
						            tooltip: {
						                pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
						            },
						            plotOptions: {
						                pie: {
						                    allowPointSelect: true,
						                    cursor: 'pointer',
						                    dataLabels: {
						                        enabled: false
						                    },
						                    showInLegend: true
						                }
						            },
						            series: [{
						                name: 'Brands',
						                colorByPoint: true,
						                data: [{
						                    name: 'Microsoft Internet Explorer',
						                    y: 56.33
						                }, {
						                    name: 'Chrome',
						                    y: 24.03,
						                    sliced: true,
						                    selected: true
						                }, {
						                    name: 'Firefox',
						                    y: 10.38
						                }, {
						                    name: 'Safari',
						                    y: 4.77
						                }, {
						                    name: 'Opera',
						                    y: 0.91
						                }, {
						                    name: 'Proprietary or Undetectable',
						                    y: 0.2
						                }]
						            }]
						        });
						    });
						});
					})
}