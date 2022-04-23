package com.github.neckbosov.bsc_bosov.dsl

sealed class ProgramExpression<LanguageTag>

class ConstantExpr<LanguageTag>(value: String) : ProgramExpression<LanguageTag>()
