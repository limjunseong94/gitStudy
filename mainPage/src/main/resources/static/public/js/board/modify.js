const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]')
const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl));

//값이 없을 때 submit 불가
let boardUpdateForm = document.forms.boardUpdateForm;
let title = boardUpdateForm.title;
let contents = boardUpdateForm.contents;
let place = boardUpdateForm["place_name"];
boardUpdateForm.addEventListener("submit", (event) => {
	if(!title.value || !contents.value || !place.value) {
		event.preventDefault();		
	}
}, {passive: false});

function noSpace(obj) { // 공백사용못하게
    var str = /\s/;  // 공백체크
    if(str.exec(obj.value.trim)) { //공백 체크
        //alert("해당 항목에는 첫 글자 공백을 사용할수 없습니다.");
        obj.focus();
        obj.value = obj.value.replace(/^ +/,''); // 공백제거
        return false;
    }
}