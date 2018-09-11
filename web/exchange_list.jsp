<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ include file="logincheck.jsp"%>
<%@ page import="kr.co.bizframe.esb.mng.utils.Strings"%>
<%
String aId = Strings.trim(request.getParameter("a"), "");
String rId = Strings.trim(request.getParameter("r"), "");
%>
<!DOCTYPE html>
<html lang="kr" dir="ltr" class="uk-height-1-1">
<%@ include file="bizframe.jsp"%>
    <body class="tm-background">
		<%@ include file="header.jsp"%>
        <div class="tm-middle">
            <div class="uk-container uk-container-center">
				<div class="uk-grid">
				    <div class="uk-width-1-1">
				    	<h1 class="uk-heading-large">Exchanges</h1>
				    </div>
				</div>
				<br>            	
           		<div class="uk-overflow-container">
           				<form class="uk-form" id="form2" name="form2">
							<input type="hidden" name="routeId" id="routeId" value="">
							<input type="hidden" name="agentId" id="agentId" value="">
							<input type="hidden" name="strSearch" id="strSearch" value="">
							<input type="hidden" name="searchKey" id="searchKey" value="">
						</form>
           				<form class="uk-form" id="form1" name="form1">           					
						<input type="hidden" name="curPage" id="curPage" value="1">
						<input type="hidden" name="routeId" id="routeId" value="">
						<input type="hidden" name="agentId" id="agentId" value="">
						<%@ include file="searchOption.jsp"%>
						<div id="list"></div><br>
						<div id="listCount" class="listCount"></div>
						<div id="pagelist" class="paginate"></div>
						</form>
                 </div>
            </div>
        </div>
        <div id="traceModal" class="uk-modal" aria-hidden="true" style="display: none; overflow-y: scroll;">
 			<div class="uk-modal-dialog" style="width:800px;">
			<button type="button" class="uk-modal-close uk-close"></button>
				<div class="uk-modal-header">
					<div id="traceTitleDiv"></div>
				</div>
				<div id="traceList"></div>
				
				<div class="uk-modal-footer uk-text-right">
					<button type="button" class="uk-button uk-button-primary" onclick='javascript:UIkit.modal("#traceModal").hide();'>Close</button>
				</div>
			</div>
		</div>
		<%@ include file="msgModal.jsp"%>
		<%@ include file="footer.jsp"%>
		<script src="js/components/datepicker.js"></script>
		<script src="js/list.js"></script>
		<script>

		var trInfo = {
				url : "exchanges",
				dataType: 'json',
				method: 'post',
				checkCallCondition: 'no',
				form:'form[name=form1]',
				callback: "showDatas",
				displayId: "list",
				pagelist: true,
				fieldWidths: [15, 30, 15, 15, 20, 5], //%
				fields: ["AgentId", "ExchangeId", "RouteId", "Created", "Finished", "Status"],
				align:["left", "left","left", "left", "left", "center"],
				jsonKey: ["agentId", "exchangeId", "routeId", "created", "finished", "success"],
				detailMethod : "showTrace",
				detailLinkArgs : ["agentId","routeId","exchangeId"],
				detailLinkTarget : "exchangeId",
				colspan:6
		};
		
		$(function () {
			var html='<select style="border:1px solid #E5E5E5;" id="searchKey" name="searchKey">';
			html +='<option value=""></option>';
			html +='<option value="agentId">AgentId</option>';
			html +='<option value="exchangeId">ExchangeId</option>';
			html +='<option value="routeId">RouteId</option>';
			html +='<option value="success">Status</option>';
			html +='</select>&nbsp;&nbsp;&nbsp;';
			
			$("#searchKeyDiv").html(html);
			var agentId = "<%=aId%>";
			var routeId = "<%=rId%>";
			if (agentId != '' && routeId !='') {
				$('#f_date').val(toDay());
				$('#form1').find("#agentId").val(agentId);
				$('#form1').find("#routeId").val(routeId);
			} else {
				$('#f_date').val(beforeDay(30));
			}
			$('#t_date').val(toDay());

			showLoading();
			callApi(trInfo);
		});		

		function showDatas(trInfo, res) {
			hideLoading();
			for ( var i = 0; i < res.messages.length; i++) {
				var ts = res.messages[i].created;
				res.messages[i].created = new Date(ts).format('yyyy-MM-dd HH:mm:ss');
				var ts0 = res.messages[i].finished;
				var exec = ts0-ts;
				res.messages[i].finished = new Date(ts0).format('yyyy-MM-dd HH:mm:ss') + "   (" +exec+ " ms)";
				var result = res.messages[i].success;
				if (result == true) {
					res.messages[i].success = '<i class="uk-icon-check-circle" style="font-size:20px;color:#4B8A08;"></i>';
				} else {
					res.messages[i].success = '<i class="uk-icon-exclamation-triangle" style="font-size:18px;color:#DF3A01;" onclick="javascript:viewError(\''+res.messages[i].errorMsg+'\');"></i>';
				}
			}
			showListJson(trInfo, res);
		}

		function viewError(error) {
			alertBox(error);
		}
		
		function showTrace(agentId, routeId, exchangeId) {
			var info = {
					url : "exchange/traces",
					dataType: 'json',
					method: 'post',
					checkCallCondition: 'no',
					form:'form[name=form2]',
					callback: "viewTraceMessages",
					exchangeId : exchangeId
			};			
			$('#form2').find("#searchKey").val("exchangeId");
			$('#form2').find("#strSearch").val(exchangeId);
			$('#form2').find("#agentId").val(agentId);
			$('#form2').find("#routeId").val(routeId);
			showLoading();
			callApi(info);
		}
		
		function viewTraceMessages(info, res) {
			hideLoading();
			$("#traceTitleDiv").html('<h2>' + info.exchangeId + ' Trace</h2>');
			var trInfo0 = {
					displayId: "traceList",
					fieldWidths: [25,15,25,15,15], //%
					fields: ["AgentId", "등록 일", "RouteId", "From", "To"],
					align:["left", "left","left", "left", "left"],
					jsonKey: ["agentId","timestamp", "routeId", "fromEndpointUri", "toNode"],
					detailMethod : "showMessage",
					detailLinkArgs : "id",
					detailLinkTarget : "routeId",
					pagelist: false,
					colspan:5
			};
			for ( var i = 0; i < res.messages.length; i++) {
				var ts = res.messages[i].timestamp;
				res.messages[i].timestamp = new Date(ts).format('yyyy-MM-dd HH:mm:ss');
			}
			showListJson(trInfo0, res);
			UIkit.modal("#traceModal").show();  
		}
		</script>
    </body>
</html>