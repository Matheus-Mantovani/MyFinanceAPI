package br.edu.ifsp.matheus.dao.transaction;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ifsp.matheus.enums.TransactionCategory;
import br.edu.ifsp.matheus.enums.TransactionType;
import br.edu.ifsp.matheus.model.Transaction;

public class TransactionDAOImpl implements TransactionDAO {
	private Connection conn;

	public TransactionDAOImpl(Connection conn) {
		this.conn = conn;
	}

	@Override
	public boolean create(Transaction transaction) {
		String sql = "INSERT INTO transactions(payer_id, receiver_id, price, description, type, category, transaction_datetime) VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (var ps = conn.prepareStatement(sql)) {
			ps.setLong(1, transaction.getPayerId());
			ps.setLong(2, transaction.getReceiverId());
			ps.setDouble(3, transaction.getPrice());
			ps.setString(4, transaction.getDescription());
			ps.setString(5, transaction.getType().getName());
			ps.setString(6, transaction.getCategory().getName());
			ps.setTimestamp(7, Timestamp.valueOf(transaction.getDateTime()));

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	@Override
	public boolean delete(Transaction transaction) {
		String sql = "DELETE FROM transactions WHERE id = ?";
		
		try(var ps = conn.prepareStatement(sql)) {
			ps.setLong(1, transaction.getId());
			
			return ps.executeUpdate() > 0;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public Transaction findById(Long id) {
		String sql = "SELECT * FROM transactions WHERE id = ?";
		Transaction transaction = null;
		
		try (var ps = conn.prepareStatement(sql)) {
			ps.setLong(1, id);
			
			try(ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					transaction = buildTransactionFromResultSet(rs);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return transaction;
	}

	@Override
	public List<Transaction> findByPayerId(Long payerId, int year, int month, TransactionType type, TransactionCategory category, int pageNumber, int pageSize) {
		StringBuilder sql = new StringBuilder("SELECT * FROM transactions WHERE payer_id = ?");
		List<Object> params = new ArrayList<>();
		List<Transaction> list = new ArrayList<>();
		
		if(year != 0) {
			sql.append(" AND YEAR(transaction_datetime) = ?");
			params.add(year);
		}
		
		if(month != 0) {
			sql.append(" AND MONTH(transaction_datetime) = ?");
			params.add(month);
		}
		
		if(type != null) {
			sql.append(" AND type = ?");
			params.add(type.getName());
		}
		
		if(category != null) {
			sql.append(" AND category = ?");
			params.add(category.getName());
		}
		
		sql.append(" ORDER BY transaction_datetime DESC LIMIT ? OFFSET ?");

		try (var ps = conn.prepareStatement(sql.toString())) {
			int index = 1;
	        ps.setLong(index++, payerId);
	        
	        for(Object param : params) {
	            ps.setObject(index++, param);
	        }
			
			int offset = (pageNumber - 1) * pageSize;
			ps.setInt(index++, pageSize);
			ps.setInt(index++, offset);
			
			try(ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					list.add(buildTransactionFromResultSet(rs));
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	@Override
	public int totalPages(Long payerId, int year, int month, TransactionType type, TransactionCategory category, int pageSize) {
		StringBuilder sql = new StringBuilder("SELECT COUNT(id) AS totalTransactions FROM transactions WHERE payer_id = ?");
		List<Object> params = new ArrayList<>();
		int totalPages = 0;
		
		if(year != 0) {
			sql.append(" AND YEAR(transaction_datetime) = ?");
			params.add(year);
		}
		
		if(month != 0) {
			sql.append(" AND MONTH(transaction_datetime) = ?");
			params.add(month);
		}
		
		if(type != null) {
			sql.append(" AND type = ?");
			params.add(type.getName());
		}
		
		if(category != null) {
			sql.append(" AND category = ?");
			params.add(category.getName());
		}
		
		try(var ps = conn.prepareStatement(sql.toString())) {
			int index = 1;
	        ps.setLong(index++, payerId);
	        
	        for(Object param : params) {
	            ps.setObject(index++, param);
	        }
	        
	        try(var rs = ps.executeQuery()) {
	        	if(rs.next()) {
	        		totalPages = rs.getInt(1);
	        		totalPages = (totalPages + pageSize - 1) / pageSize;
	        	}
	        }
	        
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return totalPages;
	}

	@Override
	public double totalIncome(Long payerId) {
		String sql = "SELECT SUM(price) FROM transactions WHERE type LIKE 'Income'";
		
		try(var ps = conn.prepareStatement(sql)) {
			try(var rs = ps.executeQuery()) {
				if(rs.next()) {
					return rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	@Override
	public double totalExpense(Long payerId) {
		String sql = "SELECT SUM(price) FROM transactions WHERE type LIKE 'Expense'";
		
		try(var ps = conn.prepareStatement(sql)) {
			try(var rs = ps.executeQuery()) {
				if(rs.next()) {
					return rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	@Override
	public Map<String, Double> incomeByCategory(Long payerId) {
		String sql = "SELECT category, SUM(price) FROM transactions WHERE type LIKE 'Income' GROUP BY category";
		Map<String, Double> incomeByCategory = new HashMap<>();
		
		try(var ps = conn.prepareStatement(sql)) {
			try(var rs = ps.executeQuery()) {
				while(rs.next()) {
					incomeByCategory.put(rs.getString(1), rs.getDouble(2));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return incomeByCategory;
	}
	
	@Override
	public Map<String, Double> expensesByCategory(Long payerId) {
		String sql = "SELECT category, SUM(price) FROM transactions WHERE type LIKE 'Expense' GROUP BY category";
		Map<String, Double> expensesByCategory = new HashMap<>();
		
		try(var ps = conn.prepareStatement(sql)) {
			try(var rs = ps.executeQuery()) {
				while(rs.next()) {
					expensesByCategory.put(rs.getString(1), rs.getDouble(2));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return expensesByCategory;
	}

	private Transaction buildTransactionFromResultSet(ResultSet rs) throws SQLException {
		return new Transaction(
				rs.getLong("id"), 
				rs.getLong("payer_id"), 
				rs.getLong("receiver_id"),
				rs.getDouble("price"), 
				rs.getString("description"), 
				TransactionType.parseType(rs.getString("type")),
				TransactionCategory.parseCategory(rs.getString("category")),
				rs.getTimestamp("transaction_datetime").toLocalDateTime()
			);
	}
}
