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

inline fun <LanguageTag : ProgramLanguageTag, reified T> ProgramScopeTemplate<LanguageTag>.constantList(vararg values: T): ProgramConstantListTemplate<LanguageTag> {
    return when (values.first()) {
        is Number -> NumConstantListTemplate(values.map {
            it as Number
            NumConstantTemplate(it)
        })
        is String -> StringConstantListTemplate(values.map {
            it as String
            StringConstantTemplate(it)
        })
        else -> error("Not supported constant type")
    }
}

fun <LanguageTag : ProgramLanguageTag, T : Number> ProgramScopeTemplate<LanguageTag>.constantList(vararg values: ProgramNumberConstantTemplate<T, LanguageTag>): ProgramConstantListTemplate<LanguageTag> {
    return NumConstantListTemplate(values.toList())
}

fun <LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.constantList(vararg values: ProgramStringConstantTemplate<LanguageTag>): ProgramConstantListTemplate<LanguageTag> {
    return StringConstantListTemplate(values.toList())
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

inline fun <reified T : Number, LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.numAttributeRefList(
    key: String
): NumAttributeRefListTemplate<T, LanguageTag> = NumAttributeRefListTemplate(getConstantType<T>(), key)

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

fun <LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.stringAttributeRefList(
    key: String
): StringAttributeRefListTemplate<LanguageTag> = StringAttributeRefListTemplate(key)