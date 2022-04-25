package com.github.neckbosov.bsc_bosov.dsl

import kotlin.random.Random

abstract class ProgramItem<LanguageTag>(val random: Random, val attributes: ProgramAttributes) {
    fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9') + ",.[]<>/?()*&^%".toCharArray().toList()
        return (1..length)
            .map { allowedChars.random(this.random) }
            .joinToString("")
    }

    fun getRandomInt(from: Int, until: Int): Int = this.random.nextInt(from, until)
}