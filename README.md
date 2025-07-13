# 💰 MyFinanceAPI

---

## 📋 Descrição

**MyFinanceAPI** é uma API RESTful desenvolvida em **Java EE com Servlets**, que oferece um sistema completo de controle financeiro pessoal. Ela permite que os usuários registrem transações (receitas e despesas), visualizem saldos, filtrem transações e obtenham resumos financeiros.  
A API também é consumida por uma aplicação cliente (web/mobile).

---

## 📡 Endpoints da API
Veja a documentação completa da API neste arquivo:
[📄 api-documentation.pdf](./docs/myfinance%20api%20documentation%20pt-br.pdf)

---

## 🎥 Demonstração
[▶️ Clique aqui para assistir ao vídeo da aplicação funcionando](https://drive.google.com/drive/folders/18-e_FM4BYizxEBkVvQ5xNHWzJ91YCBuD?usp=sharing)

---

## ⚙️ Tecnologias Utilizadas

- Java EE (Servlets)
- JSTL + JSP
- JDBC + JNDI
- JSON (Gson)
- MySQL
- Padrões: **Front Controller**, **Factory**, **Command**, **Chain of Responsibility**

---

## 📁 Estrutura do Projeto
```
src/
└── br.edu.ifsp.matheus
├── controller
│ ├── command/
│ ├── handler/
├── model/
├── dao/
├── dto/
├── enums/
├── util/
webapp/
├── WEB-INF/
├── index.jsp
└── ...
```

---

## 🚀 Executando o Projeto

### Pré-requisitos

- Java JDK 17+
- Apache Tomcat 10+
- MySQL
- Navegador ou ferramenta REST (Postman, Insomnia)

### Etapas

1. **Configure o banco de dados**:

**📄 [./docs/schema.sql](./docs/schema.sql)**

```sql
CREATE DATABASE myfinance;
USE myfinance;

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
```

2. **Configure o JNDI no context.xml do Tomcat**:
```xml
<Resource name="jdbc/MyFinanceDB" auth="Container"
          type="javax.sql.DataSource" driverClassName="com.mysql.cj.jdbc.Driver"
          url="jdbc:mysql://localhost:3306/myfinance"
          username="root" password="sua_senha"
          maxTotal="20" maxIdle="10" maxWaitMillis="-1"/>
```
3. **Implemente e execute no Tomcat.**

---

## 🙋‍♂️ Autor
```txt
Matheus Mantovani Gonçalves
Discente de Análise e Desenvolvimento de Sistemas
IFSP - Araraquara
```
