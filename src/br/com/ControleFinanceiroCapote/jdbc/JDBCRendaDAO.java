package br.com.ControleFinanceiroCapote.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.omg.CORBA.PUBLIC_MEMBER;

import com.mysql.jdbc.Statement;

import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.jdbcinterface.RendaDAO;
import br.com.ControleFinanceiroCapote.objetos.Categoria;
import br.com.ControleFinanceiroCapote.objetos.Familia;
import br.com.ControleFinanceiroCapote.objetos.Parcela;
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
				p.setDate(6, (Date) renda.getStartDate());
				p.setInt(7, renda.getIsFixed() == 0 ? renda.getTimes() : renda.getIsFixed());
				p.execute();
				rs = p.getGeneratedKeys();
				if (rs.next()) {
					renda.setId(rs.getInt(1));

					if (renda.getTimes() != 0) {
						insertParcels(renda.getId(), renda.getTimes(), renda.getTotalValue(), renda.getParcelValue());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			StringBuilder comando = new StringBuilder();
			comando.append("UPDATE rendas ");
			comando.append("SET Id_Categoria = ?, Id_Usuario = ?, Descricao_Rendas = ?, Valor_Rendas = ?, Status_Renda = ?, Data_Vencimento = ?, ");
			comando.append(renda.getIsFixed() == 0 ? "Vezes = ?" : "Renda_Fixa = ?");
			comando.append(" WHERE Id_Rendas = ?");

			PreparedStatement p;
			ResultSet rs = null;

			try {
				p = this.conexao.prepareStatement(comando.toString(), Statement.RETURN_GENERATED_KEYS);
				p.setInt(1, renda.getCategoria());
				p.setInt(2, renda.getUserId());
				p.setString(3, renda.getDescription());
				p.setInt(4, renda.getTotalValue());
				p.setInt(5, 1);
				p.setDate(6, (Date) renda.getStartDate());
				p.setInt(7, renda.getIsFixed() == 0 ? renda.getTimes() : renda.getIsFixed());
				p.setInt(8, renda.getId());
				p.execute();
					if (renda.getTimes() != 0) {
						deleteParcels(renda.getId());
						insertParcels(renda.getId(), renda.getTimes(), renda.getTotalValue(), renda.getParcelValue());
					}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public boolean deletaRenda(int id) throws ValidationException {
		
		deleteParcels(id);
		
		StringBuilder comando = new StringBuilder();
		comando.append("UPDATE rendas ");
		comando.append("SET Status_Renda = 0 ");
		comando.append("WHERE Id_Rendas = ?");	
		PreparedStatement p;	
		
		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, id);
			p.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void deleteParcels(int id) {
		StringBuilder comando = new StringBuilder();
		
		comando.append("UPDATE parcela_renda ");
		comando.append("SET Status_Parcela = 0 ");
		comando.append("WHERE Id_Renda = ?");
		PreparedStatement p;
		
		try {
			
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, id);
			p.execute();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void insertParcels(int incomeId, int times, double totalValue, double parcelValue) {
		double math = ((parcelValue*times) - totalValue);
		boolean hasDifference = (parcelValue*times) - totalValue != 0;
		
		StringBuilder comando = new StringBuilder();
		comando.append("INSERT INTO parcela_renda");
		comando.append("(Id_Renda, Valor_Parcela, Status_Parcela)");
		comando.append(" VALUES ");
		comando.append("(?,?,?)");

		PreparedStatement p;

		try {
			p = this.conexao.prepareStatement(comando.toString());

			for (int x = 0; x < times; x++) {
				p.setInt(1, incomeId);
				p.setDouble(2, hasDifference ? parcelValue - math: parcelValue);
				p.setInt(3, 1);
				p.execute();
				hasDifference = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Renda> getIncomes(int id, int userId) {
		StringBuilder comando = new StringBuilder();
		
		comando.append("SELECT r.Id_Rendas as id, r.Id_Categoria as categoryId, ");
		comando.append("r.Descricao_Rendas as descr, r.Valor_Rendas as totalValue, r.Status_Renda as status, ");
		comando.append("r.Data_Vencimento as endDate, r.Renda_Fixa as isFixed, r.Vezes as x ");
		comando.append("FROM rendas r ");
		comando.append("WHERE r.Id_Usuario = "+ userId);
		comando.append(" AND ");
		comando.append("Status_Renda = 1");
		if (id != 0) {
			comando.append(" AND ");
			comando.append("r.Id_Rendas = " + id);
		}

		List<Renda> incomeList = new ArrayList<Renda>();
		Renda income = null;
		try {
			java.sql.Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando.toString());
			while (rs.next()) {
				income = new Renda();				

				income.setId(rs.getInt("id"));
				income.setCategoria(rs.getInt("categoryId"));
				income.setDescription(rs.getString("descr"));
				income.setTotalValue(rs.getInt("totalValue"));
				income.setStatus(rs.getInt("status"));
				income.setStartDate( rs.getDate("endDate"));
				income.setIsFixed(rs.getInt("isFixed"));
				income.setTimes(rs.getInt("x"));
				
				incomeList.add(income);
			}

			for (Renda inc : incomeList) {
				try {
					inc.setCategoriaName(getCategoriesName(inc.getCategoria()));
				} catch (Exception e) {
					inc.setCategoriaName("Não há categoria");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return incomeList;
	}

	private String getCategoriesName(int categoria) throws SQLException {
		StringBuilder comando = new StringBuilder();
		comando.append("SELECT Descricao as descr ");
		comando.append("FROM categorias ");
		comando.append("WHERE Id_Categorias = "+ categoria);

		try {		
			java.sql.Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando.toString());

			while (rs.next()) {
				return rs.getString("descr");		
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}

	public List<Parcela> getParcelsById(int id) {
		StringBuilder comando = new StringBuilder();

		comando.append("SELECT Id_Renda as id, Valor_Parcela as parcelValue, Status_Parcela as parcelStatus, ");
		comando.append("Data_Pagamento as paymentDate, Data_Vencimento as dueDate ");
		comando.append("FROM parcela_renda WHERE Id_Renda = "+id);
		comando.append(" AND Status_Parcela <> 0");

		List<Parcela> parcelList = new ArrayList<Parcela>();
		Parcela parcel = null;
		try {
			java.sql.Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando.toString());
			while (rs.next()) {
				parcel = new Parcela();
				
				parcel.setId(rs.getInt("id"));
				parcel.setParcelValue(rs.getDouble("parcelValue"));
				parcel.setStatus(rs.getInt("parcelStatus"));
				parcel.setPaymentDate(rs.getDate("paymentDate"));
				parcel.setDueDate(rs.getDate("dueDate"));

				parcelList.add(parcel);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return parcelList;
	}

}
