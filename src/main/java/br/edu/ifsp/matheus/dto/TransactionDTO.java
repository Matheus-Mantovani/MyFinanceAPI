package br.edu.ifsp.matheus.dto;

import java.time.LocalDateTime;
import java.util.Objects;

import br.edu.ifsp.matheus.enums.TransactionCategory;
import br.edu.ifsp.matheus.enums.TransactionType;

public class TransactionDTO {
	private Long id;
	private Long payerId;
	private Long receiverId;
	private double price;
	private String description;
	private TransactionType type;
	private TransactionCategory category;
	private LocalDateTime dateTime;
	private String receiverEmail;
	
	public TransactionDTO(Long id, Long payerId, Long receiverId, double price, String description,
			TransactionType type, TransactionCategory category, LocalDateTime dateTime, String receiverEmail) {
		super();
		this.id = id;
		this.payerId = payerId;
		this.receiverId = receiverId;
		this.price = price;
		this.description = description;
		this.type = type;
		this.category = category;
		this.dateTime = dateTime;
		this.receiverEmail = receiverEmail;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPayerId() {
		return payerId;
	}
	public void setPayerId(Long payerId) {
		this.payerId = payerId;
	}
	public Long getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
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
	public String getReceiverEmail() {
		return receiverEmail;
	}
	public void setReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
	}
	@Override
	public int hashCode() {
		return Objects.hash(category, dateTime, description, id, payerId, price, receiverEmail, receiverId, type);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransactionDTO other = (TransactionDTO) obj;
		return category == other.category && Objects.equals(dateTime, other.dateTime)
				&& Objects.equals(description, other.description) && Objects.equals(id, other.id)
				&& Objects.equals(payerId, other.payerId)
				&& Double.doubleToLongBits(price) == Double.doubleToLongBits(other.price)
				&& Objects.equals(receiverEmail, other.receiverEmail) && Objects.equals(receiverId, other.receiverId)
				&& type == other.type;
	}
}
