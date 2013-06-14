jQuery(function() {
	$(".script-input").keyup(function() {
		var buttonName = $(this).attr("name").replace("input", "submit")
		if ($(this).val() == "") {
			disableButton(buttonName);
		} else {
			enableButton(buttonName);
		}
	});
});

function disableButton(name) {
	$("[name='" + name + "']").attr("disabled", "disabled");
}

function enableButton(name) {
	$("[name='" + name + "']").removeAttr("disabled");
}