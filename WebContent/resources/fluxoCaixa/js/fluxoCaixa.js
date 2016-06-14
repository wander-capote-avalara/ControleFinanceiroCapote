CFINAC.fluxoCaixa = new Object();

iniciaFluxoCaixa = function() {
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
                var tableConta = $('#exampleContas').DataTable({
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
                        url: "../rest/conta/getBillsPerDate/" + getTypedDate(true),
                        type: "GET"
                    },
                    select: {
                        style: 'os',
                        selector: 'td:first-child'
                    },
                    columns: [{
                            data: "description",
                            className: "center"
                        }, {
                            data: "totalValue",
                            className: "center",
                        }]
                        // # sourceURL=sourcees.coffeee
                });

                var tableRenda = $('#exampleRendas').DataTable({
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
                        url: "../rest/renda/getIncomesPerDate/" + getTypedDate(true),
                        type: "GET"
                    },
                    select: {
                        style: 'os',
                        selector: 'td:first-child'
                    },
                    columns: [{
                        data: "description",
                        className: "center"
                    }, {
                        data: "totalValue",
                        className: "center",
                    }]
                });

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
                                tableConta.ajax.url("../rest/conta/getBillsPerDate/" + getTypedDate(true));
                                tableConta.ajax.reload(null, true);
                                tableRenda.ajax.url("../rest/renda/getIncomesPerDate/" + getTypedDate(true));
                                tableRenda.ajax.reload(null, true);
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

                // # sourceURL=sourcees.coffeee

            });
}