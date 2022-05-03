package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramGlobalScope
import kotlin.random.Random

class ProgramGlobalScopeTemplate<LanguageTag> : ProgramScopeTemplate<LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): ProgramGlobalScope<LanguageTag> {
        return ProgramGlobalScope<LanguageTag>().apply {
            this.items.addAll(this@ProgramGlobalScopeTemplate.items.map { it.fillItem(random, attributes) })
        }
    }

}