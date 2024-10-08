package com.messio.clsb.services

import com.messio.clsb.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.xml.parsers.SAXParserFactory

@Service
class InitService(
    val facade: Facade,
    @Value("classpath:init.xml") val initResource: Resource,
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

                            val mirror = Account(
                                bank = bank,
                                denomination = bank.denomination
                            )
                            facade.accountRepository.save(mirror)
                            bank.mirror = mirror
                            facade.bankRepository.save(bank)
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
                                        payInSchedule = Schedule.parse(attributes.getValue("pay-in-schedule"))
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

                        "account" -> {
                            currentBank?.let { bank ->
                                accountMap[attributes.getValue("name")] = facade.accountRepository.save(
                                    Account(
                                        bank = bank,
                                        denomination = attributes.getValue("name"),
                                    )
                                )
                            }
                        }

                        "transfer" -> {
                            accountMap[attributes.getValue("principal")]?.let { principal ->
                                accountMap[attributes.getValue("counterparty")]?.let { counterparty ->
                                    facade.transferRepository.save(
                                        Transfer(
                                            principal = principal,
                                            counterparty = counterparty,
                                            execution = LocalDateTime.parse(attributes.getValue("moment")),
                                            reference = attributes.getValue("reference"),
                                            amount = Position.parse(attributes.getValue("amount")),
                                        )
                                    )
                                }
                            }
                        }

                        "pay-in" -> {
                            currentBank?.let { bank ->
                                accountMap[attributes.getValue("principal")]?.let { principal ->
                                    facade.payInRepository.save(
                                        PayIn(
                                            principal = principal,
                                            bank = bank,
                                            reference = attributes.getValue("reference"),
                                            amount = Position.parse(attributes.getValue("amount")),
                                        )
                                    )
                                }
                            }
                        }

                        "trade" -> {
                            accountMap[attributes.getValue("principal")]?.let { principal ->
                                accountMap[attributes.getValue("counterparty")]?.let { counterparty ->
                                    facade.tradeRepository.save(
                                        Trade(
                                            principal = principal,
                                            counterparty = counterparty,
                                            settlement = LocalDate.parse(attributes.getValue("settlement")),
                                            reference = attributes.getValue("reference"),
                                            amount = Position.parse(attributes.getValue("amount")),
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
