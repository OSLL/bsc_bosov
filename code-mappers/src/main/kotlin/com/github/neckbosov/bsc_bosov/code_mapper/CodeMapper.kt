package com.github.neckbosov.bsc_bosov.code_mapper

import com.github.neckbosov.bsc_bosov.dsl.Program

interface CodeMapper<LanguageTag> {
    fun generateCode(program: Program<LanguageTag>): String
}