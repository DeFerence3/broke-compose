import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android.gradle.plugin)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.androidx.room)
}

android {
    namespace = "com.diffy.broke"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.diffy.broke"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "0.0.1"

//        multiDexEnabled true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

//        ksp {
//            arg("room.schemaLocation","$projectDir/schemas")
//        }
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }

    sourceSets {
        getByName("androidTest").assets.srcDirs("$projectDir/schemas")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            applicationIdSuffix = ".dev"
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/DEPENDENCIES"
            excludes += "/META-INF/INDEX.LIST"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin{
        compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    kotlin.compilerOptions.optIn.add("kotlin.time.ExperimentalTime")
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(platform(libs.kotlin.bom))
    implementation(platform(libs.compose.bom))
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ui.test.junit4)
    androidTestImplementation(platform(libs.compose.bom))
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
    testImplementation(libs.junit.junit)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.core.ktx)

    //room-db
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    //Dagger - Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    //room-backup
//    implementation(libs.roomdatabasebackup)

    //extended-icons
    implementation(libs.material.icons.extended)

    //navigation
    implementation(libs.navigation.compose)

    //Kotlinx-serialization
    implementation(libs.kotlinx.serialization.json)

    //preferences-datastore
    implementation(libs.datastore.preferences)

    //MMKV
    implementation(libs.mmkv)

    //leak-canary
    debugImplementation(libs.leakcanary.android)

    implementation(libs.opencsv)

    //Androidx Security
    implementation(libs.security.crypto)

    //Google Guava
    implementation(libs.guava)

    //Material Design Implementation
    implementation(libs.material)

    //Apache commons io
    implementation(libs.commons.io)

    //google-drive-backup
    implementation(libs.play.services.drive)
    implementation(libs.play.services.auth)
    implementation(libs.google.auth.library.oauth2.http)
    implementation(libs.google.api.services.drive)
    implementation(libs.google.api.client.android)
    implementation(libs.google.api.client)

}