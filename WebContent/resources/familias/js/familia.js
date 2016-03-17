CFINAC.familia = new Object();

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

$(document).bind({
	ajaxStart : function() {
		$(".loadModal").css("display", "block");
	},
	ajaxStop : function() {
		$(".loadModal").css("display", "none");
	}
});
$('#tokenize').tokenize({
	text : $(".TokenSearch input").val(),
	datas : "../../rest/familia/",
	valueField : "id",
	textField : "usuario",
	hintText : "Procure algo"
});

$(document).ready(function() {
	
	var table = $('#familiesTable')
    .DataTable({
        aLengthMenu: [
            [5, 10, 100],
            [5, 10, 100]
        ],
        iDisplayLength: 5,
        sAjaxDataProp : "",
        language: {
            url: "../js/Portuguese.json"
        },
        sPaginationType: "full_numbers",
        processing: true,
        ajax: {
            url: "../../rest/familia/getFamilies",
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
            data: "name",
            className: "center"
        }, {
            data: "owner",
            className: "center"
        }, {
            data: "usersName",
            className: "center",
        },{
            data: "id",
            className: "center",
            bSortable: false,
            mRender: function(id) {
                return "<a class='link' onclick='CFINAC.usuario.editarUsuario(" + id + ")'>Editar</a> /" + " <a class='link' onclick='CFINAC.usuario.deletaUsuario(" + id + ", false)'>Deletar</a>";
            }
        }]
    	// # sourceURL=sourcees.coffeee
    });
	
	CFINAC.familia.add = function() {
		var nomeFamilia = $("#inputFamilyName").val(),
			listaUsers = $("#tokenize").val();
		
		if(nomeFamilia == "" || nomeFamilia == null){
			alertPopUp("Preencha corretamente o campo Nome da Familia!")
		}else if(listaUsers == null || listaUsers == ""){
			alertPopUp("Adicione pelo menos um integrante na familia!")
		}
		
		var newFamily = new Object();
		newFamily.name = nomeFamilia;
		newFamily.owner = listaUsers[0];
		newFamily.users = listaUsers;
		
		cfg = {
				type : "POST",
				url : "../../rest/familia/add/",
				data : newFamily,
				success : function(msg) {
					alertPopUp(msg);
					table.ajax.reload(null, false);
				},
				error : function(err) {
					alert("Erro na ação!"
							+ err.responseText);
				}
			};
			CFINAC.ajax.post(cfg);
	};
	// # sourceURL=sourcees.coffeee
})
// # sourceURL=sourcees.coffee
