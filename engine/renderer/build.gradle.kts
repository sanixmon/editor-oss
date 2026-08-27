plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android { namespace = "com.sanix.imageeditor.engine.renderer" }
kotlin { jvmToolchain(17) }
dependencies {
    api(project(":core:model"))
    api(project(":engine:texture"))
    testImplementation(kotlin("test"))
}
