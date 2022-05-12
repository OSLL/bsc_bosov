package com.github.neckbosov.bsc_bosov.server.dao

import com.github.neckbosov.bsc_bosov.dsl.tags.ProgramLanguageTag
import com.github.neckbosov.bsc_bosov.server.config.ConfigProvider
import com.mongodb.ConnectionString
import io.ktor.http.*
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

typealias MongoDB = CoroutineDatabase

suspend fun createMongoDB(): MongoDB {
    return KMongo.createClient(
        connectionString = ConnectionString(
            URLBuilder(
                protocol = URLProtocol.createOrDefault("mongodb"),
                host = ConfigProvider.dbHost,
                port = ConfigProvider.dbPort,
                user = ConfigProvider.dbUser,
                password = ConfigProvider.dbPassword,
            ).buildString()
        )
    ).coroutine.getDatabase(ConfigProvider.dbName).prepareDatabase()
}

private suspend fun MongoDB.prepareDatabase() = apply {
    val variantsCollection = getCollection<Variants>()
    variantsCollection.ensureUniqueIndex(Variants::parameters)
    val templatesCollection = getCollection<Template<out ProgramLanguageTag>>()
    templatesCollection.ensureUniqueIndex(Template<out ProgramLanguageTag>::taskName)
}