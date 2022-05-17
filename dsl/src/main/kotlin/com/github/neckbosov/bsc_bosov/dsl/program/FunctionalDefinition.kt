package com.github.neckbosov.bsc_bosov.dsl.program

import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag

class FunctionalDefinition<LanguageTag : ProgramLanguageTag>(
    val name: String,
    val arguments: List<FunctionalArgument<LanguageTag>>,
    val scope: ProgramLocalScope<LanguageTag>,
    val returnTypeName: String?
) : ProgramInstruction<LanguageTag>()