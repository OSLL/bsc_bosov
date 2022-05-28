@file:Suppress("unused")

package com.github.neckbosov.bsc_bosov.task_manager.dsl

import com.github.neckbosov.bsc_bosov.dsl.features.DymamicTyping
import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import com.github.neckbosov.bsc_bosov.dsl.template.*


fun <LanguageTag : ProgramLanguageTag> ProgramGlobalScopeTemplate<LanguageTag>.arg(
    variable: VariableTemplate<LanguageTag>,
    typeName: String,
    initialValue: ProgramExpressionTemplate<LanguageTag>?
): FunctionalArgumentTemplate<LanguageTag> = FunctionalArgumentTemplate(variable, typeName, initialValue)

fun <LanguageTag : ProgramLanguageTag> ProgramGlobalScopeTemplate<LanguageTag>.arg(
    variableName: ProgramVariableNameTemplate<LanguageTag>,
    typeName: String,
    initialValue: ProgramExpressionTemplate<LanguageTag>?
) = this.arg(VariableTemplate(variableName), typeName, initialValue)

fun <LanguageTag : ProgramLanguageTag> ProgramGlobalScopeTemplate<LanguageTag>.arg(
    variableName: String,
    typeName: String,
    initialValue: ProgramExpressionTemplate<LanguageTag>?
) = this.arg(variable(variableName), typeName, initialValue)

fun <LanguageTag> ProgramGlobalScopeTemplate<LanguageTag>.arg(
    variable: VariableTemplate<LanguageTag>,
    initialValue: ProgramExpressionTemplate<LanguageTag>?
): FunctionalArgumentTemplate<LanguageTag> where LanguageTag : ProgramLanguageTag, LanguageTag : DymamicTyping =
    FunctionalArgumentTemplate(variable, null, initialValue)

fun <LanguageTag> ProgramGlobalScopeTemplate<LanguageTag>.arg(
    variableName: ProgramVariableNameTemplate<LanguageTag>,
    initialValue: ProgramExpressionTemplate<LanguageTag>?
) where LanguageTag : ProgramLanguageTag, LanguageTag : DymamicTyping =
    this.arg(VariableTemplate(variableName), initialValue)

fun <LanguageTag> ProgramGlobalScopeTemplate<LanguageTag>.arg(
    variableName: String,
    initialValue: ProgramExpressionTemplate<LanguageTag>?
) where LanguageTag : ProgramLanguageTag, LanguageTag : DymamicTyping =
    this.arg(variable(variableName), initialValue)

fun <LanguageTag : ProgramLanguageTag> ProgramFunctionalScopeTemplate<LanguageTag>.getArg(name: ProgramVariableNameTemplate<LanguageTag>): VariableTemplate<LanguageTag> =
    this.variable(name)

fun <LanguageTag : ProgramLanguageTag> ProgramFunctionalScopeTemplate<LanguageTag>.getArg(name: String): VariableTemplate<LanguageTag> =
    this.variable(name)

fun <LanguageTag : ProgramLanguageTag> ProgramGlobalScopeTemplate<LanguageTag>.addFunctionalDef(
    returnTypeName: String,
    name: ProgramVariableNameTemplate<LanguageTag>,
    vararg arguments: FunctionalArgumentTemplate<LanguageTag>,
    scopeInit: ProgramFunctionalScopeTemplate<LanguageTag>.() -> Unit
) {
    val scope = ProgramFunctionalScopeTemplate<LanguageTag>().apply(scopeInit)
    this.items.add(FunctionalDefinitionTemplate(name, arguments.toList(), scope, returnTypeName))
}


fun <LanguageTag : ProgramLanguageTag> ProgramGlobalScopeTemplate<LanguageTag>.addFunctionalDef(
    returnTypeName: String,
    name: String,
    vararg arguments: FunctionalArgumentTemplate<LanguageTag>,
    scopeInit: ProgramFunctionalScopeTemplate<LanguageTag>.() -> Unit
): Unit = this.addFunctionalDef(returnTypeName, ConstantProgramVariableNameTemplate(name), *arguments) {
    scopeInit()
}

fun <LanguageTag> ProgramGlobalScopeTemplate<LanguageTag>.addFunctionalDef(
    name: ProgramVariableNameTemplate<LanguageTag>,
    vararg arguments: FunctionalArgumentTemplate<LanguageTag>,
    scopeInit: ProgramFunctionalScopeTemplate<LanguageTag>.() -> Unit
) where LanguageTag : ProgramLanguageTag, LanguageTag : DymamicTyping {
    val scope = ProgramFunctionalScopeTemplate<LanguageTag>().apply(scopeInit)
    this.items.add(FunctionalDefinitionTemplate(name, arguments.toList(), scope, null))
}

fun <LanguageTag> ProgramGlobalScopeTemplate<LanguageTag>.addFunctionalDef(
    name: String,
    vararg arguments: FunctionalArgumentTemplate<LanguageTag>,
    scopeInit: ProgramFunctionalScopeTemplate<LanguageTag>.() -> Unit
): Unit where LanguageTag : ProgramLanguageTag, LanguageTag : DymamicTyping =
    this.addFunctionalDef(ConstantProgramVariableNameTemplate(name), *arguments) {
        scopeInit()
    }