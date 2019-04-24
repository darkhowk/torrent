<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<%@ include file="../comm/include.jsp" %>

<body id="page-top">
	
	<!-- Page Wrapper -->
	<div id="wrapper">
		<%@ include file="../comm/sidebar.jsp" %>
		
		<!-- Content Wrapper -->
	    <div id="content-wrapper" class="d-flex flex-column">
	
		    <!-- Main Content -->
		    <div id="content">
		    
		    	<!-- Topbar -->
				<%@ include file="../comm/topbar.jsp" %>
				
				<!-- Begin Page Content -->
	       		<div class="container-fluid">
					<jsp:include page="${page }"></jsp:include>
				</div>
				<%@ include file="../comm/bottom.jsp" %>
			</div>
		</div>
	</div>
</body>
