package br.com.ControleFinanceiroCapote.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;

import br.com.ControleFinanceiroCapote.excecao.ValidationException;
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
	
	@GET
	@Path("/getCategories/{id}")
	@Produces("text/plain")
	public Response getCategories(@PathParam("id") int id) {
		try {
			return this.buildResponse(categ.getCategories(id, userId()));
		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@POST
	@Path("/add")
	@Consumes("application/*")
	@Produces("text/plain")
	
	public Response addCategoria(String CategoryParam) {
		try {
			Categoria Categoria = new ObjectMapper().readValue(CategoryParam, Categoria.class);
			categ.addCategory(Categoria, userId());
			return this.buildResponse("Operação feita com sucesso!");
		} catch (Exception e) {
			return this.buildErrorResponse("Erro na ação!");
		}
	}
	
	@POST
	@Path("/deletaCategoria/{id}")
	public Response deletaCategoria(@PathParam("id") int id) throws ValidationException {
		try {
			categ.deleteCategory(id);
			return this.buildResponse("Categoria deletada com sucesso.");
		} catch (Exception e) {
			return this.buildErrorResponse("Não foi possível deletar a categoria.");
		}
	}
}
