const loginForm = document.forms.loginForm; // 로그인 폼
const findIdForm = document.forms.findIdForm; // 아이디 찾기 폼
const findPwForm = document.forms.findPwForm; // 비밀번호 찾기 폼

// ajax url
const ajaxIdUrl = "/user/idCheck/"; // 아이디 ajax

// 로그인 제출 변수
let idSubmit = false;
let pwSubmit = false;

// 아이디 찾기 제출 변수
let namesubmit = false;
let emailsubmit = false;
let phonesubmit = false;

// 비밀번호 찾기 제출 변수
let idPwSubmit = false;
let namePwSubmit = false;
let emailPwSubmit = false;
let phonePwSubmit = false;

// 전화번호 입력 시 하이픈(-) 자동으로 생성되게 하는 함수
function phoneAutoComplete(e) {
    let number = e.value.replace(/[^0-9]/g, "");
    let phone = "";
 
    if (number.length < 4) {
         return number;
     } else if (number.length < 8) {
         phone += number.substr(0, 3);
         phone += "-";
         phone += number.substr(3);
     } else if (number.length < 9) {
         phone += number.substr(0, 3);
         phone += "-";
         phone += number.substr(3, 1);
         phone += "-";
         phone += number.substr(4, 4);
     } else if (number.length < 10) {
         phone += number.substr(0, 3);
         phone += "-";
         phone += number.substr(3, 2);
         phone += "-";
         phone += number.substr(5, 4);
     } else if (number.length < 11) {
         phone += number.substr(0, 3);
         phone += "-";
         phone += number.substr(3, 3);
         phone += "-";
         phone += number.substr(6, 4);
     } else {
		 phone += number.substr(0, 3);
         phone += "-";
         phone += number.substr(3, 4);
         phone += "-";
         phone += number.substr(7, 4);
	 }
        e.value = phone;
}

// 로그인 아이디 입력 이벤트
loginForm["user_id"].addEventListener("input", (event) => {
	let value = event.target.value;
	if(value) { // 아이디 값이 있을 때
		fetch(ajaxIdUrl + value) // 아이디 ajax 요청
			.then(response => response.json())
			.then((json) => {
				if(json.idCheck) { // 아이디가 가입되어 있을 때
					if(loginForm["user_pw"].value) { // 비밀번호 입력 후 다시 아이디를 수정할 경우 (비밀번호 값이 있을 때)
						if(json.user["user_pw"] === loginForm["user_pw"].value) { // 아이디 ajax로 가져온 유저의 정보에 있는 비밀번호와 입력한 비밀번호가 일치하는지 확인
							loginForm["user_pw"].removeAttribute("readonly");
							loginForm["user_pw"].classList.remove("is-invalid");
							loginForm["user_pw"].classList.add("is-valid");
							pwHelp.classList.remove("is-invalid");
							pwHelp.classList.add("is-valid");
							pwSubmit = true;
						} else {
							pwHelpInvalid.innerText = "잘못된 비밀번호입니다.";
							loginForm["user_pw"].removeAttribute("readonly");
							loginForm["user_pw"].classList.remove("is-valid");
							loginForm["user_pw"].classList.add("is-invalid");
							pwHelp.classList.remove("is-valid");
							pwHelp.classList.add("is-invalid");
							pwSubmit = false;
						}
					} else {
						loginForm["user_pw"].removeAttribute("readonly");
						loginForm["user_pw"].classList.remove("is-invalid");
						pwHelp.classList.remove("is-invalid");
					}
					loginForm["user_id"].classList.remove("is-invalid");
					loginForm["user_id"].classList.add("is-valid");
					idHelp.classList.remove("is-invalid");
					idHelp.classList.add("is-valid");
					idSubmit = true;
				} else { // 아이디가 가입되어 있지 않을 떄
					idHelpInvalid.innerText = "가입되어 있지 않은 아이디입니다.";
					loginForm["user_id"].classList.remove("is-valid");
					loginForm["user_id"].classList.add("is-invalid");
					idHelp.classList.remove("is-valid");
					idHelp.classList.add("is-invalid");
					idSubmit = false;
				}
			});
	} else { // 아이디 값이 없을 때
		idHelpInvalid.innerText = "아이디를 입력하세요.";
		loginForm["user_id"].classList.remove("is-valid");
		loginForm["user_id"].classList.add("is-invalid");
		idHelp.classList.remove("is-valid");
		idHelp.classList.add("is-invalid");
		idSubmit = false;
	}
	return idSubmit;
});

