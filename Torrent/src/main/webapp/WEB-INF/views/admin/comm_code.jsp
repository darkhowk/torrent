<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="content-wrapper">
	
	<div class="container-fluid">

		<!-- DataTables Example -->
		<div class="card mb-3">
			<div class="card-header">
				<i class="fas fa-table"></i>
				공통코드 목록
			</div>
			<div class="card-body">
				<div class="table-responsive">
					<table class="table table-bordered" id="dataTable" style="width: 100%">
						<thead>
							<tr>
								<th>SEQ</th>
								<th>Name</th>
								<th>Category</th>
								<th>Code</th>
								<th>UseYn</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach  items="${data}" var="list" varStatus="i">
								<tr>
									<td>${list.SEQ}</td>
									<td>${list.NAME}</td>
									<td>${list.CATEGORY}</td>
									<td>${list.CODE}</td>
									<td>${list.USE_YN}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			<div class="card-footer small text-muted">Checked at ${serverTime }</div>
		</div>
		
	</div>
	<!-- /.container-fluid -->
</div>
<!-- /.content-wrapper -->
