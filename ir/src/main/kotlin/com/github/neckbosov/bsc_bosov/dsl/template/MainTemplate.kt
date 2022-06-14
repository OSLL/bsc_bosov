package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.program.Main
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import kotlin.random.Random

@kotlinx.serialization.Serializable
class MainTemplate<LanguageTag : ProgramLanguageTag>(
    val scope: ProgramFunctionalScopeTemplate<LanguageTag>
) : ProgramInstructionTemplate<LanguageTag> {
    override fun fillItem(random: Random, attributes: ProgramAttributes): Main<LanguageTag> {
        return Main(scope.fillItem(random, attributes))
    }
}
