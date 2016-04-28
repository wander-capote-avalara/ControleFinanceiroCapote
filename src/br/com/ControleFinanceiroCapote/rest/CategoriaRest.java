package br.com.ControleFinanceiroCapote.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.com.ControleFinanceiroCapote.servicos.CategoriaService;

@Path("categoria")
public class CategoriaRest extends UtilRest {
	public CategoriaRest() {

	}
	
	CategoriaService categ = new CategoriaService();
	
	@POST
	@Path("/getCategories")
	@Produces("text/plain")
	public Response getCategories() {
		try {
			return this.buildResponse(categ.getCategories(0));
		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
}
