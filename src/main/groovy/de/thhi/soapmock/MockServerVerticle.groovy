package de.thhi.soapmock

import org.vertx.groovy.core.http.HttpServer
import org.vertx.groovy.core.http.RouteMatcher
import org.vertx.groovy.platform.Verticle

public class MockServerVerticle extends Verticle {

	def start () {

		RouteMatcher rm = new RouteMatcher()
		rm.get("/test") { request ->
			container.logger.info("MockServer: received request")
			request.response.end("Mock server running")
		}
		rm.post("/render") { request ->
			container.logger.info("MockServer: received request")
			request.bodyHandler { body ->
				container.logger.info("MockServer: ${body}")
				vertx.eventBus.send("extract", ["source" : body.toString()]) { extractReply ->
					container.logger.info("MockServer: received ${extractReply.body}")
					vertx.eventBus.send("render", ["name" : "response", "binding" : extractReply.body.binding]) { renderReply ->
						container.logger.info("MockServer: received ${renderReply.body}")
						request.response.end(renderReply.body.response)
					}
				}
			}
		}
		HttpServer server = vertx.createHttpServer()
		server.requestHandler(rm.asClosure()).listen(8080, "localhost")

		container.logger.info("MockServerVerticle started")
	}
}