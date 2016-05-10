package br.com.ControleFinanceiroCapote.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;

import br.com.ControleFinanceiroCapote.objetos.Categoria;
import br.com.ControleFinanceiroCapote.objetos.Conta;
import br.com.ControleFinanceiroCapote.servicos.CategoriaService;
import br.com.ControleFinanceiroCapote.servicos.UsuarioService;

@Path("categoria")
public class CategoriaRest extends UtilRest {
	public CategoriaRest() {

	}
		
	@Context
	HttpServletRequest request = null;
	
	public int userId() {
		return Integer.parseInt((String) request.getSession().getAttribute("id"));
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
	
	@POST
	@Path("/add")
	@Consumes("application/*")
	@Produces("text/plain")
	
	public Response addConta(String CategoryParam) {
		try {
			Categoria Categoria = new ObjectMapper().readValue(CategoryParam, Categoria.class);
			categ.addCategory(Categoria, userId());
			return this.buildResponse("Operação feita com sucesso!");
		} catch (Exception e) {
			return this.buildErrorResponse("Erro na ação!");
		}
	}
}
