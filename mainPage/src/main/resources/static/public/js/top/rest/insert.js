//값이 없을 때 submit 불가
const restInsertForm = document.forms.restInsertForm;
let tourist = restInsertForm.tourist;
let province = restInsertForm.province;
let address = restInsertForm.address2;
let search = restInsertForm.search;
restInsertForm.addEventListener("submit", (event) => {
	if(!tourist.value || !province.value || !address.value || !search.value) {
		event.preventDefault();		
	}
}, {passive: false});

function noSpace(obj) { // 공백사용못하게
    let str = /\s/;  // 공백체크
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