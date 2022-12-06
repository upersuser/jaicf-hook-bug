package com.justai.jaicf.spring.scenario

import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.KeyboardReplyMarkup
import com.github.kotlintelegrambot.entities.keyboard.KeyboardButton
import com.justai.jaicf.builder.createModel
import com.justai.jaicf.channel.telegram.telegram
import com.justai.jaicf.hook.AnyErrorHook
import com.justai.jaicf.logging.currentState
import com.justai.jaicf.model.scenario.Scenario
import org.springframework.stereotype.Component

@Component
class StartScenario(
    val authorisationScenario: AuthorisationScenario
): Scenario {
    override val model = createModel {

        append(authorisationScenario)

        handle<AnyErrorHook> {
            reactions.say("Произошла ошибка в state =\n${reactions.telegram?.currentState}")
        }

        state("From_menu", modal = true, noContext = true){
            activators {
                catchAll()
            }
            action {
                reactions.go("../Start")
            }
        }

        state("Start") {
            globalActivators {
                regex("/start")

            }
            activators {
                intent("greet")
                //intent("greet")
                regex("/start")

            }

            action {
                val userId = request.telegram?.message?.chat?.id

                reactions.telegram?.api?.sendMessage(
                    chatId = ChatId.fromId(context.clientId.toLong()),
                    text = "Выберите язык",
                    replyMarkup = KeyboardReplyMarkup(
                        keyboard = listOf(
                            listOf(
                                KeyboardButton("Ru"),
                                KeyboardButton("En")
                            )
                        ),
                        resizeKeyboard=true
                    ),
                )
            }

            state("Ru") {
                activators {
                    regex("Ru")
                    regex(".* Русский")
                }

                action {
                    val firstName = request.telegram?.message?.chat?.firstName
                    val lastName = request.telegram?.message?.chat?.lastName

                    context.client["firstName"] = firstName
                    context.client["lastName"] = lastName ?: ""

                    context.client["language"] = "ru"
                    reactions.say("Вы выбрали русский язык!")
                    reactions.go("/Authorisation")
                }
            }

            state("En") {
                activators {
                    regex("En")
                    regex(".* English")
                }

                action {
                    val firstName = request.telegram?.message?.chat?.firstName
                    val lastName = request.telegram?.message?.chat?.lastName

                    context.client["firstName"] = firstName
                    context.client["lastName"] = lastName ?: ""

                    context.client["language"] = "en"
                    reactions.say("You are selected english!")
                    reactions.go("/Authorisation")
//                    routing.route("auth", targetState = "/Authorisation")
                }
            }
            fallback {
                reactions.say(
                    "\"${reactions.telegram?.request?.input}\" звучит как что-то новое для меня, " +
                            "попробуйте повторить ввод иначе."
                )
            }
        }
    }
}