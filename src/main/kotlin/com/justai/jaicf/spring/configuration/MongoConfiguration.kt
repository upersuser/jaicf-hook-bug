package com.justai.jaicf.spring.configuration

import com.justai.jaicf.context.manager.mongo.MongoBotContextManager
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory

@Configuration
class MongoConfiguration(
    private val mongoDatabaseFactoryInstance: MongoDatabaseFactory,
    private val botConfiguration: BotConfiguration
) {
    val mongoDatabaseFactory = mongoDatabaseFactoryInstance.createContextManager(botConfiguration.mongoCollection)

    companion object {
        private fun MongoDatabaseFactory.createContextManager(collection: String) =
            MongoBotContextManager(mongoDatabase.getCollection(collection))
    }
}