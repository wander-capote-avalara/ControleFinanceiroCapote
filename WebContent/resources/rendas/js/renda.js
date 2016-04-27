CFINAC.rendas = new Object();

iniciaRenda = function() {
	alert("success");
	$(document).ready(function() {
		
		CFINAC.rendas.add = function(){
			var cfg;
			var msg, categoria = $("#inputCategory").val(),
			    description = $("#inputDescription").val(),
			    endDate = $("#inputOverdueDate").val(),
			    startDate = $("#inputInitialDate").val(),
			    isFixed = $("#isFixed").val(),
			    totalValue = $("#inputTotalValue").val(),
			    times = $("#inputTimes").val(),
			    id = $("#id").val();
			
			if(description != "" && endDate != "" && startDate != "" && totalValue != "")
			{
				var newIncome = new Object();
				newIncome.id = id;
				newIncome.description = description;
				newIncome.startDate = startDate;
				newIncome.endDate = endDate;
				newIncome.isFixed = isFixed;
				newIncome.times = times;
				newIncome.categoria = categoria;
				newIncome.totalValue = totalValue;
			
				cfg = {
					url : "../rest/renda/add",
					data : newIncome,
					success : function(r){
						alertPopUp(r);
					}, error : function(err){
						alert("Erro na ação"+ err.responseText);
					}
				};
				CFINAC.ajax.post(cfg);
			}
		}

	})
};
//# aleluiasinho.coffee
