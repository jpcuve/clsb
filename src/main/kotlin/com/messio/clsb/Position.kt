package com.messio.clsb

import java.math.BigDecimal

class Position: HashMap<String, BigDecimal>() {

    fun putLeg(iso: String, amount: BigDecimal): Position {
        this[iso] = amount
        return this
    }

    fun add(that: Position) = entries.fold(that.copy()){ p, e -> p.putLeg(e.key, e.value.add(p.getOrDefault(e.key, BigDecimal.ZERO)))}.normalize()

    fun subtract(that: Position) = add(that.negate())

    fun negate() = entries.fold(Position()){ p, e -> p.putLeg(e.key, e.value.negate())}

    fun isZero() = normalize().isEmpty()

    fun normalize() = entries.filter { it.value.signum() != 0 }.fold(Position()){ p, e -> p.putLeg(e.key, e.value) }

    fun copy() = entries.fold(Position()){ p, e -> p.putLeg(e.key, e.value)}

    override fun equals(other: Any?): Boolean = (other as Position).subtract(this).isZero()

    companion object {
        val ZERO = Position()

        fun parse(s: String): Position {
            val position = Position()
            val trim = s.trim()
            if (trim.startsWith("{") && trim.endsWith("}")){
                trim.substring(1, trim.length - 1).split(",")
                    .filter { it.contains("=") }
                    .map { Pair(it.substring(0, it.indexOf("=")).trim().uppercase(), it.substring(it.indexOf("=") + 1))}
                    .filter { it.first.length == 3 &&  it.second.toFloatOrNull() != null }
                    .forEach { position[it.first] = BigDecimal(it.second) }
            }
            return position
        }
    }
}