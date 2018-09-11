<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@page import="kr.co.bizframe.esb.mng.utils.WebUtils"%>
<%@ include file="bizframe.jsp"%>
<%@ include file="logincheck.jsp"%>
<script>
<%
	if (session_status != null && session_passwd != null) {
%>
		location.href=MAIN_PAGE;
<%
		
	} else {
		WebUtils.logout(session);
%>
	   location.href="login.jsp";
<%
	}
%>
	
</script>