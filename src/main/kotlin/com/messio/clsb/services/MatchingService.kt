package com.messio.clsb.services

import com.messio.clsb.Facade
import com.messio.clsb.Settlement
import com.messio.clsb.Trade
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MatchingService(
    private val facade: Facade,
) {
    fun matchTrades(moment: LocalDateTime){
        var match = facade.tradeRepository.findMaxMatch() ?: 0L
        facade.tradeRepository.findUnmatchedBySettlement(moment.toLocalDate())
            .map { Pair(it[0] as Trade, it[1] as Trade) }
            .filter { it.first.amount.add(it.second.amount).isZero() }
            .forEach {
                ++match
                it.first.executed = moment
                it.first.match = match
                it.second.executed = moment
                it.second.match = match
                facade.tradeRepository.saveAll(it.toList())
                facade.settlementRepository.save(
                    Settlement(
                        principal = it.first.principal,
                        counterparty = it.first.counterparty,
                        amount = it.first.amount,
                        match = match,
                    )
                )
            }
    }
}
