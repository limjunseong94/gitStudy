const recommendation = document.forms.recommendation;

recommendation.period.addEventListener("change", (event) => {
	let value = event.target.value;
	console.log(value);
	if(value == 1) {
		recommendation.acco.setAttribute("disabled", "disabled");
	}
	if(value != 1) {
		recommendation.acco.removeAttribute("disabled");
	}
});