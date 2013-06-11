package de.thhi.soapmock

import groovy.text.SimpleTemplateEngine

import org.vertx.groovy.core.eventbus.EventBus
import org.vertx.groovy.platform.Verticle

public class ExtractorVerticle extends Verticle {

	def start() {

		def now = { System.currentTimeMillis() }
		
		vertx.eventBus.registerHandler("extract") { message ->

			def body = message.body
			container.logger.info("ExtractorVerticle: received ${body}")
			try {
				body.source
			} catch (Exception e) {
				def errorMsg = "${e.message}: Expected message format: [source: <source>]"
				container.logger.error(errorMsg)
				message.reply(error(errorMsg))
			}

			try {
				def e_time = now()
				message.reply(extract(body.source))
				container.logger.info("ExtractorVerticle: parsed source into binding in ${now() - e_time}ms")
			} catch (Exception e) {
				container.logger.error(e.message)
				message.reply(error(e.message))
			}
		}
		container.logger.info("ExtractorVerticle started")
	}

	def error(message) {
		["status": "error", "message": message]
	}

	def extract(source) {
		def root = new XmlParser().parseText(source)
		["status": "ok", "binding": ["content": root.text()]]
	}
}