package com.github.neckbosov.bsc_bosov.dsl

import com.github.neckbosov.bsc_bosov.dsl.features.GlobalInstructions
import kotlin.random.Random

class FunctionalCall<LanguageTag>(
    random: Random,
    attributes: ProgramAttributes,
    val functionName: String,
    val params: List<ProgramExpression<LanguageTag>>
) : ProgramItem<LanguageTag>(random, attributes)

fun <LanguageTag> ProgramLocalScope<LanguageTag>.funcCall(
    functionName: String,
    vararg params: ProgramExpression<LanguageTag>
) {
    this.items.add(FunctionalCall(this.random, this.attributes, functionName, params.toList()))
}

fun <LanguageTag : GlobalInstructions> ProgramGlobalScope<LanguageTag>.funcCall(
    functionName: String,
    vararg params: ProgramExpression<LanguageTag>
) {
    this.items.add(FunctionalCall(this.random, this.attributes, functionName, params.toList()))
}