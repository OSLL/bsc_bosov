@file:Suppress("unused")

package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.features.DymamicTyping
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