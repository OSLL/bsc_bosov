package com.github.neckbosov.bsc_bosov.dsl

import kotlin.random.Random

typealias ProgramAttributes = Map<String, String>

class Program<LanguageTag>(seed: Long, attributes: ProgramAttributes) {
    private val random: Random = Random(seed)
    val scope = ProgramGlobalScope<LanguageTag>(random, attributes)
}