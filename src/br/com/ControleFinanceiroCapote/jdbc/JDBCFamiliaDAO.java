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
		
		beforeInsert(idFamilia);
		
		StringBuilder comando = new StringBuilder();

		comando.append("INSERT INTO user_family (Familia_Id, Usuario_Id)");
		comando.append("VALUES (?, ?)");

		PreparedStatement p;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			for (Integer id : users) {
				p.setInt(1, idFamilia);
				p.setInt(2, id);
				p.execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void beforeInsert(int id) {
		StringBuilder comando = new StringBuilder();

		comando.append("DELETE FROM user_family ");
		comando.append("WHERE Familia_Id = ?");
		
		PreparedStatement p;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, id);

			p.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Familia> getFamily() {
		StringBuilder comando = new StringBuilder();
		comando.append("SELECT fa.Id_Familias as idfamilia, ");
		comando.append("fa.Nome as nomeFamilia, us.Usuario as owner FROM familias fa ");
		comando.append("INNER JOIN user_family uf on uf.Familia_Id = fa.Id_Familias ");
		comando.append("INNER JOIN usuarios us ON us.Id_Usuarios = uf.Usuario_Id ");
		comando.append("WHERE fa.Id_Usuario = uf.Usuario_Id ");

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
					fam.setUsersName(getUserByFamilyId(fam.getId()));
				} catch (Exception e) {
					fam.setUsersName("Sem integrantes");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listFamilias;
	}

	public String getUserByFamilyId(int id) {

		StringBuilder comando2 = new StringBuilder();
		comando2.append("SELECT uss.Usuario as user from usuarios uss ");
		comando2.append("INNER join user_family uf on uf.Usuario_Id = uss.Id_Usuarios ");
		comando2.append("INNER JOIN familias faa ON faa.Id_Familias = uf.Familia_Id ");
		if (id != 0) {
			comando2.append("WHERE faa.Id_Familias = " + id);
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
			user += ", ";
			user += names.get(i);
		}

		return user;
	}
}