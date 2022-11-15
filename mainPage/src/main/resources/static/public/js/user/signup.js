const signupForm = document.forms.signupForm;

// ajax url
const ajaxIdUrl = "/user/idCheck/"; // 아이디 ajax
const ajaxEmailUrl = "/user/emailCheck/"; // 이메일 ajax
const ajaxPhoneUrl = "/user/phoneCheck/"; // 전화번호 ajax
const ajaxEmailConfirmUrl = "/user/emailConfirm/" // 이메일 인증번호 ajax
const ajaxPhoneConfirmUrl = "/user/phoneConfirm/" // 전화번호 인증번호 ajax

// submit event 처리 변수
let nameSubmit = false;
let idSubmit = false;
let pwSubmit = false;
let pwCheckSubmit = false;
let emailSubmit = false;
let emailConfirmSubmit = false;
let phoneConfirmSubmit = false;
let phoneSubmit = false;
let birthSubmit = false;
let adressSubmit = false;

// 인증번호 처리 변수
let authCode; // 이메일
let authCode2; // 전화번호

// 이름 정규식
function nameCheck(v) {
	let name_pattern = /^[a-zA-Zㄱ-힣][a-zA-Zㄱ-힣]*$/; // 영문 또는 한글
 
 	if(name_pattern.test(v) === true) {
		return true;
	} else {
		return false;
	}	
}

// 아이디 정규식
function idCheck(v) {
	let id_pattern = /^[a-zA-Z0-9]{6,20}$/g; // 영문 대소문자, 숫자 모두 가능
	
	if(id_pattern.test(v) === true) {
		return true;
	} else {
		return false;
	}	
}

// 비밀번호 정규식
function passwordCheck(v) {
	let pw_pattern = /^((?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]))|((?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W)).{8,16}$/; // 대문자 + 소문자 + 숫자 / 대문자 + 소문자 + 숫자 + 특수 문자 (8~16자)
 
 	if(pw_pattern.test(v) === true) {
		return true;
	} else {
		return false;
	}	
}

// 이메일 정규식
function checkEmail(v) {
      let email_pattern = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/; // 영문 대소문자 또는 숫자 (-_\.)(옵션)+ @ + 영문 대소문자 또는 숫자 (-_\.)(옵션) + . + 영문 대소문자 2~3자
      
      if (email_pattern.test(v) === true) {
          return true;
      } else {
		  return false;
	  }
}

// 전화번호 정규식
function checkPhone(v) {
      let phone_pattern = /^01([0|1|6|7|8|9])-?([0-9]{4})-?([0-9]{4})$/; // 01([0|1|6|7|8|9]) - 0000 - 0000
      
      if (phone_pattern.test(v) === true) {
          return true;
      } else {
		  return false;
	  }
}

// 비밀번호 보안 확인
function passwordChanged(password) {
        let strongRegex = new RegExp("^(?=.{13,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).*$", "g"); // 안전한 비밀번호 : 대문자 + 소문자 + 숫자 + 특수 문자 (13자 이상)
        let mediumRegex = new RegExp("^(?=.{8,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).*$", "g"); // 괜찮은 비밀번호 : 대문자 + 소문자 + 숫자 + 특수 문자 (8자 이상)

		if (strongRegex.test(password)) {
            pwHelpValid.innerHTML = '<span style="color:#198754">안전한 비밀번호입니다. (사용 가능 - 보안에 강한 비밀번호입니다.)</span>';
            return true;
        } else if(mediumRegex.test(password)) {
            pwHelpValid.innerHTML = '<span style="color:#ffc107">괜찮은 비밀번호입니다. (사용 가능 - 13자를 넘기면 보안이 강화됩니다.)</span>';
            return true;
        } else {
            pwHelpInvalid.innerHTML = '<span style="color:#dc3545">위험한 비밀번호입니다. (사용 불가 - 특수 문자를 포함하세요.)</span>';
            return false; 
        }
}

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

