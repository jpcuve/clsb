package com.messio.clsb

import java.time.LocalTime

class Schedule: HashMap<LocalTime, Int> {
    constructor(): super()

    constructor(map: Map<LocalTime, Int>) : super(map)

    constructor(vararg amounts: Map.Entry<LocalTime, Int>){
        amounts.forEach { put(it.key, it.value) }
    }

    constructor(vararg amounts: Pair<LocalTime, Int>) {
        amounts.forEach { put(it.first, it.second) }
    }

    companion object {
        val EMPTY = Schedule()

        fun parse(s: String): Schedule {
            val schedule = Schedule()
            val trim = s.trim()
            if (trim.startsWith("{") && trim.endsWith("}")){
                trim.substring(1, trim.length - 1).split(",")
                    .filter { it.contains("=") }
                    .map { Pair(LocalTime.parse(it.substring(0, it.indexOf("=")).trim().uppercase()), it.substring(it.indexOf("=") + 1).toInt())}
                    .filter { it.second > 0 }
                    .forEach { schedule[it.first] = it.second }
            }
            return schedule
        }
    }
}