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