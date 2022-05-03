package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.program.Program
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import kotlin.random.Random

class ProgramTemplate<LanguageTag>(code: ProgramGlobalScopeTemplate<LanguageTag>.() -> Unit) {
    val scope = ProgramGlobalScopeTemplate<LanguageTag>().apply(code)

    fun fill(seed: Long, attributes: ProgramAttributes): Program<LanguageTag> {
        val random = Random(seed)
        return Program(scope.fillItem(random, attributes))
    }
}