package br.com.ControleFinanceiroCapote.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.collections.ListUtils;

import com.mysql.jdbc.Statement;

import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.jdbcinterface.RendaDAO;
import br.com.ControleFinanceiroCapote.objetos.Graph;
import br.com.ControleFinanceiroCapote.objetos.Parcela;
import br.com.ControleFinanceiroCapote.objetos.RangeDTO;
import br.com.ControleFinanceiroCapote.objetos.Renda;
import br.com.ControleFinanceiroCapote.validacao.ValidaRenda;

public class JDBCRendaDAO implements RendaDAO {

	private Connection conexao;

	public JDBCRendaDAO(Connection conexao) {
		this.conexao = conexao;
	}

	ValidaRenda validar = new ValidaRenda();
	
	@Override
	public void inserir(Renda renda) throws ValidationException {
		if (renda.getId() == 0) {
			validar.insertValidation(renda);
			StringBuilder comando = new StringBuilder();
			comando.append("INSERT INTO rendas ");
			comando.append(
					"(Id_Categoria, Id_Usuario, Descricao_Rendas, Valor_Rendas, Status_Renda, Data_Vencimento, ");
			comando.append(renda.getIsFixed() == 0 ? "Vezes" : "Renda_Fixa");
			comando.append(") VALUES(?,?,?,?,?,?,?)");

			PreparedStatement p;
			ResultSet rs = null;

			try {
				p = this.conexao.prepareStatement(comando.toString(), Statement.RETURN_GENERATED_KEYS);
				p.setInt(1, renda.getCategoria());
				p.setInt(2, renda.getUserId());
				p.setString(3, renda.getDescription());
				p.setDouble(4, renda.getTotalValue());
				p.setInt(5, 1);
				p.setDate(6, (Date) renda.getStartDate());
				p.setInt(7, renda.getIsFixed() == 0 ? renda.getTimes() : renda.getIsFixed());
				p.execute();
				rs = p.getGeneratedKeys();
				if (rs.next()) {
					renda.setId(rs.getInt(1));

					if (renda.getTimes() != 0) {
						insertParcels(renda.getId(), renda.getTimes(), renda.getTotalValue(), renda.getParcelValue(), (Date) renda.getStartDate());
					}
				}
			} catch (Exception e) {
				throw new ValidationException(e);
			}
		} else {
			validar.updateValidation(renda);
			StringBuilder comando = new StringBuilder();
			comando.append("UPDATE rendas ");
			comando.append(
					"SET Id_Categoria = ?, Id_Usuario = ?, Descricao_Rendas = ?, Valor_Rendas = ?, Status_Renda = ?, Data_Vencimento = ?, ");
			comando.append(renda.getIsFixed() == 0 ? "Vezes = ?" : "Renda_Fixa = ?");
			comando.append(" WHERE Id_Rendas = ?");

			PreparedStatement p;
			ResultSet rs = null;

			try {
				p = this.conexao.prepareStatement(comando.toString(), Statement.RETURN_GENERATED_KEYS);
				p.setInt(1, renda.getCategoria());
				p.setInt(2, renda.getUserId());
				p.setString(3, renda.getDescription());
				p.setDouble(4, renda.getTotalValue());
				p.setInt(5, 1);
				p.setDate(6, (Date) renda.getStartDate());
				p.setInt(7, renda.getIsFixed() == 0 ? renda.getTimes() : renda.getIsFixed());
				p.setInt(8, renda.getId());
				p.execute();
				if (renda.getTimes() != 0) {
					deleteParcels(renda.getId());
					insertParcels(renda.getId(), renda.getTimes(), renda.getTotalValue(), renda.getParcelValue(), (Date) renda.getStartDate());
				}
			} catch (Exception e) {
				throw new ValidationException(e);
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
			throw new ValidationException(e);
		}
		return true;
	}

	private void deleteParcels(int id) throws ValidationException {
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
			throw new ValidationException(e);
		}

	}

