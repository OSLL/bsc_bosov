package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.program.Program
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import kotlinx.serialization.Serializable
import kotlin.random.Random


@Serializable
class ProgramTemplate<LanguageTag>(val scope: ProgramGlobalScopeTemplate<LanguageTag>) {

    constructor(code: ProgramGlobalScopeTemplate<LanguageTag>.() -> Unit) : this(
        ProgramGlobalScopeTemplate<LanguageTag>().apply(
            code
        )
    )

    fun fill(seed: Long, attributes: ProgramAttributes): Program<LanguageTag> {
        val random = Random(seed)
        return Program(scope.fillItem(random, attributes))
    }
}