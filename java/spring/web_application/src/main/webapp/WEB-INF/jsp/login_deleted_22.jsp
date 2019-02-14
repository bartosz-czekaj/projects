<%@include file="common/header.jsp"%>
<%@include file="common/navigation.jsp"%>
<form:form method="post">
    <fieldset class="form-group">
        <label path="a">Name</label>
        <input path="a" name="name" type="text" class="form-control" required="required" />
    </fieldset>
    <fieldset class="form-group">
        <label path="b">Password</label>
        <input path="b" type="password" name="password" lass="form-control" required="required"/>
    </fieldset>
    <input type="submit"/>
</form:form>
<%@include file="common/footer.jsp"%>