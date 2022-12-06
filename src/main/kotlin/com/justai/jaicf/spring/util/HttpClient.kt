package com.justai.jaicf.spring.util

import io.ktor.client.*
import io.ktor.client.engine.cio.*

val HttpClient = HttpClient(CIO) {
    expectSuccess = true
}
