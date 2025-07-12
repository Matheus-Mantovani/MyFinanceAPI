<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./includes/header.jsp" %>

<div class="container d-flex flex-column justify-content-center align-items-center text-center" style="min-height: 80vh;">
    <h1 class="mb-4 text-danger">Access Restricted</h1>
    <p class="lead mb-4">
        You must be logged in to access this page.
    </p>
    <a href="controller.do?action=login-page" class="btn btn-primary btn-lg">Go to Login</a>
</div>

<%@ include file="./includes/footer.jsp" %>
