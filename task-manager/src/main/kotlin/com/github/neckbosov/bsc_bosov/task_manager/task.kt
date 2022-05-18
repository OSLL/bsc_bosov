package com.github.neckbosov.bsc_bosov.task_manager

import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import com.github.neckbosov.bsc_bosov.dsl.tags.PythonTag
import com.github.neckbosov.bsc_bosov.dsl.template.*

fun task(): ProgramTemplate<out ProgramLanguageTag> = ProgramTemplate<PythonTag> {
    val xVar = variable("x")
    val stringLen = randomNumConstant(10, 20)
    val randomString = randomStringConstant(stringLen)
    addAssignment(xVar, randomString)
    addIfElseInstr(funcCall("len", xVar) opLt constant(15)) {
        addFuncCall("print", constant("small"))
    }.addElse {
        addFuncCall("print", constant("long"))
    }
}