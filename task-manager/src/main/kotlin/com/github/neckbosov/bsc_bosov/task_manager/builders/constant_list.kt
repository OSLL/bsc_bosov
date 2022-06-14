@file:Suppress("unused")

package com.github.neckbosov.bsc_bosov.task_manager.builders

import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import com.github.neckbosov.bsc_bosov.dsl.template.*

inline fun <LanguageTag : ProgramLanguageTag, reified T> ProgramScopeTemplate<LanguageTag>.constantList(vararg values: T): ProgramConstantListTemplate<LanguageTag> {
    return when (values.first()) {
        is Number -> NumConstantListTemplate(getConstantType<T>(), values.map {
            it as Number
            NumConstantTemplate(it)
        })
        is String -> StringConstantListTemplate(values.map {
            it as String
            StringConstantTemplate(it)
        })
        else -> error("Not supported constant type")
    }
}

inline fun <LanguageTag : ProgramLanguageTag, reified T : Number> ProgramScopeTemplate<LanguageTag>.constantList(vararg values: ProgramNumberConstantTemplate<T, LanguageTag>): ProgramConstantListTemplate<LanguageTag> {
    return NumConstantListTemplate(getConstantType<T>(), values.toList())
}

fun <LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.constantList(vararg values: ProgramStringConstantTemplate<LanguageTag>): ProgramConstantListTemplate<LanguageTag> {
    return StringConstantListTemplate(values.toList())
}

inline fun <reified T : Number, LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.numAttributeRefList(
    key: String
): NumAttributeRefListTemplate<T, LanguageTag> = NumAttributeRefListTemplate(getConstantType<T>(), key)

fun <LanguageTag : ProgramLanguageTag> ProgramScopeTemplate<LanguageTag>.stringAttributeRefList(
    key: String
): StringAttributeRefListTemplate<LanguageTag> = StringAttributeRefListTemplate(key)