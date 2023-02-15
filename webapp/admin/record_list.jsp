<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<title>客户浏览记录</title>
	<%
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" +
				request.getServerPort() + path + "/";
	%>
	<base href="<%=basePath%>">
<meta charset="utf-8"/>
<link rel="stylesheet" href="<%=basePath%>css/bootstrap.css"/>
</head>
<body>
<div class="container-fluid">

	




	<jsp:include page="header.jsp"></jsp:include>


	<br>
	<br>
	
	<table class="table table-bordered table-hover">

	<tr>
		<th width="5%">ID</th>
		<th width="10%">用户名</th>
		<th width="10%">起始时间</th>
		<th width="10%">终止时间</th>
	</tr>


		<c:forEach items="${p.list }" var="r">
			<tr>
				<td><p>${r.id }</p></td>
				<td><p>${r.user.userName }</p></td>
				<td><p>${r.startTime }</p></td>
				<td><p>${r.endTime }</p></td>

			</tr>
		</c:forEach>
     
     
</table>

<br>
	<jsp:include page="/page.jsp">
		<jsp:param value="/admin/user_list" name="url"/>
	</jsp:include>
<br>
</div>
</body>
</html>