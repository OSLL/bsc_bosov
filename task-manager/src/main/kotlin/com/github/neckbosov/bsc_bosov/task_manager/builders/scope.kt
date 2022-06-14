@file:Suppress("unused")

package com.github.neckbosov.bsc_bosov.task_manager.builders

import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import com.github.neckbosov.bsc_bosov.dsl.template.*

fun <T : Number, LanguageTag : ProgramLanguageTag> ProgramLocalScopeTemplate<LanguageTag>.foreachRepeat(
    foreachItems: ProgramNumberConstantListTemplate<T, LanguageTag>,
    scopeInit: ProgramLocalScopeTemplate<LanguageTag>.(ProgramNumberConstantTemplate<T, LanguageTag>) -> Unit
) {
    val currentItem = WrappedMutableNumConstant<T, LanguageTag>(null)
    val scope = ProgramLocalScopeTemplate<LanguageTag>().apply {
        scopeInit(currentItem)
    }
    this.items.add(ForeachNumRepeatLocalScopeTemplate(foreachItems, currentItem, scope))
}

fun <LanguageTag : ProgramLanguageTag> ProgramGlobalScopeTemplate<LanguageTag>.foreachRepeat(
    foreachItems: ProgramStringConstantListTemplate<LanguageTag>,
    scopeInit: ProgramGlobalScopeTemplate<LanguageTag>.(ProgramStringConstantTemplate<LanguageTag>) -> Unit
) {
    val currentItem = WrappedMutableStringConstant<LanguageTag>(null)
    val scope = ProgramGlobalScopeTemplate<LanguageTag>().apply {
        scopeInit(currentItem)
    }
    this.items.add(ForeachStringRepeatGlobalScopeTemplate(foreachItems, currentItem, scope))
}

fun <LanguageTag : ProgramLanguageTag> ProgramLocalScopeTemplate<LanguageTag>.foreachRepeat(
    foreachItems: ProgramStringConstantListTemplate<LanguageTag>,
    scopeInit: ProgramLocalScopeTemplate<LanguageTag>.(ProgramStringConstantTemplate<LanguageTag>) -> Unit
) {
    val currentItem = WrappedMutableStringConstant<LanguageTag>(null)
    val scope = ProgramLocalScopeTemplate<LanguageTag>().apply {
        scopeInit(currentItem)
    }
    this.items.add(ForeachStringRepeatLocalScopeTemplate(foreachItems, currentItem, scope))
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
