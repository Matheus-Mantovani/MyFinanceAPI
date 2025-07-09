package br.edu.ifsp.matheus.enums;

public enum TransactionType {
	INCOME("Income"), 
	EXPENSE("Expense");
	
	private String name;
	
	private TransactionType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static TransactionType parseType(String type) {
		switch (type.toLowerCase()) {
			case "income": {
				return INCOME;
			}
			case "expense": {
				return EXPENSE;
			}
			default: {
				throw new IllegalArgumentException("Unexpected value: " + type.toLowerCase());
			}
		}
	}
}
