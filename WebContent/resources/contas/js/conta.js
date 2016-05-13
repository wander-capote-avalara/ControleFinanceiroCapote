CFINAC.contas = new Object();

iniciaConta = function() {
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
                                data: "startDate",
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
                                    return " <a class='link' onclick='CFINAC.contas.detalheConta(" +
                                        id +
                                        ")'>Mais</a>";
                                }
                            }]
                            // # sourceURL=sourcees.coffeee
                    });

                CFINAC.contas.detalheConta = function(){
                	
                };
                
                CFINAC.contas.procuraCategoria = function() {
                    var cfg = {
                        type: "GET",
                        url: "../rest/categoria/getCategories/"+ 0,
                        data: "id=" + 0,
                        success: function(listaDeCategorias) {
                            CFINAC.contas
                                .exibirCategorias(listaDeCategorias);
                        },
                        error: function(e) {
                            alertPopUp("Erro na ação!")
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
                    $("#msg").html("Deseja realmente excluir essa conta?");
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
                        startDate != "" && totalValue != "" && categoria != 0) {
                    	if(hasDeadline == 1 && times == 0){
                    		alertPopUp("Preencha todos os campos!");
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
                                alertPopUp(r);
                                $("#conteudoRegistro .btn-danger")
                                    .click();
                                table.ajax.reload(null, false);
                            },
                            error: function(err) {
                                alert("Erro na ação" + err.responseText);
                            }
                        };
                        CFINAC.ajax.post(cfg);
                    }else{
                    	alertPopUp("Preencha todos os campos!");
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

                        },
                        error: function(rest) {
                            alert("Erro ao editar a renda");
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

            })
}