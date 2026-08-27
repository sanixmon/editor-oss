plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android { namespace = "com.sanix.imageeditor.data.media" }

kotlin { jvmToolchain(17) }

dependencies {
    api(project(":image:decoder"))
    api(project(":image:exporter"))
}
