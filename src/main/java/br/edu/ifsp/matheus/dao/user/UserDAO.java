package br.edu.ifsp.matheus.dao.user;

import br.edu.ifsp.matheus.model.User;

public interface UserDAO {
	
	boolean create(User user);
	
	User findById(Long id);
	
	User findByEmail(String email);
}
