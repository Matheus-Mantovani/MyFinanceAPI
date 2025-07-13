package br.edu.ifsp.matheus.dao.transaction;

import java.util.List;
import java.util.Map;

import br.edu.ifsp.matheus.enums.TransactionCategory;
import br.edu.ifsp.matheus.enums.TransactionType;
import br.edu.ifsp.matheus.model.Transaction;

public interface TransactionDAO {
	
	boolean create(Transaction transaction);
	
	boolean delete(Transaction transaction);
	
	public boolean update(Transaction transaction);
	
	Transaction findById(Long id);
	
	List<Transaction> findByPayerId(Long payerId, int year, int month, TransactionType type, TransactionCategory category, int pageNumber, int pageSize);
	
	int totalPages(Long payerId, int year, int month, TransactionType type, TransactionCategory category, int pageSize);
	
	double totalIncome(Long payerId);

	double totalExpense(Long payerId);
	
	Map<String, Double> incomeByCategory(Long payerId);
	
	Map<String, Double> expensesByCategory(Long payerId);
}
