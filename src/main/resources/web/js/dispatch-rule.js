jQuery(function() {
	$("#rule-script-submit").click(function() {
		submitDispatchScript();
	});
	$("a[href='#dispatch']").click(function() {
		fetchDispatchScript();
	});
});

function fetchDispatchScript() {
	eb.send("extractor.dispatchRule", {
		"action" : "fetch"
	}, function(reply) {
		if ("ok".equals(reply.body.status)) {
			showSuccessMessage(reply.body.toSource());
		} else {
			showErrorMessage(reply.body.toSource());
		}
	});
}

function submitDispatchScript() {
	eb.send("extractor.dispatchRule", {
		"action" : "fetch"
	}, function(reply) {
		if ("ok".equals(reply.body.status)) {
			showSuccessMessage(reply.body.toSource());
		} else {
			showErrorMessage(reply.body.toSource());
		}
	});
}

function showSuccessMessage(message) {
	$("#success-message").addClass("in");
	$("#success-message p").text(message);
}

function showErrorMessage(message) {
	$("#error-message").addClass("in");
	$("#success-message p").text(message);
}