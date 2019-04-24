<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script  src="http://code.jquery.com/jquery-latest.min.js"></script>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="">
<meta name="author" content="">
<title><c:if test="${pageName eq Null}">MEMORANDUM</c:if> <c:if test="${pageName ne Null}">${pageName}</c:if></title>
<!-- Custom fonts for this template-->
<link href="${pageContext.request.contextPath}/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
<!-- Page level plugin CSS-->
<link href="${pageContext.request.contextPath}/vendor/datatables/dataTables.bootstrap4.css" rel="stylesheet">
<!-- Custom styles for this template-->
<link href="${pageContext.request.contextPath}/css/sb-admin.css" rel="stylesheet">

<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/backbone.js/1.3.3/backbone.js"></script>
<script type="text/javascript" src="https://uicdn.toast.com/tui.code-snippet/v1.4.0/tui-code-snippet.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/tui-grid.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/tui-grid.css" />
