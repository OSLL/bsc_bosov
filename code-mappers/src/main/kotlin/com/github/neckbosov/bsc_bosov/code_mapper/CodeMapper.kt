package com.github.neckbosov.bsc_bosov.code_mapper

import com.github.neckbosov.bsc_bosov.dsl.program.Program
import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag

interface CodeMapper<LanguageTag : ProgramLanguageTag> {
    fun generateCode(program: Program<in LanguageTag>): String
}