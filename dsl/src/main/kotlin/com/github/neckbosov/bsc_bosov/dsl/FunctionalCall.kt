package com.github.neckbosov.bsc_bosov.dsl

import com.github.neckbosov.bsc_bosov.dsl.features.GlobalInstructions
import kotlin.random.Random


class FunctionalCall<LanguageTag>(
    random: Random,
    attributes: ProgramAttributes,
    val functionalCallExpr: FunctionalCallExpr<LanguageTag>
) : ProgramItem<LanguageTag>(random, attributes)

fun <LanguageTag> ProgramLocalScope<LanguageTag>.addFuncCall(
    functionName: String,
    vararg params: ProgramExpression<LanguageTag>
) {
    val expr = FunctionalCallExpr(functionName, params.toList())
    this.items.add(FunctionalCall(this.random, this.attributes, expr))
}

fun <LanguageTag : GlobalInstructions> ProgramGlobalScope<LanguageTag>.addFuncCall(
    functionName: String,
    vararg params: ProgramExpression<LanguageTag>
) {
    val expr = FunctionalCallExpr(functionName, params.toList())
    this.items.add(FunctionalCall(this.random, this.attributes, expr))
}