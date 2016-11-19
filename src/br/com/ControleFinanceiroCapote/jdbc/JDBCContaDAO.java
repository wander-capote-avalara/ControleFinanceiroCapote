package br.com.ControleFinanceiroCapote.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.collections.ListUtils;

import com.mysql.jdbc.Statement;

import br.com.ControleFinanceiroCapote.excecao.ValidationException;
import br.com.ControleFinanceiroCapote.jdbcinterface.ContaDAO;
import br.com.ControleFinanceiroCapote.objetos.Conta;
import br.com.ControleFinanceiroCapote.objetos.Graph;
import br.com.ControleFinanceiroCapote.objetos.Parcela;
import br.com.ControleFinanceiroCapote.objetos.RangeDTO;
import br.com.ControleFinanceiroCapote.validacao.ValidaConta;

public class JDBCContaDAO implements ContaDAO {

	private Connection conexao;
	NumberFormat nf = NumberFormat.getCurrencyInstance();

	public JDBCContaDAO(Connection conexao) {
		this.conexao = conexao;
	}
	
	ValidaConta validac = new ValidaConta();

	@Override
	public void inserir(Conta conta) throws ValidationException {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		if (conta.getId() == 0) {
			validac.insertValidation(conta);
			StringBuilder comando = new StringBuilder();
			comando.append("INSERT INTO contas ");
			comando.append("(Id_Categoria, Id_Usuario, Descricao_Contas, Valor_Contas, ");
			comando.append("Status_Conta, Data_Vencimento, Vezes, Conta_Fixa)");
			comando.append(" VALUES ");
			comando.append("(?,?,?,?,?,?,?,?)");

			PreparedStatement p;
			ResultSet rs = null;

			try {
				p = this.conexao.prepareStatement(comando.toString(), Statement.RETURN_GENERATED_KEYS);
				p.setInt(1, conta.getCategoria());
				p.setInt(2, conta.getUserId());
				p.setString(3, conta.getDescription());
				p.setDouble(4, conta.getTotalValue());
				p.setInt(5, 1);
				p.setDate(6, (Date) conta.getStartDate());
				p.setInt(7, conta.getTimes());
				p.setInt(8, conta.getHasDeadline());
				p.execute();
				rs = p.getGeneratedKeys();
				if (rs.next()) {
					conta.setId(rs.getInt(1));

					if (conta.getTimes() != 0) {
						insertParcels(conta.getId(), conta.getTimes(), conta.getParcelValue(), conta.getTotalValue(), (Date) conta.getStartDate());
					}
				}
			} catch (Exception e) {
				throw new ValidationException(e);
			}
		} else {
			validac.updateValidation(conta);
			StringBuilder comando = new StringBuilder();
			comando.append("UPDATE contas ");
			comando.append("SET Id_Categoria = ?, Id_Usuario = ?, Descricao_contas = ?, Valor_contas = ?, ");
			comando.append("Status_conta = ?, Data_Vencimento = ?, Vezes = ?, Conta_Fixa = ?");
			comando.append(" WHERE Id_contas = ?");
				
			PreparedStatement p;
			 
			try {
				p = this.conexao.prepareStatement(comando.toString(), Statement.RETURN_GENERATED_KEYS);
				p.setInt(1, conta.getCategoria());
				p.setInt(2, conta.getUserId());
				p.setString(3, conta.getDescription());
				p.setDouble(4, conta.getTotalValue());
				p.setInt(5, 1);
				p.setDate(6, new Date(conta.getStartDate().getTime()));
				p.setInt(7, conta.getTimes());
				p.setInt(8, conta.getHasDeadline());
				p.setInt(9, conta.getId());
				p.execute();
				if (conta.getTimes() != 0) {
					deleteParcels(conta.getId());
					insertParcels(conta.getId(), conta.getTimes(), conta.getParcelValue(), conta.getTotalValue(), (Date) conta.getStartDate());
				}else if(conta.getHasDeadline() == 0){
					try{
						deleteParcels(conta.getId());					
					}catch(Exception e){
						//Não há parcelas
					}				
				}
			} catch (Exception e) {
				throw new ValidationException(e);
			}
		}
	}

