const replyList = document.getElementById("replyList");
//setPageLing();
/*
function setPageLing(){
	const pageLinkList = pageAjax.querySelectorAll("#pageAjax .page-link");
	pageLinkList.forEach((item) => {
		item.addEventListener("click", replyListHadler);
	})	
}
*/
/*
async function replyListHadler(e){
	let url = e.target.dataset.url;
	let page = e.target.dataset.page;
	url += "/" + page;
	console.log(url);
	let res = await fetch(url);
	if(res.status == 200){
		let text = await res.text();
		replyList.innerHTML = text;		
	}
	setPageLing();
}	
*/
//값이 없을 때 submit 불가

const replyUpdateForm = document.querySelectorAll("form");
replyUpdateForm.forEach((form) => {
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