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
            	
                CFINAC.contas.procuraCategoria = function() {
                    var cfg = {
                        type: "POST",
                        url: "../rest/categoria/getCategories",
                        success: function(listaDeCategorias) {
                            CFINAC.contas.exibirCategorias(listaDeCategorias);
                        },
                        error: function(e) {
                            alertPopUp("Erro na ação!")
                        }
                    };
                    CFINAC.ajax.post(cfg);
                };
                
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
                        parcelValue = $("#inputParcelValue").val(),
                        id = $("#id").val();

                    if (description != "" && startDate != "" &&
                        startDate != "" && totalValue != "") {
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
								table.ajax.reload(null, false);
                            },
                            error: function(err) {
                                alert("Erro na ação" + err.responseText);
                            }
                        };
                        CFINAC.ajax.post(cfg);
                    }
                }

                CFINAC.contas.exibirCategorias = function(listaDeCategorias) {
                    var html = "Categoria:<select id='inputCategory' class='form-control'>" +
                        "<option value=0 selected>Selecione uma categoria...</option>";
                    for (var x = 0; x < listaDeCategorias.length; x++) {
                        html += "<option value='" + listaDeCategorias[x].id + "'>" +
                            listaDeCategorias[x].name;
                    }
                    html += "</select>";

                    $("#categories").html(html);
                };

                CFINAC.contas.procuraCategoria();
            	
            })
}