	private void insertParcels(int billIDd, int times, Double parcelValue, double totalValue,  Date startDate) throws ValidationException {
		double math = ((parcelValue * times) - totalValue);
		boolean hasDifference = (parcelValue * times) - totalValue != 0;
		Calendar cal = Calendar.getInstance();

		StringBuilder comando = new StringBuilder();
		comando.append("INSERT INTO parcela_conta");
		comando.append("(Id_Conta, Valor_Parcela, Status_Parcela, Data_Vencimento)");
		comando.append(" VALUES ");
		comando.append("(?,?,?,?)");

		PreparedStatement p;
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		try {
			p = this.conexao.prepareStatement(comando.toString());

			for (int x = 0; x < times; x++) {
				cal.setTime(startDate);
				cal.add(Calendar.MONTH, 1+x);
				p.setInt(1, billIDd);
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

	@Override
	public boolean deletaConta(int id) throws ValidationException {

		deleteParcels(id);

		StringBuilder comando = new StringBuilder();
		comando.append("UPDATE contas ");
		comando.append("SET Status_Conta = 0 ");
		comando.append("WHERE Id_Contas = ?");
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
		comando.append("UPDATE parcela_conta ");
		comando.append("SET Status_Parcela = 0 ");
		comando.append("WHERE Id_Conta = ?");
		PreparedStatement p;

		try {

			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, id);
			p.execute();

		} catch (Exception e) {
			throw new ValidationException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Conta> getBills(int id, int userId, RangeDTO range) throws ValidationException {
		StringBuilder comando = new StringBuilder();

		comando.append("SELECT r.Id_Contas as id, r.Id_Categoria as categoryId, ");
		comando.append("r.Descricao_Contas as descr, r.Valor_Contas as totalValue, r.Status_Conta as status, ");
		comando.append("r.Data_Vencimento as endDate, r.Conta_Fixa as isFixed, r.Vezes as x, ca.Descricao as categ ");
		comando.append("FROM contas r ");
		comando.append("INNER JOIN categorias ca ON ca.Id_Categorias = r.Id_Categoria ");
		comando.append("WHERE r.Id_Usuario = " + userId);
		comando.append(" AND ");
		comando.append("Status_Conta = 1");
		if (id != 0) {
			comando.append(" AND ");
			comando.append("r.Id_Contas = " + id);
		}
		if (range != null) {
			comando.append(" AND ");
			comando.append("r.Data_Vencimento between ");
			comando.append("'" + range.getFirstYear() + "/" + range.getFirstMonth() + "/01'");
			comando.append(" AND ");
			comando.append("'" + range.getSecondYear() + "/" + range.getSecondMonth() + "/31'");
			comando.append(" AND ");
			comando.append(" r.conta_fixa = 0 ");
		}

		List<Conta> billsList = new ArrayList<Conta>();
		SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
		Conta income = null;
		try {
			java.sql.Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando.toString());
			while (rs.next()) {
				income = new Conta();

				income.setId(rs.getInt("id"));
				income.setCategoria(rs.getInt("categoryId"));
				income.setDescription(rs.getString("descr"));
				income.setTotalValue(rs.getInt("totalValue"));
				income.setStatus(rs.getInt("status"));
				income.setStartDate(rs.getDate("endDate"));
				income.setHasDeadline(rs.getInt("isFixed"));
				income.setTimes(rs.getInt("x"));
				income.setFormatedDate(date.format(rs.getDate("endDate")).replace("-", "/"));
				income.setCategoriaName(rs.getString("categ"));
				income.setFormatedTotalValue(nf.format(rs.getDouble("totalValue")));
				income.setTotalValueString(rs.getString("totalValue"));
			
				billsList.add(income);
			}
			
			if (range != null) {
				return ListUtils.union(billsList, getBillsParcels(userId, range));
			}

		} catch (Exception e) {
			throw new ValidationException(e);
		}
		return billsList;
	}
	
	private List<Conta> getBillsParcels(int id, RangeDTO dates) throws ValidationException {
		StringBuilder comando = new StringBuilder();
		comando.append("SELECT a.Valor_Parcela as vlrConta, a.Status_Parcela as status, ca.Descricao as descr, c.Descricao_Contas as dcs ");
		comando.append("FROM parcela_conta a ");
		comando.append("INNER JOIN contas c ON c.Id_Contas = a.Id_Conta  ");
		comando.append("INNER JOIN categorias ca ON ca.Id_Categorias = c.Id_Categoria  ");
		comando.append("WHERE a.Status_Parcela <> 0 ");
		comando.append("AND a.Data_Vencimento between ? AND ? ");
		comando.append("AND c.Id_Usuario = ?");

		List<Conta> bills = new ArrayList<Conta>();
		PreparedStatement p;
		ResultSet rs = null;
		try {
			
			p = this.conexao.prepareStatement(comando.toString());
			p.setString(1, dates.getFirstYear() + "/" + dates.getFirstMonth() + "/01");
			p.setString(2, dates.getSecondYear() + "/" + dates.getSecondMonth() + "/31");
			p.setInt(3, id);
			rs = p.executeQuery();
			
			while (rs.next()) {
				Conta bill = new Conta();
					
					bill.setTotalValue(rs.getDouble("vlrConta"));
					bill.setStatus(rs.getInt("status"));
					bill.setCategoriaName(rs.getString("descr"));
					bill.setDescription(rs.getString("dcs"));
					bill.setFormatedTotalValue(nf.format(rs.getDouble("vlrConta")));
				
				bills.add(bill);
			}
			
			return bills;		
		} catch (SQLException e) {
			throw new ValidationException(e);
		}
	}

	public List<Graph> getBillsByCategory(int userId, RangeDTO range) throws ValidationException {
		StringBuilder comando = new StringBuilder();

		comando.append("SELECT SUM(root.Valor_Contas) as ValorTotal, c.Descricao as descricao ");
		comando.append("FROM contas root ");
		comando.append("INNER JOIN categorias c ON c.Id_Categorias = root.Id_Categoria ");
		comando.append("WHERE root.Id_Usuario = ? ");
		comando.append("AND root.Data_Vencimento BETWEEN ? AND ?  ");
		comando.append("AND root.Status_Conta = 1 ");
		comando.append("GROUP BY c.Id_Categorias");

		PreparedStatement p;
		ResultSet rs = null;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, userId);
			p.setString(2, range.getFirstYear() + "/" + range.getFirstMonth() + "/01");
			p.setString(3, range.getSecondYear() + "/" + range.getSecondMonth() + "/31");
			rs = p.executeQuery();

			ArrayList<Graph> billList = new ArrayList<Graph>();

			while (rs.next()) {
				Graph newBill = new Graph();

				newBill.setName(rs.getString("descricao"));
				newBill.setY(rs.getInt("ValorTotal"));

				billList.add(newBill);
			}

			return billList;

		} catch (Exception e) {
			throw new ValidationException(e);
		}
	}

	public List<Parcela> getParcelsById(int id) throws ValidationException {
		StringBuilder comando = new StringBuilder();

		comando.append("SELECT Id_Conta as id, Valor_Parcela as parcelValue, Status_Parcela as parcelStatus, ");
		comando.append("Data_Pagamento as paymentDate, Data_Vencimento as dueDate, Id_Parcela_Conta as parcelId ");
		comando.append("FROM parcela_conta WHERE Id_Conta = " + id);
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
				try{
					parcel.setPaymentDateFormated(date.format(rs.getDate("paymentDate")).replace("-", "/"));				
				}catch(Exception e){
					parcel.setPaymentDateFormated("");
				}
				parcel.setFormatedDate(date.format(rs.getDate("dueDate")).replace("-", "/"));
				parcel.setParcelId(rs.getInt("parcelId"));
				parcel.setParcelValueFormated(nf.format(rs.getDouble("parcelValue")));

				parcelList.add(parcel);
			}

		} catch (Exception e) {
			throw new ValidationException(e);
		}
		return parcelList;
	}

	public double getBillsTotalValue(RangeDTO dates, int userId) throws ValidationException {
		StringBuilder comando = new StringBuilder();
		double sum = 0;
		comando.append("SELECT SUM(Valor_Contas) as summ FROM contas a ");
		comando.append("WHERE Id_Usuario = ? ");
		comando.append("AND Status_Conta = 1 ");
		comando.append("AND Data_Vencimento between ? AND ? ");
		comando.append("AND Conta_Fixa = 0 ");

		PreparedStatement p;
		ResultSet rs = null;

		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, userId);
			p.setString(2, dates.getFirstYear() + "/" + dates.getFirstMonth() + "/01");
			p.setString(3, dates.getSecondYear() + "/" + dates.getSecondMonth() + "/31");
			rs = p.executeQuery();

			if (rs.next()) {
				sum = rs.getDouble("summ");
			}
			sum += getBillsParcelsValues(userId, dates);
			return sum;
		} catch (Exception e) {
			throw new ValidationException(e);
		}
	}
	
	private double getBillsParcelsValues(int id, RangeDTO dates) throws ValidationException {
		StringBuilder comando = new StringBuilder();
		comando.append("SELECT SUM(Valor_Parcela) as vlrConta FROM parcela_conta a ");
		comando.append("WHERE a.Status_Parcela <> 0 AND a.Id_Conta IN (SELECT c.Id_Contas FROM contas c where c.Id_Usuario = ?)");
		comando.append(" AND Data_Vencimento between ? AND ? ");

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
				balance += rs.getDouble("vlrConta");
			}
			
			return balance;		
		} catch (SQLException e) {
			throw new ValidationException(e);
		}
	}

	public List<Conta> getAllFamilyBills(int idFamily) throws ValidationException {
		StringBuilder comando = new StringBuilder();

		comando.append("SELECT c.Valor_Contas AS BillValue, u.Usuario AS Name, ");
		comando.append("ca.Descricao as Description, c.Data_Vencimento as billDate ");
		comando.append("FROM usuarios u ");
		comando.append("INNER JOIN user_family uf ON uf.Usuario_Id = u.Id_Usuarios ");
		comando.append("INNER JOIN contas c ON c.Id_Usuario = uf.Usuario_Id ");
		comando.append("INNER JOIN categorias ca ON ca.Id_Categorias = c.Id_Categoria ");
		comando.append("WHERE uf.Familia_Id = ? AND c.Status_Conta = 1 ");
		comando.append("ORDER BY c.Data_Vencimento DESC");
		
		PreparedStatement p;
		ResultSet rs = null;
		
		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, idFamily);
			rs = p.executeQuery();
			List<Conta> bills = new ArrayList<Conta>();
			SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
			
			while (rs.next()) {
				Conta newBill = new Conta();
				
				newBill.setTotalValue(rs.getDouble("BillValue"));
				newBill.setUserName(rs.getString("Name"));
				newBill.setCategoriaName(rs.getString("Description"));
				newBill.setFormatedDate(date.format(rs.getDate("billDate")).replace("-", "/"));
				newBill.setFormatedTotalValue(nf.format(rs.getDouble("BillValue")));
				
				bills.add(newBill);
			}
			
			return bills;
		} catch (Exception e) {
			throw new ValidationException(e);
		}
	}
	
	public List<Graph> getFamilyBillsTotalValue(int idFamily) throws ValidationException {
		StringBuilder comando = new StringBuilder();
		
		comando.append("SELECT SUM(c.Valor_Contas) AS BillValue, u.Usuario AS Name ");
		comando.append("FROM usuarios u ");
		comando.append("INNER JOIN user_family uf ON uf.Usuario_Id = u.Id_Usuarios ");
		comando.append("INNER JOIN contas c ON c.Id_Usuario = uf.Usuario_Id ");
		comando.append("INNER JOIN categorias ca ON ca.Id_Categorias = c.Id_Categoria ");
		comando.append("WHERE uf.Familia_Id = ? AND c.Status_Conta = 1 ");
		comando.append("GROUP BY u.Usuario");
		
		PreparedStatement p;
		ResultSet rs = null;
		
		try {
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, idFamily);
			rs = p.executeQuery();
			List<Graph> bills = new ArrayList<Graph>();
			
			while (rs.next()) {
				Graph newBill = new Graph();
				
				newBill.setName(rs.getString("Name"));
				newBill.setY(rs.getDouble("BillValue"));
				
				bills.add(newBill);
			}
			
			return bills;
		} catch (Exception e) {
			throw new ValidationException(e);
		}
	}

	public void payParcel(int id) throws ValidationException {
		StringBuilder comando = new StringBuilder();
		comando.append("UPDATE parcela_conta SET ");
		comando.append("Data_Pagamento = NOW(), Status_Parcela = 3 ");
		comando.append("WHERE Id_Parcela_Conta = ?");

		PreparedStatement p;
		try {
			
			p = this.conexao.prepareStatement(comando.toString());
			p.setInt(1, id);
			p.execute();
				
		} catch (SQLException e) {
			throw new ValidationException(e);
		}
	}

}
