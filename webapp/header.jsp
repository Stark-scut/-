<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: 19767
  Date: 2018/11/23
  Time: 15:49
  To change this template use File | Settings | File Templates.
--%>

<!--header-->
<div class="header">
    <div class="container">
        <nav class="navbar navbar-default" role="navigation">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <h1 class="navbar-brand"><a href="http://localhost:8080/KFC/goods/GoodsList"></a></h1>
            </div>
            <!--navbar-header-->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav">
                    <li><a href="http://localhost:8080/KFC/goods/goodsIndex" <c:if test="${param.flag==1}">class="active"</c:if>>首页</a></li>
                    <li class="dropdown">
                        <a href="http://localhost:8080/KFC/goods/GoodsList" class="dropdown-toggle <c:if test="${param.flag==2}">active</c:if>" data-toggle="dropdown">商品分类<b class="caret"></b></a>
                        <ul class="dropdown-menu multi-column columns-2">
                            <li>
                                <div class="row">
                                    <div class="col-sm-12">
                                        <h4>商品分类</h4>
                                        <ul class="multi-column-dropdown">

                                            <li><a class="list" href="http://localhost:8080/KFC/goods/GoodsList">全部系列</a></li>

                                            <c:forEach items="${typeList}" var="t">
                                                <li><a class="list" href="http://localhost:8080/KFC/goods/GoodsList?typeid=${t.id}">${t.name}</a></li>
                                            </c:forEach>

                                        </ul>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </li>
                    <li><a href="http://localhost:8080/KFC/goods/goodsRecommendList?type=2" <c:if test="${param.flag==3 && t==2}">class="active"</c:if>>热销</a></li>
                    <li><a href="http://localhost:8080/KFC/goods/goodsRecommendList?type=3" <c:if test="${param.flag==3 && t==3}">class="active"</c:if>>新品</a></li>

                    <c:choose><c:when test="${empty user }">
                        <li><a href="user_register.jsp" <c:if test="${param.flag==10 }">class="active"</c:if>>注册</a></li>
                        <li><a href="user_login.jsp" <c:if test="${param.flag==9 }">class="active"</c:if>>登录</a></li>
                    </c:when><c:otherwise>
                        <li><a href="http://localhost:8080/KFC/order/orderList" <c:if test="${param.flag==5 }">class="active"</c:if>>我的订单</a></li>
                        <li><a href="user_center.jsp" <c:if test="${param.flag==4 }">class="active"</c:if>>个人中心</a></li>
                        <li><a href="http://localhost:8080/KFC/user/logout" >退出</a></li>
                    </c:otherwise>
                    </c:choose>

                    <c:if test="${!empty user && user.admin }">
                        <li><a href="admin/index.jsp" target="_blank">后台管理</a></li>
                    </c:if>
                </ul>
                <!--/.navbar-collapse-->
            </div>
            <!--//navbar-header-->
        </nav>
        <div class="header-info">
            <div class="header-right search-box">
                <a href="javascript:;"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></a>
                <div class="search">
                    <form class="navbar-form" action="http://localhost:8080/KFC/goods/goodsSearch">
                        <input type="text" class="form-control" name="keyword">
                        <button type="submit" class="btn btn-default <c:if test="${param.flag==7 }">active</c:if>" aria-label="Left Align">搜索</button>
                    </form>
                </div>
            </div>
<%--href中加了/表示根路径localhost:8080/  --%>
<%--      不加/表示项目路径localhost:8080/KFC/ --%>
            <div class="header-right cart">
                <a href="goods_cart.jsp">
                    <span class="glyphicon glyphicon-shopping-cart <c:if test="${param.flag==8 }">active</c:if>" aria-hidden="true"><span class="card_num"><c:choose><c:when test="${empty order}">0</c:when><c:otherwise>${order.amount}</c:otherwise></c:choose></span></span>
                </a>
            </div>
            <div class="clearfix"> </div>
        </div>
        <div class="clearfix"> </div>
    </div>
</div>
<!--//header-->