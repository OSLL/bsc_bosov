package com.github.neckbosov.bsc_bosov.dsl.program

sealed class Constant<LanguageTag> : ProgramExpression<LanguageTag>()
class StringConstant<LanguageTag>(val value: String) : Constant<LanguageTag>()
class NumConstant<LanguageTag>(val value: Number) : Constant<LanguageTag>()