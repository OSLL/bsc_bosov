package com.github.neckbosov.bsc_bosov.dsl

sealed class ProgramExpression<LanguageTag>

class ConstantExpr<LanguageTag>(val value: String) : ProgramExpression<LanguageTag>()
class VariableExpr<LanguageTag>(val variable: Variable<LanguageTag>) : ProgramExpression<LanguageTag>()