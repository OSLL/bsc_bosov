package com.github.neckbosov.bsc_bosov.dsl.program

sealed class Constant<LanguageTag> : ProgramExpression<LanguageTag>()
class StringConstant<LanguageTag>(val value: String) : Constant<LanguageTag>()
class NumConstant<LanguageTag>(val value: Number) : Constant<LanguageTag>()

fun <LanguageTag, T> constant(value: T): Constant<LanguageTag> {
    return when (value) {
        is Number -> NumConstant(value)
        is String -> StringConstant(value)
        else -> error("This constant type is not supported yet")
    }
}