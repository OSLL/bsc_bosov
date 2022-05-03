package com.github.neckbosov.bsc_bosov.dsl.program


class FunctionalCall<LanguageTag>(
    val functionalCallExpr: FunctionalCallExpr<LanguageTag>
) : ProgramInstruction<LanguageTag>()

