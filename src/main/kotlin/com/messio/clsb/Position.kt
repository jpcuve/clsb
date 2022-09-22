package com.messio.clsb

import java.math.BigDecimal

class Position: HashMap<String, BigDecimal>() {
    companion object {
        val ZERO = Position()

        fun parse(s: String) = ZERO
    }
}