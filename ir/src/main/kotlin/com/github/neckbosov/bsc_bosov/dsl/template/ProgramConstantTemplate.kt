@file:Suppress("unused")

package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.program.Constant
import com.github.neckbosov.bsc_bosov.dsl.program.NumConstant
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import com.github.neckbosov.bsc_bosov.dsl.program.StringConstant
import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
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

inline fun <reified T> getConstantType(): NumType {
    return when (T::class) {
        Long::class -> NumType.LONG
        Int::class -> NumType.INT
        Float::class -> NumType.FLOAT
        Double::class -> NumType.DOUBLE
        else -> error("Unsupported constant type")
    }
}

@Serializable
sealed class ProgramConstantTemplate<LanguageTag : ProgramLanguageTag> : ProgramExpressionTemplate<LanguageTag>() {
    abstract override fun fillItem(random: Random, attributes: ProgramAttributes): Constant<LanguageTag>
}

@Serializable
sealed class ProgramNumberConstantTemplate<T : Number, LanguageTag : ProgramLanguageTag> :
    ProgramConstantTemplate<LanguageTag>() {
    abstract override fun fillItem(random: Random, attributes: ProgramAttributes): NumConstant<T, LanguageTag>
}

@Serializable
sealed class ProgramStringConstantTemplate<LanguageTag : ProgramLanguageTag> : ProgramConstantTemplate<LanguageTag>() {
    abstract override fun fillItem(random: Random, attributes: ProgramAttributes): StringConstant<LanguageTag>
}

@Serializable
class StringConstantTemplate<LanguageTag : ProgramLanguageTag>(val value: String) :
    ProgramStringConstantTemplate<LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): StringConstant<LanguageTag> {
        return StringConstant(value)
    }
}

@Suppress("SERIALIZER_TYPE_INCOMPATIBLE")
@Serializable
class NumConstantTemplate<T : Number, LanguageTag : ProgramLanguageTag>(
    @Serializable(with = NumConstantSerializer::class) val value: T
) :
    ProgramNumberConstantTemplate<T, LanguageTag>() {

    override fun fillItem(random: Random, attributes: ProgramAttributes): NumConstant<T, LanguageTag> {
        return NumConstant(value)
    }
}


@Suppress("UNCHECKED_CAST")
@Serializable
class RandomNumConstantTemplate<T : Number, LanguageTag : ProgramLanguageTag>(
    val numType: NumType,
    val from: ProgramNumberConstantTemplate<T, LanguageTag>?,
    val until: ProgramNumberConstantTemplate<T, LanguageTag>?
) : ProgramNumberConstantTemplate<T, LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): NumConstant<T, LanguageTag> {
        val fromVal = from?.fillItem(random, attributes)?.value
        val untilVal = until?.fillItem(random, attributes)?.value
        return when (numType) {
            NumType.LONG -> NumConstant(random.nextLong(fromVal?.toLong(), untilVal?.toLong()) as T)
            NumType.INT -> NumConstant(random.nextInt(fromVal?.toInt(), untilVal?.toInt()) as T)
            NumType.FLOAT -> NumConstant(random.nextFloat() as T)
            NumType.DOUBLE -> NumConstant(random.nextDouble(fromVal?.toDouble(), untilVal?.toDouble()) as T)
        }
    }
}


@Serializable
class RandomStringConstantTemplate<LanguageTag : ProgramLanguageTag>(
    val length: ProgramNumberConstantTemplate<Int, LanguageTag>
) :
    ProgramStringConstantTemplate<LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): StringConstant<LanguageTag> {
        val lengthValue = length.fillItem(random, attributes).value
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9') + ",.[]<>/?()*&^%_- ".toCharArray().toList()
        return (1..lengthValue)
            .map { allowedChars.random(random) }
            .joinToString("")
            .let { StringConstant(it) }
    }
}

@Suppress("UNCHECKED_CAST")
@Serializable
class NumAttributeRefTemplate<T : Number, LanguageTag : ProgramLanguageTag>(
    val numType: NumType,
    val key: String,
    val ind: ProgramNumberConstantTemplate<Int, LanguageTag>? = null
) : ProgramNumberConstantTemplate<T, LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): NumConstant<T, LanguageTag> {
        val indValue = ind?.fillItem(random, attributes)?.value
        val value = attributes.getValue(key)[indValue ?: 0]

        return when (numType) {
            NumType.LONG -> NumConstant(value.toLong() as T)
            NumType.INT -> NumConstant(value.toInt() as T)
            NumType.FLOAT -> NumConstant(value.toFloat() as T)
            NumType.DOUBLE -> NumConstant(value.toDouble() as T)
        }
    }
}


@Serializable
class StringAttributeRefTemplate<LanguageTag : ProgramLanguageTag>(
    val key: String,
    val ind: ProgramNumberConstantTemplate<Int, LanguageTag>? = null
) :
    ProgramStringConstantTemplate<LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): StringConstant<LanguageTag> {
        val indValue = ind?.fillItem(random, attributes)?.value
        val value = attributes.getValue(key)[indValue ?: 0]
        return StringConstant(value)
    }
}

@Serializable
class WrappedMutableStringConstant<LanguageTag : ProgramLanguageTag>(
    var constant: ProgramStringConstantTemplate<LanguageTag>?
) : ProgramStringConstantTemplate<LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): StringConstant<LanguageTag> {
        return constant!!.fillItem(random, attributes)
    }

}

@Serializable
class WrappedMutableNumConstant<T : Number, LanguageTag : ProgramLanguageTag>(
    var constant: ProgramNumberConstantTemplate<T, LanguageTag>?
) : ProgramNumberConstantTemplate<T, LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): NumConstant<T, LanguageTag> {
        return constant!!.fillItem(random, attributes)
    }

}


@Serializable
class RandomChoiceNumConstant<T : Number, LanguageTag : ProgramLanguageTag>(
    val items: ProgramNumberConstantListTemplate<T, LanguageTag>
) : ProgramNumberConstantTemplate<T, LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): NumConstant<T, LanguageTag> {
        val itemValues = items.fillItem(random, attributes)
        val item = itemValues.numbers.random(random)
        return NumConstant(item)
    }
}

fun <T : Number, LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.randomChoiceNum(
    items: ProgramNumberConstantListTemplate<T, LanguageTag>
): RandomChoiceNumConstant<T, LanguageTag> = RandomChoiceNumConstant(items)

@Serializable
class RandomChoiceStringConstant<LanguageTag : ProgramLanguageTag>(
    val items: ProgramStringConstantListTemplate<LanguageTag>
) : ProgramStringConstantTemplate<LanguageTag>() {
    override fun fillItem(random: Random, attributes: ProgramAttributes): StringConstant<LanguageTag> {
        val itemValues = items.fillItem(random, attributes)
        val item = itemValues.strings.random(random)
        return StringConstant(item)
    }
}
