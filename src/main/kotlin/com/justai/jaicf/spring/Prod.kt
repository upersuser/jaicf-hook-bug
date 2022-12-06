package com.justai.jaicf.spring

import org.springframework.boot.runApplication

fun main(args: Array<String>) {
    System.setProperty("spring.config.location",
        "classpath:/application-dev.yml")
    runApplication<Application>(*args)
}