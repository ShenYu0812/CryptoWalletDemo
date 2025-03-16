plugins {
    application
    alias(libs.plugins.jetbrains.kotlin.jvm)
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
    alias(libs.plugins.ktor)
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
        implementation(libs.log4j.core)
        implementation(libs.log4j.api)
        implementation(libs.logback.classic)
        implementation(libs.ktor.server.websockets)
        implementation(libs.ktor.network.tls.certificates)
        implementation(libs.ktor.server.content.negotiation.jvm)
        implementation(libs.ktor.serialization.gson)
        implementation(libs.bouncycastle.bcprov.jdk18on)
        implementation(libs.bouncycastle.bcpkix.jdk18on)
        implementation(libs.bcutil.jdk18on)
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

tasks.withType<ProcessResources> {
    // 设置重复策略
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    // 确保资源文件被复制到输出目录
    from("src/main/resources") {
        include("ssl/**")
        include("data/**")
        include("logback.xml")
    }
}

tasks.withType<Jar> {
    // 设置重复策略
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    // 确保资源文件被打包到jar中
    from("src/main/resources") {
        include("ssl/**")
        include("data/**")
        include("logback.xml")
    }
}