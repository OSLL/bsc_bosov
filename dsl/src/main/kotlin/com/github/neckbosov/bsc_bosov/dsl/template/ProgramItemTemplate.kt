package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramItem
import kotlin.random.Random

abstract class ProgramItemTemplate<LanguageTag> {
    abstract fun fillItem(random: Random, attributes: ProgramAttributes): ProgramItem<LanguageTag>
}