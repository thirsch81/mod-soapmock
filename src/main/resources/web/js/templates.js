jQuery(function() {
	$(document).on("click", ".template-delete", function() {
		var templateName = $(this).attr("id").replace("-template-delete", "");
		removeTemplate(templateName);
	});
	$(document).on("click", ".template-submit", function() {
		var templateName = $(this).attr("id").replace("-template-submit", "");
		submitTemplate(templateName);
	});
	$("a[href='#templates']").click(function() {
		fetchTemplates();
	});
	$("#add-template").click(function() {
		addTemplate();
	});
});

function fetchTemplates() {
	ebSend("renderer.templates", {
		"action" : "fetch"
	}, updateTemplates);
}

function submitTemplate(name) {
	disableButton("#" + name + "-template-submit");
	ebSend("renderer.templates", {
		"action" : "submit",
		"script" : $("#rule-script-input").val()
	}, handleReplyStatus);
}

function updateTemplates(reply) {
	handleReplyStatus(reply);
	if (reply.status == "ok") {
		insertTemplates(reply.templates)
	}
}

function insertTemplates(templates) {
	for ( var i = 0; i < templates.length; i++) {
		insertTemplate(templates[i].name);
	}
}

function addTemplate() {
	var name = $("#input-template-name").val();
	insertTemplate(name);
}

function insertTemplate(name) {
	$("#template-tabs").append(templateTab(name));
	$("#template-container").append(templateTabContent(name));
}

function removeTemplate(name) {
	$("li a[href=#" + name + "]").remove();
	$("#" + name).remove();
}

function templateTab(name) {
	return '<li><a href="#' + name + '" data-toggle="tab">' + name + '</a></li>';
}

function templateTabContent(name) {
	return '<div class="tab-pane" id="'
			+ name
			+ '"><textarea id="'
			+ name
			+ '-template-input" class="script-input span9" rows="10">'
			+ '</textarea><div class="row"><div class="offset4"><button id="'
			+ name
			+ '-template-submit" class="btn btn-inverse disabled template-submit span2">Submit template</button><button id="'
			+ name
			+ '-template-delete" class="btn btn-danger template-delete span2">Delete template</button></div></div></div>';
}