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
				    	<h1 class="uk-heading-large">Exchange Statistic</h1>
				    </div>
				</div>
				<br>            	
           		<div class="uk-overflow-container">
           				<form class="uk-form" id="form1" name="form1">           					
							<input type="hidden" name="pageCnt" id="pageCnt" value="5">
							<div>
								<input type="text" id="f_date" name="f_date" data-uk-datepicker="{format:'YYYY-MM-DD'}" style="border:1px solid #E5E5E5;"> ~
								<input type="text" id="t_date" name="t_date" data-uk-datepicker="{format:'YYYY-MM-DD'}" style="border:1px solid #E5E5E5;">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<select style="border:1px solid #E5E5E5;" id="searchKey" name="searchKey">
								<option value="">Group By</option>
								<option value="agentId">AgentId</option>
								<option value="routeId">RouteId</option>
								<option value="success">Success</option>
								</select>
								&nbsp;&nbsp;&nbsp;&nbsp;
								<i class="uk-icon-search" onclick="javascript:search(1)"></i>
								<input class="uk-search-field" type="search" placeholder="search..." autocomplete="on" id="strSearch" name="strSearch">
							</div>
							<br>
						<div id="list"></div><br>
						</form>
                 </div>
            </div>
        </div>
		<%@ include file="footer.jsp"%>
		<script src="js/components/datepicker.js"></script>
		<script src="js/list.js"></script>
		<script src="js/underscore-min.js"></script>
		<script>

		var trInfo = {
				url : "statistic/exchanges",
				dataType: 'json',
				method: 'post',
				checkCallCondition: 'no',
				form:'form[name=form1]',
				callback: "showDatas",
				displayId: "list",
				pagelist: false,
				fieldWidths: [20, 10, 30, 30, 10], //%
				fields: ["Created", "Count", "AgentId", "RouteId", "Status"],
				align:["left", "left","left", "left", "left"],
				jsonKey: ["createDate", "count", "agentId", "routeId", "success"],
				detailUrl : "showMessage",
				colspan:5
		};
		
		$(function () {
			
			$('#f_date').val(beforeDay(30));
			$('#t_date').val(toDay());

			showLoading();
			callApi(trInfo);
		});		

		function showDatas(trInfo, res) {
			hideLoading();
			var searchKey = $("#searchKey").val();
			var viewSuccess = false;
			if (searchKey == "" || searchKey == "success") {
				viewSuccess = true;
			}
			
			var messages = res.messages;
			var dateGroupby = _.groupBy(messages, function(row) {
				return row.createDate;
			});			
			
			var dateGroupbyKeys = _.sortBy(_.keys(dateGroupby), function (key) {
		       return key;
		    }).reverse();
			
			//var arr = jQuery.makeArray(dateGroupby);
			var list = [];
			dateGroupbyKeys.forEach(function(key) {
				var arr = dateGroupby[key];
				var i = 0
				_.sortBy(arr, 'count').reverse().forEach(function(m) {
					if (viewSuccess == true) {						
						var result = m.success;
						if (result == true) {
							m.success = '<i class="uk-icon-check-circle" style="font-size:20px;color:#4B8A08;"></i>';
						} else {
							m.success = '<i class="uk-icon-exclamation-triangle" style="font-size:18px;color:#DF3A01;"></i>';
						}
					} else {
						m.success = "";
					}
					if (i > 0) {
						m.createDate = "";	
					}
					i++;
					list.push(m);
				});	
			});
			res.messages = list;
			showListJson(trInfo, res);
		}
		</script>
    </body>
</html>