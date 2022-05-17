package com.github.neckbosov.bsc_bosov.dsl.program

class VariableDefinition<LanguageTag>(
    val variable: Variable<LanguageTag>,
    val typeName: String? = null,
    val initialValue: ProgramExpression<LanguageTag>? = null
) : ProgramInstruction<LanguageTag>()