import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    application

    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlin.jvm)
//    alias(libs.plugins.kotlin.qa)
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
}


repositories {
    mavenCentral()
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    mavenLocal()
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.wot)
    implementation(libs.kotlin.wot.http.binding)
    implementation(libs.kotlin.wot.mqtt.binding)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.jdk8)

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("it.unibo:wot-physical-adapter:unspecified")
    implementation("it.unibo:wot-digital-adapter:unspecified")
    api(libs.wldt)

    testImplementation(libs.bundles.kotlin.testing)
}

application {
    mainClass.set("MainKt")
}

tasks.named<KotlinCompilationTask<*>>("compileKotlin").configure {
    compilerOptions {
        allWarningsAsErrors = true
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
        showCauses = true
        showStackTraces = true
        events(
            *org.gradle.api.tasks.testing.logging.TestLogEvent
                .values(),
        )
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

kotlin {
    jvmToolchain(21)
}

