const updateForm = document.forms.updateForm;

// ajax url
const ajaxEmailUrl = "/user/emailCheck/"; // unique key
const ajaxPhoneUrl = "/user/phoneCheck/"; // unique key

// submit event 처리 변수
let nameSubmit = true;
let pwSubmit = true;
let emailSubmit = true;
let phoneSubmit = true;
let birthSubmit = true;
let adressSubmit = true;

// 이름 정규식
function nameCheck(v) {
	let name_pattern = /^[a-zA-Zㄱ-힣][a-zA-Zㄱ-힣]*$/; // 영문 또는 한글
 
 	if(name_pattern.test(v) === true) {
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

function noSpaceForm(obj) { // 공백 사용 못하게
   let str_space = /\s/;  // 공백 체크
   if(str_space.exec(obj.value.trim)) { //공백 체크
       //alert("해당 항목에는 공백을 사용할수 없습니다.\n\n공백은 자동적으로 제거 됩니다.");
       obj.focus();
       obj.value = obj.value.replace(/\s| /gi,''); // 공백 제거
       return false;
   }
}

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

// 이름 입력 이벤트
updateForm["user_name"].addEventListener("input", (event) => {
	let value = event.target.value;
	if(value && value.trim()) {
		if(value.length > 1 && value.length < 21) {
			if(nameCheck(value)) {
				updateForm["user_name"].classList.remove("is-invalid");
				updateForm["user_name"].classList.add("is-valid");
				nameHelp.classList.remove("is-invalid");
				nameHelp.classList.add("is-valid");
				nameSubmit = true;
			} else {
				nameHelpInvalid.innerText = "유효하지 않은 형식입니다. (영문 또는 한글)";
				updateForm["user_name"].classList.remove("is-valid");
				updateForm["user_name"].classList.add("is-invalid");
				nameHelp.classList.remove("is-valid");
				nameHelp.classList.add("is-invalid");
				nameSubmit = false;
			}		
		} else {
			nameHelpInvalid.innerText = "2 ~ 20자의 영문 또는 한글로 입력하세요.";
			updateForm["user_name"].classList.remove("is-valid");
			updateForm["user_name"].classList.add("is-invalid");
			nameHelp.classList.remove("is-valid");
			nameHelp.classList.add("is-invalid");
			nameSubmit = false;
		}	
	} else {
		nameHelpInvalid.innerText = "이름을 입력하세요.";
		updateForm["user_name"].classList.remove("is-valid");
		updateForm["user_name"].classList.add("is-invalid");
		nameHelp.classList.remove("is-valid");
		nameHelp.classList.add("is-invalid");
		nameSubmit = false;
	}
	return nameSubmit;
});

// 이름 공백 차단
updateForm["user_name"].addEventListener("keydown", (event) => {
	let key = event.key;
	if(key == " ") {
		event.preventDefault();
	}
});

// 비밀번호 입력 이벤트
updateForm["user_pw"].addEventListener("input", (event) => {
	let value = event.target.value;
	if(value && value.trim()) {
		if(value.length > 7 && value.length < 17) {		
			if(passwordCheck(value) && isNaN(value)) {
				if(passwordChanged(value) === false) {
					updateForm["user_pw"].classList.remove("is-valid");
					updateForm["user_pw"].classList.add("is-invalid");
					pwHelp.classList.remove("is-valid");
					pwHelp.classList.add("is-invalid");
					pwSubmit = false;
				} else {
					updateForm["user_pw"].classList.remove("is-invalid");
					updateForm["user_pw"].classList.add("is-valid");
					pwHelp.classList.remove("is-invalid");
					pwHelp.classList.add("is-valid");
					pwSubmit = true;		
				}
			} else {
				pwHelpInvalid.innerText = "유효하지 않은 형식입니다. (대문자 + 소문자 + 숫자 + 특수 문자)";
				updateForm["user_pw"].classList.remove("is-valid");
				updateForm["user_pw"].classList.add("is-invalid");
				pwHelp.classList.remove("is-valid");
				pwHelp.classList.add("is-invalid");
				pwSubmit = false;
			}	
		} else {
			pwHelpInvalid.innerText = "8 ~ 16자의 (대문자 + 소문자 + 숫자 + 특수 문자) 형식으로 입력하세요.";
			updateForm["user_pw"].classList.remove("is-valid");
			updateForm["user_pw"].classList.add("is-invalid");
			pwHelp.classList.remove("is-valid");
			pwHelp.classList.add("is-invalid");
			pwSubmit = false;
		}
	} else {
		pwHelpInvalid.innerText = "비밀번호를 입력하세요.";
		updateForm["user_pw"].classList.remove("is-valid");
		updateForm["user_pw"].classList.add("is-invalid");
		pwHelp.classList.remove("is-valid");
		pwHelp.classList.add("is-invalid");
		pwSubmit = false;
	}
	return pwSubmit;
});

// 비밀번호 공백 차단
updateForm["user_pw"].addEventListener("keydown", (event) => {
	let key = event.key;
	if(key == " ") {
		event.preventDefault();
	}
});

// 이메일 입력 이벤트
updateForm["post_user_email"].addEventListener("input", (event) => {
	let value = event.target.value;
	if(value && value.trim()) {
		if(checkEmail(value) && isNaN(value)) {
				fetch(ajaxEmailUrl + value)
					.then(response => response.json())
					.then((json) => {
						if(json.emailCheck && value != updateForm["user_email"].value) {
							emailHelpInvalid.innerText = "이미 사용 중인 이메일입니다.";
							updateForm["post_user_email"].classList.remove("is-valid");
							updateForm["post_user_email"].classList.add("is-invalid");
							emailHelp.classList.remove("is-valid");
							emailHelp.classList.add("is-invalid");
							emailSubmit = false;
						} else {
							if(value != updateForm["user_email"].value) {
								updateForm["post_user_email"].classList.remove("is-invalid");
								updateForm["post_user_email"].classList.add("is-valid");
								emailHelp.classList.remove("is-invalid");
								emailHelp.classList.add("is-valid");
								emailSubmit = true;
							} else {
								updateForm["post_user_email"].classList.remove("is-invalid");
								updateForm["post_user_email"].classList.remove("is-valid");
								emailHelp.classList.remove("is-invalid");
								emailHelp.classList.remove("is-valid");
								emailSubmit = true;
							}
						}
					});
			} else {
				emailHelpInvalid.innerText = "유효하지 않은 형식입니다. 예시) Kevin@example.com";
				updateForm["post_user_email"].classList.remove("is-valid");
				updateForm["post_user_email"].classList.add("is-invalid");
				emailHelp.classList.remove("is-valid");
				emailHelp.classList.add("is-invalid");
				emailSubmit = false;
			}	
	} else {
		emailHelpInvalid.innerText = "이메일을 입력하세요.";
		updateForm["post_user_email"].classList.remove("is-valid");
		updateForm["post_user_email"].classList.add("is-invalid");
		emailHelp.classList.remove("is-valid");
		emailHelp.classList.add("is-invalid");
		emailSubmit = false;
	}
	return emailSubmit;
});

// 이메일 공백 차단
updateForm["post_user_email"].addEventListener("keydown", (event) => {
	let key = event.key;
	if(key == " ") {
		event.preventDefault();
	}
});

// 전화번호 입력 이벤트
updateForm["post_user_phone"].addEventListener("input", (event) => {
	let value = event.target.value;
	if(value && value.trim()) {
		if(checkPhone(value)) {
			fetch(ajaxPhoneUrl + value)
				.then(response => response.json())
				.then((json) => {
					if(json.phoneCheck && value != updateForm["user_phone"].value) {
						phoneHelpInvalid.innerText = "이미 사용 중인 전화번호입니다.";
						updateForm["post_user_phone"].classList.remove("is-valid");
						updateForm["post_user_phone"].classList.add("is-invalid");
						phoneHelp.classList.remove("is-valid");
						phoneHelp.classList.add("is-invalid");
						phoneSubmit = false;
					} else {
						if(value != updateForm["user_phone"].value) {
							updateForm["post_user_phone"].classList.remove("is-invalid");
							updateForm["post_user_phone"].classList.add("is-valid");
							phoneHelp.classList.remove("is-invalid");
							phoneHelp.classList.add("is-valid");
							phoneSubmit = true;
						} else {
							updateForm["post_user_phone"].classList.remove("is-invalid");
							updateForm["post_user_phone"].classList.remove("is-valid");
							phoneHelp.classList.remove("is-invalid");
							phoneHelp.classList.remove("is-valid");
							phoneSubmit = true;
						}
						
					}
				});
		} else {
			phoneHelpInvalid.innerText = "유효하지 않은 형식입니다. (01[0|1|6|7|8|9]-1234-1234 형식의 숫자)";
			updateForm["post_user_phone"].classList.remove("is-valid");
			updateForm["post_user_phone"].classList.add("is-invalid");
			phoneHelp.classList.remove("is-valid");
			phoneHelp.classList.add("is-invalid");
			phoneSubmit = false;
		}		
	} else {
		phoneHelpInvalid.innerText = "전화번호를 입력하세요.";
		updateForm["post_user_phone"].classList.remove("is-valid");
		updateForm["post_user_phone"].classList.add("is-invalid");
		phoneHelp.classList.remove("is-valid");
		phoneHelp.classList.add("is-invalid");
		phoneSubmit = false;
	}
	return phoneSubmit;
});

// 전화번호 공백 차단
updateForm["post_user_phone"].addEventListener("keydown", (event) => {
	let key = event.key;
	if(key == " ") {
		event.preventDefault();
	}	
});

// 생일 값이 들어오면 빨간 표시 사라짐
updateForm["birth"].addEventListener("change", (event) => {
	if(updateForm["birth"].value) {
		updateForm["birth"].classList.remove("is-invalid");
		birthHelp.classList.remove("is-invalid");
		birthSubmit = true;
	}	
});

// 우편번호 버튼 클릭 시 우편번호와 메인 주소의 '필수 정보입니다.' 문구 삭제 이벤트
updateForm["user_addr_postcode_btn"].addEventListener("click", (event) => {
	updateForm["add1"].classList.remove("is-invalid");
	addPostCodeHelp.classList.remove("is-invalid");
	updateForm["add2"].classList.remove("is-invalid");
	addMainHelp.classList.remove("is-invalid");
}); 

// 우편번호나 메인 주소 값이 있을 시 상세 주소에 마우스를 올리면 readonly가 삭제되는 이벤트
updateForm["add3"].addEventListener("mouseover", (event) => {
	if(updateForm["add1"].value || updateForm["add2"].value) {
		event.target.removeAttribute("readonly");
	}
}); 

// 상세 주소 입력 이벤트
updateForm["add3"].addEventListener("input", (event) => {
	let value = event.target.value;
	if(value && value.trim()) {
		updateForm["add3"].classList.remove("is-invalid");
		addDetailHelp.classList.remove("is-invalid");
		adressSubmit = true;
	} else {
		addDetailHelpInvalid.innerText = "상세 주소를 입력하세요.";
		updateForm["add3"].classList.add("is-invalid");
		addDetailHelp.classList.add("is-invalid");
		adressSubmit = false;
	}
	return adressSubmit;
});

// 우편번호나 메인 주소 값이 없을 시 상세 주소 입력 차단
updateForm["add3"].addEventListener("keydown", (event) => {
	if(!signupForm["add1"].value || !signupForm["add2"].value) {
		event.preventDefault();
	}	
});

// 초기화 버튼 클릭 시 전부 원상복귀
updateForm.resetBtn.addEventListener("click", (event) => {
	document.querySelectorAll("input").forEach((input) => {
		input.classList.remove("is-invalid");
		input.classList.remove("is-valid");
	});
	document.querySelectorAll("span").forEach((span) => {
		span.classList.remove("is-invalid");
		span.classList.remove("is-valid");
	});
	nameSubmit = true;
	pwSubmit = true;
	emailSubmit = true;
	phoneSubmit = true;
	birthSubmit = true;
	adressSubmit = true;
});

// 최종 제출 이벤트
updateForm.addEventListener("submit", (event) => {
	event.preventDefault();
	if(!updateForm["user_name"].value) {
		nameHelpInvalid.innerText = "필수 정보입니다.";
		updateForm["user_name"].classList.remove("is-valid");
		updateForm["user_name"].classList.add("is-invalid");
		nameHelp.classList.remove("is-valid");
		nameHelp.classList.add("is-invalid");
		nameSubmit = false;
	} 
	if(!updateForm["user_pw"].value) {
		pwHelpInvalid.innerText = "필수 정보입니다.";
		updateForm["user_pw"].classList.remove("is-valid");
		updateForm["user_pw"].classList.add("is-invalid");
		pwHelp.classList.remove("is-valid");
		pwHelp.classList.add("is-invalid");
		pwSubmit = false;
	} 
	if(!updateForm["post_user_email"].value) {
		emailHelpInvalid.innerText = "필수 정보입니다.";
		updateForm["post_user_email"].classList.remove("is-valid");
		updateForm["post_user_email"].classList.add("is-invalid");
		emailHelp.classList.remove("is-valid");
		emailHelp.classList.add("is-invalid");
		emailSubmit = false;
	} 
	if(!updateForm["post_user_phone"].value) {
		phoneHelpInvalid.innerText = "필수 정보입니다.";
		updateForm["post_user_phone"].classList.remove("is-valid");
		updateForm["post_user_phone"].classList.add("is-invalid");
		phoneHelp.classList.remove("is-valid");
		phoneHelp.classList.add("is-invalid");
		phoneSubmit = false;
	} 
	if(!updateForm["birth"].value) {
		birthHelpInvalid.innerText = "필수 정보입니다.";
		updateForm["birth"].classList.remove("is-valid");
		updateForm["birth"].classList.add("is-invalid");
		birthHelp.classList.remove("is-valid");
		birthHelp.classList.add("is-invalid");
		birthSubmit = false;
	} 
	if(!updateForm["add1"].value) {
		addPostCodeHelpInvalid.innerText = "필수 정보입니다.";
		updateForm["add1"].classList.remove("is-valid");
		updateForm["add1"].classList.add("is-invalid");
		addPostCodeHelp.classList.remove("is-valid");
		addPostCodeHelp.classList.add("is-invalid");
	} 
	if(!updateForm["add2"].value) {
		addMainHelpInvalid.innerText = "필수 정보입니다.";
		updateForm["add2"].classList.remove("is-valid");
		updateForm["add2"].classList.add("is-invalid");
		addMainHelp.classList.remove("is-valid");
		addMainHelp.classList.add("is-invalid");
	} 
	if(!updateForm["add3"].value || !signupForm["add3"].value.trim()) {
		addDetailHelpInvalid.innerText = "필수 정보입니다.";
		updateForm["add3"].classList.remove("is-valid");
		updateForm["add3"].classList.add("is-invalid");
		addDetailHelp.classList.remove("is-valid");
		addDetailHelp.classList.add("is-invalid");
		adressSubmit = false;
	} 
	if(nameSubmit && pwSubmit && emailSubmit && phoneSubmit && birthSubmit && adressSubmit) {	
		updateForm.submit();	
	}
});