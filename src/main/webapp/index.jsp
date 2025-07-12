<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="./includes/header.jsp" %>

<div class="container d-flex flex-column justify-content-center align-items-center text-center" style="min-height: 80vh;">
    <h1 class="mb-4">Welcome to MyFinance 💰</h1>

    <p class="lead mb-4">
        Take control of your personal finances.<br>
        Track your income and expenses, view your balance, and analyze where your money goes.
    </p>

    <div class="d-flex gap-3">
        <a href="controller.do?action=login-page" class="btn btn-primary btn-lg">Login</a>
		<a href="controller.do?action=register-page" class="btn btn-outline-secondary btn-lg">Register</a>
    </div>

    <c:if test="${not empty sessionScope.user}">
        <p class="mt-4">
            You are logged in as <strong>${sessionScope.user.name}</strong>.
            <a href="controller.do?action=transactions-page" class="btn btn-sm btn-success ms-2">Go to Dashboard</a>
        </p>
    </c:if>
</div>

<%@ include file="./includes/footer.jsp" %>
