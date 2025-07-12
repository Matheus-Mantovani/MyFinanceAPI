package br.edu.ifsp.matheus.dao.transaction;

import java.time.LocalDate;
import java.util.List;

import br.edu.ifsp.matheus.enums.TransactionCategory;
import br.edu.ifsp.matheus.enums.TransactionType;
import br.edu.ifsp.matheus.model.Transaction;

public interface TransactionDAO {
	
	boolean create(Transaction transaction);
	
	Transaction findById(Long id);
	
	List<Transaction> findByPayerId(Long payerId);
	
	List<Transaction> findByReceiverId(Long userId, Long receiverId);
	
	List<Transaction> findByTimeFrame(Long userId, LocalDate startDate, LocalDate endDate);
	
	List<Transaction> findByType(Long userId, TransactionType type);
	
	List<Transaction> findByCategory(Long userId, TransactionCategory category);
}
