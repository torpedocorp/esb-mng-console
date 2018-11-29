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
﻿<%@ page contentType="text/html; charset=UTF-8" language="java"%>
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