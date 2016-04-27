package br.com.ControleFinanceiroCapote.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.omg.CORBA.PUBLIC_MEMBER;

import com.mysql.jdbc.Statement;

import br.com.ControleFinanceiroCapote.jdbcinterface.RendaDAO;
import br.com.ControleFinanceiroCapote.objetos.Renda;

public class JDBCRendaDAO implements RendaDAO {

	private Connection conexao;

	public JDBCRendaDAO(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(Renda renda) {
		if (renda.getId() == 0) {
			StringBuilder comando = new StringBuilder();
			comando.append("INSERT INTO rendas ");
			comando.append("(Id_Categoria, Id_Usuario, Descricao_Rendas, Valor_Rendas, Status_Renda, Data_Vencimento, ");
			comando.append(renda.getIsFixed() == 0 ? "Vezes" : "Renda_Fixa");
			comando.append(") VALUES(?,?,?,?,?,?,?)");

			PreparedStatement p;
			ResultSet rs = null;

			try {
				p = this.conexao.prepareStatement(comando.toString(), Statement.RETURN_GENERATED_KEYS);
				p.setInt(1, renda.getCategoria());
				p.setInt(2, renda.getUserId());
				p.setString(3, renda.getDescription());
				p.setInt(4, renda.getTotalValue());
				p.setInt(5, 1);
				p.setDate(6, (Date) renda.getEndDate());
				p.setInt(7, renda.getIsFixed() == 0 ? renda.getTimes() : renda.getIsFixed());
				p.execute();
				rs = p.getGeneratedKeys();
				if (rs.next()) {
					renda.setId(rs.getInt(1));

					if (renda.getTimes() != 0) {
						insertParcels(renda.getId(), renda.getTimes(), renda.getTotalValue());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			/*
			 * StringBuilder comando = new StringBuilder(); comando.append(
			 * "UPDATE rendas set Nome = ?, Id_Usuario = ? "); comando.append(
			 * "WHERE Id_Familias = ?");
			 * 
			 * PreparedStatement p; ResultSet rs = null;
			 * 
			 * try { p = this.conexao.prepareStatement(comando.toString());
			 * p.setString(1, family.getName()); p.setString(2,
			 * family.getOwner()); p.setInt(3, family.getId()); p.execute();
			 * 
			 * updateFamilies(family.getUsers(), family.getId()); } catch
			 * (Exception e) { e.printStackTrace(); }
			 */
		}
	}

	public void insertParcels(int incomeId, int times, int totalValue) {
		int parcelValue = totalValue / times;
		StringBuilder comando = new StringBuilder();
		comando.append("INSERT INTO parcela_renda");
		comando.append("(Id_Renda, Valor_Parcela, Status_Parcela)");
		comando.append(" VALUES ");
		comando.append("(?,?,?)");

		PreparedStatement p;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, incomeId);
			p.setInt(2, parcelValue);
			p.setInt(3, 1);

			for (int x = 0; x < times; x++) {
				p.execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
