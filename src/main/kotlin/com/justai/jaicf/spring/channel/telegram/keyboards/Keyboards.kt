package com.justai.jaicf.spring.channel.telegram.keyboards

import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.KeyboardReplyMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import com.github.kotlintelegrambot.entities.keyboard.KeyboardButton

object Keyboards {
    val menuScenarioMain = KeyboardReplyMarkup(
        keyboard = listOf(
            listOf(
                KeyboardButton("Настроение"),
                KeyboardButton("Выбор языка"),

            ),
        ),
        resizeKeyboard = true,
        oneTimeKeyboard = true
    )
}