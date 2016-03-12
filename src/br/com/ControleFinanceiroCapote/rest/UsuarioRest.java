package br.com.ControleFinanceiroCapote.rest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.gson.Gson;

import br.com.ControleFinanceiroCapote.bd.conexao.Conexao;
import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.jdbc.JDBCUsuarioDAO;
import br.com.ControleFinanceiroCapote.objetos.DataTable;
import br.com.ControleFinanceiroCapote.objetos.Familia;
import br.com.ControleFinanceiroCapote.objetos.Usuario;
import br.com.ControleFinanceiroCapote.servicos.UsuarioService;

import br.com.ControleFinanceiroCapote.helpers.Helper;

@Path("usuario")
public class UsuarioRest extends UtilRest {

	public UsuarioRest() {

	}

	UsuarioService service = new UsuarioService();

	@POST
	@Path("/add")
	@Consumes("application/*")
	@Produces("text/plain")

	public Response addContato(String usuarioParam) {
		try {
			Usuario usuario = new ObjectMapper().readValue(usuarioParam, Usuario.class);
			service.AddUser(usuario);
			return this.buildResponse("Opera��o feita com sucesso!");
		} catch (Exception e) {
			return this.buildErrorResponse("Ocorreu um erro ao fazer a opera��o!");
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

}
