import io.gitlab.arturbosch.detekt.Detekt

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.dagger.hilt.android") version "2.56.2" apply false
    alias(libs.plugins.detekt) apply false
}

tasks.withType<Detekt>().configureEach {
    reports {
        html.required.set(true)
    }
}