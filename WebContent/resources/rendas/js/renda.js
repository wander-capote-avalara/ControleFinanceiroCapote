CFINAC.rendas = new Object();

iniciaRenda = function() {
    $(document)
        .ready(
            function() {
            	$("#inputTotalValue").maskMoney();
            	$("#inputParcelValue").maskMoney();
            	var changes = function() {
                    if (+$("#inputTimes").val()) {
                    	var noMask = $("#inputTotalValue").val().replace(/\./g, "").replace(/,/g, "."),
                        value = noMask / $("#inputTimes").val();
                        $("#inputParcelValue").val(value.toFixed(2));
                    } else {
                        $("#inputParcelValue").val($("#inputTotalValue").val());
                    }
                }

                $("#inputTotalValue").keyup(changes);
                $("#inputTimes").keyup(changes);
                
                var disab = function() {
                    if ($("#isFixed").val() == 0) {
                        $("#inputTimes").prop("disabled", false);
                    } else {
                        $("#inputTimes").prop("disabled", true);
                        $("#inputTimes").val("");
                        $("#inputParcelValue").val("");
                    }
                };

                $("#isFixed").change(disab);
                
                var table = $('#example')
                    .DataTable({
                        aLengthMenu: [
                            [5, 10, 100],
                            [5, 10, 100]
                        ],
                        iDisplayLength: 5,
                        sAjaxDataProp: "",
                        language: {
                            url: "js/Portuguese.json"
                        },
                        sPaginationType: "full_numbers",
                        processing: true,
                        ajax: {
                            url: "../rest/renda/getIncomes/" + 0,
                            data: "id=" + 0,
                            type: "GET"
                        },
                        select: {
                            style: 'os',
                            selector: 'td:first-child'
                        },
                        columns: [{
                                data: "categoriaName",
                                className: "center"
                            }, {
                                data: "description",
                                className: "center"
                            }, {
                                data: "formatedTotalValue",
                                className: "text-right",
                            }, {
                                data: "formatedDate",
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
                            }, {
                                data: "id",
                                className: "center",
                                bSortable: false,
                                mRender: function(id) {
                                    return " <a class='link' data-toggle='modal' data-target='#Modal' onclick='CFINAC.rendas.detalheRenda(" +
                                        id +
                                        ")'>Mais</a>";
                                }
                            }]
                            // # sourceURL=sourcees.coffeee
                    });

                function showStatus(status) {
                    switch (status) {
                        case 1:
                            return "Aberto";
                        case 2:
                            return "Atrasado";
                        case 3:
                            return "Pago";
                        default:
                            return "Status Invalido!";
                    }
                }

                CFINAC.rendas.detalheRenda = function(id) {
                    var cfg = {
                        type: "GET",
                        url: "../rest/renda/getParcelsById/" + id,
                        data: "id=" + id,
                        success: function(listaDeParcelas) {
                            CFINAC.rendas
                                .exibirDetalhes(listaDeParcelas);
                        },
                        error: function(e) {
                        	CFINAC.Message(e.responseText, "error");
                        }
                    };
                    CFINAC.ajax.post(cfg);
                };

                CFINAC.rendas.exibirDetalhes = function(detailedList) {
                	$("#ModalLabel").html("Detalhes");
                    var html = "<div class='table-responsive' id='details'>";
                    html += "<table class='table table-hover table-striped'>";
                    html += "<tr>";
                    html += "<th>#</th>";
                    html += "<th style='text-align:right;'>(R$)Valor da Renda</th>";
                    html += "<th>Data de Vencimento</th>";
                    html += "</tr>";
                    if (detailedList == null || detailedList == "") {
                        html += "<tr>";
                        html += "<td colspan = 5>Essa renda não contém parcelas!!</td>";
                        html += "</tr>";
                    } else {
                        for (var x = 0; x < detailedList.length; x++) {
                            var naqfi = x + 1;

                            html += "<tr>";
                            html += "<td>" + naqfi + "</td>";
                            html += "<td style='text-align:right;'>" +
                                detailedList[x].parcelValueFormated +
                                "</td>";
                            html += "<td>" + detailedList[x].formatedDate +
                                "</td>";
                            html += "</tr>";
                        }
                    }
                    html += "</table>"
                    html += "</div>";
                    $("#content").html(html);
                };

                function paymentDate(date) {
                    return date == null ? "A pagar" : date;
                }

                CFINAC.rendas.editarRenda = function(id) {
                    $("#conteudoRegistro .btn-danger").click();
                    var cfg = {
                        type: "GET",
                        url: "../rest/renda/getIncomes/" + id,
                        data: "id=" + id,
                        success: function(incomeData) {
                            $("#id").val(incomeData[0].id);
                            $("#inputCategory").val(incomeData[0].categoria);
                            $("#inputInitialDate").val(incomeData[0].startDate);
                            $("#inputDescription").val(incomeData[0].description);
                            $("#isFixed").val(incomeData[0].isFixed);
                            $("#inputTotalValue").val(incomeData[0].totalValueString);
                            $("#inputTimes").val(incomeData[0].times);
                            changes();
                            disab();

                        },
                        error: function(rest) {
                        	CFINAC.Message(rest.responseText, "error");
                        }
                    };
                    CFINAC.ajax.post(cfg);
                };

                CFINAC.rendas.deletaRenda = function(id) {
                	var cfg = {
                            type: "POST",
                            url: "../rest/renda/deletaRenda/" + id,
                            data: "id=" + id,
                            success: function(msg) {
                            	CFINAC.Message(msg, "success");
                                table.ajax.reload(null, false);
                                CFINAC.rendas.graphDetail();
                            },
                            error: function(e) {
                            	CFINAC.Message(e.responseText, "error");
                            }
                        };
                	CFINAC.Dialog("Deseja realmente excluir essa conta?", cfg);                            
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
                            "#inputTotalValue").val().replace(/\./g, "").replace(/,/g, "."),
                        times = $(
                            "#inputTimes").val(),
                        value = totalValue / $("#inputTimes").val();
                    parcelValue = value.toFixed(2),
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
                        newIncome.parcelValue = parcelValue;

                        cfg = {
                            url: "../rest/renda/add",
                            data: newIncome,
                            success: function(r) {
                            	CFINAC.Message(r, "success");
                                $("#conteudoRegistro .btn-danger").click();
                                table.ajax.reload(null, false);
                                CFINAC.rendas.graphDetail();
                            },
                            error: function(err) {
                            	CFINAC.Message(err.responseText, "error");
                            }
                        };
                        CFINAC.ajax.post(cfg);
                    } else {
                    	CFINAC.Message("Preencha todos os campos!", "error");
                    }
                }

            })

    CFINAC.rendas.procuraCategoria = function() {
        var cfg = {
            type: "GET",
            url: "../rest/categoria/getCategories/" + 0,
            data: "id=" + 0,
            success: function(listaDeCategorias) {
                CFINAC.rendas.exibirCategorias(listaDeCategorias);
            },
            error: function(e) {
            	CFINAC.Message(e.responseText, "error");
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

    var graph = function(datas) {
        $('#highcharts')
            .highcharts({
                chart: {
                    plotBackgroundColor: null,
                    plotBorderWidth: null,
                    plotShadow: false,
                    type: 'pie'
                },
                title: {
                    text: 'Gráfico de rendas feitas em cada categorias no mês atual'
                },
                tooltip: {
                    format: '<b>{series.name}<b>{series.percentage:.1f}%',
                },
                plotOptions: {
                    pie: {
                        allowPointSelect: true,
                        cursor: 'pointer',
                        dataLabels: {
                            enabled: true,
                            format: '<b>{point.name}: (R$)</b> {point.y:.2f}',
                        },
                        showInLegend: true
                    }
                },
                series: [{
                    name: 'Valor: (R$)',
                    data: datas,
                }],
            });
    }

    function getDate(isDataTable) {

        var dateNow = new Date(),
            dates = {};

        dates.firstMonth = dateNow.getUTCMonth() + 1;
        dates.firstYear = dateNow.getUTCFullYear();
        dates.secondMonth = dateNow.getUTCMonth() + 1;
        dates.secondYear = dateNow.getUTCFullYear();

        return !isDataTable ? dates : "?firstParam=" + dates.firstMonth + "&secondParam=" + dates.firstYear + "&thirdParam=" + dates.secondMonth + "&fourthParam=" + dates.secondYear;

    }

    CFINAC.rendas.graphDetail = function() {

        var cfg = {
            type: "POST",
            url: "../rest/renda/getIncomesByCategory/",
            data: getDate(false),
            success: function(data) {
                graph(data);
            },
            error: function(e) {
            	CFINAC.Message(e.responseText, "error");
            }
        };
        CFINAC.ajax.post(cfg);
    };

    CFINAC.rendas.graphDetail();
};
//# sourceURL=sourcesRenda.js