// 로그인 아이디 공백 차단
loginForm["user_id"].addEventListener("keydown", (event) => {
	let key = event.key;
	if(key == " ") {
		event.preventDefault();
	}
});

// 로그인 비밀번호 입력 이벤트
loginForm["user_pw"].addEventListener("input", (event) => {
	let value = event.target.value;
	if(value) { // 비밀번호 값이 있을 때
		fetch(ajaxIdUrl + loginForm["user_id"].value) // 아이디 ajax 요청
			.then(response => response.json())
			.then((json) => {
				if(json.user["user_pw"] === value) { // 아이디 ajax로 가져온 유저의 정보에 있는 비밀번호와 입력한 비밀번호가 일치하는지 확인
					loginForm["user_pw"].classList.remove("is-invalid");
					loginForm["user_pw"].classList.add("is-valid");
					pwHelp.classList.remove("is-invalid");
					pwHelp.classList.add("is-valid");
					pwSubmit = true;
				} else {
					pwHelpInvalid.innerText = "잘못된 비밀번호입니다.";
					loginForm["user_pw"].classList.remove("is-valid");
					loginForm["user_pw"].classList.add("is-invalid");
					pwHelp.classList.remove("is-valid");
					pwHelp.classList.add("is-invalid");
					pwSubmit = false;
				}
			});
	} else { // 비밀번호 값이 없을 때
		pwHelpInvalid.innerText = "비밀번호를 입력하세요.";
		loginForm["user_pw"].classList.remove("is-valid");
		loginForm["user_pw"].classList.add("is-invalid");
		pwHelp.classList.remove("is-valid");
		pwHelp.classList.add("is-invalid");
		pwSubmit = false;
	}
	return pwSubmit;
});

// 로그인 비밀번호 공백 차단
loginForm["user_pw"].addEventListener("keydown", (event) => {
	let key = event.key;
	if(key == " ") {
		event.preventDefault();
	}
});

// 아이디가 올바르지 않을 시 비밀번호 readonly
loginForm["user_id"].addEventListener("keyup", (event) => {
	if(idSubmit) { // 가입되어 있는 아이디일 때
		if(loginForm["user_pw"].value) { // 비밀번호 값이 이미 있을 때
			fetch(ajaxIdUrl + loginForm["user_id"].value) // 아이디 ajax 요청
			.then(response => response.json())
			.then((json) => {
				if(json.user["user_pw"] === loginForm["user_pw"].value) { // 아이디 ajax로 가져온 유저의 정보에 있는 비밀번호와 입력한 비밀번호가 일치하는지 확인
					loginForm["user_pw"].removeAttribute("readonly");
					loginForm["user_pw"].classList.remove("is-invalid");
					loginForm["user_pw"].classList.add("is-valid");
					pwHelp.classList.remove("is-invalid");
					pwHelp.classList.add("is-valid");
					pwSubmit = true;
				} else {
					pwHelpInvalid.innerText = "잘못된 비밀번호입니다.";
					loginForm["user_pw"].removeAttribute("readonly");
					loginForm["user_pw"].classList.remove("is-valid");
					loginForm["user_pw"].classList.add("is-invalid");
					pwHelp.classList.remove("is-valid");
					pwHelp.classList.add("is-invalid");
					pwSubmit = false;
				}
			});
		} else { // 비밀번호 값이 아직 없을 때
			loginForm["user_pw"].removeAttribute("readonly");
			loginForm["user_pw"].classList.remove("is-invalid");
			pwHelp.classList.remove("is-invalid");
		}
	} else { // 가입되어 있지 않은 아이디일 때
		if(loginForm["user_id"].value) { // 아이디 값이 있을 때
			pwHelpInvalid.innerText = "아이디가 올바르지 않아 입력하거나 수정할 수 없습니다.";
			loginForm["user_pw"].setAttribute("readonly", "readonly");
			loginForm["user_pw"].classList.remove("is-valid");
			loginForm["user_pw"].classList.add("is-invalid");
			pwHelp.classList.remove("is-valid");
			pwHelp.classList.add("is-invalid");	
			pwSubmit = false;
		} else { // 아이디 값이 없을 때
			pwHelpInvalid.innerText = "먼저 아이디를 입력하세요.";
			loginForm["user_pw"].setAttribute("readonly", "readonly");
			loginForm["user_pw"].classList.remove("is-valid");
			loginForm["user_pw"].classList.add("is-invalid");
			pwHelp.classList.remove("is-valid");
			pwHelp.classList.add("is-invalid");	
			pwSubmit = false;
		}
	} 
	return pwSubmit;
});

