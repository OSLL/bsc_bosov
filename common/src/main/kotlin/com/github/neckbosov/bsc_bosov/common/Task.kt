package com.github.neckbosov.bsc_bosov.common

import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import com.github.neckbosov.bsc_bosov.dsl.template.ProgramTemplate

@kotlinx.serialization.Serializable
data class Task(
    val taskName: String,
    val tag: ProgramLanguageTag,
    val programTemplate: ProgramTemplate<out ProgramLanguageTag>
)
