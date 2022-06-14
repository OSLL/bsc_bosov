package com.github.neckbosov.bsc_bosov.code_mapper

import com.github.neckbosov.bsc_bosov.dsl.program.*
import com.github.neckbosov.bsc_bosov.dsl.tags.CppTag
import com.github.neckbosov.bsc_bosov.dsl.template.NumType

object CppMapper : CodeMapper<CppTag> {

    private fun generateConstantExprString(c: Constant<in CppTag>): String = when (c) {
        is NumConstant<*, in CppTag> -> c.value.toString()
        is StringConstant -> "\"${c.value}\""
    }

    private fun generateNumListString(expr: NumConstantList<*, in CppTag>): String {
        val itemsString = expr.numbers.joinToString(separator = ", ") { generateConstantExprString(NumConstant(it)) }
        val typeName = when (expr.numType) {
            NumType.LONG -> "long long int"
            NumType.INT -> "int"
            NumType.FLOAT -> "float"
            NumType.DOUBLE -> "double"
        }
        return "vector<$typeName>{$itemsString}"
    }

    private fun generateStringListString(expr: StringConstantList<in CppTag>): String {
        val itemsString = expr.strings.joinToString(separator = ", ") { generateConstantExprString(StringConstant(it)) }
        return "vector<string>{$itemsString}"
    }

    private fun generateExpressionString(expr: ProgramExpression<in CppTag>): String {

        return when (expr) {
            is Constant -> generateConstantExprString(expr)
            is Variable -> expr.name
            is BinOpExpr -> "${generateExpressionString(expr.lhs)} ${expr.op} ${generateExpressionString(expr.rhs)}"
            is FunctionalCallExpr -> generateFuncCallCode(expr)
            is NumConstantList<*, in CppTag> -> generateNumListString(expr)
            is StringConstantList -> generateStringListString(expr)
            is BracketsExpr -> "(${generateExpressionString(expr.innerExpr)})"
        }
    }

    private fun generateAssignmentCode(assignment: Assignment<in CppTag>): String {
        val rhsString = generateExpressionString(assignment.rhs)
        return "${assignment.lhs.name} = $rhsString;"
    }

    private fun generateMainCode(main: Main<in CppTag>): String {
        return buildString {
            appendLine("int main(int argc, char** argv) {")
            appendLine(generateScopeCode(main.scope).prependIndent("    "))
            appendLine("}")
        }
    }

    private fun generateScopeCode(scope: ProgramScope<in CppTag>): String {
        return buildString {
            for (item in scope.items) {
                appendLine(
                    when (item) {
                        is Assignment<in CppTag> -> generateAssignmentCode(item)
                        is FunctionalCall<in CppTag> -> generateFuncCallCode(item.functionalCallExpr) + ";"
                        is IfInstruction<in CppTag> -> generateIfExprCode(item)
                        is IfElseInstruction<in CppTag> -> generateIfElseExprCode(item)
                        is FunctionalDefinition<in CppTag> -> generateFuncDefCode(item)
                        is VariableDefinition<in CppTag> -> generateVarDefCode(item)
                        is WhileInstruction<in CppTag> -> generateWhileCode(item)
                        is Main<in CppTag> -> generateMainCode(item)
                        else -> error("Instruction not supported yet")
                    }
                )
            }
        }
    }

    private fun generateIfElseExprCode(item: IfElseInstruction<in CppTag>): String {
        val mainBlockString = generateScopeCode(item.block).prependIndent("    ")
        return buildString {
            appendLine("if (${generateExpressionString(item.cond)}) {\n$mainBlockString}".trimEnd())
            for (elifBlock in item.elifBlocks) {
                val blockString = generateScopeCode(elifBlock.block).prependIndent("    ")
                appendLine("else if (${generateExpressionString(elifBlock.cond)}){\n$blockString}")
            }
            val elseBlock = item.elseBlock
            if (elseBlock != null) {
                val blockString = generateScopeCode(elseBlock).prependIndent("    ")
                appendLine("else{\n$blockString}")
            }
        }
    }

    override fun generateCode(program: Program<in CppTag>): String {
        return buildString {
            appendLine("#include<bits/stdc++.h>")
            appendLine("using namespace std;")
            appendLine(generateScopeCode(program.scope))
        }
    }

    private fun generateIfExprCode(item: IfInstruction<in CppTag>): String {
        val blockString = generateScopeCode(item.block).prependIndent("    ")
        return "if (${generateExpressionString(item.cond)}){\n$blockString}\n"
    }

    private fun generateFuncCallCode(functionalCall: FunctionalCallExpr<in CppTag>): String {
        val paramsString = functionalCall.params.joinToString(separator = ", ") { generateExpressionString(it) }
        return "${functionalCall.functionName}($paramsString)"
    }

    private fun generateFunctionalArgumentCode(argument: FunctionalArgument<in CppTag>): String {
        return buildString {
            append("${argument.typeName} ${argument.variable.name}")
            val initValue = argument.initialValue
            if (initValue != null) {
                val exprString = generateExpressionString(initValue)
                append(" = $exprString")
            }
        }
    }

    private fun generateFuncDefCode(functionalDefinition: FunctionalDefinition<in CppTag>): String {
        val blockString = generateScopeCode(functionalDefinition.scope).prependIndent("    ")
        val argumentsString = functionalDefinition.arguments.joinToString(separator = ", ") {
            generateFunctionalArgumentCode(it)
        }
        return buildString {
            append("${functionalDefinition.returnTypeName} ${functionalDefinition.name}($argumentsString){")
            appendLine(blockString)
            appendLine("}")
        }
    }

    private fun generateVarDefCode(variableDefinition: VariableDefinition<in CppTag>): String {
        val typeName = variableDefinition.typeName
        val initValue = variableDefinition.initialValue
        if (typeName == null && initValue == null) {
            error("Incorrect variable definition in cpp")
        }
        return buildString {
            if (typeName == null) {
                append("auto ")
            } else {
                append("$typeName ")
            }
            append(variableDefinition.variable.name)
            if (initValue != null) {
                val exprString = generateExpressionString(initValue)
                append(" = $exprString")
            }
            appendLine(";")
        }
    }

    private fun generateWhileCode(whileInstruction: WhileInstruction<in CppTag>): String {
        val condString = generateExpressionString(whileInstruction.cond)
        val scopeString = generateScopeCode(whileInstruction.scope).prependIndent("    ")
        return "while($condString){\n$scopeString\n}\n"
    }
}