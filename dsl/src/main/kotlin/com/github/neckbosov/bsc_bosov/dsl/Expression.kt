package com.github.neckbosov.bsc_bosov.dsl

sealed class ProgramExpression<LanguageTag>

class ConstantExpr<LanguageTag>(val value: Constant) : ProgramExpression<LanguageTag>()

fun <LanguageTag, T> constantExpr(value: T): ConstantExpr<LanguageTag> {
    return ConstantExpr(constant(value))
}

class VariableExpr<LanguageTag>(val variable: Variable<LanguageTag>) : ProgramExpression<LanguageTag>()

fun <LanguageTag> Variable<LanguageTag>.toExpr() = VariableExpr(this)
fun <LanguageTag> varExpr(name: String) = VariableExpr(Variable<LanguageTag>(name))
class FunctionalCallExpr<LanguageTag>(
    val functionName: String,
    val params: List<ProgramExpression<LanguageTag>>
) : ProgramExpression<LanguageTag>()

fun <LanguageTag> funcCall(
    functionName: String,
    vararg params: ProgramExpression<LanguageTag>
) = FunctionalCallExpr(
    functionName,
    params.toList()
)

class BinOpExpr<LanguageTag>(
    val op: String,
    val lhs: ProgramExpression<LanguageTag>,
    val rhs: ProgramExpression<LanguageTag>
) : ProgramExpression<LanguageTag>()

infix fun <LanguageTag> ProgramExpression<LanguageTag>.opLess(rhs: ProgramExpression<LanguageTag>) = BinOpExpr(
    "<",
    this,
    rhs
)