package com.messio.clsb

import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class RateService(
    val facade: Facade,
) {
    fun getRate(bank: Bank, iso: String, quotedInIso: String) =
        facade.currencyRepository.findTopByBankAndIso(bank, iso)?.let { currency ->
            facade.currencyRepository.findTopByBankAndIso(bank, quotedInIso)?.let { quotedInCurrency ->
                // should use volatility margin to randomize this rate
                currency.baseRate.divide(quotedInCurrency.baseRate, currency.scale, RoundingMode.UP)
            } ?: throw EntityNotFoundException()
        } ?: throw EntityNotFoundException()

    fun getPositionValue(bank: Bank, position: Position, quotedInIso: String, withVolatilityMargin: Boolean = false) =
        position.entries.sumOf {
            facade.currencyRepository.findTopByBankAndIso(bank, it.key)?.let { currency ->
                val factor = if (withVolatilityMargin) (if (it.value.signum() < 0) currency.volatilityMargin else -currency.volatilityMargin) else BigDecimal.ZERO
                it.value.multiply(BigDecimal.ONE.add(factor)).multiply(getRate(bank, it.key, quotedInIso))
            } ?: throw EntityNotFoundException()
        }

    fun getPositionValue(bank: Bank, position: Position, withVolatilityMargin: Boolean = false) =
        getPositionValue(bank, position, bank.baseIso, withVolatilityMargin)
}