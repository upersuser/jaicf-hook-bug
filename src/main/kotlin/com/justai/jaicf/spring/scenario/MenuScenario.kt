package com.justai.jaicf.spring.scenario

import com.justai.jaicf.activator.intent.intent
import com.justai.jaicf.api.routing.routing
import com.justai.jaicf.builder.createModel
import com.justai.jaicf.channel.telegram.TelegramEvent
import com.justai.jaicf.channel.telegram.telegram
import com.justai.jaicf.context.BotContext
import com.justai.jaicf.hook.*
import com.justai.jaicf.model.scenario.Scenario
import com.justai.jaicf.spring.channel.telegram.keyboards.Keyboards
import com.justai.jaicf.spring.scenario.mood.MoodScenario
import org.springframework.stereotype.Component


@Component
class MenuScenario(
    val moodScenario: MoodScenario
) : Scenario {

    override val model = createModel {

        append("mood", moodScenario, propagateHooks = false, exposeHooks = false)

        handle<AnyErrorHook> {
            reactions.say("Сломалось")
        }

        handle<AfterActionHook> {
            reactions.say("HOOK IN MENU")
        }

        state("From_auth", modal = true, noContext = true) {
            activators {
                catchAll()
                event(TelegramEvent.CONTACT)
            }

            action {
                reactions.go("../Main")
            }
        }

        state("Main") {
            activators {
                regex("[Мм]ен[А-Яа-я]")
                regex("[Кк][нН][оО][пП][А-Яа-я]*")
                regex("[Вв][Ыы][Бб][А-Яа-я]*")
                regex("[Дд][Ее][Йй][А-Яа-я]*")
                regex("Перейти в меню")
            }

            action {
                reactions.run {
                    telegram?.say(
                        text = random(
                            "Привет,",
                            "Здравствуйте,",
                            "Хэй,"
                        ) + " ${context.client["firstName"]} ${context.client["lastName"] ?: ""}!\nphoneNumber: ${context.client["phoneNumber"]}, language: ${context.client["language"]}.\n" +
                                "Выберите действие, которое хотите выполнить.",
                        replyMarkup = Keyboards.menuScenarioMain
                    )
                }
            }

            state("to_mood") {
                activators {
                    intent("mood")
                    regex("Настроение")
                }

                action {
                    reactions.go("/mood/moodStart")
                }
            }

            state("Mood_bad") {
                activators {
                    regex("мне грустно")
//                    intent("mood_unhappy").onlyIf { activator.intent!!.confidence >= 0.5 }
                    intent("mood_unhappy")
                }

                action {
                    reactions.say(activator.intent.toString())
                    reactions.go("/mood/Choose_mood/user_mood_bad")
                }
            }

            state("Mood_happy") {
                activators {
                    regex("я счастлив")
                    intent("mood_great")
                }

                action {
                    reactions.go("/mood/Choose_mood/user_mood_happy")
                }
            }

            state("to_language") {
                activators {
                    regex("select language")
                    regex("язык")
                    regex("Выбор языка")
                }
                action {
                    reactions.say("Переход к смене языка...")
                    routing.route("start", "From_menu")
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

class MenuContext(context: BotContext) {
    var userRole: String? by context.client
    var firstUpdateId: String? by context.client
    var lastUpdateId: String? by context.client
}