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
	    <style type="text/css">
		    #mynetwork {
		      width: 100%;
		      height: 600px;
		      border: 0px solid lightgray;
		    }
	  	</style>       		
		<%@ include file="header.jsp"%>
        <div class="tm-middle">		
			<div class="uk-container uk-container-center">
				<div class="uk-grid">
				    <div class="uk-width-2-3">
				    	<h1 class="uk-heading-large">ESB Network Status</h1>
				    </div>
				    <div class="uk-width-1-3" align="right">
						<button class="uk-button uk-button-primary" style="background-color: #1ABB9C;" type="button" data-uk-modal="{target:'#agentModal'}"><i class="uk-icon-plus"></i>&nbsp;Agent 등록</button>    
				    </div>
				</div>
				<br>			
				<div id="mynetwork" class="uk-overflow-container">
	            </div>			
			</div>
		</div>
		<div id="agentModal" class="uk-modal" aria-hidden="true" style="display: none; overflow-y: scroll;">
           <form class="uk-form" id="form1" name="form1" method="POST">
           <input type="hidden" name="toList" id="toList">				           
           <div class="uk-modal-dialog">
               <button type="button" class="uk-modal-close uk-close"></button>
               <div class="uk-modal-header">
                   <h2>ESB Agent 등록</h2>
               </div>
			   <table style="width: 100%;">
               		<tr>
                		<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">아이디 *</span></td>
                		<td style="text-align:left; border-bottom : 0px;"><input type="text" placeholder="Agent 아이디" id="agentId" name="agentId" style="width:300px;"></td>
                	</tr>
               		<tr>
                		<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">이름</span></td>
                		<td style="text-align:left; border-bottom : 0px;"><input type="text" placeholder="Agent 이름 " id="label" name="label" style="width:300px;"></td>
                	</tr>
                	<tr>
                		<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">Admin Command Port</span></td>
                		<td style="text-align:left; border-bottom : 0px;"><input type="text" placeholder="9004" id="adminPort" name="adminPort" style="width:300px;"></td>
                	</tr>
                	<tr>
                		<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">API Endpoint</span></td>
                		<td style="text-align:left; border-bottom : 0px;"><input type="text" placeholder="http://localhost:8080/hawtio/" id="jolokiaUrl" name="jolokiaUrl" style="width:450px;"></td>
                	</tr>
                	<tr>
                		<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">REMOTE JMX URL</span></td>
                		<td style="text-align:left; border-bottom : 0px;"><input type="text" placeholder="service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi/camel" id="jmxUrl" name="jmxUrl" style="width:450px;"></td>
                	</tr>
                	<tr>
                		<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">To ESB</span></td>
                		<td style="text-align:left; border-bottom : 0px;"><input type="text" placeholder="To ESB 아이디" id="toList0" name="toList0" style="width:300px;"></td>
                	</tr>
				</table>			   
               <div class="uk-modal-footer uk-text-right">
                   <button type="button" class="uk-button uk-modal-close">Cancel</button>
                   <button type="button" class="uk-button uk-button-primary" onclick="javascript:setAgent();">Save</button>
               </div>
           </div>
           </form>
		</div>
        <div id="monitoringModal" class="uk-modal" aria-hidden="true" style="display: none; overflow-y: scroll;">
 			<div class="uk-modal-dialog">
			<button type="button" class="uk-modal-close uk-close"></button>
				<div class="uk-modal-header">
					<div id="mAgentIdDiv"></div>
				</div>
				<table width="100%">
               		<tr>
               			<td style="text-align:left; border-bottom : 0px;width:30%;"><span class="uk-text-bold uk-text-middle">상태</span></td>
               			<td style="padding-left:10px;text-align:left; "><div id="mStatusDiv"></div></td>
               		</tr>				
               		<tr>
               			<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">이름</span></td>
               			<td style="padding-left:10px;text-align:left; "><div id="mLabelDiv"></div></td>
               		</tr>
               		<tr>
               			<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">Admin Command Port</span></td>
               			<td style="padding-left:10px;text-align:left; "><div id="mAdminPortDiv"></div></td>
               		</tr>
               		<tr>
               			<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">API Endpoint</span></td>
               			<td style="padding-left:10px;text-align:left; "><div id="mJolokiaUrlDiv"></div></td>
               		</tr>
               		<tr>
               			<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">REMOTE JMX URL</span></td>
               			<td style="padding-left:10px;text-align:left; "><div id="mJmxUrlDiv"></div></td>
               		</tr>
               		<tr>
               			<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">APP Status</span></td>
               			<td style="padding-left:10px;text-align:left; "><div id="mAppStatusDiv"></div></td>
               		</tr>
				</table>
				<div class="uk-modal-footer uk-text-right">
					<button type="button" class="uk-button uk-button-primary" onclick="refreshPage();">Close</button>
				</div>
			</div>
		</div>
		<%@ include file="footer.jsp"%>
		<script src="js/vis.js"></script>
		<script type="text/javascript">
			var info = {
					url : "monitoring/agent",
					dataType: "json",
					method: "get",
					callback: "draw",
					checkCallCondition: "no"
			};		

			function refreshPage() {
				UIkit.modal("#monitoringModal").hide();
				//location.reload();
			}
			
			$(function () {
				showLoading();
				callApi(info);
			});	

		    var network = null;
		
		    var DIR = 'img/';
		    var EDGE_LENGTH_MAIN = 200;
		
		    function createDrawData(res) {
			      // Create a data table with nodes.
			      var nodes = [];
			
			      // Create a data table with links.
			      var edges = [];
			      for (var i = 0; i < res.agents.length; i++) {
			    	  var node = res.agents[i];
			    	  var img='computer.png';
			    	  var size=25;
			    	  if (node.mine == true) {
			    		  img = "server.jpg";
			    		  size=40;
			    	  }
			    	  var color='black';
			    	  var fontsize = 15;
			    	  var status = '';		    	  
			    	  // app
			    	  if (node.serviceStatus) {
				    		for (var key in node.serviceStatus) {	
				    			if (getMASStatusIcon(node.serviceStatus[key]) == 'img/off.png') {
				    				color='#FF8000'; // check 필요
				    				fontsize = 23;
				    			}
				    		}
			    	  }
			    	  // mas
			    	  if (node.status != 'STARTED') {
			    		  color='red';
				    	  fontsize = 23;
			    	  }
			    	  
			    	  nodes.push({id: node.agentId, label: node.label, image: DIR + img, shape: 'image', size:size,font: {size:fontsize, color:color, background:'white'}});
			    	  if (node.toList != null) {
			    		  var toList = JSON.parse(node.toList);
				    	  for(var j=0; j < toList.length; j++) {
				    		  edges.push({from: node.agentId, to: toList[j], length: EDGE_LENGTH_MAIN, arrows:'to'});  
				    	  }
			    	  }
				  }
			      var data = {
					   nodes: nodes,
					   edges: edges
				  };
			      
			      return data;
		    }
		    
		    // Called when the Visualization API is loaded.
		    function draw(info, res) {     
		      
		      hideLoading();
		      
		      // create a network
		      var container = document.getElementById('mynetwork');
		      
		      var options = {
		        nodes: {
		        	//shadow:{color:'rgb(0,255,0)'}
		        	borderWidth:3,
		  	        color: {
		              //border: '#406897',
		              //background: '#6AAFFF'
		            },
		            //font:{color:'#eeeeee'},
		            shapeProperties: {
		              useBorderWithImage:true
		            }
		        }
		      };
		      options = {};
		      network = new vis.Network(container, createDrawData(res), options);
		      network.on("click", function (params) {
		          params.event = "[original event]";
		          var agentId = this.getNodeAt(params.pointer.DOM);
		          if (agentId != null && agentId != undefined) {
		        	  getAgentStatus(agentId);
		          }
		      });
		      
		      //network.storePositions();

		      // refresh auto
		      var timer = setInterval(function() {
					//showLoading();
					var info0 = {
							url : "monitoring/agent",
							dataType: "json",
							method: "get",
							callback: "setTheData",
							checkCallCondition: "no"
					};
					
					callApi(info0);
			  }, <%=monitorInterval * 1000%>);
		    }
		    
		    function getAgentStatus(id) {
		    	
		    	var info = {
						url : "monitoring/agent/" + id,
						dataType: "json",
						method: "get",
						callback: "viewAgentInfo",
						checkCallCondition: "no"
				};				
				
				showLoading();
				callApi(info);			
		    }
		    
		    function viewAgentInfo(info, res) {   
		    	hideLoading();
		    	var title = res.agentId;		    	
		    	if (res.lastUpdated) {
		    		title += '&nbsp;&nbsp;&nbsp;Last Updated : ' + new Date(res.lastUpdated).format('yyyy-MM-dd HH:mm:ss');
		    	}
		    	title += '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i class="uk-icon-trash" style="font-size:20px;" onclick="deleteAgent(\''+res.agentId+'\')"></i>';
		    	$("#mAgentIdDiv").html(title);
		    	
		    	$("#mStatusDiv").html('<img src="'+getMASStatusIcon(res.status)+'">');
		    	$("#mAdminPortDiv").html(res.adminPort);
		    	$("#mLabelDiv").html(res.label);
		    	$("#mJolokiaUrlDiv").html('<a href="'+res.jolokiaUrl+'" target="_blank">' + res.jolokiaUrl + '</a>');
		    	$("#mJmxUrlDiv").html(res.jmxUrl);
		    	if (res.serviceStatus) {
		    		var html = '';
		    		for (var key in res.serviceStatus) {						
						html += '<img src="'+getMASStatusIcon(res.serviceStatus[key])+'">';
						html += '&nbsp;&nbsp;';
						html += key;
						html += '<br>';
		    		}
		    		$("#mAppStatusDiv").html(html);
		    	}
		    	
		    	
		    	UIkit.modal("#monitoringModal").show();
		    }
		    
		    function setAgent() {
		    	var info = {
		    			url : "agent",
		    			dataType: "json",
		    			method: "POST",		
		    			checkCallCondition: "registCallCheckCondition",
		    			callback: "setResult",
		    			form:"form[name=form1]"
		    	};
		    	
		    	showLoading();
		    	callApi(info);
		    }
		    
		    function registCallCheckCondition() {
		    	var errMsgTitle = 'Agent 등록 : 오류';
		    	
		    	var value = $('#form1').find('#agentId').val();		    	
		    	if(value == null || value == '') {
		    		hideLoading();
		    		alertBox('Agent 아이디를 입력하세요.');
		    		return false;
		    	}

		    	value = $('#form1').find('#label').val();
		    	if(value == null || value == '') {
		    		hideLoading();
		    		alertBox('Agent 이름을 입력하세요.');
		    		return false;
		    	}
		    	
		    	value = $('#form1').find('#jolokiaUrl').val();
		    	if(value == null || value == '') {
		    		hideLoading();
		    		alertBox('모니터링 API Endpoint를 입력하세요.');
		    		return false;
		    	}

		    	/* value = $('#form1').find('#jmxUrl').val();
		    	if(value == null || value == '') {
		    		hideLoading();
		    		alertBox('원격 JMX Url을 입력하세요.');
		    		return false;
		    	} */
		    	
		    	value = $('#form1').find('#toList0').val();
		    	if(value != null && value.length > 0) {
		    		var arr = [value];
			    	var toListArr = JSON.stringify(arr);
			    	$("#toList").val(toListArr);	
		    	}
		    	
		    	return true;
		    }
		    
		    function setResult(info, res) {
		    	hideLoading();		    	
		    	location.reload();
		    }
		    
		    function getMASStatusIcon(status) {
		    	var icon = "";
		    	if (status == 'Started' || status == 'STARTED' || status == 'INITED') {
		    		icon = "img/on.png";
		    	} else {
		    		icon = "img/off.png";
		    	}
		    	return icon;
		    }
		    
		    function setTheData(info, res) {
		    	//hideLoading();
		    	var data = createDrawData(res);
		        network.setData({nodes:new vis.DataSet(data.nodes), edges:new vis.DataSet(data.edges)});
		    }
		    
		    function deleteAgent(agentId) {
		    	UIkit.modal.confirm(agentId + ' 모니터링을 취소하시겠습니까?', function() { 
		    		var info = {
			    			url : "/agent/" + agentId,
			    			dataType: "json",
			    			method: "delete",
							callback: "setResult",
							checkCallCondition: "no"
				    	};
				    	
				    	showLoading();
				    	callApi(info);
					
				});
		    }
		  
		  </script>		
    </body>
</html>