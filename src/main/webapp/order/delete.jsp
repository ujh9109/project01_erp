<%@ page import="dao.StockRequestDao" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
int orderId = Integer.parseInt(request.getParameter("orderId"));

StockRequestDao dao = new StockRequestDao();
boolean isSuccess = dao.deleteRequest(orderId);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>삭제 처리</title>
    <script>
    <% if(isSuccess) { %>
        alert('삭제되었습니다.');
        location.href = 'list.jsp';
    <% } else { %>
        alert('삭제 실패!');
        history.back();
    <% } %>
    </script>
</head>
<body>
</body>
</html>