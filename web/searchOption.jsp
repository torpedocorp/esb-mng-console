<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<input type="hidden" name="pageCnt" id="pageCnt" value="5">
<div>
	<input type="text" id="f_date" name="f_date" data-uk-datepicker="{format:'YYYY-MM-DD'}" style="border:1px solid #E5E5E5;"> ~
	<input type="text" id="t_date" name="t_date" data-uk-datepicker="{format:'YYYY-MM-DD'}" style="border:1px solid #E5E5E5;">

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<span id="searchKeyDiv"></span>
<i class="uk-icon-search" onclick="javascript:search(1)"></i><input class="uk-search-field" type="search" placeholder="search..." autocomplete="on" id="strSearch" name="strSearch">

<span style="padding-right:10px;height:28px;margin-top:5px;font-weight: bold;">
Total : <span style="font-weight: bold;color:#39baea;" id="totalTopCnt"></span> 건
</span>
&nbsp;&nbsp;
<select style="border:1px solid #E5E5E5;" id="itemCnt" name="itemCnt" onchange="javascript:search(1)">
    <option value="10" selected="selected">10</option>
	<option value="15">15</option>
	<option value="25">25</option>
	<option value="50">50</option>
	<option value="100">100</option>
</select>
</div>
<br>