<%-- 
    Document   : Update
    Created on : Jul 29, 2017, 8:57:04 PM
    Author     : raccoonmoonswoon
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<script language="javascript" type="text/javascript">
    function pageAction(action){
        document.update.actiontype.value = action;
        
        document.update.submit();
    }
</script>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Update</title>
    </head>
    <body>
        <h1>Update Inventory</h1>
        <h2>User: ${u.userid} - ${u.username}, ${u.adminlevel} Level</h2>
        <br>
        <p>
            Branch #: ${s.storeid}<br>
            Branch Name: ${s.storename}<br>
            Branch Location: ${s.storeaddr} <br>
        </p>
        <br>
        <p>
            Book ID: ${book.bookid}<br>
            Book Title: ${book.title}<br>
            Book Author: ${book.author}<br>
        </p>
        <br>
        <form action ="Update" method="post" id="update" name="update">
            Inventory on hand at branch: 
            <input type="text" name="qty" id="qty" value="${book.onhand}"/>
            <input type="submit" value="Update" onclick="pageAction('update')"/>
            <br>
            <input type="submit" value="Cancel/Back" onclick="pageAction('cancel')"/>
            <input type="hidden" name="actiontype" id="actiontype" value="">
            <input type="hidden" name ="storeid" id="storeid" value="${s.storeid}">
            <input type="hidden" name ="bookid" id="bookid" value="${book.bookid}">
        </form>
    ${msg}    
    </body>
</html>
