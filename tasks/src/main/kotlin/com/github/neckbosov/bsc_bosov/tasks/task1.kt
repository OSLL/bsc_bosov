package com.github.neckbosov.bsc_bosov.tasks

import com.github.neckbosov.bsc_bosov.dsl.*
import com.github.neckbosov.bsc_bosov.dsl.tags.PythonTag

fun task1(seed: Long, attributes: ProgramAttributes) = Program<PythonTag>(seed, attributes) {
    val xVar = Variable<PythonTag>("x")
    addAssignment(xVar, ConstantExpr(""))
    addIfElseExpr {
        addIf(VariableExpr(xVar)) {
            funcCall("print", ConstantExpr("true"))
        }
        addElse {
            funcCall("print", ConstantExpr("false"))
        }
    }
}