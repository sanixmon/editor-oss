plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android { namespace = "com.sanix.imageeditor.rendering.opengl" }

kotlin { jvmToolchain(17) }

dependencies { api(project(":rendering:renderer")) }
