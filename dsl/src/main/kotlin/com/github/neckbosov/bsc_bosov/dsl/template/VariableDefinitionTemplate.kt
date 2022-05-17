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

fun <LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.varDef(
    variable: VariableTemplate<LanguageTag>,
    typeName: String,
    initialValue: ProgramExpressionTemplate<LanguageTag>? = null
) {
    val definition = VariableDefinitionTemplate(variable, typeName, initialValue)
    this.items.add(definition)
}

fun <LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.varDef(
    variableName: ProgramVariableNameTemplate<LanguageTag>,
    typeName: String,
    initialValue: ProgramExpressionTemplate<LanguageTag>?
) = this.varDef(VariableTemplate(variableName), typeName, initialValue)

fun <LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.varDef(
    variableName: String,
    typeName: String,
    initialValue: ProgramExpressionTemplate<LanguageTag>?
) = this.varDef(variable(variableName), typeName, initialValue)

fun <LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.varDef(
    variable: VariableTemplate<LanguageTag>,
    initialValue: ProgramExpressionTemplate<LanguageTag>
) {
    val definition = VariableDefinitionTemplate(variable, null, initialValue)
    this.items.add(definition)
}

fun <LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.varDef(
    variableName: ProgramVariableNameTemplate<LanguageTag>,
    initialValue: ProgramExpressionTemplate<LanguageTag>
) = this.varDef(VariableTemplate(variableName), initialValue)

fun <LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.varDef(
    variableName: String,
    initialValue: ProgramExpressionTemplate<LanguageTag>
) = this.varDef(variable(variableName), initialValue)
