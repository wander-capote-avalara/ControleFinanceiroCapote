CFINAC.familia = new Object();

var iniciaFamilia = function() {
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

	$("#tokenizeClean").click(function() {
		$("#tokenize").tokenize().clear();
	});

	$(document).bind({
		ajaxStart : function() {
			$(".loadModal").css("display", "block");
		},
		ajaxStop : function() {
			$(".loadModal").css("display", "none");
		}
	});
	$('#tokenize').tokenize({
		text : $(".TokenSearch input").val(),
		datas : "../../rest/familia/",
		valueField : "id",
		textField : "usuario",
		hintText : "Procure algo"
	});

	$('#tokenizeInvite').tokenize({
		text : $(".TokenSearch input").val(),
		datas : "../rest/familia/",
		valueField : "id",
		textField : "usuario",
		hintText : "Procure algo"
	});

	$(document)
			.ready(
					function() {

						var table = $('#familiesTable')
								.DataTable(
										{
											aLengthMenu : [ [ 5, 10, 100 ],
													[ 5, 10, 100 ] ],
											iDisplayLength : 5,
											sAjaxDataProp : "",
											language : {
												url : "../js/Portuguese.json"
											},
											sPaginationType : "full_numbers",
											processing : true,
											ajax : {
												url : "../../rest/familia/getFamilies",
												type : "GET"
											},
											select : {
												style : 'os',
												selector : 'td:first-child'
											},
											columns : [
													{
														data : "id",
														className : "center"
													},
													{
														data : "name",
														className : "center"
													},
													{
														data : "owner",
														className : "center"
													},
													{
														data : "names",
														className : "center",
													},
													{
														data : "id",
														className : "center",
														bSortable : false,
														mRender : function(id) {
															return "<a class='link' onclick='CFINAC.familia.editarFamilia("
																	+ id
																	+ ")'>Editar</a> /"
																	+ " <a class='link' onclick='CFINAC.familia.deletaFamilia("
																	+ id
																	+ ")'>Deletar</a>";
														}
													} ]
										// # sourceURL=sourcees.coffeee
										});

						CFINAC.familia.editarFamilia = function(id) {
							$("#conteudoRegistro .btn-danger").click();
							$("#tokenize").tokenize().clear();
							var cfg = {
								type : "POST",
								url : "../../rest/familia/getFamilyById/" + id,
								data : "id = " + id,
								success : function(familyData) {
									console.dir(familyData);
									$("#id").val(familyData.id);
									$("#inputFamilyName").val(familyData.name);
									for (i = 0; i < familyData.usersName.length; i++) {
										$("#tokenize")
												.tokenize()
												.tokenAdd(
														familyData.usersName[i].id,
														familyData.usersName[i].usuario);
									}
								},
								error : function(rest) {
									alert("Erro ao editar o usuário");
								}
							};
							CFINAC.ajax.post(cfg);
							// # sourceURL=sourcsees.js
						}

						CFINAC.familia.deletaFamilia = function(id) {
							var msg = "Você deseja realmente excluir essa familia?", cfg = {
								title : "Mensagem",
								height : 250,
								width : 400,
								modal : true,
								trigger : false,
								buttons : {
									"OK" : function() {
										$(this).dialog("close");

										var cfg = {
											type : "POST",
											url : "../../rest/familia/deletaFamilia/"
													+ id,
											data : "id=" + id,
											success : function(msg) {
												alertPopUp("Família excluída com sucesso");
												table.ajax.reload(null, false);
											},
											error : function(e) {
												alertPopUp("Erro na ação!")
											}
										};
										CFINAC.ajax.post(cfg);
									},
									Cancel : function() {
										$(this).dialog("close");
									}
								}
							}
							$("#msg").html(msg);
							$("#msg").dialog(cfg);
						}

						CFINAC.familia.add = function() {
							var nomeFamilia = $("#inputFamilyName").val(), listaUsers = $(
									"#tokenize").val(), idFamily = $("#id")
									.val();

							if (nomeFamilia == "" || nomeFamilia == null) {
								alertPopUp("Preencha corretamente o campo Nome da Familia!")
							} else if (listaUsers == null || listaUsers == "") {
								alertPopUp("Adicione pelo menos um integrante na familia!")
							}

							var newFamily = new Object();
							newFamily.id = idFamily;
							newFamily.name = nomeFamilia;
							newFamily.owner = listaUsers[0];
							newFamily.users = listaUsers;

							cfg = {
								type : "POST",
								url : "../../rest/familia/add/",
								data : newFamily,
								success : function(msg) {
									alertPopUp(msg);
									table.ajax.reload(null, false);
									$("#tokenize").tokenize().clear();
									$("#conteudoRegistro .btn-danger").click();
								},
								error : function(err) {
									alert("Erro na ação!" + err.responseText);
								}
							};
							CFINAC.ajax.post(cfg);
						};
						// # sourceURL=sourcees.js
					})

	CFINAC.familia.leadProvider = function(id) {
		var msg = "Você deseja realmente torna-lo lider?", cfg = {
			title : "Mensagem",
			height : 250,
			width : 400,
			modal : true,
			trigger : false,
			buttons : {
				"OK" : function() {
					$(this).dialog("close");
					var cfg = {
						type : "POST",
						url : "../rest/familia/leadProvider/" + id,
						data : "id=" + id,
						success : function(msg) {
							alertPopUp(msg);
							membersTable.ajax.reload(null, false);
						},
						error : function(e) {
							alertPopUp("Você precisa ser dono da familia para fazer essa operação!")
						}
					};
					CFINAC.ajax.post(cfg);
				},
				Cancel : function() {
					$(this).dialog("close");
				}
			}
		}
		$("#msg").html(msg);
		$("#msg").dialog(cfg);
	}
	
	
	
	CFINAC.familia.invite = function() {

		var invite = new Object();
		invite.usersToInvite = $("#tokenizeInvite").val();

		cfg = {
			type : "POST",
			url : "../rest/familia/inviteUsers/",
			data : invite,
			success : function(msg) {
				$("#tokenizeInvite").tokenize().clear();
				$("#conteudoRegistro .btn-danger").click();
			},
			error : function(err) {
				alert("Erro na ação!" + err.responseText);
			}
		};
		CFINAC.ajax.post(cfg);
	}
	
	CFINAC.familia.getInvites = function(){
			var cfg = {
				type : "GET",
				url : "../rest/familia/getInvites",
				success : function(invites) {
					if(invites[0])
						alertPopUp("deuCertinhoWOOOW");
				},
				error : function(e) {
					window.location="../Login.html";
				}
			};
			CFINAC.ajax.post(cfg);
		}
		
	CFINAC.familia.getInvites();
	setInterval(CFINAC.familia.getInvites, 30000);
	
	CFINAC.familia.kickUser = function(id) {
		var msg = "Você deseja realmente expulsa-lo?", cfg = {
			title : "Mensagem",
			height : 250,
			width : 400,
			modal : true,
			trigger : false,
			buttons : {
				"OK" : function() {
					$(this).dialog("close");
					var cfg = {
						type : "POST",
						url : "../rest/familia/kickUser/" + id,
						data : "id=" + id,
						success : function(msg) {
							alertPopUp(msg);
							membersTable.ajax.reload(null, false);
						},
						error : function(e) {
							alertPopUp("Você precisa ser dono da familia para fazer essa operação!")
						}
					};
					CFINAC.ajax.post(cfg);
				},
				Cancel : function() {
					$(this).dialog("close");
				}
			}
		}
		$("#msg").html(msg);
		$("#msg").dialog(cfg);
	}

	var membersTable = $('#familyTable')
			.DataTable(
					{
						aLengthMenu : [ [ 5, 10, 100 ], [ 5, 10, 100 ] ],
						iDisplayLength : 5,
						sAjaxDataProp : "",
						language : {
							url : "js/Portuguese.json"
						},
						sPaginationType : "full_numbers",
						processing : true,
						ajax : {
							url : "../rest/familia/getFamilyMembers",
							type : "GET"
						},
						select : {
							style : 'os',
							selector : 'td:first-child'
						},
						columns : [
								{
									data : "usuario",
									className : "center"
								},
								{
									data : "id",
									className : "center",
									bSortable : false,
									mRender : function(id) {
										return "<a class='link' onclick='CFINAC.familia.leadProvider("
												+ id
												+ ")'>Tornar dono</a> /"
												+ " <a class='link' onclick='CFINAC.familia.kickUser("
												+ id + ")'>Expulsar</a>";
									}
								} ]
					});

	var familyBills = $('#familyBills').DataTable({
		aLengthMenu : [ [ 5, 10, 100 ], [ 5, 10, 100 ] ],
		iDisplayLength : 5,
		sAjaxDataProp : "",
		language : {
			url : "js/Portuguese.json"
		},
		sPaginationType : "full_numbers",
		processing : true,
		ajax : {
			url : "../rest/familia/getAllFamilyBills",
			type : "GET"
		},
		select : {
			style : 'os',
			selector : 'td:first-child'
		},
		columns : [ {
			data : "userName",
			className : "center"
		}, {
			data : "categoriaName",
			className : "center"
		}, {
			data : "totalValue",
			className : "center"
		}, {
			data : "formatedDate",
			className : "center"
		} ]
	// # sourceURL=sourcees.coffeee
	});

	var familyIncomes = $('#familyIncomes').DataTable({
		aLengthMenu : [ [ 5, 10, 100 ], [ 5, 10, 100 ] ],
		iDisplayLength : 5,
		sAjaxDataProp : "",
		language : {
			url : "js/Portuguese.json"
		},
		sPaginationType : "full_numbers",
		processing : true,
		ajax : {
			url : "../rest/familia/getAllFamilyIncomes",
			type : "GET"
		},
		select : {
			style : 'os',
			selector : 'td:first-child'
		},
		columns : [ {
			data : "userName",
			className : "center"
		}, {
			data : "categoriaName",
			className : "center"
		}, {
			data : "totalValue",
			className : "center"
		}, {
			data : "formatedDate",
			className : "center"
		} ]
	// # sourceURL=sourcees.coffeee
	});

	var graph = function(datas) {
		$('#BillsGraph').highcharts({
			chart : {
				plotBackgroundColor : null,
				plotBorderWidth : null,
				plotShadow : false,
				type : 'pie',
				width : 350,
				height : 300
			},
			title : {
				text : ''
			},
			tooltip : {
				format : '<b>{series.name}<b>{series.percentage:.1f}%',
			},
			plotOptions : {
				pie : {
					allowPointSelect : true,
					cursor : 'pointer',
					dataLabels : {
						enabled : true,
						format : '<b>{point.name}: (R$)</b> {point.y:.2f}',
					},
					showInLegend : true
				}
			},
			series : [ {
				name : 'Valor: (R$)',
				data : datas,
			} ],
		});
	}

	CFINAC.familia.graphDetailBills = function() {

		var cfg = {
			type : "GET",
			url : "../rest/conta/getFamilyBillsTotalValue/",
			success : function(data) {
				graph(data);
			},
			error : function(e) {
				alertPopUp("Erro ao buscar informações sobre o gráfico de contas!"
						+ e)
			}
		};
		CFINAC.ajax.post(cfg);
	};

	CFINAC.familia.graphDetailBills();

	var graphIncomes = function(datas) {
		$('#IncomesGraph').highcharts({
			chart : {
				plotBackgroundColor : null,
				plotBorderWidth : null,
				plotShadow : false,
				type : 'pie',
				width : 350,
				height : 300
			},
			title : {
				text : ''
			},
			tooltip : {
				format : '<b>{series.name}<b>{series.percentage:.1f}%',
			},
			plotOptions : {
				pie : {
					allowPointSelect : true,
					cursor : 'pointer',
					dataLabels : {
						enabled : true,
						format : '<b>{point.name}: (R$)</b> {point.y:.2f}',
					},
					showInLegend : true
				}
			},
			series : [ {
				name : 'Valor: (R$)',
				data : datas,
			} ],
		});
	}

	CFINAC.familia.graphDetailIncomes = function() {

		var cfg = {
			type : "GET",
			url : "../rest/renda/getFamilyIncomesTotalValue/",
			success : function(data) {
				graphIncomes(data);
			},
			error : function(e) {
				alertPopUp("Erro ao buscar informações sobre o gráfico de contas!"
						+ e)
			}
		};
		CFINAC.ajax.post(cfg);
	};

	CFINAC.familia.graphDetailIncomes();

}
// # sourceURL=sourcees.js
