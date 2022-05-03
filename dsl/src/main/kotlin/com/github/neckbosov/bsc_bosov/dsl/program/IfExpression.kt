package com.github.neckbosov.bsc_bosov.dsl.program

class IfExpression<LanguageTag>(
    val cond: ProgramExpression<LanguageTag>,
    val block: ProgramLocalScope<LanguageTag>
) : ProgramInstruction<LanguageTag>()

class IfElseExpression<LanguageTag>(
    val cond: ProgramExpression<LanguageTag>,
    val block: ProgramLocalScope<LanguageTag>
) : ProgramInstruction<LanguageTag>() {
    val elifBlocks = mutableListOf<IfExpression<LanguageTag>>()
    var elseBlock: ProgramLocalScope<LanguageTag>? = null
}