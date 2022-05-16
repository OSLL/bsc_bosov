@file:Suppress("unused")

package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramGlobalScope
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramLocalScope
import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
class ForeachNumRepeatGlobalScopeTemplate<T, LanguageTag>(
    val items: List<ProgramNumberConstantTemplate<T, LanguageTag>>,
    val currentItem: WrappedMutableNumConstant<T, LanguageTag>,
    val scope: ProgramGlobalScopeTemplate<LanguageTag>
) : ProgramInstructionTemplate<LanguageTag>
        where T : Number,
              LanguageTag : ProgramLanguageTag {
    override fun fillItem(random: Random, attributes: ProgramAttributes): ProgramGlobalScope<LanguageTag> {
        val programScope = ProgramGlobalScope<LanguageTag>()
        for (item in items) {
            val itemValue = item.fillItem(random, attributes).value
            currentItem.constant = NumConstantTemplate(itemValue)
            val stepScope = scope.fillItem(random, attributes)
            programScope.items.addAll(stepScope.items)
        }
        return programScope
    }
}

@JvmName("foreachRepeatNumberConstantItemsGlobal")
fun <T : Number, LanguageTag : ProgramLanguageTag> ProgramGlobalScopeTemplate<LanguageTag>.foreachRepeat(
    foreachItems: List<ProgramNumberConstantTemplate<T, LanguageTag>>,
    scopeInit: ProgramGlobalScopeTemplate<LanguageTag>.(ProgramNumberConstantTemplate<T, LanguageTag>) -> Unit
) {
    if (foreachItems.isEmpty()) return
    val currentItem = WrappedMutableNumConstant(foreachItems.first())
    val scope = ProgramGlobalScopeTemplate<LanguageTag>().apply {
        scopeInit(currentItem)
    }
    this.items.add(ForeachNumRepeatGlobalScopeTemplate(foreachItems, currentItem, scope))
}

@JvmName("foreachRepeatPureNumberItemsGlobal")
fun <T : Number, LanguageTag : ProgramLanguageTag> ProgramGlobalScopeTemplate<LanguageTag>.foreachRepeat(
    foreachItems: List<T>,
    scopeInit: ProgramGlobalScopeTemplate<LanguageTag>.(ProgramNumberConstantTemplate<T, LanguageTag>) -> Unit
): Unit = this.foreachRepeat(foreachItems.map { NumConstantTemplate(it) }, scopeInit)

@Serializable
class ForeachNumRepeatLocalScopeTemplate<T, LanguageTag>(
    val items: List<ProgramNumberConstantTemplate<T, LanguageTag>>,
    val currentItem: WrappedMutableNumConstant<T, LanguageTag>,
    val scope: ProgramLocalScopeTemplate<LanguageTag>
) : ProgramInstructionTemplate<LanguageTag>
        where T : Number,
              LanguageTag : ProgramLanguageTag {
    override fun fillItem(random: Random, attributes: ProgramAttributes): ProgramLocalScope<LanguageTag> {
        val programScope = ProgramLocalScope<LanguageTag>()
        for (item in items) {
            val itemValue = item.fillItem(random, attributes).value
            currentItem.constant = NumConstantTemplate(itemValue)
            val stepScope = scope.fillItem(random, attributes)
            programScope.items.addAll(stepScope.items)
        }
        return programScope
    }
}

@JvmName("foreachRepeatNumberConstantItemsLocal")
fun <T : Number, LanguageTag : ProgramLanguageTag> ProgramLocalScopeTemplate<LanguageTag>.foreachRepeat(
    foreachItems: List<ProgramNumberConstantTemplate<T, LanguageTag>>,
    scopeInit: ProgramLocalScopeTemplate<LanguageTag>.(ProgramNumberConstantTemplate<T, LanguageTag>) -> Unit
) {
    if (foreachItems.isEmpty()) return
    val currentItem = WrappedMutableNumConstant(foreachItems.first())
    val scope = ProgramLocalScopeTemplate<LanguageTag>().apply {
        scopeInit(currentItem)
    }
    this.items.add(ForeachNumRepeatLocalScopeTemplate(foreachItems, currentItem, scope))
}

@JvmName("foreachRepeatPureNumberItemsLocal")
fun <T : Number, LanguageTag : ProgramLanguageTag> ProgramLocalScopeTemplate<LanguageTag>.foreachRepeat(
    foreachItems: List<T>,
    scopeInit: ProgramLocalScopeTemplate<LanguageTag>.(ProgramNumberConstantTemplate<T, LanguageTag>) -> Unit
): Unit = this.foreachRepeat(foreachItems.map { NumConstantTemplate(it) }, scopeInit)

