plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "test-wot-physical-adapter"

includeBuild("../wot-digital-adapter-kotlin")
includeBuild("../wot-physical-adapter-kotlin")
