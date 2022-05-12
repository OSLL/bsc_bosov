package com.github.neckbosov.bsc_bosov.server.dao

import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import com.github.neckbosov.bsc_bosov.dsl.template.ProgramTemplate
import com.mongodb.DuplicateKeyException
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import org.litote.kmongo.newId

@Serializable
data class Template<LanguageTag : ProgramLanguageTag>(
    @Contextual
    @SerialName("_id")
    val id: Id<Variants> = newId(),
    val taskName: String,
    val program: ProgramTemplate<LanguageTag>,
    val tag: ProgramLanguageTag,
    val text: String = ""
)

class TemplateOps(private val database: MongoDB) {
    val collection: CoroutineCollection<Template<out ProgramLanguageTag>>
        get() = database.getCollection("templates")

    suspend fun getTemplateAndTag(taskName: String): Pair<ProgramTemplate<out ProgramLanguageTag>, ProgramLanguageTag>? {
        val record = collection.find(Template<ProgramLanguageTag>::taskName eq taskName).first() ?: return null
        return Pair(record.program, record.tag)
    }

    suspend fun <LanguageTag : ProgramLanguageTag> addTemplate(
        taskName: String,
        programTemplate: ProgramTemplate<LanguageTag>,
        tag: ProgramLanguageTag,
        text: String = ""
    ) {
        try {
            collection.insertOne(
                Template(
                    taskName = taskName,
                    program = programTemplate,
                    tag = tag,
                    text = text
                )
            )
        } catch (e: DuplicateKeyException) {

        }

    }

    suspend fun deleteTemplate(taskName: String) {
        collection.deleteOne(Template<ProgramLanguageTag>::taskName eq taskName)
    }

    suspend fun getAllTaskNames() = collection.find().toList().map { it.taskName }
}

val MongoDB.templates
    get() = TemplateOps(this)