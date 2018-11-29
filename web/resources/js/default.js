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
var krTxt = "KR";
var enTxt = "EN";
var MAIN_PAGE = "dashboard.jsp";
function callApi(callInfo) {
	var form = $(callInfo.form);
	var method = callInfo.method.toUpperCase();
	var beforeSendResult = true;
	if (callInfo.checkCallCondition == "no") {

	} else if (callInfo.checkCallCondition != ""
			&& callInfo.checkCallCondition != null) {
		beforeSendResult = window[callInfo.checkCallCondition](callInfo);
	} else {
		beforeSendResult = checkCallCondition();
	}
	
	if (!beforeSendResult) {
		return;
	}
	
	var sendData = null;	
	if (method == 'GET') {
		if (callInfo.form != "" && callInfo.form != null) {
			sendData = form.serialize();
		}
		
	} else {
		if (callInfo.form != "" && callInfo.form != null) {
			var arr = form.serializeArray();
			var dataObj = {};
			$.each(arr, function(idx, ele) {
				// obj의 key값은 arr의 name, obj의 value는 value값
				dataObj[ele.name] = ele.value;
			});
			sendData = JSON.stringify(dataObj);
		}
	}
	$.ajax({
		type : callInfo.method,
		dataType : callInfo.dataType,
		contentType:"application/json;charset=UTF-8",
		url : callInfo.url,
		data: sendData,		
		success : function(args, textStatus , xhr) {
			if (callInfo.callback != "" && callInfo.callback != null) {
				window[callInfo.callback](callInfo, args, xhr);
			} else {
				successFunc(args);
			}
		},
		error : function(jqXHR, ajaxOptions, exception) {
			processError(jqXHR, ajaxOptions, exception);
		}
	});
}

function call(callInfo) {
	var beforeSendResult = true;
	if (callInfo.checkCallCondition == "no") {

	} else if (callInfo.checkCallCondition != ""
			&& callInfo.checkCallCondition != null) {
		beforeSendResult = window[callInfo.checkCallCondition](callInfo);
	} else {
		beforeSendResult = checkCallCondition();
	}
	
	if (!beforeSendResult) {
		return;
	}
	
	var url = callInfo.url;
	// spring, direct call url
	/*if (url.indexOf("?") > -1) {
		url = url + "&";
	} else {
		url = url + "?";
	}
	url = url + "method=" + callInfo.action;
	
	if (callInfo.fullUrl != "" && callInfo.fullUrl != null) {
		url = callInfo.fullUrl;
	}
	*/
	
	if(callInfo.multiMime != "" && callInfo.multiMime != null && callInfo.multiMime){
		var param = null;
		$(callInfo.form).attr('action', url);
		$(callInfo.form).ajaxSubmit({
		   type : callInfo.method,
           dataType: callInfo.dataType,           
	       success: function(responseText, statusText){
				if (callInfo.callback != "" && callInfo.callback != null) {
					window[callInfo.callback](callInfo, responseText);
				} else {
					successFunc(args);
				}
	       },
	       error: function(jqXHR, ajaxOptions, exception){
	         processError(jqXHR, ajaxOptions, exception);
	       }                              
		});

	} else {
		var param = null;
		if (callInfo.form != "" && callInfo.form != null) {
			param = $(callInfo.form).serialize();
		}
		if(callInfo.addtionalParams != "" && callInfo.addtionalParams != null && callInfo.addtionalParams != undefined) {
			param += '&' + $.param(callInfo.addtionalParams);
		}
		$.ajax({
			type : callInfo.method,
			url : url,
			data : param,			
			dataType : callInfo.dataType,
			success : function(args) {
				if (callInfo.callback != "" && callInfo.callback != null) {
					window[callInfo.callback](callInfo, args);
				} else {
					successFunc(args);
				}
			},
			error : function(jqXHR, ajaxOptions, exception) {
				processError(jqXHR, ajaxOptions, exception);
			}
		});
	}

}

function processError(jqXHR, ajaxOptions, exception) {
	hideLoading();
	if (jqXHR.status == 0) {
		//alert('Not connected.\nPlease verify your network connection.');
		// 재 접속하면 될거같은데.. 무한루프에 빠질 가능성이 있음....어떻게 해결하
		console.log('Not connected.\nPlease verify your network connection.');
	} else if (jqXHR.status == 404) {
		alertBox('The requested page not found. [404] ' + exception);
	} else if (jqXHR.status == 500) {
		alertBox('Internal Server Error [500]', jqXHR.responseText);
	} else if (exception == 'parsererror') {
		alertBox('Requested JSON parse failed.');
	} else if (exception == 'timeout') {
		alertBox('Time out error.');
	} else if (exception == 'abort') {
		alertBox('Ajax request aborted.');
	} else {
		console.log(jqXHR.responseText);
		alertBox(jqXHR.status + ", " + exception + ', Uncaught Error.\n'+ jqXHR.responseText);
	}
}

function checkCallCondition() {
	return true;
}

function successFunc(args) {

}

