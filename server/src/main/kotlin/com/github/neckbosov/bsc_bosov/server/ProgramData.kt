package com.github.neckbosov.bsc_bosov.server

import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes

data class ProgramData(
    val parameters: ProgramAttributes,
    val image: ByteArray,
    val codeText: String,
    val answer: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProgramData

        if (parameters != other.parameters) return false
        if (codeText != other.codeText) return false

        return true
    }

    override fun hashCode(): Int {
        var result = parameters.hashCode()
        result = 31 * result + codeText.hashCode()
        return result
    }
}
