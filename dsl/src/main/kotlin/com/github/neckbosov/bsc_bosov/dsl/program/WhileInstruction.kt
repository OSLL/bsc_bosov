package com.github.neckbosov.bsc_bosov.dsl.program

class WhileInstruction<LanguageTag>(
    val cond: ProgramExpression<LanguageTag>,
    val scope: ProgramLocalScope<LanguageTag>
) : ProgramInstruction<LanguageTag>()