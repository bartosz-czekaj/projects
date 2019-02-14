<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fm"%>

<%@include file="common/header.jsp"%>
<%@include file="common/navigation.jsp"%>
<div class="container">
<table class="table table-striped">
    <caption>Your todos are</caption>
    <thead>
        <th>Description</th>
        <th>Date</th>
        <th>Is it Done?</th>
        <th>Update</th>
        <th>Delete</th>
    </thead>
    <tbddy>
        <c:forEach items="${todos}" var="todo">
        <tr>
            <td>${todo.desc}</td>
            <td><fm:formatDate value="${todo.targetDate}" pattern="dd/MM/yyy"></fm:formatDate></td>
            <td>${todo.isDone}</td>
            <td><a type="button" class="btn btn-success" href="/update-todo?id=${todo.id}">Update</a></td>
            <td><a type="button" class="btn btn-warning" href="/delete-todo?id=${todo.id}">Delete</a></td>
        </tr>
        </c:forEach>
    </tbddy>
</table>

<div class="button">
    <a href="/add-todo" class="btn btn-success">Add a TODO</a>
</div>
</div>
<%@include file="common/footer.jsp"%>