var selectMap = new Map();
var searchMap = new Map();
var cOrderbyField = '';
var cAsc = '';
function showDetailData(callinfo, res) {
	var row = getDetailTrData(callinfo, res);
	var body = $("#"+callinfo.displayId);
	if (row != '') {
		body.empty();
	}
	body.append(row);
}

function getTrData(listInfo, row) {
	var trData = "";
	var cmn = "";
	var key = "";
	var align = "";
	var keys = listInfo.jsonKey;	
	var detailMethod = listInfo.detailMethod;
	var listTdClass = "";
	if(listInfo.listTdClass =! null){
		listTdClass = listInfo.listTdClass;
	}
	for ( var j = 0; j < keys.length; j++) {
		key = keys[j];
		align = listInfo.align[j];
		if (key == null || !key) {
			console.log("if");
			cmn = " ";
		} else {
			cmn = eval("row." + key);
		}
		if (cmn == null || !cmn) {
			cmn = " ";
		}

		if (key == listInfo.detailLinkTarget) {
			var cmn0 = cmn;
			if(detailMethod != null && detailMethod != "") {
				var detailLinkValue = listInfo.detailLinkArgs;
				var append = false;
				if (jQuery.type( detailLinkValue ) == "array") {
					cmn = '<a href=javascript:'+detailMethod+'(';				
					detailLinkValue.forEach(function(key) {
						if (append==true) {
							cmn += ',';
						}						
						cmn += '\''+escape(eval("row." + key))+'\'';
						append = true;
					});
					cmn += ');>' + cmn0 + '</a>';
				} else {
					cmn = '<a href=javascript:'+detailMethod+'(\'' + escape(eval("row." + listInfo.detailLinkArgs)) + '\');>' + cmn + '</a>';	
				}
				
			} else {
				var detailLinkValue = escape(eval("row." + listInfo.detailLinkArgs));
				var url = listInfo.detailUrl;
				var andString = '?';
				if (url.indexOf("?") > -1) {
					andString = '&';
				}
				url += andString + listInfo.detailLinkArgs + "=" + detailLinkValue;
				cmn = '<a href=' + url + '>' + cmn + '</a>';
			}
		}
		trData += '<td align="' + align + '" class="' + listTdClass + '">' + cmn + '</td>';
		
	}
	return trData;
}

