package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramScope
import kotlin.random.Random

abstract class ProgramScopeTemplate<LanguageTag> : ProgramInstructionTemplate<LanguageTag>() {
    val items = mutableListOf<ProgramInstructionTemplate<LanguageTag>>()
    abstract override fun fillItem(random: Random, attributes: ProgramAttributes): ProgramScope<LanguageTag>
}