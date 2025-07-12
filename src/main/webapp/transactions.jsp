<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ include file="./includes/header.jsp" %>

<div class="container mt-4">

<h2 class="mb-4 text-center">Transactions</h2>

<!-- Filtros -->
<form action="controller.do" method="get" class="row g-3 mb-4" novalidate>
    <input type="hidden" name="action" value="transactions-page" />
    
    <div class="col-md-4">
        <label for="month" class="form-label">Month</label>
        <select id="month" name="month" class="form-select">
            <option value="">All</option>
            <c:forEach var="m" begin="1" end="12">
                <option value="${m}" ${param.month == m ? 'selected' : ''}>${m}</option>
            </c:forEach>
        </select>
    </div>
    
    <div class="col-md-4">
        <label for="type" class="form-label">Type</label>
        <select id="type" name="type" class="form-select">
            <option value="">All</option>
            <option value="Income" ${param.type == 'Income' ? 'selected' : ''}>Income</option>
            <option value="Expense" ${param.type == 'Expense' ? 'selected' : ''}>Expense</option>
        </select>
    </div>
    
    <div class="col-md-4">
        <label for="category" class="form-label">Category</label>
        <select id="category" name="category" class="form-select">
            <option value="">All</option>
            <option value="Food" ${param.category == 'Food' ? 'selected' : ''}>Food</option>
            <option value="Health" ${param.category == 'Health' ? 'selected' : ''}>Health</option>
            <option value="Transportation" ${param.category == 'Transportation' ? 'selected' : ''}>Transportation</option>
            <option value="Salary" ${param.category == 'Salary' ? 'selected' : ''}>Salary</option>
            <option value="Shopping" ${param.category == 'Shopping' ? 'selected' : ''}>Shopping</option>
            <option value="Taxes" ${param.category == 'Taxes' ? 'selected' : ''}>Taxes</option>
            <option value="Education" ${param.category == 'Education' ? 'selected' : ''}>Education</option>
        </select>
    </div>
    
    <div class="col-12 text-end">
        <button type="submit" class="btn btn-primary">Filter</button>
        <a href="controller.do?action=transactions-page" class="btn btn-secondary ms-2">Clear</a>
    </div>
</form>

<!-- Link para nova transação -->
<div class="mb-3 text-end">
    <a href="controller.do?action=new-transaction-page" class="btn btn-success">+ New Transaction</a>
</div>

<!-- Tabela de transações -->
<table class="table table-striped table-hover">
    <thead>
        <tr>
            <th>Description</th>
            <th>Amount</th>
            <th>Type</th>
            <th>Category</th>
            <th>Date</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="transaction" items="${transactions}">
            <tr>
                <td>${transaction.description}</td>
                
                <td>
                    <c:choose>
                        <c:when test="${transaction.type == 'Income'}">
                            <span class="text-success">$${transaction.price}</span>
                        </c:when>
                        <c:otherwise>
                            <span class="text-danger">-$${transaction.price}</span>
                        </c:otherwise>
                    </c:choose>
                </td>
                
                <td>${transaction.type}</td>
                <td>${transaction.category}</td>
                <td>${transaction.dateTime}</td>
                
                <td>
                    <a href="controller.do?action=edit-transaction-page&id=${transaction.id}" class="btn btn-sm btn-primary">Edit</a>
                    <a href="controller.do?action=delete-transaction&id=${transaction.id}" 
                       class="btn btn-sm btn-danger"
                       onclick="return confirm('Are you sure you want to delete this transaction?');">
                       Delete
                    </a>
                </td>
            </tr>
        </c:forEach>
        
        <c:if test="${empty transactions}">
            <tr>
                <td colspan="6" class="text-center">No transactions found.</td>
            </tr>
        </c:if>
    </tbody>
</table>

<%@ include file="./includes/footer.jsp" %>