// result list
function showListJson(listInfo, res) {
	var tableId = "ajaxResultTable";
	var displayId = "#listContent";
	if (listInfo.displayId != "" && listInfo.displayId != null) {
		displayId = "#" + listInfo.displayId;
		tableId = listInfo.displayId;
	}

	var listJsonKey = "messages";
	if (listInfo.listJsonKey != "" && listInfo.listJsonKey != null) {
		listJsonKey = listInfo.listJsonKey;
	}

	var listTableClass = "uk-table";
	if (listInfo.listTableClass != "" && listInfo.listTableClass != null) {
		listTableClass = listInfo.listTableClass;
	}

	var pagelistDiv = "pagelist";
	if (listInfo.pagelistDiv != "" && listInfo.pagelistDiv != null) {
		pagelistDiv = listInfo.pagelistDiv;
	}

	var listCountDiv = "listCount";
	if (listInfo.listCountDiv != "" && listInfo.listCountDiv != null) {
		listCountDiv = listInfo.listCountDiv;
	}

	var noDataMsg = "There is no data";
	if (listInfo.noDataMsg != "" && listInfo.noDataMsg != null) {
		noDataMsg = listInfo.noDataMsg;
	}

	var colspan = listInfo.colspan;

	if (res.totalRows != null) {
		var listCount = '<b>Showing ' + $('#curPage').val() + ' to ' + $('#itemCnt').val() + ' of ' + res.totalRows + ' entries</b>';
		$('#totalTopCnt').html(res.totalRows);
	}

	var body = '<table class="' + listTableClass + '" id="' + tableId + '_table">';
	body += listHeader(listInfo);
	var list = eval("res." + listJsonKey);
	body += '<tbody>';
	if (list.length == 0) {
		body += '<tr>';
		body += '<td colspan="' + colspan + '" align="center">' + noDataMsg + '</td></tr>';
		body += '</tr>';
		body += '</tbody>';
		body += "</table>";
		$(displayId).html(body);
		console.log(listInfo.page);
		if (listInfo.pagelist == true) {
			$('#'+listCountDiv).html('');
			$('#'+pagelistDiv).html('');
		}
		return;
	}

	for ( var i = 0; i < list.length; i++) {
		var trData = "";
		if (listInfo.getTrData != "" && listInfo.getTrData != null) {
			trData = window[listInfo.getTrData](listInfo, list[i]);
		} else {
			trData = getTrData(listInfo, list[i]);
		}
		var trOption = '';
		if (listInfo.trOption != "" && listInfo.trOption != null) {
			trOption = getClickStr(listInfo.trOption, list[i].id) + ' style="cursor: pointer;"';
		}
		body += '<tr '+ trOption + '>' + trData + '</tr>';
	}
	body += '</tbody>';
	body += '</table>';
	$(displayId).html(body);

	if (listInfo.pagelist != "" && listInfo.pagelist != null && listInfo.pagelist){
		var curPage = $("#curPage").val();
		var totalCnt = res.totalRows;
		var scriptFunctionName = "javascript:search";
		if (listInfo.scriptFunctionName != "" && listInfo.scriptFunctionName != null){
			scriptFunctionName = listInfo.scriptFunctionName;
		}
		$('#'+pagelistDiv).html(getPageIndexForScript(curPage, totalCnt, scriptFunctionName));
		$('#'+listCountDiv).html(listCount);

	}

	var rows = $("table#" + tableId + " tr");
	if (listInfo.rowNumbering) {
		if (rows && rows.length > 0) {
			$(rows).each(function(idx) {
				var addVal = "";
				if (idx > 0) {
					addVal = idx;
				}
				var elementName = 'td';
				if (idx == 0) {
					elementName = 'th';
				}
				var firstDClone = $(rows[idx]).find(elementName + ':first').clone();
				$(rows[idx]).find(elementName + ':first').after(firstDClone);
				$(rows[idx]).find(elementName).eq(0).html(addVal);
				$(rows[idx]).find(elementName).eq(0).attr({
					'width' : '5%',
					'align' : 'center'
				});
			});
			if (listInfo.rowNumberingTitle != null
					&& listInfo.rowNumberingTitle != "") {
				$(rows[0]).find(elementName).eq(0).html(listInfo.rowNumberingTitle);
			}
		}
	}

	if (listInfo.checkBox) {
		if (rows && rows.length > 0) {
			$(rows).each(function(idx) {
				var addVal = "";
				if (idx > 0) {
					var row0 = list[idx-1];
					addVal = eval("row0." + listInfo.checkBoxValueKey);
				}
				var elementName = 'td';
				if (idx == 0) {
					elementName = 'th';
				}

				var firstDClone = $(rows[idx]).find(elementName + ':first').clone();
				$(rows[idx]).find(elementName + ':first').after(firstDClone);

				$(rows[idx]).find(elementName).eq(0).html("<label class=\"topcoat-checkbox\"><input type=\"checkbox\" name=\"idx\" id=\"idx\" value="+addVal+"><div class=\"topcoat-checkbox__checkmark\"></div>&nbsp;<span id=\"cp_disabled\"></span></label>");
				$(rows[idx]).find(elementName).eq(0).attr({
					'width' : '4%',
					'align' : 'left'
				});
			});
			$(rows[0]).find('th').eq(0).html("<label class=\"topcoat-checkbox\"><input type=\"checkbox\" onClick=\"javascript:makeAllCheckBox();\" id=\"chbox\" name=\"allChkBtn\"><div class=\"topcoat-checkbox__checkmark\"></div>&nbsp;<span id=\"cp_disabled\"></span></label>");

		}
	}
}