//첫 공백만 차단
function noSpace(obj) { // 공백사용못하게
    var str = /\s/;  // 공백체크
    if(str.exec(obj.value.trim)) { //공백 체크
        //alert("해당 항목에는 첫 글자 공백을 사용할수 없습니다.");
        obj.focus();
        obj.value = obj.value.replace(/^ +/,''); // 공백제거
        return false;
    }
}

/*
// 잘라내기, 복사, 붙여넣기 차단
document.querySelectorAll("input").forEach((input) => {
	input.addEventListener("cut", (event) => {
		event.preventDefault();
		alert("잘라낼 수 없습니다.");
	});
	input.addEventListener("copy", (event) => {
		event.preventDefault();
		alert("복사할 수 없습니다.");
	});
	input.addEventListener("paste", (event) =>{
		event.preventDefault();
		alert("붙여넣을 수 없습니다.");
	});
});
*/ 

// 이름 입력 이벤트
signupForm["user_name"].addEventListener("input", (event) => {
	let value = event.target.value;
	if(value && value.trim()) {
		if(value.length > 1 && value.length < 21) {
			if(nameCheck(value)) {
				signupForm["user_name"].classList.remove("is-invalid");
				signupForm["user_name"].classList.add("is-valid");
				nameHelp.classList.remove("is-invalid");
				nameHelp.classList.add("is-valid");
				nameSubmit = true;
			} else {
				nameHelpInvalid.innerText = "유효하지 않은 형식입니다. (영문 또는 한글)";
				signupForm["user_name"].classList.remove("is-valid");
				signupForm["user_name"].classList.add("is-invalid");
				nameHelp.classList.remove("is-valid");
				nameHelp.classList.add("is-invalid");
				nameSubmit = false;
			}		
		} else {
			nameHelpInvalid.innerText = "2 ~ 20자의 영문 또는 한글로 입력하세요.";
			signupForm["user_name"].classList.remove("is-valid");
			signupForm["user_name"].classList.add("is-invalid");
			nameHelp.classList.remove("is-valid");
			nameHelp.classList.add("is-invalid");
			nameSubmit = false;
		}	
	} else {
		nameHelpInvalid.innerText = "이름을 입력하세요.";
		signupForm["user_name"].classList.remove("is-valid");
		signupForm["user_name"].classList.add("is-invalid");
		nameHelp.classList.remove("is-valid");
		nameHelp.classList.add("is-invalid");
		nameSubmit = false;
	}
	return nameSubmit;
});

// 이름 공백 차단
signupForm["user_name"].addEventListener("keydown", (event) => {
	let key = event.key;
	if(key == " ") {
		event.preventDefault();
	}
});

// 아이디 입력 이벤트
signupForm["user_id"].addEventListener("input", (event) => {
	let value = event.target.value;	
	if(value && value.trim()) {
		if(value.length > 5 && value.length < 21) {
			if(idCheck(value) && isNaN(value)) {
				fetch(ajaxIdUrl + value)
					.then(response => response.json())
					.then((json) => {
						if(json.idCheck) {
							idHelpInvalid.innerText="이미 사용 중인 아이디입니다.";
							signupForm["user_id"].classList.remove("is-valid");
							signupForm["user_id"].classList.add("is-invalid");
							idHelp.classList.remove("is-valid");
							idHelp.classList.add("is-invalid");
							idSubmit = false;
						} else {
							signupForm["user_id"].classList.remove("is-invalid");
							signupForm["user_id"].classList.add("is-valid");
							idHelp.classList.remove("is-invalid");
							idHelp.classList.add("is-valid");
							idSubmit = true;
						}
					});
			} else {
				idHelpInvalid.innerText="유효하지 않은 형식입니다. (영문 또는 영문 + 숫자)";
				signupForm["user_id"].classList.remove("is-valid");
				signupForm["user_id"].classList.add("is-invalid");
				idHelp.classList.remove("is-valid");
				idHelp.classList.add("is-invalid");
				idSubmit = false;
			}	
		} else {
			idHelpInvalid.innerText="6 ~ 20자의 (영문 또는 영문 + 숫자) 형식으로 입력하세요.";
			signupForm["user_id"].classList.remove("is-valid");
			signupForm["user_id"].classList.add("is-invalid");
			idHelp.classList.remove("is-valid");
			idHelp.classList.add("is-invalid");
			idSubmit = false;
		}
	} else {
		idHelpInvalid.innerText="아이디를 입력하세요.";
		signupForm["user_id"].classList.remove("is-valid");
		signupForm["user_id"].classList.add("is-invalid");
		idHelp.classList.remove("is-valid");
		idHelp.classList.add("is-invalid");
		idSubmit = false;
	}
	return idSubmit;
});

