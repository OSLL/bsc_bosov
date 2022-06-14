package com.github.neckbosov.bsc_bosov.dsl.tags

@kotlinx.serialization.Serializable
object CppTag : ProgramLanguageTag() {
    override val tagName: String = "cpp"
}

