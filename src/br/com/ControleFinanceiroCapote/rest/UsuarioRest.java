package br.com.ControleFinanceiroCapote.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;

import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.objetos.DataTable;
import br.com.ControleFinanceiroCapote.objetos.Usuario;
import br.com.ControleFinanceiroCapote.servicos.UsuarioService;

@Path("usuario")
public class UsuarioRest extends UtilRest {

	public UsuarioRest() {

	}


	UsuarioService service = new UsuarioService();

	@Context
	HttpServletRequest request = null;
	
	public int userId() {
		return Integer.parseInt((String) request.getSession().getAttribute("id"));
	}
	
	@POST
	@Path("/add")
	@Consumes("application/*")
	@Produces("text/plain")

	public Response addContato(String usuarioParam) {
		try {
			Usuario usuario = new ObjectMapper().readValue(usuarioParam, Usuario.class);
			service.AddUser(usuario);
			return this.buildResponse("Operação feita com sucesso!");
		} catch (Exception e) {
			return this.buildErrorResponse("Ocorreu um erro ao fazer a operação!");
		}
	}

	@POST
	@Path("/getUserById/{id}")
	@Produces({ MediaType.APPLICATION_JSON })

	public Response getUserById(@PathParam("id") int id) {
		try {
			return this.buildResponse(service.GetUsersById(id));
		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}

	@POST
	@Path("/deletaUsuario/{id}")
	public Response deletaUsuario(@PathParam("id") int id) throws ValidationException {
		try {
			service.DeleteUser(id);
			return this.buildResponse("Usu�rio deletado com sucesso.");
		} catch (Exception e) {
			return this.buildErrorResponse("N�o foi poss�vel deletar o usu�rio.");
		}
	}

	@POST
	@Path("/ativaUsuario/{id}")
	public Response ativaUsuario(@PathParam("id") int id) throws ValidationException {
		try {
			service.ActiveUser(id);
			return this.buildResponse("Usu�rio ativado com sucesso.");
		} catch (Exception e) {
			return this.buildErrorResponse("N�o foi poss�vel ativar o usu�rio.");
		}
	}

	@GET
	@Produces("text/plain")
	public Response getUsers() {
		try {
			return this.buildResponse(new DataTable(service.GetUsers("")));
		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}

	@POST
	@Path("/getFamilies")
	@Produces("text/plain")
	public Response getFamilies() {
		try {
			return this.buildResponse(service.GetFamilies());
		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}

	@POST
	@Path("/getUserInfo")
	@Produces("text/plain")
	public Response getUserInfo() {
		try {
			Integer idUser = userId();
			return idUser == null ? null : this.buildResponse(service.getUserInfo(idUser));
		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}

}
