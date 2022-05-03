package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramInstruction
import kotlin.random.Random

abstract class ProgramInstructionTemplate<LanguageTag> : ProgramItemTemplate<LanguageTag>() {
    abstract override fun fillItem(random: Random, attributes: ProgramAttributes): ProgramInstruction<LanguageTag>
}