// 아이디 공백 차단
signupForm["user_id"].addEventListener("keydown", (event) => {
	let key = event.key;
	if(key == " ") {
		event.preventDefault();
	}
});

// 비밀번호 입력 이벤트
signupForm["user_pw"].addEventListener("input", (event) => {
	let value = event.target.value;
	if(value && value.trim()) {
		if(value.length > 7 && value.length < 17) {		
			if(passwordCheck(value) && isNaN(value)) {
				if(passwordChanged(value) === false) {
					signupForm["user_pw"].classList.remove("is-valid");
					signupForm["user_pw"].classList.add("is-invalid");
					pwHelp.classList.remove("is-valid");
					pwHelp.classList.add("is-invalid");
					pwSubmit = false;
				} else {
					signupForm["user_pw"].classList.remove("is-invalid");
					signupForm["user_pw"].classList.add("is-valid");
					pwHelp.classList.remove("is-invalid");
					pwHelp.classList.add("is-valid");
					pwSubmit = true;		
				}
			} else {
				pwHelpInvalid.innerText = "유효하지 않은 형식입니다. (대문자 + 소문자 + 숫자 + 특수 문자)";
				signupForm["user_pw"].classList.remove("is-valid");
				signupForm["user_pw"].classList.add("is-invalid");
				pwHelp.classList.remove("is-valid");
				pwHelp.classList.add("is-invalid");
				pwSubmit = false;
			}	
		} else {
			pwHelpInvalid.innerText = "8 ~ 16자의 (대문자 + 소문자 + 숫자 + 특수 문자) 형식으로 입력하세요.";
			signupForm["user_pw"].classList.remove("is-valid");
			signupForm["user_pw"].classList.add("is-invalid");
			pwHelp.classList.remove("is-valid");
			pwHelp.classList.add("is-invalid");
			pwSubmit = false;
		}
	} else {
		pwHelpInvalid.innerText = "비밀번호를 입력하세요.";
		signupForm["user_pw"].classList.remove("is-valid");
		signupForm["user_pw"].classList.add("is-invalid");
		pwHelp.classList.remove("is-valid");
		pwHelp.classList.add("is-invalid");
		pwSubmit = false;
	}
	return pwSubmit;
});

// 비밀번호 공백 차단
signupForm["user_pw"].addEventListener("keydown", (event) => {
	let key = event.key;
	if(key == " ") {
		event.preventDefault();
	}
});

// 비밀번호 확인 입력 이벤트
signupForm["pwCheck"].addEventListener("input", (event) => {
	let value = event.target.value;
		if(value && value === signupForm["user_pw"].value) {
			signupForm["pwCheck"].classList.remove("is-invalid");
			signupForm["pwCheck"].classList.add("is-valid");
			pwCheckHelp.classList.remove("is-invalid");
			pwCheckHelp.classList.add("is-valid");
			pwCheckSubmit = true;		
		} else {
			pwHelpInvalidCheck.innerText = "비밀번호가 불일치합니다.";
			signupForm["pwCheck"].classList.remove("is-valid");
			signupForm["pwCheck"].classList.add("is-invalid");
			pwCheckHelp.classList.remove("is-valid");
			pwCheckHelp.classList.add("is-invalid");
			pwCheckSubmit = false;
		}
	return pwCheckSubmit;
});

