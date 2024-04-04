package com.messio.clsb

import com.messio.clsb.services.InitService
import com.messio.clsb.services.RateService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.LocalDate

@SpringBootApplication
class ClsbApplication(
	val initService: InitService,
	val rateService: RateService,
	val facade: Facade,
) {
	init {
		initService.initialize()
		println("Hello")
		facade.bankRepository.findTopByDenomination("CLS")?.let { bank ->
			val position = Position.parse("{EUR=2,USD=1,JPY=100}")
			println(rateService.getPositionValue(bank, position))
			println(rateService.getPositionValue(bank, position, true))
		}
		val settlementQueue = facade.tradeRepository.findMatchesByDay(LocalDate.of(1970, 1, 1)).map { Pair(it[0] as Long, it[1] as Long) }
		println("Settlement queue: ${settlementQueue.joinToString(",")}")
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
