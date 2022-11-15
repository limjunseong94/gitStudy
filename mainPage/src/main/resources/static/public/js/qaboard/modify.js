const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]')
const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl));

function noSpace(obj) { // 공백사용못하게
    let str = /\s/;  // 공백체크
    if(str.exec(obj.value.trim)) { //공백 체크
        //alert("해당 항목에는 첫 글자 공백을 사용할수 없습니다.");
        obj.focus();
        obj.value = obj.value.replace(/^ +/,''); // 공백제거
        return false;
    }
}

const qaUpdateForm = document.forms.qaUpdateForm;
const qaBoardTitle = qaUpdateForm.qaBoardTitle;
const qaBoardContents = qaUpdateForm.qaBoardContents;
qaUpdateForm.addEventListener("submit", (event) => {
	if(!qaBoardTitle.value || !qaBoardContents.value) {
		event.preventDefault();		
	}
}, {passive: false});