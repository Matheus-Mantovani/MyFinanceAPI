package br.edu.ifsp.matheus.dao.transaction;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
	public List<Transaction> findByPayerId(Long payerId) {
		String sql = "SELECT * FROM transactions WHERE payerId = ?";
		List<Transaction> list = new ArrayList<>();
		
		try (var ps = conn.prepareStatement(sql)) {
			ps.setLong(1, payerId);
			
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
	public List<Transaction> findByReceiverId(Long userId, Long receiverId) {
		String sql = "SELECT * FROM transactions WHERE payerId = ? AND receiver_id = ?";
		List<Transaction> list = new ArrayList<>();
		
		try (var ps = conn.prepareStatement(sql)) {
			ps.setLong(1, userId);
			ps.setLong(2, receiverId);
			
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
	public List<Transaction> findByTimeFrame(Long userId, LocalDate startDate, LocalDate endDate) {
		String sql = "SELECT * FROM transactions WHERE payerId = ? AND transaction_datetime BETWEEN ? AND ?";
		List<Transaction> list = new ArrayList<>();
		
		try (var ps = conn.prepareStatement(sql)) {
			ps.setLong(1, userId);
			ps.setTimestamp(2, Timestamp.valueOf(startDate.atStartOfDay()));
			ps.setTimestamp(3, Timestamp.valueOf(endDate.plusDays(1).atStartOfDay()));
			
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
	public List<Transaction> findByType(Long userId, TransactionType type) {
		String sql = "SELECT * FROM transactions WHERE payerId = ? AND type = ?";
		List<Transaction> list = new ArrayList<>();
		
		try (var ps = conn.prepareStatement(sql)) {
			ps.setLong(1, userId);
			ps.setString(2, type.getName());
			
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
	public List<Transaction> findByCategory(Long userId, TransactionCategory category) {
		String sql = "SELECT * FROM transactions WHERE payerId = ? AND category = ?";
		List<Transaction> list = new ArrayList<>();
		
		try (var ps = conn.prepareStatement(sql)) {
			ps.setLong(1, userId);
			ps.setString(2, category.getName());
			
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
