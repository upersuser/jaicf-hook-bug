package com.justai.jaicf.spring.connection

import com.justai.jaicf.channel.http.HttpBotRequest
import com.justai.jaicf.spring.channel.telegram.PatchedTelegramChannel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
class WebhookEndpoint(
    private val telegramChannel: PatchedTelegramChannel
) {
    @PostMapping("/webhook")
//    @Async
    fun processRequest(req: HttpServletRequest) {

        val request = HttpBotRequest(
            stream = req.inputStream,
            headers = req.headerNames.asSequence().map { it as String to listOf(req.getHeader(it)) }.toMap(),
            parameters = req.parameterMap.map { (k, v) ->
                (k as String) to (v as Array<out String>).toList()
            }.toMap()
        )

        telegramChannel.process(request).let { response ->
            ResponseEntity
                .status(response.statusCode)
                .headers { it.setAll(response.headers) }
                .body(response.output.toString())
        }
    }
}