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

function getStatusIcon(status) {
	var icon = "";
	if (status == 'Started' || status == 'STARTED' || status == 'INITED') {
		icon = "img/on.png";
	} else if (status == 'Stopped' || status == 'STOPPED') {
		icon = "img/off.png";
	}
	return icon;
}

function operationApp(key, operation, func) {
	//http://192.168.10.101:8080/hawtio/jolokia/exec/kr.co.bizframe.mas:type=application,id=camel-activemq-consumer/start()
	if (func == 'undefined' || func == null) {
		func = function() {
			location.reload();
		}
	}
	var operationInfo = {
		url : hawtioUrl + "/jolokia/exec/" + key + "/" + operation,
		dataType : "json",
		method : "get",
		callback : "operationResult",
		checkCallCondition : "no",
		jmxOp : key + " " + operation,
		func : func
	};
	showLoading();
	callApi(operationInfo);
}

function operationResult(info, res) {
	hideLoading();
	var result = res.status;
	if (result == '200') {
		alertFuncBox(info.jmxOp + ' : 성공', info.func);
	} else {
		alertBox(info.jmxOp + ' : 오류<br>' + res.value);
	}
}



function stopApp(key) {
	operationApp(key, "stop()");
}

function startApp(key) {
	operationApp(key, "start()");
}
