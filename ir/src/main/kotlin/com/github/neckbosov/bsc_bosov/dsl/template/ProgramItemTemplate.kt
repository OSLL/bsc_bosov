package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramItem
import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import kotlin.random.Random


interface ProgramItemTemplate<LanguageTag : ProgramLanguageTag> {
    fun fillItem(random: Random, attributes: ProgramAttributes): ProgramItem<LanguageTag>
}