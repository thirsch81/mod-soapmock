jQuery(function() {
	$("[name='script-input']").keyup(function() {
		if ($(this).val() == "") {
			$("[name='script-submit']").attr("disabled", "disabled");
		} else {
			$("[name='script-submit']").removeAttr("disabled");
		}
	});
});