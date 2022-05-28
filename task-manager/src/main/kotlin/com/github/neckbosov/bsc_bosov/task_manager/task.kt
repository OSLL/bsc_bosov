package com.github.neckbosov.bsc_bosov.task_manager

import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import com.github.neckbosov.bsc_bosov.dsl.tags.PythonTag
import com.github.neckbosov.bsc_bosov.dsl.template.ProgramTemplate
import com.github.neckbosov.bsc_bosov.task_manager.dsl.*

fun task(): ProgramTemplate<out ProgramLanguageTag> = ProgramTemplate<PythonTag> {
    val xVar = variable("x")
    val stringLen = randomNumConstant(10, 20)
    val randomString = randomStringConstant(stringLen)
    xVar setTo randomString
    `if`(funcCall("len", xVar) lt constant(15)) {
        addFuncCall("print", constant("small"))
    }.`else` {
        addFuncCall("print", constant("long"))
    }
}