package com.justai.jaicf.spring.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.stereotype.Service

@ConstructorBinding
@ConfigurationProperties(prefix = "bot")
data class BotConfiguration(
    var mongoCollection: String,
    var telegramToken: String = "",
    var authorisationRasaToken: String = "",
//    var telegramBotId: String,
    var rasaHostRu: String = "",
    var rasaHostEn: String = "",
    var webhookEndpoint: String = "",
    var banActivated: Boolean = true
)
