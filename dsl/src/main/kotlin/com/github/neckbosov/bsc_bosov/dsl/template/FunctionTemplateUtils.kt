@file:Suppress("unused")

package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.features.DymamicTyping
import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag

class FunctionNameTemplate<LanguageTag : ProgramLanguageTag>(
    val context: ProgramScopeTemplate<LanguageTag>,
    val name: ProgramVariableNameTemplate<LanguageTag>
)

fun <LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.function(
    name: ProgramVariableNameTemplate<LanguageTag>
): FunctionNameTemplate<LanguageTag> = FunctionNameTemplate(this, name)

fun <LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.function(
    name: String
): FunctionNameTemplate<LanguageTag> = FunctionNameTemplate(this, ConstantProgramVariableNameTemplate(name))

class FunctionNameArgumentsTemplate<LanguageTag : ProgramLanguageTag>(
    val context: ProgramScopeTemplate<LanguageTag>,
    val name: ProgramVariableNameTemplate<LanguageTag>,
    val arguments: List<FunctionalArgumentTemplate<LanguageTag>>,
)

class FunctionArgumentsTemplate<LanguageTag : ProgramLanguageTag>(
    val arguments: MutableList<FunctionalArgumentTemplate<LanguageTag>> = mutableListOf(),
)

fun <LanguageTag : ProgramLanguageTag> FunctionArgumentsTemplate<LanguageTag>.argument(
    variable: VariableTemplate<LanguageTag>,
    typeName: String,
    initialValue: ProgramExpressionTemplate<LanguageTag>?
) {
    this.arguments.add(FunctionalArgumentTemplate(variable, typeName, initialValue))
}

fun <LanguageTag : ProgramLanguageTag> FunctionArgumentsTemplate<LanguageTag>.argument(
    variableName: ProgramVariableNameTemplate<LanguageTag>,
    typeName: String,
    initialValue: ProgramExpressionTemplate<LanguageTag>?
) {
    this.argument(VariableTemplate(variableName), typeName, initialValue)
}


fun <LanguageTag : ProgramLanguageTag> FunctionArgumentsTemplate<LanguageTag>.argument(
    variableName: String,
    typeName: String,
    initialValue: ProgramExpressionTemplate<LanguageTag>?
) {
    this.argument(ConstantProgramVariableNameTemplate(variableName), typeName, initialValue)
}

fun <LanguageTag> FunctionArgumentsTemplate<LanguageTag>.argument(
    variable: VariableTemplate<LanguageTag>,
    initialValue: ProgramExpressionTemplate<LanguageTag>?
) where LanguageTag : ProgramLanguageTag, LanguageTag : DymamicTyping {
    this.arguments.add(FunctionalArgumentTemplate(variable, null, initialValue))
}

fun <LanguageTag> FunctionArgumentsTemplate<LanguageTag>.argument(
    variable: ProgramVariableNameTemplate<LanguageTag>,
    initialValue: ProgramExpressionTemplate<LanguageTag>?
) where LanguageTag : ProgramLanguageTag, LanguageTag : DymamicTyping {
    this.argument(VariableTemplate(variable), initialValue)
}

fun <LanguageTag> FunctionArgumentsTemplate<LanguageTag>.argument(
    variable: String,
    initialValue: ProgramExpressionTemplate<LanguageTag>?
) where LanguageTag : ProgramLanguageTag, LanguageTag : DymamicTyping {
    this.argument(ConstantProgramVariableNameTemplate(variable), initialValue)
}

fun <LanguageTag : ProgramLanguageTag> FunctionNameTemplate<LanguageTag>.withArguments(
    blockInit: FunctionArgumentsTemplate<LanguageTag>.() -> Unit
): FunctionNameArgumentsTemplate<LanguageTag> {
    val arguments = FunctionArgumentsTemplate<LanguageTag>().apply(blockInit)
    return FunctionNameArgumentsTemplate(this.context, this.name, arguments.arguments.toList())
}
