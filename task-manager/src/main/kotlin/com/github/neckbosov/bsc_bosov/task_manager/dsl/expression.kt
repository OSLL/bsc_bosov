@file:Suppress("unused")

package com.github.neckbosov.bsc_bosov.task_manager.dsl

import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import com.github.neckbosov.bsc_bosov.dsl.template.*

fun <LanguageTag : ProgramLanguageTag> binOp(
    op: String,
    lhs: ProgramExpressionTemplate<LanguageTag>,
    rhs: ProgramExpressionTemplate<LanguageTag>
): BinOpExprTemplate<LanguageTag> = BinOpExprTemplate(StringConstantTemplate(op), lhs, rhs)

fun <LanguageTag : ProgramLanguageTag> binOp(
    op: ProgramStringConstantTemplate<LanguageTag>,
    lhs: ProgramExpressionTemplate<LanguageTag>,
    rhs: ProgramExpressionTemplate<LanguageTag>
): BinOpExprTemplate<LanguageTag> = BinOpExprTemplate(op, lhs, rhs)


infix fun <LanguageTag : ProgramLanguageTag> ProgramExpressionTemplate<LanguageTag>.lt(rhs: ProgramExpressionTemplate<LanguageTag>) =
    binOp("<", this, rhs)

operator fun <LanguageTag : ProgramLanguageTag> ProgramExpressionTemplate<LanguageTag>.div(rhs: ProgramExpressionTemplate<LanguageTag>) =
    binOp("/", this, rhs)

@Suppress("unused")
fun <LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.funcCall(
    functionName: String,
    vararg params: ProgramExpressionTemplate<LanguageTag>
) = FunctionalCallExprTemplate(
    ConstantProgramVariableNameTemplate(functionName),
    params.toList()
)

@Suppress("unused")
fun <LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.funcCall(
    functionName: ProgramVariableNameTemplate<LanguageTag>,
    vararg params: ProgramExpressionTemplate<LanguageTag>
) = FunctionalCallExprTemplate(
    functionName,
    params.toList()
)

