@file:Suppress("unused")

package com.github.neckbosov.bsc_bosov.task_manager.dsl

import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import com.github.neckbosov.bsc_bosov.dsl.template.*

inline fun <LanguageTag : ProgramLanguageTag, reified T : Number> ProgramScopeTemplate<LanguageTag>.constant(value: T): NumConstantTemplate<T, LanguageTag> {
    return NumConstantTemplate(value)
}

fun <LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.constant(value: String): StringConstantTemplate<LanguageTag> {
    return StringConstantTemplate(value)
}

@Suppress("unused")
inline fun <reified T : Number, LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.randomNumConstant(
    from: T? = null,
    until: T? = null
): RandomNumConstantTemplate<T, LanguageTag> =
    RandomNumConstantTemplate(
        getConstantType<T>(),
        from?.let { NumConstantTemplate(it) },
        until?.let { NumConstantTemplate(it) })


@Suppress("unused")
inline fun <reified T : Number, LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.randomNumConstant(
    from: NumConstantTemplate<T, LanguageTag>? = null,
    until: NumConstantTemplate<T, LanguageTag>? = null
): RandomNumConstantTemplate<T, LanguageTag> = RandomNumConstantTemplate(getConstantType<T>(), from, until)

@Suppress("unused")
fun <LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.randomFloatConstant(): RandomNumConstantTemplate<Float, LanguageTag> =
    RandomNumConstantTemplate(getConstantType<Float>(), null, null)

@Suppress("unused")
fun <LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.randomStringConstant(length: Int): RandomStringConstantTemplate<LanguageTag> =
    RandomStringConstantTemplate(NumConstantTemplate(length))

@Suppress("unused")
fun <LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.randomStringConstant(length: ProgramNumberConstantTemplate<Int, LanguageTag>): RandomStringConstantTemplate<LanguageTag> =
    RandomStringConstantTemplate(length)

@Suppress("unused")
inline fun <reified T : Number, LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.numAttributeRef(
    key: String,
    ind: Int? = null
): NumAttributeRefTemplate<T, LanguageTag> =
    NumAttributeRefTemplate(getConstantType<T>(), key, ind?.let { NumConstantTemplate(it) })

@Suppress("unused")
inline fun <reified T : Number, LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.numAttributeRef(
    key: String,
    ind: ProgramNumberConstantTemplate<Int, LanguageTag>? = null
): NumAttributeRefTemplate<T, LanguageTag> = NumAttributeRefTemplate(getConstantType<T>(), key, ind)

@Suppress("unused")
fun <LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.stringAttributeRef(
    key: String,
    ind: Int? = null
): StringAttributeRefTemplate<LanguageTag> =
    StringAttributeRefTemplate(key, ind?.let { NumConstantTemplate(it) })

@Suppress("unused")
fun <LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.stringAttributeRef(
    key: String,
    ind: ProgramNumberConstantTemplate<Int, LanguageTag>? = null
): StringAttributeRefTemplate<LanguageTag> = StringAttributeRefTemplate(key, ind)

fun <LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.randomChoiceString(
    items: ProgramStringConstantListTemplate<LanguageTag>
): RandomChoiceStringConstant<LanguageTag> = RandomChoiceStringConstant(items)
