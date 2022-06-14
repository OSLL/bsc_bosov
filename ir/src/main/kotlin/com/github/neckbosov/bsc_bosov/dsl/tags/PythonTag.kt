package com.github.neckbosov.bsc_bosov.dsl.tags

import com.github.neckbosov.bsc_bosov.dsl.features.DymamicTyping
import com.github.neckbosov.bsc_bosov.dsl.features.GlobalInstructions
import kotlinx.serialization.Serializable

@Serializable
object PythonTag : GlobalInstructions, DymamicTyping, ProgramLanguageTag() {
    override val tagName: String = "python"
}