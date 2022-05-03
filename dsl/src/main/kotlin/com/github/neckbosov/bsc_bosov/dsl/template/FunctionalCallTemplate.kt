package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.features.GlobalInstructions
import com.github.neckbosov.bsc_bosov.dsl.program.FunctionalCall
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
class FunctionalCallTemplate<LanguageTag>(val funcCallExpr: FunctionalCallExprTemplate<LanguageTag>) :
    ProgramInstructionTemplate<LanguageTag> {
    override fun fillItem(random: Random, attributes: ProgramAttributes): FunctionalCall<LanguageTag> {
        return FunctionalCall(funcCallExpr.fillItem(random, attributes))
    }
}

fun <LanguageTag> ProgramLocalScopeTemplate<LanguageTag>.addFuncCall(
    functionName: String,
    vararg params: ProgramExpressionTemplate<LanguageTag>
) {
    val expr = funcCall(functionName, *params)
    this.items.add(FunctionalCallTemplate(expr))
}

fun <LanguageTag> ProgramLocalScopeTemplate<LanguageTag>.addFuncCall(
    functionName: ProgramVariableNameTemplate<LanguageTag>,
    vararg params: ProgramExpressionTemplate<LanguageTag>
) {
    val expr = funcCall(functionName, *params)
    this.items.add(FunctionalCallTemplate(expr))
}

fun <LanguageTag : GlobalInstructions> ProgramGlobalScopeTemplate<LanguageTag>.addFuncCall(
    functionName: String,
    vararg params: ProgramExpressionTemplate<LanguageTag>
) {
    val expr = funcCall(functionName, *params)
    this.items.add(FunctionalCallTemplate(expr))
}

fun <LanguageTag : GlobalInstructions> ProgramGlobalScopeTemplate<LanguageTag>.addFuncCall(
    functionName: ProgramVariableNameTemplate<LanguageTag>,
    vararg params: ProgramExpressionTemplate<LanguageTag>
) {
    val expr = funcCall(functionName, *params)
    this.items.add(FunctionalCallTemplate(expr))
}