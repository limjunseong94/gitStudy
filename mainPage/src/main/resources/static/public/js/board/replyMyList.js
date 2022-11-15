//값이 없을 때 submit 불가
const replyUpdate = document.querySelectorAll("form");
replyUpdate.forEach((form) => {
	let contents = form.contents;
	form.addEventListener("submit", (event) => {
	if(!(contents.value)) {
		event.preventDefault();		
	}
	}, {passive: false});
});
function noSpace(obj) { // 공백사용못하게
    let str = /\s/;  // 공백체크
    if(str.exec(obj.value.trim)) { //공백 체크
        //alert("해당 항목에는 첫 글자 공백을 사용할수 없습니다.");
        obj.focus();
        obj.value = obj.value.replace(/^ +/,''); // 공백제거
        return false;
    }
}