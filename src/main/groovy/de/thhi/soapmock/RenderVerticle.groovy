package de.thhi.soapmock

import groovy.text.SimpleTemplateEngine

import org.vertx.groovy.core.eventbus.EventBus
import org.vertx.groovy.platform.Verticle

public class RenderVerticle extends Verticle {

	def start() {

		def now = { System.currentTimeMillis() }
		def t_time = now()
		def templates = vertx.sharedData.getMap("rendererTemplates")
		try {
			new File("templates").eachFile {
				container.logger.info("RenderVerticle: reading template ${it}")
				templates[it.name - ".template"] = it.text
			}
		} catch (Exception e) {
			container.logger.error("RenderVerticle: ${e.message}")
		}
		container.logger.info("RenderVerticle: read ${templates.size()} templates in ${now() - t_time}ms")

		vertx.eventBus.registerHandler("renderer.render") { message ->

			def body = message.body
			container.logger.info("RenderVerticle: received ${body}")
			try {
				assert body.name
				assert body.binding
			} catch (Exception e) {
				def errorMsg = "${e.message}: Expected message format: [name: <name>, binding: <binding>]"
				container.logger.error(errorMsg)
				message.reply(error(errorMsg))
			}

			try {
				def r_time = now()
				message.reply(render(body.name, body.binding))
				container.logger.info("RenderVerticle: rendered template ${body.name} in ${now() - r_time}ms")
			} catch (Exception e) {
				container.logger.error(e.message)
				message.reply(error(e.message))
			}
		}
		container.logger.info("RenderVerticle started")
	}

	def error(message) {
		["status": "error", "message": message]
	}

	def render(name, binding) {
		def template = vertx.sharedData.getMap("rendererTemplates")[name]
		["status": "ok", "${name}": new SimpleTemplateEngine().createTemplate(template).make(binding).toString()]
	}
}