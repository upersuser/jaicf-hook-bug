package com.justai.jaicf.spring.scenario.mood

import com.justai.jaicf.builder.createModel
import com.justai.jaicf.context.BotContext
import com.justai.jaicf.channel.telegram.telegram
import com.justai.jaicf.hook.AfterActionHook
import com.justai.jaicf.model.scenario.Scenario
import com.justai.jaicf.reactions.buttons
import com.justai.jaicf.reactions.toState
import org.springframework.stereotype.Component

@Component
class MoodScenario : Scenario {

    override val model = createModel {

        handle<AfterActionHook> {
            reactions.say("HOOK IN MOOD")
        }

        state("moodStart", noContext = true, modal = true){
            action {
                reactions.telegram?.say(
                    text = "Вы перешли в chooseMood"
                )
                reactions.go("../Choose_mood")
            }
        }

        state("Choose_mood", modal = true) {
            activators {
                regex("Настроение")
            }

            action {
                /*val moodContext = MoodContext(reactions.botContext)
                moodContext.needSendAnimal = null*/
                reactions.sayRandom(
                    "Как дела?",
                    "Как поживаете?",
                    "Как настроение?"
                )

                reactions.buttons(
                    "Я в порядке" toState "user_mood_happy",
                    "Мне грустно" toState "user_mood_bad"
                )
            }

            state("user_mood_bad") {
                activators("/Choose_mood") {
                    intent("mood_unhappy")
                    regex("я грустный")
                    regex("мне грустно")
                    regex("Мне грустно.")
                }

                action {

                            reactions.run {
                                sayRandom(
                                    "О нет!",
                                    "Вы грустите? Не переживайте!"
                                )
                            }
                            reactions.say(
                                "Может ли этот милый котик помочь вам перестать быть грустным?",
                            )


                }

                state("cat") {
                    activators(fromState = "/Choose_mood/user_mood_bad") {
                        regex("Кошка")
                        regex("Хочу кошку")
                    }

                    action {
                        reactions.run {
                            sayRandom(
                                "О нет!",
                                "Вы грустите? Не переживайте!"
                            )
                        }

                        reactions.run {
                            say("Может ли этот забавный котик помочь вам перестать быть грустным?")
                            buttons(
                                "Да" toState "../yes",
                                "Нет" toState "../no"
                            )
                        }
                    }
                }

                state("dog") {
                    activators(fromState = "/Choose_mood/user_mood_bad") {
                        regex("Собака")
                        regex("Хочу собаку")
                    }

                    action {
                        reactions.run {
                            sayRandom(
                                "О нет!",
                                "Вы грустите? Не переживайте!"
                            )                        }
                        reactions.run {
                            say("Может ли этот забавный пёсик помочь вам перестать быть грустным?")
                            buttons(
                                "Да" toState "../yes",
                                "Нет" toState "../no"
                            )
                        }
                    }
                }

                state("yes") {
                    activators {
                        regex("Да")
                        intent("affirm")
                    }

                    action {
                        reactions.go("../../user_mood_happy")
                    }
                }

                state("no") {
                    activators {
//                        regex("Нет")
                        intent("deny")
                    }

                    action {
                        val moodContext = MoodContext(reactions.botContext)
                        when(moodContext.needSendAnimal) {
                            "dog" -> reactions.go("../dog")
                            "cat" -> reactions.go("../cat")
                            else -> reactions.go("../")
                        }
                    }
                }
            }

            state("user_mood_happy") {
                activators {
                    intent("mood_happy")
                    regex("Я в порядке")
                }

                action {
                    reactions.sayRandom(
                        "Отлично! Хорошего дня!",
                        "Это великолепно! Удачи!",
                        "Рад, что у вас все хорошо! Удачи!"
                    )
                    reactions.go("../go_menu")
                }
            }

            state("go_menu") {
                activators {
                    regex("Вернуться в меню")
                }

                action {
                    reactions.go("/Main")
                }
            }

            fallback {
                reactions.say("Не могу понять ${request.input}, мне грустно :(")
            }
        }
    }
}

class MoodContext(context: BotContext) {
    var needSendAnimal: String? by context.client
}
