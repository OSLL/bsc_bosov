package com.github.neckbosov.bsc_bosov.tasks

import com.github.neckbosov.bsc_bosov.dsl.*
import com.github.neckbosov.bsc_bosov.dsl.tags.PythonTag

fun task1(seed: Long, attributes: ProgramAttributes) = Program<PythonTag>(seed, attributes) {
    val xVar = Variable<PythonTag>("x")
    val stringLen = getRandomInt(10, 20)
    val randomString = getRandomString(stringLen)
    addAssignment(xVar, constantExpr(randomString))
    addIfElseExpr {
        addIf(funcCall("len", VariableExpr(xVar)) opLess constantExpr(15)) {
            addFuncCall("print", constantExpr("small"))
        }
        addElse {
            addFuncCall("print", constantExpr("long"))
        }
    }
}