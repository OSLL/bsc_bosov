@file:Suppress("unused")

package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.program.ConstantList
import com.github.neckbosov.bsc_bosov.dsl.program.NumConstantList
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import com.github.neckbosov.bsc_bosov.dsl.program.StringConstantList
import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
sealed class ProgramConstantListTemplate<LanguageTag : ProgramLanguageTag> : ProgramExpressionTemplate<LanguageTag>() {
    abstract override fun fillItem(random: Random, attributes: ProgramAttributes): ConstantList<LanguageTag>
}

@Serializable
sealed class ProgramNumberConstantListTemplate<T : Number, LanguageTag : ProgramLanguageTag> :
    ProgramConstantListTemplate<LanguageTag>() {
    abstract override fun fillItem(random: Random, attributes: ProgramAttributes): NumConstantList<T, LanguageTag>
}

@Serializable
sealed class ProgramStringConstantListTemplate<LanguageTag : ProgramLanguageTag> :
    ProgramConstantListTemplate<LanguageTag>() {
    abstract override fun fillItem(random: Random, attributes: ProgramAttributes): StringConstantList<LanguageTag>
}

@Serializable
class StringConstantListTemplate<LanguageTag : ProgramLanguageTag>(
    val strings: List<ProgramStringConstantTemplate<LanguageTag>>
) : ProgramStringConstantListTemplate<LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): StringConstantList<LanguageTag> {
        return StringConstantList(strings.map { it.fillItem(random, attributes).value })
    }
}

@Serializable
class NumConstantListTemplate<T : Number, LanguageTag : ProgramLanguageTag>(
    val numbers: List<ProgramNumberConstantTemplate<T, LanguageTag>>
) :
    ProgramNumberConstantListTemplate<T, LanguageTag>() {

    override fun fillItem(random: Random, attributes: ProgramAttributes): NumConstantList<T, LanguageTag> {
        return NumConstantList(numbers.map { it.fillItem(random, attributes).value })
    }
}



@Suppress("UNCHECKED_CAST")
@Serializable
class NumAttributeRefListTemplate<T : Number, LanguageTag : ProgramLanguageTag>(
    val numType: NumType,
    val key: String
) : ProgramNumberConstantListTemplate<T, LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): NumConstantList<T, LanguageTag> {
        val values = attributes.getValue(key)
        return NumConstantList(values.map {
            when (numType) {
                NumType.LONG -> it.toLong() as T
                NumType.INT -> it.toInt() as T
                NumType.FLOAT -> it.toFloat() as T
                NumType.DOUBLE -> it.toDouble() as T
            }
        })
    }
}

@Serializable
class StringAttributeRefListTemplate<LanguageTag : ProgramLanguageTag>(
    val key: String
) :
    ProgramStringConstantListTemplate<LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): StringConstantList<LanguageTag> {
        val values = attributes.getValue(key)
        return StringConstantList(values)
    }
}

