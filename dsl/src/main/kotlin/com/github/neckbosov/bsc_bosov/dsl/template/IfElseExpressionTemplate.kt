package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.features.GlobalInstructions
import com.github.neckbosov.bsc_bosov.dsl.program.IfElseExpression
import com.github.neckbosov.bsc_bosov.dsl.program.IfExpression
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
class IfExpressionTemplate<LanguageTag : ProgramLanguageTag>(
    val cond: ProgramExpressionTemplate<LanguageTag>,
    val block: ProgramLocalScopeTemplate<LanguageTag>
) : ProgramInstructionTemplate<LanguageTag> {
    override fun fillItem(random: Random, attributes: ProgramAttributes): IfExpression<LanguageTag> {
        return IfExpression(
            cond.fillItem(random, attributes),
            block.fillItem(random, attributes)
        )
    }
}

@Serializable
class IfElseExpressionTemplate<LanguageTag : ProgramLanguageTag>(
    val cond: ProgramExpressionTemplate<LanguageTag>,
    val block: ProgramLocalScopeTemplate<LanguageTag>
) : ProgramInstructionTemplate<LanguageTag> {
    val elifBlocks = mutableListOf<IfExpressionTemplate<LanguageTag>>()
    var elseBlock: ProgramLocalScopeTemplate<LanguageTag>? = null
    override fun fillItem(random: Random, attributes: ProgramAttributes): IfElseExpression<LanguageTag> {
        return IfElseExpression(
            cond.fillItem(random, attributes),
            block.fillItem(random, attributes)
        ).apply {
            this.elifBlocks.addAll(this@IfElseExpressionTemplate.elifBlocks.map { it.fillItem(random, attributes) })
            this.elseBlock = this@IfElseExpressionTemplate.elseBlock?.fillItem(random, attributes)
        }
    }
}

fun <LanguageTag : ProgramLanguageTag> IfElseExpressionTemplate<LanguageTag>.addElif(
    cond: ProgramExpressionTemplate<LanguageTag>,
    blockInit: ProgramLocalScopeTemplate<LanguageTag>.() -> Unit
): IfElseExpressionTemplate<LanguageTag> {
    val block = ProgramLocalScopeTemplate<LanguageTag>().apply(blockInit)
    val elifExpr = IfExpressionTemplate(cond, block)
    this.elifBlocks.add(elifExpr)
    return this
}

fun <LanguageTag : ProgramLanguageTag> IfElseExpressionTemplate<LanguageTag>.addElse(
    blockInit: ProgramLocalScopeTemplate<LanguageTag>.() -> Unit
) {
    this.elseBlock = ProgramLocalScopeTemplate<LanguageTag>().apply(blockInit)
}

fun <LanguageTag : ProgramLanguageTag> ProgramLocalScopeTemplate<LanguageTag>.addIfExpr(
    cond: ProgramExpressionTemplate<LanguageTag>,
    blockInit: ProgramLocalScopeTemplate<LanguageTag>.() -> Unit
) {
    val block = ProgramLocalScopeTemplate<LanguageTag>().apply(blockInit)
    val ifExpr = IfExpressionTemplate(cond, block)
    this.items.add(ifExpr)
}

fun <LanguageTag : ProgramLanguageTag> ProgramLocalScopeTemplate<LanguageTag>.addIfElseExpr(
    cond: ProgramExpressionTemplate<LanguageTag>,
    blockInit: ProgramLocalScopeTemplate<LanguageTag>.() -> Unit
): IfElseExpressionTemplate<LanguageTag> {
    val block = ProgramLocalScopeTemplate<LanguageTag>().apply(blockInit)
    val ifElseExpr = IfElseExpressionTemplate(cond, block)
    this.items.add(ifElseExpr)
    return ifElseExpr
}

fun <LanguageTag> ProgramGlobalScopeTemplate<LanguageTag>.addIfExpr(
    cond: ProgramExpressionTemplate<LanguageTag>,
    blockInit: ProgramLocalScopeTemplate<LanguageTag>.() -> Unit
) where LanguageTag : GlobalInstructions, LanguageTag : ProgramLanguageTag {
    val block = ProgramLocalScopeTemplate<LanguageTag>().apply(blockInit)
    val ifExpr = IfExpressionTemplate(cond, block)
    this.items.add(ifExpr)
}

fun <LanguageTag> ProgramGlobalScopeTemplate<LanguageTag>.addIfElseExpr(
    cond: ProgramExpressionTemplate<LanguageTag>,
    blockInit: ProgramLocalScopeTemplate<LanguageTag>.() -> Unit
): IfElseExpressionTemplate<LanguageTag> where LanguageTag : GlobalInstructions, LanguageTag : ProgramLanguageTag {
    val block = ProgramLocalScopeTemplate<LanguageTag>().apply(blockInit)
    val ifElseExpr = IfElseExpressionTemplate(cond, block)
    this.items.add(ifElseExpr)
    return ifElseExpr
}