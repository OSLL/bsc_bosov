@file:Suppress("unused")

package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import com.github.neckbosov.bsc_bosov.dsl.program.VariableDefinition
import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import kotlin.random.Random

@kotlinx.serialization.Serializable
class VariableDefinitionTemplate<LanguageTag : ProgramLanguageTag>(
    val variable: VariableTemplate<LanguageTag>,
    val typeName: String? = null,
    val initialValue: ProgramExpressionTemplate<LanguageTag>? = null
) : ProgramInstructionTemplate<LanguageTag> {
    override fun fillItem(random: Random, attributes: ProgramAttributes): VariableDefinition<LanguageTag> {
        return VariableDefinition(
            variable.fillItem(random, attributes),
            typeName,
            initialValue?.fillItem(random, attributes)
        )
    }
}

