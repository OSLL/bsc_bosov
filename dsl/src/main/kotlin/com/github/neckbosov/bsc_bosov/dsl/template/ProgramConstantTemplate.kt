package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.program.Constant
import com.github.neckbosov.bsc_bosov.dsl.program.NumConstant
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import com.github.neckbosov.bsc_bosov.dsl.program.StringConstant
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
enum class NumType {
    LONG, INT, FLOAT, DOUBLE
}

@Serializable
sealed class ProgramNum(val kind: NumType)

@Serializable
class IntProgramNum(val number: Int) : ProgramNum(NumType.INT)

@Serializable
class LongProgramNum(val number: Long) : ProgramNum(NumType.LONG)

@Serializable
class FloatProgramNum(val number: Float) : ProgramNum(NumType.FLOAT)

@Serializable
class DoubleProgramNum(val number: Double) : ProgramNum(NumType.DOUBLE)

inline fun <reified T : Number> getConstantType(): NumType {
    return when (T::class) {
        Long::class -> NumType.LONG
        Int::class -> NumType.INT
        Float::class -> NumType.FLOAT
        Double::class -> NumType.DOUBLE
        else -> error("Unsupported constant type")
    }
}

@Serializable
sealed class ProgramConstantTemplate<LanguageTag> : ProgramExpressionTemplate<LanguageTag>() {
    abstract override fun fillItem(random: Random, attributes: ProgramAttributes): Constant<LanguageTag>
}

@Serializable
sealed class ProgramNumberConstantTemplate<T : Number, LanguageTag> : ProgramConstantTemplate<LanguageTag>() {
    abstract override fun fillItem(random: Random, attributes: ProgramAttributes): NumConstant<LanguageTag>
}

@Serializable
sealed class ProgramStringConstantTemplate<LanguageTag> : ProgramConstantTemplate<LanguageTag>() {
    abstract override fun fillItem(random: Random, attributes: ProgramAttributes): StringConstant<LanguageTag>
}

@Serializable
class StringConstantTemplate<LanguageTag>(val value: String) : ProgramStringConstantTemplate<LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): StringConstant<LanguageTag> {
        return StringConstant(value)
    }
}

@Suppress("SERIALIZER_TYPE_INCOMPATIBLE")
@Serializable
class NumConstantTemplate<T : Number, LanguageTag>(@Serializable(with = NumConstantSerializer::class) val value: T) :
    ProgramNumberConstantTemplate<T, LanguageTag>() {
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

@Serializable
class RandomNumConstantTemplate<T : Number, LanguageTag>(
    val numType: NumType,
    val from: ProgramNumberConstantTemplate<T, LanguageTag>?,
    val until: ProgramNumberConstantTemplate<T, LanguageTag>?
) : ProgramNumberConstantTemplate<T, LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): NumConstant<LanguageTag> {
        val fromVal = from?.fillItem(random, attributes)?.value
        val untilVal = until?.fillItem(random, attributes)?.value
        return when (numType) {
            NumType.LONG -> NumConstant(random.nextLong(fromVal?.toLong(), untilVal?.toLong()))
            NumType.INT -> NumConstant(random.nextInt(fromVal?.toInt(), untilVal?.toInt()))
            NumType.FLOAT -> NumConstant(random.nextFloat())
            NumType.DOUBLE -> NumConstant(random.nextDouble(fromVal?.toDouble(), untilVal?.toDouble()))
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

@Serializable
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

@Serializable
class NumAttributeRefTemplate<T : Number, LanguageTag>(
    val numType: NumType,
    val key: String,
    val ind: ProgramNumberConstantTemplate<Int, LanguageTag>? = null
) : ProgramNumberConstantTemplate<T, LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): NumConstant<LanguageTag> {
        val indValue = ind?.fillItem(random, attributes)?.value?.toInt()
        val value = attributes.getValue(key)[indValue ?: 0]
        return when (numType) {
            NumType.LONG -> NumConstant(value.toLong())
            NumType.INT -> NumConstant(value.toInt())
            NumType.FLOAT -> NumConstant(value.toFloat())
            NumType.DOUBLE -> NumConstant(value.toDouble())
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

@Serializable
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