plugins {
    androidApp()
    daggerHilt()
    kotlinParcelize()
    kotlinAndroid()
    kotlinKapt()
    ktlint()
    junit5()
}

android {
    defaultConfig {
        applicationId = Versions.PACKAGE_NAME
        compileSdk = Versions.COMPILE_SDK
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK
        versionCode = System.getenv("run_number")?.toInt() ?: 1
        versionName = "${Versions.major}.${Versions.minor}.${Versions.patch}"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName(BuildType.DEBUG) {
            isTestCoverageEnabled = false
            isMinifyEnabled = BuildTypeDebug.isMinifyEnabled
            proguardFiles(getDefaultProguardFile("proguard-android.txt"))
            proguardFiles(file("${rootProject.rootDir.absolutePath}/proguard-rules.pro"))
        }

        getByName(BuildType.RELEASE) {
            isMinifyEnabled = BuildTypeRelease.isMinifyEnabled
            proguardFiles(getDefaultProguardFile("proguard-android.txt"))
            proguardFiles(file("${rootProject.rootDir.absolutePath}/proguard-rules.pro"))
        }
    }
    viewBinding {
        isEnabled = true
    }
    dataBinding {
        isEnabled = true
    }

    configurations {
        all {
            exclude(group = "javax.annotation", module = "javax.annotation-api")
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

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(project(":common"))
    implementation(project(":domain"))
    implementation(project(":data"))

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
    implementation(Google.MATERIAL)
    implementation(Others.GLIDE)
    annotationProcessor(Others.GLIDE_COMPILER)
    implementation(Others.LOTTIE)
    implementation(Others.TIMBER)
    implementation(Others.JACKSON)
    implementation(Others.STETHO)
    implementation(Others.GSON)

    // test
    testComponent()

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
