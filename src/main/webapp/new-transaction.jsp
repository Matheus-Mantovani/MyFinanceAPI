<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="./includes/header.jsp"%>

<div class="container mt-5" style="max-width: 600px;">
	<h2 class="mb-4 text-center">New Transaction</h2>

	<c:if test="${not empty success}">
		<div class="alert alert-success alert-dismissible fade show"
			role="alert">
			${success}
			<button type="button" class="btn-close" data-bs-dismiss="alert"
				aria-label="Close"></button>
		</div>
	</c:if>

	<c:if test="${not empty error}">
		<div class="alert alert-danger alert-dismissible fade show"
			role="alert">
			${error}
			<button type="button" class="btn-close" data-bs-dismiss="alert"
				aria-label="Close"></button>
		</div>
	</c:if>

	<form action="controller.do?action=new-transaction" method="post">
		<div class="mb-3">
			<label for="receiverEmail" class="form-label">Receiver's
				Email</label> <input type="email" class="form-control" id="receiverEmail"
				name="receiverEmail" placeholder="receiver@example.com" required>
		</div>

		<div class="mb-3">
			<label for="price" class="form-label">Amount</label> <input
				type="number" step="0.01" class="form-control" id="price"
				name="price" placeholder="0.00" required>
		</div>

		<div class="mb-3">
			<label for="description" class="form-label">Description</label> <input
				type="text" class="form-control" id="description" name="description"
				placeholder="Transaction description" required>
		</div>

		<div class="mb-3">
			<label for="type" class="form-label">Type</label> <select
				class="form-select" id="type" name="type" required>
				<option value="">Select type</option>
				<option value="Income">Income</option>
				<option value="Expense">Expense</option>
			</select>
		</div>

		<div class="mb-3">
			<label for="category" class="form-label">Category</label> <select
				class="form-select" id="category" name="category" required>
				<option value="">Select category</option>
				<option value="Salary">Salary</option>
				<option value="Shopping">Shopping</option>
				<option value="Food">Food</option>
				<option value="Health">Health</option>
				<option value="Transportation">Transportation</option>
				<option value="Education">Education</option>
				<option value="Taxes">Taxes</option>
				<option value="Other">Other</option>
			</select>
		</div>

		<button type="submit" class="btn btn-success w-100">Save
			Transaction</button>
	</form>
</div>

<%@ include file="./includes/footer.jsp"%>
