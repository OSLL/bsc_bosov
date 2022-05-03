package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import com.github.neckbosov.bsc_bosov.dsl.program.StringConstant
import com.github.neckbosov.bsc_bosov.dsl.program.Variable
import kotlin.random.Random

sealed class ProgramVariableNameTemplate<LanguageTag> : ProgramStringConstantTemplate<LanguageTag>() {
    companion object {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9') + "_".toCharArray().toList()
    }

    abstract override fun fillItem(random: Random, attributes: ProgramAttributes): StringConstant<LanguageTag>
}

class ConstantProgramVariableNameTemplate<LanguageTag>(val name: String) : ProgramVariableNameTemplate<LanguageTag>() {
    init {
        if (!name.all { it in allowedChars }) {
            throw Exception("incorrect symbol in name of variable")
        }
        if (name[0] in ('0'..'9')) {
            throw Exception("Variable name should not start with digit")
        }
    }

    override fun fillItem(random: Random, attributes: ProgramAttributes): StringConstant<LanguageTag> {
        return StringConstant(name)
    }
}

class RandomVariableNameTemplate<LanguageTag>(
    val length: ProgramNumberConstantTemplate<Int, LanguageTag>
) : ProgramVariableNameTemplate<LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): StringConstant<LanguageTag> {
        val lengthValue = length.fillItem(random, attributes).value.toInt()
        val beginAllowed = allowedChars - ('0'..'9')
        return (2..lengthValue)
            .map { allowedChars.random(random) }
            .joinToString(prefix = beginAllowed.random(random).toString(), separator = "")
            .let { StringConstant(it) }
    }
}

class AttributeRefVariableNameTemplate<LanguageTag>(val key: String, val ind: Int? = null) :
    ProgramVariableNameTemplate<LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): StringConstant<LanguageTag> {
        val name = attributes.getValue(key)[ind ?: 0]
        if (!name.all { it in allowedChars }) {
            throw Exception("incorrect symbol in name of variable")
        }
        if (name[0] in ('0'..'9')) {
            throw Exception("Variable name should not start with digit")
        }
        return StringConstant(name)
    }

}

class VariableTemplate<LanguageTag>(val name: ProgramVariableNameTemplate<LanguageTag>) :
    ProgramExpressionTemplate<LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): Variable<LanguageTag> {
        val nameString = name.fillItem(random, attributes).value
        return Variable(nameString)
    }
}

fun <LanguageTag> ProgramScopeTemplate<LanguageTag>.variable(name: String): VariableTemplate<LanguageTag> =
    VariableTemplate(ConstantProgramVariableNameTemplate(name))

fun <LanguageTag> ProgramScopeTemplate<LanguageTag>.variable(varName: ProgramVariableNameTemplate<LanguageTag>) =
    VariableTemplate(varName)