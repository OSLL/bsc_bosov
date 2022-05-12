package com.github.neckbosov.bsc_bosov.code_mapper

import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import com.github.neckbosov.bsc_bosov.dsl.tags.PythonTag

val codeMappers: Map<ProgramLanguageTag, CodeMapper<out ProgramLanguageTag>> = mapOf(
    PythonTag to PythonMapper
)

@Suppress("UNCHECKED_CAST")
inline fun <reified LanguageTag : ProgramLanguageTag> getMapper(tag: LanguageTag): CodeMapper<LanguageTag> =
    codeMappers[tag] as CodeMapper<LanguageTag>
