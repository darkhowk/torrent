<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<%@ include file="../comm/include.jsp" %>

<body id="page-top">
	<%@ include file="../comm/top.jsp" %>
	<div id="wrapper">
		<%@ include file="../comm/left.jsp" %>
		<jsp:include page="${page }"></jsp:include>
		<%@ include file="../comm/bottom.jsp" %>
	</div>
</body>
