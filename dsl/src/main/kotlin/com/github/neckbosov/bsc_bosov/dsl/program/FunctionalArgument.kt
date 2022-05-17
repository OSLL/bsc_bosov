package com.github.neckbosov.bsc_bosov.dsl.program

class FunctionalArgument<LanguageTag>(
    val variable: Variable<LanguageTag>,
    val typeName: String? = null,
    val initialValue: ProgramExpression<LanguageTag>? = null
) : ProgramItem<LanguageTag>()