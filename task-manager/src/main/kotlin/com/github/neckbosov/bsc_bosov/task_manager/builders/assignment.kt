@file:Suppress("unused")

package com.github.neckbosov.bsc_bosov.task_manager.builders

import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import com.github.neckbosov.bsc_bosov.dsl.template.AssignmentTemplate
import com.github.neckbosov.bsc_bosov.dsl.template.ProgramExpressionTemplate
import com.github.neckbosov.bsc_bosov.dsl.template.ProgramScopeTemplate
import com.github.neckbosov.bsc_bosov.dsl.template.VariableTemplate

fun <LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.addAssignment(
    lhs: VariableTemplate<LanguageTag>,
    rhs: ProgramExpressionTemplate<LanguageTag>
) {
    this.items.add(AssignmentTemplate(lhs, rhs))
}

context(ProgramScopeTemplate<LanguageTag>)
        infix fun <LanguageTag : ProgramLanguageTag> VariableTemplate<LanguageTag>.setTo(rhs: ProgramExpressionTemplate<LanguageTag>) {
    val scope = this@ProgramScopeTemplate
    scope.addAssignment(this@VariableTemplate, rhs)
}