// 비밀번호 확인 공백 차단 및 비밀번호의 값이 없거나 조건에 맞지 않을 시 입력 불가능하게 설정
signupForm["pwCheck"].addEventListener("keydown", (event) => {
	let key = event.key;
	if(key == " " || !signupForm["user_pw"].value || !pwSubmit) {
		event.preventDefault();
	}
});

// 비밀번호가 조건에 맞지 않을 시 비밀번호 확인 disabled
signupForm["user_pw"].addEventListener("keyup", (event) => {
	if(pwSubmit) {
		signupForm["pwCheck"].removeAttribute("disabled");
		signupForm["pwCheck"].classList.remove("is-invalid");
		pwCheckHelp.classList.remove("is-invalid");
  		if(event.target.value && signupForm["pwCheck"].value) {
			if(event.target.value != signupForm["pwCheck"].value) {
				pwHelpInvalidCheck.innerText = "비밀번호가 불일치합니다.";
				signupForm["pwCheck"].classList.remove("is-valid");
				signupForm["pwCheck"].classList.add("is-invalid");
				pwCheckHelp.classList.remove("is-valid");
				pwCheckHelp.classList.add("is-invalid");
				pwCheckSubmit = false;
			} else {
				signupForm["pwCheck"].classList.remove("is-invalid");
				signupForm["pwCheck"].classList.add("is-valid");
				pwCheckHelp.classList.remove("is-invalid");
				pwCheckHelp.classList.add("is-valid");
				pwCheckSubmit = true;
			}			
		}
	} else {
		if(event.target.value) {
			signupForm["pwCheck"].setAttribute("disabled", "disabled");
			pwHelpInvalidCheck.innerText = "비밀번호가 조건에 맞지 않아 입력하거나 수정할 수 없습니다.";
			signupForm["pwCheck"].classList.remove("is-valid");
			signupForm["pwCheck"].classList.add("is-invalid");
			pwCheckHelp.classList.remove("is-valid");
			pwCheckHelp.classList.add("is-invalid");
			pwCheckSubmit = false;
		} else {
			signupForm["pwCheck"].setAttribute("disabled", "disabled");
			pwHelpInvalidCheck.innerText = "먼저 비밀번호를 입력하세요.";
			signupForm["pwCheck"].classList.remove("is-valid");
			signupForm["pwCheck"].classList.add("is-invalid");
			pwCheckHelp.classList.remove("is-valid");
			pwCheckHelp.classList.add("is-invalid");
			pwCheckSubmit = false;
		}
	} 
	return pwCheckSubmit;
});

// 비밀번호, 비밀번호 확인 붙여넣기 차단
signupForm["user_pw"].addEventListener("paste", (event) =>{
	event.preventDefault();
	alert("붙여넣을 수 없습니다.");
});
signupForm["pwCheck"].addEventListener("paste", (event) =>{
	event.preventDefault();
	alert("붙여넣을 수 없습니다.");
});

