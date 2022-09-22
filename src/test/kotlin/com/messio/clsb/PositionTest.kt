package com.messio.clsb

import org.junit.jupiter.api.Test

class PositionTest {
    @Test
    fun testParse(){
        val p = Position.parse("{eur=1,usd=3}")
        println(p)
    }

    @Test
    fun testAdd(){
        val p1 = Position.parse("{eur=1,usd=3}")
        val p2 = Position.parse("{usd=5,jpy=2,eur=-1}")
        println(p1.add(p2))
        println(p2.add(p1))
        println(p1.subtract(p2))
        println(p2.subtract(p1))
    }
}