@Serializable
class ForeachStringRepeatGlobalScopeTemplate<LanguageTag>(
    val items: List<ProgramStringConstantTemplate<LanguageTag>>,
    val currentItem: WrappedMutableStringConstant<LanguageTag>,
    val scope: ProgramGlobalScopeTemplate<LanguageTag>
) : ProgramInstructionTemplate<LanguageTag>
        where LanguageTag : ProgramLanguageTag {
    override fun fillItem(random: Random, attributes: ProgramAttributes): ProgramGlobalScope<LanguageTag> {
        val programScope = ProgramGlobalScope<LanguageTag>()
        for (item in items) {
            val itemValue = item.fillItem(random, attributes).value
            currentItem.constant = StringConstantTemplate(itemValue)
            val stepScope = scope.fillItem(random, attributes)
            programScope.items.addAll(stepScope.items)
        }
        return programScope
    }
}

@JvmName("foreachRepeatStringConstantItemsGlobal")
fun <LanguageTag : ProgramLanguageTag> ProgramGlobalScopeTemplate<LanguageTag>.foreachRepeat(
    foreachItems: List<ProgramStringConstantTemplate<LanguageTag>>,
    scopeInit: ProgramGlobalScopeTemplate<LanguageTag>.(ProgramStringConstantTemplate<LanguageTag>) -> Unit
) {
    if (foreachItems.isEmpty()) return
    val currentItem = WrappedMutableStringConstant(foreachItems.first())
    val scope = ProgramGlobalScopeTemplate<LanguageTag>().apply {
        scopeInit(currentItem)
    }
    this.items.add(ForeachStringRepeatGlobalScopeTemplate(foreachItems, currentItem, scope))
}

@JvmName("foreachRepeatPureStringItemsGlobal")
fun <LanguageTag : ProgramLanguageTag> ProgramGlobalScopeTemplate<LanguageTag>.foreachRepeat(
    foreachItems: List<String>,
    scopeInit: ProgramGlobalScopeTemplate<LanguageTag>.(ProgramStringConstantTemplate<LanguageTag>) -> Unit
): Unit = this.foreachRepeat(foreachItems.map { StringConstantTemplate(it) }, scopeInit)

@Serializable
class ForeachStringRepeatLocalScopeTemplate<LanguageTag>(
    val items: List<ProgramStringConstantTemplate<LanguageTag>>,
    val currentItem: WrappedMutableStringConstant<LanguageTag>,
    val scope: ProgramLocalScopeTemplate<LanguageTag>
) : ProgramInstructionTemplate<LanguageTag>
        where LanguageTag : ProgramLanguageTag {
    override fun fillItem(random: Random, attributes: ProgramAttributes): ProgramLocalScope<LanguageTag> {
        val programScope = ProgramLocalScope<LanguageTag>()
        for (item in items) {
            val itemValue = item.fillItem(random, attributes).value
            currentItem.constant = StringConstantTemplate(itemValue)
            val stepScope = scope.fillItem(random, attributes)
            programScope.items.addAll(stepScope.items)
        }
        return programScope
    }
}

@JvmName("foreachRepeatStringConstantItemsLocal")
fun <LanguageTag : ProgramLanguageTag> ProgramLocalScopeTemplate<LanguageTag>.foreachRepeat(
    foreachItems: List<ProgramStringConstantTemplate<LanguageTag>>,
    scopeInit: ProgramLocalScopeTemplate<LanguageTag>.(ProgramStringConstantTemplate<LanguageTag>) -> Unit
) {
    if (foreachItems.isEmpty()) return
    val currentItem = WrappedMutableStringConstant(foreachItems.first())
    val scope = ProgramLocalScopeTemplate<LanguageTag>().apply {
        scopeInit(currentItem)
    }
    this.items.add(ForeachStringRepeatLocalScopeTemplate(foreachItems, currentItem, scope))
}

@JvmName("foreachRepeatPureStringItemsLocal")
fun <LanguageTag : ProgramLanguageTag> ProgramLocalScopeTemplate<LanguageTag>.foreachRepeat(
    foreachItems: List<String>,
    scopeInit: ProgramLocalScopeTemplate<LanguageTag>.(ProgramStringConstantTemplate<LanguageTag>) -> Unit
): Unit = this.foreachRepeat(foreachItems.map { StringConstantTemplate(it) }, scopeInit)
