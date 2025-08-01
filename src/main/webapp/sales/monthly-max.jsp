<%@page import="java.text.NumberFormat"%>
<%@page import="dao.SalesDao"%>
<%@page import="dto.SalesDto"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
 <%
 	List<SalesDto> list = SalesDao.getInstance().getMonthlyMaxSalesDates();
 
 	NumberFormat nf = NumberFormat.getInstance();
 %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/sales/monthly-max.jsp</title>
</head>
<body>
	<h1>지점별 월간 최고 매출일</h1>
	<table border = "1">
		<tr>
			<th>번호</th>
			<th>월간</th>
			<th>지점</th>
			<th>최고 매출일</th>
			<th>매출액</th>
		</tr>
		
			<%
				int index = 1;
				for(SalesDto dto : list)
			{ %>
				<tr>
					<td><%= index++ %></td>
					<td><%=dto.getPeriod() %></td>
					<td><%=dto.getBranch() %></td>
					<td><%=dto.getMaxSalesDate() %></td>
					<td><%=nf.format(dto.getTotalSales()) %></td>
				</tr>
			<%} %>
	</table>
</body>
</html>