function listHeader(listInfo) {
	var widths = listInfo.fieldWidths;
	var fields = listInfo.fields;
	var jsonKeys = listInfo.jsonKey;
	var orderbyArgs = listInfo.orderbyArgs;
	var orderbyField = listInfo.orderbyField;
	var searchArgs = listInfo.searchArgs;
	var searchField = listInfo.searchField;
	var selectArgs = listInfo.selectArgs;
	var selectField = listInfo.selectField;
	var sortingMethod = 'sorting';
	if (listInfo.sortingMethod != "" && listInfo.sortingMethod != null) {
		sortingMethod = listInfo.sortingMethod;
	}
	var body = "<thead>";
	body += '<tr>';
	for (var j = 0; j < fields.length; j++) {
		var title = fields[j];
		var style = '';
		var icon = '';
		var onClick = '';
		var jsonKey = listInfo.jsonKey[j];

		//check box
		if(title == "checkBox"){
			title = '<input type="checkbox" id="allChkBtn" name="allChkBtn" style="vertical-align:middle;" onclick="javascript:makeAllCheckBox()"><div class="topcoat-checkbox__checkmark" style="vertical-align:middle;"></div>';
		}

		//order by
		var orderbyArgsSize = 0;
		if(orderbyArgs != undefined){
			orderbyArgsSize = orderbyArgs.length;
		}
		for (var k = 0; k < orderbyArgsSize; k++) {
			if(jsonKey == orderbyArgs[k]){
				icon = ' <i class="uk-icon-sort" style="color: #FFFFFF"></i>';
				onClick = getClickStr(sortingMethod, orderbyField[k]);
				style = ' style="cursor:pointer"';
				if(orderbyField[k] == cOrderbyField){
						icon =' <i class="uk-icon-sort-desc"></i>';
					if(cAsc == "true"){
						icon =' <i class="uk-icon-sort-asc"></i>';
					}
				}
			}
		}

		//search
		var searchArgsSize = 0;
		if(searchArgs != undefined){
			searchArgsSize = searchArgs.length;
		}
		for(var k = 0; k < searchArgsSize; k++){
			if(jsonKey == searchArgs[k]){
				var color = '000000';
				onClick = getClickStr('strSearch', searchField[k]);
				style = ' style="cursor:pointer"';
				if(!searchMap.containsKey(searchField[k])){
					color = 'FFFFFF"';
				}
				icon = ' <i class="uk-icon-search" id="' + searchField[k] + '" style="color: #' + color + '"></i>';
			}
		}

		//select
		var selectArgsSize = 0;
		if(selectArgs != undefined){
			selectArgsSize = selectArgs.length;
		}
		for(var k = 0; k < selectArgsSize; k++){
			if(jsonKey == selectArgs[k]){
				style = ' style="padding: 3px 6px 3px 2px;"';
				var checked = ' selected="selected"';
				if(selectMap.size() != 0){
					checked = '';
				}

				title = '<select class="field docNav" id="'+selectArgs[k]+'" name="'+selectArgs[k]+'" onchange="javascript:selectSearch(\''+selectArgs[k]+'\',\''+selectField[k]+'\')" style="width: 100%">';
				title += '<option value=""' + checked + '>'+fields[j]+'</option>';
				var selectvalue = listInfo.selectvalue[k];
				for(var v = 0 ; v < selectvalue.size() ; v++){
					var key = selectvalue.keys()[v];
					var checked = '';
					if(selectMap.containsKey(selectField[k]) && selectMap.get(selectField[k]) == selectvalue.get(key)){
						checked = ' selected="selected"';
					}

					title += '<option value="'+selectvalue.get(key)+'"'+checked+'>'+key+'</option>';
				}
				title += '</select>';
			}
		}

		if(j == 0) {
			body += '<th width="' + widths[j] + '%" '+ onClick + style + '>' + title + icon + '</th>';
		} else if(j == fields.length - 1) {
			body += '<th width="' + widths[j] + '%" '+ onClick + style + '>' + title + icon + '</th>';
		} else {
			body += '<th width="' + widths[j] + '%" '+ onClick + style + '>' + title + icon + '</th>';
		}
	}
	body += '</tr>';
	body += '</thead>';
	return body;
}

function getClickStr(script, arg){
	return 'onClick="javascript:'+script+'(\''+arg+'\')"';
}

