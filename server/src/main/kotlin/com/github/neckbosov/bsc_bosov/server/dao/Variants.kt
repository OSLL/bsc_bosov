package com.github.neckbosov.bsc_bosov.server.dao

import com.github.neckbosov.bsc_bosov.dsl.program.ProgramAttributes
import com.mongodb.DuplicateKeyException
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import org.litote.kmongo.newId
import java.util.*

@Serializable
data class Variants(
    @Contextual
    @SerialName("_id")
    val id: Id<Variants> = newId(),
    val parameters: ProgramAttributes,
    val image: String,
    val codeText: String,
    val answer: String
)

class VariantsOps(private val database: MongoDB) {
    val collection: CoroutineCollection<Variants>
        get() = database.getCollection("variants")

    suspend fun getImage(parameters: ProgramAttributes) =
        collection.find(Variants::parameters eq parameters).first()?.image?.let {
            Base64.getDecoder().decode(it)
        }


    suspend fun getText(parameters: ProgramAttributes) =
        collection.find(Variants::parameters eq parameters).first()?.codeText

    suspend fun addVariant(parameters: ProgramAttributes, image: ByteArray, codeText: String, answer: String) {
        val imageString = Base64.getEncoder().encodeToString(image)
        try {
            collection.insertOne(
                Variants(
                    parameters = parameters,
                    image = imageString,
                    codeText = codeText,
                    answer = answer
                )
            )
        } catch (e: DuplicateKeyException) {
        }
    }

    suspend fun getAnswer(parameters: ProgramAttributes) =
        collection.find(Variants::parameters eq parameters).first()?.answer
}

