package br.com.ControleFinanceiroCapote.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;

import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.objetos.Familia;
import br.com.ControleFinanceiroCapote.objetos.Invite;
import br.com.ControleFinanceiroCapote.servicos.FamiliaService;
import br.com.ControleFinanceiroCapote.servicos.UsuarioService;

@Path("familia")
public class FamiliaRest extends UtilRest {

	public FamiliaRest() {

	}

	UsuarioService serviceUser = new UsuarioService();
	FamiliaService serviceFamily = new FamiliaService();
	
	@POST
	@Path("/createFamily")
	@Consumes("application/*")
	@Produces("text/plain")

	public Response createFamily(String familiaParam) {
		try {
			Familia family = new ObjectMapper().readValue(familiaParam, Familia.class);			
			setFamilyId(serviceFamily.createFamily(family, userId()));
			return this.buildResponse("Operação feita com sucesso!");
		} catch (Exception e) {
			return this.buildErrorResponse("Erro na operação!");
		}
	}
	
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
			return this.buildErrorResponse("Insira usuários válidos!");
		}
	}
	
	@POST
	@Path("/inviteUsers")
	@Consumes("application/*")
	@Produces("text/plain")

	public Response inviteUsers(String inviteParam) {
		try {
			Invite invite = new ObjectMapper().readValue(inviteParam, Invite.class);
			invite.setFamilyId(familyId());
			serviceFamily.inviteUsers(invite, userId());
			return this.buildResponse("Operação feita com sucesso!");
		} catch (Exception e) {
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	
	@GET
	@Path("/getInvites")
	@Produces("text/plain")
	public Response getInvites() {
		try {
			return this.buildResponse(serviceFamily.getInvites(userId()));
		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@GET
	@Path("/getInvitesInfo")
	@Produces("text/plain")
	public Response getInvitesInfo() {
		try {
			return this.buildResponse(serviceFamily.getInvitesInfo(userId()));
		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
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
	
	@POST
	@Path("/getUserById/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getFamiliesById(@PathParam("id") int id) {
		try {
			return this.buildResponse(serviceFamily.getFamilies(id));
		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@POST
	@Path("/declineInvite/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response declineInvite(@PathParam("id") int id) {
		try {
			serviceFamily.declineInvite(id, userId());
			return this.buildResponse("Convite recusado com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@POST
	@Path("/acceptInvite/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response acceptInvite(@PathParam("id") int id) {
		try {
			serviceFamily.acceptInvite(id, userId());	
			setFamilyId(id);
			return this.buildResponse("Convite aceitado com sucesso!");
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
			return this.buildResponse(serviceFamily.getFamilies(0));
		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@POST
	@Path("/deletaFamilia/{id}")
	public Response deletaFamilia(@PathParam("id") int id) throws ValidationException {
		try {
			serviceFamily.deleteFamily(id);
			return this.buildResponse("Família deletada com sucesso.");
		} catch (Exception e) {
			return this.buildErrorResponse("Não foi possível deletar a família.");
		}
	}
	
	@POST
	@Path("/leadProvider/{id}")
	public Response leadProvider(@PathParam("id") int id) {
		try {
			serviceFamily.leadProvider(id, userId());
			return this.buildResponse("O usuário agora é o lider da família!");
		} catch (Exception e) {
			return this.buildErrorResponse("Você precisa ser dono da familia para fazer essa operação!");
		}
	}
	
	@POST
	@Path("/getFamilyById/{id}")
	@Produces({ MediaType.APPLICATION_JSON })

	public Response getFamilyById(@PathParam("id") int id) {
		try {
			/*Familia teste = serviceFamily.getFamilyById(id);
			return this.buildResponse(teste);*/
			return this.buildResponse(serviceFamily.getFamilyById(id));
		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@GET
	@Path("/getFamilyMembers")
	@Produces("text/plain")
	public Response getFamilyMembers() throws ValidationException {
		try {
			return this.buildResponse(serviceFamily.getFamilyMembers(userId(), familyId()));
		} catch (Exception e) {
			return this.buildErrorResponse("Erro ao buscar informações");
		}
	}
	
	@POST
	@Path("/kickUser/{id}")
	public Response kickUser(@PathParam("id") int id) {
		try {
			serviceFamily.kickUser(id, userId());
			return this.buildResponse("Usuario expulso com sucesso!");
		} catch (Exception e) {
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@GET
	@Path("/getAllFamilyBills/")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getAllFamilyBills() {
		try {
			return this.buildResponse(serviceFamily.getAllFamilyBills(userId(), familyId()));
		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@GET
	@Path("/getAllFamilyIncomes/")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getAllFamilyIncomes() {
		try {
			return this.buildResponse(serviceFamily.getAllFamilyIncomes(userId(), familyId()));
		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@GET
	@Path("/hasFamily/")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response hasFamily() {
		try {
			return this.buildResponse(serviceFamily.hasFamily(userId()));
		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
}
