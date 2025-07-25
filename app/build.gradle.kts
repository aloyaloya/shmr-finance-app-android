import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.detekt)
}

android {
    namespace = "com.example.shmr_finance_app_android"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.shmr_finance_app_android"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localProperties = Properties().apply {
            load(rootProject.file("local.properties").inputStream())
        }

        buildConfigField(
            "String",
            "API_TOKEN",
            "\"${localProperties.getProperty("api.token", "")}\""
        )
        buildConfigField(
            "Integer",
            "ACCOUNT_ID",
            localProperties.getProperty("account.id", "1")
        )

        val apiToken = localProperties.getProperty("api.token")
            ?: throw GradleException("API TOKEN не найден")

        buildConfigField(
            "String",
            "BUILD_TIME",
            "\"${System.currentTimeMillis()}\""
        )

        buildConfigField(
            "String",
            "VERSION_NAME",
            "\"${defaultConfig.versionName}\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}

detekt {
    config.from("$rootDir/config/detekt/detekt.yml")
    buildUponDefaultConfig = false;
    autoCorrect = false
}

dependencies {
    implementation(libs.androidx.security.crypto.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.work.runtime.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.dagger)
    ksp(libs.dagger.compiler)
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}