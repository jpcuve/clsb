package com.messio.clsb

import java.util.*

class Balance: TreeMap<String, Position>() {

    fun transfer(principal: Account, counterparty: Account, amount: Position){
        this[principal.denomination] = (this[principal.denomination] ?: Position.ZERO).subtract(amount)
        this[counterparty.denomination] = (this[counterparty.denomination] ?: Position.ZERO).add(amount)
    }

    fun isProvisioned(principal: Account, amount: Position): Boolean {
        return (this[principal.denomination] ?: Position.ZERO).subtract(amount).isLong()
    }
}