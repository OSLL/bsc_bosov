package com.github.neckbosov.bsc_bosov.dsl

import kotlin.random.Random

class Assignment<LanguageTag>(
    random: Random,
    attributes: ProgramAttributes,
    val lhs: Variable<LanguageTag>,
    val rhs: ProgramExpression<LanguageTag>
) : ProgramItem<LanguageTag>(random, attributes) {
}

fun <LanguageTag> ProgramScope<LanguageTag>.addAssignment(
    lhs: Variable<LanguageTag>,
    rhs: ProgramExpression<LanguageTag>
) {
    this.items.add(Assignment(this.random, this.attributes, lhs, rhs))
}