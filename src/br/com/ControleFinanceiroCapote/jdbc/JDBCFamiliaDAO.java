package br.com.ControleFinanceiroCapote.jdbc;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.jasper.tagplugins.jstl.core.ForEach;

import com.mysql.jdbc.Statement;
import com.sun.javafx.scene.layout.region.Margins.Converter;

import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.jdbcinterface.FamiliaDAO;
import br.com.ControleFinanceiroCapote.objetos.Familia;
import br.com.ControleFinanceiroCapote.objetos.Usuario;

public class JDBCFamiliaDAO implements FamiliaDAO {

	private Connection conexao;

	public JDBCFamiliaDAO(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(Familia family) throws ValidationException {
		if (family.getId() == 0) {
			String comando = "insert into familias " + "(Nome, Id_Usuario) " + "values(?,?)";

			PreparedStatement p;
			ResultSet rs = null;

			try {
				p = this.conexao.prepareStatement(comando, Statement.RETURN_GENERATED_KEYS);
				p.setString(1, family.getName());
				p.setString(2, family.getOwner());
				p.execute();
				rs = p.getGeneratedKeys();
				if (rs.next()) {
					family.setId(rs.getInt(1));
				}
				updateFamilies(family.getUsers(), family.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void updateFamilies(List<Integer> users, int idFamilia) {
		StringBuilder comando = new StringBuilder();
		int x = 2;

		comando.append("Update usuarios set Id_Familia=? ");
		comando.append("where Id_Usuarios in (");
		for (int i = 0; i < users.size(); i++) {
			if (i != 0) {
				comando.append(", ");
			}
			comando.append("?");
		}
		comando.append(")");

		PreparedStatement p;
		ResultSet rs = null;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, idFamilia);
			for (Integer id : users) {
				p.setInt(x, id);
				x++;
			}

			p.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Familia> getFamily() {
		StringBuilder comando = new StringBuilder();
		comando.append("SELECT fa.Id_Familias as idfamilia,");
		comando.append("fa.Nome as nomeFamilia, us.Usuario as owner FROM familias fa ");
		comando.append("INNER JOIN usuarios us ON us.Id_Familia = fa.Id_Familias");

		List<Familia> listFamilias = new ArrayList<Familia>();
		Familia familia = null;
		try {
			java.sql.Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando.toString());
			while (rs.next()) {
				familia = new Familia();
				int idFamily = rs.getInt("idfamilia");
				String nameFamily = rs.getString("nomeFamilia");
				String ownerFamily = rs.getString("owner");
				
				familia.setId(idFamily);
				familia.setName(nameFamily);
				familia.setOwner(ownerFamily);
				listFamilias.add(familia);
			}
			
			for (Familia fam : listFamilias) {
				try {
					familia.setUsersName(getUserByFamilyId(fam.getId()));
				} catch (Exception e) {
					familia.setUsersName("Sem integrantes");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listFamilias;
	}

	public String getUserByFamilyId(int id) {

		StringBuilder comando2 = new StringBuilder();
		comando2.append("SELECT us.Usuario as user FROM usuarios us ");
		comando2.append("LEFT JOIN familias fa ON us.Id_Familia = fa.Id_Familias ");
		if (id != 0) {
			comando2.append("WHERE fa.Id_Familias = "+id);
		}
		List<String> names = new ArrayList<String>();
		try {
			java.sql.Statement stmt = conexao.createStatement();
			ResultSet rs2 = stmt.executeQuery(comando2.toString());
			while (rs2.next()) {
				names.add(rs2.getString("user"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String user = names.get(0);
		for (int i = 1; i < names.size(); i++) {
			user += ",";
			user += names.get(i);
		}
		
		return user;
	}
}
