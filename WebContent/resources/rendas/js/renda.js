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
                            alertPopUp("Erro na ação!")
                        }
                    };
                    CFINAC.ajax.post(cfg);
                };

                CFINAC.rendas.exibirDetalhes = function(detailedList) {
                    var html = "<div class='table-responsive' id='details'>";
                    html += "<table class='table table-hover table-striped'>";
                    html += "<tr>";
                    html += "<th>#</th>";
                    html += "<th>Valor da Renda(R$)</th>";
                    html += "<th>Status</th>";
                    html += "<th>Data de Pagamento</th>";
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
                            html += "<td>" +
                                detailedList[x].parcelValue +
                                "</td>";
                            html += "<td>" + showStatus(detailedList[x].status) + "</td>";
                            html += "<td>" +
                                paymentDate(detailedList[x].paymentDate) +
                                "</td>";
                            html += "<td>" + detailedList[x].dueDate +
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
                            $("#inputTotalValue").val(incomeData[0].totalValue);
                            $("#inputTimes").val(incomeData[0].times);
                            changes();

                        },
                        error: function(rest) {
                            alert("Erro ao editar a renda");
                        }
                    };
                    CFINAC.ajax.post(cfg);
                };

                CFINAC.rendas.deletaRenda = function(id) {
                    var cfgg = {
                        title: "Mensagem",
                        height: 250,
                        width: 400,
                        modal: true,
                        trigger: false,
                        buttons: {
                            "OK": function() {
                                $(this).dialog("close");
                                var cfg = {
                                    type: "POST",
                                    url: "../rest/renda/deletaRenda/" + id,
                                    data: "id=" + id,
                                    success: function(msg) {
                                        alertPopUp(msg);
                                        table.ajax.reload(null, false);
                                    },
                                    error: function(e) {
                                        alertPopUp("Erro na ação!")
                                    }
                                };
                                CFINAC.ajax.post(cfg);
                            },
                            Cancel: function() {
                                $(this).dialog("close");
                            }
                        }
                    }
                    $("#msg").html("Deseja realmente excluir essa renda?");
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
                        value = $("#inputTotalValue").val() / $("#inputTimes").val();
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
                                alertPopUp(r);
                                $("#conteudoRegistro .btn-danger").click();
                                table.ajax.reload(null, false);
                            },
                            error: function(err) {
                                alert("Erro na ação" + err.responseText);
                            }
                        };
                        CFINAC.ajax.post(cfg);
                    } else {
                        alertPopUp("Preencha os campos corretamente!");
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
    
    var graph = function(dataRenda, dataConta) {
        $('#highchart')
            .highcharts({
                chart: {
                    plotBackgroundColor: null,
                    plotBorderWidth: null,
                    plotShadow: false,
                    type: 'pie'
                },
                title: {
                    text: 'Gráfico das rendas e contas'
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
                    data: [{
                        name: 'Rendas',
                        y: dataRenda
                    }, {
                        name: 'Contas',
                        y: dataConta
                    }, ]
                }],
            });
    }
    
    function getTypedDate(isDataTable) {
        firstDate = $("#initialDate").val();
        secondDate = $("#finalDate").val();

        var dates = new Object(),
            dateNow = new Date();

        if (firstDate == "" || firstDate == null &
            secondDate == "" || secondDate == null) {
            dates.firstMonth = dateNow.getUTCMonth() + 1;
            dates.firstYear = dateNow.getUTCFullYear();
            dates.secondMonth = dateNow.getUTCMonth() + 1;
            dates.secondYear = dateNow.getUTCFullYear();
            firstDate = dates.firstMonth + "/" +
                dates.firstYear;
            secondDate = dates.secondMonth + "/" +
                dates.secondYear;
        } else {
            var arrayDateIni = firstDate.split("/"),
                arrayDateFin = secondDate
                .split("/");

            dates.firstMonth = arrayDateIni[0];
            dates.firstYear = arrayDateIni[1];
            dates.secondMonth = arrayDateFin[0];
            dates.secondYear = arrayDateFin[1];
        }

        return !isDataTable ? dates : "?firstParam=" + dates.firstMonth + "&secondParam=" + dates.firstYear + "&thirdParam=" + dates.secondMonth + "&fourthParam=" + dates.secondYear;

    }

    CFINAC.fluxoCaixa.graphDetail = function() {
        var income = 0,
            rent = 0;

        var cfg = {
            type: "POST",
            url: "../rest/conta/getTotalValueBills/",
            data: getTypedDate(false),
            success: function(data) {
                income = data;
                cfg.url = "../rest/renda/getTotalValueIncome/";
                cfg.success = function(data) {
                    rent = data;
                    graph(rent, income);
                    $("#dateGraph").html(
                        firstDate + " até " +
                        secondDate);
                    $("#showttvalue").html(rent - income);
                    tableConta.ajax
                        .url("../rest/conta/getBillsPerDate/" +
                            getTypedDate(true));
                    tableConta.ajax.reload(null, true);
                    tableRenda.ajax
                        .url("../rest/renda/getIncomesPerDate/?firstParam=" + dates.firstMonth + "&secondParam=" + dates.firstYear + "&thirdParam=" + dates.secondMonth + "&fourthParam=" + dates.secondYear);
                };
                CFINAC.ajax.post(cfg);
            },
            error: function(e) {
                alertPopUp("Erro ao buscar informações sobre o gráfico!" +
                    e)
            }
        };
        CFINAC.ajax.post(cfg);
    };

    CFINAC.fluxoCaixa.graphDetail();
};
// # aleluiasinho.coffee