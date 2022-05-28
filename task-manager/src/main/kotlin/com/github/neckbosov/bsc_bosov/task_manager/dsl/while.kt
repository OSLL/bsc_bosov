@file:Suppress("unused")

package com.github.neckbosov.bsc_bosov.task_manager.dsl

import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import com.github.neckbosov.bsc_bosov.dsl.template.ProgramExpressionTemplate
import com.github.neckbosov.bsc_bosov.dsl.template.ProgramLocalScopeTemplate
import com.github.neckbosov.bsc_bosov.dsl.template.WhileInstructionTemplate

fun <LanguageTag : ProgramLanguageTag> ProgramLocalScopeTemplate<LanguageTag>.`while`(
    cond: ProgramExpressionTemplate<LanguageTag>,
    scopeInit: ProgramLocalScopeTemplate<LanguageTag>.() -> Unit
) {
    val scope = ProgramLocalScopeTemplate<LanguageTag>().apply(scopeInit)
    this.items.add(WhileInstructionTemplate(cond, scope))
}