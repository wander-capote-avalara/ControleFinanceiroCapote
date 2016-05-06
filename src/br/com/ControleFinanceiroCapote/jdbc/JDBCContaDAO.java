package br.com.ControleFinanceiroCapote.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.mysql.jdbc.Statement;

import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.jdbcinterface.ContaDAO;
import br.com.ControleFinanceiroCapote.objetos.Conta;

public class JDBCContaDAO implements ContaDAO {

	private Connection conexao;

	public JDBCContaDAO(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(Conta conta) {
		if (conta.getId() == 0) {
			StringBuilder comando = new StringBuilder();
			comando.append("INSERT INTO contas ");
			comando.append(
					"(Id_Categoria, Id_Usuario, Descricao_Contas, Valor_Contas, Status_Conta, Data_Vencimento, ");
			comando.append(conta.getHasDeadline() == 0 ? "Vezes" : "Conta_Fixa");
			comando.append(") VALUES(?,?,?,?,?,?,?)");

			PreparedStatement p;
			ResultSet rs = null;

			try {
				p = this.conexao.prepareStatement(comando.toString(), Statement.RETURN_GENERATED_KEYS);
				p.setInt(1, conta.getCategoria());
				p.setInt(2, conta.getUserId());
				p.setString(3, conta.getDescription());
				p.setInt(4, conta.getTotalValue());
				p.setInt(5, 1);
				p.setDate(6, (Date) conta.getStartDate());
				p.setInt(7, conta.getHasDeadline() == 0 ? conta.getTimes() : conta.getHasDeadline());
				p.execute();
				rs = p.getGeneratedKeys();
				if (rs.next()) {
					conta.setId(rs.getInt(1));

					if (conta.getTimes() != 0) {
						insertParcels(conta.getId(), conta.getTimes(), conta.getTotalValue());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			StringBuilder comando = new StringBuilder();
			comando.append("UPDATE contas ");
			comando.append(
					"SET Id_Categoria = ?, Id_Usuario = ?, Descricao_contas = ?, Valor_contas = ?, Status_conta = ?, Data_Vencimento = ?, ");
			comando.append(conta.getHasDeadline() == 0 ? "Vezes = ?" : "Conta_Fixa = ?");
			comando.append(" WHERE Id_contas = ?");

			PreparedStatement p;
			ResultSet rs = null;

			try {
				p = this.conexao.prepareStatement(comando.toString(), Statement.RETURN_GENERATED_KEYS);
				p.setInt(1, conta.getCategoria());
				p.setInt(2, conta.getUserId());
				p.setString(3, conta.getDescription());
				p.setInt(4, conta.getTotalValue());
				p.setInt(5, 1);
				p.setDate(6, (Date) conta.getStartDate());
				p.setInt(7, conta.getHasDeadline() == 0 ? conta.getTimes() : conta.getHasDeadline());
				p.setInt(8, conta.getId());
				p.execute();
				rs = p.getGeneratedKeys();
				if (rs.next()) {
					if (conta.getTimes() != 0) {
						insertParcels(conta.getId(), conta.getTimes(), conta.getTotalValue());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void insertParcels(int billIDd, int times, int totalValue) {
		int parcelValue = totalValue / times;
		StringBuilder comando = new StringBuilder();
		comando.append("INSERT INTO parcela_conta");
		comando.append("(Id_Conta, Valor_Parcela, Status_Parcela)");
		comando.append(" VALUES ");
		comando.append("(?,?,?)");

		PreparedStatement p;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, billIDd);
			p.setInt(2, parcelValue);
			p.setInt(3, 1);

			for (int x = 0; x < times; x++) {
				p.execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean deletaConta(int id) throws ValidationException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Conta> getBills(int id, int userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
