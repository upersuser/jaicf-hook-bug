val jaicf = "1.2.4"
val logback = "1.2.3"
val ktor = "1.5.1"
val sshj = "0.34.0"
val klaxon = "5.4"
val jupiter = "5.9.1"
val retrofit = "2.9.0"
val okhttp = "3.14.9"//"4.10.0"

object Version {
    // Kotlin
    const val kotlin = "1.4.21"
    const val stdLib = "1.4.21"
    const val reflect = "1.4.0"

    // Libraries
    const val jackson = "2.12.3"
    const val slf4j = "1.7.30"
    const val jUnit = "5.6.0"
    const val jetty = "9.4.3.v20170317"

    const val ktor = "1.5.1"
    const val serializationRuntime = "1.0.1"
    const val coroutinesCore = "1.4.2"
    const val tomcatServletApi = "6.0.53"
    const val logbackGelfAppender = "1.5"
}

group = "com.justai.jaicf"
version = "0.0.1b"

plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
    kotlin("plugin.spring") version "1.7.10"

    id("org.springframework.boot") version "2.7.5"
    id("io.spring.dependency-management") version "1.1.0"
}

repositories {
    mavenCentral()
    maven(uri("https://jitpack.io"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}

dependencies {

    implementation(kotlin("stdlib"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-mail")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("com.just-ai.jaicf:core:$jaicf")
    implementation("com.just-ai.jaicf:mongo:$jaicf")
    implementation("com.just-ai.jaicf:telegram:$jaicf")


    implementation("net.logstash.logback:logstash-logback-encoder:7.2")

    implementation("javax.servlet:javax.servlet-api:4.0.1")

    implementation("io.ktor:ktor-server-netty:$ktor")
    implementation("io.ktor:ktor-client-cio-jvm:$ktor")
    implementation("io.ktor:ktor-client-logging-jvm:$ktor")
    implementation("io.ktor:ktor-client-json-jvm:$ktor")
    implementation("io.ktor:ktor-client-serialization-jvm:$ktor")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "16"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "16"
    }
    bootJar {
        archiveFileName.set("app.jar")
        mainClass.set("com.justai.jaicf.spring.ApplicationKt")
//        mainClass.set("com.justai.jaicf.spring.ProdKt")
    }

    test {
        useJUnitPlatform()
    }
}

tasks.create("stage") {
    dependsOn("bootJar")
}

tasks.create("prod"){
    dependsOn()
}

tasks.withType<org.gradle.jvm.tasks.Jar> {
    duplicatesStrategy = org.gradle.api.file.DuplicatesStrategy.WARN
}
