<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formm" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>

<html>

<body>

<h2>Dear Employee, Please enter your details</h2>

<br>


<form:form action = "showDetails" modelAttribute = "employee">

    Name <form:input path = "name"/>
    <form:errors path="name"/>
    <br>
    Surname <form:input path = "surname"/>
    <form:errors path="surname"/>
    <br>
    Salary <form:input path = "salary"/>
    <form:errors path="salary"/>
    <br>
    Department <form:select  path="department">
<%--    <form:option value="Information Tehnology" label="IT"/>--%>
<%--    <form:option value="Human Resources" label="HR"/>--%>
<%--    <form:option value="Sales" label="Sales"/>--%>
    <form:options items="${employee.departments}"/>

    </form:select>
    <br>
    Which car do you want?

<%--    BMW <form:radiobutton path="carBrand" value="BMW"/>--%>
<%--    Audi <form:radiobutton path="carBrand" value="Audi"/>--%>
<%--    MB <form:radiobutton path="carBrand" value="Mercedes-Benz"/>--%>

    <form:radiobuttons path="carBrand" items="${employee.carBrands}"/>
    <br>
    Foreign Languages(s)
<%--    EN <form:checkbox path="languages" value="English"/>--%>
<%--    DE <form:checkbox path="languages" value="Deutch"/>--%>
<%--    FR <form:checkbox path="languages" value="French"/>--%>
    <form:checkboxes path="languages" items="${employee.languageMap}"/>

    <br>
    Phone <form:input path = "phone"/>
    <form:errors path="phone"/>
    <br>
    E-mail <form:input path = "email"/>
    <form:errors path="email"/>


    <br>
    <input type="submit" value="ok"/>

</form:form>


</body>


</html>