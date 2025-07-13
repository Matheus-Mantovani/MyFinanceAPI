<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ include file="./includes/header.jsp" %>

<div class="container mt-5" style="max-width: 600px;">
    <h2 class="mb-4 text-center">Edit Transaction</h2>

    <c:if test="${not empty error}">
        <div class="alert alert-danger" role="alert">
            ${error}
        </div>
    </c:if>

    <form action="controller.do?action=edit-transaction" method="post">
        <input type="hidden" name="_method" value="put" />
        <input type="hidden" name="id" value="${transaction.id}" />

        <div class="mb-3">
            <label for="description" class="form-label">Description</label>
            <input type="text" class="form-control" id="description" name="description"
                   value="${transaction.description}" required>
        </div>

        <div class="mb-3">
            <label for="price" class="form-label">Amount</label>
            <input type="number" step="0.01" class="form-control" id="price" name="price"
                   value="${transaction.price}" required>
        </div>

        <div class="mb-3">
            <label for="type" class="form-label">Type</label>
            <select class="form-select" id="type" name="type" required>
                <option value="Income" ${transaction.type.name == 'Income' ? 'selected' : ''}>Income</option>
                <option value="Expense" ${transaction.type.name == 'Expense' ? 'selected' : ''}>Expense</option>
            </select>
        </div>

        <div class="mb-3">
            <label for="category" class="form-label">Category</label>
            <select class="form-select" id="category" name="category" required>
                <option value="Salary" ${transaction.category.name == 'Salary' ? 'selected' : ''}>Salary</option>
                <option value="Shopping" ${transaction.category.name == 'Shopping' ? 'selected' : ''}>Shopping</option>
                <option value="Food" ${transaction.category.name == 'Food' ? 'selected' : ''}>Food</option>
                <option value="Health" ${transaction.category.name == 'Health' ? 'selected' : ''}>Health</option>
                <option value="Transportation" ${transaction.category.name == 'Transportation' ? 'selected' : ''}>Transportation</option>
                <option value="Education" ${transaction.category.name == 'Education' ? 'selected' : ''}>Education</option>
                <option value="Taxes" ${transaction.category.name == 'Taxes' ? 'selected' : ''}>Taxes</option>
                <option value="Other" ${transaction.category.name == 'Other' ? 'selected' : ''}>Other</option>
            </select>
        </div>

        <div class="mb-3">
            <label for="receiverEmail" class="form-label">Receiver Email</label>
            <input type="email" class="form-control" id="receiverEmail" name="receiverEmail"
                   value="${transaction.receiverEmail}" required>
        </div>

        <button type="submit" class="btn btn-primary w-100">Update Transaction</button>
    </form>
</div>

<%@ include file="./includes/footer.jsp" %>