// 로그인 최종 제출 이벤트
loginForm.addEventListener("submit", (event) => {
	event.preventDefault();
	if(!loginForm["user_id"].value) {
		idHelpInvalid.innerText = "필수 정보입니다.";
		loginForm["user_id"].classList.add("is-invalid");
		idHelp.classList.add("is-invalid");
		idSubmit = false;
	}
	if(!loginForm["user_pw"].value) {
		pwHelpInvalid.innerText = "필수 정보입니다.";
		loginForm["user_pw"].classList.add("is-invalid");
		pwHelp.classList.add("is-invalid");
		pwSubmit = false;
	}
	if(idSubmit && pwSubmit) {
		loginForm.submit();
	}
});

// 아이디 찾기 이름 입력 이벤트
findIdForm["user_name"].addEventListener("input", (event) => {
	let value = event.target.value;
	if(value) {
		findIdForm["user_name"].classList.remove("is-invalid");
		nameHelp.classList.remove("is-invalid");
		nameSubmit = true;
	} else {
		nameHelpInvalid.innerText = "이름을 입력하세요.";
		findIdForm["user_name"].classList.add("is-invalid");
		nameHelp.classList.add("is-invalid");
		nameSubmit = false;
	}
});

// 아이디 찾기 이름 공백 차단
findIdForm["user_name"].addEventListener("keydown", (event) => {
	let key = event.key;
	if(key == " ") {
		event.preventDefault();
	}
});

// 아이디 찾기 이메일 입력 이벤트
findIdForm["user_email"].addEventListener("input", (event) => {
	let value = event.target.value;
	if(value) {
		findIdForm["user_email"].classList.remove("is-invalid");
		emailHelp.classList.remove("is-invalid");
		emailSubmit = true;
	} else {
		emailHelpInvalid.innerText = "이메일을 입력하세요.";
		findIdForm["user_email"].classList.add("is-invalid");
		emailHelp.classList.add("is-invalid");
		emailSubmit = false;
	}
});

// 아이디 찾기 이메일 공백 차단
findIdForm["user_email"].addEventListener("keydown", (event) => {
	let key = event.key;
	if(key == " ") {
		event.preventDefault();
	}
});

// 아이디 찾기 전화번호 입력 이벤트
findIdForm["user_phone"].addEventListener("input", (event) => {
	let value = event.target.value;
	if(value) {
		findIdForm["user_phone"].classList.remove("is-invalid");
		phoneHelp.classList.remove("is-invalid");
		phoneSubmit = true;
	} else {
		phoneHelpInvalid.innerText = "전화번호를 입력하세요.";
		findIdForm["user_phone"].classList.add("is-invalid");
		phoneHelp.classList.add("is-invalid");
		phoneSubmit = false;
	}
});

// 아이디 찾기 전화번호 공백 차단
findIdForm["user_phone"].addEventListener("keydown", (event) => {
	let key = event.key;
	if(key == " ") {
		event.preventDefault();
	}
});

// 아이디 찾기 최종 제출 이벤트
findIdForm.addEventListener("submit", (event) => {
	event.preventDefault();
	if(!findIdForm["user_name"].value) {
		nameHelpInvalid.innerText = "필수 정보입니다.";
		findIdForm["user_name"].classList.add("is-invalid");
		nameHelp.classList.add("is-invalid");
		nameSubmit = false;
	}
	if(!findIdForm["user_email"].value) {
		emailHelpInvalid.innerText = "필수 정보입니다.";
		findIdForm["user_email"].classList.add("is-invalid");
		emailHelp.classList.add("is-invalid");
		emailSubmit = false;
	}
	if(!findIdForm["user_phone"].value) {
		phoneHelpInvalid.innerText = "필수 정보입니다.";
		findIdForm["user_phone"].classList.add("is-invalid");
		phoneHelp.classList.add("is-invalid");
		phoneSubmit = false;
	}
	if(nameSubmit && emailSubmit && phoneSubmit) {
		findIdForm.submit();
	}
});

