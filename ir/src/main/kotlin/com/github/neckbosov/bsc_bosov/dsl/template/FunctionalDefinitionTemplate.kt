@file:Suppress("unused")

package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.program.FunctionalArgument
import com.github.neckbosov.bsc_bosov.dsl.program.FunctionalDefinition
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
class FunctionalArgumentTemplate<LanguageTag : ProgramLanguageTag>(
    val variable: VariableTemplate<LanguageTag>,
    val typeName: String? = null,
    val initialValue: ProgramExpressionTemplate<LanguageTag>? = null
) : ProgramItemTemplate<LanguageTag> {
    override fun fillItem(random: Random, attributes: ProgramAttributes): FunctionalArgument<LanguageTag> {
        return FunctionalArgument(
            variable.fillItem(random, attributes),
            typeName,
            initialValue?.fillItem(random, attributes)
        )
    }
}


@Serializable
class FunctionalDefinitionTemplate<LanguageTag : ProgramLanguageTag>(
    val name: ProgramVariableNameTemplate<LanguageTag>,
    val arguments: List<FunctionalArgumentTemplate<LanguageTag>>,
    val scope: ProgramFunctionalScopeTemplate<LanguageTag>,
    val returnTypeName: String?
) : ProgramInstructionTemplate<LanguageTag> {
    override fun fillItem(random: Random, attributes: ProgramAttributes): FunctionalDefinition<LanguageTag> {
        return FunctionalDefinition(
            name.fillItem(random, attributes).value,
            arguments.map { it.fillItem(random, attributes) },
            scope.fillItem(random, attributes),
            returnTypeName
        )
    }
}
