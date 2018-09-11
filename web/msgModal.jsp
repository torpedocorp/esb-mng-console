<%@ page contentType="text/html; charset=UTF-8" language="java"%>
        <div id="msgModal" class="uk-modal" aria-hidden="true" style="display: none; overflow-y: scroll;">
 			<div class="uk-modal-dialog">
			<button type="button" class="uk-modal-close uk-close"></button>
				<div class="uk-modal-header">
					<div id="exchangeIdDiv"><h2></h2></div>
				</div>
				<table>
               		<tr>
               			<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">routeId</span></td>
               			<td style="padding-left:10px;text-align:left; border-bottom : 0px;"><div id="routeIdDiv"></div></td>
               		</tr>
               		<tr>
               			<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">exchangePattern</span></td>
               			<td style="padding-left:10px;padding-top:15px;text-align:left; border-bottom : 0px;"><div id="exchangePatternDiv"></div></td>
               		</tr>
               		<tr>
               			<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">Exception</span></td>
               			<td style="padding-left:10px;padding-top:15px;text-align:left; border-bottom : 0px;"><div id="exceptionDiv"></div></td>
               		</tr>
               		<tr>
               			<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">headers</span></td>
               			<td style="padding-left:10px;padding-top:15px;text-align:left; border-bottom : 0px;"><div id="headersDiv"></div></td>
               		</tr>
               		<tr>
               			<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">Body</span></td>
               			<td style="padding-left:10px;padding-top:15px;text-align:left; border-bottom : 0px;"><div id="bodyDiv"></div></td>
               		</tr> 
               		<tr>
               			<td style="text-align:left; border-bottom : 0px;"><span class="uk-text-bold uk-text-middle">PreviousNode</span></td>
               			<td style="padding-left:10px;padding-top:15px;text-align:left; border-bottom : 0px;"><div id="previousNodeDiv"></div></td>
               		</tr>             		
				</table>
				<div class="uk-modal-footer uk-text-right">
					<button type="button" class="uk-button uk-button-primary" onclick=javascript:UIkit.modal("#msgModal").hide();>Close</button>
				</div>
			</div>
		</div>
		<script>
		function showMessage(id) {
			var info = {
					url : 'msg/' + id,
					dataType: "json",
					method: "get",
					callback: "getMessage",
					checkCallCondition: "no"
			};
			
			showLoading();
			callApi(info);
			
		}
		
		function getMessage(info, res) {
			hideLoading();
			$("#routeIdDiv").html(res.routeId);
			$("#exchangeIdDiv").html(res.exchangeId);
			$("#exchangePatternDiv").html(res.exchangePattern);
			var err = res.causedByException;
			if (err == null) {
				err = "";
			}
			$("#exceptionDiv").html('<font color="red">' + err + '</font>');			
			$("#headersDiv").html(res.headers);
			$("#bodyDiv").html('<a href="#" onclick=viewBody("'+encodeURIComponent(res.body)+'")>' + res.body + '</a>');
			
			var pevNode = res.previousNode;
			if (pevNode == null) {
				pevNode = "";
			}
			$("#previousNodeDiv").html(pevNode);
			UIkit.modal("#msgModal").show();    
		}
		
		function viewBody(path) {
			UIkit.modal("#msgModal").hide();
			viewXml(path, 'Body File');
		}

		</script>