	public void insertParcels(int incomeId, int times, double totalValue, double parcelValue, Date startDate) throws ValidationException {
		double math = ((parcelValue * times) - totalValue);
		boolean hasDifference = (parcelValue * times) - totalValue != 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		
		StringBuilder comando = new StringBuilder();
		comando.append("INSERT INTO parcela_renda");
		comando.append("(Id_Renda, Valor_Parcela, Status_Parcela, Data_Vencimento)");
		comando.append(" VALUES ");
		comando.append("(?,?,?,?)");

		PreparedStatement p;

		try {
			p = this.conexao.prepareStatement(comando.toString());

			for (int x = 0; x < times; x++) {
				cal.add(Calendar.MONTH, 1);
				p.setInt(1, incomeId);
				p.setDouble(2, hasDifference ? parcelValue - math : parcelValue);
				p.setInt(3, 1);
				p.setDate(4, new Date(cal.getTimeInMillis()));//Calendar.getInstance().get(Calendar.MONTH) + 2
				p.execute();
				hasDifference = false;
			}
		} catch (Exception e) {
			throw new ValidationException(e);
		}
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<Renda> getIncomes(int id, int userId, RangeDTO range) throws ValidationException {
		StringBuilder comando = new StringBuilder();
		

		/*comando.append("SELECT r.Id_Rendas as id, r.Id_Categoria as categoryId, r.Descricao_Rendas as descr, ");
		comando.append("ifnull(a.Valor_Parcela ,r.Valor_Rendas) as totalValue, ifnull(a.Status_Parcela, r.Status_Renda) as status, ");
		comando.append("ifnull(a.Data_Vencimento, date(r.Data_Vencimento)) as endDate, r.Renda_Fixa as isFixed, r.Vezes as x ");
		comando.append("FROM rendas r ");
		comando.append("LEFT JOIN parcela_renda a ON r.Id_Rendas = a.Id_Renda AND a.Status_Parcela = 1");
		comando.append("WHERE r.Id_Usuario = "+userId+" AND r.Status_Renda = 1 ");
		comando.append("AND ifnull(a.Data_Vencimento, date(r.Data_Vencimento)) between date('2010/8/01') AND date('2016/8/31')");*/
		
		
		comando.append("SELECT r.Id_Rendas as id, r.Id_Categoria as categoryId, ");
		comando.append("r.Descricao_Rendas as descr, r.Valor_Rendas as totalValue, r.Status_Renda as status, ");
		comando.append("r.Data_Vencimento as endDate, r.Renda_Fixa as isFixed, r.Vezes as x, ca.Descricao as categ ");
		comando.append("FROM rendas r ");
		comando.append("INNER JOIN categorias ca ON ca.Id_Categorias = r.Id_Categoria ");
		comando.append("WHERE r.Id_Usuario = " + userId);
		comando.append(" AND ");
		comando.append("Status_Renda = 1");
		if (id != 0) {
			comando.append(" AND ");
			comando.append("r.Id_Rendas = " + id);
		}
		if (range != null) {
			comando.append(" AND ");
			comando.append(" r.renda_fixa = 1 ");
			comando.append(" AND ");
			comando.append(" MONTH(r.Data_Vencimento) <= "+range.getSecondMonth());
		}

		List<Renda> incomeList = new ArrayList<Renda>();
		SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
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
				income.setStartDate(rs.getDate("endDate"));
				income.setIsFixed(rs.getInt("isFixed"));
				income.setTimes(rs.getInt("x"));
				income.setFormatedDate(date.format(rs.getDate("endDate")).replace("-", "/"));
				income.setCategoriaName(rs.getString("categ"));

				incomeList.add(income);
			}
			
			if (range != null) {
				return ListUtils.union(incomeList, getIncomeParcels(userId, range));
			}
			
		} catch (Exception e) {
			throw new ValidationException(e);
		}
		return incomeList;
	}
	
	private List<Renda> getIncomeParcels(int id, RangeDTO dates) throws ValidationException {
		StringBuilder comando = new StringBuilder();
		comando.append("SELECT a.Valor_Parcela as vlrRenda, a.Status_Parcela as status, ca.Descricao as descr, c.Descricao_Rendas as dcs ");
		comando.append("FROM parcela_renda a ");
		comando.append("INNER JOIN rendas c ON c.Id_Rendas = a.Id_Renda  ");
		comando.append("INNER JOIN categorias ca ON ca.Id_Categorias = c.Id_Categoria  ");
		comando.append("WHERE a.Status_Parcela = 1 ");
		comando.append("AND a.Data_Vencimento between ? AND ? ");
		comando.append("AND c.Id_Usuario = ?");

		List<Renda> incomes = new ArrayList<Renda>();
		PreparedStatement p;
		ResultSet rs = null;
		try {
			
			p = this.conexao.prepareStatement(comando.toString());
			p.setString(1, dates.getFirstYear() + "/" + dates.getFirstMonth() + "/01");
			p.setString(2, dates.getSecondYear() + "/" + dates.getSecondMonth() + "/31");
			p.setInt(3, id);
			rs = p.executeQuery();
			
			while (rs.next()) {
				Renda income = new Renda();
					
				income.setTotalValue(rs.getDouble("vlrRenda"));
				income.setStatus(rs.getInt("status"));
				income.setCategoriaName(rs.getString("descr"));
				income.setDescription(rs.getString("dcs"));
				
				incomes.add(income);
			}
			
			return incomes;		
		} catch (SQLException e) {
			throw new ValidationException(e);
		}
	}

	private String getCategoriesName(int categoria) throws SQLException, ValidationException {
		StringBuilder comando = new StringBuilder();
		comando.append("SELECT Descricao as descr ");
		comando.append("FROM categorias ");
		comando.append("WHERE Id_Categorias = " + categoria);

		try {
			java.sql.Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando.toString());

			while (rs.next()) {
				return rs.getString("descr");
			}

		} catch (Exception e) {
			throw new ValidationException(e);
		}

		return null;

	}

	public List<Parcela> getParcelsById(int id) throws ValidationException {
		StringBuilder comando = new StringBuilder();

		comando.append("SELECT Id_Renda as id, Valor_Parcela as parcelValue, Status_Parcela as parcelStatus, ");
		comando.append("Data_Pagamento as paymentDate, Data_Vencimento as dueDate ");
		comando.append("FROM parcela_renda WHERE Id_Renda = " + id);
		comando.append(" AND Status_Parcela <> 0");

		List<Parcela> parcelList = new ArrayList<Parcela>();
		SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
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
				parcel.setFormatedDate(date.format(rs.getDate("dueDate")).replace("-", "/"));

				parcelList.add(parcel);
			}

		} catch (Exception e) {
			throw new ValidationException(e);
		}
		return parcelList;
	}

	public List<Graph> getIncomesByCategory(int userId, RangeDTO range) throws ValidationException {
		StringBuilder comando = new StringBuilder();

		comando.append("SELECT SUM(root.Valor_Rendas) as ValorTotal, c.Descricao as descricao ");
		comando.append("FROM rendas root ");
		comando.append("INNER JOIN categorias c ON c.Id_Categorias = root.Id_Categoria ");
		comando.append("WHERE root.Id_Usuario = ? ");
		comando.append("AND root.Data_Vencimento BETWEEN ? AND ? ");
		comando.append("AND root.Status_Renda = 1 ");
		comando.append("GROUP BY c.Id_Categorias ");

		PreparedStatement p;
		ResultSet rs = null;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, userId);
			p.setString(2, range.getFirstYear() + "/" + range.getFirstMonth() + "/01");
			p.setString(3, range.getSecondYear() + "/" + range.getSecondMonth() + "/31");
			rs = p.executeQuery();

			ArrayList<Graph> incomeList = new ArrayList<Graph>();

			while (rs.next()) {
				Graph newIncome = new Graph();

				newIncome.setName(rs.getString("descricao"));
				newIncome.setY(rs.getDouble("ValorTotal"));

				incomeList.add(newIncome);
			}

			return incomeList;

		} catch (Exception e) {
			throw new ValidationException(e);
		}
	}

	public int getTotalValueIncome(RangeDTO dates, int userId) throws ValidationException {
		StringBuilder comando = new StringBuilder();
		int sum = 0;

		comando.append("SELECT SUM(Valor_Rendas) as summ FROM rendas a ");
		comando.append("WHERE Id_Usuario = ? ");
		comando.append("AND Status_Renda = 1 ");
		comando.append("AND Renda_Fixa = 1 ");
		comando.append(" AND ");
		comando.append(" MONTH(Data_Vencimento) <= "+dates.getSecondMonth());

		PreparedStatement p;
		ResultSet rs = null;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, userId);
			rs = p.executeQuery();

			if (rs.next()) {
				sum = rs.getInt("summ");
			}
			
			sum += getIncomesParcelsValues(userId, dates);
			return sum;
		} catch (Exception e) {
			throw new ValidationException(e);
		}
	}
	
	private double getIncomesParcelsValues(int id, RangeDTO dates) throws ValidationException {
		StringBuilder comando = new StringBuilder();
		comando.append("SELECT SUM(Valor_Parcela) as vlrRenda FROM parcela_renda a ");
		comando.append("WHERE a.Status_Parcela = 1 AND a.Id_Renda IN (SELECT c.Id_Rendas FROM rendas c where c.Id_Usuario = ?) ");
		comando.append("AND a.Data_Vencimento BETWEEN ? AND ? ");

		double balance = 0;
		PreparedStatement p;
		ResultSet rs = null;
		try {
			
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, id);
			p.setString(2, dates.getFirstYear() + "/" + dates.getFirstMonth() + "/01");
			p.setString(3, dates.getSecondYear() + "/" + dates.getSecondMonth() + "/31");
			rs = p.executeQuery();
			
			while (rs.next()) {
				balance += (int)rs.getInt("vlrRenda");
			}
			
			return balance;		
		} catch (SQLException e) {
			throw new ValidationException(e);
		}
	}

	public List<Renda> getAllFamilyIncomes(int idFamily) throws ValidationException {
		StringBuilder comando = new StringBuilder();

		comando.append("SELECT r.Valor_Rendas AS incomeValue, u.Usuario AS Name, ");
		comando.append("ca.Descricao as Description, r.Data_Vencimento as incomeDate ");
		comando.append("FROM usuarios u ");
		comando.append("INNER JOIN user_family uf ON uf.Usuario_Id = u.Id_Usuarios ");
		comando.append("INNER JOIN rendas r ON r.Id_Usuario = uf.Usuario_Id ");
		comando.append("INNER JOIN categorias ca ON ca.Id_Categorias = r.Id_Categoria ");
		comando.append("WHERE uf.Familia_Id = ? AND r.Status_Renda = 1 ");
		comando.append("ORDER BY r.Data_Vencimento DESC");

		PreparedStatement p;
		ResultSet rs = null;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, idFamily);
			rs = p.executeQuery();
			List<Renda> incomes = new ArrayList<Renda>();
			SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
			while (rs.next()) {
				Renda newIncome = new Renda();

				newIncome.setTotalValue(rs.getDouble("incomeValue"));
				newIncome.setUserName(rs.getString("Name"));
				newIncome.setCategoriaName(rs.getString("Description"));
				newIncome.setFormatedDate(date.format(rs.getDate("incomeDate")).replace("-", "/"));

				incomes.add(newIncome);
			}

			return incomes;
		} catch (Exception e) {
			throw new ValidationException(e);
		}
	}

	public List<Graph> getFamilyIncomesTotalValue(int familyId) throws ValidationException {
		StringBuilder comando = new StringBuilder();

		comando.append("SELECT SUM(r.Valor_Rendas) AS IncomeValue, u.Usuario AS Name ");
		comando.append("FROM usuarios u ");
		comando.append("INNER JOIN user_family uf ON uf.Usuario_Id = u.Id_Usuarios ");
		comando.append("INNER JOIN rendas r ON r.Id_Usuario = uf.Usuario_Id ");
		comando.append("INNER JOIN categorias ca ON ca.Id_Categorias = r.Id_Categoria ");
		comando.append("WHERE uf.Familia_Id = ? AND r.Status_Renda = 1 ");
		comando.append("GROUP BY u.Usuario");

		PreparedStatement p;
		ResultSet rs = null;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, familyId);
			rs = p.executeQuery();
			List<Graph> incomes = new ArrayList<Graph>();

			while (rs.next()) {
				Graph newIncome = new Graph();

				newIncome.setName(rs.getString("Name"));
				newIncome.setY(rs.getDouble("IncomeValue"));

				incomes.add(newIncome);
			}

			return incomes;
		} catch (Exception e) {
			throw new ValidationException(e);
		}
	}

}
