jQuery(function() {
	$(".script-input").keyup(function() {
		var buttonName = $(this).attr("id").replace("input", "submit")
		if ($(this).val() == "") {
			disableButton(buttonName);
		} else {
			enableButton(buttonName);
		}
	});
});

function disableButton(id) {
	$("#" + id).attr("disabled", "disabled");
}

function enableButton(id) {
	$("#" + id).removeAttr("disabled");
}