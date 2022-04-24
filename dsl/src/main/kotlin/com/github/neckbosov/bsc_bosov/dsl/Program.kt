package com.github.neckbosov.bsc_bosov.dsl

import kotlin.random.Random

typealias ProgramAttributes = Map<String, String>

class Program<LanguageTag>(
    seed: Long,
    attributes: ProgramAttributes,
    code: ProgramGlobalScope<LanguageTag>.() -> Unit = {}
) {
    private val random: Random = Random(seed)
    val scope: ProgramScope<LanguageTag>

    init {
        scope = ProgramGlobalScope<LanguageTag>(random, attributes).apply(code)
    }
}