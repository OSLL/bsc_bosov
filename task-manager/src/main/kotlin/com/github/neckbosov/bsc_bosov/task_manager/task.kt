package com.github.neckbosov.bsc_bosov.task_manager

import com.github.neckbosov.bsc_bosov.dsl.tags.CppTag
import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import com.github.neckbosov.bsc_bosov.dsl.template.ProgramTemplate
import com.github.neckbosov.bsc_bosov.task_manager.builders.*

fun task(): ProgramTemplate<out ProgramLanguageTag> = ProgramTemplate<CppTag> {
    mainFun {
        val xVar = variable("x")
        val stringLen = randomNumConstant(10, 20)
        val randomString = randomStringConstant(stringLen)
        addVarDef(xVar, "string", randomString)
        `if`(funcCall("size", xVar) lt constant(15)) {
            addFuncCall("printf", constant("small"))
        }.`else` {
            addFuncCall("printf", constant("long"))
        }
    }
}