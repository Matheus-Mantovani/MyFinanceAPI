<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="./includes/header.jsp"%>

<div class="d-flex justify-content-center align-items-center"
	style="min-height: 100vh;">
	<div class="w-100" style="max-width: 400px;">
		<h2 class="mb-4 text-center">Register</h2>

		<c:if test="${not empty error}">
			<div class="alert alert-danger alert-dismissible fade show"
				role="alert">
				${error}
				<button type="button" class="btn-close" data-bs-dismiss="alert"
					aria-label="Close"></button>
			</div>
			<c:remove var="error" scope="session" />
		</c:if>

		<form action="controller.do?action=register" method="post" novalidate>
			<div class="mb-3">
				<label for="name" class="form-label">Full Name</label> <input
					type="text" class="form-control" id="name" name="name"
					placeholder="Your full name" required autofocus>
			</div>

			<div class="mb-3">
				<label for="email" class="form-label">E-mail</label> <input
					type="email" class="form-control" id="email" name="email"
					placeholder="you@example.com" required>
			</div>

			<div class="mb-3">
				<label for="password" class="form-label">Password</label> <input
					type="password" class="form-control" id="password" name="password"
					placeholder="Your password" required>
			</div>

			<button type="submit" class="btn btn-success w-100">Register</button>
		</form>

		<p class="mt-3 text-center">
			Already have an account? <a href="controller.do?action=login-page">Login
				here</a>.
		</p>
	</div>
</div>

<%@ include file="./includes/footer.jsp"%>
