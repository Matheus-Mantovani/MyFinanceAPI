package br.edu.ifsp.matheus.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;

import br.edu.ifsp.matheus.dao.transaction.TransactionDAO;
import br.edu.ifsp.matheus.dao.transaction.TransactionDAOImpl;
import br.edu.ifsp.matheus.dao.user.UserDAO;
import br.edu.ifsp.matheus.dao.user.UserDAOImpl;
import br.edu.ifsp.matheus.util.ConnectionFactory;

public class DAOFactory implements AutoCloseable{
	private final Connection conn;
	
	public DAOFactory() throws NamingException, SQLException {
		this.conn = ConnectionFactory.getConnecion();
		conn.setAutoCommit(false);
	}
	
	public void commit() throws SQLException {
		conn.commit();
	}
	
	public void rollback() {
		try {
			conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public UserDAO getUserDAO() {
		return new UserDAOImpl(conn);
	}
	
	public TransactionDAO getTransactionDAO() {
		return new TransactionDAOImpl(conn);
	}

	@Override
	public void close() throws Exception {
		if(conn != null && !conn.isClosed()) {
			conn.close();
		}
	}
}
