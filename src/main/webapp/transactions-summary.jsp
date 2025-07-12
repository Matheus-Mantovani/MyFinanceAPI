<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ include file="./includes/header.jsp" %>

<div class="container mt-4" style="max-width: 900px;">
    <h2 class="mb-4 text-center">Financial Summary</h2>

    <!-- Totals -->
    <div class="row mb-4">
        <div class="col-md-4">
            <div class="card text-white bg-success mb-3">
                <div class="card-header">Total Income</div>
                <div class="card-body">
                    <h4 class="card-title">$${totalIncome}</h4>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card text-white bg-danger mb-3">
                <div class="card-header">Total Expenses</div>
                <div class="card-body">
                    <h4 class="card-title">$${totalExpenses}</h4>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card text-white bg-primary mb-3">
                <div class="card-header">Current Balance</div>
                <div class="card-body">
                    <h4 class="card-title">$${currentBalance}</h4>
                </div>
            </div>
        </div>
    </div>

    <!-- Filter by Type -->
    <form action="controller.do" method="get" class="row mb-4">
        <input type="hidden" name="action" value="summary-page" />
        <div class="col-md-4 offset-md-8">
            <label for="summaryType" class="form-label">View Summary By</label>
            <select id="summaryType" name="summaryType" class="form-select" onchange="this.form.submit()">
                <option value="Expense" ${param.summaryType == null || param.summaryType == 'Expense' ? 'selected' : ''}>Expenses</option>
                <option value="Income" ${param.summaryType == 'Income' ? 'selected' : ''}>Income</option>
            </select>
        </div>
    </form>

    <!-- Table -->
    <h4>${param.summaryType == 'Income' ? 'Income' : 'Expenses'} by Category</h4>
    <table class="table table-striped mb-4">
        <thead>
            <tr>
                <th>Category</th>
                <th>Amount</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${param.summaryType == 'Income'}">
                    <c:forEach var="entry" items="${incomeByCategory}">
                        <tr>
                            <td>${entry.key}</td>
                            <td>$${entry.value}</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty incomeByCategory}">
                        <tr><td colspan="2" class="text-center">No income data available.</td></tr>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <c:forEach var="entry" items="${expensesByCategory}">
                        <tr>
                            <td>${entry.key}</td>
                            <td>$${entry.value}</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty expensesByCategory}">
                        <tr><td colspan="2" class="text-center">No expense data available.</td></tr>
                    </c:if>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>

    <!-- Pie Chart -->
    
    <div class="d-flex flex-column align-items-center">
    <h4>${param.summaryType == 'Income' ? 'Income' : 'Expenses'} Distribution</h4>
   		<canvas id="summaryPieChart" style="max-width: 300px; max-height: 300px;"></canvas>
    </div>
</div>

<%@ include file="./includes/footer.jsp" %>

<!-- Chart.js -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
    const ctx = document.getElementById('summaryPieChart').getContext('2d');

    const labels = [
        <c:choose>
            <c:when test="${param.summaryType == 'Income'}">
                <c:forEach var="entry" items="${incomeByCategory}" varStatus="status">
                    '${entry.key}'<c:if test="${!status.last}">,</c:if>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <c:forEach var="entry" items="${expensesByCategory}" varStatus="status">
                    '${entry.key}'<c:if test="${!status.last}">,</c:if>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    ];

    const data = [
        <c:choose>
            <c:when test="${param.summaryType == 'Income'}">
                <c:forEach var="entry" items="${incomeByCategory}" varStatus="status">
                    ${entry.value}<c:if test="${!status.last}">,</c:if>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <c:forEach var="entry" items="${expensesByCategory}" varStatus="status">
                    ${entry.value}<c:if test="${!status.last}">,</c:if>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    ];

    const backgroundColors = [
        '#FF6384',
        '#36A2EB',
        '#FFCE56',
        '#4BC0C0',
        '#9966FF',
        '#FF9F40',
        '#C9CBCF',
        '#8D6E63'
    ];

    const chartData = {
        labels: labels,
        datasets: [{
            label: '${param.summaryType == "Income" ? "Income" : "Expenses"} by Category',
            data: data,
            backgroundColor: backgroundColors,
            hoverOffset: 4
        }]
    };

    const config = {
    	    type: 'pie',
    	    data: chartData,
    	    options: {
    	        maintainAspectRatio: false,
    	        layout: {
    	            padding: 10
    	        },
    	        plugins: {
    	            legend: {
    	                position: 'bottom'
    	            }
    	        }
    	    },
    	};

    new Chart(ctx, config);
</script>
