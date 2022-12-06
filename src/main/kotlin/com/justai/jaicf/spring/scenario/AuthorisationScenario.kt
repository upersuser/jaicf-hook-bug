package com.justai.jaicf.spring.scenario


import com.justai.jaicf.api.routing.routing
import com.justai.jaicf.builder.createModel

import com.justai.jaicf.model.scenario.Scenario

import org.springframework.stereotype.Component

@Component
class AuthorisationScenario(
) : Scenario {
    override val model = createModel {

        state("Authorisation") {

            action {
                routing.route("${context.client["language"]}_menu", "From_auth")
            }

            fallback {

            }
        }
    }
}

