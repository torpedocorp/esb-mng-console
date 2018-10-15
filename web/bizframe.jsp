<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@page import="kr.co.bizframe.esb.mng.type.Constants"%>
<%@page import="javax.servlet.http.HttpSession"%>
<%@page import="kr.co.bizframe.esb.mng.config.BizFrameConfig"%>
<%@page import="org.springframework.context.annotation.AnnotationConfigApplicationContext"%>
<%@page import="org.springframework.beans.factory.annotation.AnnotatedBeanDefinition"%>
<%@page import="org.springframework.context.support.AbstractApplicationContext"%>
<%@page import="kr.co.bizframe.esb.mng.utils.WebUtils"%>
<%
AbstractApplicationContext context = new AnnotationConfigApplicationContext(BizFrameConfig.class);
WebUtils webUtils = context.getBean(WebUtils.class);
String hawtioUrl = webUtils.getHawtioUrl();
int monitorInterval = webUtils.getMonitorInterval();
%>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<meta http-equiv='cache-control' content='no-cache'>
	<meta http-equiv='expires' content='0'>
	<meta http-equiv='pragma' content='no-cache'>    
    <title>BizFrame ESB Management Console</title>
    <link rel="apple-touch-icon-precomposed" href="images/apple-touch-icon.png">
    <link id="data-uikit-theme" rel="stylesheet" href="css/uikit.docs.min.css">
    <link rel="stylesheet" href="css/docs.css">    
    <link rel="stylesheet" href="css/highlight/highlight.css">
	<script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
	<script type="text/javascript" src="js/uikit.min.js"></script>
	<script type="text/javascript" src="js/default.js"></script>
    <script src="js/highlight/highlight.js"></script>
    <script src="js/login.js"></script>
	<style type="text/css">
	    #mynetwork {
	      width: 100%;
	      height: 700px;
	      border: 0px solid lightgray;
	    }
	    .trace-msg-dialog {
		  width: 800px;
		}
			    
	</style>
<script>
// 로그인된 상태이나 클라이언트 세션 스토리지에 권한 정보가 없을 때 클라이언트 세션 스토리지에 권한 데이터를 넣어줌
<%String sessionStatus = (String)session.getAttribute(Constants.SESSION_STATUS);%>;
var isSessionAlive = '<%=sessionStatus%>';
var hawtioUrl = '<%=hawtioUrl%>';
</script>
</head>