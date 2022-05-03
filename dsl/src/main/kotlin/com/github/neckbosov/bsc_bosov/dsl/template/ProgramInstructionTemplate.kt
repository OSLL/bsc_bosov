package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramInstruction
import kotlin.random.Random


interface ProgramInstructionTemplate<LanguageTag> : ProgramItemTemplate<LanguageTag> {
    override fun fillItem(random: Random, attributes: ProgramAttributes): ProgramInstruction<LanguageTag>
}