<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<!DOCTYPE html>
<html>
<head>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" +
                request.getServerPort() + path + "/";
    %>
    <base href="<%=basePath%>">
    <title>用户注册</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link type="text/css" rel="stylesheet" href="<%=basePath%>css/bootstrap.css">
    <link type="text/css" rel="stylesheet" href="<%=basePath%>css/style.css">
    <script type="text/javascript" src="<%=basePath%>js/jquery.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>js/bootstrap.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>js/simpleCart.min.js"></script>
</head>
<body>

<!--header-->
<jsp:include page="/header.jsp">
    <jsp:param name="flag" value="10"></jsp:param>
</jsp:include>
<!--//header-->


<!--account-->
<div class="account" id="app">
    <div class="container">
        <div class="register">
            <c:if test="${!empty msg }">
                <div class="alert alert-danger">${msg }</div>
            </c:if>
            <form action="http://localhost:8080/KFC/user/register" method="post">
                <div class="register-top-grid">
                    <h3>注册新用户</h3>
                     <div class="input">
<%--                        required="required"  属性用于设置该字段必须填写，否则跳出默认提示框  --%>
                        <span>用户名 <label style="color:red;">*</label></span>
                        <input type="text" name="username" placeholder="请输入用户名" required="required" >
                    </div>
                    <div class="input">
                        <span>邮箱 <label style="color:red;">*</label></span>
                        <input type="text" name="email" placeholder="请输入邮箱" required="required">
                    </div>
                    <div class="input">
                        <span>密码 <label style="color:red;">*</label></span>
                        <input type="password" name="password" placeholder="请输入密码" required="required">
                    </div>
                    <div class="input">
                        <span>收货人<label></label></span>
                        <input type="text" name="name" placeholder="请输入收货">
                    </div>
                    <div class="input">
                        <span>收货电话<label></label></span>
                        <input type="text" name="phone" placeholder="请输入收货电话">
                    </div>
                    <div class="input">
                        <span>收货地址<label></label></span>
                        <input type="text" name="address" placeholder="请输入收货地址">
                    </div>
                    <div class="clearfix"> </div>
                </div>
                <div class="register-but text-center">
                    <input type="submit" value="提交">
                    <div class="clearfix"> </div>
                </div>
            </form>
            <div class="clearfix"> </div>
        </div>
    </div>
</div>
<!--//account-->






<!--footer-->
<jsp:include page="/footer.jsp"></jsp:include>
<!--//footer-->

<%--<script src="js/axios-0.18.0.js"></script>--%>
<%--<script src="js/vue.js"></script>--%>
<%--<script>--%>
<%--    new Vue({--%>
<%--        el: "#app",--%>
<%--        data(){--%>

<%--        }--%>
<%--    })--%>
<%--</script>--%>



</body>
</html>