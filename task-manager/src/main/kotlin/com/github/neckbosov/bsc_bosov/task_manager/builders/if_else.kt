@file:Suppress("unused")

package com.github.neckbosov.bsc_bosov.task_manager.builders

import com.github.neckbosov.bsc_bosov.dsl.features.GlobalInstructions
import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import com.github.neckbosov.bsc_bosov.dsl.template.*

fun <LanguageTag : ProgramLanguageTag> IfElseInstructionTemplate<LanguageTag>.elif(
    cond: ProgramExpressionTemplate<LanguageTag>,
    blockInit: ProgramLocalScopeTemplate<LanguageTag>.() -> Unit
): IfElseInstructionTemplate<LanguageTag> {
    val block = ProgramLocalScopeTemplate<LanguageTag>().apply(blockInit)
    val elifExpr = IfInstructionTemplate(cond, block)
    this.elifBlocks.add(elifExpr)
    return this
}

fun <LanguageTag : ProgramLanguageTag> IfElseInstructionTemplate<LanguageTag>.`else`(
    blockInit: ProgramLocalScopeTemplate<LanguageTag>.() -> Unit
) {
    this.elseBlock = ProgramLocalScopeTemplate<LanguageTag>().apply(blockInit)
}

fun <LanguageTag : ProgramLanguageTag> ProgramLocalScopeTemplate<LanguageTag>.addIfInstr(
    cond: ProgramExpressionTemplate<LanguageTag>,
    blockInit: ProgramLocalScopeTemplate<LanguageTag>.() -> Unit
) {
    val block = ProgramLocalScopeTemplate<LanguageTag>().apply(blockInit)
    val ifExpr = IfInstructionTemplate(cond, block)
    this.items.add(ifExpr)
}

fun <LanguageTag : ProgramLanguageTag> ProgramLocalScopeTemplate<LanguageTag>.`if`(
    cond: ProgramExpressionTemplate<LanguageTag>,
    blockInit: ProgramLocalScopeTemplate<LanguageTag>.() -> Unit
): IfElseInstructionTemplate<LanguageTag> {
    val block = ProgramLocalScopeTemplate<LanguageTag>().apply(blockInit)
    val ifElseExpr = IfElseInstructionTemplate(cond, block)
    this.items.add(ifElseExpr)
    return ifElseExpr
}

fun <LanguageTag> ProgramGlobalScopeTemplate<LanguageTag>.addIfInstr(
    cond: ProgramExpressionTemplate<LanguageTag>,
    blockInit: ProgramLocalScopeTemplate<LanguageTag>.() -> Unit
) where LanguageTag : GlobalInstructions, LanguageTag : ProgramLanguageTag {
    val block = ProgramLocalScopeTemplate<LanguageTag>().apply(blockInit)
    val ifExpr = IfInstructionTemplate(cond, block)
    this.items.add(ifExpr)
}

fun <LanguageTag> ProgramGlobalScopeTemplate<LanguageTag>.`if`(
    cond: ProgramExpressionTemplate<LanguageTag>,
    blockInit: ProgramLocalScopeTemplate<LanguageTag>.() -> Unit
): IfElseInstructionTemplate<LanguageTag> where LanguageTag : GlobalInstructions, LanguageTag : ProgramLanguageTag {
    val block = ProgramLocalScopeTemplate<LanguageTag>().apply(blockInit)
    val ifElseExpr = IfElseInstructionTemplate(cond, block)
    this.items.add(ifElseExpr)
    return ifElseExpr
}