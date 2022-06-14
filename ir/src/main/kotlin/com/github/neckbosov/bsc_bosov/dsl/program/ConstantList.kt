package com.github.neckbosov.bsc_bosov.dsl.program

import com.github.neckbosov.bsc_bosov.dsl.template.NumType

sealed class ConstantList<LanguageTag> : ProgramExpression<LanguageTag>()
class NumConstantList<T : Number, LanguageTag>(val numType: NumType, val numbers: List<T>) : ConstantList<LanguageTag>()
class StringConstantList<LanguageTag>(val strings: List<String>) : ConstantList<LanguageTag>()