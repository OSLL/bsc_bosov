package com.github.neckbosov.bsc_bosov.dsl

import kotlin.random.Random

open class ProgramScope<LanguageTag>(random: Random, attributes: ProgramAttributes) :
    ProgramItem<LanguageTag>(random, attributes) {
    val varNames = mutableSetOf<String>()
    val items = mutableListOf<ProgramItem<LanguageTag>>()
}