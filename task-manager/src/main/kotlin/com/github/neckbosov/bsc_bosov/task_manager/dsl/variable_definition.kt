@file:Suppress("unused")

package com.github.neckbosov.bsc_bosov.task_manager.dsl

import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import com.github.neckbosov.bsc_bosov.dsl.template.*


fun <LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.addVarDef(
    variable: VariableTemplate<LanguageTag>,
    typeName: String,
    initialValue: ProgramExpressionTemplate<LanguageTag>? = null
) {
    val definition = VariableDefinitionTemplate(variable, typeName, initialValue)
    this.items.add(definition)
}

fun <LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.addVarDef(
    variableName: ProgramVariableNameTemplate<LanguageTag>,
    typeName: String,
    initialValue: ProgramExpressionTemplate<LanguageTag>?
) = this.addVarDef(VariableTemplate(variableName), typeName, initialValue)

fun <LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.addVarDef(
    variableName: String,
    typeName: String,
    initialValue: ProgramExpressionTemplate<LanguageTag>?
) = this.addVarDef(variable(variableName), typeName, initialValue)

fun <LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.addVarDef(
    variable: VariableTemplate<LanguageTag>,
    initialValue: ProgramExpressionTemplate<LanguageTag>
) {
    val definition = VariableDefinitionTemplate(variable, null, initialValue)
    this.items.add(definition)
}

fun <LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.addVarDef(
    variableName: ProgramVariableNameTemplate<LanguageTag>,
    initialValue: ProgramExpressionTemplate<LanguageTag>
) = this.addVarDef(VariableTemplate(variableName), initialValue)

fun <LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.addVarDef(
    variableName: String,
    initialValue: ProgramExpressionTemplate<LanguageTag>
): Unit = this.addVarDef(variable(variableName), initialValue)