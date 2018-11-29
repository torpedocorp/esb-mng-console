<%
/*                                                                              
 * Copyright 2018 Torpedo corp.                                                 
 *                                                                              
 * bizframe esb-mng-console project licenses this file to you under the Apache License,     
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:                   
 *                                                                              
 *   http://www.apache.org/licenses/LICENSE-2.0                                 
 *                                                                              
 * Unless required by applicable law or agreed to in writing, software          
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT  
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the     
 * License for the specific language governing permissions and limitations      
 * under the License.                                                           
 */ 
 %>
<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html lang="kr" dir="ltr" class="uk-height-1-1">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Login</title>
<link rel="apple-touch-icon-precomposed" href="images/apple-touch-icon.png">
<link rel="stylesheet" href="css/login.min.css">

<script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
<script src="js/uikit.min.js"></script>
<script type="text/javascript" src="js/default.js"></script>
<script type="text/javascript" src="js/login.js"></script>
</head>
<body>
	<div class="uk-background-contain uk-panel uk-flex uk-flex-top" style="background-image: url(img/login-bg.png);">
		<div class="uk-card uk-card-default uk-card-hover uk-card-body uk-width-1-4 uk-align-center" style ="margin-top:200px">
			<p class="uk-h2" style="text-align:center">
				BizFrame ESB <br>
				Management Console
			</p>
			<form name="form1" id="form1" method="post">
				<div class="uk-margin">
						<input class="uk-input" type="text" name="loginid" id="loginid" placeholder="Username">
					</div>
				<div class="uk-margin">
					<input class="uk-input" type="password" name="loginpw" id="loginpw" placeholder="Password" onkeydown="onEnter(event)" >
				</div>
				<div class="uk-margin">
					<a class="uk-width-1-1 uk-button uk-button-primary uk-button-large" href="javascript:loginCall()">Login</a>
				</div>
			</form>
		</div>
	</div>
</body>
</html>