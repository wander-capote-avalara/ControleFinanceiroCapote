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
import br.com.ControleFinanceiroCapote.objetos.Conta;
import br.com.ControleFinanceiroCapote.servicos.ContaService;

@Path("conta")
public class ContaRest extends UtilRest  {
	
	public ContaRest(){
		
	}
	
	ContaService serviceConta = new ContaService();
	
	@Context
	HttpServletRequest request = null;
	
	public int userId() {
		return Integer.parseInt((String) request.getSession().getAttribute("id"));
	}
	
	@POST
	@Path("/add")
	@Consumes("application/*")
	@Produces("text/plain")

	public Response addConta(String ContaParam) {
		try {
			Conta Conta = new ObjectMapper().readValue(ContaParam, Conta.class);
			Conta.setUserId(userId());
			serviceConta.addConta(Conta);
			return this.buildResponse("Operação feita com sucesso!");
		} catch (Exception e) {
			return this.buildErrorResponse("Erro na ação!");
		}
	}
	
	@POST
	@Path("/deletaConta/{id}")
	public Response deletaConta(@PathParam("id") int id) throws ValidationException {
		try {
			serviceConta.deletaConta(id);
			return this.buildResponse("Conta deletada com sucesso.");
		} catch (Exception e) {
			return this.buildErrorResponse("Não foi possível deletar a Conta.");
		}
	}
	
	@GET
	@Path("/getBills/{id}")
	@Produces("text/plain")
	public Response getIncomes(@PathParam("id") int id) {
		try {
			return this.buildResponse(serviceConta.getBills(id, userId()));
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
			return this.buildResponse(serviceConta.getParcelsById(id));
		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
}
