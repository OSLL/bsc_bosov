package com.github.neckbosov.bsc_bosov.code_mapper

import com.github.neckbosov.bsc_bosov.dsl.program.*
import com.github.neckbosov.bsc_bosov.dsl.tags.PythonTag

object PythonMapper : CodeMapper<PythonTag> {

    private fun generateConstantExprString(c: Constant<PythonTag>): String = when (c) {
        is NumConstant<*, PythonTag> -> c.value.toString()
        is StringConstant -> "\"${c.value}\""
    }

    private fun generateNumListString(expr: NumConstantList<*, PythonTag>): String {
        val itemsString = expr.numbers.joinToString(separator = ", ") { generateConstantExprString(NumConstant(it)) }
        return "list($itemsString)"
    }

    private fun generateStringListString(expr: StringConstantList<PythonTag>): String {
        val itemsString = expr.strings.joinToString(separator = ", ") { generateConstantExprString(StringConstant(it)) }
        return "list($itemsString)"
    }

    private fun generateExpressionString(expr: ProgramExpression<PythonTag>): String {
        return when (expr) {
            is Constant -> generateConstantExprString(expr)
            is Variable -> expr.name
            is BinOpExpr -> "${generateExpressionString(expr.lhs)} ${expr.op} ${generateExpressionString(expr.rhs)}"
            is FunctionalCallExpr -> generateFuncCallCode(expr)
            is NumConstantList<*, PythonTag> -> generateNumListString(expr)
            is StringConstantList -> generateStringListString(expr)
        }
    }

    private fun generateAssignmentCode(assignment: Assignment<PythonTag>): String {
        val rhsString = generateExpressionString(assignment.rhs)
        return "${assignment.lhs.name} = $rhsString"
    }

    private fun generateScopeCode(scope: ProgramScope<PythonTag>): String {
        return buildString {
            for (item in scope.items) {
                appendLine(
                    when (item) {
                        is Assignment<PythonTag> -> generateAssignmentCode(item)
                        is FunctionalCall<PythonTag> -> generateFuncCallCode(item.functionalCallExpr)
                        is IfExpression<PythonTag> -> generateIfExprCode(item)
                        is IfElseExpression<PythonTag> -> generateIfElseExprCode(item)
                        else -> error("Instruction not supported yet")
                    }
                )
            }
        }
    }

    private fun generateIfElseExprCode(item: IfElseExpression<PythonTag>): String {
        val mainBlockString = generateScopeCode(item.block).prependIndent("    ")
        return buildString {
            appendLine("if ${generateExpressionString(item.cond)}:\n$mainBlockString".trimEnd())
            for (elifBlock in item.elifBlocks) {
                val blockString = generateScopeCode(elifBlock.block).prependIndent("    ")
                appendLine("elif ${generateExpressionString(elifBlock.cond)}:\n$blockString")
            }
            val elseBlock = item.elseBlock
            if (elseBlock != null) {
                val blockString = generateScopeCode(elseBlock).prependIndent("    ")
                appendLine("else:\n$blockString")
            }
        }
    }

    override fun generateCode(program: Program<PythonTag>): String {
        return generateScopeCode(program.scope) + "\n"
    }

    private fun generateIfExprCode(item: IfExpression<PythonTag>): String {
        val blockString = generateScopeCode(item.block).prependIndent("    ")
        return "if ${generateExpressionString(item.cond)}:\n$blockString"
    }

    private fun generateFuncCallCode(functionalCall: FunctionalCallExpr<PythonTag>): String {
        val paramsString = functionalCall.params.joinToString(separator = ", ") { generateExpressionString(it) }
        return "${functionalCall.functionName}($paramsString)"
    }
}