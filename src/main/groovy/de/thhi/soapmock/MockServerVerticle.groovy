package de.thhi.soapmock;

import com.jetdrone.vertx.yoke.GYoke
import com.jetdrone.vertx.yoke.Yoke
import com.jetdrone.vertx.yoke.engine.GroovyTemplateEngine
import com.jetdrone.vertx.yoke.middleware.GRouter
import org.vertx.groovy.platform.Verticle

public class MockServerVerticle extends Verticle {

	def start() {
		Yoke yoke = new GYoke(vertx)
				.engine("template", new GroovyTemplateEngine())
				.use(new GRouter()
				.all("") { request ->
					request.reponse().render("template/xml/response.template", [content: "Some content"])
				}
				).listen(8080)
		container.logger().info("MockServer verticle started");
	}
}