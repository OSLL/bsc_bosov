package com.github.neckbosov.bsc_bosov.dsl

import com.github.neckbosov.bsc_bosov.dsl.features.GlobalInstructions
import kotlin.random.Random

class IfExpression<LanguageTag>(
    random: Random,
    attributes: ProgramAttributes,
    val cond: ProgramExpression<LanguageTag>,
    val block: ProgramLocalScope<LanguageTag>
) : ProgramItem<LanguageTag>(random, attributes)

class IfElseExpression<LanguageTag>(
    random: Random,
    attributes: ProgramAttributes,
) : ProgramItem<LanguageTag>(random, attributes) {
    lateinit var ifBlock: IfExpression<LanguageTag>
    val elifBlocks = mutableListOf<IfExpression<LanguageTag>>()
    var elseBlock: ProgramLocalScope<LanguageTag>? = null
    fun mainIfIsInitialized() = this::ifBlock.isInitialized
}

fun <LanguageTag> IfElseExpression<LanguageTag>.addIf(
    ifCond: ProgramExpression<LanguageTag>,
    blockInit: ProgramLocalScope<LanguageTag>.() -> Unit
) {
    if (this.mainIfIsInitialized()) {
        throw Exception("Main if already exists")
    }
    val ifBlock = ProgramLocalScope<LanguageTag>(this.random, this.attributes).apply(blockInit)
    val ifExpression = IfExpression<LanguageTag>(this.random, this.attributes, ifCond, ifBlock)
    this.ifBlock = ifExpression
}

fun <LanguageTag> IfElseExpression<LanguageTag>.addElIf(
    ifCond: ProgramExpression<LanguageTag>,
    blockInit: ProgramLocalScope<LanguageTag>.() -> Unit
) {
    val ifBlock = ProgramLocalScope<LanguageTag>(this.random, this.attributes).apply(blockInit)
    val ifExpression = IfExpression<LanguageTag>(this.random, this.attributes, ifCond, ifBlock)
    this.elifBlocks.add(ifExpression)
}

fun <LanguageTag> IfElseExpression<LanguageTag>.addElse(
    blockInit: ProgramLocalScope<LanguageTag>.() -> Unit
) {
    this.elseBlock = ProgramLocalScope<LanguageTag>(this.random, this.attributes).apply(blockInit)
}

fun <LanguageTag> ProgramLocalScope<LanguageTag>.addIfExpr(
    ifCond: ProgramExpression<LanguageTag>,
    blockInit: ProgramLocalScope<LanguageTag>.() -> Unit
) {
    val ifBlock = ProgramLocalScope<LanguageTag>(this.random, this.attributes).apply(blockInit)
    val ifExpression = IfExpression<LanguageTag>(this.random, this.attributes, ifCond, ifBlock)
    this.items.add(ifExpression)
}

fun <LanguageTag : GlobalInstructions> ProgramGlobalScope<LanguageTag>.addIfExpr(
    ifCond: ProgramExpression<LanguageTag>,
    blockInit: ProgramLocalScope<LanguageTag>.() -> Unit
) {
    val ifBlock = ProgramLocalScope<LanguageTag>(this.random, this.attributes).apply(blockInit)
    val ifExpression = IfExpression<LanguageTag>(this.random, this.attributes, ifCond, ifBlock)
    this.items.add(ifExpression)
}

fun <LanguageTag> ProgramLocalScope<LanguageTag>.addIfElseExpr(
    blockInit: IfElseExpression<LanguageTag>.() -> Unit
) {
    val ifElseExpr = IfElseExpression<LanguageTag>(this.random, this.attributes).apply(blockInit)
    this.items.add(ifElseExpr)
}

fun <LanguageTag : GlobalInstructions> ProgramGlobalScope<LanguageTag>.addIfElseExpr(
    blockInit: IfElseExpression<LanguageTag>.() -> Unit
) {
    val ifElseExpr = IfElseExpression<LanguageTag>(this.random, this.attributes).apply(blockInit)
    this.items.add(ifElseExpr)
}