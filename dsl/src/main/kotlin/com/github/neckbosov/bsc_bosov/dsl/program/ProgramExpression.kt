package com.github.neckbosov.bsc_bosov.dsl.program

sealed class ProgramExpression<LanguageTag> : ProgramItem<LanguageTag>()

class FunctionalCallExpr<LanguageTag>(
    val functionName: String,
    val params: List<ProgramExpression<LanguageTag>>
) : ProgramExpression<LanguageTag>()

class BinOpExpr<LanguageTag>(
    val op: String,
    val lhs: ProgramExpression<LanguageTag>,
    val rhs: ProgramExpression<LanguageTag>
) : ProgramExpression<LanguageTag>()
