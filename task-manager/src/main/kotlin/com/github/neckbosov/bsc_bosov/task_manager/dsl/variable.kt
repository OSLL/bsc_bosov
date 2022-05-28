package com.github.neckbosov.bsc_bosov.task_manager.dsl

import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import com.github.neckbosov.bsc_bosov.dsl.template.ConstantProgramVariableNameTemplate
import com.github.neckbosov.bsc_bosov.dsl.template.ProgramScopeTemplate
import com.github.neckbosov.bsc_bosov.dsl.template.ProgramVariableNameTemplate
import com.github.neckbosov.bsc_bosov.dsl.template.VariableTemplate

@Suppress("unused")
fun <LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.variable(name: String): VariableTemplate<LanguageTag> =
    VariableTemplate(ConstantProgramVariableNameTemplate(name))

@Suppress("unused")
fun <LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.variable(varName: ProgramVariableNameTemplate<LanguageTag>) =
    VariableTemplate(varName)