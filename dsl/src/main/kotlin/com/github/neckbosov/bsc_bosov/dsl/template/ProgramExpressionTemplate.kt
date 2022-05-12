package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.program.BinOpExpr
import com.github.neckbosov.bsc_bosov.dsl.program.FunctionalCallExpr
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramExpression
import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
sealed class ProgramExpressionTemplate<LanguageTag : ProgramLanguageTag> : ProgramItemTemplate<LanguageTag> {
    abstract override fun fillItem(random: Random, attributes: ProgramAttributes): ProgramExpression<LanguageTag>
}


@Serializable
class BinOpExprTemplate<LanguageTag : ProgramLanguageTag>(
    val op: ProgramStringConstantTemplate<LanguageTag>,
    val lhs: ProgramExpressionTemplate<LanguageTag>,
    val rhs: ProgramExpressionTemplate<LanguageTag>
) : ProgramExpressionTemplate<LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): BinOpExpr<LanguageTag> {
        return BinOpExpr(
            op.fillItem(random, attributes).value,
            lhs.fillItem(random, attributes),
            rhs.fillItem(random, attributes)
        )
    }
}

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

infix fun <LanguageTag : ProgramLanguageTag> ProgramExpressionTemplate<LanguageTag>.opLt(rhs: ProgramExpressionTemplate<LanguageTag>) =
    binOp("<", this, rhs)

@Serializable
class FunctionalCallExprTemplate<LanguageTag : ProgramLanguageTag>(
    val functionName: ProgramVariableNameTemplate<LanguageTag>,
    val params: List<ProgramExpressionTemplate<LanguageTag>>
) : ProgramExpressionTemplate<LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): FunctionalCallExpr<LanguageTag> {
        return FunctionalCallExpr(
            functionName.fillItem(random, attributes).value,
            params.map { it.fillItem(random, attributes) }
        )
    }
}

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

