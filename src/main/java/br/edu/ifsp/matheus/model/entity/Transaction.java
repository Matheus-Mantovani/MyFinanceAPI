package br.edu.ifsp.matheus.model.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import br.edu.ifsp.matheus.enums.TransactionCategory;
import br.edu.ifsp.matheus.enums.TransactionType;

public class Transaction {
	private Long id;
	private double value;
	private String description;
	private TransactionType type;
	private TransactionCategory category;
	private LocalDateTime dateTime;
	
	
	public Transaction() {
		super();
	}
	
	public Transaction(double value, String description, TransactionType type, TransactionCategory category,
			LocalDateTime dateTime) {
		this(null, value, description, type, category, dateTime);
	}

	public Transaction(Long id, double value, String description, TransactionType type, TransactionCategory category,
			LocalDateTime dateTime) {
		this.id = id;
		this.value = value;
		this.description = description;
		this.type = type;
		this.category = category;
		this.dateTime = dateTime;
	}

	
	public Long getId() {
		return id;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public TransactionCategory getCategory() {
		return category;
	}

	public void setCategory(TransactionCategory category) {
		this.category = category;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	@Override
	public int hashCode() {
		return Objects.hash(category, dateTime, description, id, type, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		return category == other.category && Objects.equals(dateTime, other.dateTime)
				&& Objects.equals(description, other.description) && Objects.equals(id, other.id) && type == other.type
				&& Double.doubleToLongBits(value) == Double.doubleToLongBits(other.value);
	}
}

