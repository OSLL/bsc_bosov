package com.github.neckbosov.bsc_bosov.dsl

sealed class Constant
class StringConstant(val value: String) : Constant()
class NumConstant(val value: Number) : Constant()

fun <T> constant(value: T): Constant {
    return when (value) {
        is Number -> NumConstant(value)
        is String -> StringConstant(value)
        else -> error("This constant type is not supported yet")
    }
}