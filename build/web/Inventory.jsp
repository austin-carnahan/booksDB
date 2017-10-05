
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix ="c" uri ="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Inventory Page</title>
    </head>
    <c:if test="${!u.authenticated}">
        <script type="text/javascript">
            window.location = "/HenryBooks";
        </script>          
    </c:if>
    <c:if test="${u.authenticated}">    
    <body>
        <h1>User: ${u.userid} - ${u.username}, ${u.adminlevel} Level</h1>
        <p>
            Branch #: ${s.storeid}<br>
            Branch Name: ${s.storename}<br>
            Branch Location: ${s.storeaddr} <br>
        </p>
        <c:if test = "${u.adminlevel == 'Admn'}">
        <form action ="Inventory" method ="post">
            Book ID:
            <input type="text" name="bookid" id="bookid" value=""/>
            <input type="submit" value="Edit Record"/>
            <input type="hidden" name="storeid" id="storeid" value="${s.storeid}"/>
        </form>
        </c:if>
        <br>
        <table border="1">
            <tr>
                <th>Store</th>
                <th>Book ID</th>
                <th>Title</th>
                <th>Price</th>
                <th>Qty.</th>
            </tr>
            <c:forEach var="bk" items="${books}">
                <tr>
                    <td align="right">${s.storeid}</td>
                    <td align="right">${bk.bookid}</td>
                    <td align="left">${bk.title}</td>
                    <td align="right">${bk.formattedPrice}</td>
                    <td align="right">${bk.onhand}</td>
                </tr>
            </c:forEach>
        </table>
        <br>
        ${msg}
    </body>
    </c:if>
</html>
