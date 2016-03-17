package br.com.ControleFinanceiroCapote.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;

import br.com.ControleFinanceiroCapote.servicos.FamiliaService;
import br.com.ControleFinanceiroCapote.servicos.UsuarioService;

import br.com.ControleFinanceiroCapote.objetos.DataTable;
import br.com.ControleFinanceiroCapote.objetos.Familia;
import br.com.ControleFinanceiroCapote.objetos.Usuario;

@Path("familia")
public class FamiliaRest extends UtilRest {

	public FamiliaRest() {

	}

	UsuarioService serviceUser = new UsuarioService();
	FamiliaService serviceFamily = new FamiliaService();
	
	@POST
	@Path("/add")
	@Consumes("application/*")
	@Produces("text/plain")

	public Response addFamilia(String familiaParam) {
		try {
			Familia family = new ObjectMapper().readValue(familiaParam, Familia.class);
			serviceFamily.addfamily(family);
			return this.buildResponse("Operação feita com sucesso!");
		} catch (Exception e) {
			return this.buildErrorResponse("Ocorreu um erro ao fazer a operação!");
		}
	}

	@GET
	@Produces("text/plain")
	public Response getUsers(@QueryParam("search") String text) {
		try {
			return this.buildResponse(serviceUser.GetUsers(text));
		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@GET
	@Path("/getFamilies")
	@Produces("text/plain")
	public Response getFamilies() {
		try {
			return this.buildResponse(serviceFamily.getFamilies());
		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	
}