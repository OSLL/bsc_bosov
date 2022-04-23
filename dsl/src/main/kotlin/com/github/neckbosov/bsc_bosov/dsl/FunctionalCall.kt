package com.github.neckbosov.bsc_bosov.dsl

import kotlin.random.Random

class FunctionalCall<LanguageTag>(
    random: Random,
    attributes: ProgramAttributes,
    val functionName: String,
    vararg val params: List<ProgramExpression<LanguageTag>>
) : ProgramItem<LanguageTag>(random, attributes)

fun <LanguageTag> ProgramScope<LanguageTag>.funcCall(
    functionName: String,
    vararg params: ProgramExpression<LanguageTag>
) {
    this.items.add(FunctionalCall(this.random, this.attributes, functionName, params.toList()))
}