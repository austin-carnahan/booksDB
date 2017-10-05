<%-- 
    Document   : StoreSelection
    Created on : Jul 29, 2017, 1:41:53 PM
    Author     : raccoonmoonswoon
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix ="c" uri ="http://java.sun.com/jsp/jstl/core" %>
<script src="ajax.js" type="text/javascript"></script>
<script src="inventory.js" type="text/javascript"></script>
<script language="javascript" type="text/javascript">
    function pageAction(action){
        document.selection.actiontype.value = action;
        
        if(ajax && action === 'inventory') {
            ajax.open('get', 'StoreSelection?actiontype=inventory');
            ajax.send(null);
        }
        
        document.selection.submit();
    }
</script>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Store Selection</title>
    </head>
    <c:if test="${!u.authenticated}">
        <script type="text/javascript">
            window.location = "/HenryBooks";
        </script>          
    </c:if>
        
    <c:if test="${u.authenticated}">    
    <body>
        <h1>Inventory View & Update</h1>
        <p>
            User: ${u.userid} -- ${u.username}, ${u.adminlevel} Level
        </p>
        <form action="StoreSelection" id= "selection" method = "post">
            Store:
            <select id="storeid" name="storeid">
                <c:forEach var ="st" items ="${stores}">
                    <option ${st.storeid == user.storeid ? 'Selected' : ''}
                        value ="${st.storeid}">
                        ${st.storename}
                    </option>
                </c:forEach> 
            </select>
            <input type ="submit" value="View/Edit Inventory" 
                   onclick="pageAction('inventory')"/>
            <input type="hidden" name="actiontype" id="actiontype" value="">
        </form>
        ${msg}<br>
        <div id="inventory">
            <%-- for ajax insertion of inventory table --%>
        </div>
    </body>
</c:if>
</html>
