import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.kotlinComponent() {
    implementation(Kotlin.KOTLIN_STDLIB)
    implementation(Kotlin.COROUTINES_CORE)
    implementation(Kotlin.COROUTINES_ANDROID)
}

fun DependencyHandlerScope.androidXComponent() {
    implementation(AndroidX.CORE_KTX)
    implementation(AndroidX.APP_COMPAT)
    implementation(AndroidX.ACTIVITY_KTX)
    implementation(AndroidX.FRAGMENT_KTX)
    implementation(AndroidX.CONSTRAINT_LAYOUT)
    implementation(AndroidX.NAVIGATION_FRAGMENT)
    implementation(AndroidX.NAVIGATION_UI)
    implementation(AndroidX.NAVIGATION_FRAGMENT_KTX)
    implementation(AndroidX.NAVIGATION_UI_KTX)
    implementation(AndroidX.SWIPE_REFRESH)
}

fun DependencyHandlerScope.lifeCycleComponent() {
    implementation(AndroidX.LIFECYCLE_VIEWMODEL_KTX)
    implementation(AndroidX.LIFECYCLE_LIVEDATA_KTX)
}

fun DependencyHandlerScope.hiltComponent() {
    implementation(Google.HILT_COMMON)
    implementation(Google.HILT_ANDROID)
    kapt(Google.HILT_ANDROID_COMPILER)
}

fun DependencyHandlerScope.retrofitComponent() {
    implementation(Network.RETROFIT)
    implementation(Network.RETROFIT_ADAPTER)
    implementation(Network.RETROFIT_CONVERTER)
}

fun DependencyHandlerScope.okHttpComponent() {
    implementation(Network.OKHTTP)
    implementation(Network.OKHTTP_LOGGING_INTERCEPTOR)
}

fun DependencyHandlerScope.roomComponent() {
    implementation(Room.ROOM_RUNTIME)
    implementation(Room.ROOM_KTX)
    implementation(Room.ROOM_RXJAVA3)
    kapt(Room.ROOM_COMPILER)
    testImplementation(Room.ROOM_TESTING)
}

fun DependencyHandlerScope.testComponent() {
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.1")
    testImplementation("org.mockito:mockito-core:3.3.3")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("androidx.test:core-ktx:1.4.0")
    testImplementation("androidx.test.ext:junit-ktx:1.1.3")
    testImplementation("org.robolectric:robolectric:4.3.1")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.3")

    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}

private fun DependencyHandler.implementation(depName: String) =
    add("implementation", depName)

private fun DependencyHandler.kapt(depName: String) =
    add("kapt", depName)

private fun DependencyHandler.testImplementation(depName: String) =
    add("testImplementation", depName)

private fun DependencyHandler.androidTestImplementation(depName: String) =
    add("androidTestImplementation", depName)

private fun DependencyHandler.annotationProcessor(depName: String) =
    add("annotationProcessor", depName)


