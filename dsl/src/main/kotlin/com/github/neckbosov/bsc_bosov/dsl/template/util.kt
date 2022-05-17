package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.builtins.PairSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlin.random.Random

fun Random.nextLong(from: Long?, until: Long?): Long = if (from == null) {
    if (until == null) {
        this.nextLong()
    } else {
        this.nextLong(until)
    }
} else {
    if (until == null) {
        this.nextLong(from, Long.MAX_VALUE)
    } else {
        this.nextLong(from, until)
    }
}

fun Random.nextInt(from: Int?, until: Int?): Int = if (from == null) {
    if (until == null) {
        this.nextInt()
    } else {
        this.nextInt(until)
    }
} else {
    if (until == null) {
        this.nextInt(from, Int.MAX_VALUE)
    } else {
        this.nextInt(from, until)
    }
}

fun Random.nextDouble(from: Double?, until: Double?): Double = if (from == null) {
    if (until == null) {
        this.nextDouble()
    } else {
        this.nextDouble(until)
    }
} else {
    if (until == null) {
        this.nextDouble(from, Double.MAX_VALUE)
    } else {
        this.nextDouble(from, until)
    }
}

val dslModule = SerializersModule {
    polymorphic(ProgramInstructionTemplate::class) {
        subclass(ProgramGlobalScopeTemplate.serializer(ProgramLanguageTag.serializer()))
        subclass(ProgramLocalScopeTemplate.serializer(ProgramLanguageTag.serializer()))
        subclass(ProgramFunctionalScopeTemplate.serializer(ProgramLanguageTag.serializer()))
        subclass(FunctionalCallTemplate.serializer(ProgramLanguageTag.serializer()))
        subclass(IfElseExpressionTemplate.serializer(ProgramLanguageTag.serializer()))
        subclass(IfExpressionTemplate.serializer(ProgramLanguageTag.serializer()))
        subclass(AssignmentTemplate.serializer(ProgramLanguageTag.serializer()))
        subclass(RepeatGlobalScopeTemplate.serializer(ProgramLanguageTag.serializer()))
        subclass(RepeatLocalScopeTemplate.serializer(ProgramLanguageTag.serializer()))
        subclass(
            ForeachNumRepeatGlobalScopeTemplate.serializer(
                PolymorphicSerializer(Any::class),
                ProgramLanguageTag.serializer()
            )
        )
        subclass(
            ForeachNumRepeatLocalScopeTemplate.serializer(
                PolymorphicSerializer(Any::class),
                ProgramLanguageTag.serializer()
            )
        )
        subclass(ForeachStringRepeatGlobalScopeTemplate.serializer(ProgramLanguageTag.serializer()))
        subclass(ForeachStringRepeatLocalScopeTemplate.serializer(ProgramLanguageTag.serializer()))
        subclass(VariableDefinitionTemplate.serializer(ProgramLanguageTag.serializer()))
        subclass(FunctionalDefinitionTemplate.serializer(ProgramLanguageTag.serializer()))
    }
    polymorphic(ProgramExpressionTemplate::class) {
        subclass(StringConstantTemplate.serializer(ProgramLanguageTag.serializer()))
        subclass(NumConstantTemplate.serializer(PolymorphicSerializer(Any::class), ProgramLanguageTag.serializer()))
        subclass(RandomStringConstantTemplate.serializer(ProgramLanguageTag.serializer()))
        subclass(
            RandomNumConstantTemplate.serializer(
                PolymorphicSerializer(Any::class),
                ProgramLanguageTag.serializer()
            )
        )
        subclass(StringAttributeRefTemplate.serializer(ProgramLanguageTag.serializer()))
        subclass(
            NumAttributeRefTemplate.serializer(
                PolymorphicSerializer(Any::class),
                ProgramLanguageTag.serializer()
            )
        )
        subclass(VariableTemplate.serializer(ProgramLanguageTag.serializer()))
    }
}

class NumConstantSerializer : KSerializer<Number> {
    private val pairSerializer = PairSerializer(NumType.serializer(), String.serializer())
    override fun deserialize(decoder: Decoder): Number {
        val p = pairSerializer.deserialize(decoder)
        return when (p.first) {
            NumType.LONG -> p.second.toLong()
            NumType.INT -> p.second.toInt()
            NumType.FLOAT -> p.second.toFloat()
            NumType.DOUBLE -> p.second.toDouble()
        }
    }

    override val descriptor: SerialDescriptor = pairSerializer.descriptor

    override fun serialize(encoder: Encoder, value: Number) {
        val p = Pair(
            when (value) {
                is Long -> NumType.LONG
                is Int -> NumType.INT
                is Float -> NumType.FLOAT
                is Double -> NumType.DOUBLE
                else -> error("Unsupported constant type")
            },
            value.toString()
        )
        pairSerializer.serialize(encoder, p)
    }
}