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
        <div id="msgModal" class="uk-modal" aria-hidden="true" style="display: none; overflow-y: scroll;">
 			<div class="uk-modal-dialog trace-msg-dialog uk-overflow-auto">
				<button type="button" class="uk-modal-close uk-close"></button>
				<div class="uk-modal-header">
					<div id="exchangeIdDiv"><h2></h2></div>
				</div>
				<table class="uk-table">
               		<tr>
               			<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">RouteId</span></td>
               			<td style="padding-left:10px;text-align:left; border-bottom : 0px;"><div id="routeIdDiv" style="word-break:break-all;"></div></td>
               		</tr>
               		<tr>
               			<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">To Node</span></td>
               			<td style="padding-left:10px;text-align:left; border-bottom : 0px;"><div id="toNodeDiv" style="word-break:break-all;"></div></td>
               		</tr>
               		<tr>
               			<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">PreviousNode</span></td>
               			<td style="padding-left:10px;padding-top:15px;text-align:left; border-bottom : 0px;"><div id="previousNodeDiv" style="word-break:break-all;"></div></td>
               		</tr>               		
               		<!-- <tr>
               			<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">exchangePattern</span></td>
               			<td style="padding-left:10px;padding-top:15px;text-align:left; border-bottom : 0px;"><div id="exchangePatternDiv"></div></td>
               		</tr> -->
               		<tr>
               			<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">Exception</span></td>
               			<td style="padding-left:10px;padding-top:15px;text-align:left; border-bottom : 0px;"><div id="exceptionDiv" style="word-break:break-all;"></div></td>
               		</tr>
               		<tr>
               			<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">Headers</span></td>
               			<td style="padding-left:10px;padding-top:15px;text-align:left; border-bottom : 0px;"><div id="headersDiv" style="word-break:break-all;"></div></td>
               		</tr>
               		<tr>
               			<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">Body</span></td>
               			<td style="padding-left:10px;padding-top:15px;text-align:left; border-bottom : 0px;"><div id="bodyDiv" style="word-break:break-all;"></div></td>
               		</tr>
               		<tr>
               			<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">Out Headers</span></td>
               			<td style="padding-left:10px;padding-top:15px;text-align:left; border-bottom : 0px;"><div id="outheadersDiv" style="word-break:break-all;"></div></td>
               		</tr>
               		<tr>
               			<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">Out Body</span></td>
               			<td style="padding-left:10px;padding-top:15px;text-align:left; border-bottom : 0px;"><div id="outbodyDiv" style="word-break:break-all;"></div></td>
               		</tr>
             		
				</table>
				<div class="uk-modal-footer uk-text-right">
					<button type="button" class="uk-button uk-button-primary" onclick='javascript:UIkit.modal("#msgModal").hide();'>Close</button>
				</div>
			</div>
		</div>
		<script>
		function showMessage(id) {
			var info = {
					url : 'msg/' + id,
					dataType: "json",
					method: "get",
					callback: "getMessage",
					checkCallCondition: "no"
			};
			
			showLoading();
			callApi(info);
			
		}
		
		function getMessage(info, res) {
			hideLoading();			
			$("#routeIdDiv").html(res.routeId + ", " + res.fromEndpointUri);
			$("#toNodeDiv").html(res.toNode + ' ('+res.traceInOut.toUpperCase()+') ');
			$("#exchangeIdDiv").html('<h2>' + res.exchangeId + '</h2>');
			//$("#exchangePatternDiv").html(res.exchangePattern);
			var err = res.causedByException;
			if (err == null) {
				err = "";
			}
			$("#exceptionDiv").html('<font color="red">' + err + '</font>');			
			$("#headersDiv").html(res.headers);
			$("#bodyDiv").html(res.body);
			
			$("#outheadersDiv").html(res.outHeaders);
			$("#outbodyDiv").html(res.outBody);
			
			var pevNode = res.previousNode;
			if (pevNode == null) {
				pevNode = "";
			}
			$("#previousNodeDiv").html(pevNode);
			UIkit.modal("#msgModal").show();    
		}
		
		function viewBody(path) {
			UIkit.modal("#msgModal").hide();
			viewXml(path, 'Body File');
		}

		</script>