function noSpace(obj) { // 공백사용못하게
    let str = /\s/;  // 공백체크
    if(str.exec(obj.value.trim)) { //공백 체크
        //alert("해당 항목에는 첫 글자 공백을 사용할수 없습니다.");
        obj.focus();
        obj.value = obj.value.replace(/^ +/,''); // 공백제거
        return false;
    }
}

//값이 없을 때 submit 불가
const noticeInsertForm = document.forms.noticeInsertForm;
const title = noticeInsertForm.title;
const contents = noticeInsertForm.contents;
noticeInsertForm.addEventListener("submit", (event) => {
	if(!title.value || !contents.value) {
		event.preventDefault();		
	}
}, {passive: false});