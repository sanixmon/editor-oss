plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android { namespace = "com.sanix.imageeditor.image.decoder" }

kotlin { jvmToolchain(17) }

dependencies { implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0") }
