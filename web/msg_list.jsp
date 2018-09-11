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
				fieldWidths: [15,25,15,15,15,15], //%
				fields: ["AgentId", "ExchangeId", "등록 일", "RouteId", "From", "To"],
				align:["left", "left", "left","left", "left", "left"],
				jsonKey: ["agentId","exchangeId", "timestamp", "routeId", "fromEndpointUri", "toNode"],
				detailMethod : "showMessage",
				detailLinkArgs : "id",
				detailLinkTarget : "exchangeId",
				colspan:6
		};
		
		$(function () {
			var html='<select style="border:1px solid #E5E5E5;" id="searchKey" name="searchKey">';
			html +='<option value=""></option>';
			html +='<option value="exchangeId">ExchangeId</option>';
			html +='<option value="routeId">RouteId</option>';
			html +='<option value="fromEndpointUri">From</option>';
			html +='<option value="toNode">To</option>';
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
			}
			showListJson(trInfo, res);
		}

		</script>
    </body>
</html>