package de.thhi.soapmock

import groovy.text.SimpleTemplateEngine

import org.vertx.groovy.core.eventbus.EventBus
import org.vertx.groovy.platform.Verticle

public class ExtractorVerticle extends Verticle {

	def start() {

		def now = { System.currentTimeMillis() }
		def r_time = now()
		def rules = rules()
		try {
			new File("rules").eachFile {
				container.logger.info("ExtractorVerticle: reading rule script ${it}")
				rules[it.name - ".groovy"] = it.text
			}
		} catch (Exception e) {
			container.logger.error("ExtractorVerticle: ${e.message}")
		}
		container.logger.info("ExtractorVerticle: read ${rules.size()} rule scripts in ${now() - r_time}ms")

		vertx.eventBus.registerHandler("extractor.extract") { message ->

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
				def start = now()
				def shell = prepareShell(body.source)
				def p_time = now() - start
				start = now()
				def template = dispatch(shell, rules)
				def d_time = now() - start
				start = now()
				message.reply(extract(shell, rules, template))
				def e_time = now() - start
				container.logger.info("ExtractorVerticle: parsed XML in ${p_time}ms, dispatched in ${d_time}ms and extracted binding in ${e_time}ms")
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

	def prepareShell(source) {
		def root = new XmlParser().parseText(source)
		def binding = new Binding()
		binding.setVariable("root", root)
		new GroovyShell(binding)
	}

	def dispatch(shell, rules) {
		shell.evaluate(rules["dispatch"])
		shell.context.getVariable("template")
	}

	def extract(shell, rules, template) {
		shell.context.variables.remove("template")
		shell.evaluate(rules[template])
		shell.context.variables.remove("root")
		["status": "ok", "binding": shell.context.variables]
	}

	def rules = {
		vertx.sharedData.getMap("extractRules")
	}
}