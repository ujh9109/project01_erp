<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>/userp/hqlogin-form.jsp</title>
</head>
<body>
	<div class="container">
		<h1>본사 로그인 양식</h1>
		<form action="hqlogin.jsp" method="post">
			<div>
				<label for="userId" class="form-label">아이디</label>
				<input type="text" name="userId" id="userId" class="form-control" required/>
			</div>
			<div>
				<label for="password" class="form-label">비밀번호</label>
				<input type="password" name="password" id="password" class="form-control" required/>
			</div>
			<button type="submit">로그인</button>
		</form>
	</div>
</body>
</html>