// 비밀번호 찾기 아이디 입력 이벤트
findPwForm["user_id"].addEventListener("input", (event) => {
	let value = event.target.value;
	if(value) {
		findPwForm["user_id"].classList.remove("is-invalid");
		idHelpPw.classList.remove("is-invalid");
		idPwSubmit = true;
	} else {
		idHelpInvalidPw.innerText = "아이디를 입력하세요.";
		findPwForm["user_id"].classList.add("is-invalid");
		idHelpPw.classList.add("is-invalid");
		idPwSubmit = false;
	}
});

// 비밀번호 찾기 아이디 공백 차단
findPwForm["user_id"].addEventListener("keydown", (event) => {
	let key = event.key;
	if(key == " ") {
		event.preventDefault();
	}
});

// 비밀번호 찾기 이름 입력 이벤트
findPwForm["user_name"].addEventListener("input", (event) => {
	let value = event.target.value;
	if(value) {
		findPwForm["user_name"].classList.remove("is-invalid");
		nameHelpPw.classList.remove("is-invalid");
		namePwSubmit = true;
	} else {
		nameHelpInvalidPw.innerText = "이름을 입력하세요.";
		findPwForm["user_name"].classList.add("is-invalid");
		nameHelpPw.classList.add("is-invalid");
		namePwSubmit = false;
	}
});

// 비밀번호 찾기 이름 공백 차단
findPwForm["user_name"].addEventListener("keydown", (event) => {
	let key = event.key;
	if(key == " ") {
		event.preventDefault();
	}
});

// 비밀번호 찾기 이메일 입력 이벤트
findPwForm["user_email"].addEventListener("input", (event) => {
	let value = event.target.value;
	if(value) {
		findPwForm["user_email"].classList.remove("is-invalid");
		emailHelpPw.classList.remove("is-invalid");
		emailPwSubmit = true;
	} else {
		emailHelpInvalidPw.innerText = "이메일을 입력하세요.";
		findPwForm["user_email"].classList.add("is-invalid");
		emailHelpPw.classList.add("is-invalid");
		emailPwSubmit = false;
	}
});

// 비밀번호 찾기 이메일 공백 차단
findPwForm["user_email"].addEventListener("keydown", (event) => {
	let key = event.key;
	if(key == " ") {
		event.preventDefault();
	}
});

// 비밀번호 찾기 전화번호 입력 이벤트
findPwForm["user_phone"].addEventListener("input", (event) => {
	let value = event.target.value;
	if(value) {
		findPwForm["user_phone"].classList.remove("is-invalid");
		phoneHelpPw.classList.remove("is-invalid");
		phonePwSubmit = true;
	} else {
		phoneHelpInvalidPw.innerText = "전화번호를 입력하세요.";
		findPwForm["user_phone"].classList.add("is-invalid");
		phoneHelpPw.classList.add("is-invalid");
		phonePwSubmit = false;
	}
});

// 비밀번호 찾기 전화번호 공백 차단
findPwForm["user_phone"].addEventListener("keydown", (event) => {
	let key = event.key;
	if(key == " ") {
		event.preventDefault();
	}
});

// 비밀번호 찾기 최종 제출 이벤트
findPwForm.addEventListener("submit", (event) => {
	event.preventDefault();
	if(!findPwForm["user_id"].value) {
		idHelpInvalidPw.innerText = "필수 정보입니다.";
		findPwForm["user_id"].classList.add("is-invalid");
		idHelpPw.classList.add("is-invalid");
		idPwSubmit = false;
	}
	if(!findPwForm["user_name"].value) {
		nameHelpInvalidPw.innerText = "필수 정보입니다.";
		findPwForm["user_name"].classList.add("is-invalid");
		nameHelpPw.classList.add("is-invalid");
		namePwSubmit = false;
	}
	if(!findPwForm["user_email"].value) {
		emailHelpInvalidPw.innerText = "필수 정보입니다.";
		findPwForm["user_email"].classList.add("is-invalid");
		emailHelpPw.classList.add("is-invalid");
		emailPwSubmit = false;
	}
	if(!findPwForm["user_phone"].value) {
		phoneHelpInvalidPw.innerText = "필수 정보입니다.";
		findPwForm["user_phone"].classList.add("is-invalid");
		phoneHelpPw.classList.add("is-invalid");
		phonePwSubmit = false;
	}
	if(idPwSubmit && namePwSubmit && emailPwSubmit && phonePwSubmit) {
		findPwForm.submit();
	}
});