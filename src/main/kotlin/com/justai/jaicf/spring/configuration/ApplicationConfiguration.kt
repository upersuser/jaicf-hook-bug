package com.justai.jaicf.spring.configuration

import com.justai.jaicf.BotEngine
import com.justai.jaicf.activator.catchall.CatchAllActivator

import com.justai.jaicf.activator.regex.RegexActivator
import com.justai.jaicf.api.routing.BotRoutingEngine
import com.justai.jaicf.logging.Slf4jConversationLogger
import com.justai.jaicf.spring.channel.telegram.PatchedTelegramChannel

import com.justai.jaicf.spring.scenario.StartScenario
import com.justai.jaicf.spring.scenario.MenuScenario
import org.springframework.beans.factory.annotation.Qualifier

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationConfiguration(
    private val botConfiguration: BotConfiguration,
    private val mongoConfiguration: MongoConfiguration
) {
    @Bean
    @Qualifier("menuRu")
    fun menuEngineRu(menuScenario: MenuScenario) = BotEngine(
        scenario = menuScenario,
        defaultContextManager = mongoConfiguration.mongoDatabaseFactory,
        activators = arrayOf(
            RegexActivator,
            CatchAllActivator
        ),
        conversationLoggers = arrayOf(
            Slf4jConversationLogger()
        )
    )

    @Bean
    @Qualifier("menuEn")
    fun menuEngineEn(menuScenario: MenuScenario) = BotEngine(
        scenario = menuScenario,
        defaultContextManager = mongoConfiguration.mongoDatabaseFactory,
        activators = arrayOf(
            RegexActivator,
            CatchAllActivator
        ),
        conversationLoggers = arrayOf(
            Slf4jConversationLogger()
        )
    )

    @Bean
    @Qualifier("start")
    fun startEngine(startScenario: StartScenario) = BotEngine(
        scenario = startScenario,
        defaultContextManager = mongoConfiguration.mongoDatabaseFactory,
        activators = arrayOf(
            RegexActivator,
            CatchAllActivator
        ),
        conversationLoggers = arrayOf(
            Slf4jConversationLogger()
        )
    )

    @Bean
    fun patchedTelegramChannel(@Qualifier("mutlilang") botRoutingEngine: BotRoutingEngine) = PatchedTelegramChannel(
        botApi = botRoutingEngine,
        telegramBotToken = botConfiguration.telegramToken
    )

    @Bean
    @Qualifier("mutlilang")
    fun multilingualBotEngine(
        @Qualifier("start") startEngine: BotEngine,
        @Qualifier("menuRu") menuRuEngine: BotEngine,
        @Qualifier("menuEn") menuEnEngine: BotEngine,
    ) = BotRoutingEngine(
        main = "start" to startEngine,
        routables = mapOf(
            "ru_menu" to menuRuEngine,
            "en_menu" to menuEnEngine
        )
    )
}