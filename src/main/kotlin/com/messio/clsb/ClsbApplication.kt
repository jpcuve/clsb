package com.messio.clsb

import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.io.Resource
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.LocalTime
import javax.xml.parsers.SAXParserFactory

@SpringBootApplication
class ClsbApplication(
	val facade: Facade,
	val rateService: RateService,
	@Value("classpath:init.xml") initResource: Resource,
	@Value("\${app.mirror-name}") val mirrorName: String,
) {
	init {
		if (facade.bankRepository.count() == 0L) {
			val parser = SAXParserFactory.newInstance().newSAXParser()
			parser.parse(initResource.inputStream, object : DefaultHandler() {
				var currentBank: Bank? = null
				var currencyMap = mutableMapOf<String, Currency>()
				var accountMap = mutableMapOf<String, Account>()

				override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
					when (qName) {
						"bank" -> {
							val bank = facade.bankRepository.save(
								Bank(
									denomination = attributes.getValue("name"),
									baseIso = attributes.getValue("base-iso"),
								)
							)
							facade.accountRepository.save(
								Account(
									bank = bank,
									denomination = mirrorName
								)
							)
							currentBank = bank
						}

						"currency" -> {
							currentBank?.let { bank ->
								currencyMap[attributes.getValue("iso")] = facade.currencyRepository.save(
									Currency(
										bank = bank,
										currencyGroup = CurrencyGroup.valueOf(attributes.getValue("group")),
										iso = attributes.getValue("iso"),
										opening = LocalTime.parse(attributes.getValue("opening")),
										fundingCompletionTarget = LocalTime.parse(attributes.getValue("funding-completion-target")),
										closing = LocalTime.parse(attributes.getValue("closing")),
										close = LocalTime.parse(attributes.getValue("close")),
										volatilityMargin = BigDecimal(attributes.getValue("volatility-margin")),
										baseRate = BigDecimal(attributes.getValue("base-rate")),
										scale = attributes.getValue("scale").toInt(),
									)
								)
							}
						}

						"account" -> {
							currentBank?.let { bank ->
								accountMap[attributes.getValue("name")] = facade.accountRepository.save(
									Account(
										bank = bank,
										denomination = attributes.getValue("name"),
										shortLimit = Position.parse(attributes.getValue("short-limit"))
									)
								)
							}
						}
						"instruction" -> {
							accountMap[attributes.getValue("db")]?.let { db ->
								accountMap[attributes.getValue("cr")]?.let { cr ->
									facade.instructionRepository.save(
										Instruction(
											db = db,
											cr = cr,
											execution = LocalDateTime.parse(attributes.getValue("moment")),
											type = InstructionType.valueOf(attributes.getValue("type")),
											reference = attributes.getValue("reference"),
											amount = Position.parse(attributes.getValue("amount")) ?: Position.ZERO,
										)
									)
								}
							}
						}
						else -> {
						}
					}
				}

				override fun endElement(uri: String, localName: String, qName: String) {
					when (qName) {
						"bank" -> currentBank = null
						else -> {}
					}
				}
			})
		}
		println("Hello")
		facade.bankRepository.findTopByDenomination("CLS")?.let { bank ->
			val position = Position.parse("{EUR=2,USD=1,JPY=100}")
			println(rateService.getPositionValue(bank, position))
			println(rateService.getPositionValue(bank, position, true))
		}
	}
}

fun HttpServletRequest.getBearerToken(): String? {
	getHeader("Authorization")?.let { authorization ->
		if (authorization.lowercase().startsWith("bearer ")) {
			return authorization.substring(7).trim()
		}
	}
	return null
}


fun main(args: Array<String>) {
	runApplication<ClsbApplication>(*args)
}
