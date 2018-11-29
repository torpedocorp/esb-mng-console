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
<%@ include file="logincheck.jsp"%>
<%@ page import="kr.co.bizframe.esb.mng.utils.Strings"%>
<%
String aId = Strings.trim(request.getParameter("a"), "");
String rId = Strings.trim(request.getParameter("r"), "");
%>
<!DOCTYPE html>
<html lang="kr" dir="ltr" class="uk-height-1-1">
<%@ include file="bizframe.jsp"%>
    <body class="tm-background">
		<%@ include file="header.jsp"%>
        <div class="tm-middle">
            <div class="uk-container uk-container-center">
				<div class="uk-grid">
				    <div class="uk-width-1-1">
				    	<h1 class="uk-heading-large">Exchanges</h1>
				    </div>
				</div>
				<br>   	
           		<div class="uk-overflow-container">
           				<form class="uk-form" id="form2" name="form2">
							<input type="hidden" name="routeId" id="routeId" value="">
							<input type="hidden" name="agentId" id="agentId" value="">
							<input type="hidden" name="id" id="id" value="">
							<input type="hidden" name="strSearch" id="strSearch" value="">
							<input type="hidden" name="searchKey" id="searchKey" value="">
						</form>
           				<form class="uk-form" id="form1" name="form1">           					
						<input type="hidden" name="curPage" id="curPage" value="1">
						<input type="hidden" name="routeId" id="routeId" value="">
						<input type="hidden" name="agentId" id="agentId" value="">
						<%@ include file="searchOption.jsp"%>
						<div id="list"></div><br>
						<div id="listCount" class="listCount"></div>
						<div id="pagelist" class="paginate"></div>
						</form>
                 </div>
            </div>
        </div>
        <div id="traceModal" class="uk-modal" aria-hidden="true" style="display: none; overflow-y: scroll;">
 			<div class="uk-modal-dialog" style="width:800px;">
			<button type="button" class="uk-modal-close uk-close"></button>
				<div class="uk-modal-header">
					<div id="traceTitleDiv"></div>
					<table>
               		<tr>
               			<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">AgentId</span></td>
               			<td style="padding-left:10px;text-align:left; border-bottom : 0px;"><div id="agentIdDiv"></div></td>
               		</tr>				
               		<tr>
               			<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">RouteId</span></td>
               			<td style="padding-left:10px;text-align:left; border-bottom : 0px;"><div id="exRouteIdDiv"></div></td>
               		</tr>
               		<tr>
               			<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">From Endpoint</span></td>
               			<td style="padding-left:10px;text-align:left; border-bottom : 0px;"><div id="fromDiv" style="word-break:break-all;"></div></td>
               		</tr>
               		<tr>
               			<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">Status</span></td>
               			<td style="padding-left:10px;text-align:left; border-bottom : 0px;"><div id="statusDiv" style="word-break:break-all;"></div></td>
               		</tr>
					</table>
				</div>
								
				<div id="traceList"></div>
				
				<div class="uk-modal-footer uk-text-right">
					<button type="button" class="uk-button uk-button-primary" onclick='javascript:UIkit.modal("#traceModal").hide();'>Close</button>
				</div>
			</div>
		</div>
		<div id="toggleList"></div>
		<%@ include file="msgModal.jsp"%>
		<%@ include file="footer.jsp"%>
		<script src="js/components/datepicker.js"></script>
		<script src="js/underscore-min.js"></script>
		<script src="js/list.js"></script>
		<script>
		var trInfo = {
				url : "exchanges",
				dataType: 'json',
				method: 'post',
				checkCallCondition: 'no',
				form:'form[name=form1]',
				callback: "showDatas",
				displayId: "list",
				pagelist: true,
				fieldWidths: [15, 30, 15, 15, 20, 5], //%
				fields: ["AgentId", "ExchangeId", "RouteId", "Created", "Finished", "Status"],
				align:["left", "left","left", "left", "left", "center"],
				jsonKey: ["agentId", "exchangeId", "routeId", "created", "finished", "success"],
				detailMethod : "showTrace",
				detailLinkArgs : ["agentId","routeId","exchangeId", "id"],
				detailLinkTarget : "exchangeId",
				colspan:6
		};
		
		$(function () {
			var html='<select style="border:1px solid #E5E5E5;" id="searchKey" name="searchKey">';
			html +='<option value="">Search Field</option>';
			html +='<option value="agentId">AgentId</option>';
			html +='<option value="exchangeId">ExchangeId</option>';
			html +='<option value="routeId">RouteId</option>';
			html +='<option value="success">Success</option>';
			html +='</select>&nbsp;&nbsp;&nbsp;';
			
			$("#searchKeyDiv").html(html);
			var agentId = "<%=aId%>";
			var routeId = "<%=rId%>";
			if (agentId != '' && routeId !='') {
				$('#f_date').val(toDay());
				$('#form1').find("#agentId").val(agentId);
				$('#form1').find("#routeId").val(routeId);
			} else {
				$('#f_date').val(beforeDay(30));
			}
			$('#t_date').val(toDay());

			showLoading();
			callApi(trInfo);
		});

		function showDatas(trInfo, res) {			
			hideLoading();
			var html ="";
			for ( var i = 0; i < res.messages.length; i++) {
				var ts = res.messages[i].created;
				res.messages[i].created = new Date(ts).format('yyyy-MM-dd HH:mm:ss');
				var ts0 = res.messages[i].finished;
				var exec = ts0-ts;
				res.messages[i].finished = new Date(ts0).format('yyyy-MM-dd HH:mm:ss') + "   (" +exec+ " ms)";
				var result = res.messages[i].success;
				
				if (result == true) {
					res.messages[i].success = '<i class="uk-icon-check-circle" style="font-size:20px;color:#4B8A08;"></i>';
				} else {
					res.messages[i].success = '<i class="uk-icon-exclamation-triangle" style="font-size:18px;color:#DF3A01;" onclick="javascript:viewError(\''+res.messages[i].errorMsg+'\');"></i>';
				}
			}
			showListJson(trInfo, res);
			
		}

		function viewError(error) {
			alertBox(error);
		}
		
		function showTrace(agentId, routeId, exchangeId, id) {
			var info = {
					url : "exchange/traces",
					dataType: 'json',
					method: 'post',
					checkCallCondition: 'no',
					form:'form[name=form2]',
					callback: "viewTraceMessages",
			};			
			$('#form2').find("#searchKey").val("exchangeId");
			$('#form2').find("#strSearch").val(exchangeId);
			$('#form2').find("#agentId").val(agentId);
			$('#form2').find("#routeId").val(routeId);
			$('#form2').find("#id").val(id);
			
			showLoading();
			callApi(info);
		}
		
		function viewTraceMessages(info, res) {
			hideLoading();
			var trInfo0 = {
					fieldWidths: [40,30,30], //%
					fields: ["To Node", "Time", "InOut"],
					align:["left","left", "left"],
					jsonKey: ["toNode", "timestamp", "traceInOut"],
					colspan:5
			};
			
			var from = "";
			var groupby = _.groupBy(res.messages, function(row) {
				return row.toNode;
			});
			
			var body = '<table class="uk-table">';
			body += listHeader(trInfo0);			
			body += '<tbody>';
			var j = 0;
			for (key in groupby) {				
				var arr = groupby[key];	
				var cnt = arr.length;
				if (j == 0) {
					body += '<tr>';
					body += '<td>'+arr[0].previousNode+'</td>';
					body += '<td></td>';
					body += '<td></td>';
					body += '</tr>';
				}
				for (var i = 0; i < cnt; i++) {					
					body += '<tr>';
					if (i == 0) {						
						body += '<td rowspan="'+cnt+'">'+key+'</td>';
					}
					var ts = arr[i].timestamp;
					ts = new Date(ts).format('yyyy-MM-dd HH:mm:ss');					 
					from = arr[i].fromEndpointUri;
					var err = arr[i].causedByException;
					var errIcon ="";
					if (err != undefined && err.length > 0) {
						errIcon = '<i class="uk-icon-exclamation-triangle" style="font-size:18px;color:#DF3A01;"></i> &nbsp;';
					}
					body += '<td>'+ts+'</td>';
					body += '<td>'+errIcon+'<a href="javascript:showMessage(\''+arr[i].id+'\')">'+arr[i].traceInOut.toUpperCase()+'</a></td>';
					body += '</tr>';
				}
				j++;
			};
			$("#traceList").html(body);
			/* 
			for ( var i = 0; i < res.messages.length; i++) {
				var ts = res.messages[i].timestamp;
				res.messages[i].timestamp = new Date(ts).format('yyyy-MM-dd HH:mm:ss');
				res.messages[i].traceInOut = res.messages[i].traceInOut.toUpperCase(); 
				from = res.messages[i].fromEndpointUri;
			}
			showListJson(trInfo0, res); */
			
			var status = "";			
			if (res.exchange.success == true) {
				status = '<i class="uk-icon-check-circle" style="font-size:20px;color:#4B8A08;"></i>';
			} else {
				status = '<i class="uk-icon-exclamation-triangle" style="font-size:18px;color:#DF3A01;"></i> &nbsp;' + res.exchange.errorMsg;
			}
			$("#traceTitleDiv").html('<h2>' + res.exchange.exchangeId + '</h2>');
			$("#agentIdDiv").html(res.exchange.agentId);
			$("#exRouteIdDiv").html(res.exchange.routeId);
			$("#fromDiv").html(from);
			$("#statusDiv").html(status);
			UIkit.modal("#traceModal", {modal: false}).show();  
		}
		</script>
    </body>
</html>