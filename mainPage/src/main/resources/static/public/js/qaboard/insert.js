function noSpace(obj) { // 공백사용못하게
    let str = /\s/;  // 공백체크
    if(str.exec(obj.value.trim)) { //공백 체크
        //alert("해당 항목에는 첫 글자 공백을 사용할수 없습니다.");
        obj.focus();
        obj.value = obj.value.replace(/^ +/,''); // 공백제거
        return false;
    }
}
const qaInsertForm = document.forms.qaInsertForm;
const qaBoardTitle = qaInsertForm.qaBoardTitle;
const qaBoardContents = qaInsertForm.qaBoardContents;
qaInsertForm.addEventListener("submit", (event) => {
	if(!qaBoardTitle.value || !qaBoardContents.value) {
		event.preventDefault();		
	}
}, {passive: false});