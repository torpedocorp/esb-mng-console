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
<!DOCTYPE html>
<html lang="kr" dir="ltr" class="uk-height-1-1">
<%@ include file="bizframe.jsp"%>
    <body class="tm-background">
		<%@ include file="header.jsp"%>
        <div class="tm-middle">
            <div class="uk-container uk-container-center">
				<div class="uk-grid">
				    <div class="uk-width-1-1">
				    	<h1 class="uk-heading-large">Trace Messages</h1>
				    </div>
				</div>
				<br>            	
           		<div class="uk-overflow-container">
           				<form class="uk-form" id="form1" name="form1">           					
						<input type="hidden" name="curPage" id="curPage" value="1">
						<%@ include file="searchOption.jsp"%>
						<div id="list"></div><br>
						<div id="listCount" class="listCount"></div>
						<div id="pagelist" class="paginate"></div>
						</form>
                 </div>
            </div>
        </div>
		<%@ include file="msgModal.jsp"%>
		<%@ include file="footer.jsp"%>
		<script src="js/components/datepicker.js"></script>
		<script src="js/list.js"></script>
		<script>
				
		var trInfo = {
				url : "msgs",
				dataType: 'json',
				method: 'post',
				checkCallCondition: 'no',
				form:'form[name=form1]',
				callback: "showDatas",
				displayId: "list",
				pagelist: true,
				fieldWidths: [15,20,10,6,15,13,21], //%
				fields: ["AgentId", "ExchangeId", "To Node", "InOut", "등록 일", "RouteId", "From Endpoint"],
				align:["left", "left", "left","left", "left", "left", "left"],
				jsonKey: ["agentId","exchangeId", "toNode", "traceInOut", "timestamp", "routeId", "fromEndpointUri"],
				detailMethod : "showMessage",
				detailLinkArgs : "id",
				detailLinkTarget : "exchangeId",
				colspan:7
		};
		
		$(function () {
			var html='<select style="border:1px solid #E5E5E5;" id="searchKey" name="searchKey">';
			html +='<option value="">Search Field</option>';
			html +='<option value="exchangeId">ExchangeId</option>';
			html +='<option value="routeId">RouteId</option>';
			html +='<option value="fromEndpointUri">From Endpoint</option>';
			html +='<option value="toNode">To Node</option>';
			html +='</select>&nbsp;&nbsp;&nbsp;';
			
			$("#searchKeyDiv").html(html);
			
			$('#f_date').val(beforeDay(30));
			$('#t_date').val(toDay());

			showLoading();
			callApi(trInfo);
		});		

		function showDatas(trInfo, res) {
			hideLoading();
			for ( var i = 0; i < res.messages.length; i++) {
				var ts = res.messages[i].timestamp;
				res.messages[i].timestamp = new Date(ts).format('yyyy-MM-dd HH:mm:ss');
				res.messages[i].traceInOut = res.messages[i].traceInOut.toUpperCase();
				res.messages[i].fromEndpointUri = '<div style="word-break:break-all;">' + res.messages[i].fromEndpointUri + '</div>';
				
				var err = res.messages[i].causedByException;
				if (err != undefined && err.length > 0) {
					res.messages[i].toNode = '<i class="uk-icon-exclamation-triangle" style="font-size:18px;color:#DF3A01;"></i> &nbsp;' + res.messages[i].toNode;  
				}
			}
			
			showListJson(trInfo, res);
		}

		</script>
    </body>
</html>