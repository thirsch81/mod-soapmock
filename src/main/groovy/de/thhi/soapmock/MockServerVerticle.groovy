package de.thhi.soapmock

import org.vertx.groovy.core.http.RouteMatcher
import org.vertx.groovy.platform.Verticle

public class MockServerVerticle extends Verticle {

	def start () {

		RouteMatcher rm = new RouteMatcher()
		rm.get("/test") { request ->
			
			eb.send("render", ["name" : "response", "binding" : ["content" : "some content"]]) { reply ->
				
				request.response.end(reply.body.response)
				
			}
			
			
		}
		def server = vertx.createHttpServer()
		server.requestHandler { rm.asClosure() }.listen(8080, "localhost")
		


		container.logger.info("MockServer verticle started");
	}
}