// 이메일 입력 이벤트
signupForm["user_email"].addEventListener("input", (event) => {
	let value = event.target.value;
	if(value && value.trim()) {
		if(checkEmail(value) && isNaN(value)) {
				fetch(ajaxEmailUrl + value)
					.then(response => response.json())
					.then((json) => {
						if(json.emailCheck) {
							emailConfirmBtnDiv.setAttribute("style", "display: none");
							emailConfirmCodeDiv.setAttribute("style", "display: none");
							emailHelpInvalid.innerText = "이미 사용 중인 이메일입니다.";
							signupForm["user_email"].classList.remove("is-valid");
							signupForm["user_email"].classList.add("is-invalid");
							emailHelp.classList.remove("is-valid");
							emailHelp.classList.add("is-invalid");
							emailSubmit = false;
						} else {
							emailConfirmBtnDiv.removeAttribute("style");
							signupForm["user_email"].classList.remove("is-invalid");
							signupForm["user_email"].classList.add("is-valid");
							emailHelp.classList.remove("is-invalid");
							emailHelp.classList.add("is-valid");
							emailSubmit = true;
						}
					});
			} else {
				emailConfirmBtnDiv.setAttribute("style", "display: none");
				emailConfirmCodeDiv.setAttribute("style", "display: none");
				emailHelpInvalid.innerText = "유효하지 않은 형식입니다. 예시) Kevin@example.com";
				signupForm["user_email"].classList.remove("is-valid");
				signupForm["user_email"].classList.add("is-invalid");
				emailHelp.classList.remove("is-valid");
				emailHelp.classList.add("is-invalid");
				emailSubmit = false;
			}	
	} else {
		emailConfirmBtnDiv.setAttribute("style", "display: none");
		emailConfirmCodeDiv.setAttribute("style", "display: none");
		emailHelpInvalid.innerText = "이메일을 입력하세요.";
		signupForm["user_email"].classList.remove("is-valid");
		signupForm["user_email"].classList.add("is-invalid");
		emailHelp.classList.remove("is-valid");
		emailHelp.classList.add("is-invalid");
		emailSubmit = false;
	}
	return emailSubmit;
});

// 이메일 공백 차단
signupForm["user_email"].addEventListener("keydown", (event) => {
	let key = event.key;
	if(key == " ") {
		event.preventDefault();
	}
});

// 이메일 인증 버튼 클릭
signupForm["emailConfirmBtn"].addEventListener("click", (event) => {
	emailConfirmCodeDiv.removeAttribute("style");
	fetch(ajaxEmailConfirmUrl + signupForm["user_email"].value)
		.then(response => response.json())
		.then((json) => {
			authCode = json.authCode; // 인증번호
			console.log(authCode);
		});
	return authCode;
});

// 이메일 인증번호 입력 이벤트
signupForm["emailConfirmCode"].addEventListener("input", (event) => {
	let key = event.target.value;
	console.log(authCode);
	if(key) {
		if(key === authCode) {
			signupForm["user_email"].setAttribute("readonly", "readonly");
			signupForm["emailConfirmCode"].setAttribute("readonly", "readonly");
			emailConfirmBtnDiv.setAttribute("style", "display: none");
			signupForm["emailConfirmCode"].classList.remove("is-invalid");
			signupForm["emailConfirmCode"].classList.add("is-valid");
			codeHelp.classList.remove("is-invalid");
			codeHelp.classList.add("is-valid");
			emailConfirmSubmit = true;
		} else {
			codeHelpInvalid.innerText = "인증번호가 일치하지 않습니다.";
			signupForm["emailConfirmCode"].classList.remove("is-valid");
			signupForm["emailConfirmCode"].classList.add("is-invalid");
			codeHelp.classList.remove("is-valid");
			codeHelp.classList.add("is-invalid");
			emailConfirmSubmit = false;
		}
	} else {
		codeHelpInvalid.innerText = "인증번호를 입력해 주세요.";
		signupForm["emailConfirmCode"].classList.remove("is-valid");
		signupForm["emailConfirmCode"].classList.add("is-invalid");
		codeHelp.classList.remove("is-valid");
		codeHelp.classList.add("is-invalid");
		emailConfirmSubmit = false;
	}
	return emailConfirmSubmit;
});

// 이메일 인증번호 공백 차단
signupForm["emailConfirmCode"].addEventListener("keydown", (event) => {
	let key = event.key;
	if(key == " ") {
		event.preventDefault();
	}	
});

