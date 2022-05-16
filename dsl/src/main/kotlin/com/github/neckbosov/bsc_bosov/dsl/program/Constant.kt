package com.github.neckbosov.bsc_bosov.dsl.program

sealed class Constant<LanguageTag> : ProgramExpression<LanguageTag>()
class StringConstant<LanguageTag>(val value: String) : Constant<LanguageTag>()
class NumConstant<T : Number, LanguageTag>(val value: T) : Constant<LanguageTag>()