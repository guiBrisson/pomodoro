import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "me.brisson"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)

    // Coroutines for swing
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.7.3")

    implementation("org.slf4j:slf4j-api:1.7.5")

    // FlatLaf - Look and Feel
    implementation("com.formdev:flatlaf:3.2")

    // Exposed - Local Storage
    implementation("org.jetbrains.exposed:exposed-core:0.41.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1")

    // SQLite
    implementation(dependencyNotation = "com.h2database:h2:2.1.214")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "pomodoro"
            packageVersion = "1.0.0"
        }
    }
}
