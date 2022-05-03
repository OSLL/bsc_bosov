package com.github.neckbosov.bsc_bosov.dsl.program

class Assignment<LanguageTag>(
    val lhs: Variable<LanguageTag>,
    val rhs: ProgramExpression<LanguageTag>
) : ProgramInstruction<LanguageTag>() {
}
