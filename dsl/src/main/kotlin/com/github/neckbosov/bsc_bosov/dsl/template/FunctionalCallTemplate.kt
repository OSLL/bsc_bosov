package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.program.FunctionalCall
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
class FunctionalCallTemplate<LanguageTag : ProgramLanguageTag>(
    val funcCallExpr: FunctionalCallExprTemplate<LanguageTag>
) :
    ProgramInstructionTemplate<LanguageTag> {
    override fun fillItem(random: Random, attributes: ProgramAttributes): FunctionalCall<LanguageTag> {
        return FunctionalCall(funcCallExpr.fillItem(random, attributes))
    }
}
