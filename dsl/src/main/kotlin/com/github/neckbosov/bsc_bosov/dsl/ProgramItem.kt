package com.github.neckbosov.bsc_bosov.dsl

import kotlin.random.Random

abstract class ProgramItem<LanguageTag>(val random: Random, val attributes: ProgramAttributes)