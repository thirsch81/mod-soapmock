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
		eb.onclose = function() {
			eb = null;
		};
	}
}

function closeConn() {
	if (eb) {
		eb.close();
	}
}