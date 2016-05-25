package br.com.ControleFinanceiroCapote.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import br.com.ControleFinanceiroCapote.servicos.FluxoCaixaService;

@Path("cashFlow")
public class FluxoCaixa extends UtilRest {

	public FluxoCaixa() {

	}
	
	FluxoCaixaService cashFlow = new FluxoCaixaService();
	
	@Context
	HttpServletRequest request = null;

	public int userId() {
		return Integer.parseInt((String) request.getSession().getAttribute("id"));
	}
	
	@GET
	@Path("/getRentAndIncome/{dates}")
	@Produces("text/plain")
	public Response getIncomes(@PathParam("dates") Object dates) {
		try {
			return this.buildResponse(cashFlow.prepDates(dates, userId()));
		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
}
