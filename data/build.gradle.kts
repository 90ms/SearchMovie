plugins {
    androidLibrary()
    daggerHilt()
    kotlinParcelize()
    kotlinAndroid()
    kotlinKapt()
    ktlint()
    junit5()
}

android {
    namespace = "com.a90ms.data"
    compileSdk = Versions.COMPILE_SDK

    defaultConfig {
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

dependencies {

    implementation(project(":domain"))
    implementation(project(":common"))

    kotlinComponent()
    androidXComponent()
    lifeCycleComponent()
    hiltComponent()
    retrofitComponent()
    okHttpComponent()
    roomComponent()

    implementation(AndroidX.PAGING)
    implementation(AndroidX.PREFERENCE)
    implementation(AndroidX.DATASTORE_PREF)
    implementation(AndroidX.DATASTORE_PREF_CORE)
    implementation(Others.TIMBER)
    implementation(Others.JACKSON)
    implementation(Others.STETHO)
    implementation(Others.GSON)
}

kapt {
    useBuildCache = true
}

ktlint {
    debug.set(true)
    verbose.set(true)
    android.set(true)
    outputToConsole.set(true)
    outputColorName.set("RED")
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
    }
    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}