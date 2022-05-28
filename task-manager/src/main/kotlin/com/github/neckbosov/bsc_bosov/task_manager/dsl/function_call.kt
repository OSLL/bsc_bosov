@file:Suppress("unused")

package com.github.neckbosov.bsc_bosov.task_manager.dsl

import com.github.neckbosov.bsc_bosov.dsl.features.GlobalInstructions
import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import com.github.neckbosov.bsc_bosov.dsl.template.*


fun <LanguageTag : ProgramLanguageTag> ProgramLocalScopeTemplate<LanguageTag>.addFuncCall(
    functionName: String,
    vararg params: ProgramExpressionTemplate<LanguageTag>
) {
    val expr = funcCall(functionName, *params)
    this.items.add(FunctionalCallTemplate(expr))
}

fun <LanguageTag : ProgramLanguageTag> ProgramLocalScopeTemplate<LanguageTag>.addFuncCall(
    functionName: ProgramVariableNameTemplate<LanguageTag>,
    vararg params: ProgramExpressionTemplate<LanguageTag>
) {
    val expr = funcCall(functionName, *params)
    this.items.add(FunctionalCallTemplate(expr))
}

fun <LanguageTag> ProgramGlobalScopeTemplate<LanguageTag>.addFuncCall(
    functionName: String,
    vararg params: ProgramExpressionTemplate<LanguageTag>
) where LanguageTag : GlobalInstructions, LanguageTag : ProgramLanguageTag {
    val expr = funcCall(functionName, *params)
    this.items.add(FunctionalCallTemplate(expr))
}

fun <LanguageTag> ProgramGlobalScopeTemplate<LanguageTag>.addFuncCall(
    functionName: ProgramVariableNameTemplate<LanguageTag>,
    vararg params: ProgramExpressionTemplate<LanguageTag>
) where LanguageTag : GlobalInstructions, LanguageTag : ProgramLanguageTag {
    val expr = funcCall(functionName, *params)
    this.items.add(FunctionalCallTemplate(expr))
}