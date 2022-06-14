package com.github.neckbosov.bsc_bosov.code_mapper

import com.github.neckbosov.bsc_bosov.dsl.program.*
import com.github.neckbosov.bsc_bosov.dsl.tags.PythonTag

object PythonMapper : CodeMapper<PythonTag> {

    private fun generateConstantExprString(c: Constant<in PythonTag>): String = when (c) {
        is NumConstant<*, in PythonTag> -> c.value.toString()
        is StringConstant -> "\"${c.value}\""
    }

    private fun generateNumListString(expr: NumConstantList<*, in PythonTag>): String {
        val itemsString = expr.numbers.joinToString(separator = ", ") { generateConstantExprString(NumConstant(it)) }
        return "[$itemsString]"
    }

    private fun generateStringListString(expr: StringConstantList<in PythonTag>): String {
        val itemsString = expr.strings.joinToString(separator = ", ") { generateConstantExprString(StringConstant(it)) }
        return "[$itemsString]"
    }

    private fun generateExpressionString(expr: ProgramExpression<in PythonTag>): String {

        return when (expr) {
            is Constant -> generateConstantExprString(expr)
            is Variable -> expr.name
            is BinOpExpr -> "${generateExpressionString(expr.lhs)} ${expr.op} ${generateExpressionString(expr.rhs)}"
            is FunctionalCallExpr -> generateFuncCallCode(expr)
            is NumConstantList<*, in PythonTag> -> generateNumListString(expr)
            is StringConstantList -> generateStringListString(expr)
            is BracketsExpr -> "(${generateExpressionString(expr.innerExpr)})"
        }
    }

    private fun generateAssignmentCode(assignment: Assignment<in PythonTag>): String {
        val rhsString = generateExpressionString(assignment.rhs)
        return "${assignment.lhs.name} = $rhsString"
    }

    private fun generateMainCode(main: Main<in PythonTag>): String {
        return buildString {
            appendLine("def main():")
            appendLine(generateScopeCode(main.scope).prependIndent("    "))
            appendLine()
            appendLine("if __name__ == '__main__':")
            appendLine("main()".prependIndent("    "))
        }
    }

    private fun generateScopeCode(scope: ProgramScope<in PythonTag>): String {
        return buildString {
            for (item in scope.items) {
                appendLine(
                    when (item) {
                        is Assignment<in PythonTag> -> generateAssignmentCode(item)
                        is FunctionalCall<in PythonTag> -> generateFuncCallCode(item.functionalCallExpr)
                        is IfInstruction<in PythonTag> -> generateIfExprCode(item)
                        is IfElseInstruction<in PythonTag> -> generateIfElseExprCode(item)
                        is FunctionalDefinition<in PythonTag> -> generateFuncDefCode(item)
                        is VariableDefinition<in PythonTag> -> generateVarDefCode(item)
                        is WhileInstruction<in PythonTag> -> generateWhileCode(item)
                        is Main<in PythonTag> -> generateMainCode(item)
                        else -> error("Instruction not supported yet")
                    }
                )
            }
        }
    }

    private fun generateIfElseExprCode(item: IfElseInstruction<in PythonTag>): String {
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

    override fun generateCode(program: Program<in PythonTag>): String {
        return generateScopeCode(program.scope) + "\n"
    }

    private fun generateIfExprCode(item: IfInstruction<in PythonTag>): String {
        val blockString = generateScopeCode(item.block).prependIndent("    ")
        return "if ${generateExpressionString(item.cond)}:\n$blockString"
    }

    private fun generateFuncCallCode(functionalCall: FunctionalCallExpr<in PythonTag>): String {
        val paramsString = functionalCall.params.joinToString(separator = ", ") { generateExpressionString(it) }
        return "${functionalCall.functionName}($paramsString)"
    }

    private fun generateFunctionalArgumentCode(argument: FunctionalArgument<in PythonTag>): String {
        return buildString {
            append(argument.variable.name)
            if (argument.typeName != null) {
                append(":${argument.typeName}")
            }
            val initValue = argument.initialValue
            if (initValue != null) {
                val exprString = generateExpressionString(initValue)
                append(" = $exprString")
            }
        }
    }

    private fun generateFuncDefCode(functionalDefinition: FunctionalDefinition<in PythonTag>): String {
        val blockString = generateScopeCode(functionalDefinition.scope).prependIndent("    ")
        val argumentsString = functionalDefinition.arguments.joinToString(separator = ", ") {
            generateFunctionalArgumentCode(it)
        }
        return buildString {
            append("def ${functionalDefinition.name}($argumentsString):")
            if (functionalDefinition.returnTypeName != null) {
                append(" -> ${functionalDefinition.returnTypeName}")
            }
            appendLine()
            appendLine(blockString)
        }
    }

    private fun generateVarDefCode(variableDefinition: VariableDefinition<in PythonTag>): String {
        val typeName = variableDefinition.typeName
        val initValue = variableDefinition.initialValue
        if (typeName == null && initValue == null) {
            error("Incorrect variable definition in python")
        }
        return buildString {
            append(variableDefinition.variable.name)
            if (typeName != null) {
                append(": $typeName")
            }
            if (initValue != null) {
                val exprString = generateExpressionString(initValue)
                append(" = $exprString")
            }
        }
    }

    private fun generateWhileCode(whileInstruction: WhileInstruction<in PythonTag>): String {
        val condString = generateExpressionString(whileInstruction.cond)
        val scopeString = generateScopeCode(whileInstruction.scope).prependIndent("    ")
        return "while($condString):\n$scopeString\n"
    }
}