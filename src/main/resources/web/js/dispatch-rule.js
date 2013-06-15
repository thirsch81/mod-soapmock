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
	}, updateScript);
}

function submitDispatchScript() {
	disableButton("#rule-script-submit");
	eb.send("extractor.dispatchRule", {
		"action" : "submit",
		"script" : $("#rule-script-input").val()
	}, handleReply);
}

function handleReply(reply) {
	if ("ok" == reply.status) {
		showSuccessMessage(JSON.stringify(reply));
	} else {
		showErrorMessage(JSON.stringify(reply));
	}
}

function updateScript(reply) {
	handleReply(reply);
	$("#rule-script-input").val(reply.script);
	disableButton("#rule-script-submit");
}