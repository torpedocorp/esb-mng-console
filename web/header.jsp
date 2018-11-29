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
<%@page import="java.net.InetAddress"%>
<%@page import="kr.co.bizframe.esb.mng.type.Constants"%>
<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%
String url = javax.servlet.http.HttpUtils.getRequestURL(request).toString();
String dashboardActive = "";
if (url.indexOf("dashboard.jsp") > -1) {
	dashboardActive = "class='uk-active'";
}
String appActive = "";
if (url.indexOf("app_") > -1) {
	appActive = "class='uk-active'";
}

String msgActive = "";
if (url.indexOf("msg_") > -1) {
	msgActive = "class='uk-active'";
}

String monitoringActive = "";
if (url.indexOf("monitoring") > -1) {
	monitoringActive = "class='uk-active'";
}

String exchangeActive = "";
if (url.indexOf("exchange_") > -1) {
	exchangeActive = "class='uk-active'";
}

String sttActive = "";
if (url.indexOf("statistic") > -1) {
	sttActive = "class='uk-active'";
}
%>

        <nav class="tm-navbar uk-navbar uk-navbar-attached">
            <div class="uk-container uk-container-center"> 
                <a class="uk-navbar-brand uk-hidden-small" href="dashboard.jsp">BizFrame ESB</a>
                <ul class="uk-navbar-nav uk-hidden-small">
                    <li <%=dashboardActive %>><a href="dashboard.jsp">Dashboard</a></li>
                    <li <%=appActive %>><a href="app_list.jsp">Applications</a></li>
                    <li <%=exchangeActive %>><a href="exchange_list.jsp">Exchanges</a></li>
                    <li <%=msgActive %>><a href="msg_list.jsp">Traces</a></li>
                    <li <%=sttActive %>><a href="statistic.jsp">Statistics</a></li>                    
                    <%-- <li <%=monitoringActive %>><a href="monitoring.jsp">Monitoring</a></li> --%>   
                </ul>
				<div class="uk-navbar-flip">
                     <ul class="uk-navbar-nav">
                     	<li><a href="<%=hawtioUrl %>" target="_blank">JMX</a></li>
                      	<li><a href="javascript:userDetailPage()">[ <%=session.getAttribute(Constants.SESSION_USERID)%> ]</a></li>
                        <li><a href="javascript:logout()">Logout</a></li>
                     </ul>
                </div>
                <a href="#tm-offcanvas" class="uk-navbar-toggle uk-visible-small" data-uk-offcanvas></a>
                <div class="uk-navbar-brand uk-navbar-center uk-visible-small">BizFrame API</div>
            </div>
        </nav>
    