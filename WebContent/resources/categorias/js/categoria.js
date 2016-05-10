CFINAC.categorias = new Object();

iniciaCategoria = function() {
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

    $(document).ready(function() {
    	 CFINAC.categorias.add = function() {
             var cfg;
             var msg, categoria = $("#inputCategory").val(),
                 id = $("#id").val();

             if (description != "") {
                 var newCategory = new Object();
                 newCategory.id = id;
                 newCategory.description = description;

                 cfg = {
                     url: "../rest/categoria/add",
                     data: newCategory,
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
             }
         }
    })
}