package br.edu.ifsp.matheus.dao.user;

import java.sql.Connection;
import java.sql.SQLException;

import br.edu.ifsp.matheus.model.User;

public class UserDAOImpl implements UserDAO {
	private Connection conn;
	
	public UserDAOImpl(Connection conn) {
		this.conn = conn;
	}

	@Override
	public boolean create(User user) {
		String sql = "INSERT INTO users(name, email, password) VALUES (?, ?, ?)";
		
		try(var ps = conn.prepareStatement(sql)) {
			ps.setString(1, user.getName());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getPassword());
			
			return ps.executeUpdate() > 0;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public User findById(Long id) {
		String sql = "SELECT id, name, email, password FROM users WHERE id = ?";
		User user = null;
		
		try(var ps = conn.prepareStatement(sql)) {
			ps.setLong(1, id);
			
			try(var rs = ps.executeQuery()) {
				if(rs.next()) {
					user = new User();
					user.setId(rs.getLong(1));
					user.setName(rs.getString(2));
					user.setEmail(rs.getString(3));
					user.setPassword(rs.getString(4));
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return user;
	}

	@Override
	public User findByEmail(String email) {
		String sql = "SELECT id, name, email, password FROM users WHERE email = ?";
		User user = null;
		
		try(var ps = conn.prepareStatement(sql)) {
			ps.setString(1, email);
			
			try(var rs = ps.executeQuery()) {
				if(rs.next()) {
					user = new User();
					user.setId(rs.getLong(1));
					user.setName(rs.getString(2));
					user.setEmail(rs.getString(3));
					user.setPassword(rs.getString(4));
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return user;
	}

}
