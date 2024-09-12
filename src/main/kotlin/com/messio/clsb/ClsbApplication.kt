package com.messio.clsb

import com.messio.clsb.services.InitService
import com.messio.clsb.services.RateService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import java.time.LocalDate

@EnableWebSocketMessageBroker
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
