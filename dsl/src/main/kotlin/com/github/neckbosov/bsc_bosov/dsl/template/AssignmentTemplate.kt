package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.program.Assignment
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import kotlin.random.Random

class AssignmentTemplate<LanguageTag>(
    val lhs: VariableTemplate<LanguageTag>,
    val rhs: ProgramExpressionTemplate<LanguageTag>
) : ProgramInstructionTemplate<LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): Assignment<LanguageTag> {
        return Assignment(lhs.fillItem(random, attributes), rhs.fillItem(random, attributes))
    }
}

fun <LanguageTag> ProgramScopeTemplate<LanguageTag>.addAssignment(
    lhs: VariableTemplate<LanguageTag>,
    rhs: ProgramExpressionTemplate<LanguageTag>
) {
    this.items.add(AssignmentTemplate(lhs, rhs))
}