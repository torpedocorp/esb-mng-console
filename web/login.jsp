<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html lang="kr" dir="ltr" class="uk-height-1-1">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Login</title>
<link rel="apple-touch-icon-precomposed" href="images/apple-touch-icon.png">
<link rel="stylesheet" href="css/uikit.docs.min.css">
<script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
<script src="js/uikit.min.js"></script>
<script type="text/javascript" src="js/default.js"></script>
<script type="text/javascript" src="js/login.js"></script>
</head>
<body>
	<div class="uk-vertical-align uk-text-center">
		<div class="uk-vertical-align-middle" style="padding-top:150px;">
			<h1>BizFrame ESB <br> Management Console</h1><br>
			<form name="form1" id="form1" method="post" class="uk-panel uk-panel-box uk-form">
				<div class="uk-form-row">
					<input class="uk-width-1-1 uk-form-large" type="text" name="loginid" id="loginid" placeholder="Username">
				</div>
				<div class="uk-form-row">
					<input class="uk-width-1-1 uk-form-large" type="password" name="loginpw" id="loginpw" placeholder="Password" onkeydown="onEnter(event)" >
				</div>
				<div class="uk-form-row">
					<a class="uk-width-1-1 uk-button uk-button-primary uk-button-large" href="javascript:loginCall()">Login</a>
				</div>
				<div class="uk-form-row uk-text-small">
					
				</div>
			</form>
		</div>
	</div>
</body>
</html>