// 전화번호 입력 이벤트
signupForm["user_phone"].addEventListener("input", (event) => {
	let value = event.target.value;
	if(value && value.trim()) {
		if(checkPhone(value)) {
			fetch(ajaxPhoneUrl + value)
				.then(response => response.json())
				.then((json) => {
					if(json.phoneCheck) {
						phoneConfirmBtnDiv.setAttribute("style", "display : none");
						phoneConfirmCodeDiv.setAttribute("style", "display : none");
						phoneHelpInvalid.innerText = "이미 사용 중인 전화번호입니다.";
						signupForm["user_phone"].classList.remove("is-valid");
						signupForm["user_phone"].classList.add("is-invalid");
						phoneHelp.classList.remove("is-valid");
						phoneHelp.classList.add("is-invalid");
						phoneSubmit = false;
					} else {
						phoneConfirmBtnDiv.removeAttribute("style");
						signupForm["user_phone"].classList.remove("is-invalid");
						signupForm["user_phone"].classList.add("is-valid");
						phoneHelp.classList.remove("is-invalid");
						phoneHelp.classList.add("is-valid");
						phoneSubmit = true;
					}
				});
		} else {
			phoneConfirmBtnDiv.setAttribute("style", "display : none");
			phoneConfirmCodeDiv.setAttribute("style", "display : none");
			phoneHelpInvalid.innerText = "유효하지 않은 형식입니다. (01[0|1|6|7|8|9]-1234-1234 형식의 숫자)";
			signupForm["user_phone"].classList.remove("is-valid");
			signupForm["user_phone"].classList.add("is-invalid");
			phoneHelp.classList.remove("is-valid");
			phoneHelp.classList.add("is-invalid");
			phoneSubmit = false;
		}		
	} else {
		phoneConfirmBtnDiv.setAttribute("style", "display : none");
		phoneConfirmCodeDiv.setAttribute("style", "display : none");
		phoneHelpInvalid.innerText = "전화번호를 입력하세요.";
		signupForm["user_phone"].classList.remove("is-valid");
		signupForm["user_phone"].classList.add("is-invalid");
		phoneHelp.classList.remove("is-valid");
		phoneHelp.classList.add("is-invalid");
		phoneSubmit = false;
	}
	return phoneSubmit;
});

// 전화번호 공백 차단
signupForm["user_phone"].addEventListener("keydown", (event) => {
	let key = event.key;
	if(key == " ") {
		event.preventDefault();
	}	
});

// 전화번호 인증 버튼 클릭
signupForm["phoneConfirmBtn"].addEventListener("click", (event) => {
	phoneConfirmCodeDiv.removeAttribute("style");
	fetch(ajaxPhoneConfirmUrl + signupForm["user_phone"].value)
		.then(response => response.json())
		.then((json) => {
			authCode2 = json.authCode; // 인증번호
			console.log(authCode2);
		});
		return authCode2;
});

// 전화번호 인증 번호 입력 이벤트
signupForm["phoneConfirmCode"].addEventListener("input", (event) => {
	let key = event.target.value;
	console.log(authCode2);
	if(key) {
		if(key === authCode2) {
			signupForm["user_phone"].setAttribute("readonly", "readonly");
			signupForm["phoneConfirmCode"].setAttribute("readonly", "readonly");
			phoneConfirmBtnDiv.setAttribute("style", "display: none");
			signupForm["phoneConfirmCode"].classList.remove("is-invalid");
			signupForm["phoneConfirmCode"].classList.add("is-valid");
			codeHelp2.classList.remove("is-invalid");
			codeHelp2.classList.add("is-valid");
			phoneConfirmSubmit = true;
		} else {
			codeHelpInvalid2.innerText = "인증번호가 일치하지 않습니다.";
			signupForm["phoneConfirmCode"].classList.remove("is-valid");
			signupForm["phoneConfirmCode"].classList.add("is-invalid");
			codeHelp2.classList.remove("is-valid");
			codeHelp2.classList.add("is-invalid");
			phoneConfirmSubmit = false;
		}
	} else {
		codeHelpInvalid2.innerText = "인증번호를 입력해 주세요.";
		signupForm["phoneConfirmCode"].classList.remove("is-valid");
		signupForm["phoneConfirmCode"].classList.add("is-invalid");
		codeHelp2.classList.remove("is-valid");
		codeHelp2.classList.add("is-invalid");
		phoneConfirmSubmit = false;
	}
	return phoneConfirmSubmit;
});

