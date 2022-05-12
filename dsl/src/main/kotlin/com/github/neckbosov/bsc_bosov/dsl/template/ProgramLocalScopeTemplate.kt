package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramLocalScope
import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
class ProgramLocalScopeTemplate<LanguageTag : ProgramLanguageTag> : ProgramScopeTemplate<LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): ProgramLocalScope<LanguageTag> {
        return ProgramLocalScope<LanguageTag>().apply {
            this.items.addAll(this@ProgramLocalScopeTemplate.items.map { it.fillItem(random, attributes) })
        }
    }

}