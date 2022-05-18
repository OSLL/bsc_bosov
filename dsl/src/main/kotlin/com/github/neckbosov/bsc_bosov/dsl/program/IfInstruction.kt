package com.github.neckbosov.bsc_bosov.dsl.program

class IfInstruction<LanguageTag>(
    val cond: ProgramExpression<LanguageTag>,
    val block: ProgramLocalScope<LanguageTag>
) : ProgramInstruction<LanguageTag>()

class IfElseInstruction<LanguageTag>(
    val cond: ProgramExpression<LanguageTag>,
    val block: ProgramLocalScope<LanguageTag>
) : ProgramInstruction<LanguageTag>() {
    val elifBlocks = mutableListOf<IfInstruction<LanguageTag>>()
    var elseBlock: ProgramLocalScope<LanguageTag>? = null
}