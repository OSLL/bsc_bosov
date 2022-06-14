package com.github.neckbosov.bsc_bosov.dsl.program

open class ProgramScope<LanguageTag>() :
    ProgramInstruction<LanguageTag>() {
    val varNames = mutableSetOf<String>()
    val items = mutableListOf<ProgramItem<LanguageTag>>()
}