async function replyPreferHandler(replyNo, preferActive, btn) {
	console.log(preferActive);
	let url = "/reply/prefer";
	let prefer = ((btn == "good") ? true : false);
	let method;
	let replyLiId = "replyLi" + replyNo;
	
	if(preferActive == null) {
		url += "/insert/" + replyNo + "/" + prefer;
		method = "post";
		//msg += "등록 성공!";
	}else if((preferActive && prefer) || (!preferActive && !prefer)) {
		url += "/delete/" + replyNo;		
		method = "delete";
	}else {
		url += "/update/" + replyNo + "/" + prefer;
		method = "put";
	}
	try{
		let res = await fetch(url, {method : method});
		if(res.status == 200) {
			let htmlText = await res.text();
			console.log(htmlText);
			document.getElementById(replyLiId).innerHTML = htmlText;
		}else if(res.status == 400) {
			alert('로그인 하세요.');
		}else{
			alert('잘못된 시도입니다.(db, server 오류)');
		}
	} catch(err) {
		alert('잘못된 시도입니다.(js 오류)');
	}
}
const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]')
const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl));
//값이 없을 때 submit 불가
let replyInsert = document.forms.replyInsert;
let replyContents = replyInsert.contents;
replyInsert.addEventListener("submit", (event) => {
	if(!(replyContents.value)) {
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