function validateInteger(int) {
	var RegExp = /^[0-9]+$/;
	if (!RegExp.test(int)) {
		return false;
	} else {
		return true;
	}
}

function validateEmail(email) {
	var RegExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
	if (!RegExp.test(email)) {
		return false;
	} else {
		return true;
	}
}

function validateMobile(number){
	var RegExp = /^\d{3}-\d{3,4}-\d{4}$/;
	if (!RegExp.test(number)) {
		return false;
	} else {
		return true;
	}
}

function checkDateFormat(dateStr) {
	if(dateStr.length != 10) {
		return false;
	}
	for (var i = 0; i < 10; i++) {
		if(i == 4 || i == 7) {
			if(dateStr.charAt(i) != '-') {
				return false;
			}
		} else {
			if(isNaN(parseInt(dateStr.charAt(i))) == true) {
				return false;
			}
		}
	}
	
	return true;
}

function alarmMessage(title, msg) {
	$('#alarm_title').html(title);
	var preMsg = $('#alarm_msg').val();

	if($('#isOn').val() == '1') {
		$('#alarm_msg').val(preMsg + "\n\n" + msg);
	} else {
		$('#alarm_msg').val(msg);
		$('#alarm_popup').fadeIn($(this).data());
	}

	var msgBox = $('#alarm_msg');
	$('#alarm_msg').scrollTop(msgBox[0].scrollHeight - msgBox.height());
	$('#isOn').val('1');
}

function alarmWindowClose() {
	$('#isOn').val('0');

	$(".modal-box, .modal-overlay").fadeOut(500, function() {
		$(".modal-overlay").remove();
	});
}

function alertFuncBox(msg, func) {
	UIkit.modal.alert(msg, {center: true}).on('hide.uk.modal', func);
}

function alertBox(msg) {
	UIkit.modal.alert(msg);
}

function alertBox0(title, msg) {
	alertBox0(title, msg, '');
}

function alertBox0(title, msg, link) {
	UIkit.modal.alert(msg);
}

function showLoading() {
	$("#loadingModel").css("display", "block");
}

function hideLoading() {
	$("#loadingModel").css("display", "none"); 
}

function getCurrentTime() {
	var d = new Date();

	var minute = d.getMinutes();
	if(parseInt(minute) < 10) {
		minute = "0" + minute;
	}

	var second = d.getSeconds();
	if(parseInt(second) < 10) {
		second = "0" + second;
	}

	var result = d.getFullYear() + "-" + (d.getMonth()+1) + "-" + d.getDate() + " " + d.getHours() + ":" + minute + ":" + second;

	return result;
}

Date.prototype.format = function(f) {
    if (!this.valueOf()) return " ";
    var weekName = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];
    var d = this;

    return f.replace(/(yyyy|yy|MM|dd|E|HH|h|hh|mm|ss|a\/p)/gi, function($1) {
        switch ($1) {
            case "yyyy": return d.getFullYear();
            case "yy": return (d.getFullYear() % 1000).zf(2);
            case "MM": return (d.getMonth() + 1).zf(2);
            case "dd": return d.getDate().zf(2);
            case "E": return weekName[d.getDay()];
            case "HH": return d.getHours().zf(2);
            case "hh": return ((h = d.getHours() % 12) ? h : 12).zf(2);
            case "h": return ((h = d.getHours() % 12) ? h : 12).zf(1);
            case "mm": return d.getMinutes().zf(2);
            case "ss": return d.getSeconds().zf(2);
            case "a/p": return d.getHours() < 12 ? "오전" : "오후";
            default: return $1;
        }
    });
};

String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
Number.prototype.zf = function(len){return this.toString().zf(len);};

Map = function(){
	this.map = new Object();
};
Map.prototype = {
    put : function(key, value){
        this.map[key] = value;
    },
    get : function(key){
        return this.map[key];
    },
    containsKey : function(key){
     return key in this.map;
    },
    containsValue : function(value){
     for(var prop in this.map){
      if(this.map[prop] == value) return true;
     }
     return false;
    },
    isEmpty : function(key){
     return (this.size() == 0);
    },
    clear : function(){
     for(var prop in this.map){
      delete this.map[prop];
     }
    },
    remove : function(key){
     delete this.map[key];
    },
    keys : function(){
        var keys = new Array();
        for(var prop in this.map){
            keys.push(prop);
        }
        return keys;
    },
    values : function(){
     var values = new Array();
        for(var prop in this.map){
         values.push(this.map[prop]);
        }
        return values;
    },
    size : function(){
      var count = 0;
      for (var prop in this.map) {
        count++;
      }
      return count;
    }
};

function trim(str) {
	str = str.replace(/ /gi, ''); // 위와 같이 모든 공백을 제거
	return str;
}

function checkEmptyStr(str) {
	if (str == 'null' || str == undefined || str == null) {
	} else {
		if (str) {
			return str;
		}
	}
	return "";
}


function validExtension(path, type) {
	var pathpoint = path.lastIndexOf('.');
	var filepoint = path.substring(pathpoint+1, path.length);
	var filetype = filepoint.toLowerCase();

	if (filetype == type){
		return true;
	} else {
		return false;
	}
}