function listLoading(callInfo, error) {
	var str = map.get("processingMgs");
	if (error == 1) {
		str = map.get("correctCondtionMsg");
	}

	var displayId = "#listContent";
	if (callInfo.displayId != "" && callInfo.displayId != null) {
		displayId = "#" + callInfo.displayId;
	}
	var body = "";
	body = '<table class="TableLayout">';
	body += listHeader(callInfo);
	body += '<tr>';
	body += '   <td colspan="'
			+ callInfo.jsonKey.length
			+ '" class="ResultLastData" align="center"><span style="color:red;">' + str + '</span></td></tr>';
	body += "</table>";
	$(displayId).html(body);
}

function getDetailTrData(callinfo, res) {
	var row= '<table class="table-black table-bordered-black gradient" style="table-layout: fixed;">';
	row += '<tbody>';

	var rightCSS = 'border-right: 1px solid #E9E9E9;';
	var topCSS = 'border-top: 1px solid #E9E9E9;';
	for ( var j = 0; j < callinfo.jsonKey.length; j++) {
		var tdStyle = ' style="';
		var tdStyle1 = '';
		if(j == 0){
			tdStyle += topCSS + rightCSS + '"';
			tdStyle1 = ' style="' + topCSS + '"';
		} else {
			tdStyle += rightCSS + '"';
		}
		var key = callinfo.jsonKey[j];
		var value = eval("res." + key);
		var field = callinfo.fields[j];

		row += '<tr>';
		row += '<td width="14%"'+tdStyle+'>&nbsp;'+field+'</td>';
		if (callinfo.editable[j] == false) {
			row += '<td'+tdStyle1+'>&nbsp;&nbsp;'+value+'</td>';
		} else {
			row += '<td><input type="text" name="'+key+'" id="'+key+'" value="'+value+'" size="'+callinfo.editable[j]+'"/></td>';
		}

		row += '</tr>';
	}

	row += '</tbody>';
	row += '</table>';

	return row;
}

function downloadExcel(infoName, excelTypeBoxId) {
	// 요청 정보를 담고 있는 객체 이름이 trInfo가 아닐 때
	if(infoName != undefined) {
		eval('var infoObj =' + infoName + ';');
		eval('infoObj.excelTypeBoxId = ' + excelTypeBoxId + ';');
		return exportExcel(infoObj);
	}
	return exportExcel(trInfo);
}

function search(page, formName) {
	if(!searchMap.isEmpty()){
		var strSearch = $('#strSearch');
		if(formName) {
			strSearch = $(formName).find('#strSearch');
		}
		if(strSearch.val() == ''){
			alertBox0("확인", "검색값이 입력되지 않았습니다.");
			return false;
		}

		var count = strSearch.val().split(' ');
		var isError = false;
		$(count).each(function(t) {
			var c = trim(count[t]);
			if(c == '' || c == null) {
				isError = true;
			}
		});
		
		if(searchMap.size() != count.length || isError){
			alertBox0("확인", "선택된 검색필드들과 검색내용의 구분값이 다릅니다.");
			return false;
		}
	}
	
	var f_date = $('#f_date');
	var t_date = $('#t_date');
	var sorting = $('#sorting');
	var asc = $('#asc');
	var curPage = $('#curPage');
	if(formName) {
		f_date = $(formName).find('#f_date');
		t_date = $(formName).find('#t_date');
		sorting = $(formName).find('#sorting');
		asc = $(formName).find('#asc');
		curPage = $(formName).find('#curPage');
	}
	
	if(checkDateFormat(f_date.val()) == false || checkDateFormat(t_date.val()) == false) {
		alertBox0("Alert", "날짜 검색조건이 올바르지 않습니다.");
		return false;
	}
	
   if(cOrderbyField != ''){
    	sorting.val(cOrderbyField);
    	asc.val(cAsc);
    }

    curPage.val(page);
	showLoading();
	callApi(trInfo);
}

