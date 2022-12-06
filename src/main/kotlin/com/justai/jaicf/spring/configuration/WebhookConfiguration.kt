package com.justai.jaicf.spring.configuration

import com.justai.jaicf.spring.channel.telegram.PatchedTelegramChannel
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import org.slf4j.MDC
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

/**
 * Сервис для привязки вебхука к ngrok. Вызывается при каждом запуске приложения
 * Адрес вебхука необходимо прикреплять в переменную в docker-compose файле либо в перепенных окружения.
 *
 * <code>@PostConstruct</code> аннотация позволяет выполнять функцию при каждой инициализации приложения
 */

@Service
class WebhookConfiguration(
    private val botConfiguration: BotConfiguration,
    private val telegramChannel: PatchedTelegramChannel
) {
    @PostConstruct
    fun useWebhook() {
        val webhookUrl = telegramChannel.bot.getWebhookInfo().first?.body()?.result?.url
        if (webhookUrl == null || webhookUrl != botConfiguration.webhookEndpoint+"/webhook"){
            val botName = telegramChannel.bot.getMe().first?.body()?.result?.username

            runBlocking {
                HttpClient(CIO).post<String>(
                    "https://api.telegram.org/bot${botConfiguration.telegramToken}" +
                            "/setWebhook?url=${botConfiguration.webhookEndpoint}/webhook&" +
                            "max_connections=100&" +
                            "secret_token=$botName&" +
                            "drop_pending_updates=true"
                )
            }
            println("Установил новый вебхук на ${botConfiguration.webhookEndpoint}/webhook!")
        } else {
            println("Вебхук привязан к $webhookUrl")
        }
    }
}