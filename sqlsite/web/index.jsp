<%-- 
    Document   : index
    Created on : 7-Aug-2017, 2:10:42 PM
    Author     : pc
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SQL Interface site</title>
        <link rel="stylesheet" href="styles/main.css">
    </head>
    <body>
    <c:if test="${sqlStatement == null}">
        <c:set var="sqlStatement" value="SELECT * FROM User" />
    </c:if>
        <h1>
            SQL Interface
        </h1>
        <p>Enter SQL statement and click Execute to see the results</p>
        <p><b>SQL Statement</b></p>
        <form action="sqlsite" method="post">
            <textarea name="sqlStatement" rows="5" cols="60">
                ${sqlStatement}
            </textarea>
            <input type="submit" value="Execute">
        </form>
            <p><b>SQL Results:</b></p>
            ${sqlResult}
    </body>
</html>
