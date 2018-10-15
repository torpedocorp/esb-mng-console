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
				    <div class="uk-width-2-3">
				    	<h1 class="uk-heading-large">Camel RealTime Monitoring</h1>
				    </div>
				    <div class="uk-width-1-3" align="right">
						<button class="uk-button uk-button-primary" style="background-color: #1ABB9C;" type="button" data-uk-modal="{target:'#agentModal'}"><i class="uk-icon-plus"></i>&nbsp;Target Route 등록</button>    
				    </div>
				</div>
				<br>			
				<div id="mynetwork" class="uk-overflow-container">
	            </div>			
			</div>
		</div>
		<div id="agentModal" class="uk-modal" aria-hidden="true" style="display: none; overflow-y: scroll;">
           <form class="uk-form" id="form1" name="form1" method="POST">
           <input type="hidden" name="selectedId" id="selectedId">      
           <div class="uk-modal-dialog">
               <button type="button" class="uk-modal-close uk-close"></button>
               <div class="uk-modal-header">
                   <h2>Target Route 등록</h2>
               </div>
               <h1 class="tm-article-subtitle">From</h1>
			   <table style="width: 100%;">
               		<tr>
                		<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">아이디 *</span></td>
                		<td style="text-align:left; border-bottom : 0px;"><input type="text" placeholder="Agent 아이디" id="fromAgentId" name="fromAgentId" style="width:300px;"></td>
                	</tr>
               		<tr>
                		<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">이름</span></td>
                		<td style="text-align:left; border-bottom : 0px;"><input type="text" placeholder="Agent 이름 " id="fromLabel" name="fromLabel" style="width:300px;"></td>
                	</tr>
                	<tr>
                		<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">API Endpoint</span></td>
                		<td style="text-align:left; border-bottom : 0px;"><input type="text" placeholder="http://localhost:8080/hawtio/" id="fromJolokiaUrl" name="fromJolokiaUrl" style="width:450px;"></td>
                	</tr>
                	<tr>
                		<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">Route</span></td>
                		<td style="text-align:left; border-bottom : 0px;"><input type="text" placeholder="Camel Route Id" id="fromRouteId" name="fromRouteId" style="width:300px;"></td>
                	</tr>
				</table>
				<br>
			   <h1 class="tm-article-subtitle">To</h1>
			   <table style="width: 100%;">
               		<tr>
                		<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">아이디 *</span></td>
                		<td style="text-align:left; border-bottom : 0px;"><input type="text" placeholder="Agent 아이디" id="toAgentId" name="toAgentId" style="width:300px;"></td>
                	</tr>
               		<tr>
                		<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">이름</span></td>
                		<td style="text-align:left; border-bottom : 0px;"><input type="text" placeholder="Agent 이름 " id="toLabel" name="toLabel" style="width:300px;"></td>
                	</tr>
                	<tr>
                		<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">API Endpoint</span></td>
                		<td style="text-align:left; border-bottom : 0px;"><input type="text" placeholder="http://localhost:8081/hawtio/" id="toJolokiaUrl" name="toJolokiaUrl" style="width:450px;"></td>
                	</tr>
                	<tr>
                		<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">Route</span></td>
                		<td style="text-align:left; border-bottom : 0px;"><input type="text" placeholder="Camel Route Id" id="toRouteId" name="toRouteId" style="width:300px;"></td>
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
<!--                		<tr>
               			<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">REMOTE JMX URL</span></td>
               			<td style="padding-left:10px;text-align:left; "><div id="mJmxUrlDiv"></div></td>
               		</tr> -->
               		<tr>
               			<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">App Status</span></td>
               			<td style="padding-left:10px;text-align:left; "><div id="mAppStatusDiv"></div></td>
               		</tr>            		
               		<tr><td colspan="2"><br></td></tr>
               		<tr>
               			<td style="text-align:left;border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">Router Status</span></td>
               			<td style="padding-left:10px;text-align:left; "><div id="mRouterStatusDiv"></div></td>
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

			function refreshPage() {
				UIkit.modal("#monitoringModal").hide();
				//location.reload();
			}
			
			$(function () {
				showLoading();
				var info = {
						url : "monitoring/agent",
						dataType: "json",
						method: "get",
						callback: "draw",
						checkCallCondition: "no"
				};
				callApi(info);
			});

		    var network = null;
	         
		    var DIR = 'img/';
		    var EDGE_LENGTH_MAIN = 250;
		    var groupIds;
		    function createDrawData(res) {
			      // Create a data table with nodes.
			      var nodes = [];			      
			      groupIds = [];
			      // Create a data table with links.
			      var edges = [];
			      var routeDatas = res.routeDatas;
			      
			      for (var i = 0; i < res.agents.length; i++) {
			    	  var agent = res.agents[i];
			    	  var color='black';
			    	  var fontsize = 15;
			    	  var routeToList = agent.routeToList;
			    	  if (routeToList) {
			    	  	  for (var routeId in routeToList) {
			    	  		 // route
			    	  		 var status = agent.routeStatus[routeId];
						     if (getStatusIcon(status) == 'img/off.png') {
						    	 color='#FF8000'; // check 필요
						    	 fontsize = 20;
						     }
					    	  
					    	  // mas
					    	 if (agent.status != 'STARTED') {
					    		  color='red';
						    	  fontsize = 23;
					    	 }
					    	  
					    	 var nodeId = agent.agentId + '_' + routeId;					    	 
					    	 var sttInfo = "";
					    	 var nodeRouteData =routeDatas[nodeId];
					    	 if (nodeRouteData) {					    		
					    		 sttInfo = nodeRouteData.total + " ("+nodeRouteData.success+" / "+nodeRouteData.fail+")";
					    	 }
					    	 var label = agent.agentId;
					    	 if (agent.label) {
					    		 label = agent.label;
					    	 }
					    	 groupIds.push = agent.agentId;
					    	 nodes.push({id: nodeId, color: '#A9BCF5', group: agent.agentId, font: { multi: 'html', size: 15, color:color }, label: '<b>'+label+'</b>\n<b><i>'+routeId+'</i></b>\n\n<i>'+sttInfo+'</i>'});
					    	 var toList = routeToList[routeId];
					    	 if (toList) {		    		  
						    	  for(var j=0; j < toList.length; j++) {						    		  
						    		  edges.push({from: nodeId, to: toList[j], length: EDGE_LENGTH_MAIN, arrows:'to'});  
						    	  }
					    	 }
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
		      var groupOpts = {};
		      for (var i = 0; i < res.agents.length; i++) {
		    	  var agent = res.agents[i];		    	  
		    	  groupOpts[agent.agentId] ={color:'#A9D0F5'};
		      }
		      		      
		      // create a network
		      var container = document.getElementById('mynetwork');
			  var options = {
					    physics:{
				          barnesHut:{gravitationalConstant:-2000}
				        },
				        //physics : false,
				        /* layout: {randomSeed:0}, */
				         "layout": {
						    "hierarchical": {
						      "enabled": false
						    }
						 },
					    height: '100%',
					    width: '100%',
						edges : {
							font : {
								size : 12
							}
						},
						groups: groupOpts,
						nodes : {
							widthConstraint: { minimum: 120, maximum: 120,},						   			
							shape : 'box',
							font : {
								bold : {
									color : '#0077aa'
								},
								ital : {
									color : '#170B3B',
									size : 15
								},
							},
							size : 15,
						}
				};
			  
			    var data = createDrawData(res);
				var x = - container.clientWidth / 2 + 50;
			    var y = - container.clientHeight / 2 + 50;
			    var step = 70;
			    for (var i = 0; i < res.agents.length; i++) {
			    	var agent = res.agents[i];	   
  		    		var x0 = (i * step) + x 
  		    		//data.nodes.push({id: "agentId _ " + agent.agentId, x: x0, y: y, label: agent.label, group: agent.agentId, fixed: true, physics:false});					
			    }
			    
				network = new vis.Network(container, data, options);
				network.on("click", function(params) {
					params.event = "[original event]";
					var nodeId = this.getNodeAt(params.pointer.DOM);
					if (nodeId != null && nodeId != undefined) {
						getAgentStatus(nodeId);
					}
				});
			      
				//network.storePositions();

				// refresh auto
				var timer = setInterval(function() {
					//showLoading();
					var info0 = {
						url : "monitoring/agent",
						dataType : "json",
						method : "get",
						callback : "setTheData",
						checkCallCondition : "no"
					};

					callApi(info0);
				}, <%=monitorInterval * 1000%>);
		    }		    
		    
		    function setTheData(info, res) {
		    	//hideLoading();
		    	var data = createDrawData(res);
		        //network.setData({nodes:new vis.DataSet(data.nodes), edges:new vis.DataSet(data.edges)});
		    	network.body.data.nodes.update(data.nodes);
		    	// TODO 엣지, node 추가 시에는 화면 새로고침을 하므로..문제 없을거 같음
				//network.body.data.edges.update(data.edges);
		    }		    
		    
		    function getAgentStatus(id) {
		    	$("#selectedId").val(id);
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
		    	var nodeId = $("#selectedId").val();
		    	var prefix = res.agentId+"_";
		    	var idx=nodeId.indexOf(prefix, 0);		    	
		    	var selectRouteId = nodeId.substring(prefix.length);		    	
		    	var title = res.agentId;
		    	title += ', ' + selectRouteId;
		    	if (res.lastUpdated) {
		    		title += '&nbsp;&nbsp;&nbsp;Last Updated : ' + new Date(res.lastUpdated).format('yyyy-MM-dd HH:mm:ss');
		    	}
		    	//title += '&nbsp;&nbsp;&nbsp;&nbsp;<i class="uk-icon-table" style="font-size:20px;" onclick="viewExchangeInfos(\''+res.agentId+'\',\''+selectRouteId+'\')"></i>';
		    	title += '&nbsp;&nbsp;<i class="uk-icon-trash" style="font-size:20px;" onclick="deleteAgent(\''+res.agentId+'\', \''+selectRouteId+'\')"></i>';
		    	
		    	$("#mAgentIdDiv").html(title);
		    	
		    	$("#mStatusDiv").html('<img src="'+getStatusIcon(res.status)+'">');
		    	$("#mAdminPortDiv").html(res.adminPort);
		    	$("#mLabelDiv").html(res.label);
		    	$("#mJolokiaUrlDiv").html('<a href="'+res.jolokiaUrl+'" target="_blank">' + res.jolokiaUrl + '</a>');
		    	//$("#mJmxUrlDiv").html(res.jmxUrl);
		    	if (res.serviceStatus) {
		    		var html = '';
		    		for (var key in res.serviceStatus) {						
						html += '<img src="'+getStatusIcon(res.serviceStatus[key])+'">';
						html += '&nbsp;&nbsp;';
						html += key;
						html += '<br>';
		    		}
		    		$("#mAppStatusDiv").html(html);
		    	}
		    	var existRoute = false;
		    	if (res.routeStatus) {
		    		var html = '';
		    		for (var key in res.routeStatus) {						
						html += '<img src="'+getStatusIcon(res.routeStatus[key])+'">';
						html += '&nbsp;&nbsp;';
						if (key == selectRouteId) {
							existRoute = true;
							html += '<a href="#" onclick="viewExchangeInfos(\''+res.agentId+'\',\''+selectRouteId+'\')"><b><i><u><font color="#1D8ACB">';
						}
						html += key;
						if (key == selectRouteId) {
							html += "</font></u></i></b></a>";
						}
						html += '<br>';
		    		}
		    		if (existRoute == false) {
		    			html += '<i class="uk-icon-times" style="font-size:20px;color:red;" onclick="deleteAgent(\''+nodeId+'\')"></i>';
						html += '&nbsp;&nbsp;';
						html += '<a href="#" onclick="viewExchangeInfos(\''+res.agentId+'\',\''+selectRouteId+'\')"><b><i><u><font color="#1D8ACB">';
						html += selectRouteId;
						html += "</font></u></i></b></a>";
		    		}
		    		$("#mRouterStatusDiv").html(html);
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
		    	
		    	var value = $('#form1').find('#fromAgentId').val();		    	
		    	if(value == null || value == '') {
		    		hideLoading();
		    		alertBox('From Agent 아이디를 입력하세요.');
		    		return false;
		    	}

		    	/* value = $('#form1').find('#fromJolokiaUrl').val();
		    	if(value == null || value == '') {
		    		hideLoading();
		    		alertBox('From 모니터링 API Endpoint를 입력하세요.');
		    		return false;
		    	} */
		    	
		    	value = $('#form1').find('#fromRouteId').val();
		    	if(value == null || value == '') {
		    		hideLoading();
		    		alertBox('From 모니터링 RouteID 를 입력하세요.');
		    		return false;
		    	}

		    	/* value = $('#form1').find('#jmxUrl').val();
		    	if(value == null || value == '') {
		    		hideLoading();
		    		alertBox('원격 JMX Url을 입력하세요.');
		    		return false;
		    	} */
		    	
		    	value = $('#form1').find('#toAgentId').val();
		    	if(value != null && value.length > 0) {
		    		value = $('#form1').find('#toJolokiaUrl').val();
			    	/* if(value == null || value == '') {
			    		hideLoading();
			    		alertBox('To 모니터링 API Endpoint를 입력하세요.');
			    		return false;
			    	}
			    	 */
			    	value = $('#form1').find('#toRouteId').val();
			    	if(value == null || value == '') {
			    		hideLoading();
			    		alertBox('To 모니터링 RouteID 를 입력하세요.');
			    		return false;
			    	}
		    	}
		    	
		    	return true;
		    }
		    
		    function setResult(info, res) {
		    	hideLoading();		    	
		    	location.reload();
		    }
		    
		    function getStatusIcon(status) {
		    	var icon = "";
		    	if (status == 'Started' || status == 'STARTED' || status == 'INITED') {
		    		icon = "img/on.png";
		    	} else {
		    		icon = "img/off.png";
		    	}
		    	return icon;
		    }

		    function deleteAgent(agentId, routeId) {
		    	UIkit.modal.confirm(agentId + ', '+routeId+' 모니터링을 취소하시겠습니까?', function() { 
		    		var info = {
			    			url : "/agent/" + agentId +"/" + routeId,
			    			dataType: "json",
			    			method: "delete",
							callback: "setResult",
							checkCallCondition: "no"
				    	};
				    	
				    	showLoading();
				    	callApi(info);
					
				});
		    }
		    
		    function viewExchangeInfos(agentId, routeId) {
		    	/* UIkit.modal("#monitoringModal").hide();
		    	UIkit.modal("#monitoringModal").show(); */
		    	location.href="exchange_list.jsp?a=" + agentId + "&r="+ routeId;
		    }
		  
		  </script>		
    </body>
</html>