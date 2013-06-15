jQuery(function() {
	$(".script-input").keyup(function() {
		var buttonSelector = "#" + $(this).attr("id").replace("input", "submit");
		if ($(this).val() == "") {
			disableButton(buttonSelector);
		} else {
			enableButton(buttonSelector);
		}
	});
});

function disableButton(selector) {
	$(selector).attr("disabled", "disabled");
}

function enableButton(selector) {
	$(selector).removeAttr("disabled");
}

function showErrorMessage(message) {
	$(".alert-container").prepend(errorMessage(message));
}

function showSuccessMessage(message) {
	$(".alert-container").prepend(successMessage(message));
}

var errorMessage = function(text) {
	return '<div class="error-message alert alert-block alert-error fade in">'
		+ '	<button type="button" class="close" data-dismiss="alert">x</button>'
		+ '	<strong class="alert-heading">Error!</strong><p>' + text + '</p>'
		+ '</div>';
}
var successMessage = function(text) {
	return '<div class="success-message alert alert-block alert-success fade in">'
		+ '	<button type="button" class="close" data-dismiss="alert">x</button>'
		+ '	<strong class="alert-heading">Success!</strong><p>' + text + '</p>'
		+ '</div>';
}