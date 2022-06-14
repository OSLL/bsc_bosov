package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.program.Assignment
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
class AssignmentTemplate<LanguageTag : ProgramLanguageTag>(
    val lhs: VariableTemplate<LanguageTag>,
    val rhs: ProgramExpressionTemplate<LanguageTag>
) : ProgramInstructionTemplate<LanguageTag> {
    override fun fillItem(random: Random, attributes: ProgramAttributes): Assignment<LanguageTag> {
        return Assignment(lhs.fillItem(random, attributes), rhs.fillItem(random, attributes))
    }
}