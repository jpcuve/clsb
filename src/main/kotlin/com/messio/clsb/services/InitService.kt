package com.messio.clsb.services

import com.messio.clsb.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.LocalTime
import javax.xml.parsers.SAXParserFactory

@Service
class InitService(
    val facade: Facade,
    @Value("classpath:init.xml") val initResource: Resource,
    @Value("\${app.mirror-name}") val mirrorName: String,
) {
    fun initialize(){
        if (facade.bankRepository.count() == 0L) {
            val parser = SAXParserFactory.newInstance().newSAXParser()
            parser.parse(initResource.inputStream, object : DefaultHandler() {
                var currentBank: Bank? = null
                var currentCurrency: Currency? = null
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
                                val currency = facade.currencyRepository.save(
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
                                currencyMap[attributes.getValue("iso")] = currency
                                currentCurrency = currency
                            }
                        }

                        "real-time-gross-settlement-period" -> {
                            currentCurrency?.realTimeGrossSettlementPeriods?.add(
                                RealTimeGrossSettlementPeriod(
                                init = LocalTime.parse(attributes.getValue("init")),
                                done = LocalTime.parse(attributes.getValue("done")),
                            )
                            )
                        }

                        "pay-in-schedule" -> {
                            currentCurrency?.payInSchedules?.put(LocalTime.parse(attributes.getValue("scheduled")), attributes.getValue("proportion").toInt())
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
                        "currency" -> {
                            currentCurrency?.let { facade.currencyRepository.save(it) }
                            currentCurrency = null
                        }
                        else -> {}
                    }
                }
            })
        }

    }
}