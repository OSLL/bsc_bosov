package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramGlobalScope
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramLocalScope
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramScope
import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
sealed class ProgramScopeTemplate<LanguageTag : ProgramLanguageTag> : ProgramInstructionTemplate<LanguageTag> {
    val items = mutableListOf<ProgramInstructionTemplate<LanguageTag>>()
    abstract override fun fillItem(random: Random, attributes: ProgramAttributes): ProgramScope<LanguageTag>
}

@Serializable
open class ProgramLocalScopeTemplate<LanguageTag : ProgramLanguageTag> : ProgramScopeTemplate<LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): ProgramLocalScope<LanguageTag> {
        return ProgramLocalScope<LanguageTag>().apply {
            this.items.addAll(this@ProgramLocalScopeTemplate.items.map { it.fillItem(random, attributes) })
        }
    }

}

@Serializable
open class ProgramGlobalScopeTemplate<LanguageTag : ProgramLanguageTag> : ProgramScopeTemplate<LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): ProgramGlobalScope<LanguageTag> {
        return ProgramGlobalScope<LanguageTag>().apply {
            this.items.addAll(this@ProgramGlobalScopeTemplate.items.map { it.fillItem(random, attributes) })
        }
    }
}

@Serializable
open class ProgramFunctionalScopeTemplate<LanguageTag : ProgramLanguageTag> : ProgramLocalScopeTemplate<LanguageTag>()