function sorting(orderbyField) {
	var asc = 'false';
	if(orderbyField == cOrderbyField){
		if(cAsc != ''){
			if(cAsc == 'false'){
				cAsc = 'true';
			} else {
				cAsc = 'false';
			}
			asc = cAsc;
		} else {
			cAsc = asc;
		}

	} else {
		cAsc = asc;
		cOrderbyField = orderbyField;
	}
	$('#sorting').val(orderbyField);
	$('#asc').val(asc);
	search(1);
}

function strSearch(searchField){
	if(searchMap.containsKey(searchField)){
		searchMap.remove(searchField);
		document.getElementById(searchField).style.color = "#FFFFFF";
	} else {
		searchMap.put(searchField);
		document.getElementById(searchField).style.color = "#000000";
	}
	var json = '';
	var count = 1;
	for(var i=0;i<trInfo.searchField.length;i++){
		var searchField = trInfo.searchField[i];
		if(searchMap.containsKey(searchField)){
			json += searchField;
			if(searchMap.size() != count){
				json += ',';
			}
			count++;
		}
	}
	$('#searchField').val(json);
}

function selectSearch(selectSearch, selectField){
	var select = $('#' + selectSearch + " option:selected").val();
	if(select == ''){
		selectMap.remove(selectField);
	} else {
		selectMap.put(selectField, select);
	}

	var json = '{';
	for(var i=0;i<selectMap.size();i++){
		var key = selectMap.keys()[i];
		json += key + ":" + selectMap.get(key);
		if((selectMap.size() - 1) > i){
			json += ',';
		}
	}
	json += '}';
	$('#selectType').val(json);
	search(1);
}

function getPageIndexForScript(curPage, totalCnt, scriptFunctionName) {
	var pageCnt = $("#pageCnt").val();
	var totpage = getTotalPage(totalCnt);
	var blockpage, i;
	var outStr = "<ul>";

	if(pageCnt == undefined){
		pageCnt = 5;
	}

	if (curPage <= 0)
		curPage = 1;
	if (totpage <= 0)
		totpage = 1;

	blockpage = Math.floor(((curPage - 1) / pageCnt)) * pageCnt + 1;
	// prev block
	if (totpage >= 1) {
		if (curPage > 1)
			outStr = outStr + "<li><a href='" + scriptFunctionName + "(1)'><i class=\"uk-icon-angle-double-left\"></i></a></li>";

		if (blockpage == 1)
			outStr = outStr + "";
		else
			outStr = outStr + "<li><a href='" + scriptFunctionName + "(" + (blockpage - pageCnt) + ")'><i class=\"uk-icon-chevron-left\"></i></a></li>";
	}

	i = 1;
	while (i <= pageCnt && blockpage <= totpage) {

		if (i > 1)
			outStr = outStr + "";
		if (blockpage == curPage)
			outStr = outStr + "<li><a href='#' class='active'>" + blockpage + "</a></li>";
		else
			outStr = outStr + "<li><a href='" + scriptFunctionName + "(" + blockpage + ")' >" + blockpage + "</a></li>";

		blockpage = blockpage + 1;
		i = i + 1;
	}

	// next block
	if (totpage >= 1) {

		if (blockpage > totpage)
			outStr = outStr + "";
		else
			outStr = outStr + "<li><a href='" + scriptFunctionName + "(" + blockpage + ")'><i class=\"uk-icon-chevron-right\"></i></a></li>";

		if (curPage < totpage)
			outStr = outStr + "<li><a href='" + scriptFunctionName + "(" + totpage + ")'><i class=\"uk-icon-angle-double-right\"></i></a></li>";

	}
	outStr = outStr + "</ul>";
	return outStr;
}

function getTotalPage(totalCnt) {
	var itemCnt = $("#itemCnt").val();
	var extra = totalCnt % itemCnt;
	if (extra > 0) {
		return (totalCnt - extra) / itemCnt + 1;
	} else {
		return totalCnt / itemCnt;
	}
}


function makeAllCheckBox() {
	$(document).ready(function() {
		if($('input:checkbox[name="allChkBtn"]').is(":checked")) {
			$('input:checkbox[name="idx"]').each(function() {
				this.checked = true;
			});
		} else {
			$('input:checkbox[name="idx"]').each(function() {
				this.checked = false;
			});
		}
	});
}
