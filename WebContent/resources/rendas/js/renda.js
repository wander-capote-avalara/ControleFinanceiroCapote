CFINAC.rendas = new Object();

iniciaRenda = function() {
    function alertPopUp(msg) {
        cfg = {
            title: "Mensagem",
            height: 250,
            width: 400,
            modal: true,
            buttons: {
                "OK": function() {
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
                    .DataTable({
                        aLengthMenu: [
                            [5, 10, 100],
                            [5, 10, 100]
                        ],
                        iDisplayLength: 5,
                        sAjaxDataProp: "",
                        language: {
                            url: "../js/Portuguese.json"
                        },
                        sPaginationType: "full_numbers",
                        processing: true,
                        ajax: {
                            url: "../rest/renda/getIncomes/"+0,
                            data: "id=" + 0,
                            type: "GET"
                        },
                        select: {
                            style: 'os',
                            selector: 'td:first-child'
                        },
                        columns: [{
                                data: "id",
                                className: "center"
                            }, {
                                data: "status",
                                className: "center",
                                mRender: function(data) {
                                    return data == 0 ? "Não ativo" : "Ativo";
                                }
                            }, {
                                data: "categoriaName",
                                className: "center"
                            }, {
                                data: "description",
                                className: "center"
                            }, {
                                data: "totalValue",
                                className: "center",
                            }, {
                                data: "startDate",
                                className: "center",
                            }, {
                                data: "isFixed",
                                className: "center",
                                mRender: function(data) {
                                    return data == 0 ? "Não" : "Sim";
                                }
                            }, {
                                data: "times",
                                className: "center",
                                mRender: function(data) {
                                    return data == 0 ? "À vista" : data;
                                }
                            }, {
                            	data: "id",
                                className: "center",
                                bSortable: false,
                                mRender: function(id) {
                                    return "<a class='link' onclick='CFINAC.rendas.editarRenda(" +
                                        id +
                                        ")'>Editar</a> /" +
                                        " <a class='link' onclick='CFINAC.rendas.deletaRenda(" +
                                        id +
                                        ")'>Deletar</a>";
                                }
                            }]
                            // # sourceURL=sourcees.coffeee
                    });
                
				CFINAC.rendas.editarRenda = function(id) {
					$("#conteudoRegistro .btn-danger").click();
					var cfg = {
						type : "GET",
						url : "../rest/renda/getIncomes/" + id,
						data: "id="+id,
						success : function(incomeData) {
							$("#id").val(incomeData[0].id);
							$("#inputCategory").val(incomeData[0].categoria);
							$("#inputInitialDate").val(incomeData[0].startDate);
							$("#inputDescription").val(incomeData[0].description);
							$("#isFixed").val(incomeData[0].isFixed);
							$("#inputTotalValue").val(incomeData[0].totalValue);
							$("#inputTimes").val(incomeData[0].times);
							changes();

						},
						error : function(rest) {
							alert("Erro ao editar a renda");
						}
					};
					CFINAC.ajax.post(cfg);
				};
                
                CFINAC.rendas.deletaRenda = function(id) {
                	var cfgg = {
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
								url : "../rest/renda/deletaRenda/" + id,
								data : "id=" + id,
								success : function(msg) {
									alertPopUp(msg);
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
				$("#msg").html("Chimichanga");
				$("#msg").dialog(cfgg);
                }

                CFINAC.rendas.add = function() {
                    var cfg;
                    var msg, categoria = $("#inputCategory").val(),
                        description = $(
                            "#inputDescription").val(),
                        startDate = $(
                            "#inputInitialDate").val(),
                        isFixed = $(
                            "#isFixed").val(),
                        totalValue = $(
                            "#inputTotalValue").val(),
                        times = $(
                            "#inputTimes").val(),
                        id = $("#id").val();

                    if (description != "" && startDate != "" &&
                        startDate != "" && totalValue != "") {
                        var newIncome = new Object();
                        newIncome.id = id;
                        newIncome.description = description;
                        newIncome.startDate = startDate;
                        newIncome.isFixed = isFixed;
                        newIncome.times = times;
                        newIncome.categoria = categoria;
                        newIncome.totalValue = totalValue;

                        cfg = {
                            url: "../rest/renda/add",
                            data: newIncome,
                            success: function(r) {
                                alertPopUp(r);
                                $("#conteudoRegistro .btn-danger").click();
								table.ajax.reload(null, false);
                            },
                            error: function(err) {
                                alert("Erro na ação" + err.responseText);
                            }
                        };
                        CFINAC.ajax.post(cfg);
                    }else{
                    	alertPopUp("Preencha os campos corretamente!");
                    }
                }

            })

    CFINAC.rendas.procuraCategoria = function() {
        var cfg = {
            type: "POST",
            url: "../rest/categoria/getCategories",
            success: function(listaDeCategorias) {
                CFINAC.rendas.exibirCategorias(listaDeCategorias);
            },
            error: function(e) {
                alertPopUp("Erro na ação!")
            }
        };
        CFINAC.ajax.post(cfg);
    };

    CFINAC.rendas.exibirCategorias = function(listaDeCategorias) {
        var html = "Categoria:<select id='inputCategory' class='form-control'>" +
            "<option value=0 selected>Selecione uma categoria...</option>";
        for (var x = 0; x < listaDeCategorias.length; x++) {
            html += "<option value='" + listaDeCategorias[x].id + "'>" +
                listaDeCategorias[x].name;
        }
        html += "</select>";

        $("#categories").html(html);
    };

    CFINAC.rendas.procuraCategoria();
};
// # aleluiasinho.coffee