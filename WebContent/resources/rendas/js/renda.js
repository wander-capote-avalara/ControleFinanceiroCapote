CFINAC.rendas = new Object();

iniciaRenda = function() {
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

						var table = $('#example')
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
												url : "../rest/renda/getIncomes",
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
														data : "status",
														className : "center",
														mRender: function(data) {
											                return data == 0 ? "Não ativo" : "Ativo";
											            }
													},
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
														data : "endDate",
														className : "center",
													},
													{
														data : "isFixed",
														className : "center",
														mRender: function(data) {
											                return data == 0 ? "Não" : "Sim";
											            }
													},
													{
														data : "times",
														className : "center",
														mRender: function(data) {
											                return data == 0 ? "À vista" : data;
											            }
													},
													{
														data : "id",
														className : "center",
														bSortable : false,
														mRender : function(id) {
															return "<a class='link' onclick='CFINAC.renda.editarRenda("
																	+ id
																	+ ")'>Editar</a> /"
																	+ " <a class='link' onclick='CFINAC.renda.deletaRenda("
																	+ id
																	+ ")'>Deletar</a>";
														}
													} ]
										// # sourceURL=sourcees.coffeee
										});

						CFINAC.rendas.add = function() {
							var cfg;
							var msg, categoria = $("#inputCategory").val(), description = $(
									"#inputDescription").val(), startDate = $(
									"#inputInitialDate").val(), isFixed = $(
									"#isFixed").val(), totalValue = $(
									"#inputTotalValue").val(), times = $(
									"#inputTimes").val(), id = $("#id").val();

							if (description != "" && endDate != ""
									&& startDate != "" && totalValue != "") {
								var newIncome = new Object();
								newIncome.id = id;
								newIncome.description = description;
								newIncome.startDate = startDate;
								newIncome.isFixed = isFixed;
								newIncome.times = times;
								newIncome.categoria = categoria;
								newIncome.totalValue = totalValue;

								cfg = {
									url : "../rest/renda/add",
									data : newIncome,
									success : function(r) {
										alertPopUp(r);
									},
									error : function(err) {
										alert("Erro na ação" + err.responseText);
									}
								};
								CFINAC.ajax.post(cfg);
							}
						}

					})

	CFINAC.rendas.procuraCategoria = function() {
		var cfg = {
			type : "POST",
			url : "../rest/categoria/getCategories",
			success : function(listaDeCategorias) {
				CFINAC.rendas.exibirCategorias(listaDeCategorias);
			},
			error : function(e) {
				alertPopUp("Erro na ação!")
			}
		};
		CFINAC.ajax.post(cfg);
	};

	CFINAC.rendas.exibirCategorias = function(listaDeCategorias) {
		var html = "Categoria:<select id='inputCategory' class='form-control'>"
				+ "<option value=0 selected>Selecione uma categoria...</option>";
		for (var x = 0; x < listaDeCategorias.length; x++) {
			html += "<option value='" + listaDeCategorias[x].id + "'>"
					+ listaDeCategorias[x].name;
		}
		html += "</select>";

		$("#categories").html(html);
	};

	CFINAC.rendas.procuraCategoria();
};
// # aleluiasinho.coffee
