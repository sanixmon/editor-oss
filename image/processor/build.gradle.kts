plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android { namespace = "com.sanix.imageeditor.image.processor" }

kotlin { jvmToolchain(17) }

dependencies { api(project(":core:model")) }