// 전화번호 인증 번호 공백 차단
signupForm["phoneConfirmCode"].addEventListener("keydown", (event) => {
	let key = event.key;
	if(key == " ") {
		event.preventDefault();
	}	
});

// 생일 값이 들어오면 빨간 표시 사라짐
signupForm["birth"].addEventListener("change", (event) => {
	if(event.target.value) {
		signupForm["birth"].classList.remove("is-invalid");
		birthHelp.classList.remove("is-invalid");
		birthSubmit = true;
	}	
	return birthSubmit;
});

// 우편번호 버튼 클릭 시 우편번호와 메인 주소의 '필수 정보입니다.' 문구 삭제 이벤트
signupForm["user_addr_postcode_btn"].addEventListener("click", (event) => {
	signupForm["add1"].classList.remove("is-invalid");
	addPostCodeHelp.classList.remove("is-invalid");
	signupForm["add2"].classList.remove("is-invalid");
	addMainHelp.classList.remove("is-invalid");
}); 

// 우편번호나 메인 주소 값이 있을 시 상세 주소에 마우스를 올리면 readonly가 삭제되는 이벤트
signupForm["add3"].addEventListener("mouseover", (event) => {
	if(signupForm["add1"].value || signupForm["add2"].value) {
		event.target.removeAttribute("readonly");
	}
}); 

// 상세 주소 입력 이벤트
signupForm["add3"].addEventListener("input", (event) => {
	let value = event.target.value;
	if(value && value.trim()) {
		signupForm["add3"].classList.remove("is-invalid");
		addDetailHelp.classList.remove("is-invalid");
		adressSubmit = true;
	} else {
		addDetailHelpInvalid.innerText = "상세 주소를 입력하세요.";
		signupForm["add3"].classList.add("is-invalid");
		addDetailHelp.classList.add("is-invalid");
		adressSubmit = false;
	}
	return adressSubmit;
});

// 우편번호나 메인 주소 값이 없을 시 상세 주소 입력 차단
signupForm["add3"].addEventListener("keydown", (event) => {
	if(!signupForm["add1"].value || !signupForm["add2"].value) {
		event.preventDefault();
	}	
});

