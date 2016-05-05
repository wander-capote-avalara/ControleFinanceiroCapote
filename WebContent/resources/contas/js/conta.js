CFINAC.contas = new Object();

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
            	
            })
}