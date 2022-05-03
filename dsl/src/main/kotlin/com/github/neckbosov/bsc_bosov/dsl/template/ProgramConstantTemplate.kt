package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.program.Constant
import com.github.neckbosov.bsc_bosov.dsl.program.NumConstant
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import com.github.neckbosov.bsc_bosov.dsl.program.StringConstant
import kotlin.random.Random

enum class NumConstantType {
    LONG, INT, FLOAT, DOUBLE
}

inline fun <reified T : Number> getConstantType(): NumConstantType {
    return when (T::class) {
        Long::class -> NumConstantType.LONG
        Int::class -> NumConstantType.INT
        Float::class -> NumConstantType.FLOAT
        Double::class -> NumConstantType.DOUBLE
        else -> error("Unsupported constant type")
    }
}

sealed class ProgramConstantTemplate<LanguageTag> : ProgramExpressionTemplate<LanguageTag>() {
    abstract override fun fillItem(random: Random, attributes: ProgramAttributes): Constant<LanguageTag>
}

sealed class ProgramNumberConstantTemplate<T : Number, LanguageTag> : ProgramConstantTemplate<LanguageTag>() {
    abstract override fun fillItem(random: Random, attributes: ProgramAttributes): NumConstant<LanguageTag>
}

sealed class ProgramStringConstantTemplate<LanguageTag> : ProgramConstantTemplate<LanguageTag>() {
    abstract override fun fillItem(random: Random, attributes: ProgramAttributes): StringConstant<LanguageTag>
}

class StringConstantTemplate<LanguageTag>(val value: String) : ProgramStringConstantTemplate<LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): StringConstant<LanguageTag> {
        return StringConstant(value)
    }
}

class NumConstantTemplate<T : Number, LanguageTag>(val value: T) : ProgramNumberConstantTemplate<T, LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): NumConstant<LanguageTag> {
        return NumConstant(value)
    }
}

fun <LanguageTag, T> ProgramScopeTemplate<LanguageTag>.constant(value: T): ProgramConstantTemplate<LanguageTag> {
    return when (value) {
        is Number -> NumConstantTemplate(value)
        is String -> StringConstantTemplate(value)
        else -> error("Not supported constant type")
    }
}


class RandomNumConstantTemplate<T : Number, LanguageTag>(
    val numType: NumConstantType,
    val from: ProgramNumberConstantTemplate<T, LanguageTag>?,
    val until: ProgramNumberConstantTemplate<T, LanguageTag>?
) : ProgramNumberConstantTemplate<T, LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): NumConstant<LanguageTag> {
        val fromVal = from?.fillItem(random, attributes)?.value
        val untilVal = until?.fillItem(random, attributes)?.value
        return when (numType) {
            NumConstantType.LONG -> NumConstant(random.nextLong(fromVal?.toLong(), untilVal?.toLong()))
            NumConstantType.INT -> NumConstant(random.nextInt(fromVal?.toInt(), untilVal?.toInt()))
            NumConstantType.FLOAT -> NumConstant(random.nextFloat())
            NumConstantType.DOUBLE -> NumConstant(random.nextDouble(fromVal?.toDouble(), untilVal?.toDouble()))
        }
    }
}

inline fun <reified T : Number, LanguageTag> ProgramScopeTemplate<LanguageTag>.randomNumConstant(
    from: T? = null,
    until: T? = null
): RandomNumConstantTemplate<T, LanguageTag> =
    RandomNumConstantTemplate(
        getConstantType<T>(),
        from?.let { NumConstantTemplate(it) },
        until?.let { NumConstantTemplate(it) })


inline fun <reified T : Number, LanguageTag> ProgramScopeTemplate<LanguageTag>.randomNumConstant(
    from: NumConstantTemplate<T, LanguageTag>? = null,
    until: NumConstantTemplate<T, LanguageTag>? = null
): RandomNumConstantTemplate<T, LanguageTag> = RandomNumConstantTemplate(getConstantType<T>(), from, until)

fun <LanguageTag> ProgramScopeTemplate<LanguageTag>.randomFloatConstant(): RandomNumConstantTemplate<Float, LanguageTag> =
    RandomNumConstantTemplate(getConstantType<Float>(), null, null)

class RandomStringConstantTemplate<LanguageTag>(
    val length: ProgramNumberConstantTemplate<Int, LanguageTag>
) :
    ProgramStringConstantTemplate<LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): StringConstant<LanguageTag> {
        val lengthValue = length.fillItem(random, attributes).value.toInt()
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9') + ",.[]<>/?()*&^%_- ".toCharArray().toList()
        return (1..lengthValue)
            .map { allowedChars.random(random) }
            .joinToString("")
            .let { StringConstant(it) }
    }
}

fun <LanguageTag> ProgramScopeTemplate<LanguageTag>.randomStringConstant(length: Int): RandomStringConstantTemplate<LanguageTag> =
    RandomStringConstantTemplate(NumConstantTemplate(length))

fun <LanguageTag> ProgramScopeTemplate<LanguageTag>.randomStringConstant(length: ProgramNumberConstantTemplate<Int, LanguageTag>): RandomStringConstantTemplate<LanguageTag> =
    RandomStringConstantTemplate(length)

class NumAttributeRefTemplate<T : Number, LanguageTag>(
    val numType: NumConstantType,
    val key: String,
    val ind: ProgramNumberConstantTemplate<Int, LanguageTag>? = null
) : ProgramNumberConstantTemplate<T, LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): NumConstant<LanguageTag> {
        val indValue = ind?.fillItem(random, attributes)?.value?.toInt()
        val value = attributes.getValue(key)[indValue ?: 0]
        return when (numType) {
            NumConstantType.LONG -> NumConstant(value.toLong())
            NumConstantType.INT -> NumConstant(value.toInt())
            NumConstantType.FLOAT -> NumConstant(value.toFloat())
            NumConstantType.DOUBLE -> NumConstant(value.toDouble())
        }
    }
}

inline fun <reified T : Number, LanguageTag> ProgramScopeTemplate<LanguageTag>.numAttributeRef(
    key: String,
    ind: Int? = null
): NumAttributeRefTemplate<T, LanguageTag> =
    NumAttributeRefTemplate(getConstantType<T>(), key, ind?.let { NumConstantTemplate(it) })

inline fun <reified T : Number, LanguageTag> ProgramScopeTemplate<LanguageTag>.numAttributeRef(
    key: String,
    ind: ProgramNumberConstantTemplate<Int, LanguageTag>? = null
): NumAttributeRefTemplate<T, LanguageTag> = NumAttributeRefTemplate(getConstantType<T>(), key, ind)

class StringAttributeRefTemplate<LanguageTag>(
    val key: String,
    val ind: ProgramNumberConstantTemplate<Int, LanguageTag>? = null
) :
    ProgramStringConstantTemplate<LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): StringConstant<LanguageTag> {
        val indValue = ind?.fillItem(random, attributes)?.value?.toInt()
        val value = attributes.getValue(key)[indValue ?: 0]
        return StringConstant(value)
    }
}

fun <LanguageTag> ProgramScopeTemplate<LanguageTag>.stringAttributeRef(
    key: String,
    ind: Int? = null
): StringAttributeRefTemplate<LanguageTag> =
    StringAttributeRefTemplate(key, ind?.let { NumConstantTemplate(it) })

fun <LanguageTag> ProgramScopeTemplate<LanguageTag>.stringAttributeRef(
    key: String,
    ind: ProgramNumberConstantTemplate<Int, LanguageTag>? = null
): StringAttributeRefTemplate<LanguageTag> = StringAttributeRefTemplate(key, ind)