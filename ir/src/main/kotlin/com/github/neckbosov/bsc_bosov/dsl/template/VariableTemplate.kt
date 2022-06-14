@file:Suppress("unused")

package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import com.github.neckbosov.bsc_bosov.dsl.program.StringConstant
import com.github.neckbosov.bsc_bosov.dsl.program.Variable
import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
sealed class ProgramVariableNameTemplate<LanguageTag : ProgramLanguageTag> :
    ProgramStringConstantTemplate<LanguageTag>() {
    companion object {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9') + "_".toCharArray().toList()
    }

    abstract override fun fillItem(random: Random, attributes: ProgramAttributes): StringConstant<LanguageTag>
}

@Serializable
class ConstantProgramVariableNameTemplate<LanguageTag : ProgramLanguageTag>(val name: String) :
    ProgramVariableNameTemplate<LanguageTag>() {
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

@Serializable
class RandomVariableNameTemplate<LanguageTag : ProgramLanguageTag>(
    val length: ProgramNumberConstantTemplate<Int, LanguageTag>
) : ProgramVariableNameTemplate<LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): StringConstant<LanguageTag> {
        val lengthValue = length.fillItem(random, attributes).value
        val beginAllowed = allowedChars - ('0'..'9')
        return (2..lengthValue)
            .map { allowedChars.random(random) }
            .joinToString(prefix = beginAllowed.random(random).toString(), separator = "")
            .let { StringConstant(it) }
    }
}

@Serializable
class AttributeRefVariableNameTemplate<LanguageTag : ProgramLanguageTag>(val key: String, val ind: Int? = null) :
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

@Serializable
class VariableTemplate<LanguageTag : ProgramLanguageTag>(val name: ProgramVariableNameTemplate<LanguageTag>) :
    ProgramExpressionTemplate<LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): Variable<LanguageTag> {
        val nameString = name.fillItem(random, attributes).value
        return Variable(nameString)
    }
}

