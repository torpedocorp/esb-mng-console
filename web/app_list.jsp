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
<%@ page import="kr.co.bizframe.esb.mng.type.MonitoringURL"%>
<%@ page import="kr.co.bizframe.esb.mng.type.Constants"%>
<%@ include file="logincheck.jsp"%>

<!DOCTYPE html>
<html lang="kr" dir="ltr" class="uk-height-1-1">
<%@ include file="bizframe.jsp"%>
    <body class="tm-background">
		<%@ include file="header.jsp"%>
        <div class="tm-middle">
            <div class="uk-container uk-container-center">
				<div class="uk-grid">
				    <div class="uk-width-2-5">
				    	<h1 class="uk-heading-large">애플리케이션</h1>
				    </div>
				    <div class="uk-width-3-5" align="right">
				    	<span style="padding-right:10px;height:28px;margin-top:5px;font-weight: bold;">
							Total : <span style="font-weight: bold;color:#39baea;" id="totalTopCnt"></span> 건
						</span>
				    </div>
				    <!-- <div class="uk-width-1-5" align="right">
						<button class="uk-button uk-button-primary" style="background-color: #1ABB9C;" type="button" onclick='location.href="plugin_regist.jsp"'><i class="uk-icon-plus"></i>&nbsp;애플리케이션 디플로이</button>    
				    </div> -->
				</div>
				<br>          		
           		<div class="uk-overflow-container">          			
					<table class="uk-table" id="appListTbl">
				    	<thead>
				        	<tr>
				        		<th align="left">로딩순서</th>
				                <th align="left">아이디</th>
				                <th align="left">상태</th>
				                <th align="left">제어</th>
				                <th align="left">제어 시간</th>				                
				                <th align="left">XML</th>
				             </tr>
				       </thead>
				       <tbody></tbody>
				    </table>
				    <form id="form1" name="form1" action="app_camel_detail.jsp" method="post">
				    	<input type="hidden" name="id" id="id">
				    	<input type="hidden" name="camelIds" id="camelIds">
				    	<input type="hidden" name="appPath" id="appPath">
					</form>
                 </div>
            </div>
        </div>
        
		<%@ include file="footer.jsp"%>
		<script src="js/underscore-min.js"></script>
		<script src="js/jmx.js"></script>
		<script>
		var trInfo = {
				url : "<%=(hawtioUrl + MonitoringURL.JOLOKIA_API_READ_PREFIX + MonitoringURL.APPS_INFO)%>",
				dataType: "json",
				method: "get",
				callback: "showDatas",
				checkCallCondition: "no"
		};
		
		$(function () {
			showLoading();
			callApi(trInfo);
		});		

		function showDatas(trInfo, res) {			
			$("#appListTbl tbody tr" ).each( function(){
				this.parentNode.removeChild( this );
			});
			
			hideLoading();
			var messages = res.value;
			var totalCnt = 0;			

			var arr = [];
			for (var key in messages) {
				var app = messages[key];
				var data = {};				
				data["key"] = key;
				data["LoadSequence"] = app.LoadSequence;
				arr.push(data);
			}
			
			_.sortBy(arr, 'LoadSequence').forEach(function(m) {
				var key = m.key;
				var app = messages[key];
				var id = /<%=Constants.MAS_APP_ID_PATTERN%>/.exec(key)[1];
				var time = app.StartTime;
				var control = "";
				var idString = id;
				var camelIds="";
				var appPath = encodeURIComponent(app.ApplicationPath + "/application.xml");
				if (app.Serviceable) {
					if (app.Status == 'STARTED') {
						control = '<i class="uk-icon-stop-circle" style="font-size:25px;" onclick="stopApp(\''+key+'\')"></i>';
						if (app.ApplicationType == "<%=MonitoringURL.CAMEL_APPTYPE%>") {
							/* var json = JSON.parse('{"came-http-server":{"http-server":"Started","sendTrace":"Started"}}');
							console.log(json); */
							var json = JSON.parse(app.SubManagementInfos);
							for (var key in json) {
								if (camelIds.length > 0) {
									camelIds += ",";	
								}
								camelIds += key;
							}
																				
							idString += '&nbsp;&nbsp;&nbsp;<i class="uk-icon-angle-double-right" onclick="detailCamel(\''+id+'\',\''+camelIds+'\',\''+encodeURIComponent(app.ApplicationPath)+'\')"></i>';
						}
					} else if (app.Status == 'STOPPED') {						
						time = app.StopTime;
						control = '<i class="uk-icon-play-circle" style="color: #FF8000;font-size:25px;" onclick="startApp(\''+key+'\')"></i>';
					}
				} else {
					time = app.InitTime;
				}
				
				if (app.Status == 'PRELOADING') {
					time = "";
				}
				var html = "<tr>";
				html += '<td>'+app.LoadSequence+'</td>';
				html += '<td>'+idString+'</td>';				
				html += '<td><img src="'+getStatusIcon(app.Status)+'"></td>';							
				html += '<td>'+control+'</td>';
				html += '<td>'+time+'</td>';
				html += '<td><span class="uk-badge" style="background: #848484;font-size:12px;" onclick="viewXml(\''+appPath+'\', \'애플리케이션 Xml\')">View</span></td>';
				html += '</tr>';
				$("#appListTbl").append(html);
				totalCnt++;
			});
			
			$('#totalTopCnt').html(totalCnt);
		}

		function detailCamel(id, camelIds, path) {
			$('#id').val(id);
			$('#camelIds').val(camelIds);
			$('#appPath').val(path);
			$('#form1').submit();			
		}


		function camelRouteDumpResult(info, res) {
			hideLoading();			
			var result = res.status;
			if (result == '200') {
				$("#toggleTest_camel-http-server").text(res.value);
			} else {
				alertBox(info.jmxOp + ' : 오류<br>' + res.value);
			}
		}

		</script>
    </body>
</html>