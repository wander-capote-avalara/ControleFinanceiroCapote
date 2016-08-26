CFINAC.usuario = new Object();

$(document).bind({
	ajaxStart : function() {
		$(".loadModal").css("display", "block");
	},
	ajaxStop : function() {
		$(".loadModal").css("display", "none");
	}
});


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

function validaEmail(email) {
	msg = "";
	if (email.indexOf("@") == -1 || email.indexOf(".") == -1
			|| email.indexOf("@") == 0
			|| email.lastIndexOf(".") + 1 == email.length
			|| (email.indexOf("@") + 1 == email.indexOf("."))) {
		msg += "Digite um e-mail verdadeiro!";
		$("#inputEmailLogin").focus();
	}
	return msg;
}

var iniciaUsuario = function() {
	$(document)
			.ready(
					function() {
						
						CFINAC.usuario.add = function() {
							var cfg;
							var msg, username = $("#inputUsernameLogin").val(), email = $(
									"#inputEmailLogin").val(), nivel = $(
									"#inputNivelLogin").val(), password = $(
									"#inputPasswordLogin").val(), confirmPass = $(
									"#inputPasswordLoginConfirmation").val(), isActive = $(
									"#isActive").val(), familyId = $(
									"#families").val(), id = $("#id").val();

							urli = "rest/usuario/add", patt = /\d/;

							if (!nivel.match(patt)) {
								alertPopUp("Acho que o senhor não deveria estar nem aqui!\n");
								return false;
							} else if (nivel != 0) {
								urli = "../../rest/usuario/add";
							}

							msg = validaEmail(email);

							if (username == "" || email == "" || password == ""
									|| confirmPass == "") {
								alertPopUp("Todos os campos são obrigatórios de preenchimento correto!\n");
								return false;
							} else if (password != confirmPass) {
								alertPopUp("As senhas devem ser iguais para completar o cadastro!\n");
								return false;
							} else if (msg != "") {
								alertPopUp(msg);
								return false;
							} else {
								var newUser = new Object();
								newUser.usuario = username;
								newUser.email = email;
								newUser.senha = password;
								newUser.confirmaSenha = confirmPass;
								newUser.nivel = nivel;
								newUser.ativo = isActive;
								newUser.id_familia = familyId;
								newUser.id = id;

								cfg = {
									url : urli,
									data : newUser,
									success : function(msg) {
										alertPopUp(msg);
										$("#conteudoRegistro .btn-danger")
												.click();
										if (urli == "../../rest/usuario/add") {
											table.ajax.reload(null, false);
										}
									},
									error : function(err) {
										alert("Erro na ação!"
												+ err.responseText);
									}
								};
								CFINAC.ajax.post(cfg);
							}

						};

						var table = $('#example')
					    .DataTable({
					        aLengthMenu: [
					            [5, 10, 100],
					            [5, 10, 100]
					        ],
					        iDisplayLength: 5,
					        language: {
					            url: "../js/Portuguese.json"
					        },
					        sPaginationType: "full_numbers",
					        processing: true,
					        ajax: {
					            url: "../../rest/usuario/",
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
					            data: "nivel",
					            className: "center"
					        }, {
					            data: "usuario",
					            className: "center"
					        }, {
					            data: "nomeFamilia",
					            className: "center",
					            mRender: function(data) {
					                return data == null ? "Sem familia" : data;
					            }
					        }, {
					            data: "email",
					            className: "center"
					        }, {
					            data: "ativo",
					            className: "center",
					            mRender: function(data) {
					                return data == "0" ? "Não Ativo" : "Ativo";
					            }
					        }, {
					            data: "id",
					            className: "center",
					            bSortable: false,
					            mRender: function(id) {
					                return "<a class='link' onclick='CFINAC.usuario.deletaUsuario(" + id + ", true)'>Ativar</a> /" + " <a class='link' onclick='CFINAC.usuario.editarUsuario(" + id + ")'>Editar</a> /" + " <a class='link' onclick='CFINAC.usuario.deletaUsuario(" + id + ", false)'>Deletar</a>";
					            }
					        }]
					    });

						CFINAC.usuario.deletaUsuario = function(id, doa) {
							var type = doa ? 'ativar' : 'excluir', msg = 'Você deseja realmente '
									+ type + ' esse usuário?', cfg = {
								title : "Mensagem",
								height : 250,
								width : 400,
								modal : true,
								trigger : false,
								buttons : {
									"OK" : function() {
										$(this).dialog("close");
										var urli, type = doa ? "ativado"
												: "deletado";
										doa ? urli = "../../rest/usuario/ativaUsuario/"
												: urli = "../../rest/usuario/deletaUsuario/";

										var cfg = {
											type : "POST",
											url : urli + id,
											data : "id=" + id,
											success : function(msg) {
												alertPopUp("Usuário " + type
														+ " com sucesso");
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
							$("#msg").html(msg);
							$("#msg").dialog(cfg);
						}

						CFINAC.usuario.exibirFamilias = function(
								listaDeFamilias) {
							var html = "<select id='families' class='form-control'>"
									+ "<option value=0 selected>Selecione uma família...</option>";
							for (var x = 0; x < listaDeFamilias.length; x++) {
								html += "<option value='"
										+ listaDeFamilias[x].id + "'>"
										+ listaDeFamilias[x].name + " - "
										+ listaDeFamilias[x].owner
										+ "</option>";
							}

							html += "</select>";

							$("#familias").html(html);
						};

						CFINAC.usuario.editarUsuario = function(id) {
							$("#conteudoRegistro .btn-danger").click();
							var cfg = {
								type : "POST",
								url : "../../rest/usuario/getUserById/" + id,
								success : function(userData) {
									$("#id").val(userData.id);
									$("#inputUsernameLogin").val(
											userData.usuario);
									$("#inputEmailLogin").val(userData.email);
									$("#inputNivelLogin").val(userData.nivel);
									$("#isActive").val(userData.ativo);
									$("#families").val(userData.id_familia);

								},
								error : function(rest) {
									alert("Erro ao editar o usuário");
								}
							};
							CFINAC.ajax.post(cfg);
						};

						CFINAC.usuario.procuraFamilias = function() {
							var cfg = {
								type : "POST",
								url : "../../rest/usuario/getFamilies",
								success : function(listaDeFamilias) {
									CFINAC.usuario
											.exibirFamilias(listaDeFamilias);
								},
								error : function(e) {
									alertPopUp("Erro na ação!")
								}
							};
							CFINAC.ajax.post(cfg);
						};

						CFINAC.usuario.exibirUsuarios = function(
								listaDeUsuarios, valorBusca) {
							var html = "<table id='tableUsers' class='display' class='table table-hover'>";
							html += "<thead>" + "<tr>" + "<th>#</th>"
									+ "<th>Nível</th>" + "<th>Nome</th>"
									+ "<th>Família</th>" + "<th>@email</th>"
									+ "<th>Editar</th>" + "<th>Excluir</th>"
									+ "</tr>" + "</thead><tbody>";
							if (listaDeUsuarios != undefined
									&& listaDeUsuarios.length > 0
									&& listaDeUsuarios[0].id != undefined) {
								for (var i = 0; i < listaDeUsuarios.length; i++) {

									var family = listaDeUsuarios[i].id_familia ? listaDeUsuarios[i].id_familia
											: "Sem família";

									html += "<tr>";
									html += "<td>" + listaDeUsuarios[i].id
											+ "</td>";
									html += "<td>" + listaDeUsuarios[i].nivel
											+ "</td>";
									html += "<td>" + listaDeUsuarios[i].usuario
											+ "</td>";
									html += "<td>" + family + "</td>";
									html += "<td>" + listaDeUsuarios[i].email
											+ "</td>";
									html += "<td>"
											+ "<a class='link' onclick='CFINAC.usuario.editarContato(";
									html += +listaDeUsuarios[i].Id_Usuarios
											+ ")' >Editar</a></td>";
									html += "<td><a class='link' onclick='CFINAC.usuario.deletarContato(";
									html += +listaDeUsuarios[i].Id_Usuarios
											+ ")' >Deletar</a>" + "</td>";
									html += "</tr>";
								}
							} else {
								if (listaDeUsuarios == undefined
										|| (listaDeUsuarios != undefined && listaDeUsuarios.length > 0)) {

									var cfg = {

										type : "POST",
										url : "../../rest/usuario/getUsers",
										success : function(listaDeUsuarios) {
											CFINAC.usuario
													.exibirUsuarios(listaDeUsuarios);
										},
										error : function(err) {
											alert("Erro ao consultar os usuarios: "
													+ err.responseText);
										}
									};
									CFINAC.ajax.post(cfg);
									$("#msg").html(msg.msg);

								} else {
									html += "<tr><td colspan='7' class='text-center'>Nenhum registro encontrado</td></tr>";
								}
							}
							html += "</tbody></table>";
							$("#listaUsuarios").html(html);
						};

						CFINAC.usuario.procuraFamilias();

					})
};
//# sourceURL=source.js
