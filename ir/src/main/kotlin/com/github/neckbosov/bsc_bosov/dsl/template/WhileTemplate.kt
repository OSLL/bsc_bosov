@file:Suppress("unused")

package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import com.github.neckbosov.bsc_bosov.dsl.program.WhileInstruction
import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import kotlin.random.Random

@kotlinx.serialization.Serializable
class WhileInstructionTemplate<LanguageTag : ProgramLanguageTag>(
    val cond: ProgramExpressionTemplate<LanguageTag>,
    val scope: ProgramLocalScopeTemplate<LanguageTag>
) : ProgramInstructionTemplate<LanguageTag> {
    override fun fillItem(random: Random, attributes: ProgramAttributes): WhileInstruction<LanguageTag> {
        return WhileInstruction(
            cond.fillItem(random, attributes),
            scope.fillItem(random, attributes)
        )
    }
}

