<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@page import="kr.co.bizframe.esb.mng.type.Constants"%>
<%@page import="java.util.Enumeration"%>
<%
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);

	/**
	 * check login session
	 * @version 1.0
	 */

	String session_status = (String) session.getAttribute(Constants.SESSION_STATUS);
	String session_userid = (String) session.getAttribute(Constants.SESSION_USERID);
	String session_passwd = (String) session.getAttribute(Constants.SESSION_PASSWD);
	String session_errmsg = "Login 후 작업하시기 바랍니다";
	String loginPage = "login.jsp";
	String currentPage = request.getRequestURL().toString();
	boolean isUserRegist = false;
	String userRegistStyle = "";
	if (session_status == null || session_userid == null || session_passwd == null) {
		if(currentPage.indexOf("userRegist.jsp") > -1) {
			userRegistStyle = "display:none;";
			isUserRegist = true;
		} else {
			out.println("<script>");
			out.println("  alert('" + session_errmsg + "');");
			out.println("  window.open('" + loginPage + "', '_top')");
			out.println("</script>");
			return;
		}
	}
%>