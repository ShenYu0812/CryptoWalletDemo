plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.ktor)
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
}

group = "com.shenyu.server.netty"
version = "0.0.1"
val isDevelopment: Boolean = true

ktor {
    application {
        mainClass.set("com.shenyu.server.netty.ApplicationKt")
        applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
    }

    dependencies {
        implementation(libs.ktor.server.core)
        implementation(libs.ktor.server.netty)
        implementation(libs.ktor.server.websockets)
        implementation(libs.ktor.server.config.yaml)
        implementation(libs.ktor.network.tls.certificates)
        implementation(libs.ktor.server.content.negotiation.jvm)
    }

}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}
