$(function () {

	jQuery.support.placeholder = false;
	test = document.createElement('input');
	if('placeholder' in test) jQuery.support.placeholder = true;
	if (!$.support.placeholder) {
		$('.field').find ('label').show ();
	}
});

function checkCallCondition() {
	var id = $('#loginid').val();
	var pw = $('#loginpw').val();
	if(id == null || id == '') {
		alertBox0('[Login 결과]', '아이디를 입력하십시오');
		return false;
	}
	if(pw == null || pw == '') {
		alertBox0('[Login 결과]', '비밀번호를 입력하십시오');
		return false;
	}

	return true;
}

var loginInfo = {
	url: 'api/login',
	dataType: 'json',
	method: 'POST',
	checkCallCondition: 'no',
	form:'form[name=form1]'
};


var logOutInfo = {
		url: 'api/logout',
		dataType: 'json',
		method: 'GET',
		checkCallCondition: 'no',
		callback: 'logoutResult'
};


function successFunc(args) {
	var code = args.result;
	if (code == '001') {
		var role = args.role;
		var userObid = args.userObid;
		sessionStorageLogin(role, args.userId);
		location.href=MAIN_PAGE;

	} else if (code == '101') {
		alertBox0('[Login 결과]', '비밀번호를 입력하십시오');
	} else if (code == '102') {
		alertBox0('[Login 결과]', '비밀번호를 입력하십시오');
	} else if (code == '103') {
		alertBox0('[Login 결과]', '비밀번호가 맞지 않습니다.');
	} else if (code == '104') {
		alertBox0('[Login 결과]', '관리자로 로그인 해 주십시오');
	} else if (code == '105') {
		alertBox0('[Login 결과]', '아이디가 틀립니다');
	} else {
		alertBox0('[Login 결과]', '로그인에 실패했습니다. 다시 시도해 주십시오.');
		$('#loginid').val('');
		$('#loginpw').val('');
	}
}

function onEnter(event)
{
   if(event==null) event =  window.event;
   if(event.keyCode == 13) {
	   loginCall();
   }
}

function loginCall() {
	callApi(loginInfo);
}

function logout() {
	callApi(logOutInfo);
}

function logoutResult(callInfo, args) {
	var code = args.result;
	if (code == '001') {
		sessionStorageLogOut();
		location.href='login.jsp';
	} else {
		alertBox0('로그아웃 처리에 실패했습니다.');
	}
}

function sessionStorageLogin(role, userId) {
	sessionStorage['role'] = JSON.stringify(role);
	sessionStorage['userId'] = userId;	
}

function sessionStorageLogOut() {
	if(sessionStorage['role'] != undefined && sessionStorage['role'] != null) {
		delete sessionStorage['role'];
	}
	if(sessionStorage['userId'] != undefined && sessionStorage['userId'] != null) {
		delete sessionStorage['userId'];
	}
	if(sessionStorage['mapIntervalTime'] != undefined && sessionStorage['mapIntervalTime'] != null) {
		delete sessionStorage['mapIntervalTime'];
	}
}
