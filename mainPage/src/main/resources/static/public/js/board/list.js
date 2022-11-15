function noSpaceForm(obj) { // 공백사용못하게
    let str_space = /\s/;  // 공백체크
    if(str_space.exec(obj.value.trim)) { //공백 체크
        //alert("해당 항목에는 공백을 사용할수 없습니다.\n\n공백은 자동적으로 제거 됩니다.");
        obj.focus();
        obj.value = obj.value.replace(/\s| /gi,''); // 공백제거
        return false;
    }
}
const reviewSearchForm = document.forms.reviewSearchForm;
reviewSearchForm.addEventListener("submit", (event) => {
	if(!reviewSearchForm.search.value) {
		event.preventDefault();		
	}
}, {passive: false});