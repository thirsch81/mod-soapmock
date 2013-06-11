import static org.vertx.testtools.VertxAssert.*

import org.vertx.groovy.core.http.HttpClientResponse
import org.vertx.groovy.testtools.VertxTests

import de.thhi.soapmock.MockServerVerticle
import de.thhi.soapmock.RenderVerticle
import de.thhi.soapmock.StarterVerticle

// The test methods must being with "test"

def testHTTP() {

	container.deployVerticle("groovy:" + StarterVerticle.class.name) { result ->
		assertNotNull(result)
		assertTrue(result.cause(), result.succeeded)
		vertx.createHttpClient().setHost("localhost").setPort(8080).getNow("/test") { HttpClientResponse resp ->
			assertEquals(200, resp.statusCode)
			resp.bodyHandler {
				container.logger.info(it)
				assertTrue(it.toString().contains("Mock server running"))
				testComplete()
			}
		}
	}
}

def testDeployMockServerVerticle() {
	container.deployVerticle("groovy:" + MockServerVerticle.class.name) { result ->
		assertTrue(result.cause(), result.succeeded)
		testComplete()
	}
}

def testDeployRenderVerticle() {
	container.deployWorkerVerticle("groovy:" + RenderVerticle.class.name) { result ->
		assertTrue(result.cause(), result.succeeded)
		testComplete()
	}
}

def testRenderVerticle() {
	container.deployWorkerVerticle("groovy:" + RenderVerticle.class.name) { result ->
		assertTrue(result.cause(), result.succeeded)
		vertx.eventBus.send("render", ["name" : "response", "binding" : ["content" : "some content"]]) { reply ->
			assertNotNull(reply)
			container.logger.info(reply.body)
			reply.body.with {
				assertEquals(it.status, "ok")
				assertNotNull(it.response)
				assertTrue(it.response.contains("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"))
			}
			testComplete()
		}
	}
}

VertxTests.initialize(this)
VertxTests.startTests(this)