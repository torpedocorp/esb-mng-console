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
