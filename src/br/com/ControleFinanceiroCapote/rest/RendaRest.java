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
import br.com.ControleFinanceiroCapote.objetos.Renda;
import br.com.ControleFinanceiroCapote.servicos.RendaService;

@Path("renda")
public class RendaRest extends UtilRest  {
	
	public RendaRest(){
		
	}
	
	RendaService serviceRenda = new RendaService();
	
	@Context
	HttpServletRequest request = null;
	
	public int userId() {
		return Integer.parseInt((String) request.getSession().getAttribute("id"));
	}
	
	@POST
	@Path("/add")
	@Consumes("application/*")
	@Produces("text/plain")

	public Response addRenda(String rendaParam) {
		try {
			Renda renda = new ObjectMapper().readValue(rendaParam, Renda.class);
			renda.setUserId(userId());
			serviceRenda.addRenda(renda);
			return this.buildResponse("Operação feita com sucesso!");
		} catch (Exception e) {
			return this.buildErrorResponse("Insira usuários válidos!");
		}
	}
	
	@POST
	@Path("/deletaRenda/{id}")
	public Response deletaRenda(@PathParam("id") int id) throws ValidationException {
		try {
			serviceRenda.deletaRenda(id);
			return this.buildResponse("Renda deletada com sucesso.");
		} catch (Exception e) {
			return this.buildErrorResponse("Não foi possível deletar a renda.");
		}
	}
	
	@GET
	@Path("/getIncomes/{id}")
	@Produces("text/plain")
	public Response getIncomes(@PathParam("id") int id) {
		try {
			return this.buildResponse(serviceRenda.getIncomes(id, userId()));
		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@GET
	@Path("/getParcelsById/{id}")
	@Produces("text/plain")
	public Response getParcelsById(@PathParam("id") int id) {
		try {
			return this.buildResponse(serviceRenda.getParcelsById(id));
		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
}
