package br.edu.ifsp.matheus.enums;

public enum TransactionCategory {
	FOOD("Food"), 
	HEALTH("Health"), 
	TRANSPORTATION("Transportation"), 
	SALARY("Salary"), 
	SHOPPING("Shopping"), 
	TAXES("Taxes"), 
	EDUCATION("Education");
	
	private String name;
	
	private TransactionCategory(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static TransactionCategory parseCategory(String category) {
		switch (category.toLowerCase()) {
			case "food": {
				return FOOD;
			}
			case "health": {
				return HEALTH;
			}
			case "transportation": {
				return TRANSPORTATION;
			}
			case "salary": {
				return SALARY;
			}
			case "shopping": {
				return SHOPPING;
			}
			case "taxes": {
				return TAXES;
			}
			case "education": {
				return EDUCATION;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + category.toLowerCase());
		}
	}
}
