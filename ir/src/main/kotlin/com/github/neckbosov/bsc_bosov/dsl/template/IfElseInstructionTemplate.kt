@file:Suppress("unused")

package com.github.neckbosov.bsc_bosov.dsl.template

import com.github.neckbosov.bsc_bosov.dsl.program.IfElseInstruction
import com.github.neckbosov.bsc_bosov.dsl.program.IfInstruction
import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
class IfInstructionTemplate<LanguageTag : ProgramLanguageTag>(
    val cond: ProgramExpressionTemplate<LanguageTag>,
    val block: ProgramLocalScopeTemplate<LanguageTag>
) : ProgramInstructionTemplate<LanguageTag> {
    override fun fillItem(random: Random, attributes: ProgramAttributes): IfInstruction<LanguageTag> {
        return IfInstruction(
            cond.fillItem(random, attributes),
            block.fillItem(random, attributes)
        )
    }
}

@Serializable
class IfElseInstructionTemplate<LanguageTag : ProgramLanguageTag>(
    val cond: ProgramExpressionTemplate<LanguageTag>,
    val block: ProgramLocalScopeTemplate<LanguageTag>
) : ProgramInstructionTemplate<LanguageTag> {
    val elifBlocks = mutableListOf<IfInstructionTemplate<LanguageTag>>()
    var elseBlock: ProgramLocalScopeTemplate<LanguageTag>? = null
    override fun fillItem(random: Random, attributes: ProgramAttributes): IfElseInstruction<LanguageTag> {
        return IfElseInstruction(
            cond.fillItem(random, attributes),
            block.fillItem(random, attributes)
        ).apply {
            this.elifBlocks.addAll(this@IfElseInstructionTemplate.elifBlocks.map { it.fillItem(random, attributes) })
            this.elseBlock = this@IfElseInstructionTemplate.elseBlock?.fillItem(random, attributes)
        }
    }
}

