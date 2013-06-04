/*
 * Example Groovy integration test that deploys the module that this project builds.
 *
 * Quite often in integration tests you want to deploy the same module for all tests and you don't want tests
 * to start before the module has been deployed.
 *
 * This test demonstrates how to do that.
 */

import static org.vertx.testtools.VertxAssert.*

// And import static the VertxTests script
import org.vertx.groovy.testtools.VertxTests;

// The test methods must being with "test"

def testSomething() {
	container.logger.info("testSomething()")
	container.logger.info("vertx is ${vertx.getClass().getName()}")
	testComplete()
}

def testSomethingElse() {
	testComplete()
}

// Make sure you initialize
VertxTests.initialize(this)

// The script is execute for each test, so this will deploy the module for each one
// Deploy the module - the System property `vertx.modulename` will contain the name of the module so you
// don't have to hardcode it in your tests
container.deployModule("vertx.modulename", { asyncResult ->
	// Deployment is asynchronous and this this handler will be called when it's complete (or failed)
	container.logger.info(asyncResult)
	assertTrue(asyncResult.succeeded())
	assertNotNull("deploymentID should not be null", asyncResult.result())
	// If deployed correctly then start the tests!
	VertxTests.startTests(this)
})



