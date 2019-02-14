<%@include file="common/header.jsp"%>
<%@include file="common/navigation.jsp"%>
<div class="container">
    <form:form method="post" modelAttribute="todo">
        <form:hidden path="id"></form:hidden>
        <fieldset class="form-group">
            <form:label path="targetDate">Date</form:label>
            <form:input path="targetDate" name="targetDate" type="text" class="form-control" required="required"/>
        </fieldset>
        <fieldset class="form-group">
            <form:label path="desc">Description</form:label>
            <form:input path="desc" name="desc" type="text" class="form-control" required="required"/>
        </fieldset>
        <input type="submit" class="btn btn-success"/>
        <form:errors path="desc" cssClass="text-warning"></form:errors>
    </form:form>
</div>
<script src="webjars/bootstrap-datepicker/1.7.1/js/bootstrap-datepicker.js"></script>
<script>
    $('#targetDate').datepicker({
        format : 'dd/mm/yyyy'
    });
</script>

<%@include file="common/footer.jsp"%>