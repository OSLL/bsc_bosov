package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramInstruction
import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import kotlin.random.Random


interface ProgramInstructionTemplate<LanguageTag : ProgramLanguageTag> : ProgramItemTemplate<LanguageTag> {
    override fun fillItem(random: Random, attributes: ProgramAttributes): ProgramInstruction<LanguageTag>
}