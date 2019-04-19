<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!-- Sidebar -->
<ul class="sidebar navbar-nav">
	<li class="nav-item active">
		<a class="nav-link" href="index.jsp">
			<i class="fas fa-fw fa-tachometer-alt"></i>
			<span>메뉴</span>
		</a>
	</li>
	<!-- <li class="nav-item dropdown">
	<a class="nav-link dropdown-toggle" href="#" id="pagesDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
	<i class="fas fa-fw fa-folder"></i>
	<span>Pages</span>
	</a>
	<div class="dropdown-menu" aria-labelledby="pagesDropdown">
	<h6 class="dropdown-header">Login Screens:</h6>
	<a class="dropdown-item" href="login.html">Login</a>
	<a class="dropdown-item" href="register.html">Register</a>
	<a class="dropdown-item" href="forgot-password.html">Forgot Password</a>
	<div class="dropdown-divider"></div>
	<h6 class="dropdown-header">Other Pages:</h6>
	<a class="dropdown-item" href="404.html">404 Page</a>
	<a class="dropdown-item" href="blank.html">Blank Page</a>
	</div>
	</li>
	<li class="nav-item">
	<a class="nav-link" href="charts.html">
	<i class="fas fa-fw fa-chart-area"></i>
	<span>Charts</span></a>
	</li> -->
	<!--<li class="nav-item">
	<a class="nav-link" href="torrent.do">
	<i class="fas fa-fw fa-table"></i>
	<span>지금 실행</span></a>
	</li> -->
	<li class="nav-item dropdown">
		<a class="nav-link dropdown-toggle" href="#" id="pagesDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
			<i class="fas fa-fw fa-folder"></i>
			<span>실행</span>
		</a>
		<div class="dropdown-menu" aria-labelledby="pagesDropdown">
			<h6 class="dropdown-header">프로그램 명</h6>
			<c:forEach items="${list }" var="list">
				<a class="dropdown-item" href="login.html">${ list.NAME }</a>
			</c:forEach>
			<!--  <a class="dropdown-item" href="login.html">삼시 세끼</a>
			<a class="dropdown-item" href="torrentState.do">테스트</a> -->
		</div>
	</li>
	<li class="nav-item dropdown">
		<a class="nav-link dropdown-toggle" href="#" id="pagesDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
			<i class="fas fa-fw fa-folder"></i>
			<span>admin</span>
		</a>
		<div class="dropdown-menu" aria-labelledby="pagesDropdown">
			<a class="dropdown-item" href="comm_code.do">공통코드</a>
		</div>
	</li>
</ul>
