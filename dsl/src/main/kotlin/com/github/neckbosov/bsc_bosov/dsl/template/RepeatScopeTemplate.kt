@file:Suppress("unused")

package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramGlobalScope
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramLocalScope
import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import kotlin.random.Random

@kotlinx.serialization.Serializable
class RepeatGlobalScopeTemplate<LanguageTag>(
    val times: ProgramNumberConstantTemplate<Int, LanguageTag>,
    val counter: WrappedMutableNumConstant<Int, LanguageTag>,
    val scope: ProgramGlobalScopeTemplate<LanguageTag>
) : ProgramInstructionTemplate<LanguageTag>
        where LanguageTag : ProgramLanguageTag {
    override fun fillItem(random: Random, attributes: ProgramAttributes): ProgramGlobalScope<LanguageTag> {
        val timesValue = times.fillItem(random, attributes).value
        val programScope = ProgramGlobalScope<LanguageTag>()
        for (i in 0 until timesValue) {
            counter.constant = NumConstantTemplate(i)
            val stepScope = scope.fillItem(random, attributes)
            programScope.items.addAll(stepScope.items)
        }
        return programScope
    }
}

@kotlinx.serialization.Serializable
class RepeatLocalScopeTemplate<LanguageTag>(
    val times: ProgramNumberConstantTemplate<Int, LanguageTag>,
    val counter: WrappedMutableNumConstant<Int, LanguageTag>,
    val scope: ProgramLocalScopeTemplate<LanguageTag>
) : ProgramInstructionTemplate<LanguageTag>
        where LanguageTag : ProgramLanguageTag {
    override fun fillItem(random: Random, attributes: ProgramAttributes): ProgramLocalScope<LanguageTag> {
        val timesValue = times.fillItem(random, attributes).value.toInt()
        val programScope = ProgramLocalScope<LanguageTag>()
        for (i in 0 until timesValue) {
            counter.constant = NumConstantTemplate(i)
            val stepScope = scope.fillItem(random, attributes)
            programScope.items.addAll(stepScope.items)
        }
        return programScope
    }
}

fun <LanguageTag : ProgramLanguageTag> ProgramGlobalScopeTemplate<LanguageTag>.repeatScope(
    times: ProgramNumberConstantTemplate<Int, LanguageTag>,
    scopeInit: ProgramGlobalScopeTemplate<LanguageTag>.(ProgramNumberConstantTemplate<Int, LanguageTag>) -> Unit
) {
    val counter = WrappedMutableNumConstant<Int, LanguageTag>(NumConstantTemplate(0))
    val scope = ProgramGlobalScopeTemplate<LanguageTag>().apply {
        scopeInit(counter)
    }
    this.items.add(RepeatGlobalScopeTemplate(times, counter, scope))
}

fun <LanguageTag : ProgramLanguageTag> ProgramGlobalScopeTemplate<LanguageTag>.repeatScope(
    times: Int,
    scopeInit: ProgramGlobalScopeTemplate<LanguageTag>.(ProgramNumberConstantTemplate<Int, LanguageTag>) -> Unit
) = this.repeatScope(NumConstantTemplate(times), scopeInit)

fun <LanguageTag : ProgramLanguageTag> ProgramLocalScopeTemplate<LanguageTag>.repeatScope(
    times: ProgramNumberConstantTemplate<Int, LanguageTag>,
    scopeInit: ProgramLocalScopeTemplate<LanguageTag>.(ProgramNumberConstantTemplate<Int, LanguageTag>) -> Unit
) {
    val counter = WrappedMutableNumConstant<Int, LanguageTag>(NumConstantTemplate(0))
    val scope = ProgramLocalScopeTemplate<LanguageTag>().apply {
        scopeInit(counter)
    }
    this.items.add(RepeatLocalScopeTemplate(times, counter, scope))
}

fun <LanguageTag : ProgramLanguageTag> ProgramLocalScopeTemplate<LanguageTag>.repeatScope(
    times: Int,
    scopeInit: ProgramLocalScopeTemplate<LanguageTag>.(ProgramNumberConstantTemplate<Int, LanguageTag>) -> Unit
) = this.repeatScope(NumConstantTemplate(times), scopeInit)