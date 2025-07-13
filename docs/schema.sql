CREATE DATABASE IF NOT EXISTS myfinancedb;
USE myfinancedb;

CREATE TABLE users (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE transactions (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    payer_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    price DOUBLE NOT NULL,
    description VARCHAR(255),
    type ENUM('Income', 'Expense') NOT NULL,
    category ENUM('Food','Health','Transportation','Salary','Shopping','Taxes','Education','Other') NOT NULL,
    transaction_datetime DATETIME NOT NULL
);