object Kotlin {
    const val KOTLIN_STDLIB = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}"
    const val COROUTINES_CORE =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.KOTLINX_COROUTINES}"
    const val COROUTINES_ANDROID =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.KOTLINX_COROUTINES}"
}

object AndroidX {
    const val CORE_KTX = "androidx.core:core-ktx:${Versions.CORE_KTX}"
    const val APP_COMPAT = "androidx.appcompat:appcompat:${Versions.APP_COMPAT}"

    const val ACTIVITY_KTX = "androidx.activity:activity-ktx:${Versions.ACTIVITY_KTX}"
    const val FRAGMENT_KTX = "androidx.fragment:fragment-ktx:${Versions.FRAGMENT_KTX}"

    const val CONSTRAINT_LAYOUT =
        "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}"

    const val LIFECYCLE_VIEWMODEL_KTX =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFECYCLE_KTX}"
    const val LIFECYCLE_LIVEDATA_KTX =
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.LIFECYCLE_KTX}"
    const val PAGING = "androidx.paging:paging-runtime-ktx:${Versions.PAGING}"
    const val PREFERENCE = "androidx.preference:preference-ktx:${Versions.PREFERENCE}"
    const val DATASTORE_PREF = "androidx.datastore:datastore-preferences:${Versions.DATASTORE}"
    const val DATASTORE_PREF_CORE =
        "androidx.datastore:datastore-preferences-core:${Versions.DATASTORE}"

    const val NAVIGATION_FRAGMENT = "androidx.navigation:navigation-fragment:${Versions.NAVIGATION}"
    const val NAVIGATION_UI = "androidx.navigation:navigation-ui:${Versions.NAVIGATION}"
    const val NAVIGATION_FRAGMENT_KTX =
        "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}"
    const val NAVIGATION_UI_KTX = "androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION}"

    const val SWIPE_REFRESH = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.SWIPE}"
}

object Google {
    const val HILT_COMMON = "androidx.hilt:hilt-common:${Versions.HILT_COMMON}"
    const val HILT_ANDROID = "com.google.dagger:hilt-android:${Versions.HILT}"
    const val HILT_ANDROID_COMPILER = "com.google.dagger:hilt-android-compiler:${Versions.HILT}"

    const val MATERIAL = "com.google.android.material:material:${Versions.MATERIAL}"
}

object Network {
    const val RETROFIT = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
    const val RETROFIT_ADAPTER = "com.squareup.retrofit2:adapter-rxjava3:${Versions.RETROFIT}"
    const val RETROFIT_CONVERTER = "com.squareup.retrofit2:converter-jackson:${Versions.RETROFIT}"

    const val OKHTTP = "com.squareup.okhttp3:okhttp:${Versions.OKHTTP}"
    const val OKHTTP_LOGGING_INTERCEPTOR =
        "com.squareup.okhttp3:logging-interceptor:${Versions.OKHTTP}"
}

object Room {
    const val ROOM_RUNTIME = "androidx.room:room-runtime:${Versions.ROOM}"
    const val ROOM_COMPILER = "androidx.room:room-compiler:${Versions.ROOM}"
    const val ROOM_KTX = "androidx.room:room-ktx:${Versions.ROOM}"
    const val ROOM_RXJAVA3 = "androidx.room:room-rxjava3:${Versions.ROOM}"
    const val ROOM_TESTING = "androidx.room:room-testing:${Versions.ROOM}"
}

object Others {
    const val GLIDE = "com.github.bumptech.glide:glide:${Versions.GLIDE}"
    const val GLIDE_COMPILER = "com.github.bumptech.glide:compiler:${Versions.GLIDE}"

    const val TIMBER = "com.jakewharton.timber:timber:${Versions.TIMBER}"

    const val LOTTIE = "com.airbnb.android:lottie:${Versions.LOTTIE}"

    const val JACKSON = "com.fasterxml.jackson.module:jackson-module-kotlin:${Versions.JACKSON}"

    const val STETHO = "com.facebook.stetho:stetho-okhttp3:${Versions.STETHO}"

    const val GSON = "com.google.code.gson:gson:${Versions.GSON}"
}

object UnitTest {
    const val JUNIT = "junit:junit:${Versions.JUNIT}"
}

object AndroidTest {
    const val ANDROID_JUNIT = "androidx.test.ext:junit:${Versions.ANDROID_JUNIT}"
    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO_CORE}"
}