function openWindow(linkUrl) {
	window.open(linkUrl, "", "width=890 height=400 left=300,top=0,resizable=no,scrollbars=no,menubar=no,location=no,toolbar=no,status=no");
}

function openerLink(linkUrl) {
	opener.location.href = linkUrl;
}

function chartLink(linkUrl) {
	location.href = linkUrl;
}

function commify(n) {
	var reg = /(^[+-]?\d+)(\d{3})/;   // 정규식
	n += '';                          // 숫자를 문자열로 변환

	while (reg.test(n))
		n = n.replace(reg, '$1' + ',' + '$2');

	return n;
}

//'취소' 버튼 클릭 시, 입력창 초기화
function formclear(){
	$("form").each(function() {
        this.reset();
    });
	$("input:radio").prop("checked", false);
	$('input:checkbox').prop("checked", false);
}


function replaceAll(str, searchStr, replaceStr) {
    return str.split(searchStr).join(replaceStr);
}

function showFileName(fileTagName, divID) {
	var fileName = $("#" + fileTagName).val();
	var lastindex = fileName.lastIndexOf('\\');
	fileName = fileName.substring(lastindex + 1, fileName.length);

	$("#" + divID).html(fileName);
}

//'취소' 버튼 클릭 시, 입력창 초기화
function formclear(){
	$("form").each(function() {
        this.reset();
    });
}

function beforeDay(duration) {
    var fromDay = new Date();
    fromDay.setTime(fromDay.getTime() - (duration * 24 * 60 * 60 * 1000));
    return fromDay.format("yyyy-MM-dd");
}

function afterDay(duration) {
    var fromDay = new Date();
    fromDay.setTime(fromDay.getTime() + (duration * 24 * 60 * 60 * 1000));
    return fromDay.format("yyyy-MM-dd");
}

function toDay() {
	var toDay = new Date();
    var todate = toDay.format("yyyy-MM-dd");
    return todate;
}

function filterUndefined(input) {
	return (input==undefined)? "":input;
}

function getCurrentPageName() {
	var currentFileName = document.URL.substring(document.URL.lastIndexOf("/") + 1, document.URL.lastIndexOf("/") + 30);
	if(currentFileName.indexOf('?') > -1) {
		currentFileName = currentFileName.split('?')[0]
	}

	return currentFileName;
}

function viewXml(path, title) {
	var info = {
		url : "downloadFile?appPath=" + path,
		dataType : "text",
		method : "get",
		callback : "downloadFileResult",
		checkCallCondition : "no",
		title : title,
	};
	showLoading();
	callApi(info);
}

function downloadFileResult(info, data, xhr) {
	hideLoading();
	var type = "configFile";
	var ct = xhr.getResponseHeader("content-type");
	if (ct == null || ct == '') {
		ct = 'application/octet-stream';
	}

	var disposition = xhr.getResponseHeader('Content-Disposition');
	var filename = "";
	if (disposition && disposition.indexOf('attachment') !== -1) {
		var filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
		var matches = filenameRegex.exec(disposition);
		if (matches != null && matches[1]) {
			filename = matches[1].replace(/['"]/g, '');
		}
	} else {
		filename = id;
	}
	filename = filename.replace(" ", "");

	if (ct == 'application/octet-stream') {
		var blob;
		try {
			blob = new Blob([ data ], {
				type : ct
			});
		} catch (e) {
			if (e.name == "InvalidStateError") {
				// InvalidStateError (tested on Win8 IE11)
				var bb = new MSBlobBuilder();
				bb.append(data);
				blob = bb.getBlob(ct);
			} else {
				// We're screwed, blob constructor unsupported entirely   
			}
		}

		if (typeof window.navigator.msSaveBlob !== 'undefined') {
			// IE workaround for "HTML7007: One or more blob URLs were revoked by closing the blob for which they were created. These URLs will no longer resolve as the data backing the URL has been freed."
			window.navigator.msSaveBlob(blob, filename);
		} else {
			var URL = window.URL || window.webkitURL;
			var downloadUrl = URL.createObjectURL(blob);
			if (filename) {
				// use HTML5 a[download] attribute to specify filename
				var a = document.createElement("a");
				// safari doesn't support this yet
				if (typeof a.download === 'undefined') {
					window.location = downloadUrl;
				} else {
					a.href = downloadUrl;
					a.download = filename;
					document.body.appendChild(a);
					a.click();
				}
			} else {
				window.location = downloadUrl;
			}

			setTimeout(function() {
				URL.revokeObjectURL(downloadUrl);
			}, 100); // cleanup
		}

	} else {
		if (ct.indexOf('json') > -1) {
			data = JSON.stringify(data);
		} else {
			data = String(data).replace(/&/g, '&amp;').replace(/</g, '&lt;')
					.replace(/>/g, '&gt;').replace(/"/g, '&quot;');
		}

		alertBox(info.title + "<br><pre>" + data + "</pre>");
	}
}