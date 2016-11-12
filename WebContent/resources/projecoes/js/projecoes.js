CFINAC.projecao = new Object();

	

var iniciaProjecao = function() {
	$("#firstTable #value").maskMoney();
	$("#secondTable #value").maskMoney();
	CFINAC.Message("Poderá haver diferenças de saldos!", "warning");
	
	var billsList, incomesList, billsTtValue = 0, incomesTtValue = 0, firstDate = "MM/yyyy", secondDate = "MM/yyyy";
	CFINAC.projecao.getBills = function() {
		var cfg = {
			url : "../rest/conta/getBillsPerDate/" + getTypedDate(true),
			type : "GET",
			success : function(bills) {
				billsList = bills;
				showBillsAndIncomes(bills, 0);
				CFINAC.projecao.getIncomes();
			},
			error : function(e) {
            	CFINAC.Message(e.responseText, "error");
			}
		};
		CFINAC.ajax.post(cfg);
	};

	CFINAC.projecao.getIncomes = function() {
		var cfg = {
			url : "../rest/renda/getIncomesPerDate/" + getTypedDate(true),
			type : "GET",
			success : function(incomes) {
				incomesList = incomes;
				showBillsAndIncomes(incomes, 1);
				sum();
			},
			error : function(e) {
            	CFINAC.Message(e.responseText, "error");
			}
		};
		CFINAC.ajax.post(cfg);
	};

	function sum() {
		if (billsList != null) {
			billsTtValue = 0;
			for (x = 0; x < billsList.length; x++) {
				billsTtValue += +billsList[x].totalValue;
			}
		}
		if (incomesList != null) {
			incomesTtValue = 0;
			for (x = 0; x < incomesList.length; x++) {
				incomesTtValue += +incomesList[x].totalValue;
			}
		}
		var ballance = incomesTtValue - billsTtValue
		$("#showttvalue").html(ballance.toFixed(2).replace(/(\d)(?=(\d{3})+\.)/g, '$1,'));
	}

	function showBillsAndIncomes(list, type) {
		var html = "<div class='table-responsive' id='details' style='max-height: 70% !IMPORTANT;'>";
		html += "<table class='table table-hover table-striped'>";
		html += "<tr>";
		html += "<th>Descrição</th>";
		html += "<th style='text-align:right'>(R$)Valor</th>";
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
				html += "<td style='text-align:right'>" + list[i].formatedTotalValue + "</td>";
				html += "<td> ";
				html += "<a class='link' onclick='CFINAC.projecao.RemoveFromTable("
						+ i + ", " + type + ")'> ";
				html += "<i class='fa fa-trash-o' aria-hidden='true' />";
				html += "</a></td>";
				html += "</tr>";
			}
		}
		html += "</table>";
		html += "</div>";

		type == 0 ? $("#billsTable").html(html) : $("#incomesTable").html(html);
	}

	function getTypedDate(isDataTable) {
		firstDate = $("#initialDate").val();
		secondDate = $("#finalDate").val();

		var dates = new Object(), dateNow = new Date();

		if (firstDate == "" || firstDate == null & secondDate == ""
				|| secondDate == null) {
			dates.firstMonth = dateNow.getUTCMonth() + 1;
			dates.firstYear = dateNow.getUTCFullYear();
			dates.secondMonth = dateNow.getUTCMonth() + 1;
			dates.secondYear = dateNow.getUTCFullYear();
			firstDate = dates.firstMonth + "/" + dates.firstYear;
			secondDate = dates.secondMonth + "/" + dates.secondYear;
		} else {
			var arrayDateIni = firstDate.split("/"), arrayDateFin = secondDate
					.split("/");

			dates.firstMonth = arrayDateIni[0];
			dates.firstYear = arrayDateIni[1];
			dates.secondMonth = arrayDateFin[0];
			dates.secondYear = arrayDateFin[1];
		}

		return !isDataTable ? dates : "?firstParam=" + dates.firstMonth
				+ "&secondParam=" + dates.firstYear + "&thirdParam="
				+ dates.secondMonth + "&fourthParam=" + dates.secondYear;

	}

	CFINAC.projecao.RemoveFromTable = function(index, type) {
		if (type == 0) {
			billsList.splice(index, 1);
			showBillsAndIncomes(billsList, type);
		} else {
			incomesList.splice(index, 1);
			showBillsAndIncomes(incomesList, type);
		}
		sum();
	}

	CFINAC.projecao.add = function(type) {
		var desc = $(type == 0 ? "#billsForm #description" : "#incomesForm #description").val(), 
			value = $(type == 0 ? "#billsForm #value" : "#incomesForm #value").val();
			fValue = value.replace(/,/g, "");
		if (desc == "" || desc == null || !+fValue) {
        	CFINAC.Message("Preencha os campos corretamentes!", "error");
			return false;
		}

		var newProjection = {};
		newProjection.description = desc;
		newProjection.totalValue = fValue;
		newProjection.formatedTotalValue = "R$ "+value;

		if (type == 0) {
			billsList.push(newProjection)
			showBillsAndIncomes(billsList, type);
		} else {
			incomesList.push(newProjection)
			showBillsAndIncomes(incomesList, type);
		}
		sum();
    	CFINAC.Message("Adicionado!", "success");
	}

	CFINAC.projecao.update = function() {
		CFINAC.projecao.getBills();
		$("#dateGraph").html(firstDate + " até " + secondDate);
	}
	CFINAC.projecao.update();

}
//# sourceURL = projections.js