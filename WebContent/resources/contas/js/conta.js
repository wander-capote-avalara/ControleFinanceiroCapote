CFINAC.contas = new Object();

function payParcel(id, accountId){
    var cfg = {
            type: "POST",
            url: "../rest/conta/payParcel/" + id,
            data: "id=" + id,
            success: function(r) {
                CFINAC.contas.detalheConta(accountId);
            	CFINAC.Message(r, "success");
            },
            error: function(e) {
            	CFINAC.Message(e.responseText, "error");
            }
        };
        CFINAC.ajax.post(cfg);
}

iniciaConta = function() {

    var changes = function() {
        if (+$("#inputParcels").val()) {

            var value = $("#inputTotalValue").val() / $("#inputParcels").val();
            $("#inputParcelValue").val(value.toFixed(2));
        } else {
            $("#inputParcelValue").val($("#inputTotalValue").val());

        }
    }

    $("#inputTotalValue").change(changes);
    $("#inputParcels").change(changes);

    var disab = function() {
        if ($("#hasDeadline").val() == 1) {
            $("#inputParcels").prop("disabled", false);
        } else {
            $("#inputParcels").prop("disabled", true);
            $("#inputParcels").val("");
            $("#inputParcelValue").val("");
        }
    };

    $("#hasDeadline").change(disab);

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
                            url: "../rest/conta/getBills/" + 0,
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
                                data: "formatedDate",
                                className: "center",
                            }, {
                                data: "hasDeadline",
                                className: "center",
                                mRender: function(data) {
                                    return data == 0 ? "Não" :
                                        "Sim";
                                }
                            }, {
                                data: "times",
                                className: "center",
                                mRender: function(data) {
                                    return data == 0 ? "À vista" :
                                        data;
                                }
                            }, {
                                data: "id",
                                className: "center",
                                bSortable: false,
                                mRender: function(id) {
                                    return "<a class='link' onclick='CFINAC.contas.editarConta(" +
                                        id +
                                        ")'>Editar</a> /" +
                                        " <a class='link' onclick='CFINAC.contas.deletaConta(" +
                                        id +
                                        ")'>Deletar</a>";
                                }
                            }, {
                                data: "id",
                                className: "center",
                                bSortable: false,
                                mRender: function(id) {
                                    return " <a class='link' data-toggle='modal' data-target='#Modal' onclick='CFINAC.contas.detalheConta(" +
                                        id +
                                        ")'>Mais</a>";
                                }
                            }]
                            // # sourceURL=sourcees.coffeee
                    });

                CFINAC.contas.detalheConta = function(id) {
                    var cfg = {
                        type: "GET",
                        url: "../rest/conta/getParcelsById/" + id,
                        data: "id=" + id,
                        success: function(listaDeParcelas) {
                            CFINAC.contas
                                .exibirDetalhes(listaDeParcelas);
                        },
                        error: function(e) {
                        	CFINAC.Message(e.responseText, "error");
                        }
                    };
                    CFINAC.ajax.post(cfg);
                };

                CFINAC.contas.exibirDetalhes = function(detailedList) {
                	$("#ModalLabel").html("Detalhes");
                    var html = "<div class='table-responsive' id='details'>";
                    html += "<table class='table table-hover table-striped'>";
                    html += "<thead>";
                    html += "<tr>";
                    html += "<th>#</th>";
                    html += "<th>Valor da Parcela(R$)</th>";
                    html += "<th>Status</th>";
                    html += "<th>Data de Pagamento</th>";
                    html += "<th>Data de Vencimento</th>";
                    html += "<th>Pagar</th>";
                    html += "</tr>";
                    html += "</thead>";
                    if (detailedList == null || detailedList == "") {
                        html += "<tr>";
                        html += "<td colspan = 5>Essa conta não contém parcelas!!</td>";
                        html += "</tr>";
                    } else {
                        for (var x = 0; x < detailedList.length; x++) {
                            var status = showStatus(detailedList[x].status);
                            html += "<tr>";
                            html += "<td>" + detailedList[x].parcelId + "</td>";
                            html += "<td>(R$)" +
                                detailedList[x].parcelValue +
                                "</td>";
                            html += "<td>" + status + "</td>";
                            html += "<td>" +
                                paymentDate(detailedList[x].paymentDateFormated) +
                                "</td>";
                            html += "<td>" + detailedList[x].formatedDate +
                            "</td>";
                            html += "<td><a class='link' onclick='payParcel("+detailedList[x].parcelId+","+detailedList[x].id+")'><i class='fa fa-credit-card' aria-hidden='true'></i></a></td>";
                            html += "</tr>";
                        }
                    }
                    html += "</table>";
                    html += "</div>";
                    $("#content").html(html);
                };

                function paymentDate(date) {
                    return date == "" ? "A pagar" : date;
                }

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

                CFINAC.contas.procuraCategoria = function() {
                    var cfg = {
                        type: "GET",
                        url: "../rest/categoria/getCategories/" + 0,
                        data: "id=" + 0,
                        success: function(listaDeCategorias) {
                            CFINAC.contas
                                .exibirCategorias(listaDeCategorias);
                        },
                        error: function(e) {
                        	CFINAC.Message(e.responseText, "error");
                        }
                    };
                    CFINAC.ajax.post(cfg);
                };

                CFINAC.contas.deletaConta = function(id) {
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
                                    url: "../rest/conta/deletaConta/" +
                                        id,
                                    data: "id=" + id,
                                    success: function(msg) {
                                    	CFINAC.Message(msg, "success");
                                        table.ajax.reload(null, false);
                                        CFINAC.contas.graphDetail();
                                    },
                                    error: function(e) {
                                    	CFINAC.Message(e.responseText, "error");
                                    }
                                };
                                CFINAC.ajax.post(cfg);
                            },
                            Cancel: function() {
                                $(this).dialog("close");
                            }
                        }
                    }
                    $("#msg").html(
                        "Deseja realmente excluir essa conta?");
                    $("#msg").dialog(cfgg);
                }

                CFINAC.contas.add = function() {
                    var cfg;
                    var msg, categoria = $("#inputCategory").val(),
                        description = $(
                            "#inputDescription").val(),
                        startDate = $(
                            "#inputStartDate").val(),
                        hasDeadline = $(
                            "#hasDeadline").val(),
                        totalValue = $(
                            "#inputTotalValue").val(),
                        times = $(
                            "#inputParcels").val(),
                        id = $("#id").val()
                    parcelValue = $("#inputParcelValue").val();

                    if (description != "" && startDate != "" &&
                        startDate != "" && totalValue != "" &&
                        categoria != 0) {
                        if (hasDeadline == 1 && times == 0) {
                        	CFINAC.Message("Preencha todos os campos!", "error");
                            return false;
                        }
                        var newBill = new Object();
                        newBill.id = id;
                        newBill.description = description;
                        newBill.startDate = startDate;
                        newBill.hasDeadline = hasDeadline;
                        newBill.times = times;
                        newBill.categoria = categoria;
                        newBill.totalValue = totalValue;
                        newBill.parcelValue = parcelValue;

                        cfg = {
                            url: "../rest/conta/add",
                            data: newBill,
                            success: function(r) {
                            	CFINAC.Message(r, "success");
                                $("#conteudoRegistro .btn-danger")
                                    .click();
                                table.ajax.reload(null, false);
                                CFINAC.contas.graphDetail();
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

                CFINAC.contas.editarConta = function(id) {
                    $("#conteudoRegistro .btn-danger").click();
                    var cfg = {
                        type: "GET",
                        url: "../rest/conta/getBills/" + id,
                        data: "id=" + id,
                        success: function(billData) {
                            $("#id").val(billData[0].id);
                            $("#inputCategory").val(
                                billData[0].categoria);
                            $("#inputStartDate").val(
                                billData[0].startDate);
                            $("#inputDescription").val(
                                billData[0].description);
                            $("#hasDeadline").val(
                                billData[0].hasDeadline);
                            $("#inputTotalValue").val(
                                billData[0].totalValue);
                            $("#inputParcels").val(billData[0].times);
                            changes();
                            disab();

                        },
                        error: function(rest) {
                        	CFINAC.Message(rest.responseText, "error");
                        }
                    };
                    CFINAC.ajax.post(cfg);
                };

                CFINAC.contas.exibirCategorias = function(
                    listaDeCategorias) {
                    var html = "Categoria:<select id='inputCategory' class='form-control'>" +
                        "<option value=0 selected>Selecione uma categoria...</option>";
                    for (var x = 0; x < listaDeCategorias.length; x++) {
                        html += "<option value='" +
                            listaDeCategorias[x].id + "'>" +
                            listaDeCategorias[x].name;
                    }
                    html += "</select>";

                    $("#categories").html(html);
                };

                CFINAC.contas.procuraCategoria();

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
                                text: 'Gráfico de contas feitas em cada categorias no mês atual'
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

                    var dateNow = new Date(), dates = {};

                        dates.firstMonth = dateNow.getUTCMonth() + 1;
                        dates.firstYear = dateNow.getUTCFullYear();
                        dates.secondMonth = dateNow.getUTCMonth() + 1;
                        dates.secondYear = dateNow.getUTCFullYear();

                    return !isDataTable ? dates : "?firstParam=" + dates.firstMonth + "&secondParam=" + dates.firstYear + "&thirdParam=" + dates.secondMonth + "&fourthParam=" + dates.secondYear;

                }

                CFINAC.contas.graphDetail = function() {                	
                	
                    var cfg = {
                        type: "POST",
                        url: "../rest/conta/getBillsByCategory/",
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

                CFINAC.contas.graphDetail();
            });
            }
                // # sourceURL=sourcees.coffeee
