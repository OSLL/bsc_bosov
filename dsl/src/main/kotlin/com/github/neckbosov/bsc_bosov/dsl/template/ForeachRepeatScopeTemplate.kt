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
    val items: ProgramNumberConstantListTemplate<T, LanguageTag>,
    val currentItem: WrappedMutableNumConstant<T, LanguageTag>,
    val scope: ProgramGlobalScopeTemplate<LanguageTag>
) : ProgramInstructionTemplate<LanguageTag>
        where T : Number,
              LanguageTag : ProgramLanguageTag {
    override fun fillItem(random: Random, attributes: ProgramAttributes): ProgramGlobalScope<LanguageTag> {
        val programScope = ProgramGlobalScope<LanguageTag>()
        val filledItems = items.fillItem(random, attributes).numbers
        for (item in filledItems) {
            currentItem.constant = NumConstantTemplate(item)
            val stepScope = scope.fillItem(random, attributes)
            programScope.items.addAll(stepScope.items)
        }
        return programScope
    }
}


fun <T : Number, LanguageTag : ProgramLanguageTag> ProgramGlobalScopeTemplate<LanguageTag>.foreachRepeat(
    foreachItems: ProgramNumberConstantListTemplate<T, LanguageTag>,
    scopeInit: ProgramGlobalScopeTemplate<LanguageTag>.(ProgramNumberConstantTemplate<T, LanguageTag>) -> Unit
) {
    val currentItem = WrappedMutableNumConstant<T, LanguageTag>(null)
    val scope = ProgramGlobalScopeTemplate<LanguageTag>().apply {
        scopeInit(currentItem)
    }
    this.items.add(ForeachNumRepeatGlobalScopeTemplate(foreachItems, currentItem, scope))
}

@Serializable
class ForeachNumRepeatLocalScopeTemplate<T, LanguageTag>(
    val items: ProgramNumberConstantListTemplate<T, LanguageTag>,
    val currentItem: WrappedMutableNumConstant<T, LanguageTag>,
    val scope: ProgramLocalScopeTemplate<LanguageTag>
) : ProgramInstructionTemplate<LanguageTag>
        where T : Number,
              LanguageTag : ProgramLanguageTag {
    override fun fillItem(random: Random, attributes: ProgramAttributes): ProgramLocalScope<LanguageTag> {
        val programScope = ProgramLocalScope<LanguageTag>()
        val filledItems = items.fillItem(random, attributes).numbers
        for (item in filledItems) {
            currentItem.constant = NumConstantTemplate(item)
            val stepScope = scope.fillItem(random, attributes)
            programScope.items.addAll(stepScope.items)
        }
        return programScope
    }
}


@Serializable
class ForeachStringRepeatGlobalScopeTemplate<LanguageTag>(
    val items: ProgramStringConstantListTemplate<LanguageTag>,
    val currentItem: WrappedMutableStringConstant<LanguageTag>,
    val scope: ProgramGlobalScopeTemplate<LanguageTag>
) : ProgramInstructionTemplate<LanguageTag>
        where LanguageTag : ProgramLanguageTag {
    override fun fillItem(random: Random, attributes: ProgramAttributes): ProgramGlobalScope<LanguageTag> {
        val programScope = ProgramGlobalScope<LanguageTag>()
        val filledItems = items.fillItem(random, attributes).strings
        for (item in filledItems) {
            currentItem.constant = StringConstantTemplate(item)
            val stepScope = scope.fillItem(random, attributes)
            programScope.items.addAll(stepScope.items)
        }
        return programScope
    }
}


@Serializable
class ForeachStringRepeatLocalScopeTemplate<LanguageTag>(
    val items: ProgramStringConstantListTemplate<LanguageTag>,
    val currentItem: WrappedMutableStringConstant<LanguageTag>,
    val scope: ProgramLocalScopeTemplate<LanguageTag>
) : ProgramInstructionTemplate<LanguageTag>
        where LanguageTag : ProgramLanguageTag {
    override fun fillItem(random: Random, attributes: ProgramAttributes): ProgramLocalScope<LanguageTag> {
        val programScope = ProgramLocalScope<LanguageTag>()
        val filledItems = items.fillItem(random, attributes).strings
        for (item in filledItems) {
            currentItem.constant = StringConstantTemplate(item)
            val stepScope = scope.fillItem(random, attributes)
            programScope.items.addAll(stepScope.items)
        }
        return programScope
    }
}

