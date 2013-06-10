import static org.vertx.testtools.VertxAssert.*

import org.vertx.groovy.testtools.VertxTests
import de.thhi.soapmock.MockServerVerticle
import de.thhi.soapmock.RenderVerticle

// The test methods must being with "test"

def testHTTP() {

	container.deployVerticle("groovy:" + MockServerVerticle.class.name) { result ->
		assertNotNull(result)
		container.logger.info(result?.cause())
		container.logger.info(result.toString())
		assertTrue(result.succeeded())
	}

	//    vertx.createHttpClient().setPort(8181).getNow("/", { resp ->
	//      assertEquals(200, resp.statusCode)
	/*
	 If we get here, the test is complete
	 You must always call `testComplete()` at the end. Remember that testing is *asynchronous* so
	 we cannot assume the test is complete by the time the test method has finished executing like
	 in standard synchronous tests
	 */
	testComplete()
	//})

}

def testDeployMockServerVerticle() {
	container.deployVerticle("groovy:" + MockServerVerticle.class.name) { result ->
		container.logger.info("Error: " + result?.cause())
		assertTrue(result.succeeded())
		testComplete()
	}
}

def testDeployRenderVerticle() {
	container.deployWorkerVerticle("groovy:" + RenderVerticle.class.name) { result ->
		container.logger.info("Error: " + result?.cause())
		assertTrue(result.succeeded())
		testComplete()
	}
}

def testRenderVerticle() {
	container.deployWorkerVerticle("groovy:" + RenderVerticle.class.name) { result ->
		assertTrue(result.succeeded())
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

def testCompleteOnTimer() {
	vertx.setTimer(1000, { timerID ->
		assertNotNull(timerID)
		// This demonstrates how tests are asynchronous - the timer does not fire until 1 second later -
		// which is almost certainly after the test method has completed.
		testComplete()
	})
}

VertxTests.initialize(this)
VertxTests.startTests(this)