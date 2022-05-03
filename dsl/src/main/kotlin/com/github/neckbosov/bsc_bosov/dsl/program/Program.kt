package com.github.neckbosov.bsc_bosov.dsl.program

typealias ProgramAttributes = Map<String, List<String>>

class Program<LanguageTag>(val scope: ProgramGlobalScope<LanguageTag>)