// 최종 제출 이벤트
signupForm.addEventListener("submit", (event) => {
	event.preventDefault();
	if(!signupForm["user_name"].value) {
		nameHelpInvalid.innerText = "필수 정보입니다.";
		signupForm["user_name"].classList.remove("is-valid");
		signupForm["user_name"].classList.add("is-invalid");
		nameHelp.classList.remove("is-valid");
		nameHelp.classList.add("is-invalid");
	} 
	if(!signupForm["user_id"].value) {
		idHelpInvalid.innerText = "필수 정보입니다.";
		signupForm["user_id"].classList.remove("is-valid");
		signupForm["user_id"].classList.add("is-invalid");
		idHelp.classList.remove("is-valid");
		idHelp.classList.add("is-invalid");
	}
	if(!signupForm["user_pw"].value) {
		pwHelpInvalid.innerText = "필수 정보입니다.";
		signupForm["user_pw"].classList.remove("is-valid");
		signupForm["user_pw"].classList.add("is-invalid");
		pwHelp.classList.remove("is-valid");
		pwHelp.classList.add("is-invalid");
	}
	if(!signupForm["pwCheck"].value) {
		pwHelpInvalidCheck.innerText = "필수 정보입니다.";
		signupForm["pwCheck"].classList.remove("is-valid");
		signupForm["pwCheck"].classList.add("is-invalid");
		pwCheckHelp.classList.remove("is-valid");
		pwCheckHelp.classList.add("is-invalid");
	}
	if(!signupForm["user_email"].value) {
		emailHelpInvalid.innerText = "필수 정보입니다.";
		signupForm["user_email"].classList.remove("is-valid");
		signupForm["user_email"].classList.add("is-invalid");
		emailHelp.classList.remove("is-valid");
		emailHelp.classList.add("is-invalid");
	}
	if(!signupForm["emailConfirmCode"].value) {
		codeHelpInvalid.innerText = "필수 정보입니다.";
		signupForm["emailConfirmCode"].classList.remove("is-valid");
		signupForm["emailConfirmCode"].classList.add("is-invalid");
		codeHelp.classList.remove("is-valid");
		codeHelp.classList.add("is-invalid");
	}
	if(!signupForm["user_phone"].value) {
		phoneHelpInvalid.innerText = "필수 정보입니다.";
		signupForm["user_phone"].classList.remove("is-valid");
		signupForm["user_phone"].classList.add("is-invalid");
		phoneHelp.classList.remove("is-valid");
		phoneHelp.classList.add("is-invalid");
	}
	if(!signupForm["phoneConfirmCode"].value) {
		codeHelpInvalid2.innerText = "필수 정보입니다.";
		signupForm["phoneConfirmCode"].classList.remove("is-valid");
		signupForm["phoneConfirmCode"].classList.add("is-invalid");
		codeHelp2.classList.remove("is-valid");
		codeHelp2.classList.add("is-invalid");
	}
	if(!signupForm["birth"].value) {
		birthHelpInvalid.innerText = "필수 정보입니다.";
		signupForm["birth"].classList.remove("is-valid");
		signupForm["birth"].classList.add("is-invalid");
		birthHelp.classList.remove("is-valid");
		birthHelp.classList.add("is-invalid");
	}
	if(!signupForm["add1"].value) {
		addPostCodeHelpInvalid.innerText = "필수 정보입니다.";
		signupForm["add1"].classList.remove("is-valid");
		signupForm["add1"].classList.add("is-invalid");
		addPostCodeHelp.classList.remove("is-valid");
		addPostCodeHelp.classList.add("is-invalid");
	}
	if(!signupForm["add2"].value) {
		addMainHelpInvalid.innerText = "필수 정보입니다.";
		signupForm["add2"].classList.remove("is-valid");
		signupForm["add2"].classList.add("is-invalid");
		addMainHelp.classList.remove("is-valid");
		addMainHelp.classList.add("is-invalid");
	}
	if(!signupForm["add3"].value || !signupForm["add3"].value.trim()) {
		addDetailHelpInvalid.innerText = "필수 정보입니다.";
		signupForm["add3"].classList.remove("is-valid");
		signupForm["add3"].classList.add("is-invalid");
		addDetailHelp.classList.remove("is-valid");
		addDetailHelp.classList.add("is-invalid");
	}
	if(nameSubmit && idSubmit && pwSubmit && pwCheckSubmit && emailSubmit && emailConfirmSubmit 
		&& phoneSubmit && phoneConfirmSubmit && birthSubmit && adressSubmit) {
		if(signupForm["user_pw"].value === signupForm["pwCheck"].value) {
			signupForm.submit();	
		} else {
			pwHelpInvalidCheck.innerText = "비밀번호가 불일치합니다.";
			signupForm["pwCheck"].classList.remove("is-valid");
			signupForm["pwCheck"].classList.add("is-invalid");
			pwCheckHelp.classList.remove("is-valid");
			pwCheckHelp.classList.add("is-invalid");
		}		
	}
}); 