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
<%@ page import="java.net.URLEncoder"%>
<%@ page import="java.nio.charset.StandardCharsets"%>
<%@ page import="java.net.URLDecoder"%>
<%@ page import="kr.co.bizframe.esb.mng.utils.Strings"%>
<%@ page import="kr.co.bizframe.esb.mng.type.MonitoringURL"%>
<%@ include file="logincheck.jsp"%>
<%
String id = request.getParameter("id");
String camelIds = request.getParameter("camelIds");
String appPath = Strings.trim(request.getParameter("appPath"));
if (appPath != null) {
	String enc = StandardCharsets.UTF_8.toString();
	appPath = URLDecoder.decode(appPath, enc);	
	appPath +="/classes/camel-route.xml";
	appPath = URLEncoder.encode(appPath, enc);
}

%>
<!DOCTYPE html>
<html lang="kr" dir="ltr" class="uk-height-1-1">
<%@ include file="bizframe.jsp"%>
    <body class="tm-background">   		
		<%@ include file="header.jsp"%>
		<link href="css/jstree/jstree-style.css" rel="stylesheet" type="text/css">
        <div class="tm-middle">
            <div class="uk-container uk-container-center">
				<div class="uk-grid">
				    <div class="uk-width-4-5" id="nameDiv"></div>
				    <div class="uk-width-1-5" align="right"><button class="uk-button" type="button" onclick="location.href='app_list.jsp'"><i class="uk-icon-angle-double-left"></i> GO BACK</button></div>
				</div>
				<br>          		
           		<div class="uk-overflow-container">
          			<div id="contextsDiv"></div>
          			<br><br>
          			<dl class="uk-description-list-horizontal">
		                	<dt>&nbsp;&nbsp;Route 상세정보</dt>
		                    <dd><span class="uk-badge" style="background: #848484;font-size:12px;" onclick=viewRouteXml()>RouteXML</span></dd>
		            </dl>
          			<div id="routesDiv">
						<table class="uk-table" id="routeTbl">
					    	<thead>
					        	<tr>
					        		<th align="left">아이디</th>
					        		<th align="left">Endpoint</th>
					        		<th align="left">상태</th>
					                <th align="left">제어</th>
					                <th align="left">Uptime</th>
					             </tr>
					       </thead>
					       <tbody></tbody>
				       </table>
					    <br>
					    <dl class="uk-description-list-horizontal">
			                	<dt>&nbsp;&nbsp;Exchanges 통계</dt>
			                    <dd></dd>
			            </dl>
			            <table class="uk-table" id="routeSttTbl">
					    	<thead>
					        	<tr>
					        		<th align="left">Total</th>
					        		<th align="left">Completed</th>
					        		<th align="left">Failed</th>
					                <th align="left">Inflight</th>
					                <th align="left">마지막 수행시간</th>
					                <th align="left">수행시간(평균/맥스) ms</th>
					             </tr>
					       </thead>
					       <tbody></tbody>
					    </table>
          			</div>
                </div>
            </div>
        </div>
        
		<%@ include file="footer.jsp"%>
		<script type="text/javascript" src="js/jstree.js"></script>
		<script src="js/jmx.js"></script>
		<script>
		var opened = true;
		var arrayCollection=[];
		
		$(function () {
			$("#nameDiv").html('<h1 class="uk-heading-large">애플리케이션&nbsp;<font class="uk-text-primary uk-text-bold"><%=id%></font></h1>');
			var camelIdsString = "<%=camelIds%>";			
			makeTree(camelIdsString);
		});
		
		function makeTree(camelIdsString) {
			var camelIds = camelIdsString.split(",");
			arrayCollection.push({"id" : "all", 
				"text" : "Camel Contexts",
				"icon" : "img/tree_icon_open.png",
				"state" : {"opened" : opened},
				"parent": "#"
			});
			
			if (camelIds) {
				  for(var i=0 ; i<camelIds.length ; i++){
					  var contextId = camelIds[i];
					  var children = {
						 "id" : contextId,
						 "text" : contextId,
						 "icon" : "img/tree_icon.png",
						 "state" : {"opened" : opened},
						 "parent": "all",
					  }
					  arrayCollection.push(children);
					  callRouteInfos(contextId);
				  }
			}
			$('#contextsDiv').jstree({
				'core' : {
					'data' : arrayCollection
				},
                "plugins": ["wholerow"]
			})/* .bind("select_node.jstree", function (event, data) {
				callRouteInfos(data.node.id);
			}) */; 

		}
		
        function resfreshJSTree() {
            $('#contextsDiv').jstree(true).settings.core.data = arrayCollection;
            $('#contextsDiv').jstree(true).refresh();
        }
        
		function callRouteInfos(contextId) {
			var url0 = "<%=MonitoringURL.CAMEL_ROUTE_INFO%>";
			url0 = url0.replace("[CONTEXT_ID]", contextId);
			var info = {					
					url : "<%=(hawtioUrl +  MonitoringURL.JOLOKIA_API_READ_PREFIX)%>" + url0,
					dataType: "json",
					method: "get",
					callback: "addRouteInfos",
					checkCallCondition: "no",
					parent: contextId,
			};
			
			showLoading();
			callApi(info);
		}
		
		function addRouteInfos(info, res) {
			hideLoading();
			var messages = res.value;
			var contextId = info.parent;
			
			for (var key in messages) {
				var app = messages[key];
				var routeId = app.RouteId;
				var endpoint = app.EndpointUri;
				var parent = app.CamelId;
				var status = app.State;
				var icon = getStatusIcon(status);				
				var children = {
						 "id" : contextId + "_route_" + routeId,
						 "text" : "routes",
						 "icon" : "",
						 "state" : {"opened" : opened},
						 "parent": contextId,						 
				}
				arrayCollection.push(children);

				children = {
						 "id" : routeId,
						 "text" : '<a href="#" onclick="callRouteInfo(\''+contextId+'\',\''+routeId+'\')">' + routeId + '</a>',
						 "icon" : icon,
						 "state" : {"opened" : opened},
						 "parent": contextId + "_route_" + routeId,						 
				}
				arrayCollection.push(children);
				/* children = {
						 "id" : endpoint,
						 "text" : endpoint,
						 "icon" : "img/tree_icon.png",
						 "state" : {"opened" : opened},
						 "parent": routeId,						 
				}
				arrayCollection.push(children); */
				resfreshJSTree();
			}
		}

		function callRouteInfo(contextId, routeId) {			
			var jmxId= 'org.apache.camel:context='+contextId+',type=routes,name=!"'+routeId+'!"';
			var info = {
					url : '<%=hawtioUrl %>/jolokia/read/' + jmxId,
					dataType: "json",
					method: "get",
					callback: "addRouteInfo",
					checkCallCondition: "no",
					jmxId : jmxId,
			};
			
			showLoading();
			callApi(info);
		}

		
		function addRouteInfo(info, res) {
			hideLoading();
			$("#routeTbl tbody tr" ).each( function(){
				this.parentNode.removeChild( this );
			});
			
			$("#routeSttTbl tbody tr" ).each( function(){
				this.parentNode.removeChild( this );
			});
			
			var app = res.value;
			var routeId = app.RouteId;
			var endpoint = app.EndpointUri;
			var parent = app.CamelId;
			var status = app.State;
			var upTime = app.Uptime;
			var icon = getStatusIcon(status);
			var jmxId = info.jmxId;
			var control="";
			if (icon == 'img/on.png') {
				control = '<i class="uk-icon-stop-circle" style="font-size:25px;" onclick=stopApp(\''+jmxId+'\')></i>';				
			} else if (icon == 'img/off.png') {
				control = '<i class="uk-icon-play-circle" style="color: #FF8000;font-size:25px;" onclick=startApp(\''+jmxId+'\')></i>';
			}
			
			var html = "<tr>";
			html += '<td>'+routeId+'</td>';
			html += '<td>'+endpoint+'</td>';
			html += '<td><img src="'+icon+'"></td>';				
			html += '<td>'+control+'</td>';				
			html += '<td>'+upTime+'</td>';
			html += '</tr>';
			$("#routeTbl").append(html);
			
			
			html = "<tr>";
			html += '<td>'+app.ExchangesTotal+'</td>';
			html += '<td>'+app.ExchangesCompleted+'</td>';
			html += '<td>'+app.ExchangesFailed+'</td>';				
			html += '<td>'+app.InflightExchanges+'</td>';
			html += '<td>'+app.LastExchangeCompletedTimestamp+'</td>';
			html += '<td>'+app.MeanProcessingTime+' / '+app.MaxProcessingTime+'</td>';
			html += '</tr>';
			$("#routeSttTbl").append(html);
			
		}
		
		function viewRouteXml() {
			viewXml('<%=appPath%>', 'CamelRoute Xml');
		}
		
		</script>
    </body>
</html>