package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramScope
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
sealed class ProgramScopeTemplate<LanguageTag> : ProgramInstructionTemplate<LanguageTag> {
    val items = mutableListOf<ProgramInstructionTemplate<LanguageTag>>()
    abstract override fun fillItem(random: Random, attributes: ProgramAttributes): ProgramScope<LanguageTag>
}