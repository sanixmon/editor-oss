plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android { namespace = "com.sanix.imageeditor.engine.compositor" }
kotlin { jvmToolchain(17) }
dependencies {
    api(project(":engine:renderer"))
    api(project(":engine:texture"))
}
