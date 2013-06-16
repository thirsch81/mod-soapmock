var eb = null;

jQuery(function() {
	openConn();
});

function ebPublish(address, message, handler) {
	if (eb) {
		eb.publish(address, message, handler)
	}
}

function ebSend(address, message, handler) {
	if (eb) {
		eb.send(address, message, handler)
	}
}

function openConn() {
	if (!eb) {
		eb = new vertx.EventBus("http://localhost:8080/eventbus");
		eb.onopen = function() {
			statusConnected()
		}
		eb.onclose = function() {
			eb = null;
			statusDisconnected()
		};
	}
}

function closeConn() {
	if (eb) {
		eb.close();
	}
}

function statusConnected() {
	$connectionStatus = $(".connection-status")
	$connectionStatus.text("connected")
	$connectionStatus.removeClass("text-error")
	$connectionStatus.addClass("text-success")
}

function statusDisconnected() {
	$connectionStatus = $(".connection-status")
	$connectionStatus.text("disconnected")
	$connectionStatus.removeClass("text-success")
	$connectionStatus.addClass("text-error")
}
