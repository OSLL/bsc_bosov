package com.github.neckbosov.bsc_bosov.dsl.program

sealed class ConstantList<LanguageTag> : ProgramExpression<LanguageTag>()
class NumConstantList<T : Number, LanguageTag>(val numbers: List<T>) : ConstantList<LanguageTag>()
class StringConstantList<LanguageTag>(val strings: List<String>) : ConstantList<LanguageTag>()