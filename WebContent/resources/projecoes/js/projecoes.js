CFINAC.projecao = new Object();

var iniciaProjecao = function() {
	var billsList, incomesList, billsTtValue, incomesTtValue;
	CFINAC.projecao.getBills = function() {
		var cfg = {
			url : "../rest/conta/getBills/" + 0,
			data : "id=" + 0,
			type : "GET",
			success : function(bills) {
				billsList = bills;
				showBillsAndIncomes(bills, 0);
				sum();
			},
			error : function(e) {
				alertPopUp("Erro na ação!");
			}
		};
		CFINAC.ajax.post(cfg);
	};

	CFINAC.projecao.getIncomes = function() {
		var cfg = {
			url : "../rest/renda/getIncomes/" + 0,
			data : "id=" + 0,
			type : "GET",
			success : function(incomes) {
				incomesList = incomes;
				showBillsAndIncomes(incomes, 1);
				sum();
			},
			error : function(e) {
				alertPopUp("Erro na ação!");
			}
		};
		CFINAC.ajax.post(cfg);
	};
	
	function sum(){
		for (x = 0; x < billsList.length; x++) {
			billsTtValue += billsList[x].totalValue;
		}
		for (x = 0; x < incomesList.length; x++) {
			incomesTtValue += incomesList[x].totalValue;
		}	
	}

	function showBillsAndIncomes(list, type) {
		var html = "<table class='table table-striped'>"
		html += "<tr>";
		html += "<th>Descrição</th>";
		html += "<th>Valor(R$)</th>";
		html += "<th>Ações</th>";
		html += "</tr>"
		if (list.length == 0) {
			html += "<tr>";
			html += "<td colspan='5' style='text-align:center'>Nada aqui!</td>";
			html += "</tr>";
		} else {
			for (i = 0; i < list.length; i++) {
				html += "<tr>";
				html += "<td>" + list[i].description + "</td>";
				html += "<td>" + list[i].totalValue + "</td>";
				html += "<td> ";
				html += "<a class='link' onclick='CFINAC.projecao.test("+i+", "+type+")'> ";
				html += "<i class='fa fa-trash-o' aria-hidden='true' />";
				html += "</a></td>";
				html += "</tr>";
			}
		}
		html += "</table>";

		type == 0 ? $("#billsTable").html(html) : $("#incomesTable").html(html);
	}
	
	CFINAC.projecao.test = function(index, type){
		type == 0 ? billsList.splice(x,1) : incomesList.splice(x,1);
	}

	CFINAC.projecao.add = function(type) {
		var desc = $(type == 0 ? "#billsForm #description"
				: "#incomesForm #description").val(), value = $(type == 0 ? "#billsForm #value"
				: "#incomesForm #value").val();

		if (desc == "" || desc == null || !+value) {
			alertPopUp("Preecha os campos corretamente");
			return false;
		}

		var newProjection = {};
		newProjection.description = desc;
		newProjection.totalValue = value;

		if (type == 0) {
			billsList.push(newProjection)
			showBillsAndIncomes(billsList, type);
		} else {
			incomesList.push(newProjection)
			showBillsAndIncomes(incomesList, type);
		}
		sum();
		alertPopUp("Done!");
	}

	CFINAC.projecao.getBills();
	CFINAC.projecao.getIncomes();

}
