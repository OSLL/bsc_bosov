package com.github.neckbosov.bsc_bosov.server

import java.math.BigDecimal
import java.math.RoundingMode

fun checkAnswer(studentAnswer: String, realAnswer: String): BigDecimal {
    val studentLines = studentAnswer.lines().map { it.trim() }
    val answerLines = realAnswer.lines().map { it.trim() }
    val count = answerLines.zip(studentLines).sumOf { if (it.first == it.second) 1L else 0L }
    return BigDecimal(count).times(100.toBigDecimal()).divide(count.toBigDecimal(), 2, RoundingMode.DOWN)
}