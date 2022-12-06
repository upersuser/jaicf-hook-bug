package com.justai.jaicf.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties
@ConfigurationPropertiesScan("com.justai.jaicf.spring.configuration")
class Application
fun main(args: Array<String>) {
    System.setProperty("spring.config.location",
//        "classpath:/application-dev.yml,classpath:/dev.application-dev.yml,optional:classpath:/local.application-dev.yml")
        "classpath:/application-dev.yml")
    runApplication<Application>(*args)
}