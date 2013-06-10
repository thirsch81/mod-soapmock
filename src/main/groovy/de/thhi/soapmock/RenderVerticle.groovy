package de.thhi.soapmock

import groovy.text.SimpleTemplateEngine

import org.vertx.groovy.core.eventbus.EventBus
import org.vertx.groovy.platform.Verticle
import org.vertx.java.core.logging.Logger

public class RenderVerticle extends Verticle {

	def start() {

		def templates = vertx.sharedData.getMap("templates")
		new File("templates").eachFile {
			templates[it.name - ".template"] = it.text
		}

		vertx.eventBus.registerHandler("render") { message ->

			def body = message.body
			container.logger.info("RenderVerticle: received ${body}")
			try {
				body.name
				body.binding
			} catch (Exception e) {
				def errorMsg = "${e.message}: Expected message format: [name: <name>, binding: <binding>]"
				container.logger.error(errorMsg)
				message.reply(error(errorMsg))
			}

			try {
				def start = System.currentTimeMillis()
				message.reply(render(body.name, body.binding))
				def time = System.currentTimeMillis() - start
				container.logger.info("RenderVerticle: rendered template ${body.name} in ${time}ms")
			} catch (Exception e) {
				container.logger.error(e.message)
				message.reply(error(e.message))
			}
		}
	}

	def error(message) {
		["status": "error", "message": message]
	}

	def render(name, binding) {
		def template = vertx.sharedData.getMap("templates")[name]
		["status": "ok", "${name}": new SimpleTemplateEngine().createTemplate(template).make(binding).toString()]
	}
}