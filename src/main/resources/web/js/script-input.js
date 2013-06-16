jQuery(function() {
	$(document).on("keyup", ".script-input", function() {
		var buttonSelector = "#" + $(this).attr("id").replace("input", "submit");
		if ($(this).val() == "") {
			disableButton(buttonSelector);
		} else {
			enableButton(buttonSelector);
		}
	});
});

function disableButton(selector) {
	$(selector).addClass("disabled");
}

function enableButton(selector) {
	$(selector).removeClass("disabled");
}

function handleReplyStatus(reply) {
	if ("ok" == reply.status) {
		showSuccessMessage(JSON.stringify(reply));
	} else {
		showErrorMessage(JSON.stringify(reply));
	}
}

function showErrorMessage(message) {
	clearMessages();
	$(".alert-container").prepend(errorMessage(message));
	$(".error-message").addClass("in")
}

function showSuccessMessage(message) {
	clearMessages();
	$(".alert-container").prepend(successMessage(message));
	setTimeout(function() {
		$(".success-message").addClass("in")
	});
}

function clearMessages() {
	$(".alert-container").html("");
}

var errorMessage = function(text) {
	return '<div class="error-message alert alert-block alert-error fade">'
			+ '	<button type="button" class="close" data-dismiss="alert">x</button>'
			+ '	<strong class="alert-heading">Error!</strong><p>' + text + '</p></div>';
}
var successMessage = function(text) {
	return '<div class="success-message alert alert-block alert-success fade">'
			+ '	<button type="button" class="close" data-dismiss="alert">x</button>'
			+ '	<strong class="alert-heading">Success!</strong><p>' + text + '</p></div>';
}