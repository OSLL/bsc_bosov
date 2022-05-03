package com.github.neckbosov.bsc_bosov.tasks

import com.github.neckbosov.bsc_bosov.dsl.tags.PythonTag
import com.github.neckbosov.bsc_bosov.dsl.template.*

fun task1() = ProgramTemplate<PythonTag> {
    val xVar = variable("x")
    val stringLen = randomNumConstant(10, 20)
    val randomString = randomStringConstant(stringLen)
    addAssignment(xVar, randomString)
    addIfElseExpr(funcCall("len", xVar) opLt constant(15)) {
        addFuncCall("print", constant("small"))
    }.addElse {
        addFuncCall("print", constant("long"))
    }
}