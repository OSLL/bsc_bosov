package com.github.neckbosov.bsc_bosov.dsl.template

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
        subclass(ProgramGlobalScopeTemplate.serializer(PolymorphicSerializer(Any::class)))
        subclass(ProgramLocalScopeTemplate.serializer(PolymorphicSerializer(Any::class)))
        subclass(FunctionalCallTemplate.serializer(PolymorphicSerializer(Any::class)))
        subclass(IfElseExpressionTemplate.serializer(PolymorphicSerializer(Any::class)))
        subclass(IfExpressionTemplate.serializer(PolymorphicSerializer(Any::class)))
        subclass(AssignmentTemplate.serializer(PolymorphicSerializer(Any::class)))
    }
    polymorphic(ProgramExpressionTemplate::class) {
        subclass(StringConstantTemplate.serializer(PolymorphicSerializer(Any::class)))
        subclass(NumConstantTemplate.serializer(PolymorphicSerializer(Any::class), PolymorphicSerializer(Any::class)))
        subclass(RandomStringConstantTemplate.serializer(PolymorphicSerializer(Any::class)))
        subclass(
            RandomNumConstantTemplate.serializer(
                PolymorphicSerializer(Any::class),
                PolymorphicSerializer(Any::class)
            )
        )
        subclass(StringAttributeRefTemplate.serializer(PolymorphicSerializer(Any::class)))
        subclass(
            NumAttributeRefTemplate.serializer(
                PolymorphicSerializer(Any::class),
                PolymorphicSerializer(Any::class)
            )
        )
    }
//    polymorphic(Number::class) {
//        subclass(Int.serializer())
//        subclass(Double.serializer())
//        subclass(Long.serializer())
//        subclass(Float